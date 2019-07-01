package com.mia.miamall.manage.mapper;

import com.mia.miamall.bean.SkuSaleAttrValue;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SkuSaleAttrValueMapper extends Mapper<SkuSaleAttrValue> {

    //根据spuId查询销售属性值的id
    List<SkuSaleAttrValue> selectSkuSaleAttrValueListBySpu(String spuId);

}
