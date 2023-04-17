<template>
	<view class="whole">
		<u-sticky bgColor="#fff">
			<view style="border-bottom: 1px solid #f5f5f5;">
				<u-tabs :list="list" lineColor="#FA6400" @change="handleChange"></u-tabs>
			</view>
			
			<view style="padding: 10px;" v-for="(item, index) in list" :key="index">
				<block v-if="tabCur === index">
					<u-list style="height: 100%;">
						<u-list-item v-for="(item, tabIndex) in indexList[index]" :key="tabIndex">
							<view @click="handleClick(item.title)" style="line-height: 30px;">
								<u-row justify="space-between" customStyle="margin-bottom: 10px;border-bottom: 1px solid #f5f5f5;">
									<u-col span="4">
										<view class="title">{{ item.title }}</view>
										<view class="note">2023-03-17</view>
									</u-col>
									<u-col span="4">
										<view class="title" style="text-align: right;">+100,000 ></view>
									</u-col>
								</u-row>
							</view>
						</u-list-item>
					</u-list>
				</block>
			</view>
			
			<u-loadmore :status="status" loadingText="努力加载中..." />
		</u-sticky>
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
				indexList0: [{
					status: 1,
					title: '保证金提现中'
				}, {
					status: 2,
					title: '保证金提现'
				}, {
					status: 3,
					title: '保证金回填'
				}, {
					status: 4,
					title: '保证金预扣'
				}, {
					status: 5,
					title: '保证金充值'
				}, {
					status: 6,
					title: '保证金预扣释放'
				}, {
					status: 7,
					title: '保证金实扣'
				}],
				// 支出
				indexList1: [],
				// 收入
				indexList2: [],
				status: 'loadmore',
				timer: null,
				tabCur: 0,
				tab0: {
					total: 0,
					pageNo: 1
				},
				tab1: {
					total: 0,
					pageNo: 1
				},
				tab2: {
					total: 0,
					pageNo: 1
				},
				pageSize: 10
			}
		},
		mounted() {
			this.getList(1);
		},
		onPullDownRefresh() {
			if (this.timer != null) {
				clearTimeout(this.timer)
			}
			if (this.indexList[this.tabCur].length == this.tab[this.tabCur].total) {
				this.status = 'nomore';
				return
			}
			this.status = 'loading';
			this.timer = setTimeout(() => {
				this.tab[this.tabCur].pageNo += 1
				this.getMore();
			}, 1000)
		},
		methods: {
			handleChange(val) {
				this.tabCur = val.index;
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
					accountNo: '',
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
						this.status = 'loadmore'
					} else {
						this.status = 'nomore'
					}
					this.$modal.closeLoading();
				}).catch((error) => {
					this.status = 'nomore'
					this.$modal.closeLoading();
				})
			},
			getMore(params) {
				let data = {
					pageNo: this.tab[this.tabCur].pageNo,
					pageSize: this.pageSize,
					accountNo: '',
					type: i
				}
				getCarhList(data).then((res) => {
					this.indexList[this.tabCur] = [...this.indexList[this.tabCur], ...res.data.list];
					this.tab[this.tabCur].total = res.data.total;
					if (this.tab[this.tabCur].total > this.indexList[this.tabCur].length) {
						this.status = 'loadmore'
					} else {
						this.status = 'nomore'
					}
				})
			},
			handleClick(val) {
				if (val == '保证金提现中') {
					// 保证金提现中
					this.$tab.navigateTo('/subPages/home/account/bond/progress');
				} else if (val == '保证金提现') {
					// 保证金提现明细
					this.$tab.navigateTo('/subPages/home/account/bond/detailed');
				} else if (val == '保证金回填') {
					// 保证金回填
					this.$tab.navigateTo('/subPages/home/account/bond/info');
				} else if (val == '保证金预扣') {
					// 保证金预扣
					this.$tab.navigateTo('/subPages/home/account/bond/withhold');
				} else if (val == '保证金充值') {
					// 保证金充值
					this.$tab.navigateTo('/subPages/home/account/bond/rechargeDetails');
				} else if (val == '保证金预扣释放') {
					// 保证金预扣释放
					this.$tab.navigateTo('/subPages/home/account/bond/release');
				} else if (val == '保证金实扣') {
					// 保证金实扣
					this.$tab.navigateTo('/subPages/home/account/bond/actualDeduction');
				}
			}
		}
	}
</script>

<style lang="scss" scoped>
	.whole {
		border-top: 1px solid #f5f5f5;
	}
	
	/deep/ .u-tabs__wrapper__nav__item  {
		width: 33%;
	}
</style>