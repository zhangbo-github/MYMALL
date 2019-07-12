package com.mia.miamall.order.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.mia.miamall.bean.OrderDetail;
import com.mia.miamall.bean.OrderInfo;
import com.mia.miamall.config.RedisUtil;
import com.mia.miamall.order.mapper.OrderDetailMapper;
import com.mia.miamall.order.mapper.OrderInfoMapper;
import com.mia.miamall.service.OrderService;
import com.mia.miamall.util.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private RedisUtil redisUtil;


    @Override
    public String saveOrder(OrderInfo orderInfo) {
        // 订单的创建时间
        orderInfo.setCreateTime(new Date());
        // 过期时间
        Calendar calendar = Calendar.getInstance();
        // 当前时间+1
        calendar.add(Calendar.DATE,1);
        // 赋值一个过期时间
        orderInfo.setExpireTime(calendar.getTime());
System.out.println("赋值的过期时间: "+calendar.getTime());
        // 第三方交易编号
        String outTradeNo="MIA"+System.currentTimeMillis()+""+new Random().nextInt(1000);
        orderInfo.setOutTradeNo(outTradeNo);
System.out.println("第三方交易编号: "+outTradeNo);

        // 插入订单信息
        orderInfoMapper.insertSelective(orderInfo);
        // 订单的详细信息也要放入数据库
        List<OrderDetail> orderDetailList = orderInfo.getOrderDetailList();
        for (OrderDetail orderDetail : orderDetailList) {
            //设置订单Id
            orderDetail.setOrderId(orderInfo.getId());
            orderDetailMapper.insertSelective(orderDetail);
        }
        return orderInfo.getId();
    }

    // 生成流水号(防止重复提交)
    public String getTradeNo(String userId){

        // 生成一个流水号：
        String tradeCode = UUID.randomUUID().toString();

        // 将tradeCode 保存到redis
        Jedis jedis = redisUtil.getJedis();

        // 起key：
        String tradeNoKey="user:"+userId+":tradeCode";
        // 存值
        jedis.setex(tradeNoKey,10*60,tradeCode);

        jedis.close();
        return tradeCode;
    }

    /**
     * 比较(防止订单重复提交)
     * @param userId
     * @param tradeCodeNo
     * @return
     */
    public boolean checkTradeCode(String userId,String tradeCodeNo){
        // 取得redis - key
        String tradeNoKey="user:"+userId+":tradeCode";
        Jedis jedis = redisUtil.getJedis();
        String redisTradeCodeNo = jedis.get(tradeNoKey);
        if (tradeCodeNo.equals(redisTradeCodeNo)){
            return true;
        }else {
            return false;
        }
    }

    //比较完后删除key
    public void delTradeCode(String userId){
        // 将tradeCode 保存到redis
        Jedis jedis = redisUtil.getJedis();
        // 起key：
        String tradeNoKey="user:"+userId+":tradeCode";
        jedis.del(tradeNoKey);
        jedis.close();
    }

    @Override
    public boolean checkStock(String skuId, Integer skuNum) {
        // 调用库存系统方法 远程调用：httpClient。
        String result = HttpClientUtil.doGet("http://www.miaware.com/hasStock?skuId=" + skuId + "&num=" + skuNum);
        if ("1".equals(result)){
            return  true;
        }else {
            return false;
        }
    }

}
