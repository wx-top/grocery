import { request } from "@/utils/request";

// interface ProductRequest extends BasePage {
//     id?: number,
//     name?: string,
//     code?: string,
//     price?: number,
//     categoryId?: number,
//     unitId?: number,
//     description?: string,
//     isActive?: boolean,
// }

type ProductRequest = Partial<Pick<Product, 'name' | 'code' | 'price' | 'categoryId' | 'unitId' | 'description' | 'isActive'>>

export const fetchGetProductList = (data: ProductRequest & BasePage) => {
    return request<Page<Product>>({
        url: `/product`,
        method: 'get',
        data,
    })
}

export const fetchAddProduct = (data: ProductRequest) => {
    return request<Product>({
        url: `/product`,
        method: 'post',
        data,
    })
}

export const fetchUpdateProduct = (data: ProductRequest & { id: number }) => {
    return request<Product>({
        url: `/product`,
        method: 'put',
        data,
    })
}

export const fetchGetProduct = (id?: string, code?: string) => {
    return request<Product>({
        url: '/product/get',
        method: 'get',
        data: {
            id,
            code,
        }
    })
}

export const fetchDeleteProduct = (id: number) => {
    return request<Product>({
        url: `/product/${id}`,
        method: 'delete',
    })
}
