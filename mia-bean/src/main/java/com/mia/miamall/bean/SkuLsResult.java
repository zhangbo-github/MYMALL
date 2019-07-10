package com.mia.miamall.bean;

import java.io.Serializable;
import java.util.List;

public class SkuLsResult implements Serializable {

    List<SkuLsInfo> skuLsInfoList;

    /**
     * 总条数
     */
    long total;

    /**
     * 总页数
     */
    long totalPages;

    List<String> attrValueIdList;

    public List<SkuLsInfo> getSkuLsInfoList() {
        return skuLsInfoList;
    }

    public void setSkuLsInfoList(List<SkuLsInfo> skuLsInfoList) {
        this.skuLsInfoList = skuLsInfoList;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public List<String> getAttrValueIdList() {
        return attrValueIdList;
    }

    public void setAttrValueIdList(List<String> attrValueIdList) {
        this.attrValueIdList = attrValueIdList;
    }

    @Override
    public String toString() {
        return "SkuLsResult{" +
                "skuLsInfoList=" + skuLsInfoList +
                ", total=" + total +
                ", totalPages=" + totalPages +
                ", attrValueIdList=" + attrValueIdList +
                '}';
    }
}
