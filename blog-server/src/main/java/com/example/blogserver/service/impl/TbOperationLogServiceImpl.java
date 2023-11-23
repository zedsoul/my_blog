package com.example.blogserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.blogserver.Utils.BeanCopyUtils;
import com.example.blogserver.entity.QueryPageBean;
import com.example.blogserver.entity.TbOperationLog;
import com.example.blogserver.mapper.TbOperationLogMapper;
import com.example.blogserver.service.ITbOperationLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlc.blogcommon.dto.OperationLogDTO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 日志记录 服务实现类
 * </p>
 *
 * @author zlc
 * @since 2023-11-23
 */
@Service
public class TbOperationLogServiceImpl extends ServiceImpl<TbOperationLogMapper, TbOperationLog> implements ITbOperationLogService {


    @Override
    public Page<OperationLogDTO> listOperationLogs(QueryPageBean queryPageBean) {
        Page<TbOperationLog> page = new Page<>(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        // 查询日志列表
        Page<TbOperationLog> operationLogPage = this.page(page, new LambdaQueryWrapper<TbOperationLog>()
                .like(StringUtils.isNotBlank(queryPageBean.getQueryString()), TbOperationLog::getOptModule, queryPageBean.getQueryString())
                .or()
                .like(StringUtils.isNotBlank(queryPageBean.getQueryString()), TbOperationLog::getOptDesc, queryPageBean.getQueryString())
                .orderByDesc(TbOperationLog::getId));
        List<OperationLogDTO> operationLogDTOList = BeanCopyUtils.copyList(operationLogPage.getRecords(), OperationLogDTO.class);
        Page<OperationLogDTO> operationLogDTOPage = new Page<>();
        operationLogDTOPage.setRecords(operationLogDTOList);
        operationLogDTOPage.setTotal((int) operationLogPage.getTotal());
        return operationLogDTOPage;
    }
}
