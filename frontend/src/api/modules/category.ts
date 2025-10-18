import { request } from "@/utils/request";

type CategoryRequest = Partial<Pick<Category, 'name' | 'description'>>

export const fetchGetCategoryList = () => {
    return request<Category[]>({
        url: '/category',
        method: 'get',
    })
}

export const fetchDeleteCategory = (id: number) => {
    return request({
        url: '/category/' + id,
        method: 'delete',
    })
}

export const fetchAddCategory = (category: CategoryRequest) => {
    return request({
        url: '/category',
        method: 'post',
        data: category,
    })
}

export const fetchUpdateCategory = (category: CategoryRequest & { id: number }) => {
    return request({
        url: '/category',
        method: 'put',
        data: category,
    })
}