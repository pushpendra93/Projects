package com.project.file.search.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class SearchResponse {
    private List<String> results;
    private String msg;
}
