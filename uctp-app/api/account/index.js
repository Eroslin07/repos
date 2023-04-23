import request from '@/utils/request'

// 查询资金管理
export function getAccount() {
	return request({
		url: '/uctp/account/get',
		'method': 'GET'
	})
}