package com.example.blogserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.blogserver.entity.Link;
import com.example.blogserver.entity.QueryPageBean;
import com.example.blogserver.mapper.LinkMapper;
import com.example.blogserver.service.ILinkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlc.blogcommon.constant.CommonConst;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zlc
 * @since 2023-12-07
 */
@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements ILinkService {
    @Resource
    private LinkMapper linkDao;

    public List<String> getLinkList(long userId) {
        QueryWrapper<Link> wrapper = new QueryWrapper<>();
        wrapper.eq("status", CommonConst.SHOW_LINKS)
                .eq("user_id",userId)
                .select("link_name");
        return linkDao.selectList(wrapper).stream().map(
                Link::getLinkName).collect(Collectors.toList());
    }


    public boolean addLink(Link link) {
        int i = linkDao.insert(link);
        return i == 1;
    }


    public Page<Link> listLink(QueryPageBean queryPageBean) {
        Page<Link> page = new Page<>(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        QueryWrapper<Link> linkQueryWrapper = new QueryWrapper<>();
        linkQueryWrapper.like(queryPageBean.getQueryString() != null, "link_name", queryPageBean.getQueryString());
        return linkDao.selectPage(page, linkQueryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateLinkDisable(Link link) {
        linkDao.updateById(link);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateFriendLink(Link link) {
        this.saveOrUpdate(link);
    }
}
