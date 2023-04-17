import request from '@/utils/request'

// 查询我的保证金
export function getDetail(data) {
	return request({
		url: '/uctp/account/cash/detail',
		'method': 'POST',
		'params': data
	})
}

// 保证金交易明细
export function getCarhList(data) {
	return request({
		url: '/uctp/account/cash/list',
		'method': 'POST',
		'data': data
	})
}