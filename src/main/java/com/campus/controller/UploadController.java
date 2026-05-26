package com.campus.controller;

import com.campus.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@RestController
@Slf4j
public class UploadController {
    // 上传目录
    private static final String UPLOAD_DIR = "C:/upload/";

    @PostMapping("/upload")
    public Result<String> upload(
            @RequestParam("file") MultipartFile file) throws Exception {
        // 文件为空
        if (file.isEmpty()) {
            throw new RuntimeException("文件不能为空");
        }
        // 原文件名
        String originalFilename = file.getOriginalFilename();
        // 后缀
        String suffix = StringUtils.getFilenameExtension(
                originalFilename);
        // 新文件名
        String newFileName = UUID.randomUUID() + "." + suffix;
        // 创建目录
        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // 保存文件
        file.transferTo(
                new File(
                        UPLOAD_DIR + newFileName));
        // 返回访问路径
        String url = "http://localhost:8080/upload/" + newFileName;
        return Result.success(url);
    }
}
