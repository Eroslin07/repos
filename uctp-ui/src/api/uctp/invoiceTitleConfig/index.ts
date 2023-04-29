import request from '@/config/axios'

export interface InvoiceTitleConfigVO {
  revision: number
  id: string
  name: string
  taxNum: string
  tel: string
  address: string
  bank: string
  bankaccount: string
  createdBy: string
  createdTime: Date
  updatedBy: string
  updatedTime: Date
}

export interface InvoiceTitleConfigPageReqVO extends PageParam {
  revision?: number
  name?: string
  taxNum?: string
  tel?: string
  address?: string
  bank?: string
  bankaccount?: string
  createdBy?: string
  createdTime?: Date[]
  updatedBy?: string
  updatedTime?: Date[]
}

export interface InvoiceTitleConfigExcelReqVO {
  revision?: number
  name?: string
  taxNum?: string
  tel?: string
  address?: string
  bank?: string
  bankaccount?: string
  createdBy?: string
  createdTime?: Date[]
  updatedBy?: string
  updatedTime?: Date[]
}

// 查询开票信息配置列表
export const getInvoiceTitleConfigPageApi = async (params: InvoiceTitleConfigPageReqVO) => {
  return await request.get({ url: '/uctp/invoice-title-config/page', params })
}

// 查询开票信息配置详情
export const getInvoiceTitleConfigApi = async (id: number) => {
  return await request.get({ url: '/uctp/invoice-title-config/get?id=' + id })
}

// 新增开票信息配置
export const createInvoiceTitleConfigApi = async (data: InvoiceTitleConfigVO) => {
  return await request.post({ url: '/uctp/invoice-title-config/create', data })
}

// 修改开票信息配置
export const updateInvoiceTitleConfigApi = async (data: InvoiceTitleConfigVO) => {
  return await request.put({ url: '/uctp/invoice-title-config/update', data })
}

// 删除开票信息配置
export const deleteInvoiceTitleConfigApi = async (id: number) => {
  return await request.delete({ url: '/uctp/invoice-title-config/delete?id=' + id })
}

// 导出开票信息配置 Excel
export const exportInvoiceTitleConfigApi = async (params: InvoiceTitleConfigExcelReqVO) => {
  return await request.download({ url: '/uctp/invoice-title-config/export-excel', params })
}
