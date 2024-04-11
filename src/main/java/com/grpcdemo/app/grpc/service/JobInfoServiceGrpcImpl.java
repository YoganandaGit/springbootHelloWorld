package com.grpcdemo.app.grpc.service;

import grpcdemo.proto.CommonProto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import grpcdemo.proto.JobInfoProto;
import grpcdemo.proto.JobInfoServiceGrpc;
import grpcdemo.proto.JobQueueMessageProto;

@Slf4j
@Component
public class JobInfoServiceGrpcImpl extends JobInfoServiceGrpc.JobInfoServiceImplBase {

    @Autowired
    private ExternalApiService externalApiService;

    @Override
    public void getJobInfo(JobInfoProto.JobInfoRequest request, io.grpc.stub.StreamObserver<JobQueueMessageProto.JobInfoResponse> responseObserver) {
        try {
            responseObserver.onNext(externalApiService.getJobInfo(request.getJobId()));
            responseObserver.onCompleted();
        } catch (Throwable th) {
            log.error("Error in getDatasetAssignments: " + ExceptionUtils.getMessage(th), th);
            responseObserver.onNext(JobQueueMessageProto.JobInfoResponse.newBuilder().setStatus(CommonProto.Status.newBuilder().setSuccess(false).setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).setError(ExceptionUtils.getMessage(th)).build()).build());
            responseObserver.onCompleted();
        }
    }
}
