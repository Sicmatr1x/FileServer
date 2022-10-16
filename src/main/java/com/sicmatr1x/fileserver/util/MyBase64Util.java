package com.sicmatr1x.fileserver.util;

import java.io.*;
import java.util.Base64;

/**
 * base64工具类
 */
public class MyBase64Util {
    static final Base64.Decoder decoder = Base64.getDecoder();
    static final Base64.Encoder encoder = Base64.getEncoder();

    /**
     * 明文编码成base64
     * @param text 明文
     * @return base64
     * @throws UnsupportedEncodingException
     */
    public static String encodeBase64String(String text) throws UnsupportedEncodingException {
        Base64.Encoder encoder = Base64.getEncoder();
        final byte[] textByte = text.getBytes("UTF-8");
        return encoder.encodeToString(textByte);
    }

    /**
     * base64解码成明文
     * @param encodedText base64
     * @return 明文
     * @throws UnsupportedEncodingException
     */
    public static String decodeBase64String(String encodedText) throws UnsupportedEncodingException {
        Base64.Decoder decoder = Base64.getDecoder();
        return new String(decoder.decode(encodedText), "UTF-8");
    }

    /**
     * 读取文件并编码成base64字符串
     * @param path 读取的文件路径
     * @return base64字符串
     * @throws IOException
     */
    public static String encodeBase64File(String path) throws IOException {
        File file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int)file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return encoder.encodeToString(buffer);
    }

    /**
     * 将base64字符解码并保存到文件
     * @param base64Code base64字符串
     * @param targetPath 保存目标文件路径
     * @return 保存后的文件的md5
     * @throws IOException
     */
    public static String decoderBase64File(String base64Code, String targetPath) throws IOException {
        byte[] buffer = decoder.decode(base64Code);
        FileOutputStream out = new FileOutputStream(targetPath);
        out.write(buffer);
        out.close();
        return MD5Util.getFileMd5(targetPath);
    }

    /**
     * base64里面存在特殊字符与html不兼容
     * 将base64中的特殊字符转换掉
     * @param base64 base64
     * @return 安全的base64
     */
    public static String convertBase64ToHtmlSafeStr(String base64) {
        StringBuilder stringBuilder = new StringBuilder(base64);
        String str = stringBuilder.toString();
        str = str.replaceAll("\r\n", "-rn-");
        str = str.replaceAll("/", "--");
        return str;
    }

    /**
     * ase64里面存在特殊字符与html不兼容
     * 转换掉的特殊字符替换回来
     * @param safeBase64 安全的base64
     * @return 原本的base64
     */
    public static String convertHtmlSafeStrToBase64(String safeBase64) {
        StringBuilder stringBuilder = new StringBuilder(safeBase64);
        String str = stringBuilder.toString();
        str = str.replaceAll("-rn-", "\r\n");
        str = str.replaceAll("--", "/");
        return str;
    }

    /**
     * 将字符串保存到文本文件
     * @param str 字符串
     * @param targetPath 文本文件路径
     * @throws IOException
     */
    public static void toFile(String str, String targetPath) throws IOException {
        byte[] buffer = str.getBytes();
        FileOutputStream out = new FileOutputStream(targetPath);
        out.write(buffer);
        out.close();
    }

}
