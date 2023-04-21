<template>
	<view class="recharge">
		<uni-card>
			<view>
				<view>充值金额</view>
				<u-input border="none" v-model="amount" type="number" clearable :customStyle="{'height': '50px'}" fontSize="24px">
					<u--text text="￥" slot="prefix" margin="0 3px 0 0" type="tips"></u--text>
				</u-input>
			</view>
		</uni-card>
		<u-popup :show="show" @close="close" :customStyle="{ 'height': '240px' }">
			<view style="position: absolute;top: 10px;left: 10px;" @click="close"><u-icon name="close"></u-icon></view>
			<view style="text-align: center;margin: 10px 0;">请选择充值方式</view>
			<uni-card>
				<view style="overflow: hidden;padding: 10px 0;border-bottom: 1px solid #f5f5f5;" @click="handleWeixin">
					<view style="float: left;">
						<image src="../../../../static/images/account/wxzf.png" class="form-image" style="width: 26px;height: 26px;"></image>
						<text style="margin-left: 10px;">微信支付</text>
					</view>
					<view style="float: right;padding-top: 5px;">
						<u--text prefixIcon="arrow-right" iconStyle="font-size: 19px"></u--text>
					</view>
				</view>
				<view style="overflow: hidden;padding: 10px 0;" @click="handleYunshanfu">
					<view style="float: left;">
						<image src="../../../../static/images/account/ysf.png" class="form-image" style="width: 26px;height: 26px;"></image>
						<text style="margin-left: 10px;">云闪付</text>
					</view>
					<view style="float: right">
						<u--text prefixIcon="arrow-right" iconStyle="font-size: 19px"></u--text>
					</view>
				</view>
			</uni-card>
		</u-popup>
		<view style="padding: 20px;">
			<button class="button" @click="handleDefine">充值</button>
		</view>
	</view>
</template>

<script>
	import { getRecharge } from '@/api/account/bond.js'
	export default {
		data() {
			return {
				amount: '',
				show: false,
				revision: 0
			}
		},
		onLoad(options) {
			this.revision = options.revision;
		},
		methods: {
			// 确定
			handleDefine() {
				if (this.amount == '' || !this.amount) {
					this.$modal.msg("请输入需要充值的金额");
				} else {
					let data = {
						accountNo: this.$store.state.user.accountNo,
						tranAmount: Number(this.amount),
						revision: this.revision
					}
					getRecharge(data).then((res) => {
						this.$modal.msg("充值成功");
						this.$tab.navigateTo('/subPages/home/account/bond/bond');
					})
					// this.show = true;
				}
			},
			// 关闭弹框
			close() {
				this.show = false;
			},
			// 微信支付
			handleWeixin() {
				this.$modal.msg("微信支付");
				let data = {
					accountNo: this.$store.state.user.accountNo,
					tranAmount: Number(this.amount),
					revision: this.revision
				}
				getRecharge(data).then((res) => {
					this.$tab.navigateTo('/subPages/home/account/bond/progress');
				})
			},
			// 云闪付
			handleYunshanfu() {
				this.$modal.msg("云闪付");
				let data = {
					accountNo: this.$store.state.user.accountNo,
					tranAmount: Number(this.amount),
					revision: this.revision
				}
				getRecharge(data).then((res) => {
					this.$tab.navigateTo('/subPages/home/account/bond/progress');
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.recharge {
		border-top: 1px solid #f5f5f5;

		.button {
			background-color: #fa6401;
			color: #fff;
			margin: 10px 0;
		}
	}
</style>