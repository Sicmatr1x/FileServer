package com.sicmatr1x.fileserver.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sicmatr1x.fileserver.entity.PostmanJson;
import com.sicmatr1x.fileserver.entity.SliceEntity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * postman 自动批量发送请求的 json 文件生成器
 */
public class JsonGenerator {

    /**
     * 生成的 json 文件名
     */
    public static String JSON_FILE_NAME = "postmanRunner.json";

    /**
     * 最大POST body大小
     * 单位: 1B（byte，字节）
     * Tomcat服务器对POST大小限制为2M = 2048KB = 2048 * 1024B
     * McAcfee限制上传目前已知1024, 512不行
     * <p>
     * 最大GET大小
     * 暂时使用512
     */
    public static Integer max_request_size = 250;

    /**
     * 分割字符串，如果开始位置大于字符串长度，返回空
     *
     * @param str 原始字符串
     * @param f   开始位置
     * @param t   结束位置
     * @return
     */
    static String substring(String str, int f, int t) {
        if (f > str.length())
            return null;
        if (t > str.length()) {
            return str.substring(f, str.length());
        } else {
            return str.substring(f, t);
        }
    }

    /**
     * 把原始字符串分割成指定长度的字符串列表
     *
     * @param inputString 原始字符串
     * @param length      指定长度
     * @return
     */
    static List<String> getStrList(String inputString, int length) {
        List<String> list = new ArrayList<>();
        for (int index = 0; ; index++) {
            String childStr = substring(inputString, index * length,
                    (index + 1) * length);
            if (childStr == null) {
                break;
            }
            list.add(childStr);
        }
        return list;
    }

    /**
     * base64切割并生成 Postman Json 对象用于后续生成 Postman Json 文件
     * @param base64
     * @return
     */
    public static PostmanJson convert(String base64) {
        PostmanJson postmanJsonObj = new PostmanJson(base64);

        List<String> strList = getStrList(postmanJsonObj.getBase64(), max_request_size);

        for (int i = 0; i < strList.size(); i++) {
            SliceEntity entity = new SliceEntity();
            entity.setSeq(i);
            entity.setContext(strList.get(i));
            postmanJsonObj.getPostBodyPayloadList().add(entity);
        }
        return postmanJsonObj;
    }

    /**
     * 使用 Postman Json 对象 生成 postman自动测试json文件
     * @param postmanJson Postman Json 对象
     * @param jsonFilePath postman自动测试json文件
     * @throws IOException
     */
    public static void writeJsonFile(PostmanJson postmanJson, String jsonFilePath) throws IOException {
        // 转换成为json并写入文件
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(postmanJson.getPostBodyPayloadList());
        FileOutputStream out = new FileOutputStream(jsonFilePath);
        byte[] buffer = jsonString.getBytes();
        out.write(buffer);
        out.close();
    }

    /**
     * 生成postman批量请求json文件
     * @param sourceFile 需要转换成base64的源文件
     * @param targetFile
     * @return 片段数: base64被切分成了多少个片段写入了生成的postman json文件里面
     * @throws IOException
     */
    public static PostmanJson generatePostmanJsonFile(String sourceFile, String targetFile) throws IOException {
        String base64Code = MyBase64Util.encodeBase64File(sourceFile);
        String safeBase64 = MyBase64Util.convertBase64ToHtmlSafeStr(base64Code);

        PostmanJson postmanJson = convert(safeBase64);
        for (int i = 0; i < postmanJson.getPostBodyPayloadList().size(); i++) {
            System.out.println(postmanJson.getPostBodyPayloadList().get(i).getContext());
            System.out.println("=============================================================================");
        }
        System.out.println("filepath=" + sourceFile + ", size=" + postmanJson.getPostBodyPayloadList().size());

        // 转换成为json并写入文件
        writeJsonFile(postmanJson, targetFile);
        return postmanJson;
    }
}
