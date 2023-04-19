<template>
	<view class=" info-box">
		<view v-if="tabCar=='0'">
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
						<u-icon name="trash" color="#333333" style="width: 30rpx;height: 30rpx;"></u-icon>
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
	</view>
</template>

<script>
	export default {
		data() {
			return {
				carUpload: true,
			}
		},
		props: ['tabCar'],
		methods: {
			photograph() {
				let _this = this;
				uni.showActionSheet({
					// title: "选择类型",
					itemList: ['相册', '拍摄'],
					success: async function(res) {
						_this.chooseImages(res.tapIndex);
					}
				})
			},
			chooseImages() {},
		}
	}
</script>

<style lang="scss" scoped>
	.info-box {
		padding: 24rpx 26rpx 160rpx;

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

	}
</style>