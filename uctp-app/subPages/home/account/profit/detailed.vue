<template>
	<view class="progress">
		<uni-card :is-shadow="false" is-full>
			<view style="overflow: hidden;margin-bottom: 20px;text-align: center;">
				<view class="text" style="font-size: 16px;">实扣金额</view>
				<view class="text" style="margin-top: 10px;">{{ $amount.getComdify(data.amount || 0) }}<text style="font-size: 14px;">元</text></view>
			</view>
		</uni-card>
		<view style="overflow: hidden;padding: 20px;border-bottom: 1px solid #f5f5f5;">
			<view style="float: left;margin-right: 10px;color: #999;">当前状态</view>
			<view style="float: left;">
				<u-steps :current="data.presentStatusRecords.length" direction="column" activeColor="#fa6401">
					<u-steps-item v-for="(item, index) in data.presentStatusRecords" :title="item.statusText" :desc="item.occurredTime">
					</u-steps-item>
				</u-steps>
			</view>
		</view>
		<view style="padding: 20px;">
			<view style="overflow: hidden;">
				<view style="float: left;">
					<view class="text1">到账银行</view>
					<view class="text1">申请时间</view>
					<view class="text1">到账时间</view>
					<view class="text1">交易单号</view>
				</view>
				<view style="float: right;">
					<view class="text2">兴业银行</view>
					<view class="text2">{{ data.presentStatusRecords[0].occurredTime }}</view>
					<view class="text2">{{ data.presentStatusRecords[data.presentStatusRecords.length - 1].occurredTime }}</view>
					<view class="text2">{{ data.contractNo }}</view>
				</view>
			</view>
		</view>
		<view style="padding: 0 20px">
			<u-album :urls="urls1" keyName="src2" :singleSize="350"></u-album>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				data: null,
				urls1: [{
					src2: 'https://cdn.uviewui.com/uview/album/1.jpg',
				}]
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
	.progress {
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
	}
</style>