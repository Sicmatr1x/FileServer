package com.sicmatr1x.fileserver.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class QRCodeGenerator {

    private static final String QR_CODE_IMAGE_PATH = "D:/buf/repo/QRGenerate/MyQRCode.png";

    /**
     * 通过字符串生成QR码
     * @param text 字符串
     * @param width QR码宽度
     * @param height QR码高度
     * @param filePath 文件路径
     * @throws WriterException
     * @throws IOException
     */
    public static void generateQRCodeImage(String text, int width, int height, String filePath) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        Path path = FileSystems.getDefault().getPath(filePath);

        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

    }

    public static void main(String[] args) {
        try {
            //https://baijiahao.baidu.com/s?id=1645071094334239229&wfr=spider&for=pc
//            generateQRCodeImage("https://sz.lianjia.com/ershoufang/105106129739.html", 350, 350, QR_CODE_IMAGE_PATH);
            generateQRCodeImage("https://sz.lianjia.com/ershoufang/105106129739.html", 640, 640, QR_CODE_IMAGE_PATH);
        } catch (WriterException e) {
            System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Could not generate QR Code, IOException :: " + e.getMessage());
        }

    }


}