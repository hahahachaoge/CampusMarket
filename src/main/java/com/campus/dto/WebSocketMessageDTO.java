package com.campus.dto;

import lombok.Data;

@Data
public class WebSocketMessageDTO {
    private Long toUserId;
    private String content;
}
