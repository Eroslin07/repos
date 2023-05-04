<template>
	<view class="status-container">
		<!-- 自定义导航栏 -->
		<u-navbar :title="title" @leftClick="back" safeAreaInsetTop fixed placeholder></u-navbar>

		<u-search class="search" v-model="formData.searchValue" :showAction="false" @search="search" @clear="clear"
			placeholder="请输入客户/车架号(VIN)/品牌">
		</u-search>
		<!-- tab导航 -->
		<view id="tabBox" class="">
			<u-tabs :scrollable="false" itemStyle="height:44px;padding:0px;" :current="current" :list="navList"
				keyName="label" lineColor="#FA6400" @change="handleChange">
			</u-tabs>
		</view>
		<!-- 列表 -->
		<view class="" v-if="!isSHowTip">
			<uni-card v-for="(tab, tabIndex) in tabList" :key="tab.id" @click="handleCard(tab)">
				<view v-if="tab.status != 11">
					<uni-row :gutter="30">
						<uni-col :span="9">
							<view class="car_left">
								<view
									:class="{'car_text':true, 'cell-car-forSale':(tab.status==22||tab.status==23||tab.status==13), 'cell-car-draft':(tab.status==11 ||tab.status==31), 'cell-car-contact':(tab.status==12||tab.status==32), 'cell-car-saled':(tab.status==41||tab.status==42||tab.status==43)}">
									{{tab.name}}
								</view>
								<image :src="tab.url? tab.url:defaultUrl" class="car-image"></image>
							</view>
						</uni-col>
						<uni-col :span="15" class="right-box">
							<h3 class="right-title">{{tab.model || '暂无'}}</h3>
							<!-- <view class="fs12">VIN：{{tab.vin || '暂无'}}</view> -->
							<view class="right-mile">{{tab.firstRegistDate||'暂无'}} | {{tab.mileage || '——'}}万公里</view>
							<!-- <view class="fs12" style="color: #000;">收车价：
								<text v-if="tab.eyeIsShow"
									style="padding-right:3px;">{{tab.vehicleReceiptAmount ||0}}元</text>
								<text v-else style="padding-right:3px;">***元</text>
								<text style="padding:2px 5px;" v-if="tab.eyeIsShow" class="iconfont icon-open-eye"
									@click.stop="tab.eyeIsShow=!tab.eyeIsShow"></text>
								<text v-else style="padding:2px 5px;" class="iconfont icon-close-eye"
									@click.stop="tab.eyeIsShow=!tab.eyeIsShow"></text>
							</view>
							<view class="fs12" style="color: #f60;">卖车价：
								<text v-if="!tab.eyeIsShow && tab.vehicleReceiptAmount">***元</text>
								<text v-else>{{tab.vehicleReceiptAmount || '——'}}元</text>
							</view> -->
							<view class="right-price">
								<view class="price-show" v-if="tab.eyeIsShow">
									<view class="">
										收：<text>{{tab.vehicleReceiptAmount | handleMoney}}万元</text>
									</view>
									<view class="sell-car-price">
										卖：<text>{{tab.sellAmount | handleMoney}}万元</text>
									</view>
								</view>
								<view class="price-show" v-if="!tab.eyeIsShow">
									<view class="">
										收：<text>****万元</text>
									</view>
									<view class="sell-car-price">
										卖：<text>****万元</text>
									</view>
								</view>
								<view class="show-money">
									<text v-if="tab.eyeIsShow" class="iconfont icon-open-eye"
										@click.stop="handleShowMoney(tab,false)"></text>
									<text v-else class="iconfont icon-close-eye"
										@click.stop="handleShowMoney(tab,true)"></text>
								</view>
							</view>

							<view class="right-time">创建时间：{{ tab.createTime }}</view>
						</uni-col>
					</uni-row>
				</view>
				<!-- 草稿 -->
				<view v-if="tab.status == 11">
					<u-swipe-action>
						<u-swipe-action-item :options="options1" @click="removeItem(tab, 'del')">
							<uni-row :gutter="30">
								<uni-col :span="9">
									<view class="car_left">
										<view
											:class="{'car_text':true, 'cell-car-forSale':(tab.status==22||tab.status==23||tab.status==13), 'cell-car-draft':(tab.status==11 ||tab.status==31), 'cell-car-contact':(tab.status==12||tab.status==32), 'cell-car-saled':(tab.status==41||tab.status==42||tab.status==43)}">
											{{tab.name}}
										</view>
										<image :src="tab.url? tab.url:defaultUrl" class="car-image"></image>
									</view>
								</uni-col>
								<uni-col :span="15" class="right-box">
									<h3 class="right-title">{{tab.model || '暂无'}}</h3>
									<view class="right-mile">{{tab.firstRegistDate||'暂无'}} | {{tab.mileage || '——'}}万公里
									</view>
									<view class="right-price">
										<view class="price-show" v-if="tab.eyeIsShow">
											<view class="">
												收：<text>{{tab.vehicleReceiptAmount | handleMoney}}万元</text>
											</view>
											<view class="sell-car-price">
												卖：<text>{{tab.sellAmount | handleMoney}}万元</text>
											</view>
										</view>
										<view class="price-show" v-if="!tab.eyeIsShow">
											<view class="">
												收：<text>****万元</text>
											</view>
											<view class="sell-car-price">
												卖：<text>****万元</text>
											</view>
										</view>
										<view class="show-money">
											<text v-if="tab.eyeIsShow" class="iconfont icon-open-eye"
												@click.stop="handleShowMoney(tab,false)"></text>
											<text v-else class="iconfont icon-close-eye"
												@click.stop="handleShowMoney(tab,true)"></text>
										</view>
									</view>

									<view class="right-time">创建时间：{{ tab.createTime }}</view>
								</uni-col>
							</uni-row>
						</u-swipe-action-item>
					</u-swipe-action>
				</view>
			</uni-card>
			<u-loadmore :status="loadStatus" loadingText="努力加载中..." />
			<view v-if="loadStatus=='nomore'" class="btm-log">
				<text class="paddingr20">助力车商</text>
				<text>经纪转经销</text>
			</view>
		</view>
		<!-- 提示信息 -->
		<AbnormalPage v-else :isSHowTip="isSHowTip" />
	</view>
</template>


<script>
	import {
		getHomePageList
	} from '@/api/home.js'
	import {
		parseTime
	} from '../../../utils/ruoyi'
	import {
		delCarInfoWithCollect
	} from '@/api/home/bycar.js'
	import AbnormalPage from '@/subPages/common/abnormaPage/index.vue'
	export default {
		data() {
			return {
				title: '',
				options1: [{
					text: '删除',
					style: {
						backgroundColor: '#f56c6c'
					}
				}],
				// tab导航
				navList: [],
				current: 0,
				// 列表
				tabList: [],

				formData: {
					searchValue: null,
					salesStatus: null,
					status: null,
					statusThree: null,
					businessId: this.$store.state.user.deptId,
					"pageNo": 1,
					"pageSize": 10
				},
				detailData: {},
				childData: {},
				childArr: [],
				allChild: [],
				// 加载更多
				loadStatus: 'loadmore',
				total: 0,
				timer: {},
				defaultUrl: '/static/images/carlistImg.png',
				type: null,
				// 提示信息
				isSHowTip: '',
			}
		},
		components: {
			AbnormalPage
		},
		filters: {
			handleMoney(val) {
				let value = parseFloat(val)
				if (value > 1000) {
					return (value / 10000).toFixed(2)
				} else if (value) {
					return value
				} else {
					return '——'
				}
			}
		},
		mounted() {
			this.getList(this.formData)
		},
		onLoad(props) {
			// console.log(props)
			this.allChild = JSON.parse(props.allChild)
			this.detailData = JSON.parse(props.item)
			// 导航栏标题
			this.title = this.detailData.label
			// tab
			this.navList = [{
				label: '全部',
				status: ''
			}, ...this.detailData.child]
			this.formData.salesStatus = this.detailData.status
			if (props.childStatus) {
				this.formData.status = props.childStatus;
				this.current = this.detailData.child.findIndex((val) => val.status == props.childStatus) + 1;
			}

		},
		// 下拉刷新
		onPullDownRefresh() {
			this.formData.pageNo = 1;
			this.getList(this.formData)
		},
		// 触底加载
		onReachBottom() {
			if (this.tabList.length == this.total) {
				this.loadStatus = 'nomore';
				return
			}
			this.loadStatus = 'loading';
			this.formData.pageNo += 1
			this.getMore(this.formData)
		},
		methods: {
			// 页面返回
			back() {
				this.$tab.switchTab('/pages/index');
			},
			// 获取list数据
			getList(params) {
				let firstTime = new Date().getTime();
				this.isSHowTip = 'onLoading'
				this.tabList = []
				getHomePageList(params).then(res => {
					let secondTime = new Date().getTime();
					console.log(secondTime,firstTime,secondTime - firstTime)
					if (secondTime - firstTime > 1000) {
						if (res.data.list.length > 0) {
							this.isSHowTip = ''
						} else {
							this.isSHowTip = 'noData'
						}
					} else {
						setTimeout(() => {
							if (res.data.list.length > 0) {
								this.isSHowTip = ''
							} else {
								this.isSHowTip = 'noData'
							}
						}, 1000)

					}

					this.tabList = res.data.list.map(item => {
						let label = this.allChild.find(v => v.status == item.status)?.label
						this.$set(item, 'eyeIsShow', false)
						return {
							...item,
							createTime: parseTime(item.createTime),
							name: label,
						}
					})
					this.total = res.data.total;

					if (this.total > 10) {
						this.loadStatus = 'loadmore'
					} else {
						this.loadStatus = 'nomore'
					}
				}).catch((err) => {
					setTimeout(() => {
						if (err == '后端接口连接异常' || err == '系统接口请求超时') {
							this.isSHowTip = 'webError'
						} else {
							this.isSHowTip = 'sysError'
						}
					}, 1000)

				}).finally(() => {
					uni.stopPullDownRefresh()
				})
			},
			getMore(params) {
				getHomePageList(params).then(res => {
					this.tabList = [...this.tabList, ...res.data.list].map(item => {
						let label = this.allChild.find(v => v.status == item.status)?.label
						this.$set(item, 'eyeIsShow', false)
						return {
							...item,
							createTime: parseTime(item.createTime),
							name: label,
						}
					})
					this.total = res.data.total;
					if (this.total > this.tabList.length) {
						this.loadStatus = 'loadmore'
					} else {
						this.loadStatus = 'nomore'
					}
				}).catch(err => {
					console.log(err)
				})
			},

			// 搜索
			search(val) {
				uni.showToast({
					title: '搜索：' + val,
					icon: 'none'
				})
				this.formData.pageNo = 1;
				this.getList(this.formData)
			},
			// tab导航
			handleChange(val) {
				this.formData.status = val.status;
				this.getList(this.formData)
			},
			// 清除
			clear(val) {
				uni.showToast({
					title: '清除：' + val,
					icon: 'none'
				})
				this.getList(this.formData)
			},
			// 显示隐藏金额
			handleShowMoney(tab, flag) {
				tab.eyeIsShow = flag;
			},
			// 删除草稿
			removeItem(item, type) {
				this.type = type;
			},
			// 查看详情
			handleCard(item) {
				if (this.type) {
					let _this = this;
					uni.showModal({
						title: '提示',
						content: '是否确认删除',
						confirmText: '确认',
						confirmColor: '#fa6401',
						success: function(res) {
							if (res.confirm) {
								delCarInfoWithCollect({
									id: item.id
								}).then((res) => {
									_this.$modal.msg("车辆信息已删除");
									_this.getList(_this.formData);
									_this.type = null;
								})
							}
						}
					})
				} else {
					if (item.status === 31) {
						this.$tab.navigateTo(`/subPages/home/sellingCar/carInfo?id=${item.id}&&status=${item.status}`);
						return;
					} else if (item.status == 11) {
						this.$tab.navigateTo('/subPages/home/bycar/index?id=' + item.id)
						return;
					} else {
						this.$tab.navigateTo('/subPages/common/vehicleDetails/vehicleDetails?item=' + JSON.stringify(item))
					}
				}
			}
		}
	}
</script>

<style lang="scss" scoped>
	.status-container {
		width: 100%;
		height: 100%;
		padding: 0 15px 40px;

		.search {
			padding: 10px 15px;
		}

		/deep/ view,
		scroll-view,
		swiper-item {
			// flex-grow: 1 !important;
			// padding:0 !important;
		}

		/* #ifdef H5 */
		// /deep/ view,
		// scroll-view,
		// swiper-item {
		// 	flex-grow: 1 !important;
		// 	padding:0 !important;
		// }

		/* #endif */
		/deep/ .uni-card {
			padding: 0 !important;
			margin: 10px 0 0 !important;
		}

		.car-image {
			width: 100%;
			height: 100%;
			border-radius: 12rpx;
		}

		.car_left {
			width: 240rpx;
			height: 190rpx;
			position: relative;
			border-radius: 8px;
			overflow: hidden;

			.car_text {
				width: 100%;
				height: 36rpx;
				line-height: 36rpx;
				text-align: center;
				position: absolute;
				bottom: 0px;
				font-size: 20rpx;
				// padding: 0 5px;
				border-radius: 0 0 12rpx 12rpx;
				z-index: 999;
			}

			// image{
			// 	vertical-align: bottom;
			// }
		}

		.right-box {
			font-size: 22rpx;
			font-family: PingFangSC-Regular, PingFang SC;
			font-weight: 400;
			line-height: 38rpx;
			text-shadow: 0px 6px 30px rgba(0, 34, 81, 0.08);
		}

		.right-title {
			font-size: 28rpx;
			color: #333333;
			font-weight: 400;
		}

		.right-mile {
			font-size: 22rpx;
			color: #999999;
		}

		.right-price {
			font-size: 22rpx;
			position: relative;
		}

		.price-show {
			font-size: 22rpx;
			display: flex;
			flex-direction: row;
		}

		.sell-car-price {
			font-size: 22rpx;
			color: #FA6400;
			margin-left: 12rpx;
		}

		.show-money {
			padding: 0 10rpx 5rpx;
			position: absolute;
			right: 5rpx;
			top: 0;
		}

		.right-time {
			font-size: 22rpx;
		}

		// 待售未检测
		.cell-car-forSale {
			color: #fff;
			background-image: linear-gradient(to right, rgba(205, 116, 2, .3) 0%, rgba(205, 116, 2, .8) 50%, rgba(205, 116, 2, .3) 100%);
		}

		// 草稿
		.cell-car-draft {
			color: #fff;
			background-image: linear-gradient(to right, rgba(132, 175, 206, .3) 0%, rgba(40, 163, 269, .8) 50%, rgba(132, 175, 206, .3) 100%);
		}

		// 合同已发起
		.cell-car-contact {
			color: #fff;
			background-image: linear-gradient(to right, rgba(233, 158, 131, .3) 0%, rgba(218, 94, 45, .8) 50%, rgba(233, 158, 131, .3) 100%);
		}

		// 已售出
		.cell-car-saled {
			color: #fff;
			background-image: linear-gradient(to right, rgba(114, 241, 181, .3) 0%, rgba(15, 156, 88, .8) 50%, rgba(114, 241, 181, .3) 100%);
		}


		.empty-page {
			width: 100%;
			position: absolute;
			left: 50%;
			top: 45%;
			transform: translate(-50%, -50%);
			text-align: center;

			.empty-img {
				width: 30%;
			}

			.empty-text {}
		}

		.loading-box {
			width: 100%;
			height: 100vh;

			image {
				width: 228rpx;
				height: 228rpx;
				margin-top: 218rpx;
				margin-left: 50%;
				transform: translateX(-50%);
			}

			.loading-text {
				text-align: center;
				font-size: 28rpx;
				color: #999;
			}
		}
	}

	.btm-log {
		text-align: center;
		color: #ddd;
	}

	.paddingr20 {
		padding-right: 20px;
	}

	.fs12 {
		font-size: 12px;
	}

	// /deep/ #tabBox .u-tabs__wrapper__nav__item{
	// 	padding:0;
	// }
</style>
