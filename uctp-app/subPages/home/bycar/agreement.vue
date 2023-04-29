<template>
	<view class="agreement">
		<uni-card v-if="!isSHowTip" :is-shadow="false" is-full>
			<view class="contract-box">
				<view class="text" @click="handleViewContract('1')">
					<image src="../../../static/images/bycar/entrust.png" class="hetong_image"></image>
					<view style="margin-top: 20px;">
						<u-checkbox-group v-model="entrustValue" activeColor="#fe7345">
							<u-checkbox labelColor="#fa6400" :label="entrustName" name="委托收购协议">
							</u-checkbox>
						</u-checkbox-group>
					</view>
				</view>
				<view class="text" @click="handleViewContract('2')">
					<image src="../../../static/images/bycar/coll-contract.png" class="hetong_image"></image>
					<view style="margin-top: 20px;">
						<u-checkbox-group v-model="contractValue" activeColor="#fe7345">
							<u-checkbox labelColor="#fa6400" :label="contractName" name="收购协议">
							</u-checkbox>
						</u-checkbox-group>
					</view>
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
				checkboxValue: null,
				carId: '',
				// 合同详情
				contractDtail: [],
				isSHowTip: '',
				contractName: '收购协议',
				entrustName: '委托收购协议',

				contractValue: [],
				entrustValue: [],
			}
		},
		components: {
			AbnormalPage
		},
		onLoad(options) {
			console.log((options))
			this.carId = options.carId
			// 1650784037801914369
			this.getContractUrl()
		},
		methods: {
			// 查看合同
			handleViewContract(text) {
				this.$modal.msg('正在加载，请稍等...')
				let url = this.contractDtail.find(v => v.contractType == text)?.url
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
					fail: () => {
						this.$modal.msg('加载失败！')
					}
				});
			},
			// 合同签章
			handleAffirm() {
				if(!this.contractValue.length || !this.entrustValue.length) return this.$modal.msg('请勾选协议和委托协议！')
				this.$modal.msg('正在加载，请稍等...')
				let data = {
					...this.contractDtail.find(v => v.contractType == '1')
				}
				getQiyuesuo(data.contractId).then((res) => {
					if (res.data) {
						this.$tab.navigateTo(`/subPages/common/webview/index?title=收车合同签章&url=${res.data}`);
					}
				}).catch(() => {
					// this.$modal.msg('加载失败！')
				})
			},
			// 取消合同签章
			handleCancel() {
				this.$tab.navigateBack()
			},
			// 关闭
			handleClose() {
				this.$tab.switchTab('/pages/index');
			},
			getContractUrl() {
				this.isSHowTip = 'onLoading'
				let data = `carId=${this.carId}&&type=1`
				getContractEcho(data).then(res => {
					this.contractDtail = res.data
					this.contractName = res.data.find(v => v.contractType == '2')?.contractName || '收购协议'
					this.entrustName = res.data.find(v => v.contractType == '1')?.contractName || '委托收购协议'
				}).catch(err => {
					this.$modal.msg('获取合同失败')
				}).finally(() => {
					this.isSHowTip = ''
				})
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
	.contract-box {
		display: flex;
		flex-direction: row;
		align-items: center;
		justify-content: space-around;
	}

	.hetong_image {
		width: 185rpx;
		height: 160rpx;
	}

	.text {
		padding: 200rpx 0;
		font-size: 30rpx;
		text-align: center;
		color: #fa6400;
	}

	.button {
		margin-top: 10px;
		background-color: #fa6400;
		color: #fff;
	}
</style>
