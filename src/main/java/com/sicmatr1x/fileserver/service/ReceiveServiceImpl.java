package com.sicmatr1x.fileserver.service;

import com.sicmatr1x.fileserver.common.ResponseEntity;
import com.sicmatr1x.fileserver.config.FileConfig;
import com.sicmatr1x.fileserver.entity.SliceEntity;
import com.sicmatr1x.fileserver.util.MyBase64Util;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service("receiveService")
public class ReceiveServiceImpl implements ReceiveService {

    /**
     * 文件数据map
     * 文件名 -> 文件数据片list
     */
    private final Map<String, List<SliceEntity>> fileDataMap = new HashMap<>();

    /**
     * 文件大小
     * 文件名 -> 文件数据片总数
     */
    private final Map<String, Integer> fileSizeMap = new HashMap<>();

    /**
     * 文件数据收到的数据编号set
     * 文件名 -> 文件数据片编号
     */
    private final Map<String, Set<Integer>> seqMap = new HashMap<>();

    public ReceiveServiceImpl() {

    }

    /**
     * 如果数据片存在则替换
     * @param filename 文件名
     * @param sliceEntity 新收到的数据片
     * @return 数据片seq
     */
    boolean overwriteSliceEntity(String filename, SliceEntity sliceEntity) {
        List<SliceEntity> list = this.fileDataMap.get(filename);
        Set<Integer> set = this.seqMap.get(filename);
        boolean isFound = false;
        if (set.contains(sliceEntity.getSeq())) {
            int i = 0;
            for (; i < list.size(); i++) {
                if (list.get(i).getSeq() == sliceEntity.getSeq()) {
                    isFound = true;
                    break;
                }
            }
            if (isFound) {
                list.remove(i);
                list.add(i, sliceEntity);
            }
            Collections.sort(list);
        }
        return isFound;
    }

    /**
     * 获取缺少的数据片编号
     * @param list 本地文件数据片数组
     * @param fileDataSize 文件数据片大小
     * @return 缺少的数据片编号list
     */
    List<Integer> getMissSeq(List<SliceEntity> list, int fileDataSize) {
        boolean[] isHave = new boolean[fileDataSize];
        for (SliceEntity e : list) {
            if (e.getContext() != null && !e.getContext().isEmpty()) {
                isHave[e.getSeq()] = true;
            }
        }
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < isHave.length; i++) {
            if (!isHave[i]) {
                result.add(i);
            }
        }
        return result;
    }

    @Override
    public ResponseEntity file(String filename, Integer size, SliceEntity sliceEntity) {
        ResponseEntity entity = null;
        if (!fileDataMap.containsKey(filename)) {
            this.fileDataMap.put(filename, new ArrayList<>());
        }
        if (!fileSizeMap.containsKey(filename)) {
            this.fileSizeMap.put(filename, size);
        }
        if (!seqMap.containsKey(filename)) {
            this.seqMap.put(filename, new HashSet<>());
        }
        // 文件数据片list
        List<SliceEntity> list = this.fileDataMap.get(filename);
        // 文件数据片编号set
        Set<Integer> set = this.seqMap.get(filename);
        // 文件数据片总数
        int fileDataSize = this.fileSizeMap.get(filename);
        // 当前收到的文件数据片编号
        int seq = sliceEntity.getSeq();

        // 判断 当前收到的文件数据片编号 是否在 [0, fileDataSize) 中
        if (seq < 0 || seq >= fileDataSize) {
            return new ResponseEntity("update to list", seq).failed();
        }

        // generate response data
        Map<String, Object> data = new HashMap<>();

        // 防重复检查
        // 已存在则更新
        if (set.contains(seq)) {
            boolean isUpdate = this.overwriteSliceEntity(filename, sliceEntity);
            System.out.println("already exist and over write with new [" + seq + "]=" + sliceEntity.getContext());

            data.put("seq", seq);
            data.put("operation", "update");
            data.put("stillNeed", getMissSeq(list, fileDataSize));
            if (list.size() == size) {
                entity = new ResponseEntity("update to list and already write to file", data);
            } else {
                entity = new ResponseEntity("update to list", data);
            }
            return entity;
        }
        // 不存在则添加
        set.add(seq);
        list.add(sliceEntity);

        if (list.size() == size) {
            // 如果已经收到全部数据段
            Collections.sort(list);
            System.out.println("====================================================================");
            System.out.println("receive done: [" + list.size() + "/" + size + "]");
            // 开始拼装base64
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                stringBuilder.append(list.get(i).getContext());
            }
            // generate response
            data.put("seq", seq);
            data.put("operation", "done");
            data.put("stillNeed", getMissSeq(list, fileDataSize));
            entity = new ResponseEntity("write to file", data);
            try {
                String base64Code = MyBase64Util.convertHtmlSafeStrToBase64(stringBuilder.toString());
                String filepath = FileConfig.getFilePath(filename);
                System.out.println("write to file: " + filepath + ", base64.length=" + base64Code.length());
                String md5 = MyBase64Util.decoderBase64File(base64Code, filepath);
                System.out.println("MD5=" + md5);
                entity.setData(md5);
                // 生成文件后清空map
                this.fileDataMap.put(filename, null);
            } catch (IOException e) {
                entity.setSuccess(false);
                entity.setMessage(e.getMessage());
                entity.setData(e);
                e.printStackTrace();
            }
        } else {
            // 如果没有收到全部数据段
            // generate response
            data.put("seq", seq);
            data.put("operation", "add");
            data.put("stillNeed", getMissSeq(list, fileDataSize));
            entity = new ResponseEntity("add to list", data);
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
        if (!fileDataMap.containsKey(filename)) {
            new ResponseEntity("list not containsKey:" + filename);
        }
        List<SliceEntity> list = fileDataMap.get(filename);
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
        if (!fileDataMap.containsKey(filename)) {
            new ResponseEntity("list not containsKey:" + filename);
        }
        List<SliceEntity> list = fileDataMap.get(filename);
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
