package com.example.blogserver.service.impl;

import com.example.blogserver.entity.Type;
import com.example.blogserver.mapper.TypeMapper;
import com.example.blogserver.service.ITypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 类型 服务实现类
 * </p>
 *
 * @author zlc
 * @since 2023-11-22
 */
@Service
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Type> implements ITypeService {

}
