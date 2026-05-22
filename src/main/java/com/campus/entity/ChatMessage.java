package com.campus.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessage {
    private Long id;
    private Long fromUserId;
    private Long toUserId;
    private Long goodsId;
    private String content;
    private Integer isRead;
    private LocalDateTime createTime;
}
