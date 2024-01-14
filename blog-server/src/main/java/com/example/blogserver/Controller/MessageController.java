package com.example.blogserver.Controller;


import com.example.blogserver.annotation.IpRequired;
import com.example.blogserver.annotation.OptLog;
import com.example.blogserver.entity.Message;
import com.example.blogserver.entity.QueryPageBean;
import com.example.blogserver.service.IMessageService;
import com.zlc.blogcommon.constant.OptTypeConst;
import com.zlc.blogcommon.po.result.R;
import com.zlc.blogcommon.po.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;



/**
 * <p>
 * 留言功能的前端控制器
 * </p>
 *
 * @author fangjiale
 * @since 2021-02-08
 */
@RestController
@Api(value = "留言模块", description = "留言模块的接口信息")
@RequestMapping("/message")
public class MessageController {
    @Resource
    private IMessageService messageService;

    @OptLog(optType = OptTypeConst.Get)
    @ApiOperation("获取留言列表")
    @GetMapping("/getMessageList")
    public R getMessageList() {
        return R.data( messageService.getMessageList(),"获取留言列表信息成功");
    }

    @OptLog(optType = OptTypeConst.Get)
    @ApiOperation("获取留言分页信息")
    @GetMapping("/getMessagePage")
    public R getMessagePage( QueryPageBean queryPageBean) {
        return R.data( messageService.getMessagePage(queryPageBean),"获取留言分页信息");
    }

    @OptLog(optType = OptTypeConst.SAVE)
    @ApiOperation("添加留言")
    @IpRequired
    @PostMapping("/add")
    public R addMessage( @RequestBody Message message, HttpServletRequest request) {
        message.setCreateTime(LocalDateTime.now());
        boolean flag = messageService.addMessage(message, (String) request.getAttribute("host"));
        if (flag) {
            return R.data("添加留言成功");
        } else {
            return R.fail("添加留言失败");
        }
    }

    @OptLog(optType = OptTypeConst.REMOVE)
    @ApiOperation("删除留言")
    @DeleteMapping("/admin/delete")
    public R deleteMessage(@RequestBody List<Long> messageIdList){
        messageService.deleteMessage(messageIdList);
        return R.data("删除成功");
    }

}

