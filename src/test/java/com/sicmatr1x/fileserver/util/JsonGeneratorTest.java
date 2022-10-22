package com.sicmatr1x.fileserver.util;

import com.sicmatr1x.fileserver.common.ResponseEntity;
import com.sicmatr1x.fileserver.config.FileConfig;
import com.sicmatr1x.fileserver.entity.PostmanJson;
import com.sicmatr1x.fileserver.entity.SliceEntity;
import com.sicmatr1x.fileserver.service.ReceiveService;
import com.sicmatr1x.fileserver.service.ReceiveServiceImpl;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class JsonGeneratorTest {
    String TEST_FILE_NAME = "avatar.7z";
    String TEMP_FILE_NAME = "temp_" + TEST_FILE_NAME;
    /**
     * 1. 生成postman批量请求json文件
     * 2. 测试 ReceiveService 的 file接收方法并模拟 postman 调该方法
     * 3. 测试 ReceiveService 的 file 生成的文件的MD5是否与源文件MD5一致
     * @throws IOException
     */
    @Test
    void generatePostmanJsonFile() throws IOException {
        String testFilePath = FileConfig.getFilePath(TEST_FILE_NAME);
        String tempFilePath = FileConfig.getFilePath(TEMP_FILE_NAME);
        String jsonFilePath = FileConfig.getFilePath(JsonGenerator.JSON_FILE_NAME);
        // input
        Assert.assertTrue(FileConfig.fileExists(testFilePath));
        String md5 = MD5Util.getFileMd5(testFilePath);
        System.out.println(testFilePath + " md5=" + md5);
        Assert.assertNotNull(md5);
        // do
        PostmanJson postmanJson = JsonGenerator.generatePostmanJsonFile(testFilePath, jsonFilePath);
        // assert
        Assert.assertNotNull(postmanJson);
        Assert.assertNotNull(postmanJson.getPostBodyPayloadList());
        Assert.assertFalse(postmanJson.getPostBodyPayloadList().isEmpty());
        Assert.assertTrue(FileConfig.fileExists(jsonFilePath));
        // test service
        int count = postmanJson.getPostBodyPayloadList().size();
        ReceiveService receiveService = new ReceiveServiceImpl();
        ResponseEntity response = null;
        for (int i = 0; i < count; i++) {
            SliceEntity entity = postmanJson.getPostBodyPayloadList().get(i);
            response = receiveService.file(TEMP_FILE_NAME, count, entity);
        }
        System.out.println(TEST_FILE_NAME + ": " + md5 + " -> " + TEMP_FILE_NAME + ": " + response.getData());
        Assert.assertTrue(FileConfig.fileExists(tempFilePath));
        Assert.assertNotNull(response);
        Assert.assertEquals(md5, response.getData());

//        FileConfig.deleteFile(jsonFilePath);
        FileConfig.deleteFile(tempFilePath);
    }

}