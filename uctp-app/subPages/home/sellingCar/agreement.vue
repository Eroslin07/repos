<template>
	<view class="agreement">
		<uni-card v-if="!isSHowTip" :is-shadow="false" is-full>
			<view class="text">
				<image src="../../../static/images/bycar/hetong2.png" class="hetong_image"></image>
				<view style="margin-top: 20px;"  @click="handleViewContract">
					<image src="../../../static/images/bycar/hetong1.png" class="form-image" style="width: 16pt;height: 16pt;"></image>
					<text>{{ contractName ||'《二手车销售协议》'}}</text>
				</view>
			</view>
			<!-- 底部按钮 -->
			<button @click="handleAffirm" class="button" style="background-color: #fff;color: #333;">合同签章</button>
			<button @click="handleCancel" class="button" style="background-color: #fff;color: #333;">取消合同签章</button>
			<button @click="handleClose" class="button">保存并关闭</button>
		</uni-card>
		<!-- 提示信息 -->
		<AbnormalPage v-else :isSHowTip="isSHowTip" />
	</view>
</template>

<script>
	import {
		getQiyuesuo,
		getCancelContract,
		getContractEcho
	} from '@/api/home/bycar.js'
	import AbnormalPage from '@/subPages/common/abnormaPage/index.vue'
	export default {
		data() {
			return {
				checkboxValue: [],
				isSHowTip:'',
				contractDtail:[],
				carId:'',
				contractName:''
			}
		},
		components: {
			AbnormalPage
		},
		onLoad(options){
			this.carId = options.carId
			this.getContractUrl()
		},
		methods: {
			// 获取合同
			getContractUrl() {
				this.isSHowTip = 'onLoading'
				let data = `carId=${this.carId}&&type=2`
				getContractEcho(data).then(res => {
					this.contractDtail = res.data
					this.contractName=res.data.find(v=>v.contractType=='2')?.contractName;
				}).catch(err => {
					this.$modal.msg('获取合同失败')
				}).finally(()=>{
					this.isSHowTip = ''
				})
			},
			
			// 查看
			handleViewContract() {
				this.$modal.msg('正在加载，请稍等...')
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
					},
					fail:()=>{
						this.$modal.msg('加载失败！')
					}
				});
			},
			// 合同签章
			handleAffirm() {
				this.$modal.msg('正在加载，请稍等...')
				let data={
					...this.contractDtail.find(v=>v.contractType=='1')
				}
				getQiyuesuo().then((res) => {
					this.$tab.navigateTo(`/subPages/common/webview/index?title=卖车合同签章&url=${res.data}`);
				}).catch(()=>{
					this.$modal.msg('加载失败！')
				})
			},
			// 取消合同签章
			handleCancel() {
				// getCancelContract().then((res) => {
				// 	this.$tab.navigateTo(`/subPages/common/webview/index?title=取消卖车合同签章&url=${res.data}`);
				// })
				this.$tab.navigateBack();
			},
			// 关闭
			handleClose() {
				this.$tab.switchTab('/pages/index');
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