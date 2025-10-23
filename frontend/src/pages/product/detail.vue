<template>
  <view class="detail-container" v-if="product">
    <wd-swiper :list="imageList" v-model:current="current" autoplay
      :indicator="{ type: 'fraction' }" indicatorPosition="bottom-right">
      <template #default="{ item }">
        <image style="height: 100%; width: 100%;" :src="item" mode="aspectFit" @click="handleClick"/>
      </template>
    </wd-swiper>
    <view class="box">
      <wd-cell-group class="info" title="商品名称" border :value="product.name">
        <wd-cell title="商品价格">
          <template #default>
            <wd-text size="30rpx" bold :text="product.price" mode="price" type="error" prefix="￥"></wd-text>
          </template>
        </wd-cell>
        <wd-cell title="商品类型" :value="category?.name || '暂无'"></wd-cell>
        <wd-cell title="商品单位" :value="unit?.name || '暂无'"></wd-cell>
        <wd-cell title="商品编码" :value="product.code || '暂无'" />
        <wd-cell title="商品介绍" :value="product.description || '暂无'" />
      </wd-cell-group>
      <view class="btn-list">
        <wd-button ype="icon" @click="handleAction(2)" icon="edit-outline" block type="primary"
          size="large">编辑商品</wd-button>
        <wd-button ype="icon" @click="handleAction(3)" icon="delete" block type="error" size="large">删除商品</wd-button>
      </view>
    </view>
    <wd-message-box />
    <wd-toast />
  </view>
</template>

<script setup lang="ts">
import { computed, ref } from "vue";
import { onLoad, onShow } from "@dcloudio/uni-app"
import { useAppStore } from "@/store/modules/app";
import { useToast } from "wot-design-uni"
import { fetchDeleteProduct, fetchGetProduct } from "@/api"

const id = ref<string>('')
const code = ref<string>('')
const toast = useToast()
const appStore = useAppStore();
const product = ref<Product>();
const category = ref<Category>();
const unit = ref<Unit>();
const current = ref<number>(0);

const imageList = computed(() => {
  return product.value?.imageList.map((item: ProductImage) => item.url) || []
})

onLoad((option: any) => {
  if (option?.id) {
    id.value = option.id
  }
  if (option?.code) {
    code.value = option.code
  }
  if (!id.value && !code.value) {
    uni.showToast({
      title: '请输入商品id或编码',
      icon: 'error',
      duration: 2000
    })
    uni.navigateBack()
  }
})

onShow(async () => {
  try {
    const data = await fetchGetProduct(id.value, code.value)
    product.value = data
    if (product.value?.categoryId) {
      category.value = appStore.getCategoryById(Number(product.value.categoryId));
    }
    if (product.value?.unitId) {
      unit.value = appStore.getUnitById(Number(product.value.unitId));
    }
    console.log('onShow', product.value)
  } catch (error: any) {
    console.error('获取商品详情失败', error)
    uni.showToast({
      title: error?.msg || '获取商品详情失败',
      icon: 'error',
      duration: 2000
    })
    setTimeout(() => {
      uni.navigateBack()
    }, 1000)
  }
})
const handleAction = (type: number) => {
  if (type === 2) {
    uni.navigateTo({
      url: `/pages/product/action?type=${type}&id=${product.value!.id}`
    })
  } else if (type === 3) {
    uni.showModal({
      title: "确认删除",
      content: "确认删除商品吗？",
      success: async (res) => {
        if (res.confirm) {
          console.log("用户点击了删除");
          await fetchDeleteProduct(product.value!.id)
          toast.success('删除成功')
          appStore.setNeedRefreshProduct(true)
          setTimeout(() => {
            uni.navigateBack()
          }, 500)
        }
      }
    })
  }
};

const handleClick = () => {
  uni.previewImage({
    current: current.value,
    urls: imageList.value,
  })
};
</script>
<style lang="scss" scoped>
.detail-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: #f5f5f5;

  .box {
    flex: 1;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    padding: 30rpx;

    .info {
      flex: 1;
    }

    .btn-list {
      display: flex;
      justify-content: center;
      gap: 20rpx;
    }
  }

}
</style>