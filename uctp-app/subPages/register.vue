<template>
	<view class="register">
		<!-- 自定义导航栏 -->
		<u-navbar title="注册账号" @leftClick="back" border safeAreaInsetTop fixed placeholder></u-navbar>
		<u-modal :show="showModal" :content='content' showCancelButton @confirm="handleConfirm"
			@cancel="handleCancel"></u-modal>
		<!-- 信息填写 -->
		<view>
			<u--form labelPosition="left" :model="registerForm" :rules="rules" ref="valiForm" labelWidth="120px">
				<uni-card :is-shadow="false" is-full style="border: none;">
					<u-form-item label="上传身份证" :required="true"></u-form-item>
					<view style="color: #f56c6c;" v-if="cardStatus1">请上传身份证</view>
					<u-form-item borderBottom>
						<view class="image">
							<u-grid col="2">
								<u-grid-item>
									<u-upload v-if="fileList1.length" :fileList="fileList1" @delete="deletePic" name="1"
										width="150"></u-upload>
									<image v-else src="/static/images/home/ghm.png" mode="widthFix"
										style="width: 150px;" @click="handleOcr(1, 'idCard')"></image>
									<image v-if="fileList1.length == 0" src="../static/images/take.png"
										class="icon-image" @click="handleOcr(1, 'idCard')"></image>
								</u-grid-item>
								<u-grid-item>
									<u-upload v-if="fileList2.length" :fileList="fileList2" @delete="deletePic" name="2"
										width="150"></u-upload>
									<image v-else src="/static/images/home/rxm.png" mode="widthFix"
										style="width: 150px;" @click="handleOcr(2, 'idCard')"></image>
									<image v-if="fileList2.length == 0" src="../static/images/take.png"
										class="icon-image" @click="handleOcr(2, 'idCard')"></image>
								</u-grid-item>
							</u-grid>
						</view>
					</u-form-item>
					<u-form-item label="身份证号" :required="true" prop="idCard" borderBottom>
						<u--input v-model="registerForm.idCard" type="idcard" border="none" placeholder="请输入身份证号"></u--input>
					</u-form-item>
					<u-form-item label="姓名" :required="true" prop="name" borderBottom>
						<u--input v-model="registerForm.name" border="none" placeholder="请输入姓名"></u--input>
					</u-form-item>
					<u-form-item label="手机号" :required="true" prop="phone" borderBottom>
						<u-input v-model="registerForm.phone" type="number" border="none" placeholder="请输入手机号" @change="handleChange2">
							<!-- <template slot="suffix">
								<view @click="getVerification" style="color: #fd6601;" v-if="getTime">获取验证码</view>
								<view class="login-code-img" style="color: #fd6601;" v-else>已发送({{ time }})</view>
							</template> -->
						</u-input>
					</u-form-item>
					<!-- <u-form-item label="验证码" :required="true" prop="captcha" borderBottom>
						<u--input v-model="registerForm.captcha" border="none" placeholder="请输入验证码"></u--input>
					</u-form-item> -->
				</uni-card>
				<view class="fenge"></view>
				<uni-card :is-shadow="false" is-full style="border: none;">
					<u-form-item label="上传营业执照" :required="true"></u-form-item>
					<view style="color: #f56c6c;" v-if="cardStatus2">请上传营业执照</view>
					<u-form-item label=" " borderBottom>
						<view class="image" style="position: relative;">
							<u-upload v-if="fileList3.length" :fileList="fileList3" @delete="deletePic" name="3"
								width="150"></u-upload>
							<image v-else src="/static/images/home/yyzz.png" mode="widthFix" style="width: 150px;"
								@click="handleOcr(3, 'business')"></image>
							<image v-if="fileList3.length == 0" src="../static/images/take.png" class="icon-image"
								style="left: 75px;" @click="handleOcr(3, 'business')"></image>
						</view>
					</u-form-item>
					<u-form-item label="营业执照号" :required="true" prop="taxNum" borderBottom>
						<u--input v-model="registerForm.taxNum" border="none" placeholder="请输入营业执照号"></u--input>
					</u-form-item>
					<u-form-item label="公司名称" :required="true" prop="businessName" borderBottom>
						<u--input v-model="registerForm.businessName" disabledColor="#ffffff" placeholder="请输入公司名称"
							border="none"></u--input>
					</u-form-item>
					<u-form-item label="法定代表人" :required="true" prop="legal_representative" borderBottom>
						<u--input v-model="registerForm.legal_representative" disabledColor="#ffffff"
							placeholder="请输入法定代表人" border="none"></u--input>
					</u-form-item>
					<u-form-item label="市场所在地" :required="true" prop="marketLocationValue" borderBottom
						@click="showSex = true">
						<u--input v-model="registerForm.marketLocationValue" disabled disabledColor="#ffffff"
							placeholder="请选择市场场地编号" border="none"></u--input>
						<u-icon slot="right" name="arrow-right"></u-icon>
					</u-form-item>
					<u-form-item label="联系地址" :required="true" prop="address" borderBottom>
						<u--input v-model="registerForm.address" border="none" placeholder="请输入联系地址"></u--input>
					</u-form-item>
					<u-form-item label="开户行" :required="true" prop="bankName" borderBottom>
						<u--input v-model="registerForm.bankName" border="none" placeholder="请输入开户行"></u--input>
					</u-form-item>
					<u-form-item label="对公银行账号" :required="true" prop="bankAccount" borderBottom>
						<u--input v-model="registerForm.bankAccount" type="number" border="none" placeholder="请输入对公银行账号"
							@change="handleChange"></u--input>
					</u-form-item>
					<u-form-item label="保证金充值卡号" :required="true" prop="bondBankAccount" borderBottom>
						<u--input v-model="registerForm.bondBankAccount" type="number" border="none" placeholder="请输入保证金充值卡号"
							@change="handleChange1"></u--input>
						<view slot="right" name="arrow-right">
							<u-icon name="error-circle" @click="handleCircle"></u-icon>
						</view>
					</u-form-item>
					<!-- <u-form-item label="输入密码" :required="true" prop="password" borderBottom>
						<u--input v-model="registerForm.password" password border="none" placeholder="请输入8-32位(数字+字母)">
						</u--input>
					</u-form-item>
					<u-form-item label="再次输入密码" :required="true" prop="confirmPassword" borderBottom>
						<u--input v-model="registerForm.confirmPassword" password border="none"
							placeholder="请输入8-32位(数字+字母)"></u--input>
					</u-form-item> -->
				</uni-card>
			</u--form>
			<u-picker :show="showSex" :columns="range" keyName="name" title="请选择市场所在地" @confirm="confirm"
				@cancel="cancel"></u-picker>
		</view>
		<view class="footer">
			<!-- 底部按钮 -->
			<u-grid col="2">
				<u-grid-item>
					<button @click="handleCancel" class="button" style="background-color: #fff;color: #000;">取消</button>
				</u-grid-item>
				<u-grid-item>
					<button @click="handleSave" class="button">提交审核</button>
				</u-grid-item>
			</u-grid>
		</view>
		<!-- 遮罩层 -->
		<u-overlay :show="showOverlay"></u-overlay>
	</view>
</template>

<script>
	const bankLenght = [8, 9, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 23, 25, 26, 27]
	import config from '@/config'
	import {
		urlTobase64
	} from '@/utils/ruoyi.js'
	import {
		showConfirm
	} from '@/utils/common'
	import {
		register,
		getTenantlist,
		getCode,
		getIdCard,
		getBusinessLicense,
		deleteImage
	} from '@/api/register'
	import {
		setCreate
	} from '@/api/home'
	export default {
		data() {
			return {
				showOverlay: false,
				showModal: false,
				content: "万国二手车平台需要收集您的身份证号及银行账号用于验证您身份真实性，是否同意授权。",
				title: "注册账号",
				cardStatus1: false,
				cardStatus2: false,
				getTime: true,
				time: 60,
				timer: null,
				// 身份证
				fileList1: [],
				fileList2: [],
				// 营业执照
				fileList3: [],
				registerForm: {
					phone: "", // 手机号
					captcha: "", // 验证码
					name: "", // 姓名
					idCard: "", // 身份证号
					idCardUrl: [], // 身份证图片
					businessLicense: [], // 营业执照
					taxNum: "", // 营业执照号
					businessName: "", // 公司名称
					legal_representative: "", // 法定代表人
					marketLocation: "", // 市场所在地id
					marketLocationValue: "", // 市场所在地
					address: "", // 联系地址
					bankName: "", // 开户行
					bankAccount: "", // 对公银行账号
					bondBankAccount: "", // 保证金充值卡号
					password: "", // 密码
					confirmPassword: "" // 确认密码
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
							let str = value.replace(/\s*/g, "")
							if (!iphoneReg.test(str)) {
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
							if (!iphoneReg.test(value)) {
								return false;
							}
						},
						message: "身份证格式不正确",
						trigger: ['blur', 'change']
					}],
					taxNum: {
						type: 'string',
						required: true,
						message: '请填写营业执照号',
						trigger: ['blur', 'change']
					},
					businessName: {
						type: 'string',
						required: true,
						message: '请填写公司名称',
						trigger: ['blur', 'change']
					},
					legal_representative: {
						type: 'string',
						required: true,
						message: '请填写法定代表人',
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
					}, {
						validator: (rule, value, callback) => {
							const str = value.replace(/\s*/g, "")
							if (bankLenght.includes((str.length))) {
								return true
							} else {
								return false
							}
						},
						type: 'string',
						required: true,
						message: '请填写正确的银行卡号',
						trigger: ['blur', 'change']
					}],
					bankName: {
						type: 'string',
						required: true,
						message: '请填写开户行',
						trigger: ['blur', 'change']
					},
					address: {
						type: 'string',
						required: true,
						message: '请填写联系地址',
						trigger: ['blur', 'change']
					},
					bondBankAccount: [{
						type: 'string',
						required: true,
						message: '请填写保证金充值卡号',
						trigger: ['blur', 'change']
					}, {
						validator: (rule, value, callback) => {
							const str = value.replace(/\s*/g, "")
							if (bankLenght.includes((str.length))) {
								return true
							} else {
								return false
							}
						},
						type: 'string',
						required: true,
						message: '请填写正确的卡号',
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
							if (this.registerForm.confirmPassword && value != this.registerForm
								.confirmPassword) {
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
				date: null,
			}
		},
		onReady() {
			//onReady 为uni-app支持的生命周期之一
			this.$refs.valiForm.setRules(this.rules)
		},
		onLoad() {
			// this.showModal = true;
			this.date = uni.$u.timeFormat(Number(new Date()), 'yyyymmdd');
			// 获取手机号
			// #ifdef MP-WEIXIN
			if (this.$store.state.user.phone) {
				let phone = this.$store.state.user.phone;
				if (phone.length > 3 && phone.length < 8) {
					phone = phone.replace(/\s/g, '').replace(/[^\d]/g, '').replace(/^(\d{3})/g, '$1 ')
				} else if (phone.length >= 8) {
					phone = phone.replace(/\s/g, '').replace(/[^\d]/g, '').replace(/^(\d{3})(\d{4})/g, '$1 $2 ')
				}
				this.registerForm.phone = phone;
			}
			// #endif
			// 查询市场所在地
			getTenantlist().then((res) => {
				this.range.push(res.data)
				this.registerForm.marketLocation = res.data[0].id;
				this.registerForm.marketLocationValue = res.data[0].name;
			})
		},
		methods: {
			back() {
				// #ifndef MP-WEIXIN
				clearInterval(this.timer);
				this.$tab.reLaunch('/pages/login')
				// #endif
				// #ifdef MP-WEIXIN
				this.$tab.reLaunch('/pages/wx_login')
				// #endif
			},
			handleConfirm() {
				this.showModal = false;
			},
			handleChange(data) {
				let account = data.replace(/\s/g, '').replace(/[^\d]/g, '').replace(/(\d{4})(?=\d)/g, '$1 ')
				this.$set(this.registerForm, 'bankAccount', account)
			},
			handleChange1(data) {
				let account = data.replace(/\s/g, '').replace(/[^\d]/g, '').replace(/(\d{4})(?=\d)/g, '$1 ')
				this.$set(this.registerForm, 'bondBankAccount', account)
			},
			handleChange2(data) {
				let phone = '';
				if (data.length > 3 && data.length < 8) {
					phone = data.replace(/\s/g, '').replace(/[^\d]/g, '').replace(/^(\d{3})/g, '$1 ')
				} else if (data.length >= 8) {
					phone = data.replace(/\s/g, '').replace(/[^\d]/g, '').replace(/^(\d{3})(\d{4})/g, '$1 $2 ')
				}
				this.$set(this.registerForm, 'phone', phone)
			},
			handleCircle() {
				uni.showModal({
					title: '提示',
					showCancel: false,
					content: '提供的浦发银行个人卡号实名认证需与身份证一致。',
					confirmText: '知道了',
					confirmColor: '#fa6401'
				})
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
					// title: "选择类型",
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
					count: 1,
					sizeType: ['original', 'compressed'], //可以指定是原图还是压缩图，默认二者都有
					sourceType: tapIndex == 0 ? ['album'] : ['camera'], // 从相册选择 : 使用相机
					success: async function(res) {
						res.tempFilePaths.forEach((item) => {
							_this[`fileList${index}`].push({
								url: item,
								status: 'uploading',
								message: '上传中'
							})
						})
						if (index == 1 || index == 2) {
							// 识别身份证
							_this.registerForm.idCardUrl = [..._this.registerForm.idCardUrl, ..._this[
								`fileList${index}`]];
							for (let i = 0; i < res.tempFilePaths.length; i++) {
								let str = await urlTobase64(res.tempFilePaths[i]);
								getIdCard({
									IDCardUrl: str
								}).then((ress) => {
									let data = JSON.parse(ress.data);
									if (data.idcard_number_type == -1) {
										_this.$modal.msg("请上传正确且清晰的身份证照照片");
										_this[`fileList${index}`] = [];
									} else {
										if (data.words_result['公民身份号码']) {
											_this.registerForm.idCard = data.words_result['公民身份号码']
												.words;
											_this.registerForm.name = data.words_result['姓名'].words;
										}
										if (data.words_result['失效日期']) {
											if (_this.date > data.words_result['失效日期'].words) {
												showConfirm("您的身份证已过期，请您处理后再进行注册。").then(res => {
													_this.handleCancel();
													return;
												})
											}
										}
										if (i == res.tempFilePaths.length - 1) {
											_this.upload(res, type, index);
										}
									}
								}).catch((error) => {
									_this[`fileList${index}`] = [];
								})
							}
						} else if (index == 3) {
							// 识别营业执照
							_this.registerForm.businessLicense = _this[`fileList${index}`];
							for (let i = 0; i < res.tempFilePaths.length; i++) {
								let str = await urlTobase64(res.tempFilePaths[i]);
								getBusinessLicense({
									businessLicense: str
								}).then((ress) => {
									let data = JSON.parse(ress.data);
									if (data.error_msg) {
										_this.$modal.msg("请上传正确且清晰的营业执照照片");
										_this[`fileList${index}`] = [];
									} else {
										if (data.words_result['单位名称']) {
											_this.registerForm.businessName = data.words_result['单位名称']
												.words;
											_this.registerForm.taxNum = data.words_result['社会信用代码']
												.words;
											_this.registerForm.legal_representative = data
												.words_result['法人'].words;
											_this.registerForm.address = data.words_result['地址'].words;
										}
										if (i == res.tempFilePaths.length - 1) {
											_this.upload(res, type, index);
										}
									}
								}).catch((error) => {
									_this[`fileList${index}`] = [];
									_this.registerForm.businessLicense = [];
								})
							}
						}
					}
				})
			},
			upload(res, type, index) {
				let _this = this;
				for (let i = 0; i < res.tempFilePaths.length; i++) {
					// 图片压缩
					uni.compressImage({
						src: res.tempFilePaths[i],
						compressedWidth: 120,
						success: (r) => {
							// 上传
							uni.uploadFile({
								url: config.uploadUrl, // 仅为示例，非真实的接口地址
								// #ifdef H5
								file: res.tempFiles[i],
								// #endif
								// #ifdef MP-WEIXIN || APP
								filePath: r.tempFilePath,
								// #endif
								name: 'file',
								formData: {
									type: type
								},
								success: (ress) => {
									setTimeout(() => {
										let fileListLen = 0;
										let data = JSON.parse(ress.data).data;
										if (data) {
											for (let i = 0; i < data.length; i++) {
												let item = _this[`fileList${index}`][fileListLen]
												_this[`fileList${index}`].splice(fileListLen, 1, Object.assign(
													item, {
														status: 'success',
														message: '',
														url: data[i].url,
														id: data[i].id,
														path: data[i].path
													}))
												fileListLen++;
											}
										} else {
											_this.$modal.msg("上传失败");
											_this[`fileList${index}`] = [];
											if (index == 1) {
												_this.registerForm.idCardUrl = [];
											} else if (index == 2) {
												_this.registerForm.businessLicense = [];
											}
										}
									}, 1000);
								}
							});
						},
						fail: (f) => {
							_this.$modal.msg("图片压缩失败");
						}
					});
				}
			},
			// 删除图片
			deletePic(event) {
				deleteImage({
					id: event.file.id
				}).then((res) => {
					this.$modal.msg("删除成功");
					this[`fileList${event.name}`].splice(event.index, 1);
					if (event.name == 3) {
						this.registerForm.businessLicense = [];
						this.registerForm.businessName = '';
						this.registerForm.taxNum = '';
						this.registerForm.legal_representative = '';
						this.registerForm.address = '';
					}
				})
			},
			// 选择框确定
			confirm(val) {
				this.registerForm.marketLocation = val.value[0].id;
				this.registerForm.marketLocationValue = val.value[0].name;
				this.showSex = false;
			},
			// 选择框取消
			cancel() {
				this.showSex = false;
			},
			// 提交审核
			handleSave() {
				let list = [...this.fileList1, ...this.fileList2];
				if (list.length != 2) {
					this.cardStatus1 = true;
				} else {
					this.cardStatus1 = false;
				}
				if (this.fileList3.length == 0) {
					this.cardStatus2 = true;
				} else {
					this.cardStatus2 = false;
				}
				this.$refs.valiForm.validate().then(res => {
					if (this.registerForm.password != this.registerForm.confirmPassword) {
						this.$modal.msgError("两次密码不一致");
						return;
					}
					if (this.cardStatus1) {
						return
					}
					if (this.cardStatus2) {
						return
					}
					// 提交审核
					let data = {
						phone: this.registerForm.phone.replace(/\s*/g, ""),
						// captcha: this.registerForm.captcha,
						name: this.registerForm.name,
						idCard: this.registerForm.idCard,
						idCardUrl: list.map((item) => {
							return item.id
						}),
						taxNum: this.registerForm.taxNum,
						businessLicense: this.fileList3.map((item) => {
							return item.id
						}),
						marketLocation: this.registerForm.marketLocation,
						marketLocationValue: this.registerForm.marketLocationValue,
						address: this.registerForm.address,
						bankNumber: this.registerForm.bankAccount.replace(/\s*/g, ""),
						businessName: this.registerForm.businessName,
						legal_representative: this.registerForm.legal_representative,
						bankName: this.registerForm.bankName,
						bondBankAccount: this.registerForm.bondBankAccount.replace(/\s*/g, ""),
					}
					if (data.idCardUrl.length != 2) {
						this.$modal.msg("需要上传两张图片");
						return
					}
					this.$modal.loading("提交中，请耐心等待...")
					this.showOverlay = true;
					register(data).then((res) => {
						//  发起流程
						data.idCardUrl = list;
						data.businessLicense = this.fileList3;
						let procDefKey = "ZHSQ";
						let variables = {
							marketName: res.data.marketName,
							merchantName: res.data.merchantName,
							startUserId: res.data.startUserId,
							formDataJson: {
								formMain: {
									merchantId: res.data.thirdId,
									thirdId: res.data.thirdId,
									formDataJson: data
								}
							}
						}
						let createData = {
							procDefKey,
							variables
						};
						setCreate(createData).then((ress) => {
							this.$modal.closeLoading()
							this.showOverlay = false;
							let _this = this;
							uni.showModal({
								title: '提示',
								showCancel: false,
								content: '您的注册信息已提交市场方进行审核，审核通过后将短信通知您。',
								confirmText: '知道了',
								confirmColor: '#fa6401',
								success: function(res) {
									if (res.confirm) {
										// #ifndef MP-WEIXIN
										clearInterval(this.timer);
										_this.$tab.reLaunch('/pages/login')
										// #endif
										// #ifdef MP-WEIXIN
										_this.$tab.reLaunch('/pages/wx_login')
										// #endif
									}
								}
							})
						}).catch((error) => {
							// this.$modal.msgError(error.msg);
							this.showOverlay = false;
						})
					}).catch((error) => {
						// this.$modal.msgError(error.msg);
						this.showOverlay = false;
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

	.register {
		border-top: 1px solid #f3f3f3;
		padding-bottom: 80px;
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

	.fenge {
		height: 20px;
		background-color: #fafafa;
	}

	.footer {
		width: 100%;
		position: fixed;
		bottom: 0;
		background-color: #fff;
		padding-bottom: 10px;
		z-index: 999;

		.button {
			width: 80%;
			margin-top: 10px;
			background-color: #fa6401;
			color: #fff;
		}
	}

	.image {
		width: 100%;
	}

	/deep/ .image .u-upload__button {
		display: none;
	}
</style>