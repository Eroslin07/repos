<template>
	<view class="freeze">
		<!-- 自定义导航栏 -->
		<u-navbar title="保证金冻结明细" @leftClick="back" border safeAreaInsetTop fixed placeholder></u-navbar>
		<uni-card>
			<view style="padding: 10px 0;border-bottom: 1px solid #f5f5f5;">
				<view class="text" style="font-size: 16px;">保证金冻结总额</view>
				<view class="text" style="margin-top: 10px;">{{ $amount.getComdify(amount || 0) }}<text style="font-size: 14px;">元</text></view>
			</view>
			<view style="padding: 10px 0;color: #333333;">
				<!-- <view>保证金提现中：1,000.00元</view> -->
				<view>保证金预扣：{{ $amount.getComdify(amount || 0) }}元</view>
			</view>
		</uni-card>
		
		<uni-card>
			<view style="padding: 10px 0;border-bottom: 1px solid #f5f5f5;">
				<view style="font-size: 16px;color: #333333;">保证金冻结明细</view>
			</view>
			<view style="padding: 10px;" v-if="indexList.length != 0">
				<u-list style="height: 100%;">
					<u-list-item v-for="(item, index) in indexList" :key="index">
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
										{{ $amount.getComdify(item.payAmount / 100) }} >
									</view>
								</u-col>
							</u-row>
						</view>
					</u-list-item>
				</u-list>
				<u-loadmore :status="status" loadingText="努力加载中..." />
			</view>
			<view v-else class="empty-page">
				<image class="empty-img" src="/static/images/index/noData.png" mode="widthFix"></image><br />
				<text class="empty-text" v-if="status2">暂无数据</text>
			</view>
		</uni-card>
	</view>
</template>

<script>
	import { getCarhList } from '@/api/account/bond.js'
	export default {
		data() {
			return {
				amount: 0,
				indexList: [],
				status: 'loadmore',
				timer: null,
				total: 0,
				pageNo: 1,
				pageSize: 10,
				status2: false
			}
		},
		onLoad(options) {
			this.amount = options.amount;
		},
		mounted() {
			this.status2 = false;
			this.getList();
		},
		// 下拉刷新
		onPullDownRefresh() {
			this.getList();
		},
		// 触底加载
		onReachBottom() {
			if (this.timer != null) {
				clearTimeout(this.timer)
			}
			if (this.indexList.length == this.total) {
				this.status = 'nomore';
				return
			}
			this.status = 'loading';
			this.timer = setTimeout(() => {
				this.status2 = false;
				this.pageNo += 1
				this.getMore();
			}, 1000)
		},
		methods: {
			// 页面返回
			back() {
				this.$tab.redirectTo('/subPages/home/account/bond/bond');
			},
			// 保证金冻结明细查询
			getList() {
				let data = {
					pageNo: this.pageNo,
					pageSize: this.pageSize,
					accountNo: this.$store.state.user.accountNo,
					type: 2
				}
				this.$modal.loading("数据加载中，请耐心等待...")
				getCarhList(data).then((res) => {
					this.indexList = res.data.list;
					this.total = res.data.total;
					if (this.total > 10) {
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
			getMore(params) {
				let data = {
					pageNo: this.pageNo,
					pageSize: this.pageSize,
					accountNo: this.$store.state.user.accountNo,
					type: 2
				}
				getCarhList(data).then((res) => {
					this.indexList = [...this.indexList, ...res.data.list];
					this.total = res.data.total;
					if (this.total > this.indexList.length) {
						this.status = 'loadmore'
					} else {
						this.status = 'nomore'
					}
					this.status2 = true;
				})
			},
			handleClick(val, data) {
				if (val == '保证金预扣释放') {
					// 保证金预扣释放
					this.$tab.navigateTo('/subPages/home/account/bond/release?data='+encodeURIComponent(JSON.stringify(data)));
				} else if (val == '保证金预扣') {
					// 保证金预扣
					this.$tab.navigateTo('/subPages/home/account/bond/withhold?data='+encodeURIComponent(JSON.stringify(data)));
				} else if (val == '保证金提现中') {
					// 保证金预扣
					this.$tab.navigateTo('/subPages/home/account/bond/progress?data='+encodeURIComponent(JSON.stringify(data)));
				}
			}
		}
	}
</script>

<style lang="scss" scoped>
	.text {
		text-align: center;
		font-size: 20px;
		font-weight: bold;
	}
	
	.empty-page {
		width: 100%;
		margin-top: 50%;
		transform: translate(0%, -70%);
		text-align: center;
	
		.empty-img {
			width: 30%;
		}
	}
</style>