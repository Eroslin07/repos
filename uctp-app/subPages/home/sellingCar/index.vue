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
				title="VIN：LE4TG4DB1JL199517"
				sub-title="创建时间：2023-03-16 14:10"
				extra="车辆状态：代售已检测"
				is-full
				@click="handleCard"
				style="margin-top: 10px;"
			>
				<uni-row :gutter="30">
					<uni-col :span="8">
						<view style="height: 100px;border: 1px solid red;"></view>
					</uni-col>
					<uni-col :span="16">
						<h3>宝马-宝马×12021款 sDrive20Li 时尚型</h3>
						<view>2021年02月/2.9万公里</view>
						<view style="color: #68b4c5;">收车价：15.13万</view>
						<view style="color: #68b4c5;">卖车价：20.00万</view>
					</uni-col>
				</uni-row>
			</uni-card>
		</view>
		<!-- <u-modal
			:show="show"
			:showCancelButton="true"
			confirmText="选择其它车辆"
			cancelText="关闭卖车页面"
			@confirm="handleConfirm"
			@cancel="handleCancel"
		>
			<view>请先对该车辆进行检测处理，再进行卖车。</view>
		</u-modal> -->
	</view>
</template>

<script>
	export default {
		data() {
			return {
				searchValue: '',
				tabList: [],
				show: false
			}
		},
		mounted() {
			this.tabList = [];
			for (let i = 0; i < 10; i++) {
				this.tabList.push({})
			}
		},
		methods: {
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
			handleCard() {
				this.$tab.navigateTo('/subPages/home/sellingCar/vehicleDetails');
				return
				this.$tab.navigateTo('/subPages/home/sellingCar/carInfo');
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
</style>