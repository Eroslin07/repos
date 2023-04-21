<template>
	<view class="status-container">
		<u-search class="search" v-model="formData.searchValue" :showAction="false" @search="search" @clear="clear"
			placeholder="请输入商户/车辆型号/单号">
		</u-search>
		<!-- tab导航 -->
		<view class="" style="margin: 0 auto">
			<u-tabs :current="current" :list="navList" lineColor="#FA6400" @change="handleChange"></u-tabs>
		</view>
		<!-- 列表 -->

		<!-- <uni-card v-for="(tab, tabIndex) in 2|| tabList" :key="tabIndex" style="margin-top: 10px;">
					<uni-row :gutter="30">
						<uni-col :span="8">
							<view class="car_left">
								<view class="car_text cell-car-draft">收车草稿</view>
								<view style="height: 100px;border: 1px solid #eee;"></view>
							</view>
						</uni-col>
						<uni-col :span="16">
							<h3>宝马-宝马×12021款 sDrive20Li 时尚型</h3>
							<view>2021年02月 | 2.9万公里</view>
							<view style="color: #000;">收车价：151,300元</view>
							<view>卖车价：<text style="color: #fa6400;font-weight:bold">200,000元</text></view>
						</uni-col>
						<uni-col :span="12">
							<view style="font-size: 10px;">VIN: LE4TG4DB1JL199517</view>
						</uni-col>
						<uni-col :span="12">
							<view style="font-size: 10px;">创建时间:2023-03-1514:10</view>
						</uni-col>
					</uni-row>
				</uni-card> -->

		<view class="" v-if="tabList.length>0">
			<uni-card v-for="(tab, tabIndex) in tabList" :key="tabIndex" @click="handleCard(tab)">
				<uni-row :gutter="30">
					<uni-col :span="9">
						<view class="car_left">
							<view
								:class="{'car_text':true, 'cell-car-forSale':(tab.status==22||tab.status==23||tab.status==13), 'cell-car-draft':(tab.status==11 ||tab.status==31), 'cell-car-contact':(tab.status==12||tab.status==32), 'cell-car-saled':(tab.status==41||tab.status==42||tab.status==43)}">
								{{tab.name}}
							</view>
							<image :src="tab.url? tab.url:defaultUrl" class="car-image"></image>
							<!-- <image src="/static/images/car.jpg" class="car-image"></image> -->
						</view>
					</uni-col>
					<uni-col :span="15">
						<h3 style="color:#000">{{tab.model || '宝马-宝马×12021款 sDrive20Li 时尚型'}}</h3>
						<view class="fs12">VIN：{{tab.vin || '暂无'}}</view>
						<view class="fs12">{{tab.firstRegistDate||'暂无'}} | {{tab.mileage || '暂无'}}万公里</view>
						<view class="fs12" style="color: #000;">收车价：
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
						</view>
						<view class="fs12">创建时间：{{ tab.createTime }}</view>
					</uni-col>
				</uni-row>
			</uni-card>
			<u-loadmore :status="loadStatus" loadingText="努力加载中..." />
			<view v-if="loadStatus=='nomore'" class="btm-log">
				<text class="paddingr20">助力车商</text>
				<text>经纪转经销</text>
			</view>
		</view>
		<view v-else class="empty-page">
			<image class="empty-img" src="/static/images/index/noData.png" mode="widthFix"></image><br />
			<text class="empty-text">暂无数据</text>
		</view>
	</view>
</template>


<script>
	// import DropdownMenu from './JP-dropdown-menu/JP-dropdown-menu.vue';
	// import DropdownItem from './JP-dropdown-menu/JP-dropdown-item.vue';
	import {
		getHomePageList
	} from '@/api/home.js'
	// import cellGroup from '../../../uni_modules/uview-ui/libs/config/props/cellGroup'
	import {
		parseTime
	} from '../../../utils/ruoyi'

	export default {
		data() {
			return {
				// tab导航
				navList: [{
						name: '全部'
					},
					{
						name: '草稿'
					},
					{
						name: '合同已发起'
					},
					{
						name: '支付失败'
					}
				],
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
				defaultUrl: '/static/images/carlistImg.png'
			}
		},
		// components: {
		// 	DropdownMenu,
		// 	DropdownItem
		// },

		mounted() {
			this.getList(this.formData)
		},
		onLoad(props) {
			this.allChild = JSON.parse(props.allChild)
			this.detailData = JSON.parse(props.item)
			this.childArr = this.detailData.child.map(v => {
				return {
					name: v.label,
					...v
				}
			})
			this.navList = [{
				name: '全部',
				status: ''
			}, ...this.childArr]
			uni.setNavigationBarTitle({
				title: this.detailData.label,
			})
			this.formData.salesStatus = this.detailData.status
			if (props.child) {
				this.childData = JSON.parse(props.child)
				this.formData.status = this.childData.status
				this.current = this.detailData.child.findIndex((val) => val.status == this.formData.status) + 1
			}
			uni.startPullDownRefresh();
		},
		// 下拉刷新
		onPullDownRefresh() {
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
			// 获取list数据
			getList(params) {
				this.$modal.loading("数据加载中...");
				getHomePageList(params).then(res => {
					this.tabList = res.data.list.map(item => {
						let label = this.allChild.find(v => v.status == item.status)?.label
						return {
							eyeIsShow: false,
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
				}).catch((error) => {
					this.loadStatus = 'nomore'
				}).finally(() => {
					this.$modal.closeLoading()
					uni.stopPullDownRefresh()
				})
			},
			getMore(params) {
				getHomePageList(params).then(res => {
					this.tabList = [...this.tabList, ...res.data.list].map(item => {
						let label = this.allChild.find(v => v.status == item.status)?.label
						return {
							eyeIsShow: false,
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
				})
			},

			// 搜索
			search(val) {
				uni.showToast({
					title: '搜索：' + val,
					icon: 'none'
				})
				this.getList(this.formData)
			},
			// tab导航
			handleChange(val) {
				this.formData.status = this.detailData.child.find(item => item.status == val.status)?.status || ''
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

			// 查看详情
			handleCard(item) {
				console.log(item, 2222)
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
</script>

<style lang="scss" scoped>
	.status-container {
		width: 100%;
		height: 100%;
		padding: 0 15px 40px;

		.search {
			padding: 10px 15px;
		}

		/deep/ .u-tabs__wrapper__nav__item {
			// width:86px;
			// margin:0 auto;
		}

		/deep/ view.data-v-48634e29,
		scroll-view.data-v-48634e29,
		swiper-item.data-v-48634e29 {
			flex-grow: 1 !important;
		}

		/deep/ .uni-card {
			padding: 0 !important;
			margin: 10px 0 0 !important;
		}

		.car-image {
			width: 100%;
			height: 100px;
			border-radius: 8px;
		}

		.car_left {
			position: relative;
			border-radius: 8px;
			overflow: hidden;

			.car_text {
				width: 100%;
				text-align: center;
				position: absolute;
				bottom: 6px;
				font-size: 12px;
				padding: 0 5px;
				border-radius: 0 0 8px 8px;
				z-index: 999;
			}
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
	}

	.btm-log {
		text-align: center;
		color: #ddd;
	}

	.paddingr20 {
		padding-right: 20px;
	}

	// .cellDraft {
	// 	padding: 10px;
	// 	border-bottom: 1px solid #eee;
	// 	font-size: 12px;
	// }

	// .cellDraft:hover {
	// 	background-color: #ccc;
	// }

	// .tag-box {
	// 	width: 100vw;
	// 	height: 50px;
	// 	line-height: 50px;
	// 	// padding: 0 15px;
	// 	margin: 0 15px;
	// 	margin-right: 15px;
	// 	overflow-x: scroll;

	// 	.tag-item {
	// 		padding: 3px 5px;
	// 		margin-right: 5px;
	// 		border: 1px solid #ccc;
	// 		border-radius: 5px;
	// 	}
	// }

	// .car_left {
	// 	position: relative;
	// 	border-radius: 8px;
	// 	overflow: hidden;

	// 	.car_text {
	// 		position: absolute;
	// 		top: 0;
	// 		left: 0;
	// 		font-size: 12px;
	// 		padding: 0 5px;
	// 		border-radius: 3px;
	// 	}
	// }

	// .cell-car-draft {
	// 	color: #fff;
	// 	background-image: linear-gradient(to right, #2A93EC, #88C4F4);
	// }

	// .car-sold {
	// 	color: #fff;
	// 	background-image: linear-gradient(to right, #1D9A6D, #A2EED3);
	// }

	// .car-sell-entrust {
	// 	color: #fff;
	// 	background-image: linear-gradient(to right, #DB6A43, #F2C9BB);
	// }

	// .car-unsold-untested {
	// 	color: #fff;
	// 	background-image: linear-gradient(to right, #C07F1D, #F4DDB9);
	// }

	.fs12 {
		font-size: 12px;
	}
</style>
