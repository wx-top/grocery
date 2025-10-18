<template>
  <view class="management-container">
    <scroll-view class="scroll" scroll-y>
      <wd-swipe-action v-for="item in categoryList" :key="item.id">
        <wd-cell :title="item.name" />
        <template #right>
          <view class="action">
            <view class="button" style="background: #aba7a7;" @click="handleAction(0, item.id)">查看</view>
            <view class="button" style="background: #007AFF;" @click="handleAction(2, item.id)">修改</view>
            <view class="button" style="background: #ff0000;" @click="handleAction(3, item.id)">删除</view>
          </view>
        </template>
      </wd-swipe-action>
    </scroll-view>
    <view class="floor-button">
      <wd-button @click="handleAction(1)" block icon="add" type="primary" size="large">添加分类</wd-button>
    </view>
    <wd-message-box></wd-message-box>
    <wd-toast />
    <MyTabbar />
  </view>
</template>

<script setup lang="ts">
import MyTabbar from "@/components/MyTabbar/MyTabbar.vue";
import { storeToRefs } from "pinia";
import { useAppStore } from "@/store";
const appStore = useAppStore()
const { categoryList } = storeToRefs(appStore)
import { useMessage, useToast } from "wot-design-uni";
import { fetchDeleteCategory } from "@/api";
const toast = useToast()

const message = useMessage()

const handleAction = async (type: number, id?: number) => {
  console.log('handleAction', type, id)
  if ([2, 3].includes(Number(type)) && !id) {
    toast.error('请选择要操作的分类')
    return
  }
  if (type === 3) {
    // 删除
    message.confirm({
      title: '删除分类',
      msg: '确定删除该分类吗？',
      confirmButtonText: '确定',
      cancelButtonText: '取消',
    }).then(async () => {
      console.log('点击了确定按钮')
      await fetchDeleteCategory(id!)
      toast.success('删除成功')
      await appStore.getCategoryList()
    })
  } else {
    uni.navigateTo({
      url: `/pages/category/action?type=${type}&id=${id}`
    })
  }
}
</script>

<style lang="scss" scoped>
.management-container {
  height: 100%;
  display: flex;
  flex-direction: column;
  background: #f5f5f5;

  .scroll {
    flex: 1;
  }

  .action {
    height: 100%;
  }

  .button {
    display: inline-block;
    padding: 0 11px;
    height: 100%;
    color: white;
    line-height: 42px;
  }
}
</style>