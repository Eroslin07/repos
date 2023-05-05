import request from '@/config/axios'

export interface RecordExcelReqVO {
  revision?: number
  number?: number
  name?: string
  first?: string
  field?: string
  merchant?: string
  type?: string
  amount?: string
  status?: string
  initiationTime?: Date[]
  signTime?: Date[]
}

// 查询合同档案信息列表
export const getRecordPageApi = async (params) => {
  return await request.get({ url: '/uctp/contract/record/page', params })
}

// 导出合同档案信息 Excel
export const exportRecordApi = async (params: RecordExcelReqVO) => {
  return await request.download({ url: '/uctp/contract/record/export', params })
}
