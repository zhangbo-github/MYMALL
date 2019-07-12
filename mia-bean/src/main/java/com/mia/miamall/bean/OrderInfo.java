package com.mia.miamall.bean;


import com.mia.miamall.bean.enums.OrderStatus;
import com.mia.miamall.bean.enums.PaymentWay;
import com.mia.miamall.bean.enums.ProcessStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class OrderInfo implements Serializable {

    /**
     * 订单Id编号
     */
    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 收货人
     */
    @Column
    private String consignee;

    /**
     * 收货人电话
     */
    @Column
    private String consigneeTel;

    /**
     * 总金额
     */
    @Column
    private BigDecimal totalAmount;

    /**
     * 订单状态
     */
    @Column
    private OrderStatus orderStatus;

    /**
     * 进度状态
     */
    @Column
    private ProcessStatus processStatus;

    /**
     * 用户Id
     */
    @Column
    private String userId;

    /**
     * 付款方式
     */
    @Column
    private PaymentWay paymentWay;

    /**
     * 失效时间
     */
    @Column
    private Date expireTime;

    /**
     * 送货地址
     */
    @Column
    private String deliveryAddress;

    /**
     * 订单备注
     */
    @Column
    private String orderComment;

    /**
     * 订单创建时间
     */
    @Column
    private Date createTime;

    /**
     * 父订单编号
     */
    @Column
    private String parentOrderId;

    /**
     * 物流单编号
     */
    @Column
    private String trackingNo;


    @Transient
    private List<OrderDetail> orderDetailList;

    /**
     * 仓库Id
     */
    @Transient
    private String wareId;

    /**
     * 订单交易编号
     */
    @Column
    private String outTradeNo;

    /**
     * 取得所有订单明细价格总和
     */
    public void sumTotalAmount(){
        BigDecimal totalAmount=new BigDecimal("0");
        for (OrderDetail orderDetail : orderDetailList) {
            totalAmount= totalAmount.add(orderDetail.getOrderPrice().multiply(new BigDecimal(orderDetail.getSkuNum())));
        }
        this.totalAmount=  totalAmount;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getConsigneeTel() {
        return consigneeTel;
    }

    public void setConsigneeTel(String consigneeTel) {
        this.consigneeTel = consigneeTel;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public ProcessStatus getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(ProcessStatus processStatus) {
        this.processStatus = processStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public PaymentWay getPaymentWay() {
        return paymentWay;
    }

    public void setPaymentWay(PaymentWay paymentWay) {
        this.paymentWay = paymentWay;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getOrderComment() {
        return orderComment;
    }

    public void setOrderComment(String orderComment) {
        this.orderComment = orderComment;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getParentOrderId() {
        return parentOrderId;
    }

    public void setParentOrderId(String parentOrderId) {
        this.parentOrderId = parentOrderId;
    }

    public String getTrackingNo() {
        return trackingNo;
    }

    public void setTrackingNo(String trackingNo) {
        this.trackingNo = trackingNo;
    }

    public List<OrderDetail> getOrderDetailList() {
        return orderDetailList;
    }

    public void setOrderDetailList(List<OrderDetail> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }


    public String getWareId() {
        return wareId;
    }

    public void setWareId(String wareId) {
        this.wareId = wareId;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }
}
