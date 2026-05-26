package com.campus.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatSessionVO {
    private Long userId;
    private String nickname;
    private String avatar;
    private String lastMessage;
    private LocalDateTime lastTime;
    private Integer unreadCount;
}
