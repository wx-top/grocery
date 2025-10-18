<template>
  <view class="management-container">
    <wd-grid clickable :column="3">
      <wd-grid-item v-for="(item, index) in itemList" @itemclick="handleClick(item)" :key="index" :icon="item.icon"
        :text="item.title" />
    </wd-grid>
    <wd-toast />
    <MyTabbar />
  </view>
</template>

<script setup lang="ts">
import MyTabbar from "@/components/MyTabbar/MyTabbar.vue";
import { useToast } from "wot-design-uni"
const toast = useToast()

interface GridItem {
  title: string;
  icon: string;
  path?: string;
  linkType?: "navigateTo" | "redirectTo" | "switchTab";
  function?: (item: GridItem) => void;
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
      } else {
        toast.error('未识别有效商品条码')
      }
    },
    fail: (res) => {
      console.log(res)
      toast.error('未识别有效商品条码')
    }
  })
}

const itemList: GridItem[] = [
  {
    title: "扫码搜索",
    icon: "scan",
    function: handleScanCodeSearch,
  },
  {
    title: "扫码计价",
    icon: "scan",
    path: "/pages/valuation/index",
    linkType: "navigateTo",
  },
  {
    title: "新增商品",
    icon: "add",
    path: "/pages/product/action?type=1",
  },
  {
    title: "分类管理",
    icon: "app",
    path: "/pages/category/index",
  },
  {
    title: "单位管理",
    icon: "app",
    path: "/pages/unit/index",
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

</script>

<style lang="scss" scoped>
.management-container {
  height: 100%;
  background: #f5f5f5;
}
</style>