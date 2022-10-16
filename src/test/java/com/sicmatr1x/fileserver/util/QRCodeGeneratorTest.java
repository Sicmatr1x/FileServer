package com.sicmatr1x.fileserver.util;

import com.google.zxing.WriterException;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class QRCodeGeneratorTest {

    String TEMP_FILE = "/files/qrCode.png";
    String TEST_TEMP_FILE_PATH = "";

    public static boolean fileExists(String path) {
        File file = new File(path);
        return file.exists();
    }

    public static boolean deleteFile(String path) {
        File file = new File(path);
        return file.delete();
    }

    /**
     * 文本直接转QR码
     * @throws IOException
     * @throws WriterException
     */
    @Test
    void test_string_generate_QRCode() throws IOException, WriterException {
        String input = "中文测试中文测试中文测试中文测试";

        TEST_TEMP_FILE_PATH = System.getProperty("user.dir") + TEMP_FILE;
        QRCodeGenerator.generateQRCodeImage(input, 640, 640, TEST_TEMP_FILE_PATH);

        Assert.assertTrue(fileExists(TEST_TEMP_FILE_PATH));
        deleteFile(TEST_TEMP_FILE_PATH);
    }

    /**
     * URL直接转QR码
     * @throws IOException
     * @throws WriterException
     */
    @Test
    void test_url_generate_QRCode() throws IOException, WriterException {
        String input = "https://www.zhihu.com/question/319414486/answer/1412435049";

        TEST_TEMP_FILE_PATH = System.getProperty("user.dir") + TEMP_FILE;
        QRCodeGenerator.generateQRCodeImage(input, 350, 350, TEST_TEMP_FILE_PATH);

        Assert.assertTrue(fileExists(TEST_TEMP_FILE_PATH));
        deleteFile(TEST_TEMP_FILE_PATH);
    }

    /**
     * 文本转base64再转QR码
     * @throws IOException
     * @throws WriterException
     */
    @Test
    void test_base64_generate_QRCode() throws IOException, WriterException {
        String input = "https://www.zhihu.com/question/319414486/answer/1412435049";
        String base64 = MyBase64Util.encodeBase64String(input);

        TEST_TEMP_FILE_PATH = System.getProperty("user.dir") + TEMP_FILE;
        QRCodeGenerator.generateQRCodeImage(base64, 640, 640, TEST_TEMP_FILE_PATH);

        Assert.assertTrue(fileExists(TEST_TEMP_FILE_PATH));
        deleteFile(TEST_TEMP_FILE_PATH);
    }

    /**
     * 通过QR码发送文件
     * 文件转base64转QR码
     * @throws IOException
     * @throws WriterException
     */
    @Test
    void test_file_generate_QRCode() throws IOException, WriterException {
        String input = "/files/avatar.7z";
        String path = System.getProperty("user.dir") + input;
        String base64 = MyBase64Util.encodeBase64File(path);

        TEST_TEMP_FILE_PATH = System.getProperty("user.dir") + TEMP_FILE;
        QRCodeGenerator.generateQRCodeImage(base64, 640, 640, TEST_TEMP_FILE_PATH);

        Assert.assertTrue(fileExists(TEST_TEMP_FILE_PATH));
        deleteFile(TEST_TEMP_FILE_PATH);
    }
}