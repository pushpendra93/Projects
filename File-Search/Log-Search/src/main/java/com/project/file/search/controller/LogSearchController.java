package com.project.file.search.controller;

import com.project.file.search.dto.response.SearchResponse;
import com.project.file.search.exception.SearchException;
import com.project.file.search.services.LogSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@RestController
@RequestMapping("/log")
@Validated
public class LogSearchController {

    @Autowired
    private LogSearchService logService;

    /**
     * Endpoint for searching logs based on search criteria.
     *
     * @param searchKeyword The keyword to search for in the logs.
     * @param from          The start date-time for the search range in Unix timestamp format.
     * @param to            The end date-time for the search range in Unix timestamp format.
     * @return A list of log lines containing the search keyword.
     */
    @GetMapping("/search")
    public ResponseEntity<SearchResponse> searchLogs(
            @RequestParam(required = false, value = "keyword") String searchKeyword,
            @RequestParam(value = "from") long from,
            @RequestParam(value = "to") long to
    ) {
        if (searchKeyword == null || searchKeyword.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(SearchResponse.builder().msg("keyword can't be empty").build());
        }
        LocalDateTime fromDate = LocalDateTime.ofEpochSecond(from, 0, ZoneOffset.UTC);
        LocalDateTime toDate = LocalDateTime.ofEpochSecond(to, 0, ZoneOffset.UTC);
        List<String> matchingLogs;
        try {
            matchingLogs = logService.searchLogs(searchKeyword, fromDate, toDate);
        } catch (SearchException e) {
            return ResponseEntity.internalServerError()
                    .body(SearchResponse.builder().msg("some internal server occurred").build());
        }
        return ResponseEntity.ok(SearchResponse.builder().results(matchingLogs).build());
    }
}
