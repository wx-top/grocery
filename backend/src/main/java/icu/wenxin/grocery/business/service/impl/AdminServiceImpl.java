package icu.wenxin.grocery.business.service.impl;

import cn.hutool.jwt.JWT;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.wenxin.grocery.business.domain.dto.AdminLoginDTO;
import icu.wenxin.grocery.business.domain.dto.AdminUpdatePwdDTO;
import icu.wenxin.grocery.business.domain.po.Admin;
import icu.wenxin.grocery.business.domain.vo.AdminVO;
import icu.wenxin.grocery.business.domain.vo.LoginVO;
import icu.wenxin.grocery.business.mapper.AdminMapper;
import icu.wenxin.grocery.business.service.AdminService;
import icu.wenxin.grocery.common.resp.R;
import io.github.linpeilie.Converter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
    private final Converter converter;

    @Value("${jwt.secret}")
    private String secret = "grocery-wenxin";

    @Override
    public R<LoginVO> login(AdminLoginDTO adminCheckDTO) {
        Admin admin = lambdaQuery().eq(Admin::getUsername, adminCheckDTO.getUsername()).one();
        if (admin == null) {
            return R.error("管理员不存在");
        }
        if (!admin.getPassword().equals(adminCheckDTO.getPassword())) {
            return R.error("密码错误");
        }
        AdminVO adminVO = converter.convert(admin, AdminVO.class);
        Date expiryDate = Date.from(Instant.now().plus(30, ChronoUnit.DAYS)); // 精确30天
        String jwt = JWT.create()
                .setPayload("adminId", admin.getId())
                .setPayload("username", admin.getUsername())
                .setExpiresAt(expiryDate)
                .setKey(secret.getBytes())
                .sign();
        LoginVO loginVO = new LoginVO();
        loginVO.setAdmin(adminVO);
        loginVO.setToken(jwt);
        return R.ok("登录成功", loginVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<String> updatePwd(AdminUpdatePwdDTO updatePwdDTO, HttpServletRequest request) {
        Object adminId = request.getAttribute("adminId");
        int id = Integer.parseInt(adminId.toString());
        Admin admin = getById(id);
        if (admin == null) {
            return R.error("管理员不存在");
        }
        if (!admin.getPassword().equals(updatePwdDTO.getOldPwd())) {
            return R.error("旧密码错误");
        }
        admin.setPassword(updatePwdDTO.getNewPwd());
        updateById(admin);
        return R.ok("修改成功", null);
    }

    @Override
    public R<AdminVO> getAdminInfo(HttpServletRequest request) {
        Object adminId = request.getAttribute("adminId");
        int id = Integer.parseInt(adminId.toString());
        Admin admin = getById(id);
        if (admin == null) {
            return R.error("管理员不存在");
        }
        AdminVO adminVO = converter.convert(admin, AdminVO.class);
        return R.ok(adminVO);
    }
}
