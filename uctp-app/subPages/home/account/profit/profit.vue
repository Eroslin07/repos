<template>
	<view class="bond">
		<!-- 自定义导航栏 -->
		<u-navbar title="我的利润" bgColor="rgba(0, 0, 0, 0)" leftIconColor="#fff" :titleStyle="{'color': '#fff'}" @leftClick="back" safeAreaInsetTop fixed></u-navbar>
		<view style="position: relative;">
			<view class="cost_image"></view>
			<view class="statistics">
				<view style="overflow: hidden;">
					<view style="float: left;"><u--text :suffixIcon="eyeShow == true ? 'eye-off' : 'eye'" iconStyle="font-size: 18px;margin-left: 5px;padding-right:50rpx" text="可用余额" @click="handleEye"></u--text></view>
					<view style="float: right;" @click="handleCircle"><u--text suffixIcon="error-circle" iconStyle="font-size: 18px"></u--text></view>
				</view>
				<view style="font-size: 20px;font-weight: bold;margin: 16px 0;">{{ eyeShow == true ? '****' : $amount.getComdify(profit / 100) }}<text style="font-size: 12px;">元</text></view>
				<view style="margin-bottom: 16px;" @click="handleFreeze"><u--text suffixIcon="arrow-right" iconStyle="font-size: 18px" :text="`冻结余额 ${eyeShow == true ? '****' : $amount.getComdify(freezeProfit / 100)} 元`"></u--text></view>
				<view style="margin-bottom: 16px;" @click="handleBackfilled"><u--text suffixIcon="arrow-right" iconStyle="font-size: 18px" :text="`待回填保证金 ${eyeShow == true ? '****' : $amount.getComdify(cashBack / 100)} 元`"></u--text></view>
				<button class="button" @click="handleWithdrawal" style="background-color: #fa6401;color: #fff;">提现</button>
			</view>
		</view>
		
		<view class="mingxi">
			<view class="jiaoyi">
				<u-row justify="space-between">
					<u-col span="4">
						<view class="title">利润交易明细</view>
					</u-col>
					<u-col span="4">
						<view style="text-align: right;" @click="handleWhole">全部 ></view>
					</u-col>
				</u-row>
			</view>
			<view style="padding: 10px;" v-if="indexList.length != 0">
				<u-list style="height: 100%;">
					<u-list-item v-for="(item, index) in indexList" :key="index">
						<view @click="handleClick(item.tradeType, item)" style="line-height: 30px;">
							<u-row justify="space-between" customStyle="margin-bottom: 10px;border-bottom: 1px solid #f5f5f5;">
								<u-col span="8">
									<view class="title">{{ item.tradeTypeText }}</view>
									<view class="note">{{ item.tradeDate }}</view>
								</u-col>
								<u-col span="4">
									<view class="title" style="text-align: right;">
										<text v-if="item.profitLossTypeText == '收入'">+</text>
										<!-- <text v-if="item.profitLossTypeText == '支出'">-</text> -->
										{{ $amount.getComdify(item.amount / 100 || 0) }} >
									</view>
								</u-col>
							</u-row>
						</view>
					</u-list-item>
				</u-list>
			</view>
			<view v-else class="empty-page">
				<image class="empty-img" src="@/subPages/static/images/error/noData.png" mode="widthFix"></image><br />
				<text class="empty-text" v-if="status">暂无数据</text>
			</view>
		</view>
	</view>
</template>

<script>
	import { getSummary, getProfitList, getDetail } from '@/api/account/profit.js'
	export default {
		data() {
			return {
				eyeShow: false,
				// 商户账户号
				accountNo: this.$store.state.user.accountNo,
				// 利润余额
				profit: 0,
				// 冻结余额
				freezeProfit: 0,
				// 待回填保证金
				cashBack: 0,
				indexList: [],
				status: false
			}
		},
		onLoad() {
			uni.$on('refresh', (data) => {
				if (data.refresh) {
					this.getSummary();
					this.getList();
				}
			});
		},
		onUnload: function() {
			uni.$off('refresh'); // 需要手动解绑自定义事件
		},
		mounted() {
			this.getSummary();
			this.getList();
		},
		methods: {
			back() {
				this.$tab.switchTab('/pages/account/index');
			},
			// 是否隐藏金额
			handleEye() {
				this.eyeShow = !this.eyeShow;
			},
			handleCircle() {
				uni.showModal({
				  title: '利润提现',
					showCancel: false,
				  content: '利润提现时，请提前准备好发票，并请核对开票抬头和金额。提交申请后，请及时将纸质发票交到市场方进行审核！',
				  confirmText: '知道了',
					confirmColor: '#fa6401'
				})
			},
			// 查询利润概要信息
			getSummary() {
				getSummary({ accountNo: this.accountNo }).then((res) => {
					if (res.data) {
						this.profit = res.data.profit;
						this.freezeProfit = res.data.freezeProfit;
						this.cashBack = res.data.cashBack;
					}
				})
			},
			// 查询利润明细
			getList() {
				let data = {
					pageNo: 1,
					pageSize: 10,
					accountNo: this.accountNo,
					type: 1
				}
				getProfitList(data).then((res) => {
					this.indexList = res.data.list;
					this.status = true;
				}).catch((error) => {
					this.status = true;
				})
			},
			// 提现
			handleWithdrawal() {
				this.$tab.navigateTo('/subPages/home/account/profit/withdrawal?amount=' + this.profit);
			},
			// 点击冻结余额
			handleFreeze() {
				this.$tab.navigateTo('/subPages/home/account/profit/freeze?amount=' + this.freezeProfit);
			},
			// 点击待回填保证金
			handleBackfilled() {
				this.$tab.navigateTo('/subPages/home/account/profit/backfilled?amount=' + this.cashBack);
			},
			// 点击全部
			handleWhole() {
				this.$tab.navigateTo('/subPages/home/account/profit/whole');
			},
			// 点击利润明细列表
			handleClick(val, data) {
				getDetail({ accountNo: this.accountNo, profitId: data.id }).then((res) => {
					if (val == '利润提现中') {
						// 利润提现中
						this.$tab.navigateTo('/subPages/home/account/profit/progressDetile?data='+encodeURIComponent(JSON.stringify(res.data)));
					} else if (val == '提现手续费预扣') {
						// 提现手续费预扣明细
						this.$tab.navigateTo('/subPages/home/account/profit/withdrawalWithhold?data='+encodeURIComponent(JSON.stringify(res.data)));
					} else if (val == '10101002') {
						// 利润提现
						this.$tab.navigateTo('/subPages/home/account/profit/detailed?data='+encodeURIComponent(JSON.stringify(res.data)));
					} else if (val == '提现手续费实扣') {
						// 提现手续费实扣明细
						this.$tab.navigateTo('/subPages/home/account/profit/withdrawalActual?data='+encodeURIComponent(JSON.stringify(res.data)));
					} else if (val == '利润提现退回') {
						// 利润提现退回明细
						this.$tab.navigateTo('/subPages/home/account/profit/profitFail?data='+encodeURIComponent(JSON.stringify(res.data)));
					} else if (val == '提现手续费预扣释放') {
						// 提现手续费预扣释放明细
						this.$tab.navigateTo('/subPages/home/account/profit/withdrawalWithholdFail?data='+encodeURIComponent(JSON.stringify(res.data)));
					} else if (val == '10101001' || val == '10101006') {
						// 卖车利润
						this.$tab.navigateTo('/subPages/home/account/profit/info?data='+encodeURIComponent(JSON.stringify(res.data)));
					} else if (val == '待回填保证金') {
						// 待回填保证金
						this.$tab.navigateTo('/subPages/home/account/profit/backfilledDetile?data='+encodeURIComponent(JSON.stringify(res.data)));
					} else if (val == '10101005') {
						// 税费扣减
						this.$tab.navigateTo('/subPages/home/account/profit/taxation?data='+encodeURIComponent(JSON.stringify(res.data)));
					} else if (val == '10101004') {
						// 服务费扣减
						this.$tab.navigateTo('/subPages/home/account/profit/serviceCharge?data='+encodeURIComponent(JSON.stringify(res.data)));
					} else if (val == '10101003') {
						// 保证金回填
						this.$tab.navigateTo('/subPages/home/account/profit/deduction?data='+encodeURIComponent(JSON.stringify(res.data)));
					}
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.bond {
		clear: both;
		
		.cost_image {
			width: 100%;
			height: 360rpx;
			// #ifdef MP-WEIXIN
			height: 450rpx;
			// #endif
			background-image: url('../../../../static/images/cost/cost.png');
			background-repeat: no-repeat;
			background-size: 100% 100%;
		}
		
		.statistics {
			position: absolute;
			top: 30px;
			// #ifdef MP-WEIXIN
			top: 75px;
			// #endif
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
		
		// #ifdef MP-WEIXIN
		/deep/ .u-list {
			height: auto !important;
		}
		// #endif
		
		.empty-page {
			width: 100%;
			position: absolute;
			left: 50%;
			top: 45%;
			transform: translate(-50%, 50%);
			// #ifdef MP-WEIXIN
			transform: translate(-50%, 50%);
			// #endif
			text-align: center;
		
			.empty-img {
				width: 30%;
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
