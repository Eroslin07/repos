<template>
	<view class="freeze">
		<uni-card>
			<view style="padding: 10px 0;">
				<view class="text" style="font-size: 16px;">待回填保证金总额</view>
				<view class="text" style="margin-top: 10px;">{{ $amount.getComdify(amount / 100 || 0) }}<text style="font-size: 14px;">元</text></view>
			</view>
		</uni-card>
		
		<uni-card>
			<view style="padding: 10px 0;border-bottom: 1px solid #f5f5f5;">
				<view style="font-size: 16px;color: #333333;">待回填保证金明细</view>
			</view>
			<view style="padding: 10px;" v-if="indexList != 0">
				<u-list style="height: 100%;">
					<u-list-item v-for="(item, index) in indexList" :key="index">
						<view @click="handleClick(item.type, item)" style="line-height: 30px;">
							<u-row justify="space-between" customStyle="margin-bottom: 10px;border-bottom: 1px solid #f5f5f5;">
								<u-col span="4">
									<view class="title">{{ item.typeText }}</view>
									<view class="note">{{ item.occurredTime }}</view>
								</u-col>
								<u-col span="4">
									<view class="title" style="text-align: right;">
										<text v-if="item.profitLossTypeText == '收入'">+</text>
										<!-- <text v-if="item.profitLossTypeText == '支出'">-</text> -->
										{{ $amount.getComdify(item.amount / 100 || 0) }} >
									</view>
								</u-col>
							</u-row>
						</view>
					</u-list-item>
				</u-list>
				
				<u-loadmore :status="status" loadingText="努力加载中..." />
			</view>
			<view v-else class="empty-page">
				<image class="empty-img" src="@/subPages/static/images/error/noData.png" mode="widthFix"></image><br />
				<text class="empty-text" v-if="status2">暂无数据</text>
			</view>
		</uni-card>
	</view>
</template>

<script>
	import { getCashbackList, getCashbackDetail } from '@/api/account/profit.js'
	export default {
		data() {
			return {
				// 商户账户号
				accountNo: this.$store.state.user.accountNo,
				amount: 0,
				status2: false,
				indexList: [],
				status: 'loadmore',
				timer: null,
				total: 0,
				pageNo: 1,
				pageSize: 10,
			}
		},
		onLoad(options) {
			this.amount = options.amount;
		},
		mounted() {
			this.getList();
		},
		// 下拉刷新
		onPullDownRefresh() {
			this.status2 = false;
			this.pageNo = 1;
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
				this.pageNo += 1
				this.status2 = false;
				this.getMore();
			}, 1000)
		},
		methods: {
			// 待回填保证金明细查询
			getList() {
				let data = {
					pageNo: this.pageNo,
					pageSize: this.pageSize,
					accountNo: this.accountNo
				}
				this.$modal.loading("数据加载中，请耐心等待...")
				getCashbackList(data).then((res) => {
					this.indexList = res.data.list;
					this.total = res.data.total;
					if (this.total > 10) {
						this.status = 'loadmore'
					} else {
						this.status = 'nomore'
					}
					this.status2 = true;
					this.$modal.closeLoading2();
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
					accountNo: this.accountNo
				}
				getProfitList(data).then((res) => {
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
				getCashbackDetail({ accountNo: this.accountNo, id: data.id }).then((res) => {
					if (val == '待回填保证金') {
						// 待回填保证金
						this.$tab.navigateTo('/subPages/home/account/profit/backfilledDetile?data='+encodeURIComponent(JSON.stringify(res.data)));
					}
				})
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