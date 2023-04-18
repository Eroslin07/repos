<template>
	<view>
		<view class="search_header">
			<view class="tip-text" style="margin-bottom: 10px;">请选择您要售卖的车辆</view>
			<u-search v-model="searchValue" :showAction="false" @search="search" @clear="clear" placeholder="请输入客户/车架号(VIN)/品牌"></u-search>
		</view>
		<view style="margin-top: 85px;">
			<uni-card
				v-for="(tab, tabIndex) in 2"
				:key="tabIndex"
				@click="handleCard(tab.id)"
				style="margin-top: 10px;"
			>
				<uni-row :gutter="30">
					<uni-col :span="10">
						<view class="car_left">
							<view class="no-sell">
								不能卖出
							</view>
							<view class="car_text cell-car-draft">待售已检测</view>
							<image :src="tab.url" class="car-image"></image>
						</view>
					</uni-col>
					<uni-col :span="14">
						<h3>{{tab.model}}</h3>
						<view>VIN：{{tab.vin}}</view>
						<view>{{tab.model}} | {{tab.mileage}}万公里</view>
						<view style="color: #000;">收车价：{{tab.vehicleReceiptAmount}}元</view>
						<view>创建时间：{{tab.createTime}}</view>
					</uni-col>
				</uni-row>
			</uni-card>
		</view>
		<u-modal
			:show="show"
			:showCancelButton="true"
			confirmText="选择其它车辆"
			cancelText="关闭卖车页面"
			@confirm="handleConfirm"
			@cancel="handleCancel"
		>
			<view>请先对该车辆进行检测处理，再进行卖车。</view>
		</u-modal>
	</view>
</template>

<script>
	import { getSellPage } from '@/api/home/sellingCar.js'
	export default {
		data() {
			return {
				searchValue: '',
				tabList: [],
				show: false,
			}
		},
		mounted() {
			this.getList();
		},
		onBackPress(options) {
			this.$tab.reLaunch('/pages/index');
			return true;
		},
		methods: {
			getList() {
				this.tabList = [];
				let data = {
					pageNo: 1,
					pageSize: 10,
					searchValue: this.searchValue,
					statusThree: [231, 221],
					businessId: 130
				}
				this.$modal.loading("数据加载中...");
				getSellPage(data).then((res) => {
					this.$modal.closeLoading()
					this.tabList = res.data.list
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
			// 点击车辆卡片
			handleCard(id) {
				// this.show = true;
				// this.$tab.navigateTo('/subPages/home/sellingCar/vehicleDetails');
				// return
				this.$tab.navigateTo('/subPages/home/sellingCar/carInfo?id='+id);
			},
			// 选择其它车辆
			handleConfirm() {
				this.show = false;
			},
			// 关闭卖车页面
			handleCancel() {
				this.show = false;
				this.$tab.reLaunch('/pages/index');
			}
		}
	}
</script>

<style lang="scss" scoped>
	.tip-text{
		margin-bottom:10px;
		font-size:12px;
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
		.no-sell{
			position: absolute;
			top:0;
			left:0;
			padding:0 2px;
			background-color: rgba(0, 0, 0, .5);
			font-size: 12px;
			color:#fff;
		}
		.cell-car-draft {
			color: #fff;
			background-image: linear-gradient(to right, rgba(205, 116, 2, .3) 0%, rgba(205, 116, 2, .8) 50%, rgba(205, 116, 2, .3) 100%);
		}
	}
</style>