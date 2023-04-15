<template>
	<view class="content">
		<!-- 自定义导航栏 -->
		<u-navbar title=" ">
			<view class="u-nav-slot" slot="left">
				<image src="../static/images/home/xiaoxi.png" class="form-image"></image>
			</view>
		</u-navbar>

		<view class="image">
			<image src="/static/images/home/car.jpg"></image>
		</view>

		<uni-card :is-shadow="false" is-full class="searchCard">
			<u-search v-model="formData.searchValue" :showAction="false" @search="search" @clear="clear"
				placeholder="请输入商户/车辆型号/单号"></u-search>
		</uni-card>

		<view style="background-color: #fff;">
			<view style="border: 2px solid #fff; border-radius: 10px;margin: 0 10px;">
				<view class="grid-body">
					<uni-grid :column="2" :showBorder="false">
						<uni-grid-item>
							<view class="grid-item-box" @click="buyCar">图片</view>
							<view class="text-center">我要买车</view>
						</uni-grid-item>
						<uni-grid-item>
							<view class="grid-item-box" @click="sellingCar">图片</view>
							<view class="text-center">我要卖车</view>
						</uni-grid-item>
					</uni-grid>
				</view>

				<view class="car-status">
					<u-grid :border="true" col="4">
						<u-grid-item v-for="(item,index) in gatherData" :key="index"
							@click="tabCarStatus(item.salesStatus)">
							<view class="car-status-item" v-show="item.salesStatus==0">
								<text>收车中</text><br />
								<text style="color: #e26e1f;">{{item.num}}辆</text>
							</view>
							<view class="car-status-item" v-show="item.salesStatus==1">
								<text>待售中</text><br />
								<text style="color: #e26e1f;">{{item.num}}辆</text>
							</view>
							<view class="car-status-item" v-show="item.salesStatus==2">
								<text>卖车中</text><br />
								<text style="color: #e26e1f;">{{item.num}}辆</text>
							</view>
							<view class="car-status-item" v-show="item.salesStatus==3">
								<text>已售出</text><br />
								<text style="color: #e26e1f;">{{item.num}}辆</text>
							</view>
						</u-grid-item>
					</u-grid>
				</view>
			</view>
		</view>

		<uni-card v-for="(tab, tabIndex) in tabList" :key="tabIndex" style="margin-top: 10px;">
			<uni-row :gutter="30">
				<uni-col :span="8">
					<view class="car_left">
						<view class="car_text cell-car-draft">收车草稿</view>
						<view style="height: 100px;border: 1px solid #eee;"></view>
					</view>
				</uni-col>
				<uni-col :span="16">
					<h3><span class="paddingR10">{{tab.brand}}</span><span>{{tab.model}}</span></h3>
					<view>VIN: {{tab.vin}}</view>
					<view>{{tab.year}}年 | {{tab.mileage}}万公里</view>
					<view style="color: #000;">收车价：<text style="font-weight:bold">{{tab.vehicleReceiptAmount}}元</text>
					</view>
					<view style="color: #fa6400;">卖车价：<text style="font-weight:bold">200,000元</text></view>
					<view>创建时间:{{tab.createTime}}</view>
				</uni-col>
			</uni-row>
		</uni-card>

		<u-loadmore :status="status" loadingText="努力加载中..." />
	</view>
</template>

<script>
	import {
		getHomePageList,
		getHomeCount
	} from '@/api/home.js'
	import cellGroup from "../uni_modules/uview-ui/libs/config/props/cellGroup";
	export default {
		data() {
			return {
				// 搜索值
				// searchValue: "",
				// 标签内容
				tabList: [],
				formData: {
					searchValue: null,
					"pageNo": 1,
					"pageSize": 10,
				},
				status: 'loadmore',
				// currentPage: 1,
				total: 0,
				timer: {},

				// 统计数据
				gatherData: [],
			}
		},
		onLoad: function() {
			this.getAcount();
		},

		mounted() {
			this.getList(this.formData)
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
		// filters:{
		// 	filterMIle(val){
		// 		console.log(val,'val')
		// 		if(val>10000){
		// 			return parseFloat(val/10000).toFixed(2)
		// 		}else{
		// 			return val.toFixed(2)
		// 		}
		// 	}
		// },
		methods: {
			// 获取list数据
			getList(params) {
				getHomePageList(params).then(res => {
					this.tabList = res.data.list;
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

			//获取统计数据
			getAcount() {
				getHomeCount().then(res => {
					this.gatherData = res.data
					this.gatherData.sort(function(a, b) {
						return a.salesStatus - b.salesStatus
					})
				}).catch((error) => {
					for (let i = 0; i < 4; i++) {
						this.gatherData.push({
							salesStatus: i,
							num: 0
						})
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
			// 清除
			clear(val) {
				uni.showToast({
					title: '清除：' + val,
					icon: 'none'
				})
				this.getList(this.formData)
			},
			// 我要买车
			buyCar() {
				this.$tab.navigateTo('/subPages/home/bycar/index');
			},
			// 我要卖车
			sellingCar() {
				this.$tab.navigateTo('/subPages/home/sellingCar/index');
			},
			// 收车中
			tabCarStatus(text) {
				this.$tab.navigateTo(`/subPages/home/carStatus/carStatus?text=${text}`)
			},
			// 消息
			handleMsg() {
				this.$tab.navigateTo('/subPages/work/index')
			}
		}
	}
</script>

<style lang="scss" scoped>
	.content {
		width: 100%;
		// height: 88vh;
		overflow-x: hidden;
		overflow-y: scroll;
		background-color: #f4f6f8;
		margin-top: 44px;
	}

	.changing-over {
		font-size: 14px;
		height: 22px;
		line-height: 22px;
		color: orange;
		background-color: white;
		border: 1px solid orange;
		border-radius: 10px;
	}

	.image {
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		flex-direction: column;
		justify-content: center;
		align-items: center;
		height: 300rpx;
		line-height: 300rpx;

		uni-image {
			width: 100%;
		}
	}

	.grid-body {
		padding-bottom: 20px;
		border-bottom: 1px solid #ececec;

		.uni-grid-item {
			height: 100px !important;
		}

		.grid-item-box {
			// flex: 1;
			width: 52px;
			height: 52px;
			border: 1px solid black;
			border-radius: 50%;
			/* #ifndef APP-NVUE */
			display: flex;
			/* #endif */
			flex-direction: column;
			align-items: center;
			justify-content: center;
			margin: 10px auto;
		}
	}

	.searchCard {
		/* #ifdef H5 */
		padding: 0 !important;
		/* #endif */
		border: none;
	}

	/deep/ .uni-grid {
		justify-content: space-around;
	}

	/* #ifdef MP-WEIXIN */
	/deep/.grid-body .uni-grid-item {
		height: 100px !important;
	}

	/deep/.searchCard .uni-card {
		padding: 0 !important;
		border: none
	}

	/* #endif */

	/deep/ .uni-card__header-extra-text {
		color: #169bd5 !important;
		font-size: 14px !important;
	}

	.car_left {
		position: relative;
		border-radius: 8px;
		overflow: hidden;

		.car_text {
			position: absolute;
			top: 0;
			left: 0;
			font-size: 12px;
			padding: 0 5px;
			border-radius: 3px;
		}
	}

	.cell-car-draft {
		color: #fff;
		background-image: linear-gradient(to right, #2A93EC, #88C4F4);
	}

	.car-sold {
		color: #fff;
		background-image: linear-gradient(to right, #1D9A6D, #A2EED3);
	}

	.car-sell-entrust {
		color: #fff;
		background-image: linear-gradient(to right, #DB6A43, #F2C9BB);
	}

	.car-unsold-untested {
		color: #fff;
		background-image: linear-gradient(to right, #C07F1D, #F4DDB9);
	}

	.car-status {
		margin: 10px 10px;
		font-size: 14px;

		.car-status-item {
			text-align: center;
		}

		.last-car-item {
			// margin-right: 0px;
		}

	}

	.paddingR10 {
		padding-right: 10px;
	}

	.msg-btn {
		width: 50px;
		height: 50px;
		position: absolute;
		left: 15px;
		z-index: 100000
	}
</style>
