<template>
	<view class="normal-login-container">
		<!-- 解决窗体沉浸 -->
		<view :style="{height: `${navHeight}px`}"></view>
		<!-- 自定义导航栏 -->
		<u-navbar title=" ">
			<view class="u-nav-slot" slot="left">
				<view class="bank-logo"></view>
			</view>
		</u-navbar>
		
		<view class="logo-content align-center justify-center flex">
			<h2 class="title" style="color: #333;font-weight: normal;font-style: normal;margin-top: 15%;">车友通结算中心</h2>
		</view>
		<view class="logo-content align-center justify-center flex">
			<view class="weitu"></view>
			<!-- <h3 class="title" style="margin-right: 50%;">助力车商</h3> -->
		</view>
		<!-- <view class="logo-content align-center justify-center flex">
			<h3 class="fxnw">经纪转经销</h3>
		</view> -->
		<view class="login-form-content">
			<view class="input-item flex align-center">
				<u--input v-model="loginForm.username" border="bottom" type="number" placeholder="请输入手机号" maxlength="30" />
			</view>
			<view class="input-item flex align-center" v-if="changing">
				<u--input v-model="loginForm.password" border="bottom" type="password" placeholder="请输入密码" maxlength="32" />
			</view>
			<view class="input-item flex align-center" style="width: 74%;margin: 0px;" v-if="captchaEnabled">
				<u--input v-model="loginForm.code" border="bottom" type="number" placeholder="请输入验证码" maxlength="4"
					style="width: 89%;" />
				<view class="login-code">
					<view @click="getVerification" class="login-code-img" v-if="getTime">获取验证码</view>
					<view class="login-code-img" v-else>已发送({{ time }})</view>
				</view>
			</view>
			<view class="action-btn">
				<u-checkbox-group v-model="value">
					<u-checkbox shape="circle" activeColor="#fd6601"></u-checkbox>
					<view>同意
						<text style="color: #fd6601;" @click="handleUserAgrement">《用户协议》</text>
						<text style="color: #fd6601;" @click="handlePrivacy">《隐私政策》</text>
					</view>
				</u-checkbox-group>
				<button @click="handleLogin" class="login-btn cu-btn block lg"
					style="background-color: #fd6601;color: white;">登录</button>
				<button @click="userRegister" class="login-btn cu-btn block lg"
					style="background-color: #fff;border: 1px solid #ddd;">注册</button>
			</view>
			<view class="register">
				<view class="register-login">
					<view v-if="changing" @click="shortMessage">短信验证码登录</view>
					<view v-if="captchaEnabled" @click="handlePass">密码登录</view>
				</view>
				<view class="register-zhao" @click="retrievePassword">忘记密码</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				navHeight:0,
				captchaEnabled: false,
				globalConfig: getApp().globalData.config,
				changing: true,
				getTime: true,
				time: 60,
				timer: null,
				loginForm: {
					username: "17380123816",
					password: "123456",
					code: "",
					uuid: ''
				},
				value: [''],
				wxcode: ''
			}
		},
		onLoad(){
			/* #ifdef MP-WEIXIN */
			this.getnavigateBarHeight();
			/* #endif */
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
			// 点击短信验证码登录
			shortMessage() {
				this.captchaEnabled = true;
				this.changing = false;
			},
			// 点击用密码登录
			handlePass() {
				this.captchaEnabled = false;
				this.changing = true
			},
			// 点击用户注册
			userRegister() {
				this.$tab.navigateTo('/subPages/registerNotice');
			},
			// 点击忘记密码
			retrievePassword() {
				this.$tab.navigateTo('/subPages/retrievePassword');
			},
			// 获取验证码
			getVerification() {
				this.$modal.msg("验证码已发送");
				this.getTime = false;
				this.timer = setInterval(() => {
					this.time--;
					if (this.time == 0) {
						this.getTime = true;
						clearInterval(this.timer);
					}
				}, 1000)
			},
			// 登录方法
			async handleLogin() {
				if (this.value.length == 0) {
					this.$modal.msgError("请阅读并勾选用户协议")
					return
				}
				// this.$tab.reLaunch('/pages/index');

				// //关闭定时器
				clearInterval(this.timer);
				// return
				if (this.loginForm.username === "") {
					this.$modal.msgError("请输入您的账号")
				} else if (this.loginForm.password === "") {
					this.$modal.msgError("请输入您的密码")
				} else {
					this.$modal.loading("登录中，请耐心等待...")
					this.pwdLogin()
				}
			},
			// 密码登录
			async pwdLogin() {
				this.$store.dispatch('Login', this.loginForm).then(() => {
					this.$modal.closeLoading2()
					this.loginSuccess()
				})
			},
			// 登录成功后，处理函数
			loginSuccess(result) {
				// 设置用户信息
				this.$store.dispatch('GetInfo').then(res => {
					this.$tab.reLaunch('/pages/index')
				})
			},
			
			// 获取顶部导航栏的高度
			getnavigateBarHeight() {
				let menuButtonObject = uni.getMenuButtonBoundingClientRect();
				uni.getSystemInfo({
					success: res => {
						let statusBarHeight = res.statusBarHeight;
						let navigateBarHeight = menuButtonObject.height+(menuButtonObject.top-statusBarHeight)*2;
						this.navHeight=navigateBarHeight+statusBarHeight
					},
					fail(err) {
						console.log(err);
					}
				})
			}
		}
	}
</script>

<style lang="scss">
	.status_bar {
	      height: var(--status-bar-height);
	      width: 100%;
	}
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
			
			.weitu {
				margin-top: 20%;
				/* #ifdef MP-WEIXIN */
				margin-top: 12%;
				/* #endif */
				width: 296rpx;
				height: 172rpx;
				background-image: url('../static/images/weitu.png');
				background-repeat: no-repeat;
				background-size: 100% 100%;
			}

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


		.login-form-content {
			text-align: center;
			margin: 20px auto;
			width: 80%;

			.input-item {
				margin: 20px auto;
				height: 45px;
				border-radius: 20px;

				.title {
					font-size: 16px;
					width: 90px;
					text-align: right;
					color: #797979;
				}

				.input {
					width: 100%;
					font-size: 14px;
					line-height: 20px;
					text-align: left;
					padding-left: 15px;
					border: 1px solid #797979;
					height: 45px;
					border-radius: 10px;
				}

			}

			.action-btn {
				margin-top: 40px;
				/* #ifdef MP-WEIXIN */
				margin-top: 20px;
				/* #endif */
			}

			.login-btn {
				height: 45px;
				margin-top: 10px;
			}

			.login-code {
				height: 38px;
				float: right;

				.login-code-img {
					height: 38px;
					line-height: 38px;
					position: absolute;
					margin-left: 10px;
					width: 150rpx;
					color: #fd6601;
				}
			}

			.register {
				margin-top: 20px;
				overflow: hidden;
				/* #ifdef MP-WEIXIN */
				font-size: 16px;
				/* #endif */

				.register-login {
					float: left;
				}

				.register-zhao {
					float: right;
				}
			}
		}
	}
	
	/* #ifdef MP-WEIXIN */
	/deep/ .u-navbar__content.data-v-95dec1ae {
		align-items: center;
	}
	
	/* #endif */
</style>
