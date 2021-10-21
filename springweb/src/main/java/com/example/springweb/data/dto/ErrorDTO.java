package com.example.springweb.data.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ErrorDTO {
    private String reason;
}
