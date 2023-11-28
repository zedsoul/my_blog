package com.example.blogserver.service;

import com.example.blogserver.entity.Views;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zlc.blogcommon.dto.ViewsDTO;

import java.util.List;

/**
 * <p>
 * 博客浏览量记录表 服务类
 * </p>
 *
 * @author zlc
 * @since 2023-11-22
 */
public interface IViewsService extends IService<Views> {

    /**
     * 获取七天全站浏览量数据
     * @return list
     */
    List<ViewsDTO> getViewsData();
}
