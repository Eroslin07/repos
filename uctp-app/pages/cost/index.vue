<template>
	<view class="cost">
		<uni-card :is-shadow="false" is-full>
			<view class="">
				<view class="const-analy-header">
					<view>
						<u-datetime-picker :show="dateShow" loading :closeOnClickOverlay="false" v-model="dateValue"
							mode="year-month" @confirm="dateConfirm" @cancel="dateShow=false">
						</u-datetime-picker>
						<view class="date-box">
							<view>{{dateValue}}</view>
							<u-icon @click="dateShow=true" name="arrow-down" color="#40ADDD" class="date-picker"
								size="16">
							</u-icon>
						</view>
					</view>
					<view>
						<text @click="analiesBtn">分析</text>
					</view>
				</view>

				<view class="cost-content">
					<u-tabs :list="listArr" @click="tabClick"></u-tabs>
					<view v-if="tabValue=='支出'" class="cost-item">
						<!-- <view class="">
							支出
						</view> -->
						<view class="charts-box">
							<qiun-data-charts type="pie" :opts="opts" :chartData="chartData" />
						</view>
					</view>
					<view v-if="tabValue=='收入'" class="cost-item">
						<!-- <view class="">
							收入
						</view> -->
						<view class="charts-box">
							<qiun-data-charts type="pie" :opts="opts" :chartData="chartData" />
						</view>
					</view>
					<!-- <view class="charts-box">
						<qiun-data-charts type="pie" :opts="opts" :chartData="chartData" />
					</view> -->
				</view>
			</view>
			<u-search v-model="searchValue" :showAction="false" @search="search" @clear="clear" placeholder="请选择车辆">
			</u-search>
			<uni-card v-for="(tab, index) in tabList" :key="index" is-full style="margin-top: 10px;"
				class="car-cost-list" @click="viewDetails(tab)">
				<view>VIN：LE4TG4DB1JL199517</view>
				<h3>宝马-宝马X12021款sDrive20Li时尚型</h3>
				<view class="col40AD">
					车辆状态：{{tab.status}}
				</view>
				<h4>
					收车费用：100,500元
				</h4>
				<h4>
					实际产生费用合计：960元
				</h4>
				<view v-if="tab.status==='已售车'">
					<h4>
						卖车金额：105,500元
					</h4>
					<h4>
						<text>卖车利润：4,040元</text>
						<text class="floatR col40AD">经办人：张三</text>
					</h4>
				</view>
				<view v-else>
					<h4>
						预计将产生费用：560元
					</h4>
					<h4>
						<text>卖车金额：——元</text>
						<text>经办人：张三</text>
					</h4>
				</view>
			</uni-card>
		</uni-card>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				//日期
				dateValue: uni.$u.timeFormat(Number(new Date()), 'yyyy-mm'),
				dateShow: false,
				searchValue: "",
				tabList: [],

				chartData: {},
				opts: {
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
							borderColor: "#FFFFFF"
						}
					}
				},
				listArr: [{
					name: '支出',
				}, {
					name: '收入',
				}],
				tabValue:'支出',
			}
		},
		created() {

		},
		onReady() {
			this.getServerData();
		},
		mounted() {
			this.tabList = [];
			for (let i = 0; i < 5; i++) {
				this.tabList.push({
					status: '已售车'
				})
			}
		},
		filters: {
			moneyFormat(val) {
				if (val.toString().length > 5) {
					return (val / 10000).toFixed(2) + '万元'
				} else if (val.toString().length > 8) {
					return (val / 100000000).toFixed(2) + '亿元'
				} else {
					return val + '元'
				}

			}
		},
		methods: {
			// 日期确认
			dateConfirm(val) {
				let {
					value
				} = val;

				this.$nextTick(() => {
					this.dateValue = uni.$u.timeFormat(value, 'yyyy-mm')
				})
				this.dateShow = false;
			},
			// 分析
			analiesBtn() {
				uni.navigateTo({
					url: `/subPages/home/cost/myExpenseAnalysis/myExpenseAnalysis`
				})
			},
			back() {
				uni.navigateBack({
					delta: 1
				})
			},
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

			// 图表数据
			getServerData() {
				//模拟从服务器获取数据时的延时
				setTimeout(() => {
					//模拟服务器返回数据，如果数据格式和标准格式不同，需自行按下面的格式拼接
					let res = {
						series: [{
							data: [{
								"name": "已实际产生费用",
								"value": 50
							}, {
								"name": "预计将产生费用",
								"value": 30
							}]
						}]
					};
					this.chartData = JSON.parse(JSON.stringify(res));
				}, 500);
			},
			// 切换图表
			tabClick(item) {
				console.log(item)
				this.tabValue = item.name
			},

			//查看车辆明细
			viewDetails(item) {
				uni.navigateTo({
					url: `/subPages/common/vehicleDetails/vehicleDetails?item=` + JSON.stringify(item)
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.const-analy-header {
		display: flex;
		justify-content: space-between;
		font-size: 14px;
		color: #40ADDD;
	}

	.cost-content {
		border: 1px solid #ccc;
		border-radius: 3px;
		// padding: 10px;
		font-size: 14px;
		margin: 15px auto 10px;
		.cost-item{
			padding:10px;
		}
	}

	.date-box {
		display: flex;
		color: #40ADDD;

		.date-picker {
			margin-left: 5px;
		}
	}

	.fs14 {
		font-size: 14px;
	}

	.col40AD {
		color: #40ADDD;
	}

	.fweight {
		font-weight: bold;
	}

	.floatR {
		float: right;
	}

	.charts-box {
		width: 100%;
		height: 249px;
	}
</style>
