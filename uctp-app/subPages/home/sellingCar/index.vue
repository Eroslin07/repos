<template>
	<view>
		<view class="search_header">
			<view>请选择您要售卖的车辆。</view>
			<u-search v-model="searchValue" :showAction="false" @search="search" @clear="clear" placeholder="请输入/车辆型号/单号"></u-search>
		</view>
		<view style="margin-top: 75px;">
			<uni-card
				v-for="(tab, tabIndex) in tabList"
				:key="tabIndex"
				:title="'VIN：'+tab.vin"
				:sub-title="'创建时间：'+tab.createTime"
				:extra="'车辆状态：'+tab.status"
				is-full
				@click="handleCard(tab.id)"
				style="margin-top: 10px;"
			>
				<uni-row :gutter="30">
					<uni-col :span="8">
						<view style="height: 100px;border: 1px solid red;"></view>
					</uni-col>
					<uni-col :span="16">
						<h3>{{tab.brand}}-{{tab.year}}{{tab.model}}</h3>
						<view>{{tab.year}}年 | {{tab.mileage}}万公里</view>
						<view style="color: #68b4c5;">{{tab.vehicleReceiptAmount}}元</view>
					</uni-col>
				</uni-row>
			</uni-card>
		</view>
		<view class="warp" v-if="loading">
			<u-loading-icon text="加载中..." textSize="18" color="#fd6601" text-color="#fd6601"></u-loading-icon>
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
				loading: false
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
				this.loading = true;
				let data = {
					pageNo: 1,
					pageSize: 10,
					searchValue: this.searchValue,
					businessId: 130
				}
				getSellPage(data).then((res) => {
					this.loading = false;
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
	.search_header {
		position: fixed;
		top: 44px;
		/* #ifdef MP-WEIXIN */
		top: 0;
		/* #endif */
		width: 100%;
		padding: 10px;
		border-bottom: 1px solid #ddd;
		font-size: 16px;
		color: #fe7345;
		background-color: #fff;
		z-index: 999;
	}
	
	/deep/ .uni-card__header-extra-text {
		color: #169bd5 !important;
		font-size: 14px !important;
	}
	
	.warp {
		display: flex;
		align-items: center;
		justify-content: center;
		height: 100%;
	}
</style>