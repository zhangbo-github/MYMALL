package com.mia.miamall.manage.controller;

import org.apache.commons.lang3.StringUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class FileUploadController {
    // @Value 注解从配置文件中直接取值！使用@Value注解必须在spring 容器中才能使用！
    @Value("${fileServer.url}")
    private  String fileUrl; // fileUrl = http://192.168.67.204

    // springMVC: 百度控件能够支持多数据上传MultipartFile
    @RequestMapping(value = "fileUpload",method = RequestMethod.POST)
    public String upload(@RequestParam("file") MultipartFile file)throws IOException, MyException {

        String imgUrl = fileUrl;

        if (file!=null){
            String configFile  = this.getClass().getResource("/tracker.conf").getFile();
            ClientGlobal.init(configFile);
            TrackerClient trackerClient=new TrackerClient();
            TrackerServer trackerServer=trackerClient.getConnection();
            StorageClient storageClient=new StorageClient(trackerServer,null);
            // 获取文件名称
            String filename = file.getOriginalFilename();
            // 获取后缀名
            String extName  = StringUtils.substringAfterLast(filename,".");

            String[] upload_file = storageClient.upload_file(file.getBytes(), extName, null);
//            s = group1
//            s = M00/00/00/wKhDzFuBc3iAKTHYAACGx2c4tJ4846.jpg
            imgUrl = fileUrl;
            for (int i = 0; i < upload_file.length; i++) {
                String path = upload_file[i];
//                System.out.println("s = " + s);
                imgUrl+="/"+path;
            }
        }
        // 回显图片的路径 http://192.168.67.204/group1/M00/00/00/wKhDzFuBc3iAKTHYAACGx2c4tJ4846.jpg
        return imgUrl;
    }
}
