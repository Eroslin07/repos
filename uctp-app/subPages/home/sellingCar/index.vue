<template>
	<view class="selling-list-container">
		<!-- 自定义导航栏 -->
		<u-navbar title="我要卖车" @leftClick="back" border safeAreaInsetTop fixed placeholder></u-navbar>
		<view class="search_header">
			<view class="tip-text" style="margin-bottom: 10px;">请选择您要售卖的车辆</view>
			<u-search v-model="formData.searchValue" :showAction="false" @search="search" @clear="clear"
				placeholder="请输入客户/车架号(VIN)/品牌"></u-search>
		</view>
		<!-- status==22 未检测 -->
		<view v-if="!isSHowTip">
			<uni-card v-for="(tab, tabIndex) in tabList" :key="tabIndex" @click="handleCard(tab)"
				style="margin-top: 10px;">
				<uni-row :gutter="20">
					<uni-col :span="9">
						<view class="car_left">
							<view v-if="tab.status==22" class="no-sell">
								不能卖出
							</view>
							<view class="car_text cell-car-draft">{{tab.status==22?'待售未检测':'待售已检测'}}</view>
							<image :src="tab.url" class="car-image"></image>
						</view>
					</uni-col>
					<uni-col :span="15">
						<h3 class="right-title">{{tab.model || '暂无'}}</h3>
						<!-- <view class="fs12">VIN：{{tab.vin}}</view> -->
						<view class="right-mile">{{'2023-04' || '暂无'}} | {{tab.mileage || '——'}} 万公里</view>
						<view class="right-price">收车价：
							<text v-if="tab.isSHowMoney">{{tab.vehicleReceiptAmount | handleMoney}} 万元</text>
							<text v-else>***元</text>
							<text v-if="tab.isSHowMoney" class="iconfont icon-open-eye eyeIcon"
								@click.stop="handleMoneyShow(tab,false)"></text>
							<text v-else class="iconfont icon-close-eye eyeIcon"
								@click.stop="handleMoneyShow(tab,true)"></text>
						</view>
						<view class="right-time">创建时间：{{tab.createTime || '暂无'}}</view>
					</uni-col>
				</uni-row>
			</uni-card>
			<!-- 加载 -->
			<u-loadmore :status="loadStatus" />
		</view>
		<!-- 提示信息 -->
		<AbnormalPage v-else :isSHowTip="isSHowTip"/>
		
		<u-modal :show="show" :showCancelButton="true" confirmText="上传检测报告" cancelText="取消" @confirm="handleConfirm"
			@cancel="handleCancel">
			<view>请先对该车辆进行检测处理，再进行卖车。</view>
		</u-modal>
		
		
	</view>
</template>

<script>
	import {
		getSellPage
	} from '@/api/home/sellingCar.js'
	import {
		parseTime
	} from '@/utils/ruoyi.js'
	import AbnormalPage from '@/subPages/common/abnormaPage/index.vue'
	export default {
		data() {
			return {
				formData: {
					statusThree: [221, 231],
					searchValue: "",
					businessId: this.$store.state.user.deptId,
					pageNo: 1,
					pageSize: 10,
				},
				// searchValue: '',
				tabList: [],
				total: 0,
				show: false,
				// 加载图标
				loadStatus: 'loadmore',
				isSHowTip:'',
				carId:'',
			}
		},
		components: {
			AbnormalPage
		},
		filters:{
			handleMoney(val){
				let value=parseFloat(val)
				if(value>1000){
					return (value/10000).toFixed(2)
				}else if(value){
					return value
				}else{
					return '——'
				}
			}
		},
		mounted() {
			this.getList(this.formData);
		},
		// 下拉刷新
		onPullDownRefresh() {
			this.formData.pageNo = 1;
			this.getList(this.formData)
		},
		// 触底刷新
		onReachBottom() {
			if (this.total == this.tabList.length) {
				this.loadStatus = 'nomore'
				return
			}
			this.loadStatus = 'loading'
			this.formData.pageNo += 1
			this.getMore(this.formData)
		},
		methods: {
			// 页面返回
			back() {
				this.$tab.switchTab('/pages/index');
			},
			// 获取list
			getList(obj) {
				this.tabList = [];
				this.isSHowTip='onLoading'
				getSellPage(obj).then((res) => {
					if(res.data.total){
						this.isSHowTip=''
					}else{
						this.isSHowTip='noData'
					}
					this.tabList = res.data.list.map(val => {
						val.createTime = parseTime(val.createTime || Number(new Date()))
						this.$set(val, 'isSHowMoney', false)
						return val
					})

					this.total = res.data.total
					if (this.total > 10) {
						this.loadStatus = 'loadmore'
					} else{
						this.loadStatus = 'nomore'
					}
				}).catch((err) => {
					if (err == '后端接口连接异常' || err == '系统接口请求超时') {
						this.isSHowTip = 'webError'
					} else {
						this.isSHowTip = 'sysError'
					}
				}).finally(() => {
					this.$modal.closeLoading()
					uni.stopPullDownRefresh()
				})

			},
			// 加载更多
			getMore(obj) {
				getSellPage(obj).then(res => {
					this.tabList = [...this.tabList, ...res.data.list].map(val => {
						val.createTime = parseTime(val.createTime || Number(new Date()))
						this.$set(val, 'isSHowMoney', false)
						return val;
					})
					this.total = res.data.total
					if (this.total > this.tabList.length) {
						this.loadStatus = 'loadmore'
					} else {
						this.loadStatus = 'nomore'
					}
				}).catch(() => {
					this.loadStatus = 'nomore'
				})
			},
			// 搜索
			search(val) {
				uni.showToast({
					title: '搜索：' + val,
					icon: 'none'
				})
				this.formData.pageNo = 1;
				this.getList(this.formData);
			},
			// 清除
			clear(val) {
				uni.showToast({
					title: '清除：' + val,
					icon: 'none'
				})
				this.getList(this.formData);
			},
			// 显示隐藏金额
			handleMoneyShow(tab, flag) {
				// console.log(tab, flag, 8899)
				tab.isSHowMoney = flag;
			},
			// 点击车辆卡片
			handleCard(item) {
				this.carId=item.id
				// this.show = true;
				// this.$tab.navigateTo('/subPages/home/sellingCar/vehicleDetails');
				// return
				if (item.status == 22) {
					this.show = true;
					return
				}
				this.$tab.navigateTo('/subPages/home/sellingCar/carInfo?id=' + item.id);
			},
			// 上传检测报告
			handleConfirm() {
				this.show = false;
				this.$tab.navigateTo('/subPages/common/vehicleDetails/vehicleDetails?id='+this.carId);
			},
			// 关闭卖车页面
			handleCancel() {
				this.show = false;
				// this.$tab.reLaunch('/pages/index');
			}
		}
	}
</script>

<style lang="scss" scoped>
	.selling-list-container{
		height:100%;
	}
	.tip-text {
		margin-bottom: 10px;
		font-size: 12px;
	}

	.search_header {
		width: 100%;
		padding: 10px;
		font-size: 16px;
		background-color: #fff;
		z-index: 999;
	}

	/deep/ .uni-card__header-extra-text {
		color: #169bd5 !important;
		font-size: 14px !important;
	}

	/deep/ .uni-card {
		padding: 0 !important;
	}

	.car-image {
		width: 100%;
		height: 100%;
		border-radius: 8px;
	}

	.car_left {
		width: 240rpx;
		height: 190rpx;
		position: relative;
		border-radius: 12rpx;
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

		.no-sell {
			position: absolute;
			top: 0;
			left: 0;
			padding: 0 2px;
			background-color: rgba(0, 0, 0, .5);
			font-size: 12px;
			color: #fff;
		}

		.cell-car-draft {
			color: #fff;
			background-image: linear-gradient(to right, rgba(205, 116, 2, .3) 0%, rgba(205, 116, 2, .8) 50%, rgba(205, 116, 2, .3) 100%);
		}
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

	.eyeIcon {
		position: absolute;
		right: 6rpx;
		top: 0;
	}

	.right-time {
		font-size: 22rpx;
	}

	.fs12 {
		font-size: 12px;
	}
</style>
