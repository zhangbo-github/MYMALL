package com.mia.miamall.item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;

import com.mia.miamall.bean.SkuInfo;
import com.mia.miamall.bean.SkuSaleAttrValue;
import com.mia.miamall.bean.SpuSaleAttr;
import com.mia.miamall.config.LoginRequire;
import com.mia.miamall.service.ListService;
import com.mia.miamall.service.ManageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@Controller
public class ItemController {

    @Reference
    private ManageService manageService;

    @Reference
    private ListService listService;

//    @RequestMapping("{skuId}.html")
//    public String index(@PathVariable(value = "skuId") String skuId){
//
//        System.out.println("skuId:  "+skuId);
//        return "item";
//    }

    @LoginRequire(autoRedirect = true)
    @RequestMapping("{skuId}.html")
    public String index(@PathVariable(value = "skuId") String skuId, Model model, HttpServletRequest request){
        // 商品详细信息，是根据页面传递过来的商品Id 进行查找！动态，如何变成动态？
        // springMVC 讲的！
        System.out.println("skuId="+skuId);
        // 根据skuId 进行查找数据 ，数据库那张表？skuInfo,调用后台manageService
        /**
         * 1.根据skuId查询skuInfo,包括skuImage信息
         */
        SkuInfo skuInfo = manageService.getSkuInfo(skuId);

        // 存入，给前台使用,skuInfo 中应该包含skuImage数据
        request.setAttribute("skuInfo",skuInfo);

        // 查询销售属性，以及销售属性值
        /**
         * 2.根据skuInfo中的spuId,查询spuSaleAttr(销售属性)和spuSaleAttrValue（销售属性值）
         * 即该sku所属的spu的信息
         */
        List<SpuSaleAttr> spuSaleAttrList = manageService.selectSpuSaleAttrListCheckBySku(skuInfo);
        // 根据skuId=skuInfo--spuId。因为spu下关联着销售属性，以及销售属性值。

        // *把销售属性、销售属性值，给页面显示！用于选择不同的sku
        request.setAttribute("spuSaleAttrList",spuSaleAttrList);
    System.out.println("spuSaleAttrList: "+spuSaleAttrList.toString());

        // 做拼接字符串的功能
        /**
         * 3.获取当前专属sku的销售属性值（SkuSaleAttrValue）列表
         *  如颜色：金色
         *  版本：NX系列
         *  用于选择框得回显（让用户知道当前选的是什么选项）
         *  同时，拼接SkuSaleAttrValue（sku销售属性值），拼接出所有sku
         */
        List<SkuSaleAttrValue> skuSaleAttrValueListBySpu = manageService.getSkuSaleAttrValueListBySpu(skuInfo.getSpuId());
//    System.out.println("skuSaleAttrValueListBySpu: "+skuSaleAttrValueListBySpu.toString());
//        # 108|110 30 108|110 32 9|11110 31 == 在java代码中拼写。
//        String jsonKey = "";
//        HashMap map = new HashMap(); 5 0-4
//        map.put("108|110",30);
//        map.put("109|111",31);

        String jsonKey = "";
        HashMap<String, String> map = new HashMap<>();
        for (int i = 0; i < skuSaleAttrValueListBySpu.size(); i++) {
            // 取得集合中的数据
            SkuSaleAttrValue skuSaleAttrValue = skuSaleAttrValueListBySpu.get(i);
//    System.out.println("skuSaleAttrValue: "+skuSaleAttrValue.toString());
            if (jsonKey.length()!=0){
                jsonKey+="|";
            }
            // jsonKey+=108 jsonKey+=108|110
            jsonKey+=skuSaleAttrValue.getSaleAttrValueId();
//    System.out.println("jsonKey: "+jsonKey);
            // 什么时候将jsonKey 重置！什么时候结束拼接
            /**
             * 如果skuSaleAttrValueListBySpu中没有数据
             *  或者当前的skuSaleAttrValue的 skuId 不等于 下一个skuSaleAttrValue的skuId
             * 因为sql语句做了groupby，所以相同的skuId一定是在一起
             */
            if ((i+1)==skuSaleAttrValueListBySpu.size() || !skuSaleAttrValue.getSkuId().equals(skuSaleAttrValueListBySpu.get(i+1).getSkuId()) ){
                map.put(jsonKey,skuSaleAttrValue.getSkuId());
                jsonKey="";
            }
        }

        // 调用热度排名
        listService.incrHotScore(skuId);

        // 转换json字符串
        String valuesSkuJson = JSON.toJSONString(map);
        System.out.println("valuesSkuJson="+valuesSkuJson);

        //将这个map传给前端（前端就可以通过拼装key，并查询出对应的value也就是skuId,从而拼装请求路径）
        request.setAttribute("valuesSkuJson",valuesSkuJson);

        return "item";
    }



}
