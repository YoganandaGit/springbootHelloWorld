package com.grpcdemo.app.grpc.service;

import grpcdemo.proto.JobQueueMessageProto;
import org.springframework.stereotype.Service;

@Service
public class ExternalApiService {

    public JobQueueMessageProto.JobInfoResponse getJobInfo(String jobId) {
        JobQueueMessageProto.JobInfoResponse.Builder jobInfoResponseBuilder = JobQueueMessageProto.JobInfoResponse.newBuilder();
        jobInfoResponseBuilder.setJobId(jobId).setJobRef("2345").setJobExternalRef("ExtRef").setOtherRef1("Ref1").setOtherRef2("Ref2").build();
        return jobInfoResponseBuilder.build();
    }
}
