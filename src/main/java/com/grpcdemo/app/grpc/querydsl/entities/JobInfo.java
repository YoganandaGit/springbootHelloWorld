package com.grpcdemo.app.grpc.querydsl.entities;

import com.grpcdemo.app.grpc.querydsl.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "JobInfo")
@Getter
@Setter
public class JobInfo extends BaseEntity {
    private String jobId;
    private String jobRef;
    private String otherRef1;
    private String otherRef2;
    private String jobExternalRef;
}
