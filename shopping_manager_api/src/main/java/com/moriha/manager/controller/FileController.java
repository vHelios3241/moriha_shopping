package com.moriha.manager.controller;

import com.moriha.common.result.BaseResult;
import com.moriha.common.service.FileService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/file")
public class FileController {

    @DubboReference
    private FileService fileService;

    /**
     * 上传文件
     * @param file 文件
     * @return 文件路径
     * @throws IOException
     */
    @PostMapping("/uploadImage")
    public BaseResult<String> uploadImage(MultipartFile file) throws IOException {
         String url = fileService.uploadFile(file.getBytes(), file.getOriginalFilename());
         return BaseResult.ok(url);
     }

     /**
      * 删除文件
      * @param filePath 文件路径
      * @return 删除结果
      */
     @DeleteMapping("/deleteImage")
     public BaseResult<String> deleteImage(String filePath){
         fileService.deleteFile(filePath);
         return BaseResult.ok();
     }

}
