package com.mia.maimall.order.controller;

import com.mia.miamall.bean.UserAddress;
import com.mia.miamall.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private UserInfoService userInfoService;

    @RequestMapping("trade")
    @ResponseBody
    public List<UserAddress> getUserAddress(String userId){

        List<UserAddress> userAddressesList = userInfoService.getUserAddressList(userId);
        return userAddressesList;

    }


}
