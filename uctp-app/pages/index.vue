<template>
	<view class="content">
		<!-- 自定义导航栏 -->
		<u-navbar title="车友通">
			<view class="u-nav-slot" slot="left">
				<uni-badge class="uni-badge-left-margin" :text="msgsValue" absolute="rightTop" size="small">
					<image @click="handleMsg" style="width:22px;height:22px;" src="../static/images/home/xiaoxi.png"
						class="form-image">
					</image>
				</uni-badge>
			</view>
		</u-navbar>
		<!-- 解决窗体沉浸，内容被导航栏遮盖问题 -->
		<view :style="{height: `${navigateBarHeight}px`}"></view>
		<!-- 轮播 -->
		<view class="swiper-box" id="swiperBox">
			<u-swiper indicator indicatorMode="dot" :list="swiperList"></u-swiper>
		</view>
		<!-- 收车/卖车 -->
		<view class="menu-box">
			<view class="menu-item" @click="buyCar">
				<view class="item-title">
					<text style="margin-right:5px;">我要收车</text>
					<u-icon name="arrow-right" :size="15" color="#fff">
					</u-icon>
				</view>
				<view class="item-desp">
					COLLECT
				</view>
				<image class="img" src="/static/images/index/coll-car.png" mode="widthFix"></image>
			</view>
			<view class="menu-item sell-box" @click="sellingCar">
				<view class="item-title">
					<text style="margin-right:5px;">我要卖车</text>
					<u-icon name="arrow-right" :size="15" color="#fff">
					</u-icon>
				</view>
				<view class="item-desp sell-desp">
					SELL
				</view>
				<image class="img" src="/static/images/index/sell-money.png" mode="widthFix"></image>
			</view>
		</view>
		<!-- 交易状态 -->
		<view class="deal-dynamic">
			<h3 class="align-center">
				<image class="img-size" src="/static/images/index/trace.png" mode="widthFix"></image>
			</h3>
			<view v-for="item in gatherData" :key="item.status"
				:class="{'cars-status':true,status1:item.status==1,status2:item.status==2,status3:item.status==3,status4:item.status==4}">

				<view
					:class="{'left-title':true,bc1:item.status==1,bc2:item.status==2,bc3:item.status==3,bc4:item.status==4}"
					@click="tabCarStatus(item,allChild)">
					<view class="">{{item.label}}</view>
					<view class="" style="padding-top:3px;">
						{{item.num || 0 }} 辆
					</view>
					<image
						:class="{'bcImgs':true,'firstItem':item.status==1,'fourItem':item.status==4,'btmposit':(item.status==2||item.status==3)}"
						:src="leftImgSrc(item)" mode="">
					</image>
				</view>
				<view class="right-content">
					<u-row style="height:68px;">
						<u-col span="4" v-for="child in item.child" :key="child.status"
							@click="handleTabItem(item,child.status,allChild)">
							<view class="align-center">
								<text>{{child.label}}</text>
								<uni-icons type="right" size="12" color="#656C6E"></uni-icons>
							</view>
							<view class="align-center" style="padding-top:3px">
								{{child.num}}
							</view>
						</u-col>
					</u-row>
				</view>
			</view>
		</view>
		<!-- 加载图标 -->
		<!-- <u-loadmore :status="status" loadingText="努力加载中..." /> -->
	</view>
</template>

<script>
	import {
		getHomePageList,
		getHomeCount,
		getStatusList
	} from '@/api/home.js'
	import {
		getNoticesApi,
	} from '@/api/work/message.js'
	import {
		parseTime
	} from '@/utils/ruoyi.js'
	export default {
		data() {
			return {
				businessId: this.$store.state.user.deptId,
				// 导航栏高度
				navigateBarHeight: 0,
				// 消息数字角标
				msgsValue: 0,
				// 轮播
				swiperList: [
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
				// 统计数据
				gatherData: [],
				// 所有子项
				allChild: [],
			}
		},
		onLoad: function() {
			/* #ifdef MP-WEIXIN */
			this.getnavigateBarHeight();
			/* #endif */
			uni.startPullDownRefresh();

		},
		onShow(){
			this.getAcount();
			this.getStatusValue()
			this.getListData()	
		},
		// 下拉刷新
		onPullDownRefresh() {
			this.getAcount();
			this.getStatusValue();
			this.getListData();
		},
		methods: {
			// 获取消息列表数据
			getListData() {
				getNoticesApi(this.businessId).then(res => {
					if (res.data.length) {
						this.msgsValue = res.data.filter(v => v.status === '0').length
					}
				}).catch(err => {
					console.log(err, 'err')
				})
			},
			//获取统计数据
			getAcount() {
				this.$modal.loading("数据加载中...");
				getHomeCount().then(res => {
					this.gatherData = res.data
					res.data.forEach(item => {
						this.allChild.push(...item.child)
					})
				}).catch((error) => {
					for (let i = 0; i < 4; i++) {
						this.gatherData.push({
							// salesStatus: i,
							status: i + 1,
							num: 0,
							label: '收车中',
							child: [{
								status: 11,
								num: 1,
								label: '草稿'
							}, {
								status: 11,
								num: 1,
								label: '草稿'
							}, {
								status: 11,
								num: 1,
								label: '草稿'
							}]
						})
					}
				}).finally(() => {
					this.$modal.closeLoading()
					uni.stopPullDownRefresh()
				})
			},
			// 获取状态值
			getStatusValue() {
				let data = {
					dictTypes: 'dictTypes=car_status_three,car_status,car_sales_status'
				}
				getStatusList(data).then(res => {
					this.$store.commit('setStatus', res.data)
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
			// 我要收车
			buyCar() {
				this.$tab.navigateTo('/subPages/home/bycar/index');
			},
			// 我要卖车
			sellingCar() {
				this.$tab.navigateTo('/subPages/home/sellingCar/index');
			},
			// 收车状态
			tabCarStatus(item, allChild) {
				this.$tab.navigateTo(
					`/subPages/home/carStatus/carStatus?item=${JSON.stringify(item)}&&allChild=${JSON.stringify(allChild)}`
				)
			},
			// 收车状态子项
			handleTabItem(item, childStatus, allChild) {
				this.$tab.navigateTo(
					`/subPages/home/carStatus/carStatus?item=${JSON.stringify(item)}&&childStatus=${childStatus}&&allChild=${JSON.stringify(allChild)}`
				)
			},

			// 交易动态-背景图标
			leftImgSrc(item) {
				let urlArr = [{
					status: 1,
					url: '/static/images/index/title-car.png',
				}, {
					status: 3,
					url: '/static/images/index/title-sell.png'
				}, {
					status: 2,
					url: '/static/images/index/title-sale.png'
				}, {
					status: 4,
					url: '/static/images/index/title-saled.png'
				}, ]
				return urlArr.find(val => val.status == item.status)?.url
			},

			// 消息
			handleMsg() {
				this.$tab.navigateTo(`/subPages/work/index`)
			},
			// 获取顶部导航栏的高度
			getnavigateBarHeight() {
				let menuButtonObject = uni.getMenuButtonBoundingClientRect();
				uni.getSystemInfo({
					success: res => {
						this.navigateBarHeight = res.statusBarHeight;
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
		padding: 0 28rpx;
		// height: 88vh;
		overflow-x: hidden;
		overflow-y: scroll;
		background-color: #f4f6f8;
		margin-top: 44px;

		// 轮播
		#swiperBox {
			height: 300rpx;
			border-radius: 12rpx;

			/deep/ .u-swiper.data-v-6b019429 {
				height: 300rpx !important;
			}

			/deep/ .u-swiper__wrapper.data-v-6b019429 {
				height: 300rpx !important;
			}

			/deep/ .u-swiper__wrapper__item__wrapper__image.data-v-6b019429 {
				height: 300rpx !important;
			}
		}

		.menu-box {
			height: 190rpx;
			margin-top: 22rpx;
			display: flex;
			flex-direction: row;
			justify-content: space-between;
			align-items: center;
			font-size: 15px;
			color: #fff;

			.menu-item {
				position: relative;
				flex: 1;
				flex-shrink: 0;
				height: 190rpx;
				margin-right: 36rpx;
				padding-top: 36rpx;
				padding-left: 36rpx;
				border-radius: 12rpx;
				// background-color: #088FFE;
				background-image: url('/static/images/index/collBc.png');
				background-repeat: no-repeat;
				background-size: 100% 100%;

				.item-title {
					display: flex;
					flex-direction: row;
					justify-content: flex-start;
					align-items: center;
					font-size: 32rpx;
					font-weight: 500;
					color: #FFFFFF;
					line-height: 44rpx;
				}

				.item-desp {
					font-size: 24rpx;
					font-family: PingFangSC-Medium, PingFang SC;
					font-weight: 500;
					color: #FFFFFF;
					line-height: 34rpx;
				}

				.img {
					width: 152rpx;
					height: 152rpx;
					position: absolute;
					bottom: -12px;
					right: 5px;
				}
			}

			.sell-box {
				// background-color: #DB6A43;
				margin-right: 0;
				background-image: url('/static/images/index/sellBc.png');

				.sell-desp {
					color: #FFFFFF;
				}
			}
		}

		.deal-dynamic {
			width: 100%;
			height: 750rpx;
			overflow-x: hidden;
			overflow-y: scroll;
			padding: 22rpx 26rpx 58rpx;
			margin-top: 24rpx;
			// background-color: #f6f6f6;
			// background: url('/static/images/bc.jpg') no-repeat;
			// background-size: 100% 100%;
			background-image: linear-gradient(to bottom, #FFF9EB 72rpx, #fff 90%);

			// background-image: linear-gradient(to right, red 30%, green);
			.img-size {
				width: 128rpx;
				height: 44rpx;
			}

			.cars-status {
				// box-sizing: border-box;
				width: 100%;
				height: 132rpx;
				// border: 1px solid #088FFE;
				border-radius: 12rpx;
				margin-top: 22rpx;
				display: flex;
				flex-direction: row;
				align-items: center;
				// background-color: #96C6FE;

				.left-title {
					position: relative;
					width: 140rpx;
					height: 132rpx;
					border-top-left-radius: 12rpx;
					border-bottom-left-radius: 12rpx;
					overflow: hidden;
					text-align: center;
					font-size: 28rpx;
					// background: url('/static/images/bc.jpg') no-repeat;
					// background-size: 100% 100%;
					// background-color: #2A93EC;
					color: #fff;
					display: flex;
					flex-direction: column;
					align-items: center;
					justify-content: center;

					.bcImgs {
						width: 100rpx;
						height: 100rpx;
						position: absolute;
						bottom: -25%;
						right: -15%;
					}

					.firstItem {
						width: 140rpx;
						height: 140rpx;
						right: -45%;
					}

					.fourItem {
						width: 144rpx;
						height: 144rpx;
						bottom: -30%;
						right: -30%;
					}

					.btmposit {
						bottom: -25%;
					}
				}

				.right-content {
					flex: 1;
					color: #333333;
					font-size: 28rpx;
					line-height: 40rpx;
				}

			}
		}
	}

	.status1 {
		background-color: #96C6FE;
		border: 1px solid #088FFE;
	}

	.status2 {
		background-color: #FFEEE6;
		border: 1px solid #C07843;
	}

	.status3 {
		background-color: #FDF0F0;
		border: 1px solid #CE999A;
	}

	.status4 {
		background-color: #DCF1E9;
		border: 1px solid #639C8B;
	}

	.bc1 {
		background-color: #088FFE;
	}

	.bc2 {
		background-color: #FA6500;
	}

	.bc3 {
		background-color: #EF6667;
	}

	.bc4 {
		background-color: #5BB4A3;
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
