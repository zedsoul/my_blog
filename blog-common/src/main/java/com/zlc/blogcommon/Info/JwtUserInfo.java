package com.zlc.blogcommon.Info;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;


@Data
public class JwtUserInfo {

    private Long uid;

    private String username;

    private String nickname;

    private Integer roleId;

}
