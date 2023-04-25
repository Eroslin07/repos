<template>
	<view class="client-container">
		<view class="info-header">
			<view class="icon-header">
			</view>
			<view class="text">{{'卖家信息'}}</view>
		</view>
		<view class="client-content">
			<u--form v-if="clientTitle=='卖家信息'" labelPosition="left" :model="clientINfoForm" ref="sellerForm" labelWidth="120px">
				<!-- <u-form-item label="是否第三方代收" borderBottom>
					<u-radio-group v-model="clientINfoForm.collection" disabled activeColor="#fd6404">
						<u-radio shape="circle" label="否" :name="0"></u-radio>
						<text style="margin: 0 5px;"></text>
						<u-radio shape="circle" label="是" :name="1"></u-radio>
					</u-radio-group>
				</u-form-item> -->
				<u-form-item label="姓名" borderBottom>
					<u--input disabled disabledColor="#ffffff" v-model="clientINfoForm.sellerName"
						border="none" placeholder=" ">
					</u--input>
				</u-form-item>
				<u-form-item label="身份证号" borderBottom>
					<u--input disabled disabledColor="#ffffff" v-model="clientINfoForm.sellerIdCard"
						border="none" placeholder=" "></u--input>
				</u-form-item>

				<u-form-item label="联系地址" borderBottom>
					<u--input disabled disabledColor="#ffffff" v-model="clientINfoForm.sellerAdder"
						border="none" placeholder=" "></u--input>
				</u-form-item>
				<u-form-item label="电话" borderBottom>
					<u--input disabled disabledColor="#ffffff" v-model="clientINfoForm.sellerTel"
						border="none" placeholder=" "></u--input>
				</u-form-item>
				<u-form-item label="第三方姓名" borderBottom v-if="clientINfoForm.collection == 1">
					<u--input disabled disabledColor="#ffffff" v-model="clientINfoForm.thirdSellerName" border="none"
						placeholder=" "></u--input>
				</u-form-item>
				<u-form-item label="收款方式" borderBottom>
					<u--input disabled disabledColor="#ffffff" v-model="clientINfoForm.remitType" placeholder=" "
						border="none"></u--input>
				</u-form-item>
				<u-form-item label="开户行" borderBottom>
					<u--input disabled disabledColor="#ffffff" v-model="clientINfoForm.bankName" border="none"
						placeholder=" "></u--input>
				</u-form-item>
				<u-form-item label="银行卡号" borderBottom v-if="clientINfoForm.collection == 0">
					<u--input disabled disabledColor="#ffffff" v-model="clientINfoForm.bankCard" border="none"
						placeholder="" @change="handleChange"></u--input>
				</u-form-item>
				<u-form-item label="第三方银行卡号" borderBottom v-if="clientINfoForm.collection == 1">
					<u--input disabled disabledColor="#ffffff" v-model="clientINfoForm.thirdBankCard" border="none"
						placeholder="" @change="handleChange"></u--input>
				</u-form-item>
			</u--form>
			
			<!-- 买家信息 -->
			<u--form v-if="clientTitle=='买家信息'" labelPosition="left" :model="clientINfoForm" ref="sellerForm" labelWidth="120px">
				<!-- <u-form-item label="是否第三方代收" borderBottom>
					<u-radio-group v-model="clientINfoForm.collection" disabled activeColor="#fd6404">
						<u-radio shape="circle" label="否" :name="0"></u-radio>
						<text style="margin: 0 5px;"></text>
						<u-radio shape="circle" label="是" :name="1"></u-radio>
					</u-radio-group>
				</u-form-item> -->
				<u-form-item label="姓名" borderBottom>
					<u--input disabled disabledColor="#ffffff" v-model="clientINfoForm.buyerName" border="none"
						placeholder=" ">
					</u--input>
				</u-form-item>
				<u-form-item label="身份证号" borderBottom>
					<u--input disabled disabledColor="#ffffff" v-model="clientINfoForm.buyerIdCard" border="none"
						placeholder=" "></u--input>
				</u-form-item>

				<u-form-item label="联系地址" borderBottom>
					<u--input disabled disabledColor="#ffffff" v-model="clientINfoForm.buyerAdder" border="none"
						placeholder=" "></u--input>
				</u-form-item>
				<u-form-item label="电话" borderBottom>
					<u--input disabled disabledColor="#ffffff" v-model="clientINfoForm.buyerTel" border="none"
						placeholder=" "></u--input>
				</u-form-item>
			</u--form>
		</view>

	</view>
</template>

<script>
	export default {
		data() {
			return {
				clientINfoForm: {},
				clientTitle: '买家信息',
			}
		},
		onLoad(prop) {
			if (prop.carInfoAll) {
				this.clientINfoForm = prop.carInfoAll.carInfoDetails
			}
			if (prop.infoType == 2) {
				this.clientTitle = '卖家信息'
			}
			if (prop.infoType == 4) {
				this.clientTitle = '买家信息'
			}
			uni.setNavigationBarTitle({
				title: this.clientTitle,
			})
		},
	}
</script>

<style lang="scss" scoped>
	.client-container {
		width: 100vw;
		// padding:0 32rpx;
		font-size: 28rpx;
		font-family: Helvetica Neue, Helvetica, sans-serif;

		.info-header {
			color: #A6A6A6;
			position: relative;
			border-bottom: 2rpx solid #f5f5f5;
			border-top: 2rpx solid #f5f5f5;
			line-height: 114rpx;
			padding: 0 32rpx;
			padding-left: 55rpx;
			font-size: 32rpx;
			color: #000;

			.icon-header {
				position: absolute;
				left: 32rpx;
				top: 50%;
				height: 30rpx;
				border: 5rpx solid #fa6400;
				transform: translateY(-50%);
			}

			// .text {
			// 	font-size: 32rpx;
			// 	color: #000;
			// }
		}

		.client-content {
			margin-left: 32rpx;
		}
	}

	/deep/ .u-form-item__body__left__content__label {
		font-family: Helvetica Neue, Helvetica, sans-serif !important;
		font-size: 28rpx !important;
	}
</style>
