package com.project.file.search.services;

import com.project.file.search.exception.SearchException;

import java.time.LocalDateTime;
import java.util.List;

public interface LogService {
    List<String> searchLogs(String searchKeyword, LocalDateTime from, LocalDateTime to) throws SearchException;
}
