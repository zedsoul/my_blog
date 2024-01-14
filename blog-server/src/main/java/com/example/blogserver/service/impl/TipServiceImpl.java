package com.example.blogserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.blogserver.Vo.HistoryVo;
import com.example.blogserver.Vo.TipVO;
import com.example.blogserver.entity.History;
import com.example.blogserver.entity.QueryPageBean;
import com.example.blogserver.entity.Tip;
import com.example.blogserver.mapper.TipMapper;
import com.example.blogserver.service.ITipService;
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
 * @since 2024-01-06
 */
@Service
public class TipServiceImpl extends ServiceImpl<TipMapper, Tip> implements ITipService {
    @Autowired
    TipMapper tipMapper;

    @Override
    public boolean addMessage(Tip tip) {
        tip.setMyselfId(tipMapper.myselfName(tip.getBlogId()).get(0));
        return save(tip);
    }

    @Override
    public Page<TipVO> Messages(QueryPageBean queryPageBean) {
        String uid = queryPageBean.getQueryString();
        Integer pageSize = queryPageBean.getPageSize();
        Integer currentPage = queryPageBean.getCurrentPage();
        int offset = pageSize * (currentPage - 1);
        List<TipVO> records = tipMapper.Records(offset, pageSize, uid);
        Page<TipVO> page = new Page<>(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        page.setRecords(records);
        page.setTotal(count(new LambdaQueryWrapper<Tip>().eq(Tip::getMyselfId,uid)));
        return page;
    }
}
