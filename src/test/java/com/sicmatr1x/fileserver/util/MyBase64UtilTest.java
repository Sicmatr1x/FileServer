package com.sicmatr1x.fileserver.util;

import com.sicmatr1x.fileserver.config.FileConfig;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;


class MyBase64UtilTest {

    String TEST_FILE_NAME = "avatar.7z";
    String TEMP_FILE_NAME = "temp_avatar.7z";

    @Before
    public void before() {

    }

    @After
    public void after() {

    }

    /**
     * test String and base64 convert
     * 1. string convert to base64
     * 2. base64 convert to string
     * @throws UnsupportedEncodingException
     */
    @Test
    void test_encode_decode_String_and_Base64() throws UnsupportedEncodingException {
        String text = "中文测试中文测试中文测试中文测试";
        String encodeText = MyBase64Util.encodeBase64String(text);
        String decodeText = MyBase64Util.decodeBase64String(encodeText);
        Assert.assertEquals(text, decodeText);
    }

    /**
     * test file and base64 convert
     * 1. file convert to base64
     * 2. base64 convert to file
     * @throws IOException
     */
    @Test
    void test_encode_decode_file_and_Base64() throws IOException {
        String testFilePath = FileConfig.getFilePath(TEST_FILE_NAME);
        String tempFilePath = FileConfig.getFilePath(TEMP_FILE_NAME);
        Assert.assertTrue(FileConfig.fileExists(testFilePath));

        String sourceMd5 = MD5Util.getFileMd5(testFilePath);
        String base64 = MyBase64Util.encodeBase64File(testFilePath);
        String targetMd5 = MyBase64Util.decoderBase64File(base64, tempFilePath);
        Assert.assertTrue(FileConfig.fileExists(testFilePath));
        Assert.assertEquals(sourceMd5, targetMd5);

        FileConfig.deleteFile(tempFilePath);
    }

    /**
     * test file and base64 convert and base64 convert to html safe
     * 1. file convert to base64
     * 2. base64 convert to html safe base64
     * 3. html safe base64 convert to origin base64
     * 4. base64 convert to file
     * @throws IOException
     */
    @Test
    void test_encode_decode_base64_convert_file_and_Base64() throws IOException {
        String testFilePath = FileConfig.getFilePath(TEST_FILE_NAME);
        String tempFilePath = FileConfig.getFilePath(TEMP_FILE_NAME);
        Assert.assertTrue(FileConfig.fileExists(testFilePath));

        String sourceMd5 = MD5Util.getFileMd5(testFilePath);
        String base64 = MyBase64Util.encodeBase64File(testFilePath);

        String safeBase64 = MyBase64Util.convertBase64ToHtmlSafeStr(base64);
        String unsafeBase64 = MyBase64Util.convertHtmlSafeStrToBase64(safeBase64);
        Assert.assertEquals(base64, unsafeBase64);

        String targetMd5 = MyBase64Util.decoderBase64File(base64, tempFilePath);
        Assert.assertTrue(FileConfig.fileExists(tempFilePath));
        Assert.assertEquals(sourceMd5, targetMd5);

        FileConfig.deleteFile(tempFilePath);
    }
}