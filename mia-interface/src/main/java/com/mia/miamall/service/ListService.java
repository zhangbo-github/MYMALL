package com.mia.miamall.service;


import com.mia.miamall.bean.SkuLsInfo;
import com.mia.miamall.bean.SkuLsParams;
import com.mia.miamall.bean.SkuLsResult;

public interface ListService {

    // SkuLsInfo -- 对es 数据进行封装的对象
    void saveSkuInfo(SkuLsInfo skuLsInfo);

    // 根据用户输入的参数，返回封装好的结果集
    SkuLsResult search(SkuLsParams skuLsParams);

    //为当前的商品增加热度排名功能
    void incrHotScore(String skuId);
}
