<template>
	<view class="agreement">
		<uni-card :is-shadow="false" is-full>
			<view class="text">
				<image src="../../../static/images/bycar/hetong2.png" class="hetong_image"></image>
				<view style="margin-top: 20px;"  @click="handleLook">
					<image src="../../../static/images/bycar/hetong1.png" class="form-image" style="width: 16pt;height: 16pt;"></image>
					<text>《2021年03月20日收车合同》</text>
				</view>
			</view>
			<!-- 底部按钮 -->
			<button @click="handleAffirm" class="button" style="background-color: #fff;color: #333;">合同签章</button>
			<button @click="handleCancel" class="button" style="background-color: #fff;color: #333;">取消合同签章</button>
			<button @click="handleClose" class="button">保存并关闭</button>
		</uni-card>
	</view>
</template>

<script>
	import {
		getQiyuesuo,
		getCancelContract
	} from '@/api/home/bycar.js'
	export default {
		data() {
			return {
				checkboxValue: null
			}
		},
		methods: {
			// 查看
			handleLook() {
				this.$tab.navigateTo('/subPages/common/agreement/index?type=' + '收车');
			},
			// 合同签章
			handleAffirm() {
				const data={
					carId:'1648668268713422850',
					type:'2'
				}
				getQiyuesuo(data).then((res) => {
					this.$tab.navigateTo(`/subPages/common/webview/index?title=收车合同签章&url=${res.data}`);
				})
			},
			// 取消合同签章
			handleCancel() {
				getCancelContract().then((res) => {
					this.$tab.navigateTo(`/subPages/common/webview/index?title=取消收车合同签章&url=${res.data}`);
				})
			},
			// 关闭
			handleClose() {
				this.$tab.reLaunch('/pages/index');
			}
		}
	}
</script>

<style lang="scss" scoped>
	.uni-card--border {
		border: none;
	}

	/* #ifdef MP-WEIXIN */
	/deep/ .uni-card--border {
		border-bottom: none;
	}
	/* #endif */
	
	.hetong_image {
		width: 66pt;
		height: 66pt;
	}

	.text {
		padding: 200rpx 0;
		font-size: 16px;
		text-align: center;
		color: #fa6400;
	}

	.button {
		margin-top: 10px;
		background-color: #fa6400;
		color: #fff;
	}
</style>
