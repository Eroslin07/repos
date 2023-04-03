import request from '@/utils/request'

// 注册方法
export function register(data) {
	return request({
		url: '',
		headers: {
			isToken: false
		},
		'method': 'POST',
		'data': data
	})
}