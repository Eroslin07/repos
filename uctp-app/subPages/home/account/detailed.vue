<template>
	<view class="progress">
		<uni-card :is-shadow="false" is-full>
			<u-steps :current="type == '保证金提现' ? 3 : 4" direction="column" activeColor="#50a8bc">
				<u-steps-item title="发起提现申请" desc="2023-03-23 16:08:03">
				</u-steps-item>
				<u-steps-item title="市场方审批中" desc="2023-03-24 15:09:18" v-if="type == '利润提现'">
				</u-steps-item>
				<u-steps-item title="银行处理">
				</u-steps-item>
				<u-steps-item title="到账成功" desc="2023-03-24 17:00:00"></u-steps-item>
			</u-steps>
		</uni-card>
		<view style="padding: 20px;">
			<view style="overflow: hidden;">
				<view style="float: left;">
					<view>提现金额</view>
					<view>到账银行</view>
					<view>申请时间</view>
					<view>到账时间 </view>
					<view>提现单号</view>
				</view>
				<view style="float: right;">
					<view>￥1,000.00元</view>
					<view>招商银行</view>
					<view>2023-03-23 16:08:03</view>
					<view>2023-03-24 17:00:00</view>
					<view>222XXXXXXXXXXX</view>
				</view>
			</view>
		</view>
		<view style="padding: 0 20px" v-if="type == '利润提现'">
			<u-album :urls="urls1" keyName="src2" :singleSize="350"></u-album>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				type: null,
				urls1: [{
					src2: 'https://cdn.uviewui.com/uview/album/1.jpg',
				}]
			}
		},
		onLoad(options) {
			this.type = options.type;
			if (options.type == '保证金提现') {
				uni.setNavigationBarTitle({
					title: '保证金提现明细'
				});
			} else if (options.type == '利润提现') {
				uni.setNavigationBarTitle({
					title: '利润提现明细'
				});
			}
		},
		methods: {

		}
	}
</script>

<style lang="scss" scoped>
	.progress {
		.button {
			background-color: #50a8bc;
			color: #fff;
			margin-top: 10px;
			margin-top: 30%;
		}
	}
</style>