package com.mia.miamall.order.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mia.miamall.bean.UserAddress;
import com.mia.miamall.service.UserInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class OrderController {

    @Reference
    private UserInfoService userInfoService;

    @RequestMapping("trade")
    @ResponseBody
    public List<UserAddress> getUserAddress(String userId){

        List<UserAddress> userAddressesList = userInfoService.getUserAddressList(userId);
        return userAddressesList;

    }


}
