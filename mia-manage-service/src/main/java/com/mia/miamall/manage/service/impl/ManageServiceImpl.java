package com.mia.miamall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.mia.miamall.bean.*;
import com.mia.miamall.manage.mapper.*;
import com.mia.miamall.service.ManageService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class ManageServiceImpl implements ManageService {


    @Autowired
    private BaseCatalog1Mapper baseCatalog1Mapper;

    @Autowired
    private BaseCatalog2Mapper baseCatalog2Mapper;

    @Autowired
    private BaseCatalog3Mapper baseCatalog3Mapper;

    @Autowired
    private BaseAttrInfoMapper baseAttrInfoMapper;

    @Autowired
    private BaseAttrValueMapper baseAttrValueMapper;

    @Autowired
    private SpuInfoMapper spuInfoMapper;

    @Autowired
    private BaseSaleAttrMapper baseSaleAttrMapper;

    @Autowired
    private SpuImageMapper spuImageMapper;
    @Autowired
    private SpuSaleAttrMapper spuSaleAttrMapper;
    @Autowired
    private  SpuSaleAttrValueMapper spuSaleAttrValueMapper;

    @Autowired
    private  SkuInfoMapper skuInfoMapper;
    @Autowired
    private  SkuImageMapper skuImageMapper;
    @Autowired
    private SkuSaleAttrValueMapper skuSaleAttrValueMapper;
    @Autowired
    private SkuAttrValueMapper skuAttrValueMapper;


    @Override
    public List<BaseCatalog1> getCatalog1() {
        return baseCatalog1Mapper.selectAll();
    }

    @Override
    public List<BaseCatalog2> getCatalog2(String catalog1Id) {
        BaseCatalog2 baseCatalog2 = new BaseCatalog2();
        baseCatalog2.setCatalog1Id(catalog1Id);
        return baseCatalog2Mapper.select(baseCatalog2);
    }

    @Override
    public List<BaseCatalog3> getCatalog3(String catalog2Id) {
        BaseCatalog3 baseCatalog3 = new BaseCatalog3();
        baseCatalog3.setCatalog2Id(catalog2Id);
        return  baseCatalog3Mapper.select(baseCatalog3);
    }

    @Override
    public List<BaseAttrInfo> getAttrList(String catalog3Id) {
//        BaseAttrInfo baseAttrInfo = new BaseAttrInfo();
//        baseAttrInfo.setCatalog3Id(catalog3Id);
//        return baseAttrInfoMapper.select(baseAttrInfo);

        return  baseAttrInfoMapper.getBaseAttrInfoListByCatalog3Id(Long.parseLong(catalog3Id));
    }

    @Override
    public void saveAttrInfo(BaseAttrInfo baseAttrInfo) {
        //  判断当前id 是否存在
        if (baseAttrInfo.getId()!=null && baseAttrInfo.getId().length()>0){
            baseAttrInfoMapper.updateByPrimaryKey(baseAttrInfo);
        }else {
            // 防止baseAttrInfo.getId()="";
            if (baseAttrInfo.getId().length()==0){
                baseAttrInfo.setId(null);
            }
            baseAttrInfoMapper.insertSelective(baseAttrInfo);
        }
        // 插入平台属性值，平台属性值--平台属性的id 有关系，在插入的时候，需要先删除原来id对应的数据，再进行插入。
        BaseAttrValue baseAttrValue = new BaseAttrValue();
        // AttrId = baseAttrInfo.Id
        baseAttrValue.setAttrId(baseAttrInfo.getId());
        baseAttrValueMapper.delete(baseAttrValue);

        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
        for (BaseAttrValue attrValue : attrValueList) {
            if (attrValue.getId().length()==0){
                attrValue.setId(null);
            }
            // 要取得BaseAttrInfo的id 实际上是取得数据库中自动增长的Id
            attrValue.setAttrId(baseAttrInfo.getId());
            baseAttrValueMapper.insertSelective(attrValue);
        }
    }

    @Override
    public BaseAttrInfo getAttrInfo(String attrId) {
        // 查询到BaseAttrInfo 对象
        BaseAttrInfo baseAttrInfo = baseAttrInfoMapper.selectByPrimaryKey(attrId);
        BaseAttrValue baseAttrValue = new BaseAttrValue();
        baseAttrValue.setAttrId(baseAttrInfo.getId());
        // 查询baseAttrValue
        List<BaseAttrValue> attrValueList = baseAttrValueMapper.select(baseAttrValue);
        baseAttrInfo.setAttrValueList(attrValueList);
        return baseAttrInfo;
    }

    @Override
    public List<SpuInfo> getSpuInfoList(SpuInfo spuInfo) {
        return spuInfoMapper.select(spuInfo);
    }

    @Override
    public List<BaseSaleAttr> getBaseSaleAttrList() {
        return baseSaleAttrMapper.selectAll();
    }

    @Override
    public void saveSpuInfo(SpuInfo spuInfo) {
        // 获取spuInfo， null ==""
        if (spuInfo.getId()==null || spuInfo.getId().length()==0){
            // 插入数据，将Id设置为null
            spuInfo.setId(null);
            spuInfoMapper.insertSelective(spuInfo);
        }else{
            spuInfoMapper.updateByPrimaryKey(spuInfo);
        }

        //  插入SpuImgae之前，根据spuId 将原始数据进行删除。delete from spuImge where spuId = ?  spuId:spuInfo.id
        SpuImage spuImage = new SpuImage();
        spuImage.setSpuId(spuInfo.getId());
        spuImageMapper.delete(spuImage);

        // 插入数据在那？spuInfo.getSpuImageList();
        List<SpuImage> spuImageList = spuInfo.getSpuImageList();
        if (spuImageList!=null && spuImageList.size()>0){
            for (SpuImage image : spuImageList) {
                // image 的id设置为null == "";
                if (image.getId()!=null && image.getId().length()==0){
                    image.setId(null);
                }
                // image:中有spuId
                image.setSpuId(spuInfo.getId());
                // 插入数据
                spuImageMapper.insertSelective(image);
            }
        }

        // 添加销售属性名称，以及销售属性值
        SpuSaleAttr spuSaleAttr = new SpuSaleAttr();
        spuSaleAttr.setSpuId(spuInfo.getId());
        spuSaleAttrMapper.delete(spuSaleAttr);

        SpuSaleAttrValue spuSaleAttrValue = new SpuSaleAttrValue();
        spuSaleAttrValue.setSpuId(spuInfo.getId());
        spuSaleAttrValueMapper.delete(spuSaleAttrValue);

        // 插入数据
        List<SpuSaleAttr> spuSaleAttrList = spuInfo.getSpuSaleAttrList();

        if (spuSaleAttrList!=null && spuImageList.size()>0){
            for (SpuSaleAttr saleAttr : spuSaleAttrList) {
                if (saleAttr.getId()!=null &&saleAttr.getId().length()==0){
                    saleAttr.setId(null);
                }
                saleAttr.setSpuId(spuInfo.getId());
                spuSaleAttrMapper.insertSelective(saleAttr);

                // 插入属性值
                List<SpuSaleAttrValue> spuSaleAttrValueList = saleAttr.getSpuSaleAttrValueList();
                for (SpuSaleAttrValue saleAttrValue : spuSaleAttrValueList) {
                    if (saleAttrValue.getId()!=null && saleAttrValue.getId().length()==0){
                        saleAttrValue.setId(null);
                    }
                    saleAttrValue.setSpuId(spuInfo.getId());
                    spuSaleAttrValueMapper.insertSelective(saleAttrValue);
                }
            }
        }
    }
    @Override
    public List<SpuImage> getSpuImageList(String spuId) {
        // spuId 传入对象
        SpuImage spuImage = new SpuImage();
        spuImage.setSpuId(spuId);
        return spuImageMapper.select(spuImage);
    }

    @Override
    public List<SpuSaleAttr> getSpuSaleAttrList(String spuId) {
        return  spuSaleAttrMapper.selectSpuSaleAttrList(spuId);
    }

    @Override
    public void saveSku(SkuInfo skuInfo) {
        // 插入skuInfo
        if (skuInfo.getId()==null || skuInfo.getId().length()==0){
            skuInfo.setId(null);
            skuInfoMapper.insertSelective(skuInfo);
        }else {
            skuInfoMapper.updateByPrimaryKeySelective(skuInfo);
        }

        // 删除skuImg
        SkuImage skuImage = new SkuImage();
        skuImage.setSkuId(skuInfo.getId());
        skuImageMapper.delete(skuImage);
        // 插入
        List<SkuImage> skuImageList = skuInfo.getSkuImageList();
        if (skuImageList!=null && skuImageList.size()>0){
            for (SkuImage image : skuImageList) {
                if (image.getId()!=null && image.getId().length()==0){
                    image.setId(null);
                }
                // skuId
                image.setSkuId(skuInfo.getId());
                skuImageMapper.insertSelective(image);
            }
        }
        //  sku_sale_attr_value
        SkuSaleAttrValue skuSaleAttrValue = new SkuSaleAttrValue();
        skuSaleAttrValue.setSkuId(skuInfo.getId());
        skuSaleAttrValueMapper.delete(skuSaleAttrValue);

        // 插入数据
        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        if (skuSaleAttrValueList!=null && skuSaleAttrValueList.size()>0){
            for (SkuSaleAttrValue saleAttrValue : skuSaleAttrValueList) {
                if (saleAttrValue.getId()!=null && saleAttrValue.getId().length()==0){
                    saleAttrValue.setId(null);
                }
                // skuId
                saleAttrValue.setSkuId(skuInfo.getId());
                skuSaleAttrValueMapper.insertSelective(saleAttrValue);
            }
        }
        // sku_attr_value
        SkuAttrValue skuAttrValue = new SkuAttrValue();
        skuAttrValue.setSkuId(skuInfo.getId());
        skuAttrValueMapper.delete(skuAttrValue);

        // 插入数据

        List<SkuAttrValue> skuAttrValueList = skuInfo.getSkuAttrValueList();
        if (skuAttrValueList!=null && skuAttrValueList.size()>0){
            for (SkuAttrValue attrValue : skuAttrValueList) {
                if (attrValue.getId()!=null && attrValue.getId().length()==0){
                    attrValue.setId(null);
                }
                // skuId
                attrValue.setSkuId(skuInfo.getId());
                skuAttrValueMapper.insertSelective(attrValue);
            }
        }
    }

    @Override
    public SkuInfo getSkuInfo(String skuId) {
//        skuId=skuInfo.id  skuInfo表中的id是不是主键
        SkuInfo skuInfo = skuInfoMapper.selectByPrimaryKey(skuId);
        // skuImag放入skuInfo的imageList集合中即可！
        // select * from skuImage where skuId = skuId?
        SkuImage skuImage = new SkuImage();
        skuImage.setSkuId(skuInfo.getId());
        List<SkuImage> skuImageList = skuImageMapper.select(skuImage);

        skuInfo.setSkuImageList(skuImageList);

        return skuInfo;
    }

    /**
     * 根据SkuInfo中的spuId,查询spuSaleAttr(销售属性)和spuSaleAttrValue（销售属性值）
     * 即该sku所属的spu的信息
     * @param skuInfo
     * @return
     */
    @Override
    public List<SpuSaleAttr> selectSpuSaleAttrListCheckBySku(SkuInfo skuInfo) {
        return spuSaleAttrMapper.selectSpuSaleAttrListCheckBySku(Long.parseLong(skuInfo.getId()),Long.parseLong(skuInfo.getSpuId()));
    }

    /**
     *
     * @param spuId
     * @return
     */
    @Override
    public List<SkuSaleAttrValue> getSkuSaleAttrValueListBySpu(String spuId) {
        return skuSaleAttrValueMapper.selectSkuSaleAttrValueListBySpu(spuId);

    }

}
