package icu.wenxin.grocery.business.controller;

import icu.wenxin.grocery.common.resp.R;
import icu.wenxin.grocery.business.domain.po.Unit;
import icu.wenxin.grocery.business.service.UnitService;
import icu.wenxin.grocery.business.domain.dto.UnitCreateDTO;
import icu.wenxin.grocery.business.domain.dto.UnitUpdateDTO;
import icu.wenxin.grocery.business.domain.vo.UnitVO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
    public R<List<UnitVO>> getUnitList() {
        return unitService.getUnitList();
    }

    // 新增单位
    @PostMapping
    public R<String> addUnit(@Valid @RequestBody UnitCreateDTO unitCreateDTO) {
        return unitService.addUnit(unitCreateDTO);
    }

    // 修改单位
    @PutMapping
    public R<String> updateUnit(@Valid @RequestBody UnitUpdateDTO unitUpdateDTO) {
        return unitService.updateUnit(unitUpdateDTO);
    }

    // 删除单位
    @DeleteMapping("/{id}")
    public R<String> deleteUnit(@PathVariable @NotNull(message = "ID不能为空") Integer id) {
        return unitService.deleteUnit(id);
    }


}
