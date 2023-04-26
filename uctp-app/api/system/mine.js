import request from '@/utils/request'

// 查询用户个人信息
export function getUserInfo(data) {
  return request({
    url: '/system/appAuth/getUserInfo',
    method: 'GET',
		params: data
  })
}

// 获取子账户列表
export function getAccountList(data) {
  return request({
    url: '/system/appAuth/getAccountList',
    method: 'GET',
		params: data
  })
}

// 删除子账号
export function deleteAccount(data) {
  return request({
    url: '/system/appAuth/deleteAccount',
    method: 'GET',
		params: data
  })
}

// 新增/修改子账号
export function setAccount(data) {
  return request({
    url: '/system/appAuth/addAccount',
    method: 'POST',
		data: data
  })
}

// 重新认证
export function getAuth(data) {
  return request({
    url: '/uctp/qys/user/auth',
    method: 'POST',
		data: data
  })
}