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
			</view>
		</u-popup>
		<u-modal :show="showModel" :content='content' :showConfirmButton="true" :showCancelButton="true" @cancel="handleCancel" @confirm="handleConfirm"></u-modal>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				showModel: false,
				content: '未查询到该账号，是否前往注册',
				value: [],
				show: true,
				appId: 'wx52552be23725ae44',
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
				secret: '2cb430095c143228a44488a87350f974',
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
				// this.$store.dispatch('GetInfo').then(res => {
					this.$tab.reLaunch('/pages/index')
				// })
			},
			// 取消
			handleCancel() {
				this.showModel = false;
			},
			// 确认
			handleConfirm() {
				this.showModel = false;
				this.$tab.navigateTo('/subPages/register');
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
