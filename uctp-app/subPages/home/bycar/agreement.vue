<template>
	<view class="agreement">
		<uni-card :is-shadow="false" is-full>
			<view class="text">
				<u-row justify="space-between" customStyle="margin-bottom: 10px">
					<u-col span="3">
						<u-radio-group v-model="checkboxValue" placement="row" activeColor="#68b4c5">
							<u-radio :customStyle="{marginBottom: '8px'}" label="《XX收车合同》" name="1">
							</u-radio>
						</u-radio-group>
					</u-col>
					<u-col span="3">
						<view class="demo-layout" @click="handleLook">查看</view>
					</u-col>
				</u-row>
			</view>
			<view style="color: #fe7345;">注意：收车时，卖车人必须为本人，若非本人，需提供证明方可签约。</view>
			<!-- 底部按钮 -->
			<button @click="handleAffirm" class="button">合同签章</button>
			<button @click="handleCancel" class="button">取消合同签章</button>
			<button @click="handleClose" class="button">关闭</button>
		</uni-card>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				checkboxValue: null
			}
		},
		methods: {
			// 查看
			handleLook() {
				this.$tab.navigateTo('/subPages/common/agreement/index?type=' + '收车');
			},
			// 合同签章
			handleAffirm() {
				
			},
			// 取消合同签章
			handleCancel() {

			},
			// 关闭
			handleClose() {

			}
		}
	}
</script>

<style lang="scss" scoped>
	.uni-card--border {
		border: none;
	}

	/* #ifdef MP-WEIXIN */
	/deep/ .uni-card--border {
		border-bottom: none;
	}
	/* #endif */

	.text {
		padding: 200rpx 0;
		font-size: 16px;
		text-align: center;
		color: #2aa4d9;
	}

	.button {
		margin-top: 10px;
		background-color: #50a8bc;
		color: #fff;
	}
</style>
