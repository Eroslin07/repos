import request from '@/utils/request'

// 利润明细
export function getProfitList(data) {
	return request({
		url: '/uctp/account/profit/list',
		'method': 'GET',
		params: data
	})
}

// 查询利润概要信息
export function getSummary(data) {
	return request({
		url: '/uctp/account/profit/summary',
		'method': 'GET',
		params: data
	})
}

// 查询利润详情
export function getDetail(data) {
	return request({
		url: '/uctp/account/profit/detail',
		'method': 'GET',
		params: data
	})
}

// 利润提现
export function getPresent(data) {
	return request({
		url: '/uctp/account/profit/present',
		'method': 'POST',
		'data': data
	})
}

// 查询待回填保证金明细
export function getCashbackList(data) {
	return request({
		url: '/uctp/account/cashback/list',
		'method': 'GET',
		params: data
	})
}

// 查询待回填保证金详情
export function getCashbackDetail(data) {
	return request({
		url: '/uctp/account/cashback/detail',
		'method': 'GET',
		params: data
	})
}