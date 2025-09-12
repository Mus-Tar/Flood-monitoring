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
@RequestMapping("/api/upload")
public class UploadController {

    @Value("${app.upload-dir:./uploads}")
    private String uploadDir;

    @PostMapping
    public R upload(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return R.error("文件为空");
        }
        String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String name = UUID.randomUUID().toString().replace("-", "");
        String filename = ext == null ? name : (name + "." + ext);
        Path dir = Paths.get(uploadDir);
        Files.createDirectories(dir);
        Path target = dir.resolve(filename);
        file.transferTo(target.toFile());
        String url = "/upload/" + filename;
        return R.ok().data(url);
    }
}
