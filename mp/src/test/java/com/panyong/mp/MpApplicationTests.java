package com.panyong.mp;

import com.baomidou.mybatisplus.core.toolkit.AES;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest/*(args = "--mpw.key=bq3rh977HcYkvAC2")*/
class MpApplicationTests {
    
    @Test
    void contextLoads() {
        // 1. 生成16为随机的AES秘钥
        String randomKey = AES.generateRandomKey();
        System.out.println("randomKey = " + randomKey);
        
        // 2. 利用秘钥对用户名加密
        String username = AES.encrypt("root", randomKey);
        System.out.println("username = " + username);
        
        // 3. 利用秘钥对用户密码加密
        String password = AES.encrypt("javamysql", randomKey);
        System.out.println("password = " + password);
        
        /*
            randomKey = bq3rh977HcYkvAC2
            username = 9ibqiqKgwyp+au2cC9rpGA==
            password = OXF7TvdDM5T4eTEKpDI5UQ==
            
            在项目启动的时候，添加AES的秘钥，这样MyBatisPlus就可以解密数据了
            
            单元测试时，可以通过@SpringBootTest来指定秘钥
            
            Idea中则可以通过Program arguments来配置
*/
    }
    
}
