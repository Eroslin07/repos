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

// 合同签章
export function getQiyuesuo() {
	return request({
		url: '/uctp/car-info/qiyuesuo',
		'method': 'GET'
	})
}

// 取消合同签章
export function getCancelContract() {
	return request({
		url: '/uctp/car-info/cancelContract',
		'method': 'GET'
	})
}

// 查询公允价值
export function getFairValue(data) {
	return request({
		url: '/uctp/car-info/searchFairValue',
		'method': 'POST',
		'data': data
	})
}

// 查询品牌id
export function getCarBrandList(data) {
	return request({
		url: '/uctp/car-info/getCarBrandList',
		'method': 'POST',
		'data': data
	})
}

// 查询车系
export function getCarSeriesList(data) {
	return request({
		url: '/uctp/car-info/getCarSeriesList',
		'method': 'POST',
		'data': data
	})
}

// 查询车型
export function getCarModelList(data) {
	return request({
		url: '/uctp/car-info/getCarModelList',
		'method': 'POST',
		'data': data
	})
}