package com.example.blogserver.Controller.admin;


import com.example.blogserver.annotation.OptLog;
import com.example.blogserver.entity.QueryPageBean;
import com.example.blogserver.service.ITbOperationLogService;
import com.zlc.blogcommon.constant.OptTypeConst;
import com.zlc.blogcommon.po.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 日志控制器
 */
@Api(value = "日志模块")
@RequestMapping("/logs")
@RestController
@CrossOrigin
public class LogController {
    @Resource
    private ITbOperationLogService operationLogService;

    /**
     * 查看操作日志
     *
     * @param queryPageBean 条件
     * @return {@link } 日志列表
     */
    @OptLog(optType = OptTypeConst.Get)
    @ApiOperation(value = "查看操作日志")
    @GetMapping("/admin/operation")
    public  R listOperationLogs(QueryPageBean queryPageBean) {
        return R.data( operationLogService.listOperationLogs(queryPageBean),"获取日志列表成功");
    }

    /**
     * 删除操作日志
     *
     * @param logIdList 日志id列表
     * @return {@link }
     */
    @OptLog(optType = OptTypeConst.REMOVE)
    @ApiOperation(value = "删除操作日志")
    @PostMapping("/admin/delete")
    public R deleteOperationLogs(@RequestBody  List<Integer> logIdList) {
        operationLogService.removeByIds(logIdList);
        return R.data("删除成功");
    }

}
