package com.mia.miamall.service;

import com.mia.miamall.bean.*;

import java.util.List;

public interface ManageService {

    // 查询所有一级分类
    List<BaseCatalog1> getCatalog1();
    // 根据一级分类Id查询二级分类
    List<BaseCatalog2> getCatalog2(String catalog1Id);
    // 根据二级分类Id查询三级分类
    List<BaseCatalog3> getCatalog3(String catalog2Id);
    // 根据三级分类Id查询平台属性列表
    List<BaseAttrInfo> getAttrList(String catalog3Id);
    // 保存平台属性，平台属性值
    void saveAttrInfo(BaseAttrInfo baseAttrInfo);
    // 根据平台属性名称Id取得平台属性对象
    BaseAttrInfo getAttrInfo(String attrId);

    // 根据三级分类Id 查询spuInfo 列表
    List<SpuInfo> getSpuInfoList(SpuInfo spuInfo);

    //查询销售属性名称
    List<BaseSaleAttr> getBaseSaleAttrList();

    //保存SpuInfo
    void saveSpuInfo(SpuInfo spuInfo);

    // 查询所有图片列表
    List<SpuImage> getSpuImageList(String spuId);
    // 根据spuId 查询SpuSaleAttr 集合
    List<SpuSaleAttr> getSpuSaleAttrList(String spuId);
    // 保存skuInfo 信息
    void saveSku(SkuInfo skuInfo);



    // 根据skuId 查询数据
    SkuInfo getSkuInfo(String skuId);
    // 根据skuInfo 查询 List<SpuSaleAttr>
    List<SpuSaleAttr> selectSpuSaleAttrListCheckBySku(SkuInfo skuInfo);
    // 根据spuId查询销售属性值集合
    List<SkuSaleAttrValue> getSkuSaleAttrValueListBySpu(String spuId);

    List<BaseAttrInfo> getAttrList(List<String> attrValueIdList);




}
