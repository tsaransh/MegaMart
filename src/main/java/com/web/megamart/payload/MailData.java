package com.web.megamart.payload;

import lombok.Data;

@Data
public class MailData {
    private String sendTo;
    private String subject;
    private String message;
}
