package icu.wenxin.grocery.business.controller;
import icu.wenxin.grocery.business.domain.dto.AdminLoginDTO;
import icu.wenxin.grocery.business.domain.dto.AdminUpdatePwdDTO;
import icu.wenxin.grocery.business.domain.po.Admin;
import icu.wenxin.grocery.business.domain.vo.AdminVO;
import icu.wenxin.grocery.business.domain.vo.LoginVO;
import icu.wenxin.grocery.business.service.AdminService;
import icu.wenxin.grocery.common.resp.R;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping
    public R<AdminVO> getAdminInfo(HttpServletRequest request) {
        return adminService.getAdminInfo(request);
    }

    @PostMapping("/login")
    public R<LoginVO> login(@Valid @RequestBody AdminLoginDTO adminCheckDTO) {
        return adminService.login(adminCheckDTO);
    }

    @PostMapping("/updatePwd")
    public R<String> updatePwd(@Valid @RequestBody AdminUpdatePwdDTO updatePwdDTO, HttpServletRequest request) {
        return adminService.updatePwd(updatePwdDTO, request);
    }
}
