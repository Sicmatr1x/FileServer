package com.sicmatr1x.fileserver.config;


import java.io.File;

public class FileConfig {

    public static String TEST_FILE_NAME = "avatar.7z";
    public static String TEST_FILE_PATH = "";

    public static String JSON_FILE_NAME = "postmanRunner.json";
    public static String JSON_FILE_PATH = "";

    public static String TEMP_FILE_NAME = "test_avatar.7z";
    public static String TEMP_FILE_PATH = "";

    public static String OUTPUT_FOLDER = "/files";
    public static String OUTPUT_FOLDER_PATH = "";

    public static void init() {
        OUTPUT_FOLDER_PATH = "";
        TEST_FILE_PATH = "";
        JSON_FILE_PATH = "";
        TEMP_FILE_PATH = "";

        OUTPUT_FOLDER_PATH = System.getProperty("user.dir") + OUTPUT_FOLDER;
        TEST_FILE_PATH = getFilePath(TEST_FILE_NAME);
        JSON_FILE_PATH = getFilePath(JSON_FILE_NAME);
        TEMP_FILE_PATH = getFilePath(TEMP_FILE_NAME);
    }

    public static String getFilePath(String fileName) {
        return OUTPUT_FOLDER_PATH + "/" + fileName;
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
