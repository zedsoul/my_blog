package com.example.blogserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.blogserver.Vo.HistoryVo;
import com.example.blogserver.entity.History;
import com.example.blogserver.entity.Message;
import com.example.blogserver.entity.QueryPageBean;
import com.example.blogserver.mapper.HistoryMapper;
import com.example.blogserver.service.IHistoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zlc
 * @since 2024-01-02
 */
@Service
public class HistoryServiceImpl extends ServiceImpl<HistoryMapper, History> implements IHistoryService {
@Autowired
HistoryMapper historyMapper;

    @Override
    public boolean addRecord(History history) {
        return  save(history);
    }

    @Override
    public Page<HistoryVo> Records(QueryPageBean queryPageBean) {
        String uid = queryPageBean.getQueryString();
        Integer pageSize = queryPageBean.getPageSize();
        Integer currentPage = queryPageBean.getCurrentPage();
        int offset = pageSize * (currentPage - 1);
        List<HistoryVo> records = historyMapper.Records(offset, pageSize, uid);
        Page<HistoryVo> page = new Page<>(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        page.setRecords(records);
        page.setTotal(count(new LambdaQueryWrapper<History>().eq(History::getUid,uid)));

        return page;
    }
}
