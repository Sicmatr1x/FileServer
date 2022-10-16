package com.sicmatr1x.fileserver.service;

import com.sicmatr1x.fileserver.common.ResponseEntity;
import com.sicmatr1x.fileserver.entity.SliceEntity;


public interface ReceiveService {
    /**
     * 每一次传输文件切片base64编码数据段
     * @param sliceEntity 文件切片base64编码数据段
     * @param size 文件总共含多少个数据段
     * @return 是否添加到列表成功
     */
    public ResponseEntity file(String filename, Integer size, SliceEntity sliceEntity);

    /**
     * 获取当前文件在内存中缺失的数据片编号
     * @param filename 文件名
     * @return 提示信息
     */
    public ResponseEntity checkFileSeqList(String filename, Integer size);

    /**
     * 获取当前文件在内存中编号数据片内容
     * @param filename 文件名
     * @param seq 数据片编号
     * @return
     */
    public ResponseEntity getFileSeq(String filename, Integer seq);

    /**
     * 获取当前文件手动写入磁盘
     * @param filename 文件名
     * @param size 文件总共含多少个数据段
     * @return
     */
    public ResponseEntity writeToDisk(String filename, Integer size);
}
