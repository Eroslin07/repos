<template>
	<view class="info">
		<uni-card :is-shadow="false" is-full>
			<view style="overflow: hidden;margin-bottom: 20px;text-align: center;">
				<h3>预扣金额</h3>
				<h3 style="margin-top: 10px;">-10,000元</h3>
			</view>
		</uni-card>
		<view style="overflow: hidden;padding: 20px;border-bottom: 1px solid #f5f5f5;">
			<view style="float: left;margin-right: 10px;color: #999;">当前状态</view>
			<view style="float: left;">
				<u-steps current="2" direction="column" activeColor="#fa6401">
					<u-steps-item title="发起提现申请" desc="2023-03-23 16:08:03">
					</u-steps-item>
					<u-steps-item title="市场方审批" desc="2023-03-23 16:08:03">
					</u-steps-item>
					<u-steps-item title="银行处理" desc=" ">
					</u-steps-item>
					<u-steps-item title="到账成功" desc="2023-03-23 16:08:03"></u-steps-item>
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
					<view class="text2">2023-03-23 16:08:03</view>
					<view class="text2">2023-03-24 17:00:00</view>
					<view class="text2">MC12323432123211</view>
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
				urls1: [{
					src2: 'https://cdn.uviewui.com/uview/album/1.jpg',
				}]
			}
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