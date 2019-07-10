package com.mia.miamall.manage.mapper;

import com.mia.miamall.bean.BaseAttrInfo;
import com.mia.miamall.bean.SpuSaleAttr;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BaseAttrInfoMapper extends Mapper<BaseAttrInfo> {

    // 根据三级分类Id进行查询 List<BaseAttrInfo>
    List<BaseAttrInfo> getBaseAttrInfoListByCatalog3Id(Long catalog3Id);
    // 根据平台属性值的Id 查询平台属性集合
    List<BaseAttrInfo> selectAttrInfoListByIds(@Param("valueIds") String valueIds);
}
