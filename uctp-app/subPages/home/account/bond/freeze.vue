<template>
	<view class="freeze">
		<uni-card>
			<view style="padding: 10px 0;border-bottom: 1px solid #f5f5f5;">
				<view class="text" style="font-size: 16px;">保证金冻结总额</view>
				<view class="text" style="margin-top: 10px;">100,500<text style="font-size: 14px;">元</text></view>
			</view>
			<view style="padding: 10px 0;color: #333333;">
				<view>保证金提现中：1,000.00元</view>
				<view>保证金预扣：200,000元</view>
			</view>
		</uni-card>
		
		<uni-card>
			<view style="padding: 10px 0;border-bottom: 1px solid #f5f5f5;">
				<view style="font-size: 16px;color: #333333;">保证金冻结明细</view>
			</view>
			<view style="padding: 10px;">
				<u-list style="height: 100%;">
					<u-list-item v-for="(item, index) in indexList" :key="index">
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
			</view>
		</uni-card>
		<u-loadmore :status="status" loadingText="努力加载中..." />
	</view>
</template>

<script>
	import { getCarhList } from '@/api/account/bond.js'
	export default {
		data() {
			return {
				indexList: [{
					status: 1,
					title: '保证金提现中'
				}, {
					status: 2,
					title: '保证金预扣'
				}],
				status: 'loadmore',
				timer: null,
				total: 0,
				pageNo: 1,
				pageSize: 10
			}
		},
		mounted() {
			this.getList();
		},
		onPullDownRefresh() {
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
				this.getMore();
			}, 1000)
		},
		methods: {
			// 保证金冻结明细查询
			getList() {
				let data = {
					pageNo: this.pageNo,
					pageSize: this.pageSize,
					accountNo: '',
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
					this.$modal.closeLoading();
				}).catch((error) => {
					this.status = 'nomore'
					this.$modal.closeLoading();
				})
			},
			getMore(params) {
				let data = {
					pageNo: this.pageNo,
					pageSize: this.pageSize,
					accountNo: '',
					type: i
				}
				getCarhList(data).then((res) => {
					this.indexList = [...this.indexList, ...res.data.list];
					this.total = res.data.total;
					if (this.total > this.indexList.length) {
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
				} else if (val == '保证金预扣') {
					// 保证金预扣
					this.$tab.navigateTo('/subPages/home/account/bond/withhold');
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
</style>