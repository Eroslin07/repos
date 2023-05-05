import request from '@/config/axios'

export interface ContractTempVO {
  revision: number
  id: string
  number: string
  name: string
  type: string
  remark: string
  status: string
  createdBy: string
  createdTime: Date
  updatedBy: string
  updatedTime: Date
}

export interface ContractTempExcelReqVO {
  revision?: number
  number?: string
  name?: string
  type?: string
  remark?: string
  status?: string
  createdBy?: string
  createdTime?: Date[]
  updatedBy?: string
  updatedTime?: Date[]
}

// 查询合同模板信息列表
export const getContractTempPageApi = async (params) => {
  return await request.get({ url: '/uctp/contract/temp/page', params })
}

// 修改合同模板信息
export const updateContractTempApi = async (data: ContractTempVO) => {
  return await request.put({ url: '/uctp/contract/temp/update', data })
}

// 查询合同模板信息详情
export const getContractTempApi = async (id: number) => {
  return await request.get({ url: '/uctp/contract/temp/get?id=' + id })
}

// 删除合同模板信息
export const deleteContractTempApi = async (id: number) => {
  return await request.delete({ url: '/uctp/contract/temp/delete?id=' + id })
}

// 导出合同模板信息 Excel
export const exportContractTempApi = async (params: ContractTempExcelReqVO) => {
  return await request.download({ url: '/uctp/contract/temp/export', params })
}
