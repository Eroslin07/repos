<template>
	<view class="container">
		<uni-card :is-shadow="false" is-full>
			<view class="notice-content">

				尊敬的用户，在您注册成为平台商户的过程中，您需要通过点击<text class="agreeAndRegist">同意并注册</text>的形式在线签署平台 <text
					class="padding5">《用户协议》</text>及<text class="padding5 ">《隐私政策》</text>，
				请您务必仔细阅读、充分理解条款内容后再点击<text class="agreeAndRegist">同意并注册</text>。

				<br />请您注意：如果您不同意上述服务条款、隐私政策或其中任何约定，请您停止注册。如您阅读并点击<text
					class="agreeAndRegist">同意并注册</text>即表示您已充分阅读、理解并接受其全部内容，并表明您也同意平台可以依据以上隐私政策来处理您的个人信息。

				<br />点击<text class="agreeAndRegist">同意并注册</text>即表示您已阅读并同意平台<text class="padding5 col1A73E8"
					@click="handleUserAgree">《用户协议》</text>及<text class="padding5 col1A73E8" @click="privacyAgree">《隐私政策》</text> 。
			</view>
			<view class="btns-box">
				<button type="default" plain="true" @click="handleDisagree">不同意</button>
				<button class="btn-two" type="primary" @click="handleAgree">同意并注册</button>
			</view>
		</uni-card>
	</view>
</template>

<script>
	export default {
		methods: {
			// 不同意
			handleDisagree() {
				// #ifndef MP-WEIXIN
				this.$tab.reLaunch('/pages/login')
				// #endif
				// #ifdef MP-WEIXIN
				this.$tab.reLaunch('/pages/wx_login')
				// #endif
			},
			// 同意并注册
			handleAgree() {
				this.$tab.redirectTo('/subPages/register')
			},

			// 用户协议
			handleUserAgree() {
				this.$tab.navigateTo('/subPages/common/webview/userAgreement')
			},
			// 隐私协议
			privacyAgree() {
				this.$tab.navigateTo('/subPages/common/webview/privacyAgreement')
			}
		}
	}
</script>
<style lang="scss" scoped>
	.container {
		width: 100%;
		height: 100%;
		border-top: 1px solid #EBEEF5;

		.col1A73E8 {
			color: #1A73E8;
		}

		.notice-content {
			line-height: 48rpx;
			font-size: 28rpx;

			.agreeAndRegist {
				font-weight: bold;
			}
		}

		.btns-box {
			display: flex;
			justify-content: space-around;
			margin-top: 300rpx;
		}

		button {
			width: 249rpx;
			height: 80rpx;
			line-height: 80rpx;
			font-size: 32rpx;
			border: 1px solid rgb(221, 221, 221);
		}

		.btn-two {
			background-color: #fd6601;
			border: 1px solid #fd6601;
		}

		/deep/ .uni-card--border[data-v-19622063] {
			border-bottom: none !important;
		}

		/deep/ .uni-card--border {
			border-bottom: none !important;
		}

		.padding5 {
			padding: 0 8rpx;
		}
	}
</style>