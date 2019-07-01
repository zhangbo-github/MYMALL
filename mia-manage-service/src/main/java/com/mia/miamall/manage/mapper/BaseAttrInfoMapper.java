package com.mia.miamall.manage.mapper;

import com.mia.miamall.bean.BaseAttrInfo;
import com.mia.miamall.bean.SpuSaleAttr;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BaseAttrInfoMapper extends Mapper<BaseAttrInfo> {

    List<BaseAttrInfo> getBaseAttrInfoListByCatalog3Id(long parseLong);
}
