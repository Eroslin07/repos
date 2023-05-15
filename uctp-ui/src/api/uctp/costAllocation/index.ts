import request from '@/config/axios'

export interface CostVO {
  id?: number
  costType: number
  cost: number
  vehicleType: string
  effectiveData: Date
  expirationData: Date
  createdBy: string
  createdTime?: Date
}

export interface CostExportReqVO {
  id?: number
  costType: number
  cost: number
  vehicleType: string
  effectiveData: Date
  expirationData: Date
  createdBy: string
  createdTime?: Date
}

// 查询费用配置列表
export const getCostPageApi = async (params) => {
  return await request.get({ url: '/uctp/configuration/cost/page', params })
}

// 获取费用类型
export const getAcquireApi = async (type: number) => {
  return await request.get({ url: '/uctp/configuration/cost/acquire?type=' + type })
}

// 查询费用配置详情
export const getCostApi = async (id: number) => {
  return await request.get({ url: '/uctp/configuration/cost/get?id=' + id })
}

// 新增费用配置
export const createCostApi = async (data: CostVO) => {
  return await request.post({ url: '/uctp/configuration/cost/create', data })
}

// 修改费用配置
export const updateCostApi = async (data: CostVO) => {
  return await request.put({ url: '/uctp/configuration/cost/update', data })
}

// 导出费用配置
export const exportCostApi = async (params: CostExportReqVO) => {
  return await request.download({ url: '/uctp/configuration/cost/export', params })
}
