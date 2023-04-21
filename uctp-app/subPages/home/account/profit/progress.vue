<template>
	<view class="progress">
		<uni-card :is-shadow="false" is-full>
			<u-steps current="1" direction="column" activeColor="#fa6401">
				<u-steps-item title="发起提现申请">
				</u-steps-item>
				<u-steps-item title="市场方审批中" desc="请及时提交发票到市场处进行审批">
				</u-steps-item>
				<u-steps-item title="银行处理中...">
				</u-steps-item>
				<u-steps-item title="到账成功"></u-steps-item>
			</u-steps>
		</uni-card>
		<view style="padding: 20px;">
			<view style="overflow: hidden;">
				<view style="float: left;">
					<view class="text1">提现金额</view>
					<view class="text1">到账银行卡</view>
				</view>
				<view style="float: right;">
					<view class="text2">￥1,000.00元</view>
					<view class="text2">招商银行 尾号 1167</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				
			}
		},
		onBackPress(options) {
			this.$tab.navigateTo('/subPages/home/account/profit/profit');
			return true;
		},
		methods: {

		}
	}
</script>

<style lang="scss" scoped>
	.progress {
		.button {
			background-color: #fa6401;
			color: #fff;
			margin-top: 10px;
			margin-top: 30%;
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