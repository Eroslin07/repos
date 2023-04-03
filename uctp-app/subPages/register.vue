<template>
	<view class="register">
		<!-- 自定义导航栏 -->
		<!-- <uni-nav-bar :fixed="true" shadow left-icon="left" left-text="返回" :title="title" @clickLeft="back" /> -->
		<u-modal :show="showModal" :content='content' showCancelButton @confirm="handleConfirm" @cancel="handleCancel"></u-modal>
		<!-- 信息填写 -->
		<view>
			<uni-card :is-shadow="false" is-full style="border: none;">
				<u--form labelPosition="left" :model="registerForm" :rules="rules" ref="valiForm" labelWidth="120px">
					<u-form-item label="身份证号" :required="true" prop="number" borderBottom>
						<u--input v-model="registerForm.number" border="none" placeholder="请输入身份证号"></u--input>
						<view slot="right" name="arrow-right">
							<u-upload :fileList="fileList1" @afterRead="afterRead" @delete="deletePic" name="1" multiple
								:previewImage="false" width="60" height="60">
								<text style="color: #50a8bc;">OCR</text>
							</u-upload>
						</view>
					</u-form-item>
					<!-- <u-form-item label="身份证正反面" prop="card" borderBottom>
						<u-upload :fileList="fileList1" @afterRead="afterRead" @delete="deletePic" name="1" multiple
							width="60" height="60"></u-upload>
					</u-form-item> -->
					<u-form-item label="姓名" :required="true" prop="name" borderBottom>
						<u--input v-model="registerForm.name" border="none" placeholder="请输入姓名"></u--input>
					</u-form-item>
					<u-form-item label="手机号" :required="true" prop="phone" borderBottom>
						<u-input v-model="registerForm.phone" border="none" placeholder="请输入手机号">
							<template slot="suffix">
								<view @click="getVerification" style="color: #50a8bc;" v-if="getTime">获取验证码</view>
								<view class="login-code-img" style="color: #50a8bc;" v-else>已发送({{ time }})</view>
							</template>
						</u-input>
					</u-form-item>
					<u-form-item label="验证码" :required="true" prop="captcha" borderBottom>
						<u--input v-model="registerForm.captcha" border="none" placeholder="请输入验证码"></u--input>
					</u-form-item>
					<u-form-item label="营业执照" :required="true" prop="businessLicense" borderBottom>
						<u-upload :fileList="fileList2" @afterRead="afterRead" @delete="deletePic" name="2" multiple
							width="60" height="60"></u-upload>
					</u-form-item>
					<u-form-item label="市场所在地" :required="true" prop="marketLocation" borderBottom @click="showSex = true">
						<u--input v-model="registerForm.marketLocation" disabled disabledColor="#ffffff"
							placeholder="请选择市场场地编号" border="none"></u--input>
						<u-icon slot="right" name="arrow-right"></u-icon>
					</u-form-item>
					<u-form-item label="对公银行账号" :required="true" prop="bankAccount" borderBottom>
						<u--input v-model="registerForm.bankAccount" border="none" placeholder="请输入对公银行账号"></u--input>
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
				<u-picker :show="showSex" :columns="range" keyName="label" title="请选择市场所在地" @confirm="confirm"
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
	import { register } from '@/api/register'
	export default {
		data() {
			return {
				showModal: false,
				content: "XXX二手车平台需要收集您的身份证号及银行账号用于验证您身份真实性，是否同意授权。",
				title: "注册账号",
				getTime: true,
				time: 60,
				timer: null,
				// 身份证信息
				fileList1: [],
				// 营业执照
				fileList2: [],
				registerForm: {
					phone: "",           // 手机号
					captcha: "",         // 验证码
					name: "",            // 姓名
					number: "",          // 身份证号
					businessLicense: [], // 营业执照
					marketLocation: "",  // 市场所在地
					bankAccount: "",     // 对公银行账号
					password: "",        // 密码
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
					number: [{
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
					card: {
						type: 'string',
						required: true,
						message: '请选择身份证反面',
						trigger: ['blur', 'change']
					},
					businessLicense: {
						type: 'string',
						required: true,
						message: '请选择营业执照',
						trigger: ['blur', 'change']
					},
					marketLocation: {
						type: 'string',
						required: true,
						message: '请填写市场场地编号',
						trigger: ['blur', 'change']
					},
					bankAccount: {
						type: 'string',
						required: true,
						message: '请填写对公银行账号',
						trigger: ['blur', 'change']
					},
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
				range: [
					[{
						label: '选项一',
						id: 1
					}],
				],
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
			// 删除图片
			deletePic(event) {
				this[`fileList${event.name}`].splice(event.index, 1)
			},
			// 新增图片
			async afterRead(event) {
				// 当设置 multiple 为 true 时, file 为数组格式，否则为对象格式
				let lists = [].concat(event.file)
				let fileListLen = this[`fileList${event.name}`].length
				lists.map((item) => {
					this[`fileList${event.name}`].push({
						...item,
						status: 'uploading',
						message: '上传中'
					})
				})
				for (let i = 0; i < lists.length; i++) {
					const result = await this.uploadFilePromise(lists[i].url)
					result.forEach(d => {
						d.fileId = d.id;
						if (event.name == 1) {
							this.registerForm.card.push(d);
						} else if (event.name == 2) {
							this.registerForm.businessLicense.push(d);
						}
					})
					let item = this[`fileList${event.name}`][fileListLen]
					this[`fileList${event.name}`].splice(fileListLen, 1, Object.assign(item, {
						status: 'success',
						message: '',
						url: result
					}))
					fileListLen++
				}
			},
			// 上传文件
			uploadFilePromise(url) {
				return new Promise((resolve, reject) => {
					let a = uni.uploadFile({
						url: 'http://192.168.2.21:7001/upload', // 仅为示例，非真实的接口地址
						filePath: url,
						name: 'file',
						formData: {
							user: 'test'
						},
						success: (res) => {
							setTimeout(() => {
								resolve(res.data.data)
							}, 1000)
						}
					});
				})
			},
			// 选择框确定
			confirm(val) {
				this.registerForm.siteNumber = val.value[0].label;
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
					// 密码加密
					this.registerForm.password = rsaEncrypt(this.registerForm.password);
					this.registerForm.confirmPassword = rsaEncrypt(this.registerForm.confirmPassword);
					register(this.registerForm).then((res) => {
						if (res.code === 200) {
							clearInterval(this.timer);
							this.$tab.reLaunch('/pages/login');
						}
					}).catch((error) => {
						this.$modal.msgError("注册失败");
					})
				})
			},
			// 取消
			handleCancel() {
				this.showModal = false;
				clearInterval(this.timer);
				this.$tab.reLaunch('/pages/login');
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
</style>
