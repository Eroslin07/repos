<template>
	<view class="status-container">
		<u-search class="search" v-model="formData.searchValue" :showAction="false" @search="search" @clear="clear"
			placeholder="请输入商户/车辆型号/单号">
		</u-search>
		<zb-dropdown-menu style="width: 100%">
			<zb-dropdown-item name="品牌" :options="brandArr" v-model="formData.brand" @change="brandChange">
			</zb-dropdown-item>
			<zb-dropdown-item name="收车状态" :options="newCarStatus" v-model="formData.checkStatus" @change="changeValue">
			</zb-dropdown-item>
		</zb-dropdown-menu>

		<u-datetime-picker ref="datetimePicker" :show="timeShow" v-model="saleTime" mode="year-month"
			@cancel="timeCancle" @confirm="timeConfirm">
		</u-datetime-picker>
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

				<u-loadmore :status="status" loadingText="努力加载中..." />
			</block>
		</view>
	</view>
</template>


<script>
	import DropdownMenu from './JP-dropdown-menu/JP-dropdown-menu.vue';
	import DropdownItem from './JP-dropdown-menu/JP-dropdown-item.vue';
	import {
		getHomePageList
	} from '@/api/home.js'
	export default {
		data() {
			return {
				// searchValue: '',
				value: null,
				collectCarState: [{
						text: '收车状态',
						value: ''
					}, {
						text: '收车草稿',
						value: 11
					},
					{
						text: '收车委托已发起',
						value: 12
					},
					{
						text: '收车合同已发起',
						value: 13
					},
					{
						text: '收车支付失败',
						value: 14
					},
					{
						text: '收车退回草稿',
						value: 15
					},
				],
				tabList: [],
				carStatsus: null,
				formData: {
					searchValue: null,
					"pageNo": 1,
					"pageSize": 10,
					brand: '',
					salesStatus: null,
					checkStatus: '',
				},
				status: 'loadmore',
				total: 0,
				timer: {},

				brandArr: [{
						text: '品牌',
						value: ''
					},
					{
						text: '宝马',
						value: 1
					},
					{
						text: '奥迪',
						value: 2
					}
				],

				saleTime: uni.$u.timeFormat(Number(new Date()), 'yyyy-mm'),
				timeShow: false,

				statusNum: null
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
							text: '收车状态',
							value: ''
						}, {
							text: '待售未检测',
							value: 21
						},
						{
							text: '待售已检测',
							value: 22
						},
					]
				} else if (this.carStatsus == '卖车中') {
					return [{
							text: '收车状态',
							value: ''
						},
						{
							text: '卖车草稿',
							value: 31
						}, {
							text: '卖车委托已发起',
							value: 32
						}, {
							text: '卖车合同已发起',
							value: 33
						}, {
							text: '卖车待付款',
							value: 34
						}, {
							text: '卖车退回草稿',
							value: 35
						}
					]
				} else {
					return [{
							text: '收车状态',
							value: ''
						},
						{
							text: '销售时间',
							value: '销售时间'
						}
					]
				}
			}
		},
		mounted() {
			this.getList(this.formData)
		},
		onLoad(props) {
			switch (props.text) {
				case 1:
					this.carStatsus = '收车中'
					break;
				case 2:
					console.log(props.text)
					this.carStatsus = '待售中'
					break;
				case 3:
					this.carStatsus = '卖车中'
					break;
				case 4:
					this.carStatsus = '已售出'
					break;
			}
			uni.setNavigationBarTitle({
				title: this.carStatsus,
			})
			this.formData.salesStatus = props.text
			this.statusNum = props.text
		},
		onPullDownRefresh() {
			if (this.timer != null) {
				clearTimeout(this.timer)
			}
			if (this.tabList.length == this.total) {
				this.status = 'nomore';
				return
			}
			this.status = 'loading';
			this.timer = setTimeout(() => {
				this.formData.pageNo += 1
				this.getMore(this.formData)
			}, 1000)
		},
		methods: {
			// 获取list数据
			getList(params) {
				getHomePageList(params).then(res => {
					this.tabList = res.data.list;
					this.total = res.data.total;
					if (this.total > 10) {
						this.status = 'loadmore'
					} else {
						this.status = 'nomore'
					}
				}).catch((error) => {
					this.status = 'nomore'
				})
			},
			getMore(params) {
				getHomePageList(params).then(res => {
					this.tabList = [...this.tabList, ...res.data.list];
					this.total = res.data.total;
					if (this.total > this.tabList.length) {
						this.status = 'loadmore'
					} else {
						this.status = 'nomore'
					}
				})
			},
			
			// 搜索
			search(val) {
				uni.showToast({
					title: '搜索：' + val,
					icon: 'none'
				})
				this.getList(this.formData)
			},
			// 清除
			clear(val) {
				uni.showToast({
					title: '清除：' + val,
					icon: 'none'
				})
				this.getList(this.formData)
			},
			// 品牌 
			brandChange(val) {
				this.getList(this.formData)
			},
			
			// 销售时间
			changeValue(val) {
				if (val.text == '销售时间') {
					this.timeShow = true;
					this.formData.checkStatus=''
				}
			},

			// 时间 取消
			timeCancle() {
				this.timeShow = false
				this.formData.checkStatus=''
			},
			// 时间 确认
			timeConfirm() {
				this.timeShow = false
				this.formData.checkStatus=''
				this.$nextTick(() => {
					uni.$u.timeFormat(this.saleTime, 'yyyy-mm'),
						console.log(uni.$u.timeFormat(this.saleTime, 'yyyy-mm'), 333333)
				})

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
