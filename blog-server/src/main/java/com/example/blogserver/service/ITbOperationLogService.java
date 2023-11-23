package com.example.blogserver.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.blogserver.entity.QueryPageBean;
import com.example.blogserver.entity.TbOperationLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zlc.blogcommon.dto.OperationLogDTO;

/**
 * <p>
 * 日志记录 服务类
 * </p>
 *
 * @author zlc
 * @since 2023-11-23
 */
public interface ITbOperationLogService extends IService<TbOperationLog> {

    /**
     * 查询日志列表
     *
     * @param queryPageBean 条件
     * @return 日志列表
     */
    Page<OperationLogDTO> listOperationLogs(QueryPageBean queryPageBean);
}
