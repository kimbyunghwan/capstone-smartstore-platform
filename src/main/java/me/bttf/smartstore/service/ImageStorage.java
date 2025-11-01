package me.bttf.smartstore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ImageStorage {

    @Value("${app.upload.dir:/uploads}")
    private String uploadDir;

    public String store(MultipartFile file) {
        if (file == null || file.isEmpty()) return null;
        String ext = Optional.ofNullable(file.getOriginalFilename())
                .filter(n -> n.contains("."))
                .map(n -> n.substring(n.lastIndexOf('.')))
                .orElse("");
        String name = UUID.randomUUID() + ext;
        try {
            Path dir = Path.of(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(dir);
            Path target = dir.resolve(name);
            file.transferTo(target.toFile());
            return "/uploads/" + name; // 정적 리소스로 서빙
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 실패", e);
        }
    }

    public List<String> storeAll(List<MultipartFile> files) {
        if (files == null) return List.of();
        return files.stream().filter(f -> !f.isEmpty()).map(this::store).toList();
    }
}
