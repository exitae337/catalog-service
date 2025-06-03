package ru.example.catalogservice.model.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PageResponse<T>(

        @JsonProperty("page_number")
        int pageNumber,

        @JsonProperty("page_size")
        int pageSize,
        List<T> items
) {
}
