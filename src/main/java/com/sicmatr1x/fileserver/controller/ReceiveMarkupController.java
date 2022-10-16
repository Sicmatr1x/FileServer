package com.sicmatr1x.fileserver.controller;

import com.sicmatr1x.fileserver.common.ResponseEntity;
import com.sicmatr1x.fileserver.entity.SliceEntity;
import com.sicmatr1x.fileserver.service.ReceiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/mcafee")
public class ReceiveMarkupController {

    @Autowired
    private ReceiveService receiveService;

    @GetMapping("/hello")
    public ResponseEntity helloGet() {
        ResponseEntity responseEntity = new ResponseEntity();
        responseEntity.setSuccess(true);
        responseEntity.setMessage("test get request");
        Map<String, String> map = new HashMap<>();
        // 设置日期格式
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 获取当前系统时间
        map.put("time", df.format(new Date()));
        responseEntity.setData(map);
        System.out.println("helloGet");
        return responseEntity;
    }

    @PostMapping("/f/{filename}/s/{size}")
    public ResponseEntity file(@PathVariable("filename") String filename, @PathVariable("size") Integer size, @RequestBody(required = true) SliceEntity sliceEntity) {
        System.out.println(sliceEntity);
        return this.receiveService.file(filename, size, sliceEntity);
    }

    @GetMapping("/check/f/{filename}/s/{size}")
    public ResponseEntity checkFileSeqList(@PathVariable("filename") String filename, @PathVariable("size") Integer size) {
        return this.receiveService.checkFileSeqList(filename, size);
    }

    @GetMapping("/info/f/{filename}/s/{size}")
    public ResponseEntity getFileSeq(@PathVariable("filename") String filename, @PathVariable("size") Integer size) {
        return this.receiveService.getFileSeq(filename, size);
    }

    @GetMapping("/f/{filename}/s/{size}/seq/{seq}/b/{context}")
    public ResponseEntity file(@PathVariable("filename") String filename,
                               @PathVariable("size") Integer size,
                               @PathVariable("seq") Integer seq,
                               @PathVariable("context") String context) {
        SliceEntity sliceEntity = new SliceEntity();
        sliceEntity.setSeq(seq);
        sliceEntity.setContext(context);
        System.out.println(sliceEntity);
        return this.receiveService.file(filename, size, sliceEntity);
    }
}