import { defineStore } from "pinia";
import { fetchGetCategoryList, fetchGetUnitList, fetchGetAdminInfo, fetchUpdatePassword, fetchLoginAdmin } from "@/api";
export const useAppStore = defineStore("app", {
    state: () => {
        return {
            admin: null as Admin | null,
            token: uni.getStorageSync('token') || '',
            tabbarIndex: 0,
            unitList: [] as Unit[],
            categoryList: [] as Category[],
            needRefreshProduct: false,
        }
    },
    actions: {
        async getUnitList() {
            try {
                const data = await fetchGetUnitList();
                this.unitList = data || [];
            } catch (error: any) {
                uni.showToast({
                    title: error.msg || '获取单位列表失败',
                    icon: 'error',
                })
            }
        },
        async getCategoryList() {
            try {
                const data = await fetchGetCategoryList();
                this.categoryList = data || [];
            } catch (error: any) {
                uni.showToast({
                    title: error.msg || '获取分类列表失败',
                    icon: 'error',
                })
            }
        },
        async getAdminInfo() {
            try {
                const data = await fetchGetAdminInfo();
                this.admin = data || null;
            } catch (error: any) {
                uni.showToast({
                    title: error.msg || '获取管理员信息失败',
                    icon: 'error',
                })
                this.logoutAdmin();
            }
        },
        async loginAdmin(data: AdminLoginRequest) {
            try {
                const res = await fetchLoginAdmin(data);
                this.token = res.token || '';
                this.setAdmin(res.admin || null);
                uni.setStorageSync('token', this.token);
                return true
            } catch (error: any) {
                // 抛出异常
                throw error;
            }
        },
        async updatePassword(data: AdminUpdatePasswordRequest) {
            try {
                await fetchUpdatePassword(data);
                return true
            } catch (error: any) {
                // 抛出异常
                throw error;
            }
        },
        logoutAdmin() {
            this.token = '';
            this.setAdmin(null);
            uni.removeStorageSync('token');
        },
        getUnitById(id: number) {
            return this.unitList.find(item => item.id === id);
        },
        getCategoryById(id: number) {
            return this.categoryList.find(item => item.id === id);
        },
        setNeedRefreshProduct(value: boolean) {
            this.needRefreshProduct = value;
        },
        setAdmin(admin: Admin | null) {
            this.admin = admin;
        },
        setToken(token: string) {
            this.token = token;
            uni.setStorageSync('token', token);
        },
    },
    getters: {
        isLogin: (state) => {
            return state.token !== ''
        },
    }
})