<template>
	<view class="by-car">
		<!-- 自定义导航栏 -->
		<!-- <u-navbar title="我要收车" leftText="返回" @leftClick="back" safeAreaInsetTop fixed placeholder></u-navbar> -->
		<uni-card :is-shadow="false" is-full>
			<view class="text">收车信息录入</view>
			<!-- 步骤条 -->
			<u-steps :current="active" activeColor="#50a8bc">
				<u-steps-item title="车辆信息"></u-steps-item>
				<u-steps-item title="卖家信息"></u-steps-item>
			</u-steps>
			<!-- 车辆信息 -->
			<view v-if="vehicleInfor">
				<view class="text">车辆基础信息</view>
				<u--form
					labelPosition="left"
					:model="carForm"
					:rules="carRules"
					ref="carForm"
					labelWidth="120px"
				>
					<u-form-item label="上传车辆图片" :required="true" prop="carFile" borderBottom>
						<u-upload
							:fileList="fileList3"
							name="3"
							@afterRead="afterRead"
							@delete="deletePic"
							multiple
							width="60"
							height="60"
						></u-upload>
					</u-form-item>
					<u-form-item label="上传行驶证" :required="true" prop="registerFile" borderBottom>
						<u--input v-model="carForm.registerFile" border="none" placeholder="请上传行驶证"></u--input>
						<view slot="right" name="arrow-right">
							<u-upload
								:fileList="fileList1"
								:previewImage="false"
								name="1"
								@afterRead="afterRead"
								@delete="deletePic"
								multiple
								width="60"
								height="60"
							>
								<text style="color: #50a8bc;">OCR</text>
							</u-upload>
						</view>
					</u-form-item>
					<u-form-item label="发动机编号" :required="true" prop="engineNumber" borderBottom>
						<u--input v-model="carForm.engineNumber" border="none" placeholder="请输入发动机编号"></u--input>
					</u-form-item>
					<u-form-item label="车架号(VIN)" :required="true" prop="frame" borderBottom>
						<u--input v-model="carForm.frame" border="none" placeholder="请输入17位车架号(VIN)"></u--input>
					</u-form-item>
					<u-form-item label="首次登记日期" :required="true" prop="registrDate" borderBottom @click="showDate = true">
						<u--input
							v-model="carForm.registrDate"
							disabled
							disabledColor="#ffffff"
							placeholder="请选择登记日期"
							border="none"
						></u--input>
						<u-icon
							slot="right"
							name="arrow-right"
						></u-icon>
					</u-form-item>
					<u-form-item label="上传机动车登记证书" :required="true" prop="drivingLicense" borderBottom>
						<u-upload
							:fileList="fileList2"
							name="2"
							@afterRead="afterRead"
							@delete="deletePic"
							multiple
							width="60"
							height="60"
						></u-upload>
					</u-form-item>
					<u-form-item label="里程数" :required="true" prop="mileage" borderBottom>
						<u--input v-model="carForm.mileage" border="none" placeholder="请输入里程数"></u--input>
					</u-form-item>
					<u-form-item label="品牌/年代/型号" :required="true" prop="model" borderBottom>
						<u--input v-model="carForm.model" border="none" placeholder="请输入品牌/年代/型号"></u--input>
					</u-form-item>
					<u-form-item label="特别约定" prop="otherInfor" borderBottom>
						<u--input v-model="carForm.otherInfor" border="none" placeholder="请输入其他约定"></u--input>
					</u-form-item>
					<u-form-item label="收车金额" :required="true" prop="amount" borderBottom>
						<u-input v-model="carForm.amount" border="none" placeholder="请输入收车金额">
							<template slot="suffix">
								<view>元</view>
							</template>
						</u-input>
					</u-form-item>
					<view>保证金可用余额150000元</view>
					<u-form-item label="收车方式" :required="true" prop="way" borderBottom>
						<u-radio-group
							v-model="carForm.way"
							placement="row"
							activeColor="#50a8bc"
						>
							<u-radio
								v-for="(item, index) in sexs2"
								:key="index"
								:label="item.label"
								:name="item.value"
							>
							</u-radio>
						</u-radio-group>
					</u-form-item>
				</u--form>
				<!-- 选择登记日期 -->
				<u-datetime-picker
					:show="showDate"
					v-model="carForm.registrDate"
					mode="date"
					:formatter="formatter"
					@cancel="showDate = false"
					@confirm="handleDate"
				></u-datetime-picker>
				<!-- 底部按钮 -->
				<button @click="handleStep" class="button" v-if="vehicleInfor">下一步</button>
				<button @click="handleDraft" class="button" v-if="vehicleInfor">保存</button>
			</view>
			<!-- 卖家信息 -->
			<view v-if="sellerInfor">
				<view class="text">卖家信息</view>
				<u--form
					labelPosition="left"
					:model="sellerForm"
					:rules="sellerRules"
					ref="sellerForm"
					labelWidth="120px"
				>
					<u-form-item label="是否第三方代收" :required="true" prop="collection" borderBottom>
						<u-radio-group v-model="sellerForm.collection" activeColor="#50a8bc">
							<u-radio shape="circle" label="否" :name="0"></u-radio>
							<u-radio shape="circle" label="是" :name="1"></u-radio>
						</u-radio-group>
					</u-form-item>
					<u-form-item label="身份证号" :required="true" prop="ID" borderBottom>
						<u--input v-model="sellerForm.ID" border="none" placeholder="请输入身份证号"></u--input>
						<view slot="right" name="arrow-right">
							<u-upload :fileList="fileList4" @afterRead="afterRead" @delete="deletePic" name="1" multiple
								:previewImage="false" width="60" height="60">
								<text style="color: #50a8bc;">OCR</text>
							</u-upload>
						</view>
					</u-form-item>
					<u-form-item label="姓名" :required="true" prop="name" borderBottom>
						<u--input v-model="sellerForm.name" border="none" placeholder="请输入姓名">
						</u--input>
					</u-form-item>
					<u-form-item label="电话" :required="true" prop="phone" borderBottom>
						<u--input v-model="sellerForm.phone" border="none" placeholder="请输入11位手机号"></u--input>
					</u-form-item>
					<u-form-item label="第三方姓名" :required="true" prop="thirdName" borderBottom v-if="sellerForm.collection == 1">
						<u--input v-model="sellerForm.thirdName" border="none" placeholder="请输入第三方姓名"></u--input>
					</u-form-item>
					<u-form-item
						label="收款方式"
						:required="true"
						prop="way"
						borderBottom
						@click="showSex = true"
					>
						<u--input
							v-model="sellerForm.way"
							disabled
							disabledColor="#ffffff"
							placeholder="请选择收款方式"
							border="none"
						></u--input>
						<u-icon
							slot="right"
							name="arrow-right"
						></u-icon>
					</u-form-item>
					<u-form-item label="银行卡号" :required="true" prop="cardNumber" borderBottom v-if="sellerForm.collection == 0">
						<u--input v-model="sellerForm.cardNumber" border="none" placeholder="请输入银行卡号"></u--input>
					</u-form-item>
					<u-form-item label="第三方银行卡号" :required="true" prop="thirdCardNumber" borderBottom v-if="sellerForm.collection == 1">
						<u--input v-model="sellerForm.thirdCardNumber" border="none" placeholder="请输入银行卡号"></u--input>
					</u-form-item>
				</u--form>
				<!-- 收款方式选项 -->
				<u-picker :show="showSex" :columns="range" keyName="label" title="请选择收款方式" @confirm="confirm" @cancel="cancel"></u-picker>
				<!-- 底部按钮 -->
				<button @click="handleEntrust" class="button" v-if="sellerInfor">确认发起</button>
				<button @click="handleSubmit" class="button" v-if="sellerInfor">保存</button>
			</view>
		</uni-card>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				vehicleInfor: true,
				sellerInfor: false,
				active: 0,
				// 行驶证信息
				fileList1: [],
				// 机动车登记证书信息
				fileList2: [],
				// 车辆图片信息
				fileList3: [],
				// 身份证信息
				fileList4: [],
				// 车辆信息
				carForm: {
					registerFile: "",
					drivingLicense: [],
					carFile: [],
					amount: '',
					frame: '',
					registrDate: uni.$u.timeFormat(Number(new Date()), 'yyyy-mm-dd'),
					engineNumber: '',
					model: '',
					otherInfor: '',
					mileage: '',
					way: 0
				},
				// 车辆信息校验规则
				carRules: {
					registerFile: {
						type: 'string',
						required: true,
						message: '请上传传行驶证',
						trigger: ['blur', 'change']
					},
					drivingLicense: {
						type: 'string',
						required: true,
						message: '请上传机动车登记证书',
						trigger: ['blur', 'change']
					},
					carFile: {
						type: 'string',
						required: true,
						message: '请上传车辆图片',
						trigger: ['blur', 'change']
					},
					frame: {
						type: 'string',
						required: true,
						message: '请填写车架号',
						trigger: ['blur', 'change']
					},
					engineNumber: {
						type: 'string',
						required: true,
						message: '请填写发动机编号',
						trigger: ['blur', 'change']
					},
					model: {
						type: 'string',
						required: true,
						message: '请填写品牌/年代/型号',
						trigger: ['blur', 'change']
					},
					mileage: {
						type: 'string',
						required: true,
						message: '请填写里程数',
						trigger: ['blur', 'change']
					},
					registrDate: {
						type: 'string',
						required: true,
						message: '请选择登记日期',
						trigger: ['blur', 'change']
					},
					amount: {
						type: 'number',
						required: true,
						message: '请填写收车金额',
						trigger: ['blur', 'change']
					},
					way: {
						type: 'number',
						required: true,
						message: '请选择收车方式',
						trigger: ['blur', 'change']
					}
				},
				// 收车方式
				sexs2: [{
					label: '全款',
					value: 0
				}, {
					label: '定金+尾款',
					value: 1
				}],
				// 是否弹出登记日期
				showDate: false,
				// 卖家信息
				sellerForm: {
					collection: 0,
					ID: '',
					name: '',
					phone: '',
					way: '转账',
					cardNumber: ''
				},
				// 卖家信息校验规则
				sellerRules: {
					ID: [{
						type: 'string',
						required: true,
						message: '请填写身份证号',
						trigger: ['blur', 'change']
					},{
						validator(rule, value, data, callback) {
							let iphoneReg = (
								/^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/
							);
							if(!iphoneReg.test(value)){
								return false;
							}
						},
						message:"身份证格式不正确",
						trigger: ['blur', 'change'],
					}],
					name: {
						type: 'string',
						required: true,
						message: '请填写姓名',
						trigger: ['blur', 'change']
					},
					phone: [{
						type: 'string',
						required: true,
						message: '请填写手机号',
						trigger: ['blur', 'change']
					},{
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
					way: {
						type: 'string',
						required: true,
						message: '请选择收款方式',
						trigger: ['blur', 'change']
					},
					cardNumber: {
						type: 'string',
						required: true,
						message: '请填写银行卡号',
						trigger: ['blur', 'change']
					},
					thirdCardNumber: {
						type: 'string',
						required: true,
						message: '请填写第三方收款人银行卡号',
						trigger: ['blur', 'change']
					},
					thirdName: {
						type: 'string',
						required: true,
						message: '请填写第三方收款人姓名',
						trigger: ['blur', 'change']
					}
				},
				showSex: false,
				range: [
					[{
						label: '转账',
						id: 1
					}],
				],
			}
		},
		onBackPress(options) {
			this.handleSaveCar();
			return true;
		},
		methods: {
			back() {
				uni.navigateBack({
					delta: 1
				})
			},
			formatter(type, value) {
				if (type === 'year') {
					return `${value}年`
				}
				if (type === 'month') {
					return `${value}月`
				}
				if (type === 'day') {
					return `${value}日`
				}
				return value
			},
			// 删除图片
			deletePic(event) {
				this[`fileList${event.name}`].splice(event.index, 1)
			},
			// 新增图片
			async afterRead(event) {
				let lists = [].concat(event.file);
				let fileListLen = this[`fileList${event.name}`].length;
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
							this.carForm.registerFile.push(d);
						} else if (event.name == 2) {
							this.carForm.drivingLicense.push(d);
						} else if (event.name == 3) {
							this.carForm.carFile.push(d);
						} else if (event.name == 4) {
							this.carForm.IDFile.push(d);
						}
					})
					let item = this[`fileList${event.name}`][fileListLen];
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
			// 确认登记日期
			handleDate(e) {
				this.$nextTick(() => {
					const timeFormat = uni.$u.timeFormat;
					this.carForm.registrDate = timeFormat(e.value, 'yyyy-mm-dd');
					this.showDate = false;
				})
			},
			// 下一步
			handleStep() {
				this.$refs.carForm.validate().then(res => {
					this.vehicleInfor = false;
					this.sellerInfor = true;
					this.active = 1;
				})
			},
			// 点击车辆信息保存
			handleSaveCar() {
				uni.showActionSheet({
					itemList: ['保存草稿', '放弃编辑'],
					success: (res) => {
						if (res.tapIndex == 0) {
							this.handleDraft()
						} else {
							this.handleGive()
						}
					}
				})
			},
			// 放弃编辑
			handleGive() {
				this.$tab.reLaunch('/pages/index');
			},
			// 保存车辆信息草稿
			handleDraft() {
				this.$modal.msg("保存草稿成功");
			},
			// 收款方式选择框确定
			confirm(val) {
				this.sellerForm.way = val.value[0].label;
				this.showSex = false;
			},
			// 收款方式选择框取消
			cancel() {
				this.showSex = false;
			},
			// 点击发起委托合同
			handleEntrust() {
				this.$refs.sellerForm.validate().then(res => {
					this.$tab.navigateTo('/subPages/home/bycar/agreement');
				})
			},
			// 点击卖家信息保存
			handleSubmit() {
				this.$refs.sellerForm.validate().then(res => {
					
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.uni-card--border {
		border: none;
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
	
	.button {
		margin-top: 10px;
		background-color: #50a8bc;
		color: #fff;
	}
</style>