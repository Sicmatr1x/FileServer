package com.sicmatr1x.fileserver.entity;

import java.util.ArrayList;
import java.util.List;

public class PostmanJson {

    private String base64;

    private List<SliceEntity> postBodyPayloadList = new ArrayList<>();

    public PostmanJson() {

    }

    public PostmanJson(String base64) {
        this.base64 = base64;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public List<SliceEntity> getPostBodyPayloadList() {
        return postBodyPayloadList;
    }

    public void setPostBodyPayloadList(List<SliceEntity> postBodyPayloadList) {
        this.postBodyPayloadList = postBodyPayloadList;
    }
}
