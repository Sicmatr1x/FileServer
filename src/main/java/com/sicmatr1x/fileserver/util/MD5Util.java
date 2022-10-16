package com.sicmatr1x.fileserver.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 文件MD5工具
 */
public class MD5Util {

    /**
     * 获取文件的MD5值 方法一
     * @param path
     * @return
     */
    public static String getFileMd5(String path) {
        StringBuffer sb = new StringBuffer("");
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            File file = new File(path);
            FileInputStream inputFile = new FileInputStream(file);
            byte[] buffer = new byte[(int)file.length()];
            inputFile.read(buffer);
            inputFile.close();
            md.update(buffer);
            byte b[] = md.digest();
            int d;
            for (int i = 0; i < b.length; i++) {
                d = b[i];
                if (d < 0) {
                    d = b[i] & 0xff;
                }
                if (d < 16)
                    sb.append("0");
                sb.append(Integer.toHexString(d));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 获取文件的MD5值 方法二
     * @param path
     * @return
     */
    public static String getFileMd5ByBigInteger(String path) {
        try {
            InputStream fis = new FileInputStream(path);
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = fis.read(buffer, 0, 1024)) != -1) {
                md.update(buffer, 0, length);
            }
            fis.close(); //转换并返回包含16个元素字节数组,返回数值范围为-128到127
            byte[] md5Bytes = md.digest();
            BigInteger bigInt = new BigInteger(1, md5Bytes);
            return bigInt.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
