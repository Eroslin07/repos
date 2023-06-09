import request from '@/config/axios'

export interface DeptVO {
  id?: number
  name: string
  parentId: number
  status: number
  sort: number
  leaderUserId: number
  phone: string
  email: string
  createTime: Date
}

export interface DeptPageReqVO {
  name?: string
  status?: number
}

export interface DeptUserUpdateReqVO {
  id?: number
  status?: number
}

// 查询部门（精简)列表
export const listSimpleDeptApi = async () => {
  return await request.get({ url: '/system/dept/list-all-simple' })
}

// 查询部门列表
export const getDeptPageApi = async (params: DeptPageReqVO) => {
  return await request.get({ url: '/system/dept/list', params })
}

// 查询部门详情
export const getDeptApi = async (id: number) => {
  return await request.get({ url: '/system/dept/get?id=' + id })
}

// 新增部门
export const createDeptApi = async (data: DeptVO) => {
  return await request.post({ url: '/system/dept/create', data: data })
}

// 修改部门
export const updateDeptApi = async (params: DeptVO) => {
  return await request.put({ url: '/system/dept/update', data: params })
}

// 删除部门
export const deleteDeptApi = async (id: number) => {
  return await request.delete({ url: '/system/dept/delete?id=' + id })
}

// 根据部门状态修改用户状态
export const updateDeptUsersApi = async (params: DeptUserUpdateReqVO) => {
  return await request.put({ url: '/system/dept/update_state', data: params })
}

// 企业认证详情
export const certificationDeptApi = async (userId: number) => {
  return await request.post({ url: '/uctp/certification/enterprise?userId=' + userId })
}
