package com.sicmatr1x.fileserver.service;

import com.sicmatr1x.fileserver.common.ResponseEntity;
import com.sicmatr1x.fileserver.config.FileConfig;
import com.sicmatr1x.fileserver.entity.SliceEntity;
import com.sicmatr1x.fileserver.util.MyBase64Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service("receiveService")
public class ReceiveServiceImpl implements ReceiveService {

    private final Map<String, List<SliceEntity>> map = new HashMap<>();

    private final Map<String, Set<Integer>> seqMap = new HashMap<>();

    public ReceiveServiceImpl() {
        FileConfig.init();
    }

    /**
     * 如果数据片存在则替换
     * @param filename 文件名
     * @param sliceEntity 新收到的数据片
     * @return 数据片seq
     */
    int overwriteSliceEntity(String filename, SliceEntity sliceEntity) {
        List<SliceEntity> list = this.map.get(filename);
        Set<Integer> set = this.seqMap.get(filename);
        if (set.contains(sliceEntity.getSeq())) {
            for (SliceEntity slice : list) {
                if (slice.getSeq() == sliceEntity.getSeq()) {
                    list.remove(slice);
                    list.add(sliceEntity);
                }
            }
            Collections.sort(list);
        }
        return sliceEntity.getSeq();
    }

    @Override
    public ResponseEntity file(String filename, Integer size, SliceEntity sliceEntity) {
        ResponseEntity entity = null;
        if (!map.containsKey(filename)) {
            this.map.put(filename, new ArrayList<>());
        }
        if (!seqMap.containsKey(filename)) {
            this.seqMap.put(filename, new HashSet<>());
        }
        List<SliceEntity> list = this.map.get(filename);
        Set<Integer> set = this.seqMap.get(filename);
        // 防重复检查
        if (set.contains(sliceEntity.getSeq())) {
            int seq = this.overwriteSliceEntity(filename, sliceEntity);
            System.out.println("already exist and over write with new [" + seq + "]=" + sliceEntity.getContext());
        }
        set.add(sliceEntity.getSeq());
        list.add(sliceEntity);
        if (list.size() == size) { // 如果已经收到全部数据段
            Collections.sort(list);
            System.out.println("接收完毕: [" + list.size() + "/" + size + "]");
            // 开始拼装base64
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                stringBuilder.append(list.get(i).getContext());
            }
            // generate response
            entity = new ResponseEntity("write to file", sliceEntity.getSeq());
            try {
                String base64Code = MyBase64Util.convertHtmlSafeStrToBase64(stringBuilder.toString());
                String filepath = FileConfig.getFilePath(filename);
                System.out.println("写入文件: " + filepath + ", base64.length=" + base64Code.length());
                String md5 = MyBase64Util.decoderBase64File(base64Code, filepath);
                System.out.println("MD5=" + md5);
                entity.setData(md5);
                // 生成文件后清空map
                this.map.put(filename, null);
            } catch (IOException e) {
                entity.setSuccess(false);
                entity.setMessage(e.getMessage());
                entity.setData(e);
                e.printStackTrace();
            }
        } else {
            // generate response
            entity = new ResponseEntity("add to list", sliceEntity.getSeq());
        }
        return entity;
    }

    /**
     * 检查缺失文件片
     * @param list
     * @param size
     * @return
     */
    int[] check(List<SliceEntity> list, Integer size) {
        if (list == null || size == null || size <= 0) {
            return null;
        }
        if (list.size() == size) {
            return new int[0];
        }
        int missCount = size-list.size();
        int [] missIndexList = new int[missCount];
        Collections.sort(list);
        int index = 0;
        int j = 0;
        for (SliceEntity slice : list) {
            if (slice.getSeq() != index) {
                for (; index < slice.getSeq(); index++) {
                    missIndexList[j] = index;
                    j++;
                }
            }
            index++;
        }
        return missIndexList;
    }

    @Override
    public ResponseEntity checkFileSeqList(String filename, Integer size) {
        ResponseEntity response = null;
        if (!map.containsKey(filename)) {
            new ResponseEntity("list not containsKey:" + filename);
        }
        List<SliceEntity> list = map.get(filename);
        // 检查缺失文件片
        Collections.sort(list);
        String hint = "";
        int [] missIndexList = check(list, size);

        String msg = filename + "[" + list.size() + "/" + size + "] miss:";
        for (Integer i : missIndexList) {
            msg += (i + ",");
        }
        System.out.println(msg);
        response = new ResponseEntity(msg);
        return response;
    }

    @Override
    public ResponseEntity getFileSeq(String filename, Integer seq) {
        ResponseEntity response = null;
        if (!map.containsKey(filename)) {
            new ResponseEntity("list not containsKey:" + filename);
        }
        List<SliceEntity> list = map.get(filename);
        String context = "";
        for (SliceEntity slice : list) {
            if (slice.getSeq() == seq) {
                context = slice.getContext();
            }
        }
        System.out.println(filename + "[" + seq + "]=" + context);
        response = new ResponseEntity("seq:" + seq, context);
        return response;
    }

    @Override
    public ResponseEntity writeToDisk(String filename, Integer size) {
        return null;
    }


}
