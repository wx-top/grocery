package icu.wenxin.grocery.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.wenxin.grocery.domain.po.Category;
import icu.wenxin.grocery.mapper.CategoryMapper;
import icu.wenxin.grocery.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
}
