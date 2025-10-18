
export { };
declare global {
    interface BasePage {
        current?: number = 1,
        size?: number = 10
    }

    interface Page<T> {
        current: number,
        pages: number,
        size: number,
        total: number,
        records: T[],
    }

    interface ImageRequest {
        uid: number,
        url: string,
        name: string,
        fileName: string,
    }

    interface Category {
        id: number,
        name: string,
        description: string,
        createAt: string,
        updateAt: string,
    }

    interface Unit {
        id: number,
        name: string,
        description: string,
        createAt: string,
        updateAt: string,
    }

    interface ProductImage {
        id: number,
        url: string,
        name: string,
        fileName: string,
        productId: number,
        sortOrder: number,
        createAt: string,
        updateAt: string,
    }

    interface Product {
        id: number,
        name: string,
        code: string,
        price: number,
        categoryId: number,
        unitId: number,
        description: string,
        isActive: boolean,
        imageList: ProductImage[],
        createAt: string,
        updateAt: string,
    }

    interface R<T = any> {
        code: number,
        data: T,
        msg: string
    }

    interface RequestOptions {
        url: string;
        method: 'get' | 'post' | 'put' | 'delete';
        data?: any;
        params?: any;
        header?: Record<string, any>;
    }

}