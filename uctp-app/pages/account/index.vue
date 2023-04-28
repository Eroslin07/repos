<template>
	<view class="account">
		<!-- 自定义导航栏 -->
		<u-navbar title="资金管理" bgColor="rgba(0, 0, 0, 0)" :titleStyle="{'color': '#fff'}" safeAreaInsetTop fixed>
			<view class="u-nav-slot" slot="left">
				<view class="bank-logo"></view>
			</view>
		</u-navbar>
		
		<view style="position: relative;">
			<view class="cost_image"></view>
			<view class="notice">
				<view class="zijin">
					<view style="float: left;margin-right: 5px;padding-top: 2px;">
						<u-icon name="error-circle" color="#FA6400"></u-icon>
					</view>
					<view>{{ text }}</view>
				</view>
			</view>
			<view class="statistics">
				<view style="margin-bottom: 10px;">资产总额</view>
				<view style="font-size: 24px;">{{ $amount.getComdify((data.cash + data.profit) / 100 || 0) }}<text style="font-size: 14px;">元</text></view>
			</view>
		</view>
		
		<view style="margin-top: -75px;">
			<uni-card>
				<u-grid col="2" :border="true">
					<u-grid-item>
						<view @click="handleClick(1)" style="text-align: center;">
							<view>保证金 ></view>
							<view>{{ $amount.getComdify(data.cash / 100 || 0) }}元</view>
						</view>
					</u-grid-item>
					<u-grid-item>
						<view @click="handleClick(2)" style="text-align: center;">
							<view>利润 ></view>
							<view>{{ $amount.getComdify(data.profit / 100 || 0) }}元</view>
						</view>
					</u-grid-item>
				</u-grid>
			</uni-card>
		</view>
		
		<uni-card>
			<view class="charts-box">
				<qiun-data-charts type="pie" :opts="opts" :chartData="chartData" />
			</view>
		</uni-card>
		<!-- 自定义tabbar -->
		<tab-bar :name="2" :type="type"></tab-bar>
	</view>
</template>

<script>
	import { getAccount } from '@/api/account/index.js'
	export default {
		data() {
			return {
				type: 0,
				text: '资金受浦发银行监管、保证资金账户7×24小时充值/提现',
				data: {},
				chartData: {},
				opts: {
					title: {
						name: '1'
					},
					color: ["#1890FF", "#91CB74", "#FAC858", "#EE6666", "#73C0DE", "#3CA272", "#FC8452", "#9A60B4",
						"#ea7ccc"
					],
					width: '50px',
					height: '50px',
					padding: [5, 5, 5, 5],
					enableScroll: false,
					legend: {
						float: "left",
					},
					extra: {
						pie: {
							activeOpacity: 0.5,
							activeRadius: 10,
							offsetAngle: 0,
							labelWidth: 15,
							border: true,
							borderWidth: 3,
							borderColor: "#FFFFFF",
							linearType: "custom"
						}
					}
				},
			}
		},
		onLoad() {
			this.type = this.$store.state.user.staffType;
		},
		// 下拉刷新
		onPullDownRefresh() {
			this.getList();
		},
		mounted() {
			this.getList();
		},
		methods: {
			getList() {
				getAccount().then((res) => {
					this.data = res.data;
					uni.stopPullDownRefresh();
					this.getServerData();
				})
			},
			// 图表数据
			getServerData() {
				setTimeout(() => {
					let res = {
						series: [{
							data: [{
								"name": "利润",
								"value": this.data.profit / 100
							}, {
								"name": "保证金",
								"value": this.data.cash / 100
							}]
						}]
					};
					this.chartData = JSON.parse(JSON.stringify(res));
				}, 500);
			},
			handleClick(val) {
				if (val === 1) {
					this.$tab.navigateTo('/subPages/home/account/bond/bond');
				} else if (val === 2) {
					this.$tab.navigateTo('/subPages/home/account/profit/profit');
				}
			}
		}
	}
</script>

<style lang="scss" scoped>
	.account {
		.cost_image {
			width: 100%;
			height: 220pt;
			background-image: url('../../static/images/cost/cost.png');
			background-repeat: no-repeat;
			background-size: 100% 100%;
		}
		
		.notice {
			position: absolute;
			top: 44px;
			// #ifdef MP-WEIXIN
			top: 88px;
			// #endif
			width: 100%;
			
			.zijin {
				padding: 5px;
				color: #fa6805;
				background-color: #f8c089;
				overflow: hidden;
			}
		}
		
		.statistics {
			text-align: center;
			color: #fff;
			font-size: 16px;
			position: absolute;
			left: 50%;
			top: 60%;
			transform: translate(-50%, -100%);
			// #ifdef MP-WEIXIN
			transform: translate(-50%, -60%);
			// #endif
		}
		
		.charts-box {
			width: 100%;
			height: 249px;
		}
	}
</style>