import { useAppStore } from "@/store";

// 全局请求封装
// const base_url = 'http://192.168.43.34:8080'
// const base_url = 'http://192.168.20.180:8080'
const base_url = 'https://grocery.wenxin.icu'
// const base_url = 'http://localhost:8081'
const upload_url = `${base_url}/minio/upload`

// 请求超出时间
const timeout = 5000

// 需要修改token，和根据实际修改请求头
const request = <T = any>(params: RequestOptions): Promise<T> => {
    let url = params.url;
    // 统一为大写以匹配 uni.request 的类型定义
    type HttpMethod = 'OPTIONS' | 'GET' | 'HEAD' | 'POST' | 'PUT' | 'DELETE' | 'TRACE' | 'CONNECT';
    let method: HttpMethod = (params.method || "get").toUpperCase() as HttpMethod;
    let data = params.data || {};
    let header: Record<string, any> = {
        'Content-Type': 'application/json;charset=UTF-8',
        ...params.header
    }
    // 处理token
    const token = uni.getStorageSync('token');
    if (token) {
        header['Token'] = token;
    }
    const appStore = useAppStore();
    return new Promise<T>((resolve, reject) => {
        uni.showLoading({
            title: '加载中...',
        })
        uni.request({
            url: base_url + url,
            method: method,
            header: header,
            data: data,
            timeout,
            success(response) {
                const res = response
                if (res.statusCode == 200) {
                    const r = res.data as R<T>;
                    if (r.code == 200) {
                        resolve(r.data);
                    } else {
                        // 业务错误，reject错误信息
                        reject(r);
                    }
                } else if (res.statusCode == 401) {
                    uni.showToast({
                        title: '登录过期，请重新登录',
                        icon: "error",
                        duration: 2000
                    })
                    appStore.setAdmin(null);
                    uni.removeStorageSync('token');
                    reject(res.data);
                } else {
                    reject(res.data);
                }
            },
            fail(err) {
                if (err.errMsg.indexOf('request:fail') !== -1) {
                    uni.showToast({
                        title: '网络异常',
                        icon: "error",
                        duration: 2000
                    })
                } else {
                    uni.showToast({
                        title: '未知异常',
                        duration: 2000
                    })
                }
                reject(err);
            },
            complete() {
                // 不管成功还是失败都会执行
                uni.hideLoading();
                uni.hideToast();
            }
        });
    });
};

export {
    request,
    base_url,
    upload_url
};
