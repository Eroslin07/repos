<template>
	<view class="withdrawal">
		<uni-card>
			<view style="margin-bottom: 10px;">到账银行卡</view>
			<view>{{ bankName }}（{{ formatBank(bankNo) }}）</view>
		</uni-card>
		<uni-card>
			<view>
				<view>提现金额</view>
				<view style="height: 30rpx;">{{amountText}}</view>
				<u-input border="none" v-model="amount" type="digit" :focus="true" clearable
					:customStyle="{'height': '50px'}" @input="handleInput" fontSize="24px" :maxlength='maxlength'>
					<u--text text="￥" slot="prefix" margin="0 3px 0 0" type="tips"></u--text>
					<template slot="suffix">
						<view style="color: #fa6401;" @click="handleQuanbu">全部提现</view>
					</template>
				</u-input>
			</view>
			<view style="height: 30rpx;color: red;font-size: 20rpx;">{{amountVisible?'输入金额超过保证金余额':''}}</view>
			<view>可用保证金余额{{ $amount.getComdify(allAmount) }}元。</view>
		</uni-card>
		<view style="padding: 20px;">
			<button class="button" @click="handleDefine">提现</button>
		</view>
	</view>
</template>

<script>
	import {
		getWithdraw
	} from '@/api/account/bond.js'
	export default {
		data() {
			return {
				amount: '',
				allAmount: null,
				bankName: '',
				bankNo: '',
				revision: 0,
				amountText: '',
				amountVisible: false,
				maxlength: 12
			}
		},
		onLoad(options) {
			this.allAmount = options.amount;
			this.revision = options.revision;
			this.bankName = options.bankName;
			this.bankNo = options.bankNo;
			uni.setNavigationBarTitle({
				title: '保证金提现'
			});
		},
		methods: {
			// 格式化银行卡
			formatBank(val) {
				if (val) {
					return val.replace(/\s*/g, "").replace(/^(.{1})(?:\d+)(.{1})$/, "$1***********$2")
				} else {
					return ''
				}
			},
			// 输入金额回调
			handleInput(val) {
				const texts = ['百', '千', '万', '十万', '百万', '千万', '亿', '十亿', '百亿', '千亿']
				if (val) {
					this.$nextTick(() => {
						if (parseFloat(val) > parseFloat(this.$amount.getDelcommafy(this.allAmount))) {
							this.amountVisible = true
						} else {
							this.amountVisible = false
						}
						if (val.indexOf('.') > -1) {
							let arr = val.split('.');
							if (arr[0].length > 2) {
								this.amountText = texts[arr[0].length - 3]
							} else {
								this.amountText = ''
							}
							if (arr[0].length > 9) {
								this.maxlength = 12
							} else {
								this.maxlength = arr[0].length + 3
							}
						} else {
							if (val.length > 2) {
								this.amountText = texts[val.length - 3]
							} else {
								this.amountText = ''
							}
							this.maxlength = 12
						}
					})
				} else {
					this.amountText = ''
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
					uni.$emit('refresh', {
						refresh: true
					});
					this.$tab.navigateBack();
					// this.$tab.redirectTo('/subPages/home/account/bond/progress?data='+encodeURIComponent(JSON.stringify(res.data.cashDetails)));
				})
			},
			// 点击全部提现
			handleQuanbu() {
				this.amount = this.$amount.getDelcommafy(this.allAmount)
				const texts = ['百', '千', '万', '十万', '百万', '千万', '亿', '十亿', '百亿', '千亿']
				if (this.amount) {

					if (this.amount.indexOf('.') > -1) {
						let arr = this.amount.split('.')
						if (arr[0].length > 2) {
							this.amountText = texts[arr[0].length - 3]
						} else {
							this.amountText = ''
						}
					} else {
						if (this.amount.length > 2) {
							this.amountText = texts[this.amount.length - 3]
						} else {
							this.amountText = ''
						}
					}
				} else {
					this.amountText = ''
				}
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