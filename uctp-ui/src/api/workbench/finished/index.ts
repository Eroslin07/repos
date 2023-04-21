import request from '@/config/axios'

// 已办列表
export const getFinished = async (params) => {
  return await request.get({ url: '/bpm/task/v2/done-page', params })
}
