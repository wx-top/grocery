package icu.wenxin.grocery.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.wenxin.grocery.business.domain.po.Category;
import icu.wenxin.grocery.business.mapper.CategoryMapper;
import icu.wenxin.grocery.business.service.CategoryService;
import icu.wenxin.grocery.business.domain.dto.CategoryCreateDTO;
import icu.wenxin.grocery.business.domain.dto.CategoryUpdateDTO;
import icu.wenxin.grocery.business.domain.vo.CategoryVO;
import icu.wenxin.grocery.common.resp.R;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    private final Converter converter;

    @Override
    public R<List<CategoryVO>> getCategoryList() {
        List<CategoryVO> list = this.list().stream()
                .map(item -> converter.convert(item, CategoryVO.class))
                .toList();
        return R.ok(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<String> addCategory(CategoryCreateDTO categoryCreateDTO) {
        boolean exists = lambdaQuery().eq(Category::getName, categoryCreateDTO.getName()).exists();
        if (exists) {
            return R.error("分类已存在");
        }
        Category category = converter.convert(categoryCreateDTO, Category.class);
        save(category);
        return R.ok("添加成功", null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<String> updateCategory(CategoryUpdateDTO categoryUpdateDTO) {
        Category dbCategory = getById(categoryUpdateDTO.getId());
        if (dbCategory == null) {
            return R.error("分类不存在");
        }
        Category category = converter.convert(categoryUpdateDTO, dbCategory);
        updateById(category);
        return R.ok("修改成功", null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<String> deleteCategory(Integer id) {
        boolean exists = lambdaQuery().eq(Category::getId, id).exists();
        if (!exists) {
            return R.error("分类不存在");
        }
        return removeById(id) ? R.ok("删除成功", null) : R.error("删除失败");
    }
}
