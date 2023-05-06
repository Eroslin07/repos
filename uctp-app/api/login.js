import request from '@/utils/request'

// 登录方法
export function login(username, password, captchaVerification) {
	const data = {
		username,
		password,
		captchaVerification
	}
	return request({
		url: '/system/appAuth/appLogin',
		headers: {
			isToken: false
		},
		'method': 'POST',
		'data': data
	})
}

// 登录方法
export function wxLogin(phone) {
	const data = {
		username: phone
	}
	return request({
		url: '/system/appAuth/wxLogin',
		headers: {
			isToken: false
		},
		'method': 'POST',
		'data': data
	})
}

// 获取微信openid
export function getCheckIsLogin(data) {
	return request({
		url: '/system/appAuth/checkIsLogin',
		'method': 'GET',
		params: data
	})
}

// 获取微信授权token
export function getWxToken(data) {
	return request({
		url: '/system/appAuth/wxToken',
		'method': 'GET',
		params: data
	})
}

// 获取用户详细信息
export function getInfo() {
	return request({
		url: '/system/appAuth/get-permission-info',
		'method': 'GET'
	})
}

// 退出方法(旧)
// export function logout() {
// 	return request({
// 		url: '/system/appAuth/logout',
// 		'method': 'POST'
// 	})
// }

// 退出方法(新)
export function logout(data) {
	return request({
		url: '/system/appAuth/logout?wxCode='+data.wxCode,
		'method': 'POST'
	})
}
