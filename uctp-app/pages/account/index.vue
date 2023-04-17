<template>
	<view class="account">
		<view style="position: relative;">
			<view class="cost_image"></view>
			<view class="notice">
				<u-notice-bar :text="text" bgColor="#f8c089" color="#fa6805"></u-notice-bar>
			</view>
			<view class="statistics">
				<view>资产总额</view>
				<view style="font-size: 20px;">20.73万元</view>
			</view>
		</view>
		
		<uni-card>
			<u-grid col="2" :border="true">
				<u-grid-item>
					<view @click="handleClick(1)" style="text-align: center;">
						<view>保证金 ></view>
						<view>20.05万元</view>
					</view>
				</u-grid-item>
				<u-grid-item>
					<view @click="handleClick(2)" style="text-align: center;">
						<view>利润 ></view>
						<view>6,800.00元</view>
					</view>
				</u-grid-item>
			</u-grid>
		</uni-card>
		
		<uni-card>
			<view class="charts-box">
				<qiun-data-charts type="pie" :opts="opts" :chartData="chartData" />
			</view>
		</uni-card>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				text: '资金受兴业银行监管、保证资金账户7×24小时充值/提现',
				
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
						// lineHeight: 20,
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
		onReady() {
			this.getServerData();
		},
		methods: {
			// 图表数据
			getServerData() {
				//模拟从服务器获取数据时的延时
				setTimeout(() => {
					//模拟服务器返回数据，如果数据格式和标准格式不同，需自行按下面的格式拼接
					let res = {
						series: [{
							data: [{
								"name": "利润",
								"value": 50
							}, {
								"name": "保证金",
								"value": 30
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
			height: 180px;
			background-image: url('../../static/images/cost/cost.png');
			background-repeat: no-repeat;
			background-size: 100% 100%;
		}
		
		.notice {
			position: absolute;
			top: 0;
		}
		
		.statistics {
			text-align: center;
			color: #fff;
			font-size: 16px;
			position: absolute;
			left: 50%;
			top: 50%;
			transform: translate(-50%, -100%);
		}
		
		.charts-box {
			width: 100%;
			height: 249px;
		}
	}
</style>