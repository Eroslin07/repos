import request from '@/utils/request'

// 查询我的保证金
export function getDetail(data) {
	return request({
		url: '/uctp/account/cash/detail',
		'method': 'GET',
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

// 保证金提现
export function getWithdraw(data) {
	return request({
		url: '/uctp/account/cash/withdraw',
		'method': 'POST',
		'data': data
	})
}

// 保证金提现
export function getRecharge(data) {
	return request({
		url: '/uctp/account/cash/recharge',
		'method': 'POST',
		'data': data
	})
}