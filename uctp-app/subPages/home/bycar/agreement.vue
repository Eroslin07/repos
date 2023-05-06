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
			<button @click="handleClose" class="button">关闭</button>
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
	import {
		setCreate
	} from '@/api/home'
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
				fairVisible: true,
				contractValue: [],
				entrustValue: [],
				jsonData: {},
				fairValue: {
					value1: '',
					value2: ''
				},
				available: ''
			}
		},
		components: {
			AbnormalPage
		},
		onLoad(options) {
			console.log((options))
			this.carId = options.carId
			this.fairVisible = options.fairVisible
			this.jsonData = JSON.parse(options.data)
			this.fairValue = JSON.parse(options.fairValue)
			this.available = options.available
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
				let _this = this
				if (!this.contractValue.length || !this.entrustValue.length) return this.$modal.msg('请勾选协议和委托协议！')
				if (this.jsonData.carInfo.vehicleReceiptAmount > this.available) return uni.showModal({
					title: '提示',
					content: `收车金额${this.$amount.getComdify(this.jsonData.carInfo.vehicleReceiptAmount)}元超过保证金余额${this.$amount.getComdify(this.available)}元，余额不足，不能发起收车，请充值后再发起收车操作。`,
					showCancel: false,
					confirmText: '知道了',
					confirmColor: '#fa6401',
					success(res){
						_this.$tab.switchTab('/pages/index');
					}

				})
				if (!this.fairVisible) return uni.showModal({
					title: '提示',
					content: '您的收车价格不在市场评估价格之内，继续提交会触发平台方审核，是否继续提交？',
					confirmText: '是',
					cancelText: '否',
					confirmColor: '#fa6401',
					success(ress) {
						if (ress.confirm) {
							_this.handleFair();
						}
					}
				})
				this.$modal.msg('正在加载，请稍等...')
				let data = {
					...this.contractDtail.find(v => v.contractType == '1')
				}
				getQiyuesuo(data).then((res) => {
					if (res.data) {
						this.$modal.msg("合同已签章");
						this.$tab.switchTab('/pages/index');
						// this.$tab.navigateTo(`/subPages/common/webview/index?title=收车合同签章&url=${res.data}`);
					}
				}).catch(() => {
					// this.$modal.msg('加载失败！')
				})
			},
			handleFair() {
				this.$modal.loading("提交中，请耐心等待...");
				// 发起公允值审批流程
				let procDefKey = "SGYZ";
				this.jsonData.fairValue = this.fairValue;
				let variables = {
					marketName: this.$store.state.user.tenantName,
					merchantName: this.$store.state.user.deptName,
					startUserId: this.$store.state.user.id,
					formDataJson: {
						formMain: {
							merchantId: this.$store.state.user.deptId,
							thirdId: this.jsonData.carInfoDetails.carId,
							formDataJson: this.jsonData
						}
					}
				}
				let createData = {
					procDefKey,
					variables
				};
				setCreate(createData).then((ress) => {
					this.$modal.closeLoading()
					this.showOverlay = false;
					this.$modal.msg("已提交审核");
					this.$tab.switchTab('/pages/index');
				}).catch((error) => {
					this.$modal.closeLoading()
					this.showOverlay = false;
					// this.$modal.msgError("发起流程失败");
				})

			},
			// 取消合同签章
			// handleCancel() {
			// 	this.$tab.navigateBack()
			// },
			// 关闭
			handleClose() {
				this.$tab.switchTab('/pages/index');
			},
			getContractUrl() {
				let starTime=new Date().getTime();
				this.isSHowTip = 'creating'
				let data = `carId=${this.carId}&&type=1`
				getContractEcho(data).then(res => {
					this.contractDtail = res.data
					this.contractName = res.data.find(v => v.contractType == '2')?.contractName || '收购协议'
					this.entrustName = res.data.find(v => v.contractType == '1')?.contractName || '委托收购协议'
				}).catch(err => {
					this.$modal.msg('获取合同失败')
				}).finally(() => {
					let endTime=new Date().getTime();
					if(endTime-starTime>1500){
						this.isSHowTip = ''
					}else{
						setTimeout(()=>{
							this.isSHowTip = ''
						},1500)
					}
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