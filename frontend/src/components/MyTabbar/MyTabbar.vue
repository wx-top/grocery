<template>
  <wd-tabbar bordered placeholder safeAreaInsetBottom v-model="tabbarIndex" @change="handleChange" fixed>
    <wd-tabbar-item v-for="(item, index) in tabbarList" :key="index" :icon="item.icon"
      :title="item.title"></wd-tabbar-item>
  </wd-tabbar>
</template>
<script setup lang="ts">
import { storeToRefs } from "pinia";
import { useAppStore } from "@/store";
const { tabbarIndex } = storeToRefs(useAppStore());

const tabbarList = [
  {
    title: "首页",
    icon: "home",
    path: "/pages/index/index",
  },
  {
    title: "商品",
    icon: "app",
    path: "/pages/product/index",
  },
  {
    title: "我的",
    icon: "user",
    path: "/pages/user/index",
  },
];

const handleChange = (e: { value: number }) => {
  // 跳转页面, 重定向
  uni.redirectTo({
    url: tabbarList[e.value].path
  })
};

</script>