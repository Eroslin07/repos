<template>
	<view class="info">
		<uni-card :is-shadow="false" is-full>
			<view style="overflow: hidden;margin-bottom: 20px;text-align: center;">
				<h3 v-if="type == '保证金扣减'">支出金额</h3>
				<h3 v-else>入账金额</h3>
				<h3 style="margin-top: 10px;">+10,000元</h3>
			</view>
		</uni-card>
		<view style="padding: 20px;">
			<view style="overflow: hidden;">
				<view style="float: left;">
					<view class="text1">交易类型</view>
					<view v-if="type != '保证金扣减'" class="text1">交易去向</view>
					<view v-if="type == '保证金扣减'" class="text1">支出位置</view>
					<view class="text1">交易时间</view>
					<view class="text1">交易合同号</view>
					<view v-if="type != '保证金充值'" class="text1">关联车辆明细</view>
				</view>
				<view style="float: right;">
					<view class="text2">保证金回填</view>
					<view class="text2">我的保证金</view>
					<view class="text2">2023-03-23 16:08:03</view>
					<view class="text2">222XXXXXXXXXXX</view>
					<view v-if="type != '保证金充值'" @click="handleClick" class="text2">显示车架号</view>
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

<style lang="scss" scoped>
	.text1 {
		text-align: left;
		color: #999;
		margin-bottom: 10px;
	}
	
	.text2 {
		text-align: right;
		margin-bottom: 10px;
	}
</style>