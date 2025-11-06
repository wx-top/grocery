<template>
    <view class="valuation-container">
        <view class="camera-area">
            <camera device-position="back" resolution="high" flash="off" mode="scanCode" @error="handleError"
                @scancode="handleScanCode" style="width: 100%; height: 400rpx;"></camera>
        </view>

        <wd-popup v-model="showPopup" v-if="scanProduct" custom-style="border-radius:32rpx; width: 600rpx;">
            <view class="popup-content">
                <wd-swiper :list="scanProduct?.imageList?.map((item: ProductImage) => item.url) || []"
                    v-model:currentSwiper="currentSwiper" :indicator="{ type: 'fraction' }" indicatorPosition="bottom-right">
                    <template #default="{ item }">
                        <image style="height: 100%; width: 100%;" :src="item" mode="aspectFit" />
                    </template>
                </wd-swiper>
                <wd-cell-group class="info" title="商品名称" border :value="scanProduct.name">
                    <wd-cell title="商品价格">
                        <template #default>
                            <wd-text size="30rpx" bold :text="scanProduct.price" mode="price" type="error"
                                prefix="￥"></wd-text>
                        </template>
                    </wd-cell>
                    <wd-cell title="商品类型"
                        :value="appStore.getCategoryById(scanProduct.categoryId)?.name || '暂无'"></wd-cell>
                    <wd-cell title="商品单位" :value="appStore.getUnitById(scanProduct.unitId)?.name || '暂无'"></wd-cell>
                    <wd-cell title="商品编码" :value="scanProduct.code || '暂无'" />
                </wd-cell-group>
                <view class="action-btn">
                    <wd-button @click="handlePopupClose">取消</wd-button>
                    <wd-button @click="handlePopupConfirm">确认</wd-button>
                </view>
            </view>
        </wd-popup>
        <scroll-view scroll-y class="product-list">
            <view class="product-item" v-for="(product, index) in products" :key="index">
                <wd-img :width="60" :height="60" :src="product.imageList?.[0]?.url" mode="aspectFit">
                    <template #error>
                        <view class="error-wrap">加载失败</view>
                    </template>
                    <template #loading>
                        <view class="loading-wrap">
                            <wd-loading />
                        </view>
                    </template>
                </wd-img>
                <view class="info" @click="clickProduct(product.id)">
                    <wd-text bold size="30rpx" :text="product.name" :lines="1">
                    </wd-text>
                    <wd-text size="30rpx" :text="product.price" mode="price" type="error" :lines="1">
                        <template #prefix>
                            <text style="font-size: 23rpx">￥</text>
                        </template>
                    </wd-text>
                </view>
                <wd-input-number @change="handleCountChange($event, index)" :min="0" v-model="product.count" />
            </view>
        </scroll-view>
        <wd-cell title="商品总价">
            <template #default>
                <wd-text size="30rpx" bold :text="totalPrice" mode="price" type="error" prefix="￥"></wd-text>
            </template>
        </wd-cell>
        <view class="floor-button">
            <wd-button block type="primary" size="large" @click="handleClear">清空</wd-button>
        </view>
        <wd-gap safe-area-bottom height="0"></wd-gap>
    </view>
</template>

<script lang="ts" setup>
import { ref, computed } from 'vue';
import { fetchGetProduct } from '@/api';
import { useAppStore } from '@/store';
const appStore = useAppStore()
type ScanProduct = Product & {
    count: number
}

const products = ref<ScanProduct[]>([])
const showPopup = ref<boolean>(false)
const scanProduct = ref<Product>()
const currentSwiper = ref<number>(0)

const totalPrice = computed(() => {
    if (products.value.length === 0) {
        return 0
    }
    return products.value.reduce((acc, cur) => acc + cur.price * cur.count, 0)
})
function handleCountChange({ value }: { value: number }, index: number) {
    if (value === 0) {
        products.value.splice(index, 1)
    }
}
const clickProduct = (id: number) => {
    console.log(id);
    uni.navigateTo({
        url: `/pages/product/detail?id=${id}`
    });
};
function handleClear() {
    uni.showModal({
        title: '确认清空吗？',
        success: (res) => {
            if (res.confirm) {
                products.value = []
            }
        }
    })
}
const handleError = (e: any) => {
    console.log('handleError', e.detail)
}
const handlePopupClose = () => {
    showPopup.value = false
    currentSwiper.value = 0
}

const handlePopupConfirm = () => {
    if (scanProduct.value) {
        const index = products.value.findIndex(item => item.id === scanProduct.value!.id)
        if (index !== -1) {
            products.value[index].count++
        } else {
            products.value.push({
                ...scanProduct.value,
                count: 1
            })
        }
    }
    handlePopupClose()
}

const handleScanCode = async (e: any) => {
    console.log('handleScanCode', e.detail)
    if (showPopup.value) {
        return
    }
    if (e.detail.type === 'barcode') {
        const code: string = e.detail.result
        if (code.length !== 13 && code.length !== 14) {
            console.error('商品条码长度错误')
            return
        }
        uni.vibrateShort({
            success: function () {
                console.log('success');
            }
        });
        try {
            const data = await fetchGetProduct('', code)
            console.log('找到商品', JSON.stringify(data))
            scanProduct.value = data
            showPopup.value = true
        } catch (error: any) {
            uni.showToast({
                title: error?.msg || '没有找到该商品',
                icon: 'error'
            })
        }
    }
}
</script>

<style scoped lang="scss">
.valuation-container {
    padding: 20rpx;
    background: #f5f5f5;
    height: 100vh;
    box-sizing: border-box;
    display: flex;
    flex-direction: column;

    .popup-content {
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        padding: 20rpx;

        .content-info {
            flex: 1;
            margin-left: 10rpx;
            display: flex;
        }

        .content-name {
            font-size: 32rpx;
            font-weight: bold;
        }

        .content-price {
            font-size: 28rpx;
            color: #ff4d4f;
        }

        .action-btn {
            display: flex;
            justify-content: space-between;
        }
    }

    .product-list {
        flex: 1;
        overflow: auto;

        .product-item {
            padding: 20rpx;
            display: flex;
            gap: 20rpx;
            align-items: center;


            .info {
                flex: 1;
                display: flex;
                flex-direction: column;
                justify-content: space-between;
            }

            .action-btn {
                display: flex;
                justify-content: space-between;
                gap: 20rpx;
                align-items: center;
            }
        }
    }


    .scan-item {
        .scan-item-info {
            flex: 1;
            margin-left: 10rpx;
        }

        .scan-item-name {
            font-size: 32rpx;
            font-weight: bold;
        }

        .scan-item-price {
            font-size: 28rpx;
            color: #ff4d4f;
        }
    }
}
</style>