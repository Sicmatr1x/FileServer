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

    /**
     * 1. 生成postman批量请求json文件
     * 2. 测试 ReceiveService 的 file接收方法并模拟 postman 调该方法
     * 3. 测试 ReceiveService 的 file 生成的文件的MD5是否与源文件MD5一致
     * @throws IOException
     */
    @Test
    void generatePostmanJsonFile() throws IOException {
        // input
        FileConfig.init();
        Assert.assertTrue(FileConfig.fileExists(FileConfig.TEST_FILE_PATH));
        String md5 = MD5Util.getFileMd5(FileConfig.TEST_FILE_PATH);
        System.out.println(FileConfig.TEST_FILE_PATH + " md5=" + md5);
        Assert.assertNotNull(md5);
        // do
        PostmanJson postmanJson = JsonGenerator.generatePostmanJsonFile(FileConfig.TEST_FILE_PATH, FileConfig.JSON_FILE_PATH);
        // assert
        Assert.assertNotNull(postmanJson);
        Assert.assertNotNull(postmanJson.getPostBodyPayloadList());
        Assert.assertFalse(postmanJson.getPostBodyPayloadList().isEmpty());
        Assert.assertTrue(FileConfig.fileExists(FileConfig.JSON_FILE_PATH));
        // test service
        int count = postmanJson.getPostBodyPayloadList().size();
        ReceiveService receiveService = new ReceiveServiceImpl();
        ResponseEntity response = null;
        for (int i = 0; i < count; i++) {
            SliceEntity entity = postmanJson.getPostBodyPayloadList().get(i);
            response = receiveService.file(FileConfig.TEMP_FILE_NAME, count, entity);
        }
        Assert.assertTrue(FileConfig.fileExists(FileConfig.TEMP_FILE_PATH));
        Assert.assertNotNull(response);
        Assert.assertEquals(md5, response.getData());

        FileConfig.deleteFile(FileConfig.JSON_FILE_PATH);
        FileConfig.deleteFile(FileConfig.TEMP_FILE_PATH);
    }

}