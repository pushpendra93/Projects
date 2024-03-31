package com.project.file.search.controller;

import com.project.file.search.service.impl.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@RestController
@Validated
public class LogController {

    @Autowired
    private LogService logService;

    /**
     * Endpoint for searching logs based on search criteria.
     *
     * @param searchKeyword The keyword to search for in the logs.
     * @param from          The start date-time for the search range in Unix timestamp format.
     * @param to            The end date-time for the search range in Unix timestamp format.
     * @return A list of log lines containing the search keyword.
     */
    @GetMapping("/search")
    public ResponseEntity<List<String>> searchLogs(
            @NotEmpty(message = "Search keyword cannot be empty") @RequestParam String searchKeyword,
            @RequestParam long from,
            @RequestParam long to
    ) {
        LocalDateTime fromDate = LocalDateTime.ofEpochSecond(from, 0, ZoneOffset.UTC);
        LocalDateTime toDate = LocalDateTime.ofEpochSecond(to, 0, ZoneOffset.UTC);
        List<String> matchingLogs = logService.searchLogs(searchKeyword, fromDate, toDate);
        return ResponseEntity.ok(matchingLogs);
    }
}
