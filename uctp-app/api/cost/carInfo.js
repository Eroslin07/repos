import request from '@/utils/request'

// 详情
export function getCarInfoById(data) {
	return request({
		url: '/uctp/car-info/getCardByID',
		'method': 'GET',
		'params': data
	})
}
// 合同作废
export function contractInvalid(query){
	return request({
		url:`/uctp/contract/contract/invalid?${query}`,
		method:'POST'
	})
}