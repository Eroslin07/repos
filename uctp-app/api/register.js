import request from '@/utils/request'

// 注册方法
export function register(data) {
	return request({
		url: '/system/auth/registerAccount',
		headers: {
			isToken: false
		},
		'method': 'POST',
		'data': data
	})
}

// 查询市场所在地
export function getTenantlist() {
	return request({
		url: '/system/tenant/getTenantlist',
		headers: {
			isToken: false,
			ContentType: 'application/x-www-form-urlencoded'
		},
		'method': 'GET'
	})
}

// 获取验证码
export function getCode(data) {
	return request({
		url: '/system/auth/lsend-sms-code',
		headers: {
			isToken: false
		},
		'method': 'POST',
		'data': data
	})
}

// 识别身份证
export function getIdCard(data) {
	return request({
		url: '/system/auth/orcIdCard',
		headers: {
			isToken: false
		},
		'method': 'POST',
		'data': data
	})
}

// 识别营业执照
export function getBusinessLicense(data) {
	return request({
		url: '/system/auth/orcBusinessLicense',
		headers: {
			isToken: false
		},
		'method': 'POST',
		'data': data
	})
}