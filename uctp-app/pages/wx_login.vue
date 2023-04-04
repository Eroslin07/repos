<template>
	<view class="normal-login-container">
		<view class="logo-content align-center justify-center flex">
			<h2 class="title" style="color: #000;font-weight: normal;font-style: normal;">xxx结算中心</h2>
		</view>
		<view class="logo-content align-center justify-center flex">
			<h3 class="title" style="margin-right: 50%;">助力车商</h3>
		</view>
		<view class="logo-content align-center justify-center flex">
			<h3 class="fxnw">经纪转经销</h3>
		</view>
		<u-popup :show="show" :overlay="false">
			<view class="action-btn">
				<u-checkbox-group v-model="value">
					<u-checkbox shape="circle" activeColor="#68b4c5"></u-checkbox>
					<view>同意<text style="color: #4ba4ff;">《xx用户协议》《隐私政策》</text></view>
				</u-checkbox-group>
				<!-- #ifdef MP-WEIXIN -->
				<button shape="circle" type="primary" link="true" open-type="getPhoneNumber" @getphonenumber="getphonenumber" class="login-btn">微信一键登录</button>
				<!-- #endif -->
				<!-- <button @click="handleLogin" class="login-btn cu-btn block lg" style="background-color: #68b4c5;color: white;">微信用户一键登录</button> -->
			</view>
		</u-popup>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				value: [],
				show: true
			}
		},
		onLoad() {
			// #ifdef MP-WEIXIN
			uni.login({
				provider: 'weixin',
				success(res) {
					this.wxcode = res.code;
				}
			})
			// #endif
		},
		methods: {
			getphonenumber(e) {
				console.log(e)
			},
			// 登录方法
			async handleLogin() {
				if (this.value.length == 0) {
					this.$modal.msgError("请阅读并勾选用户协议")
					return
				}
				this.$tab.reLaunch('/pages/index');
				return
			},
		}
	}
</script>

<style lang="scss" scoped>
	page {
		background-color: #ffffff;
	}

	.normal-login-container {
		width: 100%;

		.logo-content {
			width: 100%;
			font-size: 21px;
			text-align: center;

			.title {
				padding-top: 10%;
				/* #ifdef MP-WEIXIN */
				padding-top: 6%;
				/* #endif */
				margin-left: 10px;
				color: #fe7345;
				font-style: italic;
				font-weight: normal;
			}

			.fxnw {
				padding-top: 3%;
				color: #fe7345;
				margin-left: 40%;
				font-style: italic;
				font-weight: normal;
			}
		}
	}
	
	.action-btn {
		padding: 20px;
		margin-top: 20px;
		height: 200px 
	}
	
	.login-btn {
		height: 45px;
		margin-top: 10px;
	}
</style>
