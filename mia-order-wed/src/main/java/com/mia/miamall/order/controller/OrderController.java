package com.mia.miamall.order.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mia.miamall.bean.CartInfo;
import com.mia.miamall.bean.OrderDetail;
import com.mia.miamall.bean.OrderInfo;
import com.mia.miamall.bean.UserAddress;
import com.mia.miamall.bean.enums.OrderStatus;
import com.mia.miamall.bean.enums.ProcessStatus;
import com.mia.miamall.config.LoginRequire;
import com.mia.miamall.service.CartService;
import com.mia.miamall.service.OrderService;
import com.mia.miamall.service.UserInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderController {

    // 在订单项目中，调用usermanage项目中 根据userId查询userAddress方法
//    @Autowired
    @Reference
    private UserInfoService userInfoService;

    @Reference
    private CartService cartService;

    @Reference
    private OrderService orderService;

    @RequestMapping("trade")
    @LoginRequire
    public String trade(HttpServletRequest request){

        // 被选中的商品 redis user:userId:checked
        String userId = (String) request.getAttribute("userId");
        // 用户等信息,获取用户地址！ 为啥是一个list？
        List<UserAddress> userAddressList = userInfoService.getUserAddressList(userId);

        // 循环选中商品列表
        List<CartInfo> cartInfoList =  cartService.getCartCheckedList(userId);
        // 将cartInfo 的集合 循环遍历给订单详情表
        ArrayList<OrderDetail> orderDetailList = new ArrayList<>(cartInfoList.size());
        for (CartInfo cartInfo : cartInfoList) {
            OrderDetail orderDetail = new OrderDetail();
            // 属性赋值
            orderDetail.setSkuId(cartInfo.getSkuId());
            orderDetail.setSkuName(cartInfo.getSkuName());
            orderDetail.setImgUrl(cartInfo.getImgUrl());
            orderDetail.setSkuNum(cartInfo.getSkuNum());
            orderDetail.setOrderPrice(cartInfo.getCartPrice());
            orderDetailList.add(orderDetail);
        }

        // 页面需要商品总金额：商品详情列表中的所有价格的总和
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderDetailList(orderDetailList);
        // 计算总价格  this.totalAmount=  totalAmount;
        orderInfo.sumTotalAmount();

        request.setAttribute("totalAmount",orderInfo.getTotalAmount());

        request.setAttribute("orderDetailList",orderDetailList);
        request.setAttribute("userAddressList",userAddressList);
        request.setAttribute("cartInfoList",cartInfoList);
        // 将流水号存储
        request.setAttribute("tradeCode",orderService.getTradeNo(userId));

        return "trade";
    }


    @RequestMapping(value = "submitOrder",method = RequestMethod.POST)
    @LoginRequire
    public  String submitOrder(OrderInfo orderInfo,HttpServletRequest request){
        // 获取登录userId
        String userId = (String) request.getAttribute("userId");
        // 先获取页面传递过来的tradeCode(订单流水号)
        String tradeNo = request.getParameter("tradeNo");
        // 做比较
        boolean flag = orderService.checkTradeCode(userId, tradeNo);
        if (!flag){
            request.setAttribute("errMsg","该页面已失效，请重新提交!");
            return "tradeFail";
        }

        // 验证库存！验证orderDetailList中每一个商品
        List<OrderDetail> orderDetailList = orderInfo.getOrderDetailList();
        if (orderDetailList!=null && orderDetailList.size()>0){

            // 遍历
            for (OrderDetail orderDetail : orderDetailList) {
                // 验价：cartInfo.cartPrice() == {cartInfo.skuPrice=skuInfo.price} 展示商品价格应该是skuInfo.price ,验价= cartPrice - skuInfo.price

                boolean result = orderService.checkStock(orderDetail.getSkuId(), orderDetail.getSkuNum());
                if (!result){
                    request.setAttribute("errMsg","库存不足，请重新下单!");
                    return "tradeFail";
                }
            }
        }


        // 设置订单状态，订单进程状态，总金额
        orderInfo.setOrderStatus(OrderStatus.UNPAID);
        orderInfo.setProcessStatus(ProcessStatus.UNPAID);
        orderInfo.sumTotalAmount();

        orderInfo.setUserId(userId);
        // 插入数据
        String orderId = orderService.saveOrder(orderInfo);
        // 删除redis 中的 tradeNo(订单流水号)
        orderService.delTradeCode(userId);
        // 根据订单的id 进行支付
        return "redirect://payment.miamall.com/index?orderId="+orderId;
    }


}
