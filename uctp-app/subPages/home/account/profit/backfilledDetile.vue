<template>
	<view class="info">
		<uni-card :is-shadow="false" is-full>
			<view style="overflow: hidden;margin-bottom: 20px;text-align: center;">
				<view class="text" style="font-size: 16px;">待回填金额</view>
				<view class="text" style="margin-top: 10px;">{{ $amount.getComdify(data.amount / 100 || 0) }}<text style="font-size: 14px;">元</text></view>
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
					<view class="text2">{{ data.typeText || '' }}</view>
					<view class="text2">{{ data.tradeToText || '' }}</view>
					<view class="text2">{{ data.occurredTime || '' }}</view>
					<view class="text2">{{ data.contractCode || '' }}</view>
					<view @click="handleClick" class="text2">{{ data.vin || '' }}</view>
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
			handleClick() {
				this.$tab.navigateTo('/subPages/common/vehicleDetails/vehicleDetails?id=' + this.data.carId);
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
	
	.text1 {
		text-align: left;
		color: #999;
		height: 25px;
		line-height: 25px;
	}
	
	.text2 {
		text-align: right;
		height: 25px;
		line-height: 25px;
	}
</style>