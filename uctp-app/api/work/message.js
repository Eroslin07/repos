import request from '@/utils/request'

export const getNoticesApi = () => {
	return request({
		url: '/uctp/car-notice/getNotices',
		method: 'GET',
		header: {
			'tenant-id': '1'
		}
	})
}
