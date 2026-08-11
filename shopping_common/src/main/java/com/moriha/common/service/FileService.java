package com.moriha.common.service;

public interface FileService {

    /**
     * 上传文件
     * @param filebytes
     * @param fileName
     * @return
     */
    String uploadFile(byte[] filebytes, String fileName);

    /**
     * 删除文件
     * @param  filePath
     */
    void deleteFile(String  filePath);
}
