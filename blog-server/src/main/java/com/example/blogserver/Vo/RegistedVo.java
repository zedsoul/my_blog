package com.example.blogserver.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Assinss
 * @date 2023/11/15
 * 注册包装类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistedVo {
    public RegistedVo(String email) {
        this.email = email;
    }
    String  email;
    String  username;
    String  password;
    Integer vertifyCode;
}
