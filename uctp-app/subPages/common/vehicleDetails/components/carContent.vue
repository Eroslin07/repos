<template>
	<view class=" info-box">
		<!-- 车辆信息 -->
		<view v-if="tabCar=='0'" class="car-info-box">
			<view class="car-upload">
				<view class="car-upload-title">
					<image src="../../../../static/images/home/inspect-annually.svg"></image>
					<text class="car-upload-title__title">车辆检测报告</text>
					<text class="car-upload-title__text">未检测</text>
				</view>
				<view class="upload-content">
					<view class="upload-text" @click="photograph" v-if="carUpload">
						<text>上传检测报告</text>
						<u-icon name="arrow-upward" color="#333333" style="width: 30rpx;height: 30rpx;"></u-icon>
					</view>
					<view v-else class="upload-text">
						<image src="../../../../static/images/home/checkmark.svg"></image>
						<text>您已经上传检测报告!</text>
						<u-icon @click="handleDelete" name="trash" color="#333333" style="width: 30rpx;height: 30rpx;">
						</u-icon>
					</view>
				</view>
			</view>
			<view class="driving-license">
				<view class="driving-license__icon">

				</view>
				<text>行驶证</text>
			</view>
			<view class="driving-image">
				<image src="../../../../static/images/home/driving-license.svg" mode=""
					style="width: 232rpx;height: 166rpx;"></image>
			</view>
			<view class="driving-content">
				<view class="car-details">
					<view class="car-item">
						<text>发动机编号</text>
						<text>19289098</text>
					</view>
					<view class="car-item">
						<text>首次登录日期</text>
						<text>2019年6月30日</text>
					</view>
				</view>
				<view class="car-details">
					<view class="car-item">
						<text>车牌号</text>
						<text>川A 2989</text>
					</view>
					<view class="car-item">
						<text>使用性质</text>
						<text>非营运</text>
					</view>
				</view>
			</view>
			<view class="driving-license car-registration">
				<view class="driving-license__icon">

				</view>
				<text>机动车登记证书</text>
			</view>
			<view class="driving-image">
				<image src="../../../../static/images/home/car-registration.svg" mode=""
					style="width: 232rpx;height: 166rpx;"></image>
			</view>
			<view class="driving-content driving-details">
				<view class="car-details">
					<view class="car-item">
						<text>登记证号</text>
						<text>19289098</text>
					</view>
					<view class="car-item">
						<text>颜色</text>
						<text>白色</text>
					</view>
				</view>
				<view class="car-details">
					<view class="car-item">
						<text>里程数</text>
						<text>29万公里</text>
					</view>
					<view class="car-item">
						<text>使用年限至</text>
						<text>2023-12-30</text>
					</view>
				</view>
				<view class="car-details">
					<view class="car-item">
						<text>年检签章有效期</text>
						<text>29万公里</text>
					</view>
					<view class="car-item">
						<text>保险险种</text>
						<text>交强险</text>
					</view>
				</view>
				<view class="car-details">
					<view class="car-item">
						<text>保险期至</text>
						<text>2023-12-30</text>
					</view>
				</view>
			</view>
		</view>
		<!-- 合同信息 -->
		<view v-if="tabCar=='1'" class="car-contract">
			<view class="driving-license car-registration">
				<view class="">
					<view class="driving-license__icon">

					</view>
					<text>有效合同</text>
				</view>
				<view class="download">
					<text>批量下载</text>
					<u-icon name="download" style="width: 30rpx;height: 30rpx;" color="#FA6400"></u-icon>
				</view>
			</view>
			<view class="contrart-info">
				<view class="flex contrart-info__row">
					<text>XXX某收车委托合同</text>
					<text class="button">作废</text>
				</view>
				<view class="flex">
					<text>XXX某收车合同</text>
					<view class="flex">
						<text>卖家信息（第三方收款）</text>
						<u-icon name="arrow-right" style="width: 30rpx;height: 30rpx;" color="#FA6400"></u-icon>
					</view>
				</view>
				<view class="flex  contrart-info__row">
					<text>XXX某卖委托合同</text>
					<text class="button">作废</text>
				</view>
				<view class="flex">
					<text>XXX某卖车合同</text>
					<view class="flex">
						<text>买家信息</text>
						<u-icon name="arrow-right" style="width: 30rpx;height: 30rpx;" color="#FA6400"></u-icon>
					</view>
				</view>
			</view>
			<view class="driving-license car-registration">
				<view class="">
					<view class="driving-license__icon">

					</view>
					<text>无效合同</text>
				</view>
				<view class="download">
					<text>批量下载</text>
					<u-icon name="download" style="width: 30rpx;height: 30rpx;" color="#FA6400"></u-icon>
				</view>
			</view>
			<view class="contrart-info contrart-bottom">
				<view class="flex contrart-info__row">
					<view class="contart-info__">
						<text class="contrart-info__text">作废文件</text>
						<text>XXX某收车委托合同（已作废）</text>
					</view>
				</view>
				<view class="flex">
					<view class="">
						<text class="contrart-info__text">作废文件</text>
						<text>XXX某收车合同（已作废）</text>
					</view>
					<view class="flex">
						<text>卖家信息（第三方收款）</text>
						<u-icon name="arrow-right" style="width: 30rpx;height: 30rpx;" color="#FA6400"></u-icon>
					</view>
				</view>
				<view class="flex  contrart-info__row">
					<view class="">
						<text class="contrart-info__text">作废文件</text>
						<text>XXX某卖委托合同（已作废）</text>
					</view>
				</view>
				<view class="flex">
					<view class="">
						<text class="contrart-info__text">作废文件</text>
						<text>XXX某卖车合同（已作废）</text>
					</view>
					<view class="flex">
						<text>买家信息</text>
						<u-icon name="arrow-right" style="width: 30rpx;height: 30rpx;" color="#FA6400"></u-icon>
					</view>
				</view>
			</view>
			<view class="car-button">
				<u-button text="签章" color="#FA6400" @click="handleSignature"></u-button>
			</view>
		</view>
		<view class="car-fund" v-if="tabCar=='2'">
			<view class="car-upload">
				<view class="car-upload-title">
					<image src="../../../../static/images/home/bank-card.svg"></image>
					<text class="car-upload-title__title">银行电子回单</text>
					<text class="car-upload-title__text">待匹配</text>
				</view>
				<view class="upload-content">
					<view class="upload-text" @click="photograph" v-if="carUpload">
						<text>上传银行电子回单</text>
						<u-icon name="arrow-upward" color="#333333" style="width: 30rpx;height: 30rpx;"></u-icon>
					</view>
					<view v-else class="upload-text">
						<image src="../../../../static/images/home/checkmark.svg"></image>
						<text>您已经上传检测报告!</text>
						<u-icon name="trash" color="#333333" style="width: 30rpx;height: 30rpx;"></u-icon>
					</view>
				</view>
			</view>
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
							<text>{{eyeIsShow?'234':'****'}}</text>
							<text>元</text>
						</view>
					</view>
				</view>
				<view class="car-fund-info-content flex">
					<view class="flex">
						<view class="flex">
							<text>收车款</text>
							<text>{{eyeIsShow?'234':'****'}}元</text>
							<view class="flex">
								<text>公允价值-通过</text>
								<u-icon name="arrow-right" style="width: 28rpx;height: 28rpx;" color="#FA6400"></u-icon>
							</view>
						</view>
						<u-line direction="col" length="76rpx" style="width: 2rpx;"></u-line>
						<view class="flex">
							<text>卖车款</text>
							<text>{{eyeIsShow?'234':'****'}}元</text>
							<view class="flex">
								<text>公允价值-通过</text>
								<u-icon name="arrow-right" style="width: 28rpx;height: 28rpx;" color="#FA6400"></u-icon>
							</view>
						</view>
					</view>
					<view class="flex">
						<view class="flex">
							<text>税</text>
							<text>{{eyeIsShow?'234':'****'}}元</text>
						</view>
						<u-line direction="col" length="76rpx" style="width: 2rpx;"></u-line>
						<view class="flex">
							<text>服务费</text>
							<text>{{eyeIsShow?'234':'****'}}元</text>
						</view>
					</view>
					<view class="flex">
						<view class="flex">
							<text>本次回填保证金</text>
							<text>{{eyeIsShow?'234':'****'}}元</text>
						</view>
						<u-line direction="col" length="76rpx" style="width: 2rpx;"></u-line>
						<view class="flex">
							<text>本次待回填差额</text>
							<text>{{eyeIsShow?'234':'****'}}元</text>
						</view>
					</view>
				</view>
			</view>
		</view>
		<view class="car-invoice" v-if="tabCar=='3'">
			<view class="flex">
				<text>二手车销售统一发票(反向)</text>
				<view class="flex">
					<text>待开票</text>
					<text class="button">开票信息</text>
				</view>
			</view>
			<view class="flex">
				<text>二手车销售统一发票(正向)</text>
				<view class="flex">
					<text>待开票</text>
					<text class="button">开票信息</text>
				</view>
			</view>
			<view class="flex">
				<text>增值税发票</text>
				<view class="flex">
					<text>待开票</text>
					<text class="button">下载PDF文件</text>
				</view>
			</view>
		</view>

	</view>
</template>

<script>
	import {
		showConfirm
	} from '@/utils/common'
	export default {
		data() {
			return {
				carUpload: true,
				eyeIsShow: false
			}
		},
		props: ['tabCar'],
		methods: {
			// 上传检测报告
			photograph() {
				let _this = this;
				uni.showActionSheet({
					// title: "选择类型",
					itemList: ['相册', '拍摄'],
					success: async function(res) {
						console.log(res.tapIndex, 'res.tapIndex')
						_this.chooseImages(res.tapIndex);
					}
				})
			},
			chooseImages(tapIndex) {
				let _this = this;
				uni.chooseImage({
					count: 1,
					sizeType: ['original', 'compressed'], //可以指定是原图还是压缩图，默认二者都有
					sourceType: tapIndex == 0 ? ['album'] : ['camera'], // 从相册选择 : 使用相机
					success: function(res) {
						console.log(JSON.stringify(res.tempFilePaths));
						uni.showModal({
							title: '提示',
							content: '您的车辆检测报告已经上传完成。',
							showCancel: false,
							confirmText: '知道了',
							confirmColor: '#f60',
							success: function(res) {
								if (res.confirm) {

								}
							}
						})
					}
				})
			},
			// 删除检测报告
			handleDelete() {
				showConfirm('删除检测报告后，您的车辆将无法发起卖车业务，若需要继续卖车请重新上传新的检测报告').then(res => {
					console.log(res, 'res')
					if (res.confirm) {

					}
				})
			},
			// 签章
			handleSignature() {
				this.$tab.navigateTo('/subPages/home/sellingCar/agreement')
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
			padding: 24rpx 26rpx 160rpx;
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

				>view {
					color: #222222;
					height: 128rpx;
					margin-left: 32rpx;
					padding-right: 38rpx;
					border-bottom: 2rpx solid #F5F5F5;
					flex-direction: column;
					justify-content: center;

					>view:nth-child(2) {
						align-items: center;
						color: #FA6400;
					}
				}

				.contrart-info__row {
					height: 78rpx;
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

		.car-fund {
			.car-fund-info {
				margin-top: 26rpx;
				width: 698rpx;
				height: 810rpx;
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
					height: calc(100% - 78rpx);
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

	}
</style>