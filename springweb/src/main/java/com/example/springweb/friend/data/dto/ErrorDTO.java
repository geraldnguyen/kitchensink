package com.example.springweb.friend.data.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ErrorDTO {
    private String reason;
}
