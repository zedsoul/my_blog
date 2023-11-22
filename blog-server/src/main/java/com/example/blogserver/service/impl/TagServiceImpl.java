package com.example.blogserver.service.impl;

import com.example.blogserver.entity.Tag;
import com.example.blogserver.mapper.TagMapper;
import com.example.blogserver.service.ITagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 标签 服务实现类
 * </p>
 *
 * @author zlc
 * @since 2023-11-22
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {

}
