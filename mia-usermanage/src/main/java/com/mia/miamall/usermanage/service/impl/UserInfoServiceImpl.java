package com.mia.miamall.usermanage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.mia.miamall.bean.UserAddress;
import com.mia.miamall.bean.UserInfo;
import com.mia.miamall.config.RedisUtil;
import com.mia.miamall.service.UserInfoService;
import com.mia.miamall.usermanage.mapper.UserAddressMapper;
import com.mia.miamall.usermanage.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import redis.clients.jedis.Jedis;

import java.util.List;

@Service
public class UserInfoServiceImpl implements UserInfoService {


    public String userKey_prefix="user:";
    public String userinfoKey_suffix=":info";
    public int userKey_timeOut=60*60;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Override
    public List<UserInfo> findAll() {
        return userInfoMapper.selectAll();
    }

    @Override
    public List<UserAddress> getUserAddressList(String userId) {

        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);

        return userAddressMapper.select(userAddress);
    }

    @Override
    public UserInfo login(UserInfo userInfo) {
        // 页面传递 admin  -- 123
        // db   admin -- 202cb962ac59075b964b07152d234b70
        // 将 123 加密
        String passwd = userInfo.getPasswd();
        // 123 -- 202cb962ac59075b964b07152d234b70
        // 将passwd加密
        String newPwd = DigestUtils.md5DigestAsHex(passwd.getBytes());
        userInfo.setPasswd(newPwd);
        //从数据库查询对应的userInfo
        UserInfo info = userInfoMapper.selectOne(userInfo);

        Jedis jedis = redisUtil.getJedis();

        if (info!=null){
            // 定义key：user:1:info
            String userKey = userKey_prefix+info.getId()+userinfoKey_suffix;
            // 做存储,// 如果用户登录成功则将用户信息存放到redis
            jedis.setex(userKey,userKey_timeOut, JSON.toJSONString(info));

            jedis.close();

            return info;
        }
        return null;
    }

    @Override
    public UserInfo verify(String userId) {
        // 根据userId redis 中查数据
        Jedis jedis = redisUtil.getJedis();
        // 定义key
        String key = userKey_prefix+userId+userinfoKey_suffix;
System.out.println("认证方法制拼装的key: "+key);
        // 通过key 获取数据
        String userJson = jedis.get(key);
        // 因为认证操作相当于其他模块 ，在登录，就需要延长用户过期时间
        jedis.expire(key,userKey_timeOut);
System.out.println("认证后返回的userJson: "+userJson);
        if (userJson!=null && userJson.length()>0){
            // 将字符串转换为对象
            UserInfo userInfo = JSON.parseObject(userJson, UserInfo.class);
System.out.println("认证后返回的userInfo: "+userInfo);
            return userInfo;
        }
        return null;
    }
}
