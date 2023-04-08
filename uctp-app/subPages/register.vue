<template>
	<view class="register">
		<!-- 自定义导航栏 -->
		<!-- <uni-nav-bar :fixed="true" shadow left-icon="left" left-text="返回" :title="title" @clickLeft="back" /> -->
		<u-modal :show="showModal" :content='content' showCancelButton @confirm="handleConfirm" @cancel="handleCancel"></u-modal>
		<!-- 信息填写 -->
		<view>
			<uni-card :is-shadow="false" is-full style="border: none;">
				<u--form labelPosition="left" :model="registerForm" :rules="rules" ref="valiForm" labelWidth="120px">
					<u-form-item label="身份证号" :required="true" prop="idCard" borderBottom>
						<u--input v-model="registerForm.idCard" border="none" placeholder="请输入身份证号"></u--input>
						<view slot="right" name="arrow-right">
							<text style="color: #50a8bc;" @click="handleOcr(1, 'idCard')">上传图片</text>
						</view>
					</u-form-item>
					<u-form-item label="姓名" :required="true" prop="name" borderBottom>
						<u--input v-model="registerForm.name" border="none" placeholder="请输入姓名"></u--input>
					</u-form-item>
					<u-form-item label="手机号" :required="true" prop="phone" borderBottom>
						<u-input v-model="registerForm.phone" border="none" placeholder="请输入手机号">
							<!-- #ifndef MP-WEIXIN -->
							<template slot="suffix">
								<view @click="getVerification" style="color: #50a8bc;" v-if="getTime">获取验证码</view>
								<view class="login-code-img" style="color: #50a8bc;" v-else>已发送({{ time }})</view>
							</template>
							<!-- #endif -->
						</u-input>
					</u-form-item>
					<!-- #ifndef MP-WEIXIN -->
					<u-form-item label="验证码" :required="true" prop="captcha" borderBottom>
						<u--input v-model="registerForm.captcha" border="none" placeholder="请输入验证码"></u--input>
					</u-form-item>
					<!-- #endif -->
					<u-form-item label="营业执照" :required="true" prop="businessLicense" borderBottom>
						<view class="image">
							<u-upload
								v-if="fileList2.length"
								:fileList="fileList2"
								@delete="deletePic"
								name="2"
								width="70"
								height="70"
							></u-upload>
						</view>
						<view slot="right" name="arrow-right">
							<text style="color: #50a8bc;" @click="handleOcr(2, 'businessLicense')">上传图片</text>
						</view>
					</u-form-item>
					<u-form-item label="市场所在地" :required="true" prop="marketLocationValue" borderBottom @click="showSex = true">
						<u--input v-model="registerForm.marketLocationValue" disabled disabledColor="#ffffff"
							placeholder="请选择市场场地编号" border="none"></u--input>
						<u-icon slot="right" name="arrow-right"></u-icon>
					</u-form-item>
					<u-form-item label="对公银行账号" :required="true" prop="bankAccount" borderBottom>
						<u--input v-model="registerForm.bankAccount" border="none" placeholder="请输入对公银行账号" @change="handleChange"></u--input>
					</u-form-item>
					<u-form-item label="输入密码" :required="true" prop="password" borderBottom>
						<u--input v-model="registerForm.password" password border="none" placeholder="请输入8-32位(数字+字母)">
						</u--input>
					</u-form-item>
					<u-form-item label="再次输入密码" :required="true" prop="confirmPassword" borderBottom>
						<u--input v-model="registerForm.confirmPassword" password border="none"
							placeholder="请输入8-32位(数字+字母)"></u--input>
					</u-form-item>
				</u--form>
				<u-picker :show="showSex" :columns="range" keyName="contactName" title="请选择市场所在地" @confirm="confirm"
					@cancel="cancel"></u-picker>
				<view class="action-btn">
					<button @click="handleSave" class="button">提交审核</button>
				</view>
				<view class="action-btn">
					<button @click="handleCancel" class="button">取消</button>
				</view>
			</uni-card>
		</view>
	</view>
</template>

<script>
	import { rsaEncrypt } from '@/utils/rsa.js'
	import { urlTobase64 } from '@/utils/ruoyi.js'
	import {
		register,
		getTenantlist,
		getCode,
		getIdCard,
		getBusinessLicense,
		deleteImage
	} from '@/api/register'
	export default {
		data() {
			return {
				showModal: false,
				content: "XXX二手车平台需要收集您的身份证号及银行账号用于验证您身份真实性，是否同意授权。",
				title: "注册账号",
				getTime: true,
				time: 60,
				timer: null,
				// 身份证
				fileList1: [],
				// 营业执照
				fileList2: [],
				registerForm: {
					phone: "",               // 手机号
					captcha: "",             // 验证码
					name: "",                // 姓名
					idCard: "",              // 身份证号
					idCardUrl: [],           // 身份证图片
					businessLicense: [],     // 营业执照
					marketLocation: "",      // 市场所在地id
					marketLocationValue: "", // 市场所在地
					bankAccount: "",         // 对公银行账号
					password: "",            // 密码
					confirmPassword: ""      // 确认密码
				},
				// 校验规则
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
					captcha: {
						type: 'string',
						required: true,
						message: '请填写验证码',
						trigger: ['blur', 'change']
					},
					name: {
						type: 'string',
						required: true,
						message: '请填写姓名',
						trigger: ['blur', 'change']
					},
					idCard: [{
						type: 'string',
						required: true,
						message: '请填写身份证号',
						trigger: ['blur', 'change']
					}, {
						validator(rule, value, data, callback) {
							let iphoneReg = (
								/^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/
							);
							if(!iphoneReg.test(value)){
								return false;
							}
						},
						message:"身份证格式不正确",
						trigger: ['blur', 'change']
					}],
					businessLicense: {
						type: 'string',
						required: true,
						message: '请选择营业执照',
						trigger: ['blur', 'change']
					},
					marketLocationValue: {
						type: 'string',
						required: true,
						message: '请选择市场场地',
						trigger: ['blur', 'change']
					},
					bankAccount: [{
						type: 'string',
						required: true,
						message: '请填写对公银行账号',
						trigger: ['blur', 'change']
					}],
					password: [{
						required: true,
						message: '请填写密码',
						trigger: ['blur', 'change']
					}, {
						pattern: /[A-Za-z].*[0-9]|[0-9].*[A-Za-z]/,
						transform(value) {
							return String(value);
						},
						min: 8,
						max: 32,
						message: '只能包含8-32个字母和数字',
						trigger: ['blur', 'change']
					}, {
						asyncValidator: (rules, value, callback) => {
							if (this.registerForm.confirmPassword && value != this.registerForm.confirmPassword) {
								callback(new Error('两次密码校验不一致'))
							}
							return uni.$u.test.object({
								values: confirmPassword
							});
						},
						message: '两次密码校验不一致',
						trigger: ["change", "blur"]
					}],
					confirmPassword: [{
						type: 'string',
						required: true,
						message: '请填写密码',
						trigger: ['blur', 'change']
					}, {
						asyncValidator: (rules, value, callback) => {
							if (value != this.registerForm.password) {
								callback(new Error('两次密码校验不一致'))
							}
							return uni.$u.test.object({
								values: password
							});
						},
						message: '两次密码校验不一致',
						trigger: ["change", "blur"]
					}]
				},
				showSex: false,
				range: [],
			}
		},
		onLoad() {
			this.showModal = true;
		},
		methods: {
			back() {
				uni.navigateBack({
					delta: 1
				})
			},
			handleConfirm() {
				this.showModal = false;
				getTenantlist().then((res) => {
					this.range.push(res.data)
				})
			},
			handleChange(data) {
				let account = data.replace(/\s/g, '').replace(/[^\d]/g, '').replace(/(\d{4})(?=\d)/g, '$1 ')
				this.$set(this.registerForm, 'bankAccount', account)
			},
			// 获取验证码
			getVerification() {
				getCode().then((res) => {
					this.$modal.msg("验证码已发送");
					this.getTime = false;
					this.timer = setInterval(() => {
						this.time--;
						if (this.time == 0) {
							this.getTime = true;
							clearInterval(this.timer);
						}
					}, 1000)
				}).catch((error) => {
					this.$modal.msg("验证码发送失败");
				})
			},
			// 点击OCR
			handleOcr(index, type) {
				let _this = this;
				uni.showActionSheet({
					title: "选择类型",
					itemList: ['相册', '拍摄'],
					success: async function(res) {
						_this.chooseImages(index, type, res.tapIndex);
					}
				})
			},
			// 上传图片
			chooseImages(index, type, tapIndex) {
				let _this = this;
				uni.chooseImage({
					// count: 1,
					sizeType: ['original', 'compressed'], //可以指定是原图还是压缩图，默认二者都有
					sourceType: tapIndex == 0 ? ['album'] : ['camera'], // 从相册选择 : 使用相机
					success: async function(res) {
						let str = await urlTobase64(res.tempFilePaths[0])
						res.tempFilePaths.forEach((item) => {
							_this[`fileList${index}`].push({
								url: item,
								status: 'uploading',
								message: '上传中'
							})
						})
						if (index == 1) {
							// 识别身份证
							_this.registerForm.idCardUrl = _this[`fileList${index}`];
							getIdCard({ IDCardUrl: str }).then((ress) => {
								let data = JSON.parse(ress.data);
								_this.registerForm.idCard = data.words_result['公民身份号码'].words;
								_this.registerForm.name = data.words_result['姓名'].words;
								_this.upload(res, type, index);
							})
						} else if (index == 2) {
							// 识别营业执照
							_this.registerForm.businessLicense = _this[`fileList${index}`];
							_this.upload(res, type, index);
						}
					}
				})
			},
			upload(res, type, index) {
				let _this = this;
				uni.uploadFile({
					url: 'http://172.17.10.127:48080/app-api/infra/file/upload', // 仅为示例，非真实的接口地址
					file: res.tempFiles,
					name: 'file',
					formData: {
						type: type
					},
					success: (ress) => {
						let fileListLen = 0;
						let data = JSON.parse(ress.data).data;
						for (let i = 0; i < data.length; i++) {
							let item = _this[`fileList${index}`][fileListLen]
							_this[`fileList${index}`].splice(fileListLen, 1, Object.assign(item, {
								status: 'success',
								message: '',
								url: data[i].url,
								id: data[i].id
							}))
							fileListLen++;
						}
					}
				});
			},
			// 删除图片
			deletePic(event) {
				deleteImage(event.file.id).then((res) => {
					this.$modal.msg("删除成功");
					this[`fileList${event.name}`].splice(event.index, 1);
				})
			},
			// 选择框确定
			confirm(val) {
				this.registerForm.marketLocation = val.value[0].id;
				this.registerForm.marketLocationValue = val.value[0].contactName;
				this.showSex = false;
			},
			// 选择框取消
			cancel() {
				this.showSex = false;
			},
			// 提交审核
			handleSave() {
				this.$refs.valiForm.validate().then(res => {
					if (this.registerForm.password != this.registerForm.confirmPassword) {
						this.$modal.msgError("两次密码不一致");
						return;
					}
					let data = {
						phone: this.registerForm.phone,
						captcha: this.registerForm.captcha,
						name: this.registerForm.name,
						idCard: this.registerForm.idCard,
						idCardUrl: this.fileList1.map((item) => { return item.id }),
						businessLicense: this.fileList2.map((item) => { return item.id }),
						marketLocation: this.registerForm.marketLocation,
						bankAccount: this.registerForm.bankAccount,
						password: rsaEncrypt(this.registerForm.password),
						confirmPassword: rsaEncrypt(this.registerForm.confirmPassword)
					}
					register(data).then((res) => {
						this.$modal.msg("已提交审核");
						// #ifndef MP-WEIXIN
						clearInterval(this.timer);
						this.$tab.reLaunch('/pages/login')
						// #endif
						// #ifdef MP-WEIXIN
						this.$tab.reLaunch('/pages/wx_login')
						// #endif
					}).catch((error) => {
						this.$modal.msgError("提交审核失败");
					})
				})
			},
			// 取消
			handleCancel() {
				this.showModal = false;
				// #ifndef MP-WEIXIN
				clearInterval(this.timer);
				this.$tab.reLaunch('/pages/login')
				// #endif
				// #ifdef MP-WEIXIN
				this.$tab.reLaunch('/pages/wx_login')
				// #endif
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

	.action-btn {
		text-align: center;
		margin-top: 20px;
	}

	.button {
		background-color: #68b4c5;
		color: #fff;
	}
	
	/deep/ .image .u-upload__button {
		display: none;
	}
</style>
