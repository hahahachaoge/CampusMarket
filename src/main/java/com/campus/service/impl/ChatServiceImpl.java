package com.campus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.context.UserContext;
import com.campus.entity.ChatMessage;
import com.campus.entity.User;
import com.campus.enums.MessageReadEnum;
import com.campus.mapper.ChatMessageMapper;
import com.campus.mapper.UserMapper;
import com.campus.service.ChatService;
import com.campus.vo.ChatSessionVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatMessageMapper chatMessageMapper;
    private final UserMapper userMapper;

    @Override
    public Page<ChatMessage> history(Long toUserId,Integer current,Integer size){
        Long currentUserId = UserContext.getUserId();
        LambdaQueryWrapper<ChatMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(w -> w.eq(ChatMessage::getFromUserId,currentUserId).eq(ChatMessage::getToUserId,toUserId).or().eq(ChatMessage::getFromUserId,toUserId).eq(ChatMessage::getToUserId,currentUserId));
        wrapper.orderByDesc(ChatMessage::getCreateTime);
        Page<ChatMessage> page = new Page<>(current,size);
        chatMessageMapper.selectPage(page,wrapper);
        return page;
    }

    @Override
    public void read(Long fromUserId){
        Long currentUserId = UserContext.getUserId();
        LambdaQueryWrapper<ChatMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChatMessage::getFromUserId,fromUserId).eq(ChatMessage::getToUserId,currentUserId).eq(ChatMessage::getIsRead, MessageReadEnum.UNREAD.getCode());
        ChatMessage update = new ChatMessage();
        update.setIsRead(MessageReadEnum.READ.getCode());

        chatMessageMapper.update(update,wrapper);
    }

    @Override
    public Integer unreadCount(){
        Long currentUserId = UserContext.getUserId();

        LambdaQueryWrapper<ChatMessage> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(ChatMessage::getToUserId,currentUserId).eq(ChatMessage::getIsRead,MessageReadEnum.UNREAD.getCode());

        return Math.toIntExact(chatMessageMapper.selectCount(wrapper));
    }

    @Override
    public List<ChatSessionVO> sessionList(){
        Long currentUserId = UserContext.getUserId();
        //查询所有聊天记录
        LambdaQueryWrapper<ChatMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(w -> w.eq(ChatMessage::getFromUserId,currentUserId).or().eq(ChatMessage::getToUserId,currentUserId));
        wrapper.orderByDesc(ChatMessage::getCreateTime);
        List<ChatMessage> messages = chatMessageMapper.selectList(wrapper);
        //去重会话
        Map<Long,ChatSessionVO> map = new LinkedHashMap<>();
        for(ChatMessage msg:messages){
            //对方ID
            Long targetUserId;
            if(msg.getFromUserId().equals(currentUserId)){
                targetUserId = msg.getToUserId();
            }
            else{
                targetUserId = msg.getFromUserId();
            }

            //已存在会话
            if(map.containsKey(targetUserId)){
                continue;
            }

            //查询用户
            User user = userMapper.selectById(targetUserId);
            ChatSessionVO vo = new ChatSessionVO();
            vo.setUserId(targetUserId);
            if(user != null){
                vo.setNickname(user.getNickname());
                vo.setAvatar(user.getAvatar());
            }

            //最后一条消息
            vo.setLastMessage(msg.getContent());
            //最后时间
            vo.setLastTime(msg.getCreateTime());
            //未读数量
            LambdaQueryWrapper<ChatMessage> unreadWrapper = new LambdaQueryWrapper<>();
            unreadWrapper.eq(ChatMessage::getFromUserId,targetUserId).eq(ChatMessage::getToUserId,currentUserId).eq(ChatMessage::getIsRead,MessageReadEnum.UNREAD.getCode());

            Long count = chatMessageMapper.selectCount(unreadWrapper);
            vo.setUnreadCount(count.intValue());
            map.put(targetUserId,vo);
        }
        return new ArrayList<>(map.values());
    }
}
