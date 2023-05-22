<template>
	<view class="progress">
		<uni-card :is-shadow="false" is-full>
			<view style="overflow: hidden;margin-bottom: 20px;text-align: center;">
				<view class="text" style="font-size: 16px;">预扣金额</view>
				<view class="text" style="margin-top: 10px;">{{ $amount.getComdify(data.amount / 100 || 0) }}<text style="font-size: 14px;">元</text></view>
			</view>
		</uni-card>
		<view style="overflow: hidden;padding: 20px;border-bottom: 1px solid #f5f5f5;">
			<view style="float: left;margin-right: 10px;color: #999;">当前状态</view>
			<view style="float: left;">
				<u-steps :current="data.presentStatusRecords.length" direction="column" activeColor="#fa6401">
					<u-steps-item v-for="(item, index) in data.presentStatusRecords" :title="item.statusText" :desc="item.occurredTime">
					</u-steps-item>
				</u-steps>
			</view>
			<view style="color: red;">您的保证金充值申请因XXX原因被市场方退回并关闭，请重新利润提现。</view>
		</view>
		<view style="padding: 20px;">
			<view style="overflow: hidden;">
				<view style="float: left;">
					<view class="text1">交易类型</view>
					<view class="text1">交易去向</view>
					<view class="text1">提交时间</view>
					<view class="text1">到账时间</view>
				</view>
				<view style="float: right;">
					<view class="text2">保证金充值</view>
					<view class="text2">我的保证金</view>
					<view class="text2">2023-03-23 16:08:03</view>
					<view class="text2"></view>
				</view>
			</view>
		</view>
		<view style="padding: 0 20px">
			<u-album :urls="urls" :singleSize="150" :multipleSize="70" maxCount="3" rowCount="3"></u-album>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				data: null,
				urls: []
			}
		},
		onLoad(options) {
			this.data = JSON.parse(decodeURIComponent(options.data));
			this.urls = [];
			this.data.invoiceFiles.forEach((item) => {
				this.urls.push(item.fileUrl);
			})
		},
		methods: {

		}
	}
</script>

<style lang="scss" scoped>
	.progress {
		.text {
			text-align: center;
			font-size: 20px;
			font-weight: bold;
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