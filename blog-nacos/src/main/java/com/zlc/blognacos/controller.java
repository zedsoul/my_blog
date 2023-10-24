package com.zlc.blognacos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RefreshScope
@RestController
@RequestMapping("/service")
public class controller {

    @Autowired
    user user;

        @GetMapping("/111/test")
        public String providerTest() {
            return "我是provider，已成功获取nacos配置中心的数据：(id:" + user.getId() + ",name:" + user.getName() + ",age:" + user.getAge() + ")";
        }
    }


