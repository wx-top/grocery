package icu.wenxin.grocery.controller;

import icu.wenxin.grocery.common.resp.R;
import icu.wenxin.grocery.utils.MinioUtils;
import io.minio.StatObjectResponse;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/minio")
@RequiredArgsConstructor
public class MinioController {
    
    private final MinioUtils minioUtils;

    /**
     * 简单文件上传
     */
    @PostMapping("/upload")
    public R<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            Map<String, String> fileInfo = minioUtils.uploadFile(file);
            if (fileInfo != null) {

                return R.ok("上传成功", fileInfo);
            } else {
                return R.error("上传失败");
            }
        } catch (Exception e) {
            log.error("上传异常：{}", e.getMessage(), e);
            return R.error("上传异常：" + e.getMessage());
        }
    }

    /**
     * 批量文件上传
     */
    @PostMapping("/upload/batch")
    public R<List<Map<String, String>>> uploadFiles(@RequestParam("files") List<MultipartFile> files) {
        try {
            List<Map<String, String>> fileInfos = minioUtils.uploadFiles(files);
            return R.ok("上传成功", fileInfos);
        } catch (Exception e) {
            log.error("批量文件上传：{}", e.getMessage(), e);
            return R.error("批量文件上传：" + e.getMessage());
        }
    }

    /**
     * 删除文件
     */
    @DeleteMapping("/delete/{fileName}")
    public R<String> deleteFile(@PathVariable("fileName") String fileName) {
        try {
            minioUtils.deleteFile(fileName);
            return R.ok("删除成功", null);
        } catch (Exception e) {
            log.error("删除异常：{}", e.getMessage(), e);
            return R.error("删除异常：" + e.getMessage());
        }
    }

    /**
     * 列出所有文件
     */
    @GetMapping("/list")
    public R<List<Item>> listFiles() {
        try {
            List<Item> items = minioUtils.listObjects();

            return R.ok(items);
        } catch (Exception e) {
            log.error("列出所有文件：{}", e.getMessage(), e);
            return R.error("列出所有文件：" + e.getMessage());
        }
    }



}