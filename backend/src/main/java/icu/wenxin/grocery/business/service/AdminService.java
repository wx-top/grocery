package icu.wenxin.grocery.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import icu.wenxin.grocery.business.domain.dto.AdminLoginDTO;
import icu.wenxin.grocery.business.domain.dto.AdminUpdatePwdDTO;
import icu.wenxin.grocery.business.domain.po.Admin;
import icu.wenxin.grocery.business.domain.vo.AdminVO;
import icu.wenxin.grocery.business.domain.vo.LoginVO;
import icu.wenxin.grocery.common.resp.R;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

public interface AdminService extends IService<Admin> {
    R<LoginVO> login(AdminLoginDTO adminService);

    R<String> updatePwd(AdminUpdatePwdDTO updatePwdDTO, HttpServletRequest request);

    R<AdminVO> getAdminInfo(HttpServletRequest request);
}
