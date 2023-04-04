/**
 * 通用js方法封装处理
 * Copyright (c) 2019 ruoyi
 */

// 日期格式化
export function parseTime(time, pattern) {
	if (arguments.length === 0 || !time) {
		return null
	}
	const format = pattern || '{y}-{m}-{d} {h}:{i}:{s}'
	let date
	if (typeof time === 'object') {
		date = time
	} else {
		if ((typeof time === 'string') && (/^[0-9]+$/.test(time))) {
			time = parseInt(time)
		} else if (typeof time === 'string') {
			time = time.replace(new RegExp(/-/gm), '/').replace('T', ' ').replace(new RegExp(/\.[\d]{3}/gm), '');
		}
		if ((typeof time === 'number') && (time.toString().length === 10)) {
			time = time * 1000
		}
		date = new Date(time)
	}
	const formatObj = {
		y: date.getFullYear(),
		m: date.getMonth() + 1,
		d: date.getDate(),
		h: date.getHours(),
		i: date.getMinutes(),
		s: date.getSeconds(),
		a: date.getDay()
	}
	const time_str = format.replace(/{(y|m|d|h|i|s|a)+}/g, (result, key) => {
		let value = formatObj[key]
		// Note: getDay() returns 0 on Sunday
		if (key === 'a') {
			return ['日', '一', '二', '三', '四', '五', '六'][value]
		}
		if (result.length > 0 && value < 10) {
			value = '0' + value
		}
		return value || 0
	})
	return time_str
}

// 图片转base64方法
export function urlTobase64(url) {
	// #ifdef MP-WEIXIN
	return new Promise((resolve, reject) => {
		uni.getFileSystemManager().readFile({
			filePath: url, //选择图片返回的相对路径
			encoding: 'base64', //编码格式
			success: res => { //成功的回调
				let base64 = 'data:image/jpeg;base64,' + res.data //不加上这串字符，在页面无法显示的哦
				resolve(base64)
			},
			fail: (e) => {
				console.log("图片转换失败");
			}
		})
	})
	// #endif

	// #ifndef MP-WEIXIN
	return new Promise((resolve, reject) => {
		uni.request({
			url: url,
			method: 'GET',
			responseType: 'arraybuffer',
			success: ress => {
				let base64 = wx.arrayBufferToBase64(ress.data); //把arraybuffer转成base64 
				base64 = 'data:image/jpeg;base64,' + base64 //不加上这串字符，在页面无法显示的哦
				resolve(base64)
			},
			fail: (e) => {
				console.log("图片转换失败");
			}
		})
	})
	// #endif
}