package com.mia.miamall.service;

import com.mia.miamall.bean.UserAddress;

import java.util.List;

public interface UserAddressService {

    List<UserAddress> findByUserId(String userId);

}
