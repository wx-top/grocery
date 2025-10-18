import { request } from "@/utils/request";

type UnitRequest = Partial<Pick<Unit, 'name' | 'description'>>

export const fetchGetUnitList = () => {
    return request<Unit[]>({
        url: '/unit',
        method: 'get',
    })
}

export const fetchAddUnit = (unit: UnitRequest) => {
    return request<Unit>({
        url: '/unit',
        method: 'post',
        data: unit,
    })
}

export const fetchUpdateUnit = (unit: UnitRequest & { id: number }) => {
    return request<Unit>({
        url: '/unit',
        method: 'put',
        data: unit,
    })
}


export const fetchDeleteUnit = (id: number) => {
    return request<Unit>({
        url: '/unit/' + id,
        method: 'delete',
    })
}