package icu.wenxin.grocery.controller;

import icu.wenxin.grocery.common.resp.R;
import icu.wenxin.grocery.domain.po.Category;
import icu.wenxin.grocery.service.CategoryService;
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
    public R<List<Category>> getCategoryList() {
        return R.ok(categoryService.list());
    }

    // 新增分类
    @PostMapping
    public R<String> addCategory(@RequestBody Category category) {
        return categoryService.save(category) ? R.ok() : R.error();
    }

    // 修改分类
    @PutMapping
    public R<String> updateCategory(@RequestBody Category category) {
        return categoryService.updateById(category) ? R.ok() : R.error();
    }

    // 删除分类
    @DeleteMapping("/{id}")
    public R<String> deleteCategory(@PathVariable Integer id) {
        return categoryService.removeById(id) ? R.ok() : R.error();
    }
}
