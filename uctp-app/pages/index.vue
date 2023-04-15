<template>
	<view class="content">
		<!-- 自定义导航栏 -->
		<u-navbar title="二手车交易平台">
			<view class="u-nav-slot" slot="left">
				<image style="width:22px;height:22px;" src="../static/images/home/xiaoxi.png" class="form-image">
				</image>
			</view>
		</u-navbar>
		<!-- 解决窗体沉浸，内容被导航栏遮盖问题 -->
		<view :style="{height: `${navigateBarHeight}px`}"></view>
		<!-- 轮播 -->
		<view class="" style="height:130px;border-radius:4px;">
			<u-swiper indicator indicatorMode="dot" :list="swiperList"></u-swiper>
		</view>
		<!-- 收车/卖车 -->
		<view class="menu-box">
			<view class="menu-item">
				<view class="item-title">
					<text style="margin-right:5px;">我要收车</text>
					<u-icon name="arrow-right" :size="15" color="#fff">
					</u-icon>
				</view>
				<view class="item-desp">
					COLLECT
				</view>
			</view>
			<view class="menu-item sell-box">
				<view class="item-title">
					<text style="margin-right:5px;">我要卖车</text>
					<u-icon name="arrow-right" :size="15" color="#fff">
					</u-icon>
				</view>
				<view class="item-desp sell-desp">
					SELL
				</view>
			</view>
		</view>
		<!-- 交易状态 -->
		<view class="deal-dynamic">
			<h3 class="align-center">交易动态</h3>
			<view class="cars-status" v-for="item in gatherData ||4" :key="item">
				<view class="left-title">
					<view class="">收车中</view>
					<view class="">
						2
					</view>
				</view>
				<view class="right-content">
					<u-row style="height:68px;">
						<u-col span="4">
							<view class="align-center">草稿<uni-icons type="right" size="12" color="#656C6E"></uni-icons>
							</view>
							<view class="align-center">
								2
							</view>
						</u-col>
						<u-col span="4">
							<view class="align-center">合同已发起<uni-icons type="right" size="12" color="#656C6E">
								</uni-icons>
							</view>
							<view class="align-center">
								2
							</view>
						</u-col>
						<u-col span="4">
							<view class="align-center">支付失败
								<uni-icons type="right" size="12" color="#ccc"></uni-icons>
							</view>
							<view class="align-center">
								2
							</view>
						</u-col>
					</u-row>
				</view>
			</view>
		</view>
		<!-- 加载图标 -->
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
				// 导航栏高度
				navigateBarHeight: 0,
				// 轮播
				swiperList: [
					'/static/images/swiper.jpg',
					'https://img2.baidu.com/it/u=1279827528,969264118&fm=253&fmt=auto&app=138&f=JPEG?w=889&h=500',
					'https://img1.baidu.com/it/u=2974906504,2372510003&fm=253&fmt=auto&app=138&f=JPEG?w=889&h=500',
					'https://img1.baidu.com/it/u=2953355259,1397462208&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=281',
					'https://img1.baidu.com/it/u=4091777166,1843960962&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=625'
				],
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
			/* #ifdef MP-WEIXIN */
			this.getnavigateBarHeight();
			/* #endif */
		},

		mounted() {
			this.getList(this.formData);

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
			},
			// 获取顶部导航栏的高度
			getnavigateBarHeight() {
				let menuButtonObject = uni.getMenuButtonBoundingClientRect();
				uni.getSystemInfo({
					success: res => {
						let navHeight = menuButtonObject.height + (menuButtonObject.top - res
							.statusBarHeight) * 2; //导航栏高度=菜单按钮高度+（菜单按钮与顶部距离-状态栏高度）*2
						this.navigateBarHeight = navHeight + 4;
						console.log(navHeight, 'navHeight')
					},
					fail(err) {
						console.log(err);
					}
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.content {
		width: 100%;
		height: calc(100vh - 44px);
		padding: 0 15px;
		// height: 88vh;
		overflow-x: hidden;
		overflow-y: scroll;
		background-color: #f4f6f8;
		margin-top: 44px;

		.menu-box {
			height: 90px;
			margin-top: 15px;
			display: flex;
			flex-direction: row;
			justify-content: space-between;
			align-items: center;
			font-size: 15px;
			color: #fff;

			.menu-item {
				flex: 1;
				flex-shrink: 0;
				height: 90px;
				margin-right: 20px;
				padding: 15px;
				border-radius: 5px;
				background-color: #088FFE;

				.item-title {
					display: flex;
					flex-direction: row;
					justify-content: flex-start;
					align-items: center;
				}

				.item-desp {
					font-size: 13px;
					margin-top: 8px;
					color: #96C6FE;
				}
			}

			.sell-box {
				background-color: #DB6A43;
				margin-right: 0;

				.sell-desp {
					color: #FFD7B1;
				}
			}
		}

		.deal-dynamic {
			width: 100%;
			height: calc(100vh - 370px);
			overflow-x: hidden;
			overflow-y: scroll;
			padding: 15px;
			margin-top: 10px;
			background-color: #f6f6f6;
			// background: url('/static/images/bc.jpg') no-repeat;
			// background-size: 100% 100%;

			.cars-status {
				box-sizing: border-box;
				width: 100%;
				height: 70px;
				border: 1px solid #088FFE;
				border-radius: 5px;
				margin-top: 10px;
				display: flex;
				flex-direction: row;
				align-items: center;
				background-color: #96C6FE;

				.left-title {
					width: 68px;
					height: 68px;
					text-align: center;
					font-size: 12px;
					// background: url('/static/images/bc.jpg') no-repeat;
					// background-size: 100% 100%;
					background-color: #2A93EC;
					color: #fff;
					display: flex;
					flex-direction: column;
					align-items: center;
					justify-content: center;
				}

				.right-content {
					flex: 1;
					color: #656C6E;
					font-size: 12px;
				}
			}
		}
	}

	/* #ifdef MP-WEIXIN */
	/deep/ .u-navbar__content.data-v-95dec1ae {
		align-items: center;
	}

	/* #endif */



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



	.align-center {
		text-align: center;
	}
</style>
