<template>
	<view class="work-container">
		<u-swipe-action>
			<u-swipe-action-item :options="item.options" v-for="(item, index) in listData" :key="index"
				@click="handleSwipe(item,index)">
				<uni-list-chat :avatar-circle="true" badgeText="dot" badgePositon="left" :title="item.title"
					avatar="/static/images/wenjianjia.svg" :note="item.content" :time="item.createTime" clickable
					@click="handleListItem(item,index)">
				</uni-list-chat>
			</u-swipe-action-item>
		</u-swipe-action>

	</view>
</template>

<script>
	import {
		getNoticesApi
	} from '@/api/work/message.js'
	import {
		parseTime
	} from '@/utils/ruoyi.js'
	export default {
		data() {
			return {
				// 列表
				listData: [{
					title: '公允值',
					content: '这是一条新消息这是一条新消息这是一条新消息这是一条新消息这是一条新消息这是一条新消息这是一条新消息这是一条新消息这是一条新消息这是一条新消息这是一条新消息这是一条新消息',
					createTime: parseTime(Number(new Date())),
					options: [{
							text: '删除',
							style: {
								backgroundColor: '#f56c6c',
							}
						}

					]
				}],
				current: 0,
				swiperDotIndex: 0,
				data: [{
						image: '/static/images/car.jpg'
					},
					{
						image: '/static/images/car.jpg'
					},
					{
						image: '/static/images/car.jpg'
					}
				]
			}
		},
		onLoad() {
			let options = [{
				text: '删除',
				style: {
					backgroundColor: '#f56c6c'
				}
			}]
			getNoticesApi().then(res => {
				if (res.data.length) {
					this.listData = res.data.map(item => {
						this.$set(item, 'options', options);
						item.createTime = parseTime(item.createTime);
						return item
					})
				}
			}).catch(err => {
				console.log(err, 'err')
			})
		},
		methods: {
			// 左滑删除
			handleSwipe(item, index) {
				console.log(item, index, 'swipe')
			},
			// 点击消息列表
			handleListItem(item, index) {
				// console.log(item, index, 'list')
				console.log(encodeURIComponent(JSON.stringify(item)))
				this.$tab.navigateTo('/subPages/work/infoDetail?item=' + encodeURIComponent(JSON.stringify(item)))
			},
		}
	}
</script>

<style lang="scss">
	/deep/ .uni-list-chat__header[data-v-3d9bc00b] {
		border: none;
	}
</style>
