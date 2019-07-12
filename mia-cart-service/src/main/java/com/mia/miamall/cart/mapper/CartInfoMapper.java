package com.mia.miamall.cart.mapper;

import com.mia.miamall.bean.CartInfo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CartInfoMapper extends Mapper<CartInfo> {
    /**
     * 为商品验价做准备
     * @param userId
     * @return
     */
    List<CartInfo> selectCartListWithCurPrice(String userId);

    /**
     * 查询购物车
     * @param skuId
     * @param userId
     * @return
     */
    CartInfo selectOneByUserIdAndSkuId(String skuId, String userId);
}
