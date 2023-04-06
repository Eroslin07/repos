import request from '@/utils/request'

export const getHomePageList = (params) => {
	return request({
		url: '/uctp/car-info/home/page',
		method: 'GET',
		params
	})
}

// 获得App首页统计
export const getHomeCount = () => {
	return request({
		url: `/uctp/car-info/home/count`,
		method: 'GET'
	})
}
