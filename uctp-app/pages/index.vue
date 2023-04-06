<template>
	<view class="content">
		<view class="image">
			<image src="/static/images/home/car.jpg"></image>
		</view>

		<uni-card :is-shadow="false" is-full class="searchCard">
			<u-search v-model="searchValue" :showAction="false" @search="search" @clear="clear"
				placeholder="请输入商户/车辆型号/单号"></u-search>
			<!-- <liuyuno-tabs :tabData="tabs" @tabClick='tabClick' /> -->
		</uni-card>

		<view class="grid-body">
			<uni-grid :column="4" :showBorder="false">
				<uni-grid-item>
					<view class="grid-item-box" @click="buyCar">图片</view>
					<view class="text-center">我要买车</view>
				</uni-grid-item>
				<uni-grid-item>
					<view class="grid-item-box" @click="sellingCar">图片</view>
					<view class="text-center">我要卖车</view>
				</uni-grid-item>
				<uni-grid-item>
					<view class="grid-item-box" @click="handleCost">图片</view>
					<view class="text-center">我的费用</view>
				</uni-grid-item>
				<uni-grid-item>
					<view class="grid-item-box" @click="handleAccount">图片</view>
					<view class="text-center">我的账户</view>
				</uni-grid-item>
			</uni-grid>
		</view>

		<view class="car-status">
			<view v-for="(item,index) in gatherData" :key="index" class="car-status-item" @click="tabCarStatus(item.salesStatus)">
				<view class="" v-if="item.salesStatus==1">
					<text>收车中</text><br />
					<text>{{item.num}}辆</text>
				</view>
				<view class="" v-if="item.salesStatus==2">
					<text>待售中</text><br />
					<text>{{item.num}}辆</text>
				</view>
				<view class="" v-if="item.salesStatus==3">
					<text>卖车中</text><br />
					<text>{{item.num}}辆</text>
				</view>
				<view class="" v-if="item.salesStatus==4">
					<text>已售出</text><br />
					<text>{{item.num}}辆</text>
				</view>
			</view>
		</view>

		<view v-for="(item, index) in tabs" :key="index">
			<block v-if="tabCur === index">
				<uni-card v-for="(tab, tabIndex) in tabList" :key="tabIndex" style="margin-top: 10px;">
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
				</uni-card>
			</block>
		</view>

		<u-loadmore :status="status" loadingText="努力加载中..." />
	</view>
</template>

<script>
	import liuyunoTabs from "@/components/liuyuno-tabs/liuyuno-tabs.vue";
	import {
		getHomePageList,
		getHomeCount
	} from '@/api/home.js'
	import cellGroup from "../uni_modules/uview-ui/libs/config/props/cellGroup";
	export default {
		components: {
			liuyunoTabs
		},
		data() {
			return {
				// 搜索值
				searchValue: "",
				// 标签页内容
				tabs: ["收车中12辆", "查验中12辆", "待售中12辆", "卖车中12辆", "已售出12辆"],
				// 选中标签页
				tabCur: 0,
				// 标签内容
				tabList: [],
				formData: {
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
		methods: {
			// 获取list数据
			getList(params) {
				getHomePageList(params).then(res => {
					this.tabList = res.data.list;
					this.total = res.data.total;
					if(this.total>10){
						this.status='loadmore'
					}else{
						this.status='nomore'	
					}
				})
			},
			getMore(params) {
				getHomePageList(params).then(res => {
					this.tabList = [...this.tabList, ...res.data.list];
					this.total = res.data.total;
					if(this.total>this.tabList.length){
						this.status='loadmore'
					}else{
						this.status='nomore'	
					}
				})
			},

			//获取统计数据
			getAcount() {
				getHomeCount().then(res => {
					console.log(res)
					this.gatherData = res.data
				})
			},
			// 搜索
			search(val) {
				uni.showToast({
					title: '搜索：' + val,
					icon: 'none'
				})
			},
			// 清除
			clear(val) {
				uni.showToast({
					title: '清除：' + val,
					icon: 'none'
				})
			},
			// tabs切换
			tabClick(e) {
				this.tabCur = e;
				this.tabList = [];
				for (var i = 0; i < 10; i++) {
					this.tabList.push({})
				}
			},
			// 我要买车
			buyCar() {
				this.$tab.navigateTo('/subPages/home/bycar/index');
			},
			// 我要卖车
			sellingCar() {
				this.$tab.navigateTo('/subPages/home/sellingCar/index');
			},
			// 我的费用
			handleCost() {
				this.$tab.navigateTo('/subPages/home/cost/index');
			},
			// 我的账户
			handleAccount() {
				this.$tab.navigateTo('/subPages/home/account/index');
			},
			// 收车中
			tabCarStatus(text) {
				this.$tab.navigateTo('/subPages/home/carStatus/carStatus?text=' + text)

			}
		}
	}
</script>

<style lang="scss" scoped>
	.content {
		width: 100%;
		height: 100vh;
		overflow-x: hidden;
		overflow-y: scroll;
		background-color: #f1f1f1;
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
		.uni-grid-item {
			// height: 100px !important;
		}

		.grid-item-box {
			// flex: 1;
			width: 16vw;
			height: 16vw;
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
	}

	/deep/ .uni-grid {
		justify-content: space-around;
	}

	/* #ifdef MP-WEIXIN */
	/deep/.grid-body .uni-grid-item {
		// height: 100px !important;
	}

	/deep/.searchCard .uni-card {
		padding: 0 !important;
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
		height: 80px;
		margin: 10px 10px;
		display: flex;
		overflow: hidden;
		align-items: center;
		justify-content: space-around;

		.car-status-item {
			width: 100%;
			height: 100%;
			border: 1px solid #ccc;
			margin: 0 5px;
			border-radius: 3px;
			text-align: center;
			// line-height: 80px;
			background-color: #fff;
			display: flex;
			align-items: center;
			justify-content: center;
		}

		.last-car-item {
			// margin-right: 0px;
		}

	}
</style>
