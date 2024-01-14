package com.example.blogserver.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.blogserver.entity.QueryPageBean;
import com.example.blogserver.entity.Tip;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zlc
 * @since 2024-01-06
 */
public interface ITipService extends IService<Tip> {

    boolean addMessage(Tip tip);

    Page Messages(QueryPageBean queryPageBean);
}
