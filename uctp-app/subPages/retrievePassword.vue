<template>
	<view class="retrievePassword">
		<!-- 自定义导航栏 -->
		<u-navbar :title="title" leftText="返回" @leftClick="back" safeAreaInsetTop fixed placeholder></u-navbar>
		<!-- 验证中心 -->
		<view v-if="validationCenter">
			<uni-card :is-shadow="false" is-full style="border: none;">
				<view class="text">请填写您要找回密码的账号</view>
				<u--form labelPosition="left" :model="registerForm" :rules="rules" ref="valiForm" labelWidth="60px">
					<u-form-item label="手机号" prop="phone" borderBottom>
						<u-input v-model="registerForm.phone" type="number" border="none" placeholder="请输入手机号">
							<template slot="suffix">
								<view @click="getVerification" style="color: #50a8bc;" v-if="getTime">获取验证码</view>
								<view class="login-code-img" style="color: #50a8bc;" v-else>已发送({{ time }})</view>
							</template>
						</u-input>
					</u-form-item>
					<u-form-item label="验证码" prop="code" borderBottom>
						<u--input v-model="registerForm.code" border="none" placeholder="请输入验证码"></u--input>
					</u-form-item>
				</u--form>
			</uni-card>
		</view>
		<!-- 设置新密码 -->
		<view v-if="setupNewPassword">
			<uni-card :is-shadow="false" is-full>
				<view class="text">请为你的账号{{ registerForm.phone }}设置一个新密码</view>
				<view class="flex align-center">
					<input v-model="registerForm.password" class="password-input" type="text"
						placeholder="请设置8-32为(数字+字母)" maxlength="32" />
				</view>
			</uni-card>
		</view>
		<view class="footer">
			<button @click="handleVerify" class="button" v-if="validationCenter">验证</button>
			<button @click="handleSave" class="button" v-if="setupNewPassword">保存新密码</button>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				title: "找回密码",
				getTime: true,
				time: 60,
				timer: null,
				registerForm: {
					phone: "",
					code: "",
					password: ""
				},
				rules: {
					phone: [{
						type: 'string',
						required: true,
						message: '请填写手机号',
						trigger: ['blur', 'change']
					}, {
						validator(rule, value, data, callback) {
							let iphoneReg = (
								/^(13[0-9]|14[1579]|15[0-3,5-9]|16[6]|17[0123456789]|18[0-9]|19[89])\d{8}$/
							);
							if (!iphoneReg.test(value)) {
								return false
							}
						},
						message: '手机号格式不正确',
						trigger: ['change', 'blur'],
					}],
					code: {
						type: 'string',
						required: true,
						message: '请填写验证码',
						trigger: ['blur', 'change']
					}
				},
				validationCenter: true,
				setupNewPassword: false,
			}
		},
		methods: {
			back() {
				if (this.validationCenter) {
					this.validationCenter = false;
					uni.navigateBack({
						delta: 1
					})
				} else if (this.setupNewPassword) {
					this.setupNewPassword = false;
					this.validationCenter = true;
				}
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
			// 验证
			handleVerify() {
				this.$refs.valiForm.validate().then(res => {
					this.validationCenter = false;
					this.setupNewPassword = true;
					this.title = "设置新密码";
				})
			},
			// 保存新密码
			handleSave() {
				if (this.registerForm.password === "") {
					this.$modal.msgError("请输入您的新密码");
				} else {
					this.validationCenter = false;
					this.setupNewPassword = false;
					// #ifndef MP-WEIXIN
					this.$tab.reLaunch('/pages/login')
					// #endif
					// #ifdef MP-WEIXIN
					this.$tab.reLaunch('/pages/wx_login')
					// #endif
				}
			}
		}
	}
</script>

<style lang="scss" scoped>
	page {
		background-color: #ffffff;
	}

	/* #ifdef MP-WEIXIN */
	/deep/ .uni-card--border {
		border-bottom: none;
	}

	/* #endif */

	.text {
		font-size: 16px;
		color: #000;
		margin: 8px 0;
	}

	.input-item {
		margin: 20px auto;
		height: 45px;
		border-radius: 20px;

		.title {
			font-size: 16px;
			width: 90px;
			text-align: right;
		}

		.input {
			width: 100%;
			font-size: 14px;
			line-height: 20px;
			text-align: left;
			padding-left: 15px;
			background-color: #f5f6f7;
			height: 45px;
			border-radius: 10px;
		}
	}

	.footer {
		width: 100%;
		position: fixed;
		bottom: 0;

		.button {
			background-color: #68b4c5;
			color: #fff;
		}
	}

	.password-input {
		width: 100%;
		height: 45px;
		margin-top: 10px;
		padding: 10px 0;
		border-bottom: 1px solid #ddd;
	}
</style>
