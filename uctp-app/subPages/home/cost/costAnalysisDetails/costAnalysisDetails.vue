<template>
	<view>
		<uni-card :is-shadow="false" is-full>
			<view class="charts-box">
				<qiun-data-charts type="line" :opts="opts" :chartData="chartData" />
			</view>
			
			
			<view class="cost-detail">
				<view class="cost-title col40AD" >
					<text>收车费用 101100元</text>
					<text>3月共2笔</text>
				</view>
				<view class="cost-content" v-for="(item, index) in 2" :key="index">
					<view class="">
						<view>收车支出</view>
						<text class="col3d">张三</text>
					</view>
					<view class="">
						<view class="">
							-50000元
						</view>
						<text class="col3d">2023-3-27 17:17：20</text>
					</view>
				</view>
			</view>
		</uni-card>
	</view>
</template>

<script>
	export default {
		data() {
			return {
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
				}
			}
		},
		onReady() {
			this.getServerData();
		},
		methods: {
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
		}
	}
</script>

<style lang="scss" scoped>
	.charts-box {
		width: 100%;
		height: 200px;
		margin: 10px auto;
	}
	.col40AD {
		color: #40ADDD;
	}
	.col3d{
		color:#A9A6A6;
	}
	.cost-detail{
		.cost-title{
			display: flex;
			justify-content: space-between;
		}
		.cost-content{
			border-bottom:1px solid #ddd;
			display:flex;
			justify-content: space-between;
			margin-top:20px;
			text-align: center;
		}
	}
</style>
