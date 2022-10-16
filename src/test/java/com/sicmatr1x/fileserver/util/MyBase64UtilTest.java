package com.sicmatr1x.fileserver.util;

import com.sicmatr1x.fileserver.config.FileConfig;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;


class MyBase64UtilTest {

    @Before
    public void before() {
        FileConfig.init();
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
        FileConfig.init();
        Assert.assertTrue(FileConfig.fileExists(FileConfig.TEST_FILE_PATH));

        String sourceMd5 = MD5Util.getFileMd5(FileConfig.TEST_FILE_PATH);
        String base64 = MyBase64Util.encodeBase64File(FileConfig.TEST_FILE_PATH);
        String targetMd5 = MyBase64Util.decoderBase64File(base64, FileConfig.TEMP_FILE_PATH);
        Assert.assertTrue(FileConfig.fileExists(FileConfig.TEMP_FILE_PATH));
        Assert.assertEquals(sourceMd5, targetMd5);

        FileConfig.deleteFile(FileConfig.TEMP_FILE_PATH);
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
        FileConfig.init();
        Assert.assertTrue(FileConfig.fileExists(FileConfig.TEST_FILE_PATH));

        String sourceMd5 = MD5Util.getFileMd5(FileConfig.TEST_FILE_PATH);
        String base64 = MyBase64Util.encodeBase64File(FileConfig.TEST_FILE_PATH);

        String safeBase64 = MyBase64Util.convertBase64ToHtmlSafeStr(base64);
        String unsafeBase64 = MyBase64Util.convertHtmlSafeStrToBase64(safeBase64);
        Assert.assertEquals(base64, unsafeBase64);

        String targetMd5 = MyBase64Util.decoderBase64File(base64, FileConfig.TEMP_FILE_PATH);
        Assert.assertTrue(FileConfig.fileExists(FileConfig.TEMP_FILE_PATH));
        Assert.assertEquals(sourceMd5, targetMd5);

        FileConfig.deleteFile(FileConfig.TEMP_FILE_PATH);
    }
}