package com.emailsendermultiple.dto;

import lombok.Data;

import java.util.List;

@Data
public class EmailRequest {
    private String recipient;
    private String subject;
}
