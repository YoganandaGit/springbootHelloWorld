package com.grpcdemo.app.grpc.service;

import com.grpcdemo.app.grpc.querydsl.entities.JobInfo;
import com.grpcdemo.app.grpc.querydsl.entities.QJobInfo;
import com.grpcdemo.app.grpc.querydsl.repository.JobInfoRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import grpcdemo.proto.JobQueueMessageProto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ExternalApiService {

    private final JobInfoRepository jobInfoRepository;

    public ExternalApiService(JobInfoRepository jobInfoRepository) {
        this.jobInfoRepository = jobInfoRepository;
    }

    public JobQueueMessageProto.JobInfoResponse getJobInfo(String jobId) {
        //Save job information
        JobInfo jobInfo = saveJobInfo(jobId, "JobRef_" + jobId, "OtherRef_" + jobId, "OtherRef2_" + jobId, "JobExternalRef_" + jobId);
        JobInfo jobInfoByJobId = IterableUtils.first(getJobInfos());
        JobQueueMessageProto.JobInfoResponse.Builder jobInfoResponseBuilder = JobQueueMessageProto.JobInfoResponse.newBuilder();
        jobInfoResponseBuilder.setJobId(jobId).setJobRef(jobInfoByJobId.getJobRef()).setJobExternalRef(jobInfo.getJobExternalRef()).setOtherRef1(jobInfo.getOtherRef1()).setOtherRef2(jobInfo.getOtherRef2()).build();
        return jobInfoResponseBuilder.build();
    }

    public JobQueueMessageProto.JobInfoResponses getJobInfoList() {
        //Print all job information
        IterableUtils.emptyIfNull(getJobInfos()).forEach(jobInfo1 -> log.info("JobInfo: " + jobInfo1));
        //Retrieve all job information and return
        JobQueueMessageProto.JobInfoResponses.Builder jobInfoResponsesBuilder = JobQueueMessageProto.JobInfoResponses.newBuilder();
        IterableUtils.emptyIfNull(getJobInfos()).forEach(jobInfo -> {
            JobQueueMessageProto.JobInfoResponse.Builder jobInfoResponseBuilder = JobQueueMessageProto.JobInfoResponse.newBuilder();
            jobInfoResponseBuilder.setJobId(jobInfo.getJobId()).setJobRef(jobInfo.getJobRef()).setJobExternalRef(jobInfo.getJobExternalRef()).setOtherRef1(jobInfo.getOtherRef1()).setOtherRef2(jobInfo.getOtherRef2()).build();
            jobInfoResponsesBuilder.addItems(jobInfoResponseBuilder.build());
        });
        return jobInfoResponsesBuilder.build();
    }

    public JobInfo saveJobInfo(String jobId, String jobRef, String otherRef1, String otherRef2, String jobExternalRef) {
        JobInfo jobInfo = new JobInfo(jobId, jobRef, otherRef1, otherRef2, jobExternalRef);
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
