<template>
	<view class="analys">
		<uni-card :is-shadow="false" is-full height="100vh">
			<view class="">
				<view>
					<view class="displayF col40AD">
						<view>
							<u-picker :show="typeShow" :closeOnClickOverlay="false" :columns="columns"
								@confirm="confirmBtn" @cancel="typeShow=false"></u-picker>
							<view class="displayF">
								<text>{{typeValue}}</text>
								<u-icon @click="typePicker" name="arrow-down" color="#40ADDD" class="date-picker"
									size="16">
								</u-icon>
							</view>
						</view>

						<view class="ml10">
							<view v-if="typeValue=='年度'" class="displayF">
								<text>{{ yearDate }}</text>
								<picker mode="date" :value="yearDate" fields="year" :start="startDate" :end="endDate"
									@change="yearChange">
									<u-icon name="arrow-down" style="padding-top:2px;" color="#40ADDD"
										class="date-picker" size="16">
									</u-icon>
								</picker>
							</view>
							<view v-else class="displayF">
								<u-datetime-picker :show="dateShow" loading :closeOnClickOverlay="false"
									v-model="dateValue" mode="year-month" @confirm="dateConfirm"
									@cancel="dateShow=false">
								</u-datetime-picker>
								<text>{{dateValue}}</text>
								<u-icon name="arrow-down" style="padding-top:2px;" @click="dateShow=true"
									color="#40ADDD" class="date-picker" size="16">
								</u-icon>
							</view>
						</view>
					</view>

					<view class="tab-box">
						<u-tabs :list="costList" lineWidth="0" :activeStyle="{
						            backgroundColor:'#40ADDD',
									color:'#fff'
						        }" itemStyle="padding:0" @click="tabClick">
						</u-tabs>
					</view>
					<view v-if="costValue==='支出'" class="charts-box">
						<text>1</text>
						<qiun-data-charts type="line" :opts="opts" :chartData="chartData" />
					</view>
					<view v-if="costValue==='收入'" class="charts-box">
						<text>2</text>
						<qiun-data-charts type="line" :opts="opts" :chartData="chartData" />
					</view>
				</view>
			</view>
			<view class="">
				<view class="col40AD">
					分类
				</view>
				<view class="process-container" @click="checkDetail('收车费用')">
					<text>收车费用</text>
					<view class="process-box">
						<u-line-progress style="width:80%" :percentage="30" activeColor="#40ADDD" height="8"
							:showText="false"></u-line-progress>
						<view class="process-text">
							<text>2笔</text>
							<text>100500元</text>
						</view>
					</view>
				</view>
				<view class="process-container" @click="checkDetail('检测服务费')">
					<text>检测服务费</text>
					<view class="process-box">
						<u-line-progress style="width:80%" :percentage="30" activeColor="#40ADDD" height="8"
							:showText="false"></u-line-progress>
						<view class="process-text">
							<text>2笔</text>
							<text>100500元</text>
						</view>
					</view>
				</view>
				<view class="process-container" @click="checkDetail('过户服务费')">
					<text>过户服务费</text>
					<view class="process-box">
						<u-line-progress style="width:80%" :percentage="30" activeColor="#40ADDD" height="8"
							:showText="false"></u-line-progress>
						<view class="process-text">
							<text>2笔</text>
							<text>100500元</text>
						</view>
					</view>
				</view>
			</view>

		</uni-card>
	</view>
</template>

<script>
	function getDate(type) {
		const date = new Date();
		let year = date.getFullYear();
		if (type === 'start') {
			year = year - 10;
		} else if (type === 'end') {
			year = year + 10;
		}
		return `${year}`;
	};
	export default {
		data() {
			return {
				typeShow: false,
				columns: [
					['年度', '月度']
				],
				typeValue: '年度',

				dateShow: false,
				dateValue: uni.$u.timeFormat(Number(new Date()), 'yyyy-mm'),

				yearDate: getDate({
					format: true
				}),
				startDate: getDate('start'),
				endDate: getDate('end'),

				chartData: {},
				opts: {
					color: ["#1890FF", "#91CB74", "#FAC858", "#EE6666", "#73C0DE", "#3CA272", "#FC8452", "#9A60B4",
						"#ea7ccc"
					],
					padding: [15, 10, 0, 15],
					enableScroll: false,
					legend: {},
					xAxis: {
						disableGrid: false,
					},
					yAxis: {
						disableGrid: true,
						gridType: "dash",
						dashLength: 1
					},
					extra: {
						line: {
							type: "curve",
							width: 2,
							activeType: "hollow"
						}
					}
				},
				costList: [{
					name: '支出',
				}, {
					name: '收入'
				}],
				costValue: '支出',
			}
		},
		onReady() {
			this.getServerData();
		},
		methods: {
			typePicker() {
				this.typeShow = true;
			},

			// 确认
			confirmBtn(e) {
				this.typeValue = e.value[0];
				this.typeShow = false;
			},

			// 年月确认
			dateConfirm(val) {
				let {
					value
				} = val;

				this.$nextTick(() => {
					this.dateValue = uni.$u.timeFormat(value, 'yyyy-mm')
				})
				this.dateShow = false;
			},
			//change年份
			yearChange(e) {
				this.yearDate = e.detail.value
			},

			//获取图表数据
			getServerData() {
				//模拟从服务器获取数据时的延时
				setTimeout(() => {
					//模拟服务器返回数据，如果数据格式和标准格式不同，需自行按下面的格式拼接
					let res = {
						categories: ["11", "12", "1", "3", "3", "4"],
						series: [{
							name: "汇总",
							lineType: "",
							data: [10, 8, 9, 8, 7, 11]
						}, ]
					};
					this.chartData = JSON.parse(JSON.stringify(res));
				}, 500);
			},
			// 切换图表
			tabClick(item) {
				this.costValue = item.name;
			},
			checkDetail(text) {
				console.log(text)
				uni.navigateTo({
					url: `/subPages/home/cost/costAnalysisDetails/costAnalysisDetails`
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.date-picker {
		margin-left: 5px;
	}

	.displayF {
		display: flex;
	}

	.ml10 {
		margin-left: 10px;
	}

	.col40AD {
		color: #40ADDD;
	}

	.tab-box {
		float: right;
		margin: 5px 0;
	}

	.charts-box {
		width: 100%;
		height: 200px;
		margin: 10px auto;
	}

	.process-container {
		margin-top: 10px;
		font-size: 13px;

		.process-box {
			position: relative;
			margin-top: 6px;

			.process-text {
				position: absolute;
				right: 0;
				bottom: -10px;

				text {
					display: block;
					text-align: center;
				}
			}
		}

	}

	/deep/ .u-tabs__wrapper__nav__item__text span {
		padding: 3px 8px;
	}
</style>
