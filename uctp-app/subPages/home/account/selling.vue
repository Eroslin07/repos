<template>
	<view class="collection">
		<!-- 自定义导航栏 -->
		<!-- <u-navbar title="我的卖车明细" leftText="返回" @leftClick="back" safeAreaInsetTop fixed placeholder></u-navbar> -->
		<uni-card :is-shadow="false" is-full>
			<u-row justify="space-between" customStyle="margin-bottom: 10px">
				<u-col span="3">
					<view class="title">已售数量</view>
					<view class="title">10辆</view>
				</u-col>
				<u-col span="3">
					<view class="demo-layout">
						<view class="title">卖车金额</view>
						<view class="title">100,500元</view>
					</view>
				</u-col>
			</u-row>
		</uni-card>
		<view class="note">已售明细</view>
		<uni-card
			v-for="(tab, tabIndex) in tabList"
			:key="tabIndex"
			title="VIN：LE4TG4DB1JL199517"
			sub-title="创建时间：2023-03-16 14:10"
			extra="车辆状态：已售出"
			is-full
			style="margin-top: 10px;"
			@click="viewDetails(tab)"
		>
			<uni-row :gutter="30">
				<uni-col :span="8">
					<view style="height: 100px;border: 1px solid red;"></view>
				</uni-col>
				<uni-col :span="16">
					<h3>宝马-宝马×12021款 sDrive20Li 时尚型</h3>
					<view>2021年02月/2.9万公里</view>
					<view style="color: #169bd5;">卖车价：100,500元</view>
				</uni-col>
			</uni-row>
		</uni-card>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				tabList: [],
			}
		},
		mounted() {
			this.tabList = [];
			for (let i = 0; i < 10; i++) {
				this.tabList.push({})
			}
		},
		methods: {
			back() {
				uni.navigateBack({
					delta: 1
				})
			},
			// 车辆明细
			viewDetails(item) {
				this.$tab.navigateTo(`/subPages/common/vehicleDetails/vehicleDetails?item=` + item);
			}
		}
	}
</script>

<style lang="scss" scoped>
	.title {
		color: #000;
		font-size: 16px;
	}
	
	.note {
		color: #000;
		height: 30px;
		line-height: 30px;
		margin-left: 20px;
	}
	
	/deep/ .uni-card__header-extra-text {
		color: #169bd5 !important;
		font-size: 14px !important;
	}
</style>