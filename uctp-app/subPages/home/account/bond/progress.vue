<template>
	<view class="progress">
		<uni-card :is-shadow="false" is-full>
			<u-steps current="1" direction="column" activeColor="#fa6401">
				<u-steps-item v-for="(item, index) in data.presentStatusRecords" :title="item.statusText" :desc="item.occurredTime">
				</u-steps-item>
			</u-steps>
		</uni-card>
		<view style="padding: 20px;">
			<view style="overflow: hidden;">
				<view style="float: left;">
					<view class="text1">提现金额</view>
					<view class="text1">到账银行卡</view>
				</view>
				<view style="float: right;">
					<view class="text2">{{ $amount.getComdify(data.payAmount / 100) }}</view>
					<view class="text2">{{ data.payChannelName }} 尾号{{ data.payeeBankAccount }}</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				data: null
			}
		},
		onLoad(options) {
			this.data = JSON.parse(decodeURIComponent(options.data))
			uni.setNavigationBarTitle({
				title: '保证金提现'
			});
		},
		onBackPress(options) {
			this.$tab.navigateTo('/subPages/home/account/bond/bond');
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