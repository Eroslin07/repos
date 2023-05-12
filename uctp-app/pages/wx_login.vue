<template>
	<view class="normal-login-container" v-if="!showToken">
		<!-- 自定义导航栏 -->
		<u-navbar safeAreaInsetTop fixed placeholder>
			<view class="u-nav-slot" slot="left">
				<view class="bank-logo"></view>
			</view>
		</u-navbar>
		
		<view class="logo-content align-center justify-center flex">
			<h2 class="title" style="color: #333;font-weight: bold;font-style: normal;margin-top: 15%;font-size: 28px;">车友通结算中心</h2>
		</view>
		<view class="logo-content align-center justify-center flex">
			<view class="weitu"></view>
			<!-- <h3 class="title" style="margin-right: 50%;">助力车商</h3> -->
		</view>
		<!-- <view class="logo-content align-center justify-center flex">
			<h3 class="fxnw">经纪转经销</h3>
		</view> -->
		
		<view class="action-btn">
			<u-checkbox-group  @change="checkboxGroupChange"  v-model="value" >
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
	import { getWxToken, getCheckIsLogin } from '@/api/login'
	import { setToken } from '@/utils/auth'
	export default {
		data() {
			return {
				showToken: true,
				showModel: false,
				content: '您的手机号尚未在平台注册，是否要注册?',
				value: [],
				show: true,
				wxcode: '',
				phone: null
			}
		},
		onLoad() {
			// #ifdef MP-WEIXIN
			let _this = this;
			if (!_this.$store.state.user.loginStatus) {
				_this.showToken = false;
				return
			}
			uni.login({
				provider: 'weixin',
				success(res) {
					_this.wxcode = res.code;
					_this.$modal.loading("检测登录环境")
					// 获取openid
					getCheckIsLogin({ wxCode: _this.wxcode }).then((ress) => {
						if (ress.data.accessToken) {
							_this.showToken = true;
							setToken(ress.data);
							_this.loginSuccess();
						} else {
							_this.showToken = false;
						}
					}).catch((error) => {
						_this.showToken = false;
					}).finally(() => {
						_this.$modal.closeLoading()
					})
				}
			})
			// #endif
		},
		methods: {
			checkboxGroupChange(e){
				this.value=e
			},
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
						title: "获取手机号失败",
						icon: "none"
					})
					return
				} else {
					const encryptedData = e.detail.encryptedData;
					 if (typeof encryptedData === 'undefined' || encryptedData == null || encryptedData === '') {
						//前往手机登录界面
						uni.showToast({
							title: "获取手机号失败",
							icon: "none"
						})
						return;
					}
				}
				_this.$modal.loading("登录中，请耐心等待...")
				uni.login({
					provider: 'weixin',
					success(res) {
						getWxToken({ code: e.detail.code, wxCode: res.code }).then((ress) => {
							_this.phone = ress.data;
							_this.$store.commit('SET_PHONE', _this.phone);
							_this.phoneLogin();
						})
					}
				})
			},
			// 手机号登录
			async phoneLogin(captchaParams) {
			  // 执行登录 15328756760
				this.$store.dispatch('phoneLogin', this.phone).then(() => {
					// this.$modal.closeLoading()
					this.loginSuccess()
				}).catch((error) => {
					if (error == 1002000012) {
						this.$modal.hideMsg()
						this.showModel = true;
					}
				}).finally(()=>{
					this.$modal.closeLoading()
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
			
			.weitu {
				margin-top: 20%;
				/* #ifdef MP-WEIXIN */
				margin-top: 28%;
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
	}
	
	.action-btn {
		padding: 20px;
		margin-top: 30%;
		height: 200px;
	}
	
	.login-btn {
		height: 45px;
		margin-top: 20px;
		background-color: #fe7345;
	}
</style>
