package com.grpcdemo.app.grpc.service;

import com.google.common.collect.Lists;
import com.grpcdemo.app.grpc.querydsl.entities.JobInfo;
import com.grpcdemo.app.grpc.querydsl.entities.QJobInfo;
import com.grpcdemo.app.grpc.querydsl.repository.JobInfoRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import grpcdemo.proto.JobQueueMessageProto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ExternalApiService {

    private final JobInfoRepository jobInfoRepository;

    public ExternalApiService(JobInfoRepository jobInfoRepository) {
        this.jobInfoRepository = jobInfoRepository;
    }

    public JobQueueMessageProto.JobInfoResponse getJobInfo(String jobId) {
        //Get all the JobInfos and print title
        JobInfo jobInfo = saveJobInfo(jobId);
        //Print all job information
        IterableUtils.emptyIfNull(getJobInfos()).forEach(jobInfo1 -> log.info("JobInfo: " + jobInfo1));
        JobInfo jobInfoByJobId = IterableUtils.first(getJobInfos());
        JobQueueMessageProto.JobInfoResponse.Builder jobInfoResponseBuilder = JobQueueMessageProto.JobInfoResponse.newBuilder();
        jobInfoResponseBuilder.setJobId(jobId).setJobRef(jobInfoByJobId.getJobRef()).setJobExternalRef(jobInfo.getJobExternalRef()).setOtherRef1(jobInfo.getOtherRef1()).setOtherRef2(jobInfo.getOtherRef2()).build();
        deleteJobInfo(NumberUtils.createLong(String.valueOf(jobInfoByJobId.getId())));
        return jobInfoResponseBuilder.build();
    }

    public JobQueueMessageProto.JobInfoResponses getJobInfoList() {
        JobQueueMessageProto.JobInfoResponses.Builder jobInfoResponsesBuilder = JobQueueMessageProto.JobInfoResponses.newBuilder();
        IterableUtils.emptyIfNull(getJobInfos()).forEach(jobInfo -> {
            JobQueueMessageProto.JobInfoResponse.Builder jobInfoResponseBuilder = JobQueueMessageProto.JobInfoResponse.newBuilder();
            jobInfoResponseBuilder.setJobId(jobInfo.getJobId()).setJobRef(jobInfo.getJobRef()).setJobExternalRef(jobInfo.getJobExternalRef()).setOtherRef1(jobInfo.getOtherRef1()).setOtherRef2(jobInfo.getOtherRef2()).build();
            jobInfoResponsesBuilder.addItems(jobInfoResponseBuilder.build());
        });
        return jobInfoResponsesBuilder.build();
    }

    public JobInfo saveJobInfo(String jobId) {
        JobInfo jobInfo = new JobInfo();
        //Set the fields
        jobInfo.setJobId(jobId);
        jobInfo.setJobRef("8888");
        jobInfo.setOtherRef1("9999");
        jobInfo.setOtherRef2("1010");
        jobInfo.setJobExternalRef("ExtRef1111");
        jobInfoRepository.save(jobInfo);
        log.info("JobInfo saved successfully");
        return jobInfo;
    }

    public void updateJobInfo(String jobId) {
        BooleanBuilder where = new BooleanBuilder();
        QJobInfo qJobInfo = QJobInfo.jobInfo;
        where.and(qJobInfo.jobId.eq(jobId));
        IterableUtils.emptyIfNull(jobInfoRepository.findAll(where)).forEach(jobInfo -> {
            jobInfo.setJobRef("UpdatedRef");
            jobInfoRepository.save(jobInfo);
        });
        log.info("JobInfo updated successfully");
    }

    public JobInfo getJobInfoByJobId(String jobId) {
        QJobInfo qJobInfo = QJobInfo.jobInfo;
        BooleanExpression where = qJobInfo.jobId.eq(jobId);
        JobInfo jobInfo = IterableUtils.first(jobInfoRepository.findAll(where));
        log.info("JobInfo fetched successfully");
        return jobInfo;
    }

    public void deleteJobInfo(Long id) {
        jobInfoRepository.deleteById(id);
        log.info("JobInfo deleted successfully");
    }

    public Iterable<JobInfo> getJobInfos() {
        return jobInfoRepository.findAll();
    }
}
