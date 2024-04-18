package com.grpcdemo.app.grpc.service;

import com.grpcdemo.app.grpc.querydsl.entities.JobInfo;
import com.grpcdemo.app.grpc.querydsl.entities.QJobInfo;
import com.grpcdemo.app.grpc.querydsl.repository.JobInfoRepository;
import com.querydsl.core.BooleanBuilder;
import grpcdemo.proto.CommonProto;
import grpcdemo.proto.JobQueueMessageProto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ExternalApiService {

    private final JobInfoRepository jobInfoRepository;

    public ExternalApiService(JobInfoRepository jobInfoRepository) {
        this.jobInfoRepository = jobInfoRepository;
    }

    public JobQueueMessageProto.JobInfoResponse getJobInfo(String jobId) {
        //Get all the JobInfos and print title
        JobInfo jobInfo = saveJobInfo("11");
        updateJobInfo("11");
        JobInfo jobInfoByJobId = getJobInfoByJobId("11");
        //Job queue response.
        if (jobInfoByJobId == null) {
            return JobQueueMessageProto.JobInfoResponse.newBuilder().setStatus(CommonProto.Status.newBuilder().setSuccess(false).setErrorCode(HttpStatus.NOT_FOUND.value()).setError("JobInfo not found in the DB.").build()).build();
        }
        JobQueueMessageProto.JobInfoResponse.Builder jobInfoResponseBuilder = JobQueueMessageProto.JobInfoResponse.newBuilder();
        jobInfoResponseBuilder.setJobId(jobId).setJobRef(jobInfoByJobId.getJobRef()).setJobExternalRef(jobInfo.getJobExternalRef()).setOtherRef1(jobInfo.getOtherRef1()).setOtherRef2(jobInfo.getOtherRef2()).build();
        deleteJobInfo(NumberUtils.createLong(String.valueOf(jobInfoByJobId.getId())));
        return jobInfoResponseBuilder.build();
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
        BooleanBuilder where = new BooleanBuilder();
        QJobInfo qJobInfo = QJobInfo.jobInfo;
        where.and(qJobInfo.jobId.eq(jobId));
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
