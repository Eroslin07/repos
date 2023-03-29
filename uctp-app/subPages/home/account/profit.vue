<template>
	<view class="profit">
		<!-- 自定义导航栏 -->
		<!-- <u-navbar title="我的利润" leftText="返回" @leftClick="back" safeAreaInsetTop fixed placeholder></u-navbar> -->
		<uni-card :is-shadow="false" is-full style="border-bottom: none;">
			<u-row justify="space-between" customStyle="margin-bottom: 10px">
				<u-col span="3">
					<h3>100,500元</h3>
				</u-col>
				<u-col span="3">
					<view class="demo-layout">
						<u-button type="warning" :plain="true" text="提现" @click="handleWithdrawal"></u-button>
					</view>
				</u-col>
			</u-row>
			<view>提现需要提交开票申请，开票申请金额为提现金额，待申请通过后，开具发票并上传。上传成功后即提现。</view>
		</uni-card>
		<uni-card :is-shadow="false" is-full style="border-bottom: none;">
			<view style="margin-bottom: 10px;overflow: hidden;">
				<view class="title" style="float: left;">利润明细</view>
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
					title: '利润提现中'
				}, {
					status: 2,
					title: '卖车利润'
				}, {
					status: 3,
					title: '利润提现'
				}, {
					status: 4,
					title: '利润扣减补保证金'
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
				this.$tab.navigateTo('/subPages/home/account/withdrawal?type=' + 2);
			},
			// 点击利润明细列表
			handleClick(val) {
				if (val == '利润提现中') {
					// 利润提现中
					this.$tab.navigateTo('/subPages/home/account/progress?type=' + 2);
				} else if (val == '利润提现') {
					// 利润提现明细
					this.$tab.navigateTo('/subPages/home/account/detailed?type=' + val);
				} else {
					this.$tab.navigateTo('/subPages/home/account/info?type=' + val);
				}
			}
		}
	}
</script>

<style lang="scss" scoped>
	.title {
		color: #000;
	}
	
	.note {
		font-size: 12px;
		color: #a19d9d;
	}
</style>
