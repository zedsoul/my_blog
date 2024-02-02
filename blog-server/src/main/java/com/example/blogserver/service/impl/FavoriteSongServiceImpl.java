package com.example.blogserver.service.impl;

import com.example.blogserver.entity.FavoriteSong;
import com.example.blogserver.mapper.FavoriteSongMapper;
import com.example.blogserver.service.IFavoriteSongService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zlc
 * @since 2024-02-02
 */
@Service
public class FavoriteSongServiceImpl extends ServiceImpl<FavoriteSongMapper, FavoriteSong> implements IFavoriteSongService {

}
