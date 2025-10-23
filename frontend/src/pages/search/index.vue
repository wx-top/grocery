<template>
    <view class="search-container">
        <wd-search v-model="value" @search="search" @clear="clear" @cancel="cancel" @change="change" />
        <scroll-view class="product-list" scroll-y refresher-enabled scroll-with-animation :show-scrollbar="false"
            :scroll-top="scrollTop" :throttle="false" @scrolltolower="handleScrollLower">
            <view class="product-item" @click="clickProduct(product.id)" v-for="(product, index) in products"
                :key="index">
                <wd-img v-if="product.imageList.length > 0" :width="60" :height="60" :src="product.imageList?.[0]?.url"
                    mode="aspectFit">
                    <template #error>
                        <view class="error-wrap">加载失败</view>
                    </template>
                    <template #loading>
                        <view class="loading-wrap">
                            <wd-loading />
                        </view>
                    </template>
                </wd-img>
                <wd-img v-else :width="60" :height="60" src="/static/notImg.png" mode="aspectFit">
                    <template #error>
                        <view class="error-wrap">加载失败</view>
                    </template>
                    <template #loading>
                        <view class="loading-wrap">
                            <wd-loading />
                        </view>
                    </template>
                </wd-img>
                <view class="info">
                    <wd-text bold size="30rpx" :text="product.name" :lines="1">
                    </wd-text>
                    <wd-text size="30rpx" :text="product.price" mode="price" type="error" :lines="1">
                        <template #prefix>
                            <text style="font-size: 23rpx">￥</text>
                        </template>
                    </wd-text>
                </view>
            </view>
        </scroll-view>
        <wd-toast />
    </view>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { useToast } from "wot-design-uni"
import { fetchGetProductList } from "@/api";
const toast = useToast()
const products = ref<Product[]>([])
const value = ref("");
const scrollTop = ref<number>(0)
const current = ref<number>(1)
const size = ref<number>(10)
const notMore = ref<boolean>(false)
const isLoading = ref<boolean>(false)


const clickProduct = (id: number) => {
    console.log(id);
    uni.navigateTo({
        url: `/pages/product/detail?id=${id}`
    });
};

const handleScrollLower = () => {
    console.log("handleScrollLower");
    getProductList()
}



const getProductList = async () => {
    if (isLoading.value) return
    if (notMore.value) {
        toast.warning({ msg: '没有更多商品了', duration: 2000 })
        return
    }
    isLoading.value = true
    try {
        const data = await fetchGetProductList({
            current: current.value,
            size: size.value,
            name: value.value
        })

        const records = data?.records ?? []
        if (records.length === 0) {
            notMore.value = true
            toast.warning({ msg: '没有更多商品了', duration: 2000 })
            return
        }

        const merged = [...products.value, ...records]
        const seen = new Set<number>()
        products.value = merged.filter((p) => {
            if (seen.has(p.id)) return false
            seen.add(p.id)
            return true
        })

        if (records.length < size.value) {
            notMore.value = true
        }

        // 将页码递增放在请求成功后，避免漂移；优先使用服务端返回
        const serverCurrent = data.current
        if (typeof serverCurrent === 'number') {
            current.value = serverCurrent + 1
        } else {
            current.value++
        }
    } catch (error) {
        console.log(error)
        toast.error({ msg: '加载失败，请重试', duration: 2000 })
    } finally {
        isLoading.value = false
    }
}

const search = async () => {
    console.log("search");
    current.value = 1
    notMore.value = false
    products.value = []
    await getProductList()

};

const clear = () => {
    console.log("clear");
};

const cancel = () => {
    console.log("cancel");
};

const change = (val: string) => {
    console.log("change", val);
};

</script>

<style scoped lang="scss">
.search-container {
    padding: 20rpx;
    background: #f5f5f5;
    height: 100%;
    overflow: hidden;

    .product-list {
        box-sizing: border-box;
        height: 100%;

        .product-item {
            padding: 20rpx;
            display: flex;
            gap: 20rpx;
            background: #fff;

            .info {
                flex: 1;
                display: flex;
                flex-direction: column;
                justify-content: space-between;
            }
        }
    }
}
</style>
