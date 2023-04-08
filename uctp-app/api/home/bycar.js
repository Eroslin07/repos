import request from '@/utils/request'

// 识别行驶证
export function getVehicleLicense(data) {
	return request({
		url: '/system/appAuth/orcVehicleLicense',
		'method': 'POST',
		'data': data
	})
}

// 保存车辆信息
export function setCarInfo(data) {
	return request({
		url: '/uctp/car-info/insertCarInfo',
		'method': 'POST',
		'data': data
	})
}

// 保存卖家信息
export function setSellerInfo(data) {
	return request({
		url: '/uctp/car-info/insertSellerInfo',
		'method': 'POST',
		'data': data
	})
}

// 
export function getQiyuesuo() {
	return request({
		url: '/uctp/car-info/qiyuesuo',
		'method': 'GET'
	})
}