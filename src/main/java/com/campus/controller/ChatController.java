package com.campus.controller;

import com.campus.common.Result;
import com.campus.entity.ChatMessage;
import com.campus.service.ChatService;
import com.campus.vo.ChatSessionVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "聊天模块", description = "消息发送、历史记录、未读消息、会话列表")
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @Operation(summary = "获取聊天历史记录", description = "分页查询与指定用户的聊天消息")
    @GetMapping("/history/{toUserId}")
    @Parameter(
            name = "token",
            description = "用户token",
            required = true,
            in = ParameterIn.HEADER
    )
    public Result<Page<ChatMessage>> history(
            @PathVariable Long toUserId,
            @RequestHeader("token") String token,

            @RequestParam(defaultValue = "1")
            Integer current,

            @RequestParam(defaultValue = "10")
            Integer size
    ){
        return Result.success(chatService.history(toUserId,current,size));
    }

    @Operation(summary = "标记消息已读", description = "将指定用户发送的消息标记为已读状态")
    @PutMapping("/read/{fromUserId}")
    @Parameter(
            name = "token",
            description = "用户token",
            required = true,
            in = ParameterIn.HEADER
    )
    public Result<String> read(
            @PathVariable Long fromUserId,

            @RequestHeader("token")
            String token
    ){
        chatService.read(fromUserId);
        return Result.success("已读成功");
    }

    @Operation(summary = "统计未读消息总数", description = "获取当前用户所有未读消息数量")
    @GetMapping("/unread/count")
    @Parameter(
            name = "token",
            description = "用户token",
            required = true,
            in = ParameterIn.HEADER
    )
    public Result<Integer> unreadCount(
            @RequestHeader("token")
            String token
    ){
        return Result.success(chatService.unreadCount());
    }

    @Operation(summary = "获取聊天会话列表", description = "查询所有聊天对象及最新消息")
    @GetMapping("/session/list")
    @Parameter(
            name = "token",
            description = "用户token",
            required = true,
            in = ParameterIn.HEADER
    )
    public Result<List<ChatSessionVO>> sessionList(
            @RequestHeader("token")
            String token
    ){
        return Result.success(chatService.sessionList());
    }
}
