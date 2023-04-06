<template>
	<view class="work-container">
		<uni-list-chat
			v-for="(item, index) in data"
			:key="index"
			:avatar-circle="true"
			title="交易成功"
			:avatar="item.image"
			note="您收到一条新的消息"
			time="2020-02-02 20:20"
		></uni-list-chat>
	</view>
</template>

<script>
	export default {
		data() {
			return {
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
		methods: {
			
		}
	}
</script>

<style lang="scss">
	
</style>
