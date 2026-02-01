package org.example.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public record MarketDto(String name, List<RunnerDto> runners) {
}
