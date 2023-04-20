import request from '@/utils/request'

// 获取消息列表
export const getNoticesApi = (id) => {
	return request({
		url: '/uctp/car-notice/getNotices?businessID='+id,
		method: 'GET',
		// header: {
		// 	'tenant-id': '1'
		// }
	})
}

// 删除
export const deleteNoticeApi = (data) => {
	return request({
		url: `/uctp/car-notice/deleteBatchNoticeStatus`,
		method: 'POST',
		headers: {
			isToken: false
		},
		data
	})
}

//批量已读
export const updateAllNoticeApi = (data) => {
	return request({
		url: `/uctp/car-notice/updateBatchNoticeStatus`,
		method: 'POST',
		data
	})
}

// 单条已读
export const updateNoticeApi = (data) => {
	return request({
		url: `/uctp/car-notice/updateNoticeStatus`,
		method: 'POST',
		headers: {
			isToken: false
		},
		data
	})
}
