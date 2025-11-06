package icu.wenxin.grocery.business.domain.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import icu.wenxin.grocery.business.domain.dto.UnitCreateDTO;
import icu.wenxin.grocery.business.domain.dto.UnitUpdateDTO;
import icu.wenxin.grocery.business.domain.vo.AdminVO;
import icu.wenxin.grocery.business.domain.vo.UnitVO;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AutoMappers({
    @AutoMapper(target = AdminVO.class)
})
public class Admin {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String nickname;
    private String username;
    private String password;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateAt;
}
