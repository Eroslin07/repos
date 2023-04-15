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
				index: 0
			}
		},
		methods: {
			handleChange(val) {
				this.index = val.index;
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