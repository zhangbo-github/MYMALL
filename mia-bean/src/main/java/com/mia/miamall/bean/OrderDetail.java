package com.mia.miamall.bean;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

public class OrderDetail implements Serializable {
    @Id
    /**
     * 订单详情编号
     */
    @Column
    private String id;

    /**
     * 订单编号
     */
    @Column
    private String orderId;

    /**
     * skuId商品Id
     */
    @Column
    private String skuId;

    /**
     * 商品名称
     */
    @Column
    private String skuName;

    /**
     * 图片地址
     */
    @Column
    private String imgUrl;

    /**
     * 购买价格(下单时sku价格）
     */
    @Column
    private BigDecimal orderPrice;

    /**
     * 购买个数
     */
    @Column
    private Integer skuNum;

    /**
     * hasStock表示是否有库存
     */
    @Transient
    private String hasStock;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public Integer getSkuNum() {
        return skuNum;
    }

    public void setSkuNum(Integer skuNum) {
        this.skuNum = skuNum;
    }

    public String getHasStock() {
        return hasStock;
    }

    public void setHasStock(String hasStock) {
        this.hasStock = hasStock;
    }
}
