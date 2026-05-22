package com.campus.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageVO {
    private Long fromUserId;
    private String content;
}
