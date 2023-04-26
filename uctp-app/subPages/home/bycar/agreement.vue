<template>
	<view class="agreement">
		<uni-card :is-shadow="false" is-full>
			<view class="text">
				<image src="../../../static/images/bycar/hetong2.png" class="hetong_image"></image>
				<view style="margin-top: 20px;" @click="handleLook">
					<image src="../../../static/images/bycar/hetong1.png" class="form-image"
						style="width: 16pt;height: 16pt;"></image>
					<text @click="handleViewContract">《二手车收购协议》</text>
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
		getCancelContract,
		getContractEcho
	} from '@/api/home/bycar.js'
	export default {
		data() {
			return {
				checkboxValue: null,
				carId: '',
				// 合同详情
				contractDtail: [],
			}
		},
		onLoad(options) {
			console.log((options))
			this.carId = options.carId
			this.getContractUrl()
		},
		methods: {
			// 查看
			handleLook() {
				// this.$tab.navigateTo('/subPages/common/agreement/index?type=' + '收车');
			},
			// 合同签章
			handleAffirm() {
				const data = {
					...this.contractDtail.find(v=>v.contractType=='1'),
					type:'1'
				}
				getQiyuesuo(data).then((res) => {
					this.$tab.navigateTo(`/subPages/common/webview/index?title=收车合同签章&url=${res.data}`);
				})
			},
			// 取消合同签章
			handleCancel() {
				this.$tab.navigateBack()
			},
			// 关闭
			handleClose() {
				this.$tab.reLaunch('/pages/index');
			},
			getContractUrl() {
				let data = `carId=${this.carId}&&type=1`
				getContractEcho(data).then(res => {
					console.log(res.data)
					this.contractDtail = res.data
				}).catch(err => {
					this.$modal.msg('获取合同失败')
				})
			},
			// 查看合同
			handleViewContract() {
				let url=this.contractDtail.find(v=>v.contractType=='2')?.url
				uni.downloadFile({
					url: url,
					success: function(res) {
						var filePath = res.tempFilePath;
						uni.openDocument({
							filePath: filePath,
							showMenu: false,
							success: function(res) {
								console.log('打开文档成功');
							}
						});
					}
				});
			},

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
