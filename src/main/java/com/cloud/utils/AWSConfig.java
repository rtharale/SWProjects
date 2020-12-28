package com.cloud.utils;

import com.amazonaws.services.ec2.AmazonEC2Async;
import com.amazonaws.services.ec2.AmazonEC2AsyncClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AWSConfig {

    @Bean
    public AmazonSQSAsync getSQS() {
        return AmazonSQSAsyncClientBuilder.standard().withRegion(GlobalConstants.REGION).build();
    }

    @Bean
    public AmazonS3 getS3() {
        final AmazonS3ClientBuilder s3ClientBuilder = AmazonS3ClientBuilder.standard().withRegion(GlobalConstants.REGION);
        return s3ClientBuilder.build();
    }

    @Bean
    public AmazonEC2Async getEC2() {
        return AmazonEC2AsyncClientBuilder.standard().withRegion(GlobalConstants.REGION).build();
    }
}
