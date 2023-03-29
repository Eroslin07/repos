<template>
	<view class="info">
		<uni-card :is-shadow="false" is-full>
			<view style="overflow: hidden;margin-bottom: 20px;">
				<h3 v-if="type == '保证金扣减'">支出金额</h3>
				<h3 v-else>入账金额</h3>
				<h3 style="float: right;">+10,000元</h3>
			</view>
		</uni-card>
		<view style="padding: 20px;">
			<view style="overflow: hidden;">
				<view style="float: left;">
					<view>交易类型</view>
					<view v-if="type != '保证金扣减'">交易去向</view>
					<view v-if="type == '保证金扣减'">支出位置</view>
					<view>交易时间</view>
					<view>交易单号</view>
					<view v-if="type != '保证金充值'">关联车辆</view>
				</view>
				<view style="float: right;">
					<view>保证金回填</view>
					<view>我的保证金</view>
					<view>2023-03-23 16:08:03</view>
					<view>222XXXXXXXXXXX</view>
					<view v-if="type != '保证金充值'" @click="handleClick">显示车架号</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				type: null
			}
		},
		onLoad(options) {
			this.type = options.type;
			if (options.type == '保证金回填') {
				uni.setNavigationBarTitle({
					title: '保证金回填明细'
				});
			} else if (options.type == '保证金扣减') {
				uni.setNavigationBarTitle({
					title: '保证金扣减明细'
				});
			} else if (options.type == '保证金充值') {
				uni.setNavigationBarTitle({
					title: '保证金充值明细'
				});
			} else if (options.type == '卖车利润') {
				uni.setNavigationBarTitle({
					title: '卖车利润明细'
				});
			} else if (options.type == '利润扣减补保证金') {
				uni.setNavigationBarTitle({
					title: '利润扣减补保证金'
				});
			}
		},
		methods: {
			handleClick() {
				this.$tab.navigateTo('/subPages/common/vehicleDetails/vehicleDetails');
			}
		}
	}
</script>

<style>
</style>