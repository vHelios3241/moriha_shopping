package com.moriha.shopping_file_service.service;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.moriha.common.result.BusException;
import com.moriha.common.result.CodeEnum;
import org.apache.dubbo.config.annotation.DubboService;

import com.moriha.common.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.ByteArrayInputStream;

@DubboService
public class FileServiceImpl implements FileService{

    @Autowired
    private FastFileStorageClient fastFileStorageClient;
    @Value("${fdfs.fileUrl}")
    private String fileUrl;  // Nginx访问FastDFS中文件的路径

    /**
     * 上传文件
     * @param filebytes
     * @param fileName
     * @return
     */
    @Override
    public String uploadFile(byte[] filebytes, String fileName) {
        if(filebytes.length != 0){
            try{
                // 1.将文件字节数组转为输入流
                ByteArrayInputStream inputStream  = new ByteArrayInputStream(filebytes);
                // 2.获取文件的后缀名
                String fileExtension = fileName.substring(fileName.lastIndexOf(".")+1);
                // 3.上传文件
                StorePath storePath = fastFileStorageClient.uploadFile(inputStream, inputStream.available(), fileExtension, null);
                // 4.返回文件路径
                String imageUrl = fileUrl + "/"+storePath.getFullPath();
                return imageUrl;
            }catch(Exception e){
                throw new BusException(CodeEnum.UPLOAD_FILE_ERROR);
            }
        }else{
            throw new BusException(CodeEnum.UPLOAD_FILE_ERROR);
        }
    }

    /**
     * 删除文件
     * @param filePath
     */
    @Override
    public void deleteFile(String filePath) {
        fastFileStorageClient.deleteFile(filePath);
    }
}
