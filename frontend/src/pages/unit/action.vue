<template>
    <view class="action-container">
        <wd-form ref="form" :model="model">
            <wd-cell-group v-if="actionType !== 'view'" border>
                <wd-input label="单位名称" label-width="100px" prop="name" clearable v-model="model.name"
                    placeholder="请输入单位名称" :rules="[{ required: true, message: '请填写单位名称' }]" />
                <wd-textarea label="单位介绍" label-width="100px" type="textarea" v-model="model.description"
                    :maxlength="300" show-word-limit placeholder="请输入单位介绍" clearable prop="description" />
            </wd-cell-group>
            <wd-cell-group v-else border>
                <wd-cell title="单位名称" :value="unit?.name || '暂无'" />
                <wd-cell title="单位介绍" :value="unit?.description || '暂无'" />
                <wd-cell title="创建时间" :value="unit?.createAt || '暂无'" />
                <wd-cell title="更新时间" :value="unit?.updateAt || '暂无'" />
            </wd-cell-group>
            <view class="footer" v-if="actionType !== 'view'">
                <wd-button type="primary" size="large" @click="handleSubmit" block>{{ title }}</wd-button>
            </view>
        </wd-form>
        <wd-toast />
    </view>
</template>

<script setup lang="ts">
import { reactive, ref } from "vue";
import { fetchAddUnit, fetchUpdateUnit } from "@/api";
import { useAppStore } from "@/store";
import { onLoad } from "@dcloudio/uni-app";
import { useToast } from "wot-design-uni";
import { storeToRefs } from "pinia";
const appStore = useAppStore()
const { success: showSuccess } = useToast()
const { unitList } = storeToRefs(appStore)
const id = ref<number | null>(null)
const actionType = ref<string>('')
const unit = ref<Unit>()
const title = ref<string>('')
type Model = Partial<Pick<Unit, 'name' | 'description'>>
const model = reactive<Model>({
    name: '',
    description: ''
})
const form = ref()

const handleSubmit = async () => {
    form.value.validate().then(async (r: { valid: boolean }) => {
        if (!r.valid) {
            return
        }
        if (actionType.value === 'add') {
            // 新增
            await fetchAddUnit(model)
            showSuccess('新增成功')
        } else if (actionType.value === 'edit') {
            // 修改
            await fetchUpdateUnit({
                id: unit.value!.id,
                name: model.name,
                description: model.description,
            })
            showSuccess('修改成功')
        }
        await appStore.getUnitList()
        setTimeout(() => {
            uni.navigateBack()
        }, 500)
    })
}

onLoad(async (option: any) => {
    const { id, type } = option
    actionType.value = type
    title.value = actionType.value === 'view' ? '查看单位' : (actionType.value === 'add' ? '新增单位' : '修改单位')
    uni.setNavigationBarTitle({
        title: title.value
    });
    if (id) {
        // 查看、修改
        unit.value = unitList.value.find((item) => item.id === Number(id))
        if (unit.value) {
            model.name = unit.value.name
            model.description = unit.value.description
        }
    }
})
</script>

<style lang="scss" scoped>
.action-container {
    height: 100vh;
    background: #f5f5f5;
    .footer {
        padding: 12px;
    }
}
</style>