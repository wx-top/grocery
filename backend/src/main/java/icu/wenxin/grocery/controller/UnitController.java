package icu.wenxin.grocery.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import icu.wenxin.grocery.common.resp.R;
import icu.wenxin.grocery.domain.po.Unit;
import icu.wenxin.grocery.service.UnitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/unit")
@RequiredArgsConstructor
public class UnitController {

    private final UnitService unitService;

    // 获取所有单位
    @GetMapping
    public R<List<Unit>> getUnitList() {
        return R.ok(unitService.list());
    }

    // 新增单位
    @PostMapping
    public R<String> addUnit(@RequestBody Unit unit) {
        return unitService.save(unit) ? R.ok() : R.error();
    }

    // 修改单位
    @PutMapping
    public R<String> updateUnit(@RequestBody Unit unit) {
        return unitService.updateById(unit) ? R.ok() : R.error();
    }

    // 删除单位
    @DeleteMapping("/{id}")
    public R<String> deleteUnit(@PathVariable Integer id) {
        return unitService.removeById(id) ? R.ok() : R.error();
    }


}
