<template>
	<view class="whole">
		<u-sticky bgColor="#fff">
			<view style="border-bottom: 1px solid #f5f5f5;display: flex;width: 100%">
				<u-tabs :list="list" lineColor="#FA6400" @change="handleChange"></u-tabs>
			</view>
		</u-sticky>
		<view v-for="(item, index) in list" :key="index">
			<view v-if="tabCur === index" style="padding: 10px;">
				<view v-if="indexList[index].length != 0">
					<u-list style="height: 100%;">
						<u-list-item v-for="(item, tabIndex) in indexList[index]" :key="tabIndex">
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
					
					<u-loadmore :status="status[index]" loadingText="努力加载中..." />
				</view>
				<view v-else  class="empty-page">
					<image class="empty-img" src="@/subPages/static/images/error/noData.png" mode="widthFix"></image><br />
					<text class="empty-text" v-if="status2">暂无数据</text>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import { getCarhList } from '@/api/account/bond.js'
	export default {
		data() {
			return {
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
				status: {
					'0': 'loadmore',
					'1': 'loadmore',
					'2': 'loadmore'
				},
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
			this.tab[this.tabCur].pageNo = 1;
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
				this.status[this.tabCur] = 'nomore';
				return
			}
			this.status[this.tabCur] = 'loading';
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
			// 保证金交易明细查询
			getList(i) {
				let data = {
					pageNo: this.tab[this.tabCur].pageNo,
					pageSize: this.pageSize,
					accountNo: this.$store.state.user.accountNo,
					type: i
				}
				if (this.indexList[this.tabCur].length != 0) {
					return;
				}
				this.$modal.loading("数据加载中，请耐心等待...");
				getCarhList(data).then((res) => {
					this.indexList[this.tabCur] = res.data.list;
					this.tab[this.tabCur].total = res.data.total;
					if (this.tab[this.tabCur].total > 10) {
						this.status[this.tabCur] = 'loadmore'
					} else {
						this.status[this.tabCur] = 'nomore'
					}
					this.status2 = true;
					this.$modal.closeLoading2();
				}).catch((error) => {
					this.status[this.tabCur] = 'nomore'
					this.status2 = true;
					this.$modal.closeLoading();
				})
			},
			getMore(i) {
				let data = {
					pageNo: this.tab[this.tabCur].pageNo,
					pageSize: this.pageSize,
					accountNo: this.$store.state.user.accountNo,
					type: i
				}
				getCarhList(data).then((res) => {
					this.indexList[this.tabCur] = [...this.indexList[this.tabCur], ...res.data.list];
					this.tab[this.tabCur].total = res.data.total;
					if (this.tab[this.tabCur].total > this.indexList[this.tabCur].length) {
						this.status[this.tabCur] = 'loadmore'
					} else {
						this.status[this.tabCur] = 'nomore'
					}
					this.status2 = true;
				})
			},
			getListRefresh(i) {
				let data = {
					pageNo: this.tab[this.tabCur].pageNo,
					pageSize: this.pageSize,
					accountNo: this.$store.state.user.accountNo,
					type: i
				}
				this.$modal.loading("数据加载中，请耐心等待...");
				getCarhList(data).then((res) => {
					this.indexList[this.tabCur] = res.data.list;
					this.tab[this.tabCur].total = res.data.total;
					if (this.tab[this.tabCur].total > 10) {
						this.status[this.tabCur] = 'loadmore'
					} else {
						this.status[this.tabCur] = 'nomore'
					}
					this.$modal.closeLoading2();
					this.status2 = true;
					uni.stopPullDownRefresh();
				}).catch((error) => {
					this.status[this.tabCur] = 'nomore'
					this.status2 = true;
					this.$modal.closeLoading();
					uni.stopPullDownRefresh();
				})
			},
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
				} else if (val == '收车手续费预扣') { // 线下
					// 收车手续费预扣明细
					this.$tab.navigateTo('/subPages/home/account/bond/withholdCollection?data='+encodeURIComponent(JSON.stringify(data)));
				} else if (val == '保证金充值') {
					// 保证金充值
					this.$tab.navigateTo('/subPages/home/account/bond/rechargeDetails?data='+encodeURIComponent(JSON.stringify(data)));
				} else if (val == '保证金预扣释放') {
					// 保证金预扣释放
					this.$tab.navigateTo('/subPages/home/account/bond/release?data='+encodeURIComponent(JSON.stringify(data)));
				} else if (val == '收车手续费预扣释放') { // 线下
					// 收车手续费预扣释放明细
					this.$tab.navigateTo('/subPages/home/account/bond/releaseCollection?data='+encodeURIComponent(JSON.stringify(data)));
				} else if (val == '保证金实扣') {
					// 保证金实扣
					this.$tab.navigateTo('/subPages/home/account/bond/actualDeduction?data='+encodeURIComponent(JSON.stringify(data)));
				} else if (val == '收车手续费实扣') { // 线下
					// 收车手续费实扣明细
					this.$tab.navigateTo('/subPages/home/account/bond/actualCollection?data='+encodeURIComponent(JSON.stringify(data)));
				} else if (val == '保证金回填(利润)') {
					// 保证金回填(利润)
					this.$tab.navigateTo('/subPages/home/account/bond/info?data='+encodeURIComponent(JSON.stringify(data)));
				} else if (val == '保证金充值中') { // 线下
					// 保证金充值中明细
					this.$tab.navigateTo('/subPages/home/account/bond/rechargeInfo?data='+encodeURIComponent(JSON.stringify(data)));
				} else if (val == '保证金充值') { // 线下
					// 保证金充值成功明细
					this.$tab.navigateTo('/subPages/home/account/bond/rechargeSuccess?data='+encodeURIComponent(JSON.stringify(data)));
				} else if (val == '保证金充值失败') { // 线下
					// 保证金充值失败明细
					this.$tab.navigateTo('/subPages/home/account/bond/rechargeFail?data='+encodeURIComponent(JSON.stringify(data)));
				} else if (val == '保证金提现中') { // 线下
					// 保证金提现中明细
					this.$tab.navigateTo('/subPages/home/account/bond/withdrawalInfo?data='+encodeURIComponent(JSON.stringify(data)));
				} else if (val == '提现手续费预扣') { // 线下
					// 提现手续费预扣明细
					this.$tab.navigateTo('/subPages/home/account/bond/withdrawalWithhold?data='+encodeURIComponent(JSON.stringify(data)));
				} else if (val == '保证金提现') { // 线下
					// 保证金提现明细
					this.$tab.navigateTo('/subPages/home/account/bond/withdrawalSuccess?data='+encodeURIComponent(JSON.stringify(data)));
				} else if (val == '提现手续费实扣') { // 线下
					// 提现手续费实扣明细
					this.$tab.navigateTo('/subPages/home/account/bond/withdrawalActual?data='+encodeURIComponent(JSON.stringify(data)));
				} else if (val == '保证金提现退回') { // 线下
					// 保证金提现退回明细
					this.$tab.navigateTo('/subPages/home/account/bond/withdrawalFail?data='+encodeURIComponent(JSON.stringify(data)));
				} else if (val == '提现手续费预扣释放') { // 线下
					// 提现手续费预扣释放明细
					this.$tab.navigateTo('/subPages/home/account/bond/withdrawalWithholdFail?data='+encodeURIComponent(JSON.stringify(data)));
				}
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
	
	/deep/ .u-tabs {
		width: 100% !important;
	}
	
	/deep/ .u-list {
		height: auto !important;
	}
</style>