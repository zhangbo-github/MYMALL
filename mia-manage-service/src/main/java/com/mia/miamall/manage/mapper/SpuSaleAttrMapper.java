package com.mia.miamall.manage.mapper;

import com.mia.miamall.bean.SpuSaleAttr;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SpuSaleAttrMapper extends Mapper<SpuSaleAttr> {
    // 写方法 ：根据spuId 查询SpuSaleAttr。 ..获取数据的时候使用get ,如果跟db进行交互查询select
    List<SpuSaleAttr> selectSpuSaleAttrList(String spuId);
    // 根据skuId ,spuId ，查找销售属性，销售属性值
    List<SpuSaleAttr> selectSpuSaleAttrListCheckBySku(long skuId,long spuId);
}
