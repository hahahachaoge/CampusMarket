package com.campus.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.entity.ChatMessage;
import com.campus.vo.ChatSessionVO;

import java.util.List;

public interface ChatService {
    Page<ChatMessage> history(Long toUserId, Integer current, Integer size);
    void read(Long fromUserId);
    Integer unreadCount();
    List<ChatSessionVO> sessionList();
}
