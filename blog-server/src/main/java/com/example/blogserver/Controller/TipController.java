package com.example.blogserver.controller;


import com.example.blogserver.Utils.JWTUtils;
import com.example.blogserver.Utils.WebUtil;
import com.example.blogserver.annotation.OptLog;
import com.example.blogserver.entity.History;
import com.example.blogserver.entity.QueryPageBean;
import com.example.blogserver.entity.Tip;
import com.example.blogserver.service.IHistoryService;
import com.example.blogserver.service.ITipService;
import com.zlc.blogcommon.constant.OptTypeConst;
import com.zlc.blogcommon.po.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zlc
 * @since 2024-01-06
 */
@RestController
@RequestMapping("/tip")
@Api(value = "消息中心模块", description = "消息中心模块的接口信息")
public class TipController {
    @Autowired
    ITipService tipService;

    @PostMapping("/addmessage")
    @OptLog(optType = OptTypeConst.SAVE)
    @ApiOperation(value = "消息中心添加记录")
    public R addMessage(@RequestBody Tip tip){
        String uid= JWTUtils.getTokenInfo(WebUtil.getHeader("jj-auth")).getClaim("id").asString();
        tip.setCreateTime(LocalDateTime.now());
        tip.setOtherId(Long.valueOf(uid));
        if( tipService.addMessage(tip)){
            return R.data("消息记录+1");
        }
        return R.fail("添加记录出错了！");
    }
    @ApiOperation(value = "消息中心查询记录")
    @OptLog(optType = OptTypeConst.Get)
    @GetMapping("/selectmessage")
    public R selectmessage( QueryPageBean queryPageBean){
        String uid= JWTUtils.getTokenInfo(WebUtil.getHeader("jj-auth")).getClaim("id").asString();
        queryPageBean.setQueryString(uid);
        return R.data( tipService.Messages( queryPageBean),"查询消息记录成功");
    }
}
