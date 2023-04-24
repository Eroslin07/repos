import request from '@/utils/request'

// 详情
export function getCarInfoById(data) {
	return request({
		url: '/uctp/car-info/getCardByID',
		'method': 'GET',
		'params': data
	})
}