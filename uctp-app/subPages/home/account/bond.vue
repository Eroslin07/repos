<template>
	<view class="bond">
		<!-- 自定义导航栏 -->
		<!-- <u-navbar title="我的保证金" leftText="返回" @leftClick="back" safeAreaInsetTop fixed placeholder></u-navbar> -->
		<uni-card :is-shadow="false" is-full style="border-bottom: none;">
			<u-row justify="space-between" customStyle="margin-bottom: 10px">
				<u-col span="3">
					<h3>100,500元</h3>
				</u-col>
				<u-col span="3">
					<view class="demo-layout">
						<u-button type="warning" :plain="true" text="提现" @click="handleWithdrawal"></u-button>
						<u-button :plain="true" text="充值" class="button" @click="handleRecharge"></u-button>
					</view>
				</u-col>
			</u-row>
		</uni-card>
		<uni-card :is-shadow="false" is-full style="border-bottom: none;">
			<view style="margin-bottom: 10px;overflow: hidden;">
				<view class="title" style="float: left;">保证金明细</view>
				<view class="title" style="float: right;color: #75c2ff;">筛选</view>
			</view>
			<u-list style="height: 100%;">
				<u-list-item v-for="(item, index) in indexList" :key="index">
					<view @click="handleClick(item.title)">
						<u-row justify="space-between" customStyle="margin-bottom: 10px;border-bottom: 1px solid #ddd;">
							<u-col span="4">
								<view class="title">{{ item.title }}</view>
								<view class="note">2023-03-17</view>
							</u-col>
							<u-col span="4">
								<view class="title">+100,000</view>
								<view class="note">余额：100,500元</view>
							</u-col>
						</u-row>
					</view>
				</u-list-item>
			</u-list>
		</uni-card>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				indexList: [{
					status: 1,
					title: '保证金提现中'
				}, {
					status: 2,
					title: '保证金提现'
				}, {
					status: 3,
					title: '保证金回填'
				}, {
					status: 4,
					title: '保证金扣减'
				}, {
					status: 5,
					title: '保证金充值'
				}]
			}
		},
		onBackPress(options) {
			this.$tab.navigateTo('/subPages/home/account/index');
			return true;
		},
		methods: {
			back() {
				uni.navigateBack({
					delta: 1
				})
			},
			// 提现
			handleWithdrawal() {
				this.$tab.navigateTo('/subPages/home/account/withdrawal?type=' + 1);
			},
			// 充值
			handleRecharge() {
				
			},
			// 点击保证金明细列表
			handleClick(val) {
				if (val == '保证金提现中') {
					// 保证金提现中
					this.$tab.navigateTo('/subPages/home/account/progress?type=' + 1);
				} else if (val == '保证金提现') {
					// 保证金提现明细
					this.$tab.navigateTo('/subPages/home/account/detailed?type=' + val);
				} else {
					this.$tab.navigateTo('/subPages/home/account/info?type=' + val);
				}
			}
		}
	}
</script>

<style lang="scss" scoped>
	.button {
		margin-top: 10px;
	}
	
	/* #ifdef MP-WEIXIN */
	/deep/ .u-button {
		margin-top: 10px !important;
	}
	/* #endif */
	
	.title {
		color: #000;
	}
	
	.note {
		font-size: 12px;
		color: #a19d9d;
	}
</style>
