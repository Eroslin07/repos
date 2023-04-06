<template>
	<view class="status-container">
		<u-search class="search" v-model="searchValue" :showAction="false" @search="search" @clear="clear"
			placeholder="请输入商户/车辆型号/单号">
		</u-search>

		<!-- 下拉框 -->
		<dropdown-menu>
			<dropdown-item title="品牌" ref="rangeDropdown">
				<view class="cellDraft" @click="selectBtn('宝马')">宝马</view>
			</dropdown-item>
			<dropdown-item title=" 收车状态" ref="dateDropdown">
				<view v-for="item in newCarStatus" :key="item.value" class="cellDraft" @click="selectBtn(item.label)">
					{{item.label}}
				</view>
			</dropdown-item>
		</dropdown-menu>

		<!-- 筛选项 -->
		<view class="tag-box">

			<text class="tag-item" v-for="(item,index) in 2" :key="index">
				宝马
				<uni-icons style="padding-left:3px" color="#ccc" type="closeempty" size="14"></uni-icons>
			</text>
			<view class="" style="display: inline-block;">
				<!-- <uni-icons style="padding-left:3px" color="#ccc" type="trash" size="14"></uni-icons> -->
				<u--text style="font-size:12px;" prefixIcon="trash" iconStyle="font-size: 16px" text="清空"></u--text>
			</view>
		</view>

		<!-- 列表 -->
		<view>
			<block>
				<uni-card v-for="(tab, tabIndex) in tabList" :key="tabIndex" style="margin-top: 10px;">
					<uni-row :gutter="30">
						<uni-col :span="8">
							<view class="car_left">
								<view class="car_text cell-car-draft">收车草稿</view>
								<view style="height: 100px;border: 1px solid #eee;"></view>
							</view>
						</uni-col>
						<uni-col :span="16">
							<h3>宝马-宝马×12021款 sDrive20Li 时尚型</h3>
							<view>2021年02月 | 2.9万公里</view>
							<view style="color: #000;">收车价：151,300元</view>
							<view>卖车价：<text style="color: #fa6400;font-weight:bold">200,000元</text></view>
						</uni-col>
						<uni-col :span="12">
							<view style="font-size: 10px;">VIN: LE4TG4DB1JL199517</view>
						</uni-col>
						<uni-col :span="12">
							<view style="font-size: 10px;">创建时间:2023-03-1514:10</view>
						</uni-col>
					</uni-row>
				</uni-card>
			</block>
		</view>
	</view>
</template>


<script>
	import DropdownMenu from '@/components/JP-dropdown-menu/JP-dropdown-menu.vue';
	import DropdownItem from '@/components/JP-dropdown-menu/JP-dropdown-item.vue';
	export default {
		data() {
			return {
				searchValue: '',
				value: null,
				collectCarState: [{
						label: '收车草稿',
						value: '收车草稿'
					},
					{
						label: '收车委托已发起',
						value: '收车委托已发起'
					},
					{
						label: '收车合同已发起',
						value: '收车合同已发起'
					},
					{
						label: '收车支付失败',
						value: '收车支付失败'
					},
					{
						label: '收车退回草稿',
						value: '收车退回草稿'
					},
				],
				tabList: [],
				carStatsus: null,
			}
		},
		components: {
			DropdownMenu,
			DropdownItem
		},
		computed: {
			newCarStatus() {
				if (this.carStatsus == '收车中') {
					return this.collectCarState
				} else if (this.carStatsus == '待售中') {
					return [{
							label: '待售未检测',
							value: '待售未检测'
						},
						{
							label: '待售已检测',
							value: '待售已检测'
						},
					]
				} else if (this.carStatsus == '卖车中') {
					return [{
						label: '卖车草稿',
						value: '卖车草稿'
					}, {
						label: '卖车委托已发起',
						value: '卖车委托已发起'
					}, {
						label: '卖车合同已发起',
						value: '卖车合同已发起'
					}, {
						label: '卖车待付款',
						value: '卖车待付款'
					}, {
						label: '卖车退回草稿',
						value: '卖车退回草稿'
					}]
				} else {
					return [{
						label: '已售出',
						value: '已售出'
					}]
				}
			}
		},
		mounted() {
			this.tabList = [];
			for (let i = 0; i < 10; i++) {
				this.tabList.push({})
			}
		},
		onLoad(props) {
			switch (props.text) {
				case '1':
					this.carStatsus = '收车中'
					break;
				case '2':
					this.carStatsus = '待售中'
					console.log(this.carStatsus, 'status')
					break;
				case '3':
					this.carStatsus = '卖车中'
					break;
				case '4':
					this.carStatsus = '已售出'
					break;
			}
			uni.setNavigationBarTitle({
				title: this.carStatsus,
			})
			// this.carStatsus = props.text
		},
		methods: {
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

			selectBtn(text) {
				console.log(text)
				setTimeout(() => {
					this.$refs.dateDropdown.close()
					this.$refs.rangeDropdown.close()
				}, 100)
			}
		}
	}
</script>

<style lang="scss" scoped>
	.status-container {
		width: 100%;
		height: 100%;

		.search {
			padding: 10px 15px;
		}
	}

	.cellDraft {
		padding: 10px;
		border-bottom: 1px solid #eee;
		font-size: 12px;
	}

	.cellDraft:hover {
		background-color: #ccc;
	}

	.tag-box {
		width: 100vw;
		height: 50px;
		line-height: 50px;
		// padding: 0 15px;
		margin: 0 15px;
		margin-right: 15px;
		overflow-x: scroll;

		.tag-item {
			padding: 3px 5px;
			margin-right: 5px;
			border: 1px solid #ccc;
			border-radius: 5px;
		}
	}

	.car_left {
		position: relative;
		border-radius: 8px;
		overflow: hidden;

		.car_text {
			position: absolute;
			top: 0;
			left: 0;
			font-size: 12px;
			padding: 0 5px;
			border-radius: 3px;
		}
	}

	.cell-car-draft {
		color: #fff;
		background-image: linear-gradient(to right, #2A93EC, #88C4F4);
	}

	.car-sold {
		color: #fff;
		background-image: linear-gradient(to right, #1D9A6D, #A2EED3);
	}

	.car-sell-entrust {
		color: #fff;
		background-image: linear-gradient(to right, #DB6A43, #F2C9BB);
	}

	.car-unsold-untested {
		color: #fff;
		background-image: linear-gradient(to right, #C07F1D, #F4DDB9);
	}
</style>
