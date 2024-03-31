package com.project.file.search.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class LogService {

    private static final Logger logger = LoggerFactory.getLogger(LogService.class);

    @Value("${aws.bucket.name}")
    private String bucketName;

    public List<String> searchLogs(String searchKeyword, LocalDateTime from, LocalDateTime to) {
        List<String> matchingLines = new ArrayList<>();
        S3Client s3Client = S3Client.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();

        try {
            List<LocalDateTime> dateRange = getDateRange(from, to);
            for (LocalDateTime date : dateRange) {
                String folderName = date.toString().substring(0, 10);
                ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                        .bucket(bucketName)
                        .prefix(folderName + "/")
                        .build();
                ListObjectsV2Response listResponse = s3Client.listObjectsV2(listRequest);

                for (S3Object s3Object : listResponse.contents()) {
                    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                            .bucket(bucketName).key(s3Object.key()).build();

                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(s3Client.getObject(getObjectRequest)));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.contains(searchKeyword)) {
                            matchingLines.add(line);
                        }
                    }
                }
            }
        } catch (S3Exception | IOException e) {
            logger.error("Error occurred while searching logs: {}", e.getMessage());
            throw new RuntimeException("Error occurred while searching logs", e);
        }
        return matchingLines;
    }

    private List<LocalDateTime> getDateRange(LocalDateTime fromDate, LocalDateTime toDate) {
        List<LocalDateTime> dateRange = new ArrayList<>();
        LocalDateTime currentDate = fromDate;
        while (!currentDate.isAfter(toDate)) {
            dateRange.add(currentDate);
            currentDate = currentDate.plusDays(1);
        }
        return dateRange;
    }
}
