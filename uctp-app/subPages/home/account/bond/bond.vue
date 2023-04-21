<template>
	<view class="bond">
		<!-- 自定义导航栏 -->
		<u-navbar title="我的保证金" bgColor="rgba(0, 0, 0, 0)" leftIconColor="#fff" :titleStyle="{'color': '#fff'}" @leftClick="back" safeAreaInsetTop fixed></u-navbar>
		<view style="position: relative;">
			<view class="cost_image"></view>
			<view class="statistics">
				<view><u--text :suffixIcon="eyeShow == true ? 'eye-off' : 'eye'" iconStyle="font-size: 18px;margin-left: 5px" text="可用余额" @click="handleEye"></u--text></view>
				<view style="font-size: 20px;font-weight: bold;margin: 16px 0;">{{ eyeShow == true ? '****' : available }}<text style="font-size: 12px;">元</text></view>
				<view style="margin-bottom: 16px;" @click="handleFreeze"><u--text suffixIcon="arrow-right" iconStyle="font-size: 18px" :text="'冻结余额 '+blockedBalances+' 元'"></u--text></view>
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
			<view style="padding: 10px;" v-if="indexList.length != 0">
				<u-list style="height: 100%;">
					<u-list-item v-for="(item, index) in indexList" :key="index">
						<view @click="handleClick(item.tradeTypeName, item)" style="line-height: 30px;">
							<u-row justify="space-between" customStyle="margin-bottom: 10px;border-bottom: 1px solid #f5f5f5;">
								<u-col span="8">
									<view class="title">{{ item.tradeTypeName }}</view>
									<view class="note">{{ item.createTime }}</view>
								</u-col>
								<u-col span="4">
									<view class="title" style="text-align: right;">
										<text v-if="item.profitLossTypeName == '收入'">+</text>
										<text v-if="item.profitLossTypeName == '支出'">-</text>
										{{ $amount.getComdify(item.payAmount / 100 || 0) }} >
									</view>
								</u-col>
							</u-row>
						</view>
					</u-list-item>
				</u-list>
			</view>
			<view v-else class="empty-page">
				<image class="empty-img" src="/static/images/index/noData.png" mode="widthFix"></image><br />
				<text class="empty-text" v-if="status">暂无数据</text>
			</view>
		</view>
	</view>
</template>

<script>
	import { getCarhList, getDetail } from '@/api/account/bond.js'
	export default {
		data() {
			return {
				eyeShow: false,
				// 可用余额
				available: 0,
				// 冻结余额
				blockedBalances: 0,
				indexList: [],
				revision: 0,
				status: false
			}
		},
		onBackPress(options) {
			this.$tab.switchTab('/pages/account/index');
			return true;
		},
		mounted() {
			this.$modal.loading("数据加载中，请耐心等待...");
			this.getBondDetail();
		},
		methods: {
			back() {
				uni.navigateBack({
					delta: 1
				})
			},
			// 是否隐藏金额
			handleEye() {
				this.eyeShow = !this.eyeShow;
			},
			// 查询我的保证金
			getBondDetail() {
				getDetail({ accountNo: this.$store.state.user.accountNo }).then((res) => {
					this.available = this.$amount.getComdify(res.data.availableCash / 100);
					this.blockedBalances = this.$amount.getComdify(res.data.freezeCash / 100);
					this.indexList = res.data.cashDetails;
					this.revision = res.data.revision;
					this.status = true;
					this.$modal.closeLoading();
				}).catch((error) => {
					this.status = true;
					this.$modal.closeLoading();
				})
			},
			// 提现
			handleWithdrawal() {
				this.$tab.navigateTo('/subPages/home/account/bond/withdrawal?amount=' + this.available + '&revision=' + this.revision);
			},
			// 充值
			handleRecharge() {
				this.$tab.navigateTo('/subPages/home/account/bond/recharge?revision=' + this.revision);
			},
			// 点击冻结余额
			handleFreeze() {
				this.$tab.navigateTo('/subPages/home/account/bond/freeze?amount=' + this.blockedBalances);
			},
			// 点击全部
			handleWhole() {
				this.$tab.navigateTo('/subPages/home/account/bond/whole');
			},
			// 点击保证金明细列表
			handleClick(val, data) {
				if (val == '保证金提现中') {
					// 保证金提现中
					this.$tab.navigateTo('/subPages/home/account/bond/progress?data='+encodeURIComponent(JSON.stringify(data)));
				} else if (val == '保证金提现') {
					// 保证金提现明细
					this.$tab.navigateTo('/subPages/home/account/bond/detailed?data='+encodeURIComponent(JSON.stringify(data)));
				} else if (val == '保证金回填') {
					// 保证金回填
					this.$tab.navigateTo('/subPages/home/account/bond/info?data='+encodeURIComponent(JSON.stringify(data)));
				} else if (val == '保证金预扣') {
					// 保证金预扣
					this.$tab.navigateTo('/subPages/home/account/bond/withhold?data='+encodeURIComponent(JSON.stringify(data)));
				} else if (val == '保证金充值') {
					// 保证金充值
					this.$tab.navigateTo('/subPages/home/account/bond/rechargeDetails?data='+encodeURIComponent(JSON.stringify(data)));
				} else if (val == '保证金预扣释放') {
					// 保证金预扣释放
					this.$tab.navigateTo('/subPages/home/account/bond/release?data='+encodeURIComponent(JSON.stringify(data)));
				} else if (val == '保证金实扣') {
					// 保证金实扣
					this.$tab.navigateTo('/subPages/home/account/bond/actualDeduction?data='+encodeURIComponent(JSON.stringify(data)));
				} else if (val == '保证金回填(利润)') {
					// 保证金回填(利润)
					this.$tab.navigateTo('/subPages/home/account/bond/info?data='+encodeURIComponent(JSON.stringify(data)));
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
			margin-top: 60px;
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
			transform: translate(-50%, -50%);
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
