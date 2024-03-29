package com.mia.miamall.bean;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class SpuImage implements Serializable {
    @Column
    @Id
    private String id;
    @Column
    private String spuId;
    @Column
    private String imgName;
    @Column
    private String imgUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSpuId() {
        return spuId;
    }

    public void setSpuId(String spuId) {
        this.spuId = spuId;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        return "SpuImage{" +
                "id='" + id + '\'' +
                ", spuId='" + spuId + '\'' +
                ", imgName='" + imgName + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
