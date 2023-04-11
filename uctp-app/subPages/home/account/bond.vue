<template>
	<view class="bond">
		<!-- 自定义导航栏 -->
		<!-- <u-navbar title="我的保证金" leftText="返回" @leftClick="back" safeAreaInsetTop fixed placeholder></u-navbar> -->
		<uni-card>
			<view><u--text suffixIcon="eye" iconStyle="font-size: 18px" text="可用余额"></u--text></view>
			<view style="font-size: 20px;font-weight: bold;margin: 16px 0;">100,500<text style="font-size: 12px;">元</text></view>
			<view style="margin-bottom: 16px;"><u--text suffixIcon="arrow-right" iconStyle="font-size: 18px" text="冻结余额 100,000 元"></u--text></view>
			<u-grid col="2">
				<u-grid-item>
					<button class="button" @click="handleWithdrawal" style="background-color: #fff;">提现</button>
				</u-grid-item>
				<u-grid-item>
					<button class="button" @click="handleRecharge" style="background-color: #fa6401;color: #fff;">充值</button>
				</u-grid-item>
			</u-grid>
		</uni-card>
		<uni-card>
			<u-list style="height: 100%;">
				<view style="line-height: 45px;">
					<u-row justify="space-between" customStyle="margin-bottom: 10px;border-bottom: 1px solid #ddd;">
						<u-col span="4">
							<view class="title">保证金交易明细</view>
						</u-col>
						<u-col span="4">
							<view style="text-align: right;">全部 ></view>
						</u-col>
					</u-row>
				</view>
				<u-list-item v-for="(item, index) in indexList" :key="index">
					<view @click="handleClick(item.title)">
						<u-row justify="space-between" customStyle="margin-bottom: 10px;border-bottom: 1px solid #ddd;">
							<u-col span="4">
								<view class="title">{{ item.title }}</view>
								<view class="note">2023-03-17</view>
							</u-col>
							<u-col span="4">
								<view class="title" style="text-align: right;">+100,000 ></view>
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
			this.$tab.switchTab('/pages/account/index');
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
		width: 80%;
	}
	
	.title {
		color: #000;
	}
	
	.note {
		font-size: 12px;
		color: #a19d9d;
	}
</style>
