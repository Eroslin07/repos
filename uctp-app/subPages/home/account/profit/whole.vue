<template>
	<view class="whole">
		<u-sticky bgColor="#fff">
			<view style="border-bottom: 1px solid #f5f5f5;">
				<u-tabs :list="list" lineColor="#FA6400" @change="handleChange"></u-tabs>
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
		</u-sticky>
	</view>
</template>

<script>
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
				indexList: [{
					status: 1,
					title: '利润提现中'
				}, {
					status: 2,
					title: '利润提现'
				}, {
					status: 3,
					title: '卖车利润'
				}, {
					status: 4,
					title: '待回填保证金'
				}, {
					status: 5,
					title: '税费扣减'
				}, {
					status: 6,
					title: '服务费扣减'
				}, {
					status: 7,
					title: '保证金回填'
				}],
				index: 0
			}
		},
		methods: {
			handleChange(val) {
				this.index = val.index;
			},
			handleClick(val) {
				if (val == '利润提现中') {
					// 利润提现中
					this.$tab.navigateTo('/subPages/home/account/profit/progressDetile');
				} else if (val == '利润提现') {
					// 利润提现
					this.$tab.navigateTo('/subPages/home/account/profit/detailed');
				} else if (val == '卖车利润') {
					// 卖车利润
					this.$tab.navigateTo('/subPages/home/account/profit/info');
				} else if (val == '待回填保证金') {
					// 待回填保证金
					this.$tab.navigateTo('/subPages/home/account/profit/backfilledDetile');
				} else if (val == '税费扣减') {
					// 税费扣减
					this.$tab.navigateTo('/subPages/home/account/profit/taxation');
				} else if (val == '服务费扣减') {
					// 服务费扣减
					this.$tab.navigateTo('/subPages/home/account/profit/serviceCharge');
				} else if (val == '保证金回填') {
					// 保证金回填
					this.$tab.navigateTo('/subPages/home/account/profit/deduction');
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