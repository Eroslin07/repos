<template>
	<view class="account">
		<uni-card :is-shadow="false" title="我的总资产">
			<view style="color: #64f122;">资金受XX银行监管、保证资金账户7×24小时充值/提现</view>
			<view>资产总额：10.73万元</br>其中：</view>
			<u-row justify="space-between" gutter="10">
				<u-col span="6">
					<view class="cell-col" @click="handleClick(1)">
						<view>保证金：</view>
						<view>10.05万元</view>
					</view>
				</u-col>
				<u-col span="6">
					<view class="cell-col" @click="handleClick(2)">
						<view>利润：</view>
						<view>0.68万元</view>
					</view>
				</u-col>
			</u-row>
		</uni-card>
		<uni-card :is-shadow="false" title="我的库存">
			<view>车辆总量：20辆</br>其中：</view>
			<u-row justify="space-between" gutter="10">
				<u-col span="6">
					<view class="cell-col" @click="handleClick(3)">
						<view>目前在售车辆：</view>
						<view>10辆</view>
					</view>
				</u-col>
				<u-col span="6">
					<view class="cell-col" @click="handleClick(4)">
						<view>已销售车辆</view>
						<view>10辆</view>
					</view>
				</u-col>
			</u-row>
		</uni-card>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				
			}
		},
		methods: {
			handleClick(val) {
				if (val === 1) {
					this.$tab.navigateTo('/subPages/home/account/bond');
				} else if (val === 2) {
					this.$tab.navigateTo('/subPages/home/account/profit');
				} else if (val === 3) {
					this.$tab.navigateTo('/subPages/home/account/collection');
				} else if (val === 4) {
					this.$tab.navigateTo('/subPages/home/account/selling');
				}
			}
		}
	}
</script>

<style lang="scss" scoped>
	.cell-col {
		text-align: center;
		border: 1px solid #ddd;
		border-radius: 8px;
	}
</style>