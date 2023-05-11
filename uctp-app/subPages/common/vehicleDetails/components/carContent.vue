<template>
	<view class=" info-box">
		<!-- 车辆信息 -->
		<view v-if="tabCar==0" class="car-info-box">
			<view  class="car-upload">
				<view class="car-upload-title">
					<image src="../../../../static/images/home/inspect-annually.svg"></image>
					<text class="car-upload-title__title">车辆检测报告</text>
					<text v-if="!isShowTest" class="car-upload-title__text">未检测</text>
					<text v-else class="car-upload-title__text">已检测</text>
				</view>
				<view v-if="carInfoAll.carInfo&&carInfoAll.carInfo.status>21" class="upload-content">
					<view v-if="!isShowTest" class="upload-text" @click="photograph(1)">
						<text>上传检测报告</text>
						<u-icon name="arrow-upward" color="#333333" style="width: 30rpx;height: 30rpx;"></u-icon>
					</view>
					<view  v-else class="upload-text">
						<image src="../../../../static/images/home/checkmark.svg"></image>
						<text @click="handleImage(carInfoAll.fileF)">您已经上传检测报告!</text>
						<view v-if="carInfoAll.carInfo&&carInfoAll.carInfo.status<30"  @click="handleDelete" class="icon-right-box">
							<u-icon  name="trash" color="#333333">
							</u-icon>
						</view>
					</view>
				</view>
				<view v-if="carInfoAll.carInfo.status==23" class="btn-box">
					<u-button type="primary" size="mini" shape="circle" @click="sellCarBtn(carInfoAll.carInfo.id)" text="我要卖车"></u-button>
				</view>
			</view>
			<view class="driving-license car-registration">
				<view class="driving-license__icon">

				</view>
				<text>行驶证</text>
			</view>
			<view v-if="carInfoAll.fileB && carInfoAll.fileB.length" class="driving-image">
				<!-- <image :src="carInfoAll.fileB[0].url" mode="" style="width: 232rpx;height: 166rpx;"></image> -->
				<u-album :urls="carInfoAll.fileB" :singleSize="rpxToPx(232)" keyName="url" ></u-album>
			</view>
			<view v-else class="driving-image">
				<image :src="drivingImg" mode="" style="width: 232rpx;height: 166rpx;"></image>
			</view>
			<view class="driving-content">
				<view class="car-details">
					<view class="car-item">
						<text>发动机编号</text>
						<text>{{carInfoAll.carInfo.engineNum || '暂无'}}</text>
					</view>
					<view class="car-item">
						<text>首次登录日期</text>
						<text>{{carInfoAll.carInfoDetails.firstRegistDate || '暂无'}}</text>
					</view>
				</view>
				<view class="car-details">
					<view class="car-item">
						<text>车牌号</text>
						<text>{{carInfoAll.carInfo.plateNum || '暂无'}}</text>
					</view>
					<view class="car-item">
						<text>使用性质</text>
						<text>{{carInfoAll.carInfoDetails.natureOfOperat || '暂无'}}</text>
					</view>
				</view>
			</view>
			<view class="driving-license car-registration">
				<view class="driving-license__icon">

				</view>
				<text>机动车登记证书</text>
			</view>
			<view id="carRegister" v-if="carInfoAll.fileC && carInfoAll.fileC.length" class="driving-image">
				<!-- <image :src="drivingImg" mode="" style="width: 232rpx;height: 166rpx;"></image> -->
				<u-album :urls="carInfoAll.fileC" :singleSize="rpxToPx(232)" keyName="url" singleModex="scaleToFill" ></u-album>
			</view>
			<view v-else class="driving-image">
				<image :src="drivingImg" mode="" style="width: 232rpx;height: 166rpx;"></image>
			</view>
			<view class="driving-content driving-details">
				<view class="car-details">
					<view class="car-item">
						<text>登记证号</text>
						<text>{{carInfoAll.carInfoDetails.certificateNo || '暂无'}}</text>
					</view>
					<view class="car-item">
						<text>颜色</text>
						<text>{{carInfoAll.carInfoDetails.colour || '暂无'}}</text>
					</view>
				</view>
				<view class="car-details">
					<view class="car-item">
						<text>里程数</text>
						<text>{{carInfoAll.carInfoDetails.mileage || '暂无'}}万公里</text>
					</view>
					<view class="car-item">
						<text>使用年限至</text>
						<text>{{carInfoAll.carInfo.scrapDate || '暂无'}}</text>
					</view>
				</view>
				<view class="car-details">
					<view class="car-item">
						<text>年检签章有效期</text>
						<text>{{carInfoAll.carInfo.annualInspectionDate || '暂无'}}</text>
					</view>
					<view class="car-item">
						<text>保险险种</text>
						<text>{{carInfoAll.carInfo.insurance || '暂无'}}</text>
					</view>
				</view>
				<view class="car-details">
					<view class="car-item">
						<text>保险期至</text>
						<text>{{carInfoAll.carInfo.insuranceEndData || '暂无'}}</text>
					</view>
				</view>
			</view>
		</view>
		<!-- 合同信息 -->
		<view v-if="tabCar==1" class="car-contract">
			<view class="driving-license car-registration">
				<view class="">
					<view class="driving-license__icon">

					</view>
					<text>有效合同</text>
				</view>
				<!-- <view class="download">
					<text>批量下载</text>
					<u-icon name="download" style="width: 30rpx;height: 30rpx;" color="#FA6400"></u-icon>
				</view> -->
			</view>
			<view class="contrart-info" v-for="(contract,index) in carInfoAll.contractCardVOS" :key="index">
				<view v-if="contract.contractDO.contractType==1 || contract.contractDO.contractType==3"
					class="flex contrart-info__row">
					<text @click="handleContact(contract.url)">{{contract.contractDO.contractName}}合同</text>
					<text class="button" @click="handleCancle(contract.contractDO.id)">作废</text>
				</view>
				<view v-else class="flex contrart-info__row">
					<view class="">
						<text @click="handleContact(contract.url)">{{contract.contractDO.contractName}}</text>
						<view class="flex selInfo">
							<text
								@click="handleOwnerInfo(contract.contractDO.contractType)">{{contract.contractDO.contractType==2?'卖家信息（第三方收款）':'买家信息'}}</text>
							<u-icon name="arrow-right" size="24rpx" color="#FA6400"></u-icon>
						</view>
					</view>
					<!-- <text class="button" @click="handleCancle(contract.contractDO.contractId)">作废</text> -->
				</view>
			</view>
			<view v-if="!carInfoAll.contractCardVOS.length" class="empty-info">
				<text>暂无合同</text>
			</view>
			<view class="driving-license car-registration">
				<view class="">
					<view class="driving-license__icon">

					</view>
					<text>无效合同</text>
				</view>
				<!-- <view class="download">
					<text>批量下载</text>
					<u-icon name="download" style="width: 30rpx;height: 30rpx;" color="#FA6400"></u-icon>
				</view> -->
			</view>
			<view v-for="(contract,index) in carInfoAll.contractCardNOS" :key="index"
				class="contrart-info contrart-bottom">
				<view v-if="contract.contractDO.contractType==1 || contract.contractDO.contractType==3"
					class="flex contrart-info__row">
					<view class="contart-info__">
						<text class="contrart-info__text">作废文件</text>
						<text  @click="handleContact(contract.url)">{{contract.contractDO.contractName}}（已作废）</text>
					</view>
				</view>
				<view v-else class="flex">
					<view class="">
						<text class="contrart-info__text">作废文件</text>
						<text  @click="handleContact(contract.url)">{{contract.contractDO.contractName}}（已作废）</text>
					</view>
					<view class="flex selInfo">
						<text
							@click="handleOwnerInfo(contract.contractDO.contractType)">{{contract.contractDO.contractType==2?'卖家信息（第三方收款）':'买家信息'}}</text>
						<u-icon name="arrow-right" size="24rpx" color="#FA6400"></u-icon>
					</view>
				</view>
			</view>
			<view v-if="!carInfoAll.contractCardNOS.length" class="empty-info">
				<text>暂无合同</text>
			</view>
			<!-- <view class="car-button">
				<u-button v-show="signInShow"  text="签章" color="#FA6400" @click="handleSignature"></u-button>
			</view> -->
		</view>
		<view class="car-fund" v-if="tabCar==2">
			<!-- <view class="car-upload">
				<view class="car-upload-title">
					<image src="../../../../static/images/home/bank-card.svg"></image>
					<text class="car-upload-title__title">银行电子回单</text>
					<text class="car-upload-title__text">待匹配</text>
				</view>
				<view class="upload-content">
					<view class="upload-text" @click="photograph(2)" v-if="!carUpload">
						<text>上传银行电子回单</text>
						<u-icon name="arrow-upward" color="#333333" style="width: 30rpx;height: 30rpx;"></u-icon>
					</view>
					<view v-else class="upload-text">
						<image src="../../../../static/images/home/checkmark.svg"></image>
						<text>您已经上传检测报告!</text>
						<u-icon name="trash" color="#333333" style="width: 30rpx;height: 30rpx;"></u-icon>
					</view>
				</view>
			</view> -->
			<view class="car-fund-info">
				<view class="car-fund-info-title">
					<view class="">
						<view class="">
							<u-icon name="rmb-circle-fill" style="width: 30rpx;height: 30rpx;margin-right: 10rpx;"
								color="#FA6400"></u-icon>
							<text class="car-upload-title__title">资金</text>
						</view>
						<view class="eye-show">
							<text v-if="eyeIsShow" class="iconfont icon-open-eye" @click="eyeIsShow=!eyeIsShow"></text>
							<text v-else class="iconfont icon-close-eye" @click="eyeIsShow=!eyeIsShow"></text>
						</view>
					</view>
					<image style="width: 206rpx;height: 212rpx;" src="../../../../static/images/home/car-money.svg"
						mode=""></image>
					<view class="flex car-profit">
						<text>
							实际利润
						</text>
						<view class="">
							<text v-if="eyeIsShow">{{ carInfoAll.carInfo.status>31?carInfoAll.appCarInfoAmountRespVO.profit:'——' | transMoney }}</text>
							<text v-else>{{'****'}}</text>
							<text>元</text>
						</view>
					</view>
				</view>
				<view class="car-fund-info-content flex">
					<view class="flex">
						<view class="flex">
							<text>收车款</text>
							<text
								v-if="eyeIsShow">{{carInfoAll.appCarInfoAmountRespVO.vehicleReceiptAmount | transMoney}}元</text>
							<text v-else>{{'****'}}元</text>
							<!-- <view class="flex">
								<text>公允价值-通过</text>
								<u-icon name="arrow-right" style="width: 28rpx;height: 28rpx;" color="#FA6400"></u-icon>
							</view> -->
						</view>
						<u-line direction="col" length="76rpx" style="width: 2rpx;"></u-line>
						<view class="flex">
							<text>卖车款</text>
							<text v-if="eyeIsShow">{{carInfoAll.appCarInfoAmountRespVO.sellAmount | transMoney}}元</text>
							<text v-else>{{'****'}}元</text>
							<!-- <view class="flex">
								<text>公允价值-通过</text>
								<u-icon name="arrow-right" style="width: 28rpx;height: 28rpx;" color="#FA6400"></u-icon>
							</view> -->
						</view>
					</view>
					<view class="flex">
						<view class="flex">
							<text>税</text>
							<text v-if="eyeIsShow">{{carInfoAll.appCarInfoAmountRespVO.vat | transMoney}}元</text>
							<text v-else>{{'****'}}元</text>
						</view>
						<u-line direction="col" length="76rpx" style="width: 2rpx;"></u-line>
						<view class="flex">
							<text>服务费</text>
							<text
								v-if="eyeIsShow">{{carInfoAll.appCarInfoAmountRespVO.operation | transMoney}}元</text>
							<text v-else>{{'****'}}元</text>
						</view>
					</view>
					<view class="flex">
						<view class="flex">
							<text>本次回填保证金</text>
							<text v-if="eyeIsShow">{{0 | transMoney}}元</text>
							<text v-else>{{'****'}}元</text>
						</view>
						<u-line direction="col" length="76rpx" style="width: 2rpx;"></u-line>
						<view class="flex">
							<text>本次待回填差额</text>
							<text v-if="eyeIsShow">{{0 | transMoney}}元</text>
							<text v-else>{{'****'}}元</text>
						</view>
					</view>
				</view>
			</view>
		</view>
		<view class="car-invoice" v-if="tabCar==3">
			<view class="flex">
				<text>二手车销售统一发票(反向)</text>
				<view class="flex">
					<text>{{colInvoiced?'已开票':'待开票'}}</text>
					<text class="button">开票信息</text>
				</view>
			</view>
			<view class="flex">
				<text>二手车销售统一发票(正向)</text>
				<view class="flex">
					<text>{{sellInvoiced?'已开票':'待开票'}}</text>
					<text class="button">开票信息</text>
				</view>
			</view>
			<view class="flex">
				<text>增值税发票</text>
				<view class="flex">
					<text>{{sellInvoiced?'已开票':'待开票'}}</text>
					<text class="button">下载PDF文件</text>
				</view>
			</view>
		</view>
		<!-- 作废理由 -->
		<u-modal :show="cancelModal" :closeOnClickOverlay="false" :showCancelButton="true" :negativeTop="100" width="700rpx" @cancel="cancelBtn" @confirm="confirmBtn">
			<view slot-content style="width:100%">
				<u--form
					labelPosition="top"
					:model="form"
					:rules="rules"
					ref="uForm"
					errorType="toast"
				>
					<u-form-item label="理由:" prop="reason" required>
						<u--textarea  v-model="form.reason" placeholder="请输入理由" ></u--textarea>
					</u-form-item>
				</u--form>
			</view>
		</u-modal>
	</view>
</template>

<script>
	let that;
	import {
		showConfirm
	} from '@/utils/common'
	import config from '@/config'
	import {
		getAccessToken
	} from '@/utils/auth'
	import {
		deleteTestImage
	} from '@/api/register'
	import { contractInvalid } from '@/api/cost/carInfo.js'
	export default {
		data() {
			return {
				carUpload: false,
				eyeIsShow: false,
				// 检测报告
				fileList1: [],
				// 检测报告ID
				testFileId: 0,
				// 银行电子回单
				fileList2: [],
				// 行驶证 机动车登记证 默认
				drivingImg: '/static/images/home/driving-license.svg',
				// 信息类型
				infoType: null,
				//合同id
				contractId:'',
				cancelModal:false,
				form:{
					reason:'',
				},
				rules:{
					reason:[
						{
							type: 'string',
							required: true,
							message: '请输入理由',
							trigger: ['change','blur']
						}
					]
				}
			}
		},
		props: {
			tabCar: {
				type: Number,
				default: 0
			},
			carInfoAll: {
				type: Object,
				default: () => {}
			},
			isShowTest: {
				type: Boolean,
				default: false
			}
		},
		beforeCreate() {
			that = this;
		},
		filters: {
			transMoney(val) {
				if(val=='——'){
					return '——'
				}else if(parseFloat(val)){
					 return that.$amount.getComdify(val)
				}else{
					return 0
				}
			}
		},
		computed:{
			signInShow(){
				let {statusThree}=this.carInfoAll.carInfo
				if(statusThree==121 || statusThree==122 || statusThree==123 || statusThree==321 || statusThree==322 || statusThree==323){
					return true
				}else{
					return false
				}
			},
			colInvoiced(){
				let {status,statusThree}=this.carInfoAll.carInfo;
				if(status>21 || statusThree>211){
					return true;
				}else{
					return false;
				}
			},
			sellInvoiced(){
				let {status,statusThree}=this.carInfoAll.carInfo;
				if(status>41||statusThree>411){
					return true;
				}else{
					return false;
				}
			}
		},
		methods: {
			// 上传检测报告
			photograph(index) {
				let _this = this;
				uni.showActionSheet({
					// title: "选择类型",
					itemList: ['相册', '拍摄'],
					success: async function(res) {
						_this.chooseImages(res.tapIndex, index);
					}
				})
			},
			chooseImages(tapIndex, index) {
				let _this = this;
				uni.chooseImage({
					count: 1,
					sizeType: ['original', 'compressed'], //可以指定是原图还是压缩图，默认二者都有
					sourceType: tapIndex == 0 ? ['album'] : ['camera'], // 从相册选择 : 使用相机
					success: function(res) {
						res.tempFilePaths.forEach((item) => {
							_this[`fileList${index}`].push({
								url: item,
								status: 'uploading',
								message: '上传中'
							})
						})
						for (let i = 0; i < res.tempFilePaths.length; i++) {
							if (i == res.tempFilePaths.length - 1) {
								_this.upload(res, index);
							}
						}
					}
				})
			},
			upload(res, index) {
				let _this = this;
				_this.$modal.loading('正在上传中')
				for (let i = 0; i < res.tempFilePaths.length; i++) {
					uni.uploadFile({
						url: config.upLoadTestUrl, // 仅为示例，非真实的接口地址
						// #ifdef H5
						file: res.tempFiles[i],
						// #endif
						// #ifdef MP-WEIXIN || APP
						filePath: res.tempFilePaths[i],
						// #endif
						name: 'file',
						header: {
							Authorization: 'Bearer ' + getAccessToken()
						},
						formData: {
							'carId': _this.carInfoAll.carInfo.id
						},
						success: (ress) => {
							_this.$modal.closeLoading();
							setTimeout(() => {
								let fileListLen = 0;
								let data = JSON.parse(ress.data).data;
								if (data) {
									for (let i = 0; i < data.length; i++) {
										let item = _this[`fileList${index}`][fileListLen]
										_this[`fileList${index}`].splice(fileListLen, 1, Object.assign(
											item, {
												status: 'success',
												message: '',
												url: data[i].url,
												id: data[i].id
											}))
										fileListLen++;
									}
									if (index == 1) {
										uni.showModal({
											title: '提示',
											content: '您的车辆检测报告已经上传完成。',
											showCancel: false,
											confirmText: '知道了',
											confirmColor: '#f60',
											success: function(res) {
												if (res.confirm) {
													_this.carUpload = true;
													_this.$emit('changeTest', _this
														.carUpload)
														
													uni.$emit('listRefresh', {
													      refresh: true
													});
												}
											}
										})
									}
								} else {
									_this.$modal.msg("上传失败");
									_this[`fileList${index}`] = [];
								}
							}, 1000)
						}
					});
				}
			},
			// 查看检测报告
			handleImage(arr){
				if(!arr[0]?.url) return this.$modal.msg('检测报告不存在!')
				let urls=[arr[0].url]
				uni.previewImage({
					current:0,
					urls,
				})
			},
			
			// 删除检测报告
			handleDelete() {
				// let _this=this
				showConfirm('删除检测报告后，您的车辆将无法发起卖车业务，若需要继续卖车请重新上传新的检测报告').then(res => {
					if (res.confirm) {
						deleteTestImage({
							id: this.fileList1[0]?.id || this.carInfoAll.fileF[0].id,
							carId:this.carInfoAll?.carInfo.id
						}).then((res) => {
							this.$modal.msg("删除成功");
							this.carUpload = false
							this.$emit('changeTest', this.carUpload)
							uni.$emit('listRefresh', {
							      refresh: true
							});
						}).catch(err => {
							this.$modal.msgError('删除失败')
						})
					}
				})
			},
			// 我要卖车
			sellCarBtn(id){
				this.$tab.navigateTo('/subPages/home/sellingCar/carInfo?id='+id)
			},
			// 预览合同
			handleContact(url) {
				// console.log(url)
				let _this=this
				_this.$modal.loading('正在打开...')
				uni.downloadFile({
					url: url,
					success: function(res) {
						var filePath = res.tempFilePath;
						uni.openDocument({
							filePath: filePath,
							fileType: 'pdf',
							showMenu: true,
							success: function(res) {
								console.log('打开文档成功');
								_this.$modal.closeLoading();
							}
						});
					}
				});
			},
			// 查看买家、卖家信息
			handleOwnerInfo(type) {
				this.infoType = type
				this.$tab.navigateTo(
					`/subPages/common/vehicleDetails/components/clientInfo?carInfoAll=${encodeURIComponent(JSON.stringify(this.carInfoAll))}&&infoType=${this.infoType}`
					)
			},
			// 作废
			handleCancle(id) {
				this.contractId=id;
				this.cancelModal=true;
			},
			confirmBtn(){
				this.$refs.uForm.validate().then(res=>{
					let data=`id=${this.contractId}&reason=${this.form.reason}`
					contractInvalid(data).then(res=>{
						this.$modal.msg('合同作废成功');
						this.cancelModal=false;
						this.form.reason='';
						this.$emit('inviladContract')
					}).catch(err=>{
						this.$modal.msg(err.msg);
					})
				})
				
			},
			// 弹框取消
			cancelBtn(){
				this.cancelModal=false;
				this.form.reason='';
			},
			// 签章
			// handleSignature() {
			// 	let {status,id} = this.carInfoAll.carInfo
			// 	if(status==12){
			// 		this.$tab.navigateTo('/subPages/home/bycar/agreement?carId='+id)
			// 	}else{
			// 		this.$tab.navigateTo('/subPages/home/sellingCar/agreement?carId='+id)
			// 	}			
			// },
			
			// rpx转px
			rpxToPx(rpx) {
			  const screenWidth = uni.getSystemInfoSync().screenWidth
			  return (screenWidth * Number.parseInt(rpx)) / 750
			}
		}
	}
</script>

<style lang="scss" scoped>
	
	.flex {
		display: flex;
	}

	.info-box {
		.car-info-box,
		.car-fund {
			padding: 24rpx 26rpx 0rpx;
		}

		.button {
			width: 82rpx;
			height: 46rpx;
			font-size: 24rpx;
			line-height: 46rpx;
			color: #FA6400;
			border-radius: 8rpx;
			text-align: center;
			border: 2rpx solid #FA6400;
		}

		.car-upload {
			width: 698rpx;
			height: 186rpx;
			background: rgba(249, 249, 249, 0.02);
			border-radius: 8rpx;
			border: 2rpx solid #EDF3F6;
			position:relative;
			.btn-box{
				position:absolute;
				right:16rpx;
				top:19rpx;
			}
			.car-upload-title {
				width: 698rpx;
				height: 78rpx;
				background: linear-gradient(90deg, #F9F9F9 0%, #F9F9F9 100%);
				border-radius: 8rpx;
				display: flex;
				align-items: center;
				color: #ffffff;
				padding: 0 16rpx;

				image {
					width: 48rpx;
					height: 48rpx;
					margin-right: 4rpx;
				}

				&__title {
					font-size: 28rpx;
					color: #333333;
					margin-right: 18rpx;
				}

				&__text {
					width: 106rpx;
					height: 32rpx;
					background: #FA6400;
					border-radius: 4rpx;
					text-align: center;
					line-height: 32rpx;
					font-size: 20rpx;
				}
			}

			.upload-content {
				height: calc(100% - 78rpx);
				display: flex;
				align-items: center;
			}

			.upload-text {
				width: 100%;
				display: flex;
				margin: 0 auto;
				justify-content: center;
				align-items: center;
				color: #333333;

				>image {
					width: 30rpx;
					height: 30rpx;
				}

				>text {
					font-size: 28rpx;
					margin: 0 3rpx;
				}
				.icon-right-box{
					display: inline-block;
					padding:0 50rpx 0 10rpx;
				}
			}
			
		}

		.driving-license {
			margin: 96rpx 0 40rpx;
			padding: 4rpx;
			display: flex;
			align-items: center;

			&__icon {
				width: 12rpx;
				height: 12rpx;
				border-radius: 12rpx;
				background: #FA6400;
				margin-right: 16rpx;
			}

			text {
				font-size: 30rpx;
				font-weight: 500;
				color: #333333;
				line-height: 42rpx;

			}
		}

		.driving-image {
			width: 694rpx;
			height: 280rpx;
			background: #FEFEFE;
			border-radius: 8rpx;
			border: 2rpx solid #EDF3F6;
			display: flex;
			align-items: center;
			justify-content: center;
		}

		.driving-content {
			width: 698rpx;
			height: 146rpx;
			background: #F9F9F9;
			border-radius: 8rpx;
			margin-top: 26rpx;
			padding: 26rpx 22rpx;
			font-size: 24rpx;
			display: flex;
			flex-direction: column;
			justify-content: space-evenly;

			.car-details {
				display: flex;
				justify-content: space-between;
				position: relative;

				.car-item {
					width: 50%;
					display: flex;
					color: #333333;

					text: {
						line-height: 34rpx;
					}

					>text:first-child {
						color: #999999;
						width: 160rpx;
						margin-right: 8rpx;
					}
				}
			}
		}

		.car-registration {
			margin: 36rpx 0 44rpx;
		}

		.driving-details {
			height: 256rpx;

			.car-details .car-item>text:first-child {
				width: 168rpx;
			}
		}

		.car-contract {
			padding-bottom: 160rpx;

			.driving-license {
				padding: 30rpx 36rpx 28rpx;
				margin: 0;
				justify-content: space-between;
				border-bottom: 2rpx solid #F5F5F5;

				>view {
					display: flex;
					align-items: center;
				}

				.download {
					text {
						color: #FA6400;
					}

				}
			}

			.contrart-info {
				border-bottom: 20rpx solid #FAFAFA;
				font-size: 24rpx;
				color: #222222;

				>view {
					// height: 128rpx;
					padding: 22rpx 0;
					margin-left: 32rpx;
					padding-right: 38rpx;
					border-bottom: 2rpx solid #F5F5F5;
					flex-direction: column;
					justify-content: center;

					.selInfo {
						align-items: center;
						color: #FA6400;
					}
				}

				.contrart-info__row {
					// height: 78rpx;
					padding: 22rpx 38rpx 22rpx 0;
					flex-direction: row;
					justify-content: space-between;
					align-items: center;
				}

				.contrart-info__text {
					width: 106rpx;
					height: 32rpx;
					background: rgba(109, 114, 120, 0.6);
					border-radius: 4rpx;
					line-height: 32rpx;
					text-align: center;
					color: #FFFFFF;
					font-size: 20rpx;
					margin-right: 16rpx;
				}
			}

			.contrart-bottom {
				border-bottom: 2rpx solid #F5F5F5;
			}

			.car-button {
				width: 696rpx;
				height: 80rpx;
				margin: 36rpx 28rpx 0;
			}
		}

		.empty-info {
			min-height: 128rpx;
			line-height: 128rpx;
			text-align: center;
			border-bottom: 2rpx solid #f5f5f5;
		}

		.car-fund {
			.car-fund-info {
				// margin-top: 26rpx;
				width: 100%;
				height: 660rpx;
				background: rgba(249, 249, 249, 0.02);
				border-radius: 8rpx;
				border: 2rpx solid rgba(250, 100, 0, 0.05);

				.car-fund-info-title {
					overflow: hidden;
					display: flex;
					flex-direction: column;
					background: linear-gradient(360deg, rgba(216, 216, 216, 0) 0%, rgba(250, 100, 0, 0.05) 100%);
					align-items: center;
					position: relative;

					>view:first-child {
						padding: 0 34rpx 0 22rpx;
						border-radius: 8rpx;
						color: #FA6400;
						display: flex;
						justify-content: space-between;
						align-items: center;
						width: 698rpx;
						height: 104rpx;

						>view {
							display: flex;
							align-items: center;
						}
					}

					.eye-show {
						position: relative;
						z-index: 10;
					}

					>image {
						position: absolute;
						right: -26rpx;
						top: -48rpx;
					}

					.car-profit {
						flex-direction: column;
						align-items: center;
						justify-content: center;
						color: #333333;

						text:first-child {
							font-size: 32rpx;
						}

						view text {
							font-size: 32rpx;

							&:first-child {
								font-size: 48rpx;
								color: #FA6400;
								font-weight: 600;
							}
						}

					}
				}

				.car-fund-info-content {
					height: calc(100% - 192rpx);
					flex-direction: column;
					justify-content: space-evenly;
					padding: 0 30rpx;
					color: #333333;

					>view {
						height: calc(100% / 4);
						align-items: center;
						border-bottom: 2rpx solid #F5F5F5;
						position: relative;


						>view {
							width: 50%;
							align-items: center;
							flex-direction: column;

							>text {
								text-align: center;

								&:first-child {
									font-size: 28rpx;
									font-weight: 400;
									line-height: 40rpx;
								}

								&:nth-child(2) {
									font-size: 32rpx;
									font-weight: 500;
									line-height: 44rpx;
								}
							}

							>view {
								font-size: 24rpx;
								align-items: center;
								color: #FA6400;
							}
						}

						&:last-child {
							border: none;
						}
					}
				}
			}
		}

		.car-invoice {
			padding: 0 0 160rpx 36rpx;

			>view {
				border-bottom: 2rpx solid #F5F5F5;
				height: 160rpx;
				padding-right: 32rpx;
				flex-direction: column;
				justify-content: space-evenly;
				color: #222222;

				>view {
					justify-content: space-between;

					>text {
						width: 106rpx;
						height: 32rpx;
						background: #FA6400;
						border-radius: 4rpx;
						line-height: 32rpx;
						text-align: center;
						color: #FFFFFF;
					}

					.button {
						width: 132rpx;
						height: 46rpx;
						font-size: 24rpx;
						line-height: 46rpx;
						color: #FA6400;
						border-radius: 8rpx;
						background: none;
						text-align: center;
						border: 2rpx solid #FA6400;
					}
				}

				&:last-child>view .button {
					width: 188rpx;
				}
			}
		}

		.info-box {
			padding: 15px;
			line-height: 40rpx;
		}
	}
</style>
