package icu.wenxin.grocery.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import icu.wenxin.grocery.business.domain.po.Unit;
import icu.wenxin.grocery.business.domain.dto.UnitCreateDTO;
import icu.wenxin.grocery.business.domain.dto.UnitUpdateDTO;
import icu.wenxin.grocery.business.domain.vo.UnitVO;
import icu.wenxin.grocery.common.resp.R;

import java.util.List;

public interface UnitService extends IService<Unit> {
    R<List<UnitVO>> getUnitList();
    R<String> addUnit(UnitCreateDTO unitCreateDTO);
    R<String> updateUnit(UnitUpdateDTO unitUpdateDTO);
    R<String> deleteUnit(Integer id);
}
