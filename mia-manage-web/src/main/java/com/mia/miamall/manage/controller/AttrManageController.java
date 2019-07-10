package com.mia.miamall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mia.miamall.bean.*;
import com.mia.miamall.service.ListService;
import com.mia.miamall.service.ManageService;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

//平台属性的数据展示
@RestController
public class AttrManageController {

    @Reference
    private ManageService manageService;

    @Reference
    private ListService listService;

    // 因为easyUI 控件是基于Json格式数据，所以返回Json数据
    @RequestMapping("getCatalog1")
    public List<BaseCatalog1> getCatalog1(){
        // 调用服务层查询所有数据。
        return  manageService.getCatalog1();
    }

    @RequestMapping("getCatalog2")
    public List<BaseCatalog2> getCatalog2(String catalog1Id){
        // 调用服务层查询所有数据。
        return  manageService.getCatalog2(catalog1Id);
    }

    @RequestMapping("getCatalog3")
    public List<BaseCatalog3> getCatalog3(String catalog2Id){
        // 调用服务层查询所有数据。
        return  manageService.getCatalog3(catalog2Id);
    }

    @RequestMapping("attrInfoList")
    public List<BaseAttrInfo> attrInfoList(String catalog3Id){
        // 调用服务层查询所有数据。
        return  manageService.getAttrList(catalog3Id);
    }

    //保存AttrInfo及其AttrValue们
    @RequestMapping("saveAttrInfo")
    public void saveAttrInfo(BaseAttrInfo baseAttrInfo){
        manageService.saveAttrInfo(baseAttrInfo);
    }

    //用于编辑时数据回显，获取AttrInfo及其AttrValue们
    @RequestMapping("getAttrValueList")
    public List<BaseAttrValue> getAttrValueList(String attrId){
        // 根据平台属性名称Id 查询到BaseAttrInfo 对象
        BaseAttrInfo baseAttrInfo = manageService.getAttrInfo(attrId);
        return  baseAttrInfo.getAttrValueList();
    }



    @RequestMapping(value = "onSale",method = RequestMethod.GET)
    @ResponseBody
    public void onSale(String skuId){

        // 根据skuId 查询skuInfo 信息
        SkuInfo skuInfo = manageService.getSkuInfo(skuId);
        // 数据从哪来？skuInfo
        SkuLsInfo skuLsInfo = new SkuLsInfo();
        // 属性赋值！工具类
        try {
            BeanUtils.copyProperties(skuLsInfo,skuInfo);//只要字段相同，就全部拷贝
            System.out.println("onSale ==> skuLsInfo:  "+skuLsInfo);
            //System.out.println("onSale ==> skuInfo:  "+skuInfo);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        // 调用服务层
        listService.saveSkuInfo(skuLsInfo);
    }

}
