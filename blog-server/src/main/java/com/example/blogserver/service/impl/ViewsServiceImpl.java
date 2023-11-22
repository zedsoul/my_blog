package com.example.blogserver.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.example.blogserver.entity.Views;
import com.example.blogserver.mapper.ViewsMapper;
import com.example.blogserver.service.IViewsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlc.blogcommon.dto.ViewsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 博客浏览量记录表 服务实现类
 * </p>
 *
 * @author zlc
 * @since 2023-11-22
 */
@Service
public class ViewsServiceImpl extends ServiceImpl<ViewsMapper, Views> implements IViewsService {
    @Autowired
    ViewsMapper viewsMapper;

    @Cacheable(value = {"viewsData"}, key = "#root.methodName")
    public List<ViewsDTO> getViewsData() {
        DateTime startTime = DateUtil.beginOfDay(DateUtil.offsetDay(new Date(), -7));
        DateTime endTime = DateUtil.endOfDay(new Date());
        return viewsMapper.getViewsData(startTime, endTime);
    }
}
