<template>
	<view class="agreement">
		<uni-card v-if="!isSHowTip" :is-shadow="false" is-full>
			<view class="contract-box">
				<view class="text" @click="handleViewContract('1')">
					<image src="../../../static/images/bycar/entrust.png" class="hetong_image"></image>
					<view style="margin-top: 20px;">
						<u-checkbox-group v-model="entrustValue" activeColor="#fe7345">
							<u-checkbox labelColor="#fa6400" :label="entrustName" name="委托销售协议">
							</u-checkbox>
						</u-checkbox-group>
					</view>
				</view>
				<view class="text"  @click="handleViewContract('2')">
					<image src="../../../static/images/bycar/sell-contract.png" class="hetong_image"></image>
					<view style="margin-top: 20px;">
						<u-checkbox-group v-model="contractValue" activeColor="#fe7345">
							<u-checkbox labelColor="#fa6400" :label="contractName" name="二手车销售协议">
							</u-checkbox>
						</u-checkbox-group>
					</view>
				</view>
			</view>
			
			<!-- 底部按钮 -->
			<button @click="handleAffirm" class="button" style="background-color: #fff;color: #333;">合同签章</button>
			<!-- <button @click="handleCancel" class="button" style="background-color: #fff;color: #333;">取消合同签章</button> -->
			<button @click="handleClose" class="button">关闭</button>
		</uni-card>
		<!-- 提示信息 -->
		<AbnormalPage v-else :isSHowTip="isSHowTip" />
		<button v-show="isSHowTip=='createFail'|| isSHowTip=='signFail'" @click="failBtn"  class="button confitmBtn">确定</button>
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
	import AbnormalPage from '@/subPages/common/abnormaPage/index.vue'
	export default {
		data() {
			return {
				checkboxValue: [],
				isSHowTip:'',
				contractDtail:[],
				carId:'',
				contractName: '二手车销售协议',
				entrustName: '委托销售协议',
				contractValue: [],
				entrustValue: [],
				fairValue: undefined,
				carData: undefined,
				gxzStatus: 1,
			}
		},
		components: {
			AbnormalPage
		},
		onLoad(options){
			this.carId = options.carId
			this.fairValue = JSON.parse(options.fairValue)
			this.carData = JSON.parse(options.data)
			this.gxzStatus = options.gxzStatus
			this.getContractUrl()
		},
		methods: {
			// 获取合同
			getContractUrl() {
				this.isSHowTip = 'creating'
				let data = `carId=${this.carId}&&type=2`
				getContractEcho(data).then(res => {
					this.isSHowTip = ''
					this.contractDtail = res.data
					this.contractName = res.data.find(v => v.contractType == '2')?.contractName || '二手车销售协议'
					this.entrustName = res.data.find(v => v.contractType == '1')?.contractName || '委托销售协议'
				}).catch(err => {
					// this.$modal.msg('获取合同失败')
					this.isSHowTip = 'createFail'
				})
			},
			
			// 查看
			handleViewContract(text) {
				this.$modal.msg('正在加载，请稍等...')
				let _this=this
				let url=this.contractDtail.find(v=>v.contractType==text)?.url
				uni.downloadFile({
					url: url,
					success: function(res) {
						var filePath = res.tempFilePath;
						uni.openDocument({
							filePath: filePath,
							showMenu: false,
							success: function(res) {
								console.log('打开文档成功');
								setTimeout(()=>{
									if(text=='1'){
										_this.entrustValue=['委托销售协议']
									}else{
										_this.contractValue=['二手车销售协议']
									}
								},1000)
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
				let _this = this;
				if(!_this.contractValue.length || !_this.entrustValue.length) return _this.$modal.msg('请勾选协议和委托协议！')
				// 判断是否超出公允值
				if (_this.gxzStatus == 0) {
					uni.showModal({
						title: '提示',
						content: '您的卖车价格不在市场评估价格之内，继续提交会触发平台方审核，是否继续提交？',
						confirmText: '是',
						cancelText: '否',
						confirmColor: '#fa6401',
						success(ress) {
							if (ress.confirm) {
								_this.handleDraft();
							}
						}
					})
				} else {
					// _this.$modal.msg('正在加载，请稍等...')
					_this.isSHowTip='signing'
					let data={
						..._this.contractDtail.find(v=>v.contractType=='1')
					}
					getQiyuesuo(data).then((res) => {
						if (res.data) {
							// _this.$modal.msg("合同已签章");
							_this.isSHowTip='signSuccess'
							// _this.$tab.switchTab('/pages/index');
							// _this.$tab.navigateTo(`/subPages/common/webview/index?title=卖车合同签章&url=${res.data}`);
						}
					}).catch(()=>{
						// this.$modal.msg('加载失败！')
						this.isSHowTip='signFail'
					})
				}
			},
			// 发起公允值审批流程
			handleDraft() {
				let procDefKey = "MGYZ";
				this.carData.fairValue = this.fairValue;
				let variables = {
					marketName: this.$store.state.user.tenantName,
					merchantName: this.$store.state.user.deptName,
					startUserId: this.$store.state.user.id,
					formDataJson: {
						formMain: {
							merchantId: this.$store.state.user.deptId,
							thirdId: this.carData.carInfoDetails.carId,
							formDataJson: this.carData
						}
					}
				}
				let createData = {
					procDefKey,
					variables
				};
				this.$modal.loading("提交中，请耐心等待...");
				setCreate(createData).then((ress) => {
					this.$modal.closeLoading()
					this.showOverlay = false;
					this.$modal.msg("已提交审核");
					this.$tab.switchTab('/pages/index');
				}).catch((error) => {
					this.$modal.closeLoading()
					// this.$modal.msgError("发起流程失败");
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
	.confitmBtn{
		margin:80rpx 30rpx 0;
		color:#000;
		background-color: #fff;
		font-size: 32rpx;
	}
</style>