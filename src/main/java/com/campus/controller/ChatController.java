package com.campus.controller;

import com.campus.common.Result;
import com.campus.entity.ChatMessage;
import com.campus.service.ChatService;
import com.campus.vo.ChatSessionVO;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

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
