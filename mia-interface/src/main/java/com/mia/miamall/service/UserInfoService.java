package com.mia.miamall.service;

import com.mia.miamall.bean.UserAddress;
import com.mia.miamall.bean.UserInfo;

import java.util.List;

public interface UserInfoService {

    //查询全部用户
    List<UserInfo> findAll();
    // 根据userId 查询用户地址列表
    List<UserAddress> getUserAddressList(String userId);
}
