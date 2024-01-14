package com.example.blogserver.Controller;


import com.example.blogserver.Utils.JWTUtils;
import com.example.blogserver.Utils.WebUtil;
import com.example.blogserver.annotation.OptLog;
import com.example.blogserver.entity.History;
import com.example.blogserver.entity.QueryPageBean;
import com.example.blogserver.service.IHistoryService;
import com.zlc.blogcommon.constant.OptTypeConst;
import com.zlc.blogcommon.po.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zlc
 * @since 2024-01-02
 */
@RestController
@RequestMapping("/history")
@Api(value = "归档模块", description = "归档模块的接口信息")
public class HistoryController {
    @Autowired
    IHistoryService historyService;

    @PostMapping("/addrecords")
    @OptLog(optType = OptTypeConst.SAVE)
    @ApiOperation(value = "归档添加记录")
    public R addRecord(  @RequestBody  History history){
      String uid= JWTUtils.getTokenInfo(WebUtil.getHeader("jj-auth")).getClaim("id").asString();
     history.setUid(Long.valueOf(uid));
     if( historyService.addRecord(history)){
         return R.data("历史记录+1");
     }
     return R.fail("添加记录出错了！");
  }
    @ApiOperation(value = "归档查询记录")
    @OptLog(optType = OptTypeConst.Get)
  @GetMapping("/selectRecord")
  public R selectRecord( QueryPageBean queryPageBean){
      String uid= JWTUtils.getTokenInfo(WebUtil.getHeader("jj-auth")).getClaim("id").asString();
      queryPageBean.setQueryString(uid);
      return R.data( historyService.Records( queryPageBean),"查询历史记录成功");
  }

}
