package com.mia.miamall.passport;

import com.mia.miamall.passport.util.JwtUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MiaPassportWebApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void test01(){
        String key = "miamiall";
        String ip="192.168.1.150";
        Map map = new HashMap();
        map.put("userId","1001");
        map.put("nickName","zhangsan");
        String token = JwtUtil.encode(key, map, ip);
        System.out.println("--------------------token: "+token);
        Map<String, Object> decode = JwtUtil.decode(token, key, "192.168.1.150");
        System.out.println("--------------------decode: "+decode);
    }

}
