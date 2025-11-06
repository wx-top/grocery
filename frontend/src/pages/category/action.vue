<template>
    <view class="action-container">
        <wd-form ref="form" :model="model">
            <wd-cell-group v-if="actionType !== 'view'" border>
                <wd-input label="分类名称" label-width="100px" prop="name" clearable v-model="model.name"
                    placeholder="请输入分类名称" :rules="[{ required: true, message: '请填写分类名称' }]" />
                <wd-input label="分类描述" label-width="100px" prop="description" clearable v-model="model.description"
                    placeholder="请输入分类描述" />
            </wd-cell-group>
            <wd-cell-group v-else border>
                <wd-cell title="分类名称" :value="category?.name || '暂无'" />
                <wd-cell title="分类描述" :value="category?.description || '暂无'" />
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
import { fetchAddCategory, fetchUpdateCategory } from "@/api";
import { useAppStore } from "@/store";
import { onLoad } from "@dcloudio/uni-app";
import { useToast } from "wot-design-uni";
import { storeToRefs } from "pinia";
const appStore = useAppStore()
const { success: showSuccess } = useToast()
const { categoryList } = storeToRefs(appStore)
const actionType = ref<string>('')
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
        if (actionType.value === 'add') {
            // 新增
            await fetchAddCategory(model)
            showSuccess('新增成功')
        } else if (actionType.value === 'edit') {
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

onLoad(async (option: any) => {
    const { id, type } = option
    console.log('id type', id, type)

    actionType.value = type
    title.value = actionType.value === 'view' ? '查看分类' : (actionType.value === 'add' ? '新增分类' : '修改分类')
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