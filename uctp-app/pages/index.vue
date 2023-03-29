<template>
	<view class="content">
		<view class="image">
			<image src="/static/images/home/car.jpg"></image>
		</view>

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

		<uni-card :is-shadow="false" is-full class="searchCard">
			<u-search v-model="searchValue" :showAction="false" @search="search" @clear="clear" placeholder="请输入商户/车辆型号/单号"></u-search>
			<liuyuno-tabs :tabData="tabs" @tabClick='tabClick' />
		</uni-card>
		<view v-for="(item, index) in tabs" :key="index">
			<block v-if="tabCur === index">
				<uni-card
					v-for="(tab, tabIndex) in tabList"
					:key="tabIndex"
					title="VIN：LE4TG4DB1JL199517"
					sub-title="创建时间：2023-03-16 14:10"
					extra="车辆状态：收车拟稿中"
					is-full
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
			</block>
		</view>
	</view>
</template>

<script>
	import liuyunoTabs from "@/components/liuyuno-tabs/liuyuno-tabs.vue";
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
			}
		},
		onLoad: function() {},
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
			}
		}
	}
</script>

<style lang="scss" scoped>
	.content {
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
			height: 100px !important;
		}

		.grid-item-box {
			flex: 1;
			border: 1px solid black;
			border-radius: 10px;
			/* #ifndef APP-NVUE */
			display: flex;
			/* #endif */
			flex-direction: column;
			align-items: center;
			justify-content: center;
			margin: 6px 10px;
		}
	}

	.searchCard {
		/* #ifdef H5 */
		padding: 0 !important;
		/* #endif */
	}

	/* #ifdef MP-WEIXIN */
	/deep/.grid-body .uni-grid-item {
		height: 100px !important;
	}

	/deep/.searchCard .uni-card {
		padding: 0 !important;
	}
	/* #endif */
	
	/deep/ .uni-card__header-extra-text {
		color: #169bd5 !important;
		font-size: 14px !important;
	}
</style>
