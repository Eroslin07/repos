<template>
	<view class="progress">
		<view style="padding: 10px 0;border-bottom: 1px solid #f5f5f5;">
			<view class="text" style="font-size: 16px;">实扣金额</view>
			<view class="text" style="margin-top: 10px;">-100,500<text style="font-size: 14px;">元</text></view>
		</view>
		<view style="padding: 20px;border-bottom: 1px solid #f5f5f5;">
			<view style="overflow: hidden;">
				<view style="float: left;">
					<view class="text1">可用余额</view>
					<view class="text1">冻结余额</view>
				</view>
				<view style="float: right;">
					<view class="text2">10,000.00元</view>
					<view class="text2">0.00元</view>
				</view>
			</view>
		</view>
		<view style="padding: 20px;">
			<view style="overflow: hidden;">
				<view style="float: left;">
					<view class="text1">到账银行</view>
					<view class="text1">申请时间</view>
					<view class="text1">到账时间 </view>
					<view class="text1">交易单号</view>
				</view>
				<view style="float: right;">
					<view class="text2">招商银行</view>
					<view class="text2">2023-03-23 16:08:03</view>
					<view class="text2">2023-03-24 17:00:00</view>
					<view class="text2">222XXXXXXXXXXX</view>
				</view>
			</view>
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
		onLoad(options) {
			uni.setNavigationBarTitle({
				title: '保证金提现明细'
			});
		},
		methods: {

		}
	}
</script>

<style lang="scss" scoped>
	.progress {
		border-top: 1px solid #f5f5f5;
		
		.text {
			text-align: center;
			font-size: 20px;
			font-weight: bold;
		}
		
		.text1 {
			text-align: left;
			color: #999;
			margin-bottom: 10px;
		}
		
		.text2 {
			text-align: right;
			margin-bottom: 10px;
		}
	}
</style>