<template>
  <view class="product-container">
    <view class="wraper">
      <wd-sidebar v-model="active" @change="handleChange">
        <wd-sidebar-item v-for="(item, index) in categoryList" :key="index" :value="index" :label="item.name" />
      </wd-sidebar>
      <view class="content" :style="`transform: translateY(-${active * 100}%)`">
        <scroll-view v-for="(item, index) in categoriesWithProducts" :key="index" class="category" scroll-y
          refresher-enabled :refresher-triggered="triggered" @scrolltolower="handleScrollLower"
          @refresherrefresh="loadData" scroll-with-animation :show-scrollbar="false" :scroll-top="scrollTop"
          :throttle="false">
          <wd-cell-group :title="item.name" border>
            <view class="product-item" @click="clickProduct(product.id)" v-for="(product, index) in item.items"
              :key="index">
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
          </wd-cell-group>
          <wd-gap bg-color="#FFFFFF"></wd-gap>
        </scroll-view>
      </view>
    </view>
    <wd-toast />
    <MyTabbar />
  </view>

</template>

<script setup lang="ts">
import MyTabbar from "@/components/MyTabbar/MyTabbar.vue";
import { ref, computed, nextTick, onMounted } from "vue";
import { fetchGetProductList } from "@/api";
import { useAppStore } from "@/store";
import { storeToRefs } from "pinia";
import { useToast } from "wot-design-uni"
import { onShow } from "@dcloudio/uni-app";
const toast = useToast()
const triggered = ref<boolean>(false)
const isLoading = ref<boolean>(false)
const { categoryList } = storeToRefs(useAppStore());
const products = ref<Product[]>([])
const active = ref<number>(0)
const scrollTop = ref<number>(0)
const current = ref<number>(1)
const size = ref<number>(10)
const notMore = ref<boolean>(false)
const appStore = useAppStore();

const categoriesWithProducts = computed(() => {
  return categoryList.value.map((category) => {
    return {
      ...category,
      items: products.value.filter((product) => product.categoryId === category.id),
    };
  });
});

const clickProduct = (id: number) => {
  console.log(id);
  uni.navigateTo({
    url: `/pages/product/detail?id=${id}`
  });
};
function handleChange(e: { value: number }) {
  active.value = e.value
  scrollTop.value = -1
  nextTick(() => {
    scrollTop.value = 0
  })
}

const getProductList = async () => {
  if (isLoading.value) return
  const activeCategory = categoryList.value[active.value]
  if (!activeCategory) return
  if (notMore.value) {
    toast.warning({ msg: '没有更多商品了', duration: 2000 })
    return
  }
  isLoading.value = true
  try {
    const data = await fetchGetProductList({
      current: current.value,
      size: size.value,
      categoryId: activeCategory.id,
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
    toast.error?.({ msg: '加载失败，请重试', duration: 2000 })
  } finally {
    isLoading.value = false
  }
}

const handleScrollLower = () => {
  console.log('handleScrollLower')
  getProductList()
}

const loadData = async () => {
  console.log('handleRefresh')
  triggered.value = true
  products.value = []
  current.value = 1
  notMore.value = false
  await getProductList()
  triggered.value = false
  appStore.setNeedRefreshProduct(false)
}

onMounted(() => {
  getProductList()
})

onShow(async () => {
  if (appStore.needRefreshProduct) {
    console.log('needRefreshProduct')
    await loadData()
  }
})

</script>

<style lang="scss" scoped>
.product-container {
  height: 100%;
  background: #f5f5f5;
  overflow: hidden;

  .wraper {
    display: flex;
    height: calc(100vh - var(--window-top));
    height: calc(100vh - var(--window-top) - constant(safe-area-inset-bottom));
    height: calc(100vh - var(--window-top) - env(safe-area-inset-bottom));
    overflow: hidden;

    .content {
      flex: 1;
      background: #fff;
      transition: transform 0.3s ease;

      .category {
        box-sizing: border-box;
        height: 100%;
        padding-bottom: 50rpx;

        .product-item {
          padding: 20rpx;
          display: flex;
          gap: 20rpx;

          .info {
            flex: 1;
            display: flex;
            flex-direction: column;
            justify-content: space-between;
          }
        }
      }
    }
  }
}
</style>
