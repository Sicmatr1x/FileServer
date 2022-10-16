package com.sicmatr1x.fileserver.util;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MD5UtilTest {

    String TEST_FILE = "/files/avatar.7z";
    String TEST_FILE_PATH = "";

    @Test
    void getFileMd5() {
        TEST_FILE_PATH = System.getProperty("user.dir") + TEST_FILE;
        String result = MD5Util.getFileMd5(TEST_FILE_PATH);
        // certutil -hashfile avatar.7z MD5 = 90bff85dda082a8aba6c96903463d004
        Assert.assertEquals("90bff85dda082a8aba6c96903463d004", result);
    }

    @Test
    void getFileMd5ByBigInteger() {
        TEST_FILE_PATH = System.getProperty("user.dir") + TEST_FILE;
        String result = MD5Util.getFileMd5ByBigInteger(TEST_FILE_PATH);
        // certutil -hashfile avatar.7z MD5 = 90bff85dda082a8aba6c96903463d004
        Assert.assertEquals("90bff85dda082a8aba6c96903463d004", result);
    }
}