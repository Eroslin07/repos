<template>
	<view class="agreement">
		<uni-card v-if="!isSHowTip" :is-shadow="false" is-full>
			<view class="contract-box">
				<view class="text" @click="handleViewContract('1')">
					<image src="/subPages/static/images/bycar/entrust.png" class="hetong_image"></image>
					<image v-show="entrustChecked" class="badge" src="/subPages/static/images/bycar/checkedIcons.png"></image>
				</view>
				<view class="text" @click="handleViewContract('2')">
					<image src="/subPages/static/images/bycar/coll-contract.png" class="hetong_image"></image>
					<image v-show="contractChecked" class="badge" src="/subPages/static/images/bycar/checkedIcons.png"></image>
				</view>
			</view>

			<!-- 底部按钮 -->
			<button @click="handleAffirm" class="button" style="background-color: #fff;color: #333;">合同签章</button>
			<button @click="handleClose" class="button">关闭</button>
		</uni-card>
		<!-- 提示信息 -->
		<AbnormalPage v-else :isSHowTip="isSHowTip" />
		<button v-show="isSHowTip=='createFail' || isSHowTip=='signFail'" @click="failBtn"  class="button confitmBtn">确定</button>
		<button v-show="isSHowTip=='signSuccess'" @click="signSucessBtn"  class="button confitmBtn">确定</button>
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
	import {
		getDetail
	} from '@/api/account/bond.js'
	import AbnormalPage from '@/subPages/common/abnormaPage/index.vue'
	export default {
		data() {
			return {
				checkboxValue: null,
				carId: '',
				// 合同详情
				contractDtail: [],
				isSHowTip: '',
				fairVisible: 1,
				jsonData: {},
				fairValue: {
					value1: '',
					value2: ''
				},
				available: '',
				// 委托选中
				entrustChecked:false,
				// 合同选中
				contractChecked:false
			}
		},
		components: {
			AbnormalPage
		},
		onShow() {
			let pages = getCurrentPages();
			let currPage = pages[pages.length - 1];
			if(currPage.__data__.isRefresh){
				// 重新获取数据
				this.getAvailableCash()
				// 每一次需要清除，否则会参数会缓存
				currPage.__data__.isRefresh=false
			}
		},
		onLoad(options) {
			// console.log((options))
			this.carId = options.carId
			this.fairVisible = options.fairVisible
			this.jsonData = JSON.parse(options.data)
			this.fairValue = JSON.parse(options.fairValue)
			this.available = options.available
			// 1650784037801914369
			this.getContractUrl()
		},
		methods: {
			// 查询可用保证金余额
			getAvailableCash() {
				console.log(1)
				getDetail({
					accountNo: this.$store.state.user.accountNo
				}).then((res) => {
					this.available = res.data.availableCash / 100;
				})
			},
			// 查看合同
			handleViewContract(text) {
				this.$modal.msg('正在加载，请稍等...')
				let _this=this
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
								setTimeout(() => {
									if (text == '1') {
										_this.entrustChecked = true;
									} else {
										_this.contractChecked = true;
									}
								}, 1000)
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
				if(!this.entrustChecked || !this.contractChecked) return this.$modal.msg('请查看协议和合同！')
				if (this.jsonData.carInfo.vehicleReceiptAmount > this.available) return uni.showModal({
					title: '提示',
					content: `收车金额${this.$amount.getComdify(this.jsonData.carInfo.vehicleReceiptAmount)}元超过保证金余额${this.$amount.getComdify(this.available)}元，余额不足，不能发起收车，请充值后再发起收车操作。`,
					cancelText: '取消',
					confirmText: '前往充值',
					confirmColor: '#fa6401',
					success(res){
						_this.$tab.navigateTo('/subPages/home/account/bond/recharge');
					}
				})
				if (this.fairVisible == 0) return uni.showModal({
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
				// this.$modal.msg('正在加载，请稍等...')
				this.isSHowTip='signing'
				let data = {
					...this.contractDtail.find(v => v.contractType == '1')
				}
				getQiyuesuo(data).then((res) => {
					if (res.data) {
						// this.$modal.msg("合同已签章");
						this.isSHowTip='signSuccess'
						// this.$tab.switchTab('/pages/index');
						// this.$tab.navigateTo(`/subPages/common/webview/index?title=收车合同签章&url=${res.data}`);
					}
				}).catch(() => {
					// this.$modal.msg('加载失败！')
					this.isSHowTip='signFail'
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
				this.isSHowTip = 'creating'
				let data = `carId=${this.carId}&&type=1`
				getContractEcho(data).then(res => {
					this.isSHowTip = ''
					this.contractDtail = res.data
				}).catch(err => {
					// this.$modal.msg('获取合同失败')
					this.isSHowTip = 'createFail'
				})
			},
			// 合同生成失败
			failBtn(){
				this.$tab.navigateBack();
				this.isSHowTip = ''
			},
			// 合同签属成功
			signSucessBtn(){
				this.$tab.switchTab('/pages/index');
				this.isSHowTip = ''
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
	.contract-box {
		display: flex;
		flex-direction: row;
		align-items: center;
		justify-content: space-around;
		.text {
			margin:200rpx auto;
			font-size: 30rpx;
			text-align: center;
			color: #fa6400;
			position:relative;
			.hetong_image {
				width: 170rpx;
				height: 190rpx;
			}
			.badge{
				position:absolute;
				width:38rpx;
				height: 38rpx;
				top:0;
				left:0;
			}
		}
		
	}
	.button {
		margin-top: 10px;
		background-color: #fa6400;
		color: #fff;
	}
	.confitmBtn{
		margin:80rpx 30rpx 0;
		color:#000;
		background-color: #fff;
		font-size: 32rpx;
	}
</style>