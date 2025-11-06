import { request } from "@/utils/request";

export const fetchGetAdminInfo = () => {
    return request<Admin>({
        url: '/admin',
        method: 'get',
    })
}


export const fetchLoginAdmin = (data: AdminLoginRequest) => {
    return request<LoginVO>({
        url: '/admin/login',
        method: 'post',
        data,
    })
}

export const fetchUpdatePassword = (data: AdminUpdatePasswordRequest) => {
    return request<string>({
        url: '/admin/updatePwd',
        method: 'post',
        data,
    })
}