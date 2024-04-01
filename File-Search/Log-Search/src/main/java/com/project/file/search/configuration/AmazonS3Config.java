package com.project.file.search.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AmazonS3Config {

    @Value("${aws.bucket.region}")
    private String bucketRegion;

    @Bean
    public S3Client amazonS3Client() {
        return S3Client.builder()
                .region(Region.of(bucketRegion))// Default to a region, this will be overridden by the environment variable if set
                .build();
    }
}
