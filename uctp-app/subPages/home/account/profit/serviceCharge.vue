<template>
	<view class="info">
		<uni-card :is-shadow="false" is-full>
			<view style="overflow: hidden;margin-bottom: 20px;text-align: center;">
				<h3>支出金额</h3>
				<h3 style="margin-top: 10px;">-10,000.00元</h3>
			</view>
		</uni-card>
		<view style="padding: 20px;">
			<view style="overflow: hidden;">
				<view style="float: left;">
					<view class="text1">交易类型</view>
					<view class="text1">交易去向</view>
					<view class="text1">交易时间</view>
					<view class="text1">交易合同号</view>
					<view class="text1">关联车辆明细</view>
				</view>
				<view style="float: right;">
					<view class="text2">服务费扣减</view>
					<view class="text2">市场方</view>
					<view class="text2">2023-03-23 16:08:03</view>
					<view class="text2">MC12323432123211</view>
					<view @click="handleClick" class="text2">显示车架号</view>
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