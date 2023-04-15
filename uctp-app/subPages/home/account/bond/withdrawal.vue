<template>
	<view class="withdrawal">
		<uni-card>
			<view style="margin-bottom: 10px;">到账银行卡</view>
			<view>兴业银行（***1167）</view>
		</uni-card>
		<uni-card>
			<view>
				<view>提现金额</view>
				<u-input
					border="none"
					v-model="amount"
					clearable
					:customStyle="{'height': '50px'}"
					fontSize="24px"
				>
					<u--text
						text="￥"
						slot="prefix"
						margin="0 3px 0 0"
						type="tips"
					></u--text>
					<template slot="suffix">
						<view style="color: #fa6401;">全部提现</view>
					</template>
				</u-input>
			</view>
			<view>可用利润余额13,000.00元。</view>
		</uni-card>
		<view style="padding: 20px;">
			<button class="button" @click="handleDefine">提现</button>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				amount: ''
			}
		},
		onLoad(options) {
			uni.setNavigationBarTitle({
				title: '保证金提现'
			});
		},
		methods: {
			// 确定
			handleDefine() {
				this.$tab.navigateTo('/subPages/home/account/bond/progress');
			},
		}
	}
</script>

<style lang="scss" scoped>
	.withdrawal {
		border-top: 1px solid #f5f5f5;
		
		.button {
			background-color: #fa6401;
			color: #fff;
			margin: 10px 0;
		}
	}
</style>