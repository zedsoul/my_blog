package com.example.blogserver.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.blogserver.Vo.HistoryVo;
import com.example.blogserver.entity.History;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blogserver.entity.QueryPageBean;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zlc
 * @since 2024-01-02
 */
public interface IHistoryService extends IService<History> {


    /**
     * 添加记录
     * @param history
     */
    boolean  addRecord(History history);


    /**
     * 查询根据uid记录
     *
     * @return {@link List}<{@link HistoryVo}>
     */
    Page<HistoryVo> Records(QueryPageBean queryPageBean );
}
