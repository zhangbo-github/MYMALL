package com.mia.miamall.thym.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Map;

@Controller
public class IndexController {

    @RequestMapping("index")
    public String index(Map map , Model model, ModelMap modelMap, HttpServletRequest request, HttpSession session){
        // 存储值
        request.setAttribute("name","刘德华");
        model.addAttribute("age","60");
        // session
        session.setAttribute("sname","宝宝");
        //
        request.setAttribute("gname","<span style=\"color:green\">宝强</span>");

        // 在后台添加一个集合

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("大乔");
        arrayList.add("小乔");
        arrayList.add("吕布");
        arrayList.add("马蓉");
        arrayList.add("陈冠希");
        arrayList.add("周瑜");
        arrayList.add("貂蝉");
        arrayList.add("悟空");

        request.setAttribute("arrayList",arrayList);



        return "index";
    }
}
