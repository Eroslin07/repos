import request from '@/utils/request'

// 获取卖车页面
export function getSellPage(data) {
	return request({
		url: '/uctp/car-info/sell/page',
		'method': 'GET',
		'params': data
	})
}

// 查询车辆详情
export function getSellCarInfo(data) {
	return request({
		url: '/uctp/car-info/get/sell',
		'method': 'GET',
		params: data
	})
}

// 查询车辆明细金额数据
export function getAmount(data) {
	return request({
		url: '/uctp/car-info/amount',
		'method': 'GET',
		params: data
	})
}


// 保存卖车信息
export function setSellCarInfo(data) {
	return request({
		url: '/uctp/car-info/save/sell',
		'method': 'POST',
		data: data
	})
}
