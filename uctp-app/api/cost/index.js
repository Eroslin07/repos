import request from '@/utils/request'

// 费用分析
export function getCostList(data) {
	return request({
		url: '/uctp/account/profit/cost/list',
		'method': 'GET',
		'params': data
	})
}