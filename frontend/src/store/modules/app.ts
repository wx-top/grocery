import {defineStore} from "pinia";

import {fetchGetCategoryList, fetchGetUnitList} from "@/api";

export const useAppStore = defineStore("app", {
    state: () => {
        return {
            tabbarIndex: 0,
            unitList: [] as Unit[],
            categoryList: [] as Category[],
            needRefreshProduct: false,
        }
    },
    actions: {
        async getUnitList() {
            const data = await fetchGetUnitList();
            console.log('getUnitList', data);
            this.unitList = data || [];
        },
        async getCategoryList() {
            const data = await fetchGetCategoryList();
            console.log('getCategoryList', data);
            this.categoryList = data || [];
        },
        getUnitById(id: number) {
            return this.unitList.find(item => item.id === id);
        },
        getCategoryById(id: number) {
            return this.categoryList.find(item => item.id === id);
        },
        setNeedRefreshProduct(value: boolean) {
            this.needRefreshProduct = value;
        }
    }
})