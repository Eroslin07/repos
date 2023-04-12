<template>
	<view class="cost">
		<uni-card :is-shadow="false" is-full>
			<view class="">
				<view class="const-analy-header">
					<view>
						<zb-dropdown-menu>
							<zb-dropdown-item :name="dateTitle" :options="options" v-model="dateValue">
							</zb-dropdown-item>
						</zb-dropdown-menu>
					</view>
					<view>
						<text @click="analiesBtn">分析</text>
					</view>
				</view>

				<view class="charts-box">
					<qiun-data-charts type="column" :opts="opts" :chartData="chartData" />
				</view>
			</view>
			<u-search v-model="searchValue" :showAction="false" @search="search" @clear="clear"
				placeholder="请输入车架号(VIN)/品牌/收车费用/卖车费用">
			</u-search>
			<uni-card v-for="(tab, index) in tabList" :key="index" is-full style="margin-top: 10px;border-radius: 8px;"
				class="car-cost-list" @click="viewDetails(tab)">
				<view class="col3b fs13 mb5">VIN：LE4TG4DB1JL199517</view>
				<h3 class="car-title">宝马-宝马X12021款sDrive20Li时尚型</h3>
				<u-line dashed style="margin:10px 0"></u-line>
				<view class="disFlex fs13">
					<text class="col3b">车辆状态：</text>
					<text>{{tab.status}}</text>
				</view>
				<view class="disFlex fs13">
					<text class="col3b">收车费用：</text>
					<text>100,500元</text>
				</view>
				<view v-if="tab.status==='待售中已检测'" class="disFlex fs13">
					<text class="col3b">卖车金额：</text>
					<text>——</text>
				</view>
				<view v-if="tab.status==='已售车'" class="disFlex fs13">
					<text class="col3b">卖车金额：</text>
					<text>105,500元</text>
				</view>
				<view v-if="tab.status==='已售车'" class="disFlex fs13">
					<text class="col3b">平台扣减费用：</text>
					<text>105,500元</text>
				</view>
				<view v-if="tab.status==='已售车'" class="disFlex fs13">
					<text class="col3b">卖车利润：</text>
					<text>4,040元</text>
				</view>
				<u-line dashed style="margin:10px 0"></u-line>
				<view class="disFlex fs13">
					<text class="col3b">经办人：</text>
					<text>张三</text>
				</view>
			</uni-card>
		</uni-card>
	</view>
</template>

<script>
	import DropdownMenu from '../../subPages/home/carStatus/JP-dropdown-menu/JP-dropdown-menu.vue';
	import DropdownItem from '../../subPages/home/carStatus/JP-dropdown-menu/JP-dropdown-item.vue';
	export default {
		data() {
			return {
				// 日期
				startYear: 2023,
				endYear: new Date().getFullYear(),
				dateValue: '',
				dateTitle: '',
				options: [],
				// 搜索
				searchValue: "",
				// 列表
				tabList: [],
				// 图表
				chartData: {},
				opts: {
					color: ["#fa6400", "#0091ff", "#FAC858", "#EE6666", "#73C0DE", "#3CA272", "#FC8452", "#9A60B4",
						"#ea7ccc"
					],
					padding: [15, 15, 0, 5],
					enableScroll: false,
					legend: {},
					xAxis: {
						disableGrid: true
					},
					yAxis: {
						data: [{
							min: 0
						}]
					},
					extra: {
						column: {
							type: "group",
							width: 30,
							activeBgColor: "#000000",
							activeBgOpacity: 0.08
						}
					}
				}

			}
		},
		onLoad() {
			let currentMonth = new Date().getMonth() + 1;
			let currentQuarter = Math.floor((currentMonth % 3 == 0) ? (currentMonth / 3) : (currentMonth / 3 + 1));
			let currentValue = (String(this.endYear) + currentQuarter) * 1
			let quarterArr = ['第一季度', '第二季度', '第三季度', '第四季度']
			for (let i = this.startYear; i <= this.endYear; i++) {
				for (let q = 0; q < quarterArr.length; q++) {
					let obj = {
						text: i + quarterArr[q],
						value: (String(i) + (q + 1)) * 1
					}
					this.options.push(obj)
				}
			}
			this.dateValue = this.options.find(item => item.value == currentValue).value
			this.dateTitle = this.options.find(item => item.value == currentValue).text
		},
		created() {

		},
		onReady() {
			this.getServerData();
		},
		mounted() {
			this.tabList = [{
				status: '待售中已检测'
			}];
			for (let i = 0; i < 4; i++) {
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
						categories: ["1月", "2月", "3月"],
						series: [{
								name: "税费",
								data: [35, 36, 31]
							},
							{
								name: "服务费",
								data: [18, 27, 21]
							}
						]
					};
					this.chartData = JSON.parse(JSON.stringify(res));
				}, 500);
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
		align-items: center;
		font-size: 14px;
		color: #40ADDD;
	}

	.cost-content {
		border: 1px solid #ccc;
		border-radius: 3px;
		// padding: 10px;
		font-size: 14px;
		margin: 15px auto 10px;

		.cost-item {
			padding: 10px;
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

	.fs13 {
		font-size: 13px;
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

	.car-title {
		white-space: nowrap;
		overflow: hidden;
		text-overflow: ellipsis;
	}

	.margin-tb10 {
		margin: 10px 0;
	}

	.disFlex {
		display: flex;
		justify-content: space-between;
	}

	.col3b {
		color: #bbb;
	}

	.mb5 {
		margin-bottom: 5px;
	}

	/deep/ .zb-dropdown-menu__bar {
		box-shadow: none;
	}

	/deep/.zb-dropdown-menu__title::after {
		border-width: 5px;
		border-bottom-color: #aaa;
		border-left-color: #aaa;
		margin-top: -8px;
		right: -10px;
		opacity: 0.9
	}

	/deep/.zb-dropdown-menu__title--down::after {
		margin-top: -3px;
	}
</style>
