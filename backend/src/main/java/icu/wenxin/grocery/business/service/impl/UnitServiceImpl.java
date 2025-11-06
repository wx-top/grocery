package icu.wenxin.grocery.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.wenxin.grocery.business.domain.po.Unit;
import icu.wenxin.grocery.business.mapper.UnitMapper;
import icu.wenxin.grocery.business.service.UnitService;
import icu.wenxin.grocery.business.domain.dto.UnitCreateDTO;
import icu.wenxin.grocery.business.domain.dto.UnitUpdateDTO;
import icu.wenxin.grocery.business.domain.vo.UnitVO;
import icu.wenxin.grocery.common.resp.R;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UnitServiceImpl extends ServiceImpl<UnitMapper, Unit> implements UnitService {

    private final Converter converter;

    @Override
    public R<List<UnitVO>> getUnitList() {
        List<UnitVO> list = this.list().stream()
                .map(item -> converter.convert(item, UnitVO.class))
                .toList();
        return R.ok(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<String> addUnit(UnitCreateDTO unitCreateDTO) {
        boolean exists = lambdaQuery().eq(Unit::getName, unitCreateDTO.getName()).exists();
        if (exists) {
            return R.error("单位已存在");
        }
        Unit unit = converter.convert(unitCreateDTO, Unit.class);
        save(unit);
        return R.ok("添加成功", null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<String> updateUnit(UnitUpdateDTO unitUpdateDTO) {
        Unit dbUnit = getById(unitUpdateDTO.getId());
        if (dbUnit == null) {
            return R.error("单位不存在");
        }
        Unit unit = converter.convert(unitUpdateDTO, dbUnit);
        updateById(unit);
        return R.ok("修改成功", null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<String> deleteUnit(Integer id) {
        boolean exists = lambdaQuery().eq(Unit::getId, id).exists();
        if (!exists) {
            return R.error("单位不存在");
        }
        return removeById(id) ? R.ok("删除成功", null) : R.error("删除失败");
    }
}
