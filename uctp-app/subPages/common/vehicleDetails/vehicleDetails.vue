<template>
	<view class="car-info">
		<uni-card :is-shadow="false" is-full padding="0" spacing="0" style="height:100%">
			<u-swiper :list="carsList" @change="e => currentNum = e.current"
				indicatorStyle="right: 30rpx;left:36rpx;bottom:44rpx;" height="426rpx">
				<view slot="indicator" style="display: flex;justify-content: space-between;">
					<view class="header-text">
						<text>卖车中-合同已发起</text>
						<text>检</text>
						<text>户</text>
					</view>
					<view class="indicator-num">
						<text class="indicator-num__text">{{ currentNum + 1 }}/{{ carsList.length }}</text>
					</view>
				</view>
			</u-swiper>
			<view class="car-content">
				<view class="car-title">
					<text>宝马-宝马X1 2021款 sDrive20Li 时尚型</text>
				</view>
				<view class="car-vin">
					<text>车架号（VIN）</text>
					<text>LE4TG4DB1JL199517</text>
				</view>
				<view class="car-details car-money">
					<view class="car-item car-payment">
						<text>付款方式</text>
						<text>定金+尾款</text>
					</view>
					<view class="_br">

					</view>
					<view class="car-item car-collection">
						<text>收款方式</text>
						<text>全款</text>
					</view>
				</view>
				<view class="car-details">
					<view class="car-item">
						<text>在库时长</text>
						<text>30天</text>
					</view>
					<view class="car-item">
						<text>首次上牌</text>
						<text>2019年6月</text>
					</view>
				</view>
				<view class="car-details">
					<view class="car-item">
						<text>里程数</text>
						<text>3万公里</text>
					</view>
					<view class="car-item">
						<text>经办人</text>
						<text>张三</text>
					</view>
				</view>
			</view>
			<view class="car-status">
				<text>车辆状态：</text>
				<text @click="showAndHide">折叠/展开</text>
			</view>
			<view class="info-box">
				<!-- 折叠信息 -->
				<view v-if="!infoIsSHow" class="info-list" v-for="(item,index) in listData" :key="index">
					<view class="info-img">
						<image class="left-image" src="@/static/images/car.jpg" mode="widthFix"></image>
						<text class="title">{{item.title}}</text>
					</view>
					<view class="baseInfo">
						{{item.status}}
					</view>
				</view>

				<!-- 展开详情 -->
				<!-- 车辆信息 -->
				<view v-if="infoIsSHow" class="show-info-list">
					<view class="displayF">
						<text class="fontW">车辆信息</text>
						<text>信息已完善</text>
					</view>
					<view class="">
						车架号：
					</view>
					<view class="">
						收车时间：：
					</view>
					<view class="">
						里程数：
					</view>
					<view class="">
						<view class="displayF">
							<text>机动车登记证书：</text>
							<text class="mr30" @click="isImgShow=!isImgShow">{{isImgShow===false?'查看':'隐藏'}}</text>
						</view>

						<view class="img-list" v-show="isImgShow">
							<u-album singleSize="70" rowCount="4" :urls="urls1" keyName="src2">
							</u-album>
						</view>
					</view>
					<view class="">
						<view class="displayF">
							<text>行驶证：</text>
							<text class="mr30"
								@click="isDriverShow=!isDriverShow">{{isDriverShow===false?'查看':'隐藏'}}</text>
						</view>
						<view class="img-list" v-show="isDriverShow">
							<u-album singleSize="70" rowCount="4" :urls="urls1" keyName="src2">
							</u-album>
						</view>
					</view>
					<view class="" style="display: flex;">
						<text>检测信息：</text>
						<u-upload v-if="fileList4.length>0" width="70" height="70" :fileList="fileList4" name="4"
							@afterRead="afterRead" @delete="deletePic" multiple :maxCount="2"></u-upload>

						<u--image v-else :showLoading="true" src="https://cdn.uviewui.com/uview/album/1.jpg"
							width="70px" height="70px"></u--image>
					</view>
				</view>
				<!-- 资金信息 -->
				<view v-if="infoIsSHow" class="show-info-list">
					<view class="displayF">
						<text class="fontW">资金信息</text>
						<text>已支出：200,000元</text>
					</view>
					<view class="">
						收车价：
					</view>
					<view class="">
						检测服务费：
					</view>
					<view class="">
						运营服务费：
					</view>
					<view class="">
						过户服务费：

					</view>
					<view class="">
						增值税费用：
					</view>
					<view class="">
						杂税费用：
					</view>
					<view class="">
						卖车价：
					</view>
					<view class="">
						利润：
					</view>
				</view>
				<!-- 合同信息 -->
				<view v-if="infoIsSHow" class="show-info-list">
					<view class="displayF">
						<text class="fontW">合同信息</text>
						<text>已签署XX份</text>
					</view>
					<view class="">
						收车委托合同：
					</view>
					<view class="">
						收车合同：
					</view>
					<view class="">
						卖车委托合同：
					</view>
					<view class="">
						卖车合同：
					</view>
					<view class="color40Ad textAlignR">
						批量下载
					</view>
					<view class="">
						卖方信息
					</view>
					<view class="">
						买方信息
					</view>
				</view>
				<!-- 发票信息 -->
				<view v-if="infoIsSHow" class="show-info-list">
					<view class="displayF">
						<text class="fontW">发票信息</text>
						<text>已开具XX份</text>
					</view>
					<view class="">
						反向二手车销售统一发票
					</view>
					<view class="">
						正向二手车销售统一发票
					</view>
					<view class="displayF">
						<text>增值税发票</text>
						<text class="color40Ad">下载</text>
					</view>

				</view>
			</view>
		</uni-card>





	</view>
</template>

<script>
	export default {
		data() {
			return {
				currentNum: 0,
				carsList: [
					'https://cdn.uviewui.com/uview/swiper/swiper1.png',
					'https://cdn.uviewui.com/uview/swiper/swiper2.png',
					'https://cdn.uviewui.com/uview/swiper/swiper3.png',
				],
				listData: [{
						title: '车辆信息',
						status: '信息已完善'
					},
					{
						title: '资金信息',
						status: '已支出：200,000元'
					},
					{
						title: '合同信息',
						status: '已签署XX份'
					},
					{
						title: '发票信息',
						status: '已开具XX份'
					},
				],
				infoIsSHow: false,
				urls1: ['https://cdn.uviewui.com/uview/album/1.jpg',
					'https://cdn.uviewui.com/uview/album/2.jpg',
					'https://cdn.uviewui.com/uview/album/3.jpg',
					'https://cdn.uviewui.com/uview/album/3.jpg',
				],

				isImgShow: false,
				isDriverShow: false,

				fileList4: [{
						url: 'https://cdn.uviewui.com/uview/swiper/1.jpg',
					},
					{
						url: 'https://cdn.uviewui.com/uview/swiper/1.jpg',
					}
				],

			};
		},
		methods: {
			showAndHide() {
				this.infoIsSHow = !this.infoIsSHow
			},

			// 删除图片
			deletePic(event) {
				this[`fileList${event.name}`].splice(event.index, 1)
			},
			// 新增图片
			async afterRead(event) {
				// 当设置 multiple 为 true 时, file 为数组格式，否则为对象格式
				let lists = [].concat(event.file)
				let fileListLen = this[`fileList${event.name}`].length
				lists.map((item) => {
					this[`fileList${event.name}`].push({
						...item,
						status: 'uploading',
						message: '上传中'
					})
				})
				for (let i = 0; i < lists.length; i++) {
					const result = await this.uploadFilePromise(lists[i].url)
					let item = this[`fileList${event.name}`][fileListLen]
					this[`fileList${event.name}`].splice(fileListLen, 1, Object.assign(item, {
						status: 'success',
						message: '',
						url: result
					}))
					fileListLen++
				}
			},
		}
	}
</script>

<style lang="scss" scoped>
	.car-info {
		height: 100vh;
		overflow: hidden;
	}
	.header-text {
		width: 334rpx;
		height: 52rpx;
		background: rgba(0, 0, 0, 0.5);
		border-radius: 28rpx;
		display: flex;
		align-items: center;
		justify-content: space-evenly;
		font-size: 22rpx;

		text {
			color: #FFFFFF;
		}

		>text:first-child {
			width: 218rpx;
			height: 40rpx;
			background: rgba(250, 100, 0, 0.8);
			border-radius: 20rpx;
			text-align: center;
		}

		>text:nth-child(2),
		>text:last-child {
			width: 40rpx;
			height: 40rpx;
			text-align: center;
			border-radius: 50%;
			background: rgba(109, 114, 120, 0.6)
		}
	}

	.indicator-num {
		padding: 2px 0;
		background-color: rgba(0, 0, 0, 0.35);
		border-radius: 100px;
		width: 35px;
		@include flex;
		justify-content: center;

		&__text {
			color: #FFFFFF;
			font-size: 12px;
		}
	}

	.car-content {
		border-radius: 24rpx 24rpx 0rpx 0rpx;
		padding: 0 36rpx;
		font-size: 24rpx;
		background: #FFFFFF;
		margin-top: -22rpx;
		position: relative;
		z-index: 10;

		>view {
			margin-bottom: 18rpx;
		}

		.car-title {
			box-sizing: border-box;
			padding: 42rpx 0 0;
			font-weight: 500;
			font-size: 36rpx;
		}

		.car-vin {
			>text:first-child {
				color: #999999;
			}
		}

		.car-details {
			display: flex;
			justify-content: space-between;
			position: relative;

			.car-item {
				width: 50%;
				display: flex;
				color: #333333;

				>text:first-child {
					color: #999999;
					width: 136rpx;
				}
			}

			._br {
				width: 2rpx;
				height: 40rpx;
				background: #DBDBDB;
				position: absolute;
				left: 50%;
				top: 2rpx;
				transform: translateX(-50%);
			}

			&.car-money {
				.car-item text {
					border-radius: 4rpx;
					font-weight: 400;
					color: #FFFFFF;
					line-height: 44rpx;
					text-align: center;
					height: 44rpx;
				}
			}

			.car-payment {
				box-sizing: border-box;
				width: 40%;

				>text:first-child {
					width: 136rpx;
					background: #FA6400;
				}

				>text:last-child {
					box-sizing: border-box;
					width: calc(100% - 136rpx);
					border: 2rpx solid #FA6400;
					color: #FA6400;
				}

			}

			.car-collection {
				width: 40%;

				>text:first-child {
					width: 138rpx;
					background: #0091FF;

				}

				>text:last-child {
					width: calc(100% - 138rpx);
					box-sizing: border-box;
					border: 2rpx solid #0091FF;
					color: #0091FF;
				}
			}
		}
	}

	.info-list {
		padding: 0 10px;
		display: flex;
		justify-content: flex-start;
		align-items: center;
		margin: 20px auto;

		.info-img {
			margin-right: 40px;

			.left-image {
				width: 80px;
				height: 80px;
				vertical-align: middle;
				margin-right: 10px;
			}

			.title {
				font-weight: bold;
				font-size: 14px;
			}

		}

		.baseInfo {
			padding-bottom: 2px;
			font-size: 14px;
		}
	}

	.show-info-list {
		padding: 10px;
		margin: 10px;
		border: 1px solid #ddd;
		line-height: 25px;

		.img-list {
			margin: 10px auto;
		}
	}

	.displayF {
		display: flex;
		justify-content: space-between;
	}

	.fontW {
		font-weight: bold;
	}

	.mr30 {
		margin-right: 30px;
	}

	.color40Ad {
		color: #40ADDD;
	}

	.textAlignR {
		text-align: right;
	}

	::v-deep .uni-card--border {
		border-bottom: none;
	}
</style>