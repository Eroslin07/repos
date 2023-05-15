import request from '@/config/axios'

export interface TaxVO {
  id?: number
  taxType: string
  taxRate: string
  effectiveData: Date
  expirationData: Date
  createdBy: string
  createdTime?: Date
}

export interface TaxExportReqVO {
  id?: number
  taxType: string
  taxRate: string
  effectiveData: Date
  expirationData: Date
  createdBy: string
  createdTime?: Date
}

// 查询税费列表
export const getTaxPageApi = async (params) => {
  return await request.get({ url: '/uctp/configuration/tax-config/page', params })
}

// 获取所有税率
export const getAcquireApi = async (type: String) => {
  return await request.get({ url: '/uctp/configuration/tax-config/acquire?type=' + type })
}

// 查询税费详情
export const getTaxApi = async (id: number) => {
  return await request.get({ url: '/uctp/configuration/tax-config/get?id=' + id })
}

// 新增税费
export const createTaxApi = async (data: TaxVO) => {
  return await request.post({ url: '/uctp/configuration/tax-config/create', data })
}

// 修改税费
export const updateTaxApi = async (data: TaxVO) => {
  return await request.put({ url: '/uctp/configuration/tax-config/update', data })
}

// 导出税费
export const exportTaxApi = async (params: TaxExportReqVO) => {
  return await request.download({ url: '/uctp/configuration/tax-config/export', params })
}
