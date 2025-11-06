<template>
  <view class="management-container">
    <scroll-view class="scroll-view" scroll-y>
      <wd-swipe-action v-for="item in unitList" :key="item.id">
        <wd-cell :title="item.name" />
        <template #right>
          <view class="action">
            <view class="button" style="background: #aba7a7;" @click="handleAction('view', item.id)">查看</view>
            <view class="button" style="background: #007AFF;" @click="handleAction('edit', item.id)">修改</view>
            <view class="button" style="background: #ff0000;" @click="handleAction('delete', item.id)">删除</view>
          </view>
        </template>
      </wd-swipe-action>
    </scroll-view>
    <view class="floor-button">
      <wd-button @click="handleAction('add')" block icon="add" type="primary" size="large">添加单位</wd-button>
    </view>
    <wd-message-box></wd-message-box>
    <wd-toast />
    <wd-gap safe-area-bottom height="0"></wd-gap>
  </view>
</template>

<script setup lang="ts">
import MyTabbar from "@/components/MyTabbar/MyTabbar.vue";
import { storeToRefs } from "pinia";
import { useAppStore } from "@/store";
const appStore = useAppStore()
const { unitList } = storeToRefs(appStore)
import { useMessage, useToast } from "wot-design-uni";
import { fetchDeleteUnit } from "@/api";
const toast = useToast()

const message = useMessage()

const handleAction = async (type: string, id?: number) => {
  console.log('handleAction', type, id)
  if (['edit', 'delete'].includes(type) && !id) {
    toast.error('请选择要操作的单位')
    return
  }
  if (type === 'delete') {
    // 删除
    message.confirm({
      title: '删除单位',
      msg: '确定删除该单位吗？',
      confirmButtonText: '确定',
      cancelButtonText: '取消',
    }).then(async () => {
      console.log('点击了确定按钮')
      await fetchDeleteUnit(id!)
      toast.success('删除成功')
      await appStore.getUnitList()
    })
  } else {
    let url = `/pages/unit/action?type=${type}`
    if (['edit', 'view'].includes(type)) {
      url += `&id=${id}`
    }
    uni.navigateTo({
      url: url
    })
  }
}
</script>

<style lang="scss" scoped>
.management-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: #f5f5f5;

  .scroll-view {
    flex: 1;
    overflow: hidden;

    .action {
      height: 100%;

      .button {
        display: inline-block;
        padding: 0 11px;
        height: 100%;
        color: white;
        line-height: 42px;
      }
    }
  }

  .floor-button {
    flex-shrink: 0;
    padding: 12px;
  }
}
</style>