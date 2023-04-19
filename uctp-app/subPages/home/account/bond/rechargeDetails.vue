<template>
	<view class="info">
		<uni-card :is-shadow="false" is-full>
			<view style="overflow: hidden;margin-bottom: 20px;text-align: center;">
				<h3>入账金额</h3>
				<h3 style="margin-top: 10px;">+{{ $amount.getComdify(data.payAmount || 0) }}元</h3>
			</view>
		</uni-card>
		<view style="padding: 20px;">
			<view style="overflow: hidden;">
				<view style="float: left;">
					<view class="text1">交易类型</view>
					<view class="text1">交易去向</view>
					<view class="text1">交易时间</view>
					<view class="text1">交易单号</view>
				</view>
				<view style="float: right;">
					<view class="text2">{{ data.tradeTypeName }}</view>
					<view class="text2">{{ data.tradeToName }}</view>
					<view class="text2">{{ data.createTime }}</view>
					<view class="text2">{{ data.contractNo }}</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				type: null,
				data: null
			}
		},
		onLoad(options) {
			this.data = JSON.parse(decodeURIComponent(options.data))
		},
		methods: {
			
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