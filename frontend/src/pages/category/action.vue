<template>
    <view class="action-container">
        <wd-form ref="form" :model="model">
            <wd-cell-group v-if="actionType !== 0" border>
                <wd-input label="分类名称" label-width="100px" prop="name" clearable v-model="model.name"
                    placeholder="请输入分类名称" :rules="[{ required: true, message: '请填写分类名称' }]" />
                <wd-textarea label="分类介绍" label-width="100px" type="textarea" v-model="model.description"
                    :maxlength="300" show-word-limit placeholder="请输入分类介绍" clearable prop="description" />
            </wd-cell-group>
            <wd-cell-group v-else border>
                <wd-cell title="分类名称" :value="category?.name || '暂无'" />
                <wd-cell title="分类介绍" :value="category?.description || '暂无'" />
                <wd-cell title="创建时间" :value="category?.createAt || '暂无'" />
                <wd-cell title="更新时间" :value="category?.updateAt || '暂无'" />
            </wd-cell-group>
            <view class="footer" v-if="actionType !== 0">
                <wd-button type="primary" size="large" @click="handleSubmit" block>{{ title }}</wd-button>
            </view>
        </wd-form>
        <wd-toast />
    </view>
</template>

<script setup lang="ts">
import { reactive, ref } from "vue";
import { fetchAddCategory, fetchUpdateCategory } from "@/api";
import { useAppStore } from "@/store";
import { onLoad } from "@dcloudio/uni-app";
import { useToast } from "wot-design-uni";
import { storeToRefs } from "pinia";
const appStore = useAppStore()
const { success: showSuccess } = useToast()
const { categoryList } = storeToRefs(appStore)
const actionType = ref<number>(0)
const category = ref<Category>()
const title = ref<string>('')
type Model = Partial<Pick<Category, 'name' | 'description'>>
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
        if (actionType.value === 1) {
            // 新增
            await fetchAddCategory(model)
            showSuccess('新增成功')
        } else if (actionType.value === 2) {
            // 修改
            await fetchUpdateCategory({
                id: category.value!.id,
                name: model.name,
                description: model.description,
            })
            showSuccess('修改成功')
        }
        await appStore.getCategoryList()
        setTimeout(() => {
            uni.navigateBack()
        }, 500)
    })
}

onLoad((option: any) => {
    const { id, type } = option
    actionType.value = Number(type)
    title.value = actionType.value === 0 ? '查看分类' : (actionType.value === 1 ? '新增分类' : '修改分类')
    uni.setNavigationBarTitle({
        title: title.value
    });
    if (id) {
        // 查看、修改
        category.value = categoryList.value.find((item) => item.id === Number(id))
        if (category.value) {
            model.name = category.value.name
            model.description = category.value.description
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