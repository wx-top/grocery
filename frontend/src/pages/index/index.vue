<template>
  <view class="management-container">
    <wd-grid clickable :column="3">
      <wd-grid-item v-for="(item, index) in itemList.filter(item => item.show)" @itemclick="handleClick(item)" :key="index" :icon="item.icon"
        :text="item.title" />
    </wd-grid>
    <wd-toast />
    <MyTabbar />
  </view>
</template>

<script setup lang="ts">
import MyTabbar from "@/components/MyTabbar/MyTabbar.vue";
import { useAppStore } from "@/store";
import { storeToRefs } from "pinia";
import { onShow } from "@dcloudio/uni-app";
import { useToast } from "wot-design-uni"
const toast = useToast()
const appStore = useAppStore()
const { tabbarIndex, isLogin } = storeToRefs(appStore)


interface GridItem {
  title: string;
  icon: string;
  path?: string;
  linkType?: "navigateTo" | "redirectTo" | "switchTab";
  function?: (item: GridItem) => void;
  show: boolean;
}

const handleScanCodeSearch = (item: GridItem) => {
  uni.scanCode({
    success: (res) => {
      console.log(res)
      if (res.result) {
        toast.success('识别到商品条码：' + res.result)
        setTimeout(() => {
          uni.navigateTo({
            url: "/pages/product/detail?code=" + res.result
          })
        }, 1000)
      }
    },
    fail: (res) => {
      console.log(res)
    }
  })
}

const itemList: GridItem[] = [
  {
    title: "商品搜索",
    icon: "search",
    path: "/pages/search/index",
    linkType: "navigateTo",
    show: true,
  },
  {
    title: "扫码搜索",
    icon: "scan",
    function: handleScanCodeSearch,
    show: true,
  },
  {
    title: "扫码计价",
    icon: "scan",
    path: "/pages/valuation/index",
    linkType: "navigateTo",
    show: true,
  },
  {
    title: "新增商品",
    icon: "add",
    path: "/pages/product/action?type=add",
    show: isLogin.value,
  },
  {
    title: "分类管理",
    icon: "app",
    path: "/pages/category/index",
    show: isLogin.value,
  },
  {
    title: "单位管理",
    icon: "app",
    path: "/pages/unit/index",
    show: isLogin.value,
  },
];

const handleClick = (item: GridItem) => {
  console.log('click item')
  if (item.path) {
    if (item.linkType === "switchTab") {
      uni.switchTab({
        url: item.path
      })
    } else if (item.linkType === "redirectTo") {
      uni.redirectTo({
        url: item.path
      })
    } else {
      uni.navigateTo({
        url: item.path
      })
    }
  } else if (item.function) {
    item.function(item);
  } else {
    uni.showToast({
      title: "功能未实现",
      icon: "none"
    })
  }
}

onShow(() => {
  if (tabbarIndex.value !== 0) {
    tabbarIndex.value = 0
  }
})

</script>

<style lang="scss" scoped>
.management-container {
  height: 100vh;
  background: #f5f5f5;
}
</style>