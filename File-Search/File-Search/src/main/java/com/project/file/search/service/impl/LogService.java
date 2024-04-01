package com.project.file.search.service.impl;

import com.project.file.search.exception.SearchException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class LogService {
    @Value("${aws.bucket.name}")
    private String bucketName;

    @Autowired
    private S3Client s3Client;

    public List<String> searchLogs(String searchKeyword, LocalDateTime from, LocalDateTime to) throws SearchException {

        List<String> matchingLines = new ArrayList<>();
        if(searchKeyword.isEmpty()){
            return matchingLines;
        }
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
            log.error("Error occurred while searching logs with search-keyword: {}",
                    searchKeyword,
                    e);
            throw new SearchException("Error occurred while searching logs", HttpStatus.INTERNAL_SERVER_ERROR);
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
