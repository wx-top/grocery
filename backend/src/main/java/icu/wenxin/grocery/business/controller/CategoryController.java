package icu.wenxin.grocery.business.controller;

import icu.wenxin.grocery.common.resp.R;
import icu.wenxin.grocery.business.domain.po.Category;
import icu.wenxin.grocery.business.service.CategoryService;
import icu.wenxin.grocery.business.domain.dto.CategoryCreateDTO;
import icu.wenxin.grocery.business.domain.dto.CategoryUpdateDTO;
import icu.wenxin.grocery.business.domain.vo.CategoryVO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    // 获取所有分类
    @GetMapping
    public R<List<CategoryVO>> getCategoryList() {
        return categoryService.getCategoryList();
    }

    // 新增分类
    @PostMapping
    public R<String> addCategory(@Valid @RequestBody CategoryCreateDTO categoryCreateDTO) {
        return categoryService.addCategory(categoryCreateDTO);
    }

    // 修改分类
    @PutMapping
    public R<String> updateCategory(@Valid @RequestBody CategoryUpdateDTO categoryUpdateDTO) {
        return categoryService.updateCategory(categoryUpdateDTO);
    }

    // 删除分类
    @DeleteMapping("/{id}")
    public R<String> deleteCategory(@PathVariable @NotNull(message = "ID不能为空") Integer id) {
        return categoryService.deleteCategory(id);
    }
}
