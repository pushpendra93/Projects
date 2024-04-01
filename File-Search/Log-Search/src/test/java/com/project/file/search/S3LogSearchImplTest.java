package com.project.file.search;

import com.project.file.search.exception.SearchException;
import com.project.file.search.service.impl.S3LogSearchImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class S3LogSearchImplTest {

    @InjectMocks
    private S3LogSearchImpl logService;
    @Mock
    private S3Client s3Client;


    @Test
    public void testSearchLogs() throws SearchException {
        // Mock list response
        ListObjectsV2Response listResponse = ListObjectsV2Response.builder()
                .contents(S3Object.builder().key("2022-01-01/00.txt").build())
                .build();
        when(s3Client.listObjectsV2(any(ListObjectsV2Request.class))).thenReturn(listResponse);

        // Mock log content
        String logContent = "2022-01-01 00:00:00 INFO: Sample log entry";
        InputStream inputStream = new ByteArrayInputStream(logContent.getBytes());
        ResponseInputStream responseInputStream = new ResponseInputStream(inputStream, inputStream);
        when(s3Client.getObject(any(GetObjectRequest.class))).thenReturn(responseInputStream);

        // Search logs
        List<String> matchingLogs = logService.searchLogs("INFO", LocalDateTime.of(2022, 1, 1, 0, 0),
                LocalDateTime.of(2022, 1, 1, 23, 59));

        // Verify
        assertEquals(1, matchingLogs.size());
        assertEquals(logContent, matchingLogs.get(0));
    }
}
