<template>
	<view class="freeze">
		<uni-card>
			<view style="padding: 10px 0;border-bottom: 1px solid #f5f5f5;">
				<view class="text" style="font-size: 16px;">保证金冻结总额</view>
				<view class="text" style="margin-top: 10px;">100,500<text style="font-size: 14px;">元</text></view>
			</view>
			<view style="padding: 10px 0;color: #333333;">
				<view>保证金提现中：1,000.00元</view>
				<view>保证金预扣：200,000元</view>
			</view>
		</uni-card>
		
		<uni-card>
			<view style="padding: 10px 0;border-bottom: 1px solid #f5f5f5;">
				<view style="font-size: 16px;color: #333333;">保证金冻结明细</view>
			</view>
			<view style="padding: 10px;">
				<u-list style="height: 100%;">
					<u-list-item v-for="(item, index) in 10" :key="index">
						<view style="line-height: 30px;">
							<u-row justify="space-between" customStyle="margin-bottom: 10px;border-bottom: 1px solid #f5f5f5;">
								<u-col span="4">
									<view class="title">保证金提现中</view>
									<view class="note">2023-03-17</view>
								</u-col>
								<u-col span="4">
									<view class="title" style="text-align: right;">+100,000 ></view>
								</u-col>
							</u-row>
						</view>
					</u-list-item>
				</u-list>
			</view>
		</uni-card>
	</view>
</template>

<script>
</script>

<style lang="scss" scoped>
	.text {
		text-align: center;
		font-size: 20px;
		font-weight: bold;
	}
</style>