package com.example.blogserver.Controller.admin;

import com.example.blogserver.Utils.JWTUtils;
import com.example.blogserver.Utils.WebUtil;
import com.example.blogserver.annotation.LoginRequired;
import com.example.blogserver.annotation.OptLog;
import com.example.blogserver.service.ReportService;
import com.zlc.blogcommon.po.User;
import com.zlc.blogcommon.po.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.zlc.blogcommon.constant.OptTypeConst.Get;
import static com.zlc.blogcommon.constant.OptTypeConst.SAVE_OR_UPDATE;


/**
 * 后台数据统计模块
 *
 * @author fangjiale 2021年02月17日
 */

@Api(value = "后台数据统计模块", description = "后台数据统计模块的接口信息")
@RequestMapping("/report")
@RestController
@CrossOrigin
public class ReportController {
    @Resource
    private ReportService reportService;



    @LoginRequired
    @OptLog(optType = Get)
    @GetMapping("/admin/getReport")
    @ApiOperation(value = "获取数据统计模块1")
    public R getReport() throws Exception {
        String uid= JWTUtils.getTokenInfo(WebUtil.getHeader("jj-auth")).getClaim("id").asString();
        return R.data( reportService.getReport(Long.valueOf(uid)),"获取博文数据成功!");
    }

    @LoginRequired
    @GetMapping("/admin/getReport2")

    public R getReport2(HttpServletRequest request) throws Exception {
        String uid=JWTUtils.getTokenInfo(WebUtil.getHeader("jj-auth")).getClaim("id").asString();
        return R.data( reportService.getReport2(Long.valueOf(uid)),"获取单篇博文分析数据成功!");
    }
}
