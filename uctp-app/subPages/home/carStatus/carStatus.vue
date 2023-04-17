<template>
	<view class="status-container">
		<u-search class="search" v-model="formData.searchValue" :showAction="false" @search="search" @clear="clear"
			placeholder="请输入商户/车辆型号/单号">
		</u-search>
		<!-- tab导航 -->
		<view class="" style="margin: 0 auto">
			<u-tabs :list="navList" lineColor="#FA6400" @change="handleChange"></u-tabs>
		</view>
		<!-- 列表 -->
		<view>
			<block>
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
				<uni-card v-for="(tab, tabIndex) in 2 || tabList" :key="tabIndex" @click="handleCard(tab.id)">
					<uni-row :gutter="30">
						<uni-col :span="9">
							<view class="car_left">
								<view class="car_text cell-car-draft">代售已检测</view>
								<image :src="tab.url" class="car-image"></image>
							</view>
						</uni-col>
						<uni-col :span="15">
							<h3 style="color:#000">{{tab.model || '宝马-宝马×12021款 sDrive20Li 时尚型'}}</h3>
							<view class="fs12">VIN：{{tab.vin}}</view>
							<view class="fs12">{{tab.model}} | {{tab.mileage}}万公里</view>
							<view class="fs12" style="color: #000;">收车价：
								<text v-if="eyeIsShow" style="padding-right:3px;">{{tab.vehicleReceiptAmount}}元</text>
								<text v-else style="padding-right:3px;">***元</text>
								<text v-if="eyeIsShow" class="iconfont icon-open-eye"
									@click="eyeIsShow=!eyeIsShow"></text>
								<text v-else class="iconfont icon-close-eye" @click="eyeIsShow=!eyeIsShow"></text>
							</view>
							<view class="fs12" style="color: #f60;">卖车价：
								<text v-if="!eyeIsShow && tab.vehicleReceiptAmount">***元</text>
								<text v-else>{{tab.vehicleReceiptAmount || '——'}}元</text>
							</view>
							<view class="fs12">创建时间：{{tab.createTime}}</view>
						</uni-col>
					</uni-row>
				</uni-card>

				<u-loadmore :status="status" loadingText="努力加载中..." />
			</block>
		</view>
	</view>
</template>


<script>
	import DropdownMenu from './JP-dropdown-menu/JP-dropdown-menu.vue';
	import DropdownItem from './JP-dropdown-menu/JP-dropdown-item.vue';
	import {
		getHomePageList
	} from '@/api/home.js'
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

				// 状态值数组
				collectCarState: [{
						text: '收车状态',
						value: ''
					}, {
						text: '收车草稿',
						value: 11
					},
					{
						text: '收车委托已发起',
						value: 12
					},
					{
						text: '收车合同已发起',
						value: 13
					},
					{
						text: '收车支付失败',
						value: 14
					},
					{
						text: '收车退回草稿',
						value: 15
					},
				],

				// 列表
				tabList: [],
				//收车状态
				carStatsus: null,
				formData: {
					searchValue: null,
					salesStatus: '',
					status: '',
					statusThree: '',
					businessId: '',
					"pageNo": 1,
					"pageSize": 10
				},

				// 是否展示价格
				eyeIsShow: false,
				// 加载更多
				status: 'loadmore',
				total: 0,
				timer: {},
			}
		},
		components: {
			DropdownMenu,
			DropdownItem
		},

		mounted() {
			this.getList(this.formData)
		},
		onLoad(props) {
			switch (props.text * 1) {
				case 1:
					this.carStatsus = '收车中'
					break;
				case 2:
					this.carStatsus = '待售中'
					break;
				case 3:
					this.carStatsus = '卖车中'
					break;
				case 4:
					this.carStatsus = '已售出'
					break;
			}
			uni.setNavigationBarTitle({
				title: this.carStatsus,
			})
			this.formData.salesStatus = props.text * 1 || 1
		},
		onPullDownRefresh() {
			if (this.timer != null) {
				clearTimeout(this.timer)
			}
			if (this.tabList.length == this.total) {
				this.status = 'nomore';
				return
			}
			this.status = 'loading';
			this.timer = setTimeout(() => {
				this.formData.pageNo += 1
				this.getMore(this.formData)
			}, 1000)
		},
		methods: {
			// 获取list数据
			getList(params) {
				getHomePageList(params).then(res => {
					this.tabList = res.data.list.map(v => {
						let value = this.colorArr.find(t => t.status == v.status)
						this.$set(v, 'verifyColor', value)
					})
					this.total = res.data.total;
					if (this.total > 10) {
						this.status = 'loadmore'
					} else {
						this.status = 'nomore'
					}
				}).catch((error) => {
					this.status = 'nomore'
				})
			},
			getMore(params) {
				getHomePageList(params).then(res => {
					this.tabList = [...this.tabList, ...res.data.list];
					this.total = res.data.total;
					if (this.total > this.tabList.length) {
						this.status = 'loadmore'
					} else {
						this.status = 'nomore'
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
			handleChange() {
				console.log(1111)
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
			handleCard() {
				console.log(2222)
			}
		}
	}
</script>

<style lang="scss" scoped>
	.status-container {
		width: 100%;
		height: 100%;
		padding: 0 15px;

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

			.cell-car-draft {
				color: #fff;
				background-image: linear-gradient(to right, rgba(205, 116, 2, .3) 0%, rgba(205, 116, 2, .8) 50%, rgba(205, 116, 2, .3) 100%);
			}
		}
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
