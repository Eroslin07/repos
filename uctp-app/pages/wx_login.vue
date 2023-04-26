<template>
	<view class="normal-login-container">
		<!-- 自定义导航栏 -->
		<u-navbar safeAreaInsetTop fixed placeholder>
			<view class="u-nav-slot" slot="left">
				<view class="bank-logo"></view>
			</view>
		</u-navbar>
		
		<view class="logo-content align-center justify-center flex">
			<h2 class="title" style="color: #000;font-weight: normal;font-style: normal;margin-top: 20%;">结算中心</h2>
		</view>
		<view class="logo-content align-center justify-center flex">
			<h3 class="title" style="margin-right: 50%;">助力车商</h3>
		</view>
		<view class="logo-content align-center justify-center flex">
			<h3 class="fxnw">经纪转经销</h3>
		</view>
		
		<view class="action-btn">
			<u-checkbox-group v-model="value">
				<u-checkbox shape="circle" activeColor="#fe7345"></u-checkbox>
				<view>同意
					<text style="color: #fe7345;" @click="handleUserAgrement">《用户协议》</text>及
					<text style="color: #fe7345;" @click="handlePrivacy">《隐私政策》</text>
				</view>
			</u-checkbox-group>
			<button shape="circle" type="primary" link="true" open-type="getPhoneNumber" @getphonenumber="getphonenumber" class="login-btn">微信用户一键登录</button>
		</view>
		<u-modal :show="showModel" :content='content' :showConfirmButton="true" :showCancelButton="true" confirmText="是" cancelText="否" @cancel="handleCancel" @confirm="handleConfirm" confirmColor="#fe7345"></u-modal>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				showModel: false,
				content: '您的手机号尚未在平台注册，是否要注册?',
				value: [''],
				show: true,
				// 小程序ID
				appId: 'wx9decec45b7374b90',
				wxcode: '',
				sessionKey: null,
				phone: null
			}
		},
		onLoad() {
			// #ifdef MP-WEIXIN
			let _this = this;
			uni.login({
				provider: 'weixin',
				success(res) {
					_this.wxcode = res.code;
				}
			})
			const params = {
				appId: _this.appId,
				secret: '45323149c53d4340dfad4a304803eeaf', // 小程序秘钥
				grant_type: 'client_credential',
				js_code: _this.wxcode
			}
			uni.request({
				method: 'GET',
				url: `https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=${params.appId}&secret=${params.secret}`,
			}).then((res) => {
				if (res.errcode) {
					uni.showToast({
						title: '获取用户信息失败',
						icon: 'none',
						duration: 2000
					});
					return
				}
				_this.sessionKey = res[1].data.access_token
			})
			// #endif
		},
		methods: {
			// 隐私协议
			handlePrivacy() {
				this.$tab.navigateTo('/subPages/common/webview/privacyAgreement')
			},
			// 用户协议
			handleUserAgrement() {
				this.$tab.navigateTo('/subPages/common/webview/userAgreement')
			},
			getphonenumber(e) {
				if (this.value.length == 0) {
					this.$modal.msgError("请阅读并勾选用户协议")
					return
				}
				let _this = this;
				if (e.detail.errMsg.indexOf('deny') > -1) {
					uni.showToast({
						title: "获取手机号失败，请前往手机登录界面",
						icon: "none"
					})
					return
				} else {
					const encryptedData = e.detail.encryptedData;
					 if (typeof encryptedData === 'undefined' || encryptedData == null || encryptedData === '') {
						//前往手机登录界面
						uni.showToast({
							title: "获取手机号失败，请前往手机登录界面",
							icon: "none"
						})
						return;
					}
				}
				uni.request({
					method: 'POST',
					url: `https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=${_this.sessionKey}`,
					data: {
						code: e.detail.code
					}
				}).then((res) => {
					if (res.errcode) {
						uni.showToast({
							title: '获取用户信息失败',
							icon: 'none',
							duration: 2000
						});
						return
					}
					_this.phone = res[1].data.phone_info.phoneNumber;
					_this.$store.dispatch('GetPhone', _this.phone);
					_this.phoneLogin();
				})
			},
			// 手机号登录
			async phoneLogin(captchaParams) {
			  this.$modal.loading("登录中，请耐心等待...")
			  // 执行登录
				this.$store.dispatch('phoneLogin', this.phone).then(() => {
					this.$modal.closeLoading()
					this.loginSuccess()
				}).catch((error) => {
					this.showModel = true;
				})
			},
			// 登录成功后，处理函数
			loginSuccess(result) {
				// 设置用户信息
				this.$store.dispatch('GetInfo').then(res => {
					this.$tab.reLaunch('/pages/index')
				})
			},
			// 取消
			handleCancel() {
				this.showModel = false;
			},
			// 确认
			handleConfirm() {
				this.showModel = false;
				this.$tab.navigateTo('/subPages/registerNotice');
			}
		}
	}
</script>

<style lang="scss" scoped>
	page {
		background-color: #ffffff;
	}

	.normal-login-container {
		width: 100%;
		
		.bank-logo {
			margin-top: 5px;
			width: 95px;
			height: 25px;
			background: url('/static/images/home/bankLogo.png');
			background-repeat: no-repeat;
			background-size: 100% 100%;
		}

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
		margin-top: 50%;
		height: 200px;
	}
	
	.login-btn {
		height: 45px;
		margin-top: 20px;
		background-color: #fe7345;
	}
</style>
