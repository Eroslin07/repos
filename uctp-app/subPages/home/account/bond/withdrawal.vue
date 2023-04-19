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
					type="number"
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
						<view style="color: #fa6401;" @click="handleQuanbu">全部提现</view>
					</template>
				</u-input>
			</view>
			<view>可用保证金余额{{ $amount.getComdify(allAmount) }}元。</view>
		</uni-card>
		<view style="padding: 20px;">
			<button class="button" @click="handleDefine">提现</button>
		</view>
	</view>
</template>

<script>
	import { getWithdraw } from '@/api/account/bond.js'
	export default {
		data() {
			return {
				amount: '',
				allAmount: null
			}
		},
		onLoad(options) {
			this.allAmount = options.amount;
			uni.setNavigationBarTitle({
				title: '保证金提现'
			});
		},
		methods: {
			// 确定
			handleDefine() {
				if (this.amount == "" || !this.amount) {
					this.$modal.msg("请输入需要提现的金额");
					return
				}
				let data = {
					accountNo: this.$store.state.user.accountNo,
					tranAmount: this.amount,
					revision: 3
				}
				getWithdraw(data).then((res) => {
					this.$tab.navigateTo('/subPages/home/account/bond/progress');
				})
			},
			// 点击全部提现
			handleQuanbu() {
				this.amount = this.allAmount
			}
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