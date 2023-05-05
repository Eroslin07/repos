import request from '@/config/axios'

export interface InvoiceVO {
  revision: number
  id: number
  companyName: string
  taxNum: string
  represent: string
  address: string
  tel: string
  opening: string
  name: string
  bank: string
  bankAccount: string
}

// 新建发票配置信息
export const createMakeInvoicePageApi = async (data: InvoiceVO) => {
  return await request.post({ url: '/uctp/configuration/makeinvoice/create', data })
}

// 编辑发票配置信息
export const updateMakeInvoiceApi = async (data: InvoiceVO) => {
  return await request.put({ url: '/uctp/configuration/makeinvoice/update', data })
}

// 查询发票配置信息详情
export const getMakeInvoiceApi = async (tenantId: number) => {
  return await request.get({ url: '/uctp/configuration/makeinvoice/get?tenantId=' + tenantId })
}
