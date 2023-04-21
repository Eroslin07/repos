<template>
	<view class="whole">
		<u-sticky bgColor="#fff">
			<view style="border-bottom: 1px solid #f5f5f5;">
				<u-tabs :list="list" lineColor="#FA6400" @change="handleChange"></u-tabs>
			</view>
			
			<view v-for="(item, index) in list" :key="index">
				<view v-if="tabCur === index" style="padding: 10px;">
					<view v-if="indexList[index].length != 0">
						<u-list style="height: 100%;">
							<u-list-item v-for="(item, tabIndex) in indexList[index]" :key="tabIndex">
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
												{{ $amount.getComdify(item.amount || 0) }} >
											</view>
										</u-col>
									</u-row>
								</view>
							</u-list-item>
						</u-list>
						
						<u-loadmore :status="status" loadingText="努力加载中..." />
					</view>
					<view v-else  class="empty-page">
						<image class="empty-img" src="/static/images/index/noData.png" mode="widthFix"></image><br />
						<text class="empty-text" v-if="status2">暂无数据</text>
					</view>
				</view>
			</view>
		</u-sticky>
	</view>
</template>

<script>
	import { getProfitList, getDetail } from '@/api/account/profit.js'
	export default {
		data() {
			return {
				// 商户账户号
				accountNo: '55555555',
				// accountNo: this.$store.state.user.accountNo,
				list: [{
					name: '全部',
				}, {
					name: '支出',
				}, {
					name: '收入'
				}],
				// 全部
				indexList: {
					'0': [],
					'1': [],
					'2': []
				},
				status: 'loadmore',
				timer: null,
				tabCur: 0,
				tab: {
					'0': {
						total: 0,
						pageNo: 1
					},
					'1': {
						total: 0,
						pageNo: 1
					},
					'2': {
						total: 0,
						pageNo: 1
					}
				},
				pageSize: 10,
				status2: false
			}
		},
		mounted() {
			this.getList(1);
		},
		// 下拉刷新
		onPullDownRefresh() {
			this.status2 = false;
			if (this.tabCur == 0) {
				// 全部
				this.getListRefresh(1);
			} else if (this.tabCur == 1) {
				// 支出
				this.getListRefresh(3);
			} else if (this.tabCur == 2) {
				// 收入
				this.getListRefresh(4);
			}
		},
		// 触底加载
		onReachBottom() {
			if (this.timer != null) {
				clearTimeout(this.timer)
			}
			if (this.indexList[this.tabCur].length == this.tab[this.tabCur].total) {
				this.status = 'nomore';
				return
			}
			this.status = 'loading';
			this.timer = setTimeout(() => {
				this.tab[this.tabCur].pageNo += 1;
				this.status2 = false;
				if (this.tabCur == 0) {
					// 全部
					this.getMore(1);
				} else if (this.tabCur == 1) {
					// 支出
					this.getMore(3);
				} else if (this.tabCur == 2) {
					// 收入
					this.getMore(4);
				}
			}, 1000)
		},
		methods: {
			handleChange(val) {
				this.tabCur = val.index;
				this.status2 = false;
				if (this.tabCur == 0) {
					// 全部
					this.getList(1);
				} else if (this.tabCur == 1) {
					// 支出
					this.getList(3);
				} else if (this.tabCur == 2) {
					// 收入
					this.getList(4);
				}
			},
			// 利润交易明细查询
			getList(i) {
				let data = {
					pageNo: this.tab[this.tabCur].pageNo,
					pageSize: this.pageSize,
					accountNo: this.accountNo,
					type: i
				}
				if (this.indexList[this.tabCur].length != 0) {
					return;
				}
				this.$modal.loading("数据加载中，请耐心等待...");
				getProfitList(data).then((res) => {
					this.indexList[this.tabCur] = res.data.list;
					this.tab[this.tabCur].total = res.data.total;
					if (this.tab[this.tabCur].total > 10) {
						this.status = 'loadmore'
					} else {
						this.status = 'nomore'
					}
					this.status2 = true;
					this.$modal.closeLoading();
				}).catch((error) => {
					this.status = 'nomore'
					this.status2 = true;
					this.$modal.closeLoading();
				})
			},
			getMore(i) {
				let data = {
					pageNo: this.tab[this.tabCur].pageNo,
					pageSize: this.pageSize,
					accountNo: this.accountNo,
					type: i
				}
				getProfitList(data).then((res) => {
					this.indexList[this.tabCur] = [...this.indexList[this.tabCur], ...res.data.list];
					this.tab[this.tabCur].total = res.data.total;
					if (this.tab[this.tabCur].total > this.indexList[this.tabCur].length) {
						this.status = 'loadmore'
					} else {
						this.status = 'nomore'
					}
					this.status2 = true;
				})
			},
			getListRefresh(i) {
				let data = {
					pageNo: this.tab[this.tabCur].pageNo,
					pageSize: this.pageSize,
					accountNo: this.accountNo,
					type: i
				}
				this.$modal.loading("数据加载中，请耐心等待...");
				getProfitList(data).then((res) => {
					this.indexList[this.tabCur] = res.data.list;
					this.tab[this.tabCur].total = res.data.total;
					if (this.tab[this.tabCur].total > 10) {
						this.status = 'loadmore'
					} else {
						this.status = 'nomore'
					}
					this.status2 = true;
					this.$modal.closeLoading();
					uni.stopPullDownRefresh();
				}).catch((error) => {
					this.status = 'nomore'
					this.status2 = true;
					this.$modal.closeLoading();
					uni.stopPullDownRefresh();
				})
			},
			handleClick(val, data) {
				getDetail({ accountNo: this.accountNo, profitId: data.id }).then((res) => {
					if (val == '利润提现中') {
						// 利润提现中
						this.$tab.navigateTo('/subPages/home/account/profit/progressDetile?data='+encodeURIComponent(JSON.stringify(res.data)));
					} else if (val == '10101002') {
						// 利润提现
						this.$tab.navigateTo('/subPages/home/account/profit/detailed?data='+encodeURIComponent(JSON.stringify(res.data)));
					} else if (val == '10101001') {
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
	.whole {
		border-top: 1px solid #f5f5f5;
		height: 100vh;
		
		position: relative;
		
		.empty-page {
			width: 100%;
			position: absolute;
			left: 50%;
			transform: translate(-50%, 50%);
			text-align: center;
		
			.empty-img {
				width: 30%;
			}
		}
	}
	
	/deep/ .u-tabs__wrapper__nav__item  {
		width: 33%;
	}
	
	.note {
		font-size: 12px;
		color: #a19d9d;
	}
</style>