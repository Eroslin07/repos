import request from '@/config/axios'

// 待办列表
export const getToDoList = async (params) => {
  return await request.get({ url: '/bpm/task/v2/todo-page', params })
}
