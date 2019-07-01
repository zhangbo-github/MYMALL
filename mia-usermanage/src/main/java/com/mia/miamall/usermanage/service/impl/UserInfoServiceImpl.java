package com.mia.miamall.usermanage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.mia.miamall.bean.UserAddress;
import com.mia.miamall.bean.UserInfo;
import com.mia.miamall.service.UserInfoService;
import com.mia.miamall.usermanage.mapper.UserAddressMapper;
import com.mia.miamall.usermanage.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class UserInfoServiceImpl implements UserInfoService {

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
}
