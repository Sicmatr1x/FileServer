
获取当前电脑IP
- Linux: ifconfig -a
- Windows: ipconfig /all

GET /mcafee/f/{filename}/s/{size}/seq/{seq}/b/{context}
- 192.168.50.234:7010/mcafee/f/receive_avatar.7z/s/2479/seq/{{seq}}/b/{{context}}

生成 postman 自动测试 json文件
运行 `com.sicmatr1x.fileserver.util.JsonGeneratorTest.generatePostmanJsonFile` 方法
并注释掉`FileConfig.deleteFile(FileConfig.JSON_FILE_PATH);`

URL直接转QR码
运行 `com.sicmatr1x.fileserver.util.QRCodeGeneratorTest.test_url_generate_QRCode` 方法

文本转base64再转QR码
运行 `com.sicmatr1x.fileserver.util.QRCodeGeneratorTest.test_base64_generate_QRCode` 方法

文本直接转QR码
运行 `com.sicmatr1x.fileserver.util.QRCodeGeneratorTest.test_string_generate_QRCode` 方法
