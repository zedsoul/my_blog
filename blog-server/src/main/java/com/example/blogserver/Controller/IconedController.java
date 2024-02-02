package com.example.blogserver.Controller;


import com.example.blogserver.Utils.JWTUtils;
import com.example.blogserver.Utils.WebUtil;
import com.example.blogserver.annotation.OptLog;
import com.example.blogserver.entity.Iconed;
import com.example.blogserver.service.impl.IconedServiceImpl;
import com.zlc.blogcommon.constant.OptTypeConst;
import com.zlc.blogcommon.po.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zlc
 * @since 2024-01-31
 */
@Api(value = "图标模块")
@RestController
@RequestMapping("/iconed")
public class IconedController {

    @Autowired
    IconedServiceImpl iconedService;
    @OptLog(optType = OptTypeConst.UPDATE)
    @ApiOperation(value = "更新用户信息")
    @GetMapping("/admin/geticon")
    public R getIconed(){
        String id= JWTUtils.getTokenInfo(WebUtil.getHeader("jj-auth")).getClaim("id").asString();
        Iconed byId = iconedService.getById(id);
        return R.data(byId,"获取图标连接成功！");

    }

}
