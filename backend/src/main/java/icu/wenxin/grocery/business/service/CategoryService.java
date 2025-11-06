package icu.wenxin.grocery.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import icu.wenxin.grocery.business.domain.po.Category;
import icu.wenxin.grocery.business.domain.dto.CategoryCreateDTO;
import icu.wenxin.grocery.business.domain.dto.CategoryUpdateDTO;
import icu.wenxin.grocery.business.domain.vo.CategoryVO;
import icu.wenxin.grocery.common.resp.R;

import java.util.List;

public interface CategoryService extends IService<Category> {
    R<List<CategoryVO>> getCategoryList();
    R<String> addCategory(CategoryCreateDTO categoryCreateDTO);
    R<String> updateCategory(CategoryUpdateDTO categoryUpdateDTO);
    R<String> deleteCategory(Integer id);
}
