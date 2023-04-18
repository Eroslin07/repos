<template>
	<view>
		<view class="search_header">
			<view class="tip-text" style="margin-bottom: 10px;">请选择您要售卖的车辆</view>
			<u-search v-model="formData.searchValue" :showAction="false" @search="search" @clear="clear"
				placeholder="请输入客户/车架号(VIN)/品牌"></u-search>
		</view>
		<!-- status==22 未检测 -->
		<view style="margin-top: 85px;">
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
						<h3>{{tab.model || '宝马-宝马×12021款 sDrive20Li 时尚型'}}</h3>
						<view class="fs12">VIN：{{tab.vin}}</view>
						<view class="fs12">{{'2023-04' || '暂无'}} | {{tab.mileage || 0}} 万公里</view>
						<view style="color: #000;" class="fs12">收车价：
							<text v-if="isSHowMoney">{{tab.vehicleReceiptAmount || 0}} 元</text>
							<text v-else>***元</text>
							<text v-if="isSHowMoney" class="iconfont icon-open-eye"
								@click.stop="isSHowMoney=!isSHowMoney"></text>
							<text v-else class="iconfont icon-close-eye" @click.stop="isSHowMoney=!isSHowMoney"></text>
						</view>
						<view class="fs12">创建时间：{{tab.createTime || '暂无'}}</view>
					</uni-col>
				</uni-row>
			</uni-card>
		</view>
		<u-modal :show="show" :showCancelButton="true" confirmText="上传检测报告" cancelText="取消" @confirm="handleConfirm"
			@cancel="handleCancel">
			<view>请先对该车辆进行检测处理，再进行卖车。</view>
		</u-modal>

		<!-- 加载 -->
		<u-loadmore :status="loadStatus" />
	</view>
</template>

<script>
	import {
		getSellPage
	} from '@/api/home/sellingCar.js'
	import {
		parseTime
	} from '@/utils/ruoyi.js'
	export default {
		data() {
			return {
				formData: {
					statusThree: [221, 231],
					searchValue: "",
					businessId:this.$store.state.user.deptId,
					pageNo: 1,
					pageSize: 10,
				},
				// searchValue: '',
				tabList: [],
				total: 0,
				show: false,
				// 加载图标
				loadStatus: 'loadmore',
				// 是否展示金额
				isSHowMoney: false
			}
		},
		mounted() {
			this.getList(this.formData);
		},
		onBackPress(options) {
			this.$tab.reLaunch('/pages/index');
			return true;
		},

		// 下拉刷新
		onPullDownRefresh() {
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
			// 获取list
			getList(obj) {
				this.tabList = [];
				this.$modal.loading("数据加载中...");
				getSellPage(obj).then((res) => {
					this.tabList = res.data.list.map(val => {
						val.createTime = parseTime(val.createTime || Number(new Date()))
						return val
					})

					this.total = res.data.total
					if (this.total > 10) {
						this.loadStatus = 'loadmore'
					} else {
						this.loadStatus = 'nomore'
					}
				}).catch(() => {
					this.loadStatus = 'nomore'
				}).finally(() => {
					this.$modal.closeLoading()
				})

			},
			// 加载更多
			getMore(obj) {
				getSellPage(obj).then(res => {
					this.tabList = [...this.tabList, ...res.data.list].map(val => {
						val.createTime = parseTime(val.createTime || Number(new Date()))
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
			// 点击车辆卡片
			handleCard(item) {
				// this.show = true;
				// this.$tab.navigateTo('/subPages/home/sellingCar/vehicleDetails');
				// return
				if (item.status == 22) {
					this.show = true;
					return
				}
				this.$tab.navigateTo('/subPages/home/sellingCar/carInfo?id=' + item.id);
			},
			// 选择其它车辆
			handleConfirm() {
				this.show = false;
				this.$tab.navigateTo('/subPages/common/vehicleDetails/vehicleDetails');
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
	.tip-text {
		margin-bottom: 10px;
		font-size: 12px;
	}

	.search_header {
		position: fixed;
		top: 44px;
		/* #ifdef MP-WEIXIN */
		top: 0;
		/* #endif */
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
		height: 100px;
		border-radius: 8px;
	}

	.car_left {
		position: relative;
		border-radius: 8px;
		overflow: hidden;
		background-color: #169bd5;

		.car_text {
			width: 100%;
			text-align: center;
			position: absolute;
			bottom: 0px;
			font-size: 12px;
			padding: 0 5px;
			border-radius: 0 0 8px 8px;
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

	.fs12 {
		font-size: 12px;
	}
</style>
