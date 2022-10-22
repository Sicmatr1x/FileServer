package com.sicmatr1x.fileserver.config;


import java.io.File;

public class FileConfig {

    public static String WORK_FOLDER = System.getProperty("user.dir") + "/files";

    public static String TEST_FILE_NAME = "avatar.7z";

    /**
     * 根据默认工作路径生成文件路径
     * @param fileName 文件名
     * @return 文件路径
     */
    public static String getFilePath(String fileName) {
        return WORK_FOLDER + "/" + fileName;
    }

    public static boolean fileExists(String path) {
        File file = new File(path);
        return file.exists();
    }

    public static boolean deleteFile(String path) {
        File file = new File(path);
        return file.delete();
    }
}
