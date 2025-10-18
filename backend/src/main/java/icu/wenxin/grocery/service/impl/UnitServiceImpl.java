package icu.wenxin.grocery.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.wenxin.grocery.domain.po.Unit;
import icu.wenxin.grocery.mapper.UnitMapper;
import icu.wenxin.grocery.service.UnitService;
import org.springframework.stereotype.Service;

@Service
public class UnitServiceImpl extends ServiceImpl<UnitMapper, Unit> implements UnitService {
}
