import request from '@/config/axios'

// 查询列表
export const getInvoice = async (params) => {
  return await request.get({ url: '/uctp/settlement/invoice/page', params })
}

// 导出
export const exportTack = async (params) => {
  return await request.download({ url: '/uctp/settlement/invoice/export', params })
}
