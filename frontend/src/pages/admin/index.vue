<template>
    <view class="admin-container">
        <!-- 当前管理员状态 -->
        <view class="status-section">
            <wd-cell-group title="管理员状态" border>
                <wd-cell title="当前状态" :value="isLogin ? '已登录' : '未登录'"></wd-cell>
            </wd-cell-group>
        </view>

        <!-- 管理员信息 -->
        <view class="admin-info-section" v-if="isLogin && appStore.admin">
            <wd-cell-group title="管理员信息" border>
                <wd-cell title="昵称" :value="appStore.admin.nickname || '未设置'"></wd-cell>
                <wd-cell title="用户名" :value="appStore.admin.username || '未知'"></wd-cell>
            </wd-cell-group>
        </view>

        <!-- 管理员验证表单 -->
        <view class="auth-section" v-if="!isLogin">
            <wd-cell-group title="管理员登录" border>
                <wd-form ref="form" :model="adminForm">
                    <wd-input v-model="adminForm.username" label="管理员账号" placeholder="请输入管理员账号" clearable
                        prop="username" />
                    <wd-input label="管理员密码" clearable v-model="adminForm.password" show-password placeholder="请输入管理员密码"
                        prop="password" />
                </wd-form>
            </wd-cell-group>
            <view class="auth-actions">
                <wd-button type="primary" size="large" :loading="isLoading" @click="handleAdminAuth" block>
                    {{ isLoading ? '登录中...' : '登录管理员账号' }}
                </wd-button>
            </view>
        </view>

        <!-- 管理员操作区域 -->
        <view class="admin-actions" v-if="isLogin">
            <wd-cell-group title="管理员操作" border>
                <wd-cell title="修改密码" is-link @click="changePassword = !changePassword">
                </wd-cell>
                <view v-if="changePassword">
                    <wd-form ref="form" :model="updatePwdForm">
                        <wd-input v-model="updatePwdForm.oldPwd" label="旧密码" placeholder="请输入旧密码" clearable
                            prop="oldPwd" />
                        <wd-input label="新密码" clearable v-model="updatePwdForm.newPwd" show-password
                            placeholder="请输入新密码" prop="newPwd" />
                    </wd-form>
                    <view class="auth-actions">
                        <wd-button type="primary" size="large" :loading="isLoading" @click="handleChangePassword" block>
                            {{ isLoading ? '修改中...' : '修改密码' }}
                        </wd-button>
                    </view>
                </view>
                <wd-cell title="退出登录" is-link @click="handleLogout">
                </wd-cell>
            </wd-cell-group>
        </view>
        <wd-toast />
        <wd-message-box />
        <MyTabbar />
    </view>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { useMessage, useToast } from 'wot-design-uni'
import { useAppStore } from '@/store/modules/app'
import MyTabbar from '@/components/MyTabbar/MyTabbar.vue'

const message = useMessage()
const appStore = useAppStore()
const changePassword = ref(false)
const isLoading = ref(false)
const toast = useToast()

const adminForm = reactive({
    username: '',
    password: ''
})

const updatePwdForm = reactive({
    oldPwd: '',
    newPwd: ''
})

const form = ref()

// 计算属性：是否为管理员
const isLogin = computed(() => appStore.isLogin)



onMounted(async () => {
    if (isLogin.value) {
        await appStore.getAdminInfo()
    }
})

// 管理员身份验证
const handleAdminAuth = async () => {
    if (!form.value?.validate()) {
        return
    }
    isLoading.value = true
    try {
        await appStore.loginAdmin({
            username: adminForm.username,
            password: adminForm.password
        })
        toast.success('登录成功')
        // 清空表单
        adminForm.username = ''
        adminForm.password = ''
        isLoading.value = false
    } catch (error: any) {
        toast.error(error.msg || '登录失败')
    } finally {
        isLoading.value = false
    }
}

// 修改密码
const handleChangePassword = async () => {
    if (!form.value?.validate()) {
        return
    }
    isLoading.value = true
    try {
        await appStore.updatePassword({
            oldPwd: updatePwdForm.oldPwd,
            newPwd: updatePwdForm.newPwd
        })
        // 清空表单
        changePassword.value = false
        updatePwdForm.oldPwd = ''
        updatePwdForm.newPwd = ''
        toast.success('修改密码成功，请重新登录')
        appStore.logoutAdmin()
    } catch (error: any) {
        toast.error(error.msg || '修改密码失败')
    } finally {
        isLoading.value = false
    }
}

// 退出登录
const handleLogout = () => {
    message.confirm({
        title: '退出登录',
        msg: '确定要退出登录吗？'
    }).then(() => {
        appStore.logoutAdmin()
        toast.success('退出登录成功')
    }).catch(() => {
        // 用户取消
    })
}
</script>

<style lang="scss" scoped>
.admin-container {
    min-height: 100vh;
    background: #f5f5f5;
    padding-bottom: 40rpx;
}

.status-section,
.admin-info-section,
.auth-section,
.admin-actions,
.info-section {
    margin-bottom: 20rpx;
}

.auth-actions {
    padding: 40rpx;
}

.status-section {
    .wd-cell-group {
        background: #fff;
    }
}

.admin-info-section {
    .wd-cell-group {
        background: #fff;
    }
}

.auth-section {
    .wd-cell-group {
        background: #fff;
    }
}

.admin-actions {
    .wd-cell-group {
        background: #fff;
    }
}

.info-section {
    .wd-cell-group {
        background: #fff;
    }
}
</style>