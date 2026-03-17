package com.yourcompany.yourapp.controller;

import com.yourcompany.yourapp.util.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload") // 文件上传接口入口
public class UploadController {

    // 上传目录，支持配置，默认 ./uploads
    @Value("${app.upload-dir:./uploads}")
    private String uploadDir;

    // 接收前端上传的单个文件并保存到服务器
    @PostMapping
    public R upload(MultipartFile file) throws IOException {
        // 校验文件是否为空
        if (file == null || file.isEmpty()) {
            return R.error("文件为空");
        }

        // 获取文件扩展名
        String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());

        // 使用 UUID 生成唯一文件名，避免重名
        String name = UUID.randomUUID().toString().replace("-", "");
        String filename = ext == null ? name : (name + "." + ext);

        // 确保上传目录存在
        Path dir = Paths.get(uploadDir);
        Files.createDirectories(dir);

        // 构建目标文件路径并保存
        Path target = dir.resolve(filename);
        file.transferTo(target.toFile());

        // 返回前端可访问的文件路径
        String url = "/upload/" + filename;
        return R.ok().data(url);
    }
}
