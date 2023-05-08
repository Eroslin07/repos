<template>
	<view class="car-info">
		<uni-card v-if="!isSHowTip" :is-shadow="false" is-full padding="0" spacing="0" style="height:100%">
			<u-swiper :list="carsList" @change="e => currentNum = e.current"
				indicatorStyle="right: 30rpx;left:36rpx;bottom:44rpx;" height="426rpx">
				<view slot="indicator" style="display: flex;justify-content: space-between;">
					<view class="header-text">
						<text>{{firstStatus}}-{{secondStatus}}</text>
						<text :class="{bcClor:isShowTest}">检</text>
						<text :class="{bcClor:isShowTransfer}">户</text>
					</view>
					<view class="indicator-num">
						<text class="indicator-num__text">{{ currentNum + 1 }}/{{ carsList.length }}</text>
					</view>
				</view>
			</u-swiper>
			<view class="car-content">
				<view class="car-title">
					<text>{{carInfoAll.carInfo.model}}</text>
				</view>
				<view class="car-vin">
					<text>车架号（VIN）</text>
					<text>{{carInfoAll.carInfo.vin || '暂无'}}</text>
				</view>
				<view class="car-details car-money">
					<view class="car-item car-payment">
						<text>付款方式</text>
						<text>{{ carInfoAll.carInfoDetails.remitType || '暂无'}}</text>
					</view>
					<view class="_br">

					</view>
					<view class="car-item car-collection">
						<text>收款方式</text>
						<text>{{carInfoAll.carInfo.paymentType || '暂无'}}</text>
					</view>
				</view>
				<view class="car-details">
					<view class="car-item">
						<text>在库时长</text>
						<text>{{carInfoAll.carInfoDetails.days || 0 }}天</text>
					</view>
					<view class="car-item">
						<text>首次上牌</text>
						<text>{{carInfoAll.carInfoDetails.firstRegistDate || '暂无'}}</text>
					</view>
				</view>
				<view class="car-details">
					<view class="car-item">
						<text>里程数</text>
						<text>{{carInfoAll.carInfoDetails.mileage || 0}}万公里</text>
					</view>
					<view class="car-item">
						<text>经办人</text>
						<text>{{carInfoAll.carInfo.creator || '暂无'}}</text>
					</view>
				</view>
			</view>
			<view class="car-status" id="carStatus">
				<u-tabs :list="tabsData" @click="changeTab" :activeStyle="{ color: '#FA6400'}" lineColor="#FA6400"
					lineWidth="40rpx" lineHeight="4rpx" :scrollable="false"></u-tabs>
			</view>
			<!-- 卡片信息 -->
			<ca-content ref="contractInfo" :tabCar="tabCar" :carInfoAll="carInfoAll" :isShowTest="isShowTest"
				@changeTest="changeTest">
			</ca-content>
		</uni-card>
		<!-- 提示信息 -->
		<AbnormalPage v-else :isSHowTip="isSHowTip" />
	</view>
</template>

<script>
	import caContent from './components/carContent.vue'
	import {
		getCarInfoById
	} from '@/api/cost/carInfo.js'
	import {
		parseTime
	} from '@/utils/ruoyi.js'
	import store from '@/store/index.js'
	import AbnormalPage from '@/subPages/common/abnormaPage/index.vue'
	export default {
		components: {
			caContent,
			AbnormalPage
		},
		data() {
			return {
				//卡片详情
				carInfoAll: {
					carInfo: {},
					carInfoDetails: {},
					contractCardVOS: [],
					contractCardNOS: [],
					fileF: [],
				},
				currentNum: 0,
				// carUpload: true,
				carsList: [
					'https://img2.baidu.com/it/u=1279827528,969264118&fm=253&fmt=auto&app=138&f=JPEG?w=889&h=500',
					'https://img1.baidu.com/it/u=2974906504,2372510003&fm=253&fmt=auto&app=138&f=JPEG?w=889&h=500',
					'https://img1.baidu.com/it/u=2953355259,1397462208&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=281',
					'https://img1.baidu.com/it/u=4091777166,1843960962&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=625'
				],
				tabsData: [{
						name: '车辆信息'
					},
					{
						name: '合同信息'
					},
					{
						name: '资金信息'
					},
					{
						name: '发票信息'
					},
				],
				tabCar: 0,
				uploadeList: [],
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
				// 是否检测
				carUpload: false,

				// 父组件传过来的值
				fatherProps: null,

				// 是否展示系统异常
				isSHowTip: '',
				
				// 父组件传过来的id
				carId:'',
			};
		},

		onLoad(props) {
			// console.log(props, 'this.fatherProps.id')
			this.carId = JSON.parse(props.item)?.id || '1650085024672796674'
			this.getCarDetails(this.carId)
		},
		computed: {
			firstStatus() {
				let statusValue = store.state.allStatus.statusValue
				if (statusValue[this.carInfoAll.carInfo.salesStatus]) {
					return statusValue[this.carInfoAll.carInfo.salesStatus]
				} else {
					return '暂无'
				}
			},
			secondStatus() {
				let statusValue = store.state.allStatus.statusValue
				if (statusValue[this.carInfoAll.carInfo.status]) {
					return statusValue[this.carInfoAll.carInfo.status]
				} else {
					return '暂无'
				}

			},
			isShowTest() {
				if (this.carInfoAll.fileF.length > 0 || this.carUpload) {
					return true
				} else {
					return false
				}
			},
			isShowTransfer() {
				let arr = [22, 23, 42, 43]
				let flag = arr.indexOf(this.carInfoAll.carInfo.status)
				// console.log(flag,this.carInfoAll.carInfo.status,8899)
				if (flag > -1) {
					return true
				} else {
					return false
				}
			}
		},
		methods: {
			// 获取数据
			getCarDetails(id) {
				this.isSHowTip = 'onLoading'
				let data = {
					ID: id
				}
				getCarInfoById(data).then(res => {
					if (Object.keys(res.data)?.length) {
						this.isSHowTip = ''
					} else {
						this.isSHowTip = 'noData'
					}
					this.carInfoAll = res.data
					let {
						carInfo: {
							scrapDate,
							annualInspectionDate,
							insuranceEndData
						}
					} = res.data
					this.carInfoAll.carInfo.scrapDate = parseTime(scrapDate, '{y}-{m}-{d}')
					this.carInfoAll.carInfo.annualInspectionDate = parseTime(annualInspectionDate, '{y}-{m}-{d}')
					this.carInfoAll.carInfo.insuranceEndData = parseTime(insuranceEndData, '{y}-{m}-{d}')
					this.carsList = this.carInfoAll.fileA.map(v => v.url);
					// 库存天数
					this.$set(this.carInfoAll.carInfoDetails, 'days', this.getDays(res.data.carInfoDetails
						.createTime))
				}).catch((err) => {
					this.$modal.hideMsg();
					if (err == '后端接口连接异常' || err == '系统接口请求超时') {
						this.isSHowTip = 'webError'
					} else {
						this.isSHowTip = 'sysError'
					}
				})
			},
			// 切换tab
			changeTab(item) {
				// console.log(item)
				this.tabCar = item.index
				if (item.index != 2) {
					this.$refs.contractInfo.eyeIsShow = false
				}

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
			changeTest(val) {
				this.carUpload = val
				this.getCarDetails(this.carId)
			},

			// 时间戳转天
			getDays(value = 0) {
				let currentTime = new Date().getTime();
				return Math.floor((currentTime - value) / 1000 / 3600 / 24)
			}
		}
	}
</script>

<style lang="scss" scoped>
	.car-info {
		height: 100%;

		.img-error {
			width: 246rpx;
			height: 208rpx;
			margin-top: 514rpx;
			margin-left: 50%;
			transform: translateX(-50%);
		}

		.error-tip {
			padding-top: 18rpx;
			text-align: center;
			font-size: 24rpx;
			color: #999;
		}
	}

	.header-text {
		// width: 334rpx;
		// height: 52rpx;
		padding: 6rpx 8rpx;
		background: rgba(0, 0, 0, 0.5);
		border-radius: 28rpx;
		display: flex;
		align-items: center;
		justify-content: space-evenly;
		font-size: 22rpx;

		text {
			color: #FFFFFF;
			line-height: 40rpx;
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
			margin-left: 10rpx;
			text-align: center;
			border-radius: 50%;
			background: rgba(109, 114, 120, 0.6)
		}

		.bcClor {
			background-color: #FA6400 !important;
		}
	}

	.indicator-num {
		padding: 2px 0;
		background-color: rgba(0, 0, 0, 0.35);
		border-radius: 100px;
		width: 35px;
		height: 40rpx;
		line-height: 40rpx;
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
		border-bottom: 20rpx solid #FAFAFA;

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
					// border-radius: 4rpx;
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

	::v-deep #carStatus .u-tabs__wrapper__nav__item {
		padding: 0 !important;
	}
</style>
