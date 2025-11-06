<template>
    <view class="action-container">
        <wd-form ref="form" :rules="rules" :model="model">
            <wd-cell-group border>
                <wd-input label="商品名称" label-width="100px" prop="name" clearable v-model="model.name"
                    placeholder="请输入商品名称" />
                <wd-input label="商品编码" label-width="100px" prop="code" clearable v-model="model.code" suffix-icon="scan"
                    @clicksuffixicon="scanCode" placeholder="请输入商品编码" />
                <wd-picker label="商品分类" label-width="100px" prop="categoryId" v-model="model.categoryId"
                    :columns="categoryList" value-key="id" label-key="name" placeholder="请选择商品分类" />
                <wd-picker label="商品单位" label-width="100px" prop="unitId" v-model="model.unitId" :columns="unitList"
                    value-key="id" label-key="name" placeholder="请选择商品单位" />
                <wd-input label="商品单价" inputmode="decimal" label-width="100px" prop="price" clearable
                    v-model="model.price" placeholder="请输入商品单价" />
                <wd-cell title="商品图片" title-width="100px" prop="fileList">
                    <wd-upload :upload-method="customUpload" v-model:file-list="fileList" multiple
                        accept="image"></wd-upload>
                </wd-cell>

                <wd-textarea label="商品介绍" label-width="100px" type="textarea" v-model="model.description"
                    :maxlength="300" show-word-limit placeholder="请输入商品介绍" clearable prop="description" />
            </wd-cell-group>
            <view class="footer">
                <wd-button type="primary" size="large" @click="handleSubmit" block>{{ title }}</wd-button>
            </view>

        </wd-form>
        <wd-message-box />
        <wd-toast />
    </view>
</template>

<script setup lang="ts">
import { upload_url } from "@/utils/request";
import { useToast } from "wot-design-uni";
import { reactive, ref } from "vue";
import { useAppStore } from "@/store";
import { onLoad } from "@dcloudio/uni-app";
import { fetchAddProduct, fetchGetProduct, fetchUpdateProduct } from "@/api";
import { storeToRefs } from "pinia";
import type { UploadFileItem, UploadFormData, UploadMethod } from "wot-design-uni/components/wd-upload/types";
const toast = useToast()
const title = ref<string>('')
const fileList = ref<UploadFileItem[]>([])
const rules = {
    name: [{ required: true, message: '请填写商品名称' }],
    categoryId: [{ required: true, message: '请选择商品分类' }],
    unitId: [{ required: true, message: '请选择商品单位' }],
    price: [{ validator: (val: number) => val >= 0, message: '商品单价必须大于等于0', required: true }],
}
const appStore = useAppStore()
const { categoryList, unitList } = storeToRefs(appStore)
const actionType = ref<string>('')
const product = ref<Product>()

type Model = Partial<Pick<Product, 'name' | 'description' | 'price' | 'code'>> & {
    categoryId: string,
    unitId: string,
    imageList: ImageRequest[],
}
const model = reactive<Model>({
    name: '',
    description: '',
    price: 0,
    code: '',
    categoryId: '',
    unitId: '',
    imageList: [],
})
const form = ref()
const handleSubmit = async () => {
    form.value.validate().then(async (r: { valid: boolean }) => {
        if (!r.valid) {
            return
        }

        if (fileList.value.length > 0) {
            console.log(fileList.value)
            model.imageList = fileList.value.filter((item: UploadFileItem) => item.status === 'success').map(
                (item: UploadFileItem) => {
                    console.log(item)
                    if (item.name && item.fileName) {
                        return {
                            uid: item.uid,
                            url: item.url || '',
                            name: item.name || '',
                            fileName: item.fileName || '',
                        }
                    } else {
                        const result: { code: number, data: ImageRequest, msg: string } = JSON.parse(item.response?.toString() || '{}')
                        return {
                            uid: item.uid,
                            url: result.data.url,
                            name: result.data.name,
                            fileName: result.data.fileName,
                        }
                    }

                }
            )
        }
        if (actionType.value === 'add') {
            // 新增
            await fetchAddProduct({
                ...model,
                categoryId: Number(model.categoryId),
                unitId: Number(model.unitId),
            })
            toast.success({ msg: '新增成功', duration: 2000 })
        } else if (actionType.value === 'edit') {
            // 修改
            await fetchUpdateProduct({
                id: product.value!.id,
                ...model,
                categoryId: Number(model.categoryId),
                unitId: Number(model.unitId),
            })
            toast.success({ msg: '修改成功', duration: 2000 })
        }
        appStore.setNeedRefreshProduct(true)
        setTimeout(() => {
            uni.navigateBack()
        }, 500)
    }).catch((error: any) => {
        console.log(error)
        toast.error({ msg: error.msg || '操作失败', duration: 2000 })
    })
}


const customUpload: UploadMethod = (file: UploadFileItem, formData: UploadFormData, options) => {
    const uploadTask = uni.uploadFile({
        url: upload_url,
        header: options.header,
        name: options.name,
        fileName: options.name,
        fileType: options.fileType,
        formData,
        filePath: file.url,
        success(res) {
            const result: { code: number, data: { url: string, originalFilename: string, fileName: string }, msg: string } = JSON.parse(res.data)
            if (res.statusCode === options.statusCode && result.code === 200) {
                // 设置上传成功
                options.onSuccess(res, file, formData)
            } else {
                // 设置上传失败
                options.onError({ ...res, errMsg: res.errMsg || '' }, file, formData)
            }
        },
        fail(err) {
            // 设置上传失败
            options.onError(err, file, formData)
        }
    })
    // 设置当前文件加载的百分比
    uploadTask.onProgressUpdate((res) => {
        options.onProgress(res, file)
    })
}
const scanCode = async () => {
    uni.scanCode({
        scanType: ['barCode'],
        success: function (res) {
            console.log('条码类型：' + res.scanType);
            console.log('条码内容：' + res.result);
            model.code = res.result
            toast.success({ msg: '条码扫描成功', duration: 2000 })
        },
        fail: function (res) {
            console.log('条码扫描失败：' + res.errMsg);
            toast.error({ msg: '条码扫描失败：' + res.errMsg, duration: 2000 })
        }
    });
}

onLoad(async (option: any) => {
    const { id, type } = option
    actionType.value = type
    title.value = actionType.value === 'add' ? '新增商品' : '修改商品'
    uni.setNavigationBarTitle({
        title: title.value
    });
    if (actionType.value === 'edit') {
        if (!id) {
            uni.showToast({
                title: '请选择要操作的商品',
                icon: 'error',
                duration: 2000
            })
            setTimeout(() => {
                uni.navigateBack()
            }, 500)
            return
        }
        product.value = await fetchGetProduct(id)
        model.name = product.value.name
        model.description = product.value.description
        model.price = product.value.price
        model.code = product.value.code
        model.categoryId = product.value.categoryId.toString()
        model.unitId = product.value.unitId.toString()
        fileList.value = product.value.imageList?.map((item: ProductImage) => ({
            uid: item.id,
            url: item.url,
            name: item.name,
            fileName: item.fileName,
        })) || []
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