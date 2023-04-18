<template>
	<view class="bond">
		<!-- 自定义导航栏 -->
		<u-navbar title="我的利润" bgColor="rgba(0, 0, 0, 0)" leftIconColor="#fff" :titleStyle="{'color': '#fff'}" @leftClick="back" safeAreaInsetTop fixed></u-navbar>
		<view style="position: relative;">
			<view class="cost_image"></view>
			<view class="statistics">
				<view style="overflow: hidden;">
					<view style="float: left;"><u--text suffixIcon="eye" iconStyle="font-size: 18px" text="可用余额"></u--text></view>
					<view style="float: right;" @click="handleCircle"><u--text suffixIcon="error-circle" iconStyle="font-size: 18px"></u--text></view>
				</view>
				<view style="font-size: 20px;font-weight: bold;margin: 16px 0;">100,500<text style="font-size: 12px;">元</text></view>
				<view style="margin-bottom: 16px;" @click="handleFreeze"><u--text suffixIcon="arrow-right" iconStyle="font-size: 18px" text="冻结余额 100,000 元"></u--text></view>
				<view style="margin-bottom: 16px;" @click="handleBackfilled"><u--text suffixIcon="arrow-right" iconStyle="font-size: 18px" text="待回填保证金0元"></u--text></view>
				<button class="button" @click="handleWithdrawal" style="background-color: #fa6401;color: #fff;">提现</button>
			</view>
		</view>
		
		<view class="mingxi">
			<view class="jiaoyi">
				<u-row justify="space-between" customStyle="margin-bottom: 10px;">
					<u-col span="4">
						<view class="title">利润交易明细</view>
					</u-col>
					<u-col span="4">
						<view style="text-align: right;" @click="handleWhole">全部 ></view>
					</u-col>
				</u-row>
			</view>
			<view style="padding: 10px;">
				<u-list style="height: 100%;">
					<u-list-item v-for="(item, index) in indexList" :key="index">
						<view @click="handleClick(item.title)" style="line-height: 30px;">
							<u-row justify="space-between" customStyle="margin-bottom: 10px;border-bottom: 1px solid #f5f5f5;">
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
			</view>
		</view>
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
					title: '利润提现'
				}, {
					status: 3,
					title: '卖车利润'
				}, {
					status: 4,
					title: '待回填保证金'
				}, {
					status: 5,
					title: '税费扣减'
				}, {
					status: 6,
					title: '服务费扣减'
				}, {
					status: 7,
					title: '保证金回填'
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
			handleCircle() {
				uni.showModal({
				  title: '利润提现',
					showCancel: false,
				  content: '利润提现时，请提前开好发票，在APP提现申请时上传发票照片，提交申请后，请及时将纸质发票交至市场处，便于市场审核通过。',
				  confirmText: '知道了',
					confirmColor: '#fa6401'
				})
			},
			// 提现
			handleWithdrawal() {
				this.$tab.navigateTo('/subPages/home/account/profit/withdrawal');
			},
			// 点击冻结余额
			handleFreeze() {
				this.$tab.navigateTo('/subPages/home/account/profit/freeze');
			},
			// 点击待回填保证金
			handleBackfilled() {
				this.$tab.navigateTo('/subPages/home/account/profit/backfilled');
			},
			// 点击全部
			handleWhole() {
				this.$tab.navigateTo('/subPages/home/account/profit/whole');
			},
			// 点击利润明细列表
			handleClick(val) {
				if (val == '利润提现中') {
					// 利润提现中
					this.$tab.navigateTo('/subPages/home/account/profit/progressDetile');
				} else if (val == '利润提现') {
					// 利润提现
					this.$tab.navigateTo('/subPages/home/account/profit/detailed');
				} else if (val == '卖车利润') {
					// 卖车利润
					this.$tab.navigateTo('/subPages/home/account/profit/info');
				} else if (val == '待回填保证金') {
					// 待回填保证金
					this.$tab.navigateTo('/subPages/home/account/profit/backfilledDetile');
				} else if (val == '税费扣减') {
					// 税费扣减
					this.$tab.navigateTo('/subPages/home/account/profit/taxation');
				} else if (val == '服务费扣减') {
					// 服务费扣减
					this.$tab.navigateTo('/subPages/home/account/profit/serviceCharge');
				} else if (val == '保证金回填') {
					// 保证金回填
					this.$tab.navigateTo('/subPages/home/account/profit/deduction');
				}
			}
		}
	}
</script>

<style lang="scss" scoped>
	.bond {
		clear: both;
		
		.cost_image {
			width: 100%;
			height: 180px;
			background-image: url('../../../../static/images/cost/cost.png');
			background-repeat: no-repeat;
			background-size: 100% 100%;
		}
		
		.statistics {
			position: absolute;
			top: 30px;
			background-color: #fff;
			margin: 15px;
			padding: 10px;
			width: calc(100% - 30px);
			border-radius: 8px;
			box-shadow: rgba(0, 0, 0, 0.08) 0px 0px 3px 1px;
		}
		
		.mingxi {
			margin: 15px;
			margin-top: 100px;
			border-radius: 8px;
			box-shadow: rgba(0, 0, 0, 0.08) 0px 0px 3px 1px;
			
			.jiaoyi {
				padding: 0 10px;
				line-height: 45px;
				border-bottom: 1px solid #f5f5f5;
			}
		}
	}
	.button {
		width: 100%;
	}
	
	.title {
		color: #000;
	}
	
	.note {
		font-size: 12px;
		color: #a19d9d;
	}
</style>
