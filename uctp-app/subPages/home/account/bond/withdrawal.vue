<template>
	<view class="withdrawal">
		<uni-card>
			<view style="margin-bottom: 10px;">到账银行卡</view>
			<view>浦发银行（***1167）</view>
		</uni-card>
		<uni-card>
			<view>
				<view>提现金额</view>
				<u-input
					border="none"
					v-model="amount"
					type="digit"
					:focus="true"
					clearable
					:customStyle="{'height': '50px'}"
					@input="handleInput"
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
				allAmount: null,
				revision: 0
			}
		},
		onLoad(options) {
			this.allAmount = options.amount;
			this.revision = options.revision;
			uni.setNavigationBarTitle({
				title: '保证金提现'
			});
		},
		methods: {
			// 输入金额回调
			handleInput(val) {
				if (val) {
					this.$nextTick(() => {
						if (val.indexOf('.') > -1) {
							let arr = val.split('.');
							arr[1] = arr[1].slice(0, 2);
							this.amount = arr.join('.');
						}
					})
				}
			},
			// 确定
			handleDefine() {
				if (this.amount == "" || !this.amount) {
					this.$modal.msg("请输入需要提现的金额");
					return
				}
				if (parseFloat(this.amount) > parseFloat(this.$amount.getDelcommafy(this.allAmount))) {
					this.$modal.msg("提现金额大于可用保证金余额，请重新输入提现金额");
					return
				}
				let data = {
					accountNo: this.$store.state.user.accountNo,
					tranAmount: Number(this.amount * 100),
					revision: this.revision
				}
				getWithdraw(data).then((res) => {
					this.$modal.msg("提现成功");
					this.$tab.navigateBack();
					// this.$tab.redirectTo('/subPages/home/account/bond/progress?data='+encodeURIComponent(JSON.stringify(res.data.cashDetails)));
				})
			},
			// 点击全部提现
			handleQuanbu() {
				this.amount = this.$amount.getDelcommafy(this.allAmount)
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