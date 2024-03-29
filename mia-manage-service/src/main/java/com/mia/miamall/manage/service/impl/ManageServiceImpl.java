package com.mia.miamall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.mia.miamall.bean.*;
import com.mia.miamall.config.RedisUtil;
import com.mia.miamall.manage.constant.ManageConst;
import com.mia.miamall.manage.mapper.*;
import com.mia.miamall.service.ManageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

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

    @Autowired
    private RedisUtil redisUtil;

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

    /*根据catalog3Id获取平台属性信息*/
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

    /*获取spuInfo列表*/
    @Override
    public List<SpuInfo> getSpuInfoList(SpuInfo spuInfo) {
        return spuInfoMapper.select(spuInfo);
    }

    /*获取基本销售属性列表*/
    @Override
    public List<BaseSaleAttr> getBaseSaleAttrList() {
        return baseSaleAttrMapper.selectAll();
    }

    /*添加spuInfo*/
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

    /*获取spu的图片信息列表*/
    @Override
    public List<SpuImage> getSpuImageList(String spuId) {
        // spuId 传入对象
        SpuImage spuImage = new SpuImage();
        spuImage.setSpuId(spuId);
        return spuImageMapper.select(spuImage);
    }

    /*获取spu的销售属性列表*/
    @Override
    public List<SpuSaleAttr> getSpuSaleAttrList(String spuId) {
        return  spuSaleAttrMapper.selectSpuSaleAttrList(spuId);
    }

    /*添加sku*/
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

    /*通过skuId获取对应的SkuInfo*/
    @Override
    public SkuInfo getSkuInfo(String skuId) {
        SkuInfo skuInfo = null;

        try{
            Jedis jedis = redisUtil.getJedis();
            // 定义key
            //skuInfoKey= sku:skuId:info
            //                                  sku:                            :info
            String skuInfoKey = ManageConst.SKUKEY_PREFIX+skuId+ManageConst.SKUKEY_SUFFIX;

            //获取redis中key为skuInfoKey的数据
            String skuJson = jedis.get(skuInfoKey);

            //如果没有数据
            if (skuJson==null || skuJson.length()==0){
                // 没有数据 ,需要加锁！取出完数据，还要放入缓存中，下次直接从缓存中取得即可！
                System.out.println("没有命中缓存");
                // 定义key user:userId::lock
                //                                 sku:     skuId               :lock
                String skuLockKey=ManageConst.SKUKEY_PREFIX+skuId+ManageConst.SKULOCK_SUFFIX;
                // 生成锁                                                                                    10000
                String lockKey  = jedis.set(skuLockKey, "OK", "NX", "PX", ManageConst.SKULOCK_EXPIRE_PX);
                //如果锁还存在，则其他用户无法再访问（防止缓存击穿）（理解:redis中的key处于过期状态时，却高并发的访问这个key）
                if ("OK".equals(lockKey)){
                    System.out.println("获取锁！");
                    // 从数据库中取得数据
                    skuInfo = getSkuInfoDB(skuId);
                    // 将对象转换成json字符串
                    String skuRedisStr = JSON.toJSONString(skuInfo);
                    // 将数据放入缓存，并设置过期时间
                    jedis.setex(skuInfoKey, ManageConst.SKUKEY_TIMEOUT,skuRedisStr);
                    jedis.close();
                    return skuInfo;
                }else {
                    System.out.println("等待！");
                    // 等待
                    Thread.sleep(1000);
                    // 自旋（再次查询）
                    return getSkuInfo(skuId);
                }
            }else{
                // 有数据，把数据转为对象
                skuInfo = JSON.parseObject(skuJson, SkuInfo.class);
                //关闭jedis-client
                jedis.close();

                return skuInfo;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        // 从数据库返回数据
        return getSkuInfoDB(skuId);
    }

    // 快速提取方法：ctrl+alt+m
    /*从数据库获取skuInfo*/
    private SkuInfo getSkuInfoDB(String skuId) {
        SkuInfo skuInfo = skuInfoMapper.selectByPrimaryKey(skuId);
        // skuImag放入skuInfo的imageList集合中即可！
        // select * from skuImage where skuId = skuId?
        SkuImage skuImage = new SkuImage();
        skuImage.setSkuId(skuInfo.getId());
        List<SkuImage> skuImageList = skuImageMapper.select(skuImage);
        skuInfo.setSkuImageList(skuImageList);

        // es用到！将平台属性值进行查询并保存！
        SkuAttrValue skuAttrValue = new SkuAttrValue();
        skuAttrValue.setSkuId(skuId);
        List<SkuAttrValue> skuAttrValueList = skuAttrValueMapper.select(skuAttrValue);
        skuInfo.setSkuAttrValueList(skuAttrValueList);

        //将销售属性值查询并保存（备用）
        SkuSaleAttrValue skuSaleAttrValue = new SkuSaleAttrValue();
        skuSaleAttrValue.setSkuId(skuId);
        List<SkuSaleAttrValue> saleAttrValueList = skuSaleAttrValueMapper.select(skuSaleAttrValue);
        skuInfo.setSkuSaleAttrValueList(saleAttrValueList);

        return skuInfo;
    }

    /**
     * 根据skuInfo中skuId及关联的spuId，查询出该spu的全部销售属性名和销售属性值,
     * 			用于当前spu中不同sku的选择,
     * 			还要查询出该sku对应的sku_sale_attr_value，用于选中框的显示
     * 			添加is_checked字段，如果为1，则表示是该sku的sku_sale_attr_value
     * 			sql语句中：IF(sku_sale_attr_value.sku_id IS NOT NULL,1,0)  is_checked
     */
    @Override
    public List<SpuSaleAttr> selectSpuSaleAttrListCheckBySku(SkuInfo skuInfo) {
        return spuSaleAttrMapper.selectSpuSaleAttrListCheckBySku(Long.parseLong(skuInfo.getId()),Long.parseLong(skuInfo.getSpuId()));
    }

    /**
     *  把当前spu下的所有sku列出来，大致格式为
     * 	sku_attr_value的id(1)|sku_attr_value的id(2)|(...)... skuId
     * 	如 33|44 55   (白色|黑色 55)
     * 	sql语句：查询该spu下的所有sku
     * @param spuId
     * @return
     */
    @Override
    public List<SkuSaleAttrValue> getSkuSaleAttrValueListBySpu(String spuId) {
        return skuSaleAttrValueMapper.selectSkuSaleAttrValueListBySpu(spuId);

    }

    /*根据attrValueId集合，查询对应的BaseAttrInfo集合（面包屑前奏，筛选条件的功能，
    需要展示 平台属性名和对应的平台属性值）*/
    //这是一个重载方法，还有通过catalog3Id查询对应的平台属性名和对应的平台属性值
    @Override
    public List<BaseAttrInfo> getAttrList(List<String> attrValueIdList) {
        // 使用工具类
        String attrValueIds = StringUtils.join(attrValueIdList.toArray(), ",");
        // 调用mapper == attrValueIdList 集合，我们需要将集合中的所有Id 遍历查出数据，foreach标签：
        // select * from baseAttrInfo where id in (1,2,3,4);
        // foreach ： 逊色 --  select * from baseAttrInfo where id in (1,2,3,4);
        List<BaseAttrInfo> baseAttrInfoList = baseAttrInfoMapper.selectAttrInfoListByIds(attrValueIds);
        return baseAttrInfoList;
    }

}
