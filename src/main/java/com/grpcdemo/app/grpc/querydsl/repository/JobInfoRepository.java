package com.grpcdemo.app.grpc.querydsl.repository;

import com.grpcdemo.app.grpc.querydsl.entities.JobInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JobInfoRepository extends JpaRepository<JobInfo, Long>, QuerydslPredicateExecutor<JobInfo> {
	// Custom methods can be added here
}
