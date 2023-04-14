<template>
	<view class="progress">
		<uni-card :is-shadow="false" is-full>
			<u-steps current="1" direction="column" activeColor="#fa6401">
				<u-steps-item title="发起提现申请">
				</u-steps-item>
				<u-steps-item title="市场方审批中" desc="请及时提交发票到市场处进行审批" v-if="type == 2">
				</u-steps-item>
				<u-steps-item title="银行处理">
				</u-steps-item>
				<u-steps-item title="到账成功"></u-steps-item>
			</u-steps>
		</uni-card>
		<view style="padding: 20px;">
			<view style="overflow: hidden;">
				<view style="float: left;">
					<view>提现金额</view>
					<view>到账银行卡</view>
				</view>
				<view style="float: right;">
					<view>￥1,000.00元</view>
					<view>招商银行 尾号 1167</view>
				</view>
			</view>
			<button class="button" @click="handleCancel">完成</button>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				type: null
			}
		},
		onLoad(options) {
			this.type = options.type;
			if (options.type == 1) {
				uni.setNavigationBarTitle({
					title: '保证金提现'
				});
			} else if (options.type == 2) {
				uni.setNavigationBarTitle({
					title: '我的利润提现'
				});
			}
		},
		methods: {
			// 取消
			handleCancel() {
				if (this.type == 1) {
					this.$tab.navigateTo('/subPages/home/account/bond');
				} else if (this.type == 2) {
					this.$tab.navigateTo('/subPages/home/account/profit');
				}
			}
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
	}
</style>