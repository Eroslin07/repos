<template>
	<view class="bond">
		<!-- 自定义导航栏 -->
		<!-- <u-navbar title="我的保证金" leftText="返回" @leftClick="back" safeAreaInsetTop fixed placeholder></u-navbar> -->
		<view style="position: relative;">
			<view class="cost_image"></view>
			<view class="statistics">
				<view><u--text suffixIcon="eye" iconStyle="font-size: 18px" text="可用余额"></u--text></view>
				<view style="font-size: 20px;font-weight: bold;margin: 16px 0;">100,500<text style="font-size: 12px;">元</text></view>
				<view style="margin-bottom: 16px;" @click="handleFreeze"><u--text suffixIcon="arrow-right" iconStyle="font-size: 18px" text="冻结余额 100,000 元"></u--text></view>
				<u-grid col="2">
					<u-grid-item>
						<button class="button" @click="handleWithdrawal" style="background-color: #fff;">提现</button>
					</u-grid-item>
					<u-grid-item>
						<button class="button" @click="handleRecharge" style="background-color: #fa6401;color: #fff;">充值</button>
					</u-grid-item>
				</u-grid>
			</view>
		</view>
		
		<view class="mingxi">
			<view class="jiaoyi">
				<u-row justify="space-between" customStyle="margin-bottom: 10px;">
					<u-col span="4">
						<view class="title">保证金交易明细</view>
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
	import { getCarhList, getDetail } from '@/api/account/bond.js'
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
					title: '保证金预扣'
				}, {
					status: 5,
					title: '保证金充值'
				}, {
					status: 6,
					title: '保证金预扣释放'
				}, {
					status: 7,
					title: '保证金实扣'
				}]
			}
		},
		onBackPress(options) {
			this.$tab.switchTab('/pages/account/index');
			return true;
		},
		mounted() {
			this.$modal.loading("数据加载中，请耐心等待...");
			this.getBondDetail();
			this.getList();
		},
		methods: {
			back() {
				uni.navigateBack({
					delta: 1
				})
			},
			// 查询我的保证金
			getBondDetail() {
				getDetail({ accountNo: '' }).then((res) => {
					
				})
			},
			// 保证金交易明细查询
			getList() {
				let data = {
					pageNo: 1,
					pageSize: 10,
					accountNo: '',
					type: 1
				}
				getCarhList(data).then((res) => {
					this.indexList = res.data.list;
					this.$modal.closeLoading();
				}).catch((error) => {
					this.$modal.closeLoading();
				})
			},
			// 提现
			handleWithdrawal() {
				this.$tab.navigateTo('/subPages/home/account/bond/withdrawal');
			},
			// 充值
			handleRecharge() {
				this.$tab.navigateTo('/subPages/home/account/bond/recharge');
			},
			// 点击冻结余额
			handleFreeze() {
				this.$tab.navigateTo('/subPages/home/account/bond/freeze');
			},
			// 点击全部
			handleWhole() {
				this.$tab.navigateTo('/subPages/home/account/bond/whole');
			},
			// 点击保证金明细列表
			handleClick(val) {
				if (val == '保证金提现中') {
					// 保证金提现中
					this.$tab.navigateTo('/subPages/home/account/bond/progress');
				} else if (val == '保证金提现') {
					// 保证金提现明细
					this.$tab.navigateTo('/subPages/home/account/bond/detailed');
				} else if (val == '保证金回填') {
					// 保证金回填
					this.$tab.navigateTo('/subPages/home/account/bond/info');
				} else if (val == '保证金预扣') {
					// 保证金预扣
					this.$tab.navigateTo('/subPages/home/account/bond/withhold');
				} else if (val == '保证金充值') {
					// 保证金充值
					this.$tab.navigateTo('/subPages/home/account/bond/rechargeDetails');
				} else if (val == '保证金预扣释放') {
					// 保证金预扣释放
					this.$tab.navigateTo('/subPages/home/account/bond/release');
				} else if (val == '保证金实扣') {
					// 保证金实扣
					this.$tab.navigateTo('/subPages/home/account/bond/actualDeduction');
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
			margin-top: 60px;
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
