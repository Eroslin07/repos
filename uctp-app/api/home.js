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

// 流程发起
export const setCreate = (data) => {
	return request({
		url: `/bpm/app/process-instance/v3/create`,
		method: 'POST',
		data
	})
}

// 获得车辆状态
export const  getStatusList=(data)=>{
	return request({
		url:`/system/dict-data/list-map`,
		method:'GET',
		params:data
	})
}