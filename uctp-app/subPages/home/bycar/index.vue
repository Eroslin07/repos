<template>
	<view class="by-car">
		<!-- 自定义导航栏 -->
		<!-- <u-navbar title="我要收车" leftText="返回" @leftClick="back" safeAreaInsetTop fixed placeholder></u-navbar> -->
		<u-grid col="2" :border="true" style="margin-top: 10px;">
			<u-grid-item>
				<u-icon
					:customStyle="{paddingTop:20+'rpx'}"
					name="level"
					:color="active == 0 ? '#fd6601' : ''"
					:size="30"
				></u-icon>
				<text class="grid-text" :style="{'color': active == 0 ? '#fd6601' : ''}">车辆信息</text>
			</u-grid-item>
			<u-grid-item>
				<u-icon
					:customStyle="{paddingTop:20+'rpx'}"
					name="level"
					:color="active == 1 ? '#fd6601' : ''"
					:size="30"
				></u-icon>
				<text class="grid-text" :style="{'color': active == 1 ? '#fd6601' : ''}">卖家信息</text>
			</u-grid-item>
		</u-grid>
		<uni-card>
			<!-- 车辆信息 -->
			<view v-if="vehicleInfor">
				<u--form
					labelPosition="left"
					:model="carForm"
					:rules="carRules"
					ref="carForm"
					labelWidth="120px"
				>
					<view style="color: #A6A6A6;position: relative;margin: 0 0 0 26rpx;">
						<view style="position: absolute;top: 3rpx;height: 30rpx;border: 5rpx solid #fa6400;left: -23rpx;"></view>
						<view class="text">车辆基础信息</view>
					</view>
					<u-form-item label="上传车辆图片" :required="true" prop="carUrl" borderBottom>
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
							<image src="../../../static/images/home/camera.png" class="form-image" @click="handleOcr(2)"></image>
						</view>
					</u-form-item>
					<u-form-item label="上传行驶证" :required="true" prop="drivingLicenseUrl" borderBottom>
						<view class="image">
							<u-upload
								v-if="fileList1.length"
								:fileList="fileList1"
								@delete="deletePic"
								name="1"
								width="70"
								height="70"
							></u-upload>
						</view>
						<view slot="right" name="arrow-right">
							<image src="../../../static/images/home/camera.png" class="form-image" @click="handleOcr(1)"></image>
						</view>
					</u-form-item>
					<u-form-item label="发动机编号" :required="true" prop="engineNum" borderBottom>
						<u--input v-model="carForm.engineNum" border="none" placeholder="请输入发动机编号"></u--input>
					</u-form-item>
					<u-form-item label="车架号(VIN)" :required="true" prop="vin" borderBottom>
						<u--input v-model="carForm.vin" border="none" placeholder="请输入17位车架号(VIN)"></u--input>
					</u-form-item>
					<u-form-item label="使用性质" :required="true" prop="natureOfOperat" borderBottom>
						<u--input v-model="carForm.natureOfOperat" border="none" placeholder="请输入使用性质"></u--input>
					</u-form-item>
					<u-form-item label="首次登记日期" :required="true" prop="firstRegistDate" borderBottom @click="showDate = true">
						<u--input
							v-model="carForm.firstRegistDate"
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
					<u-form-item label="车牌号" :required="true" prop="licensePlateNum" borderBottom>
						<u--input v-model="carForm.licensePlateNum" border="none" placeholder="请输入车牌号"></u--input>
					</u-form-item>
					<u-form-item label="上传机动车登记证书" :required="true" prop="certificateUrl" borderBottom>
						<view class="image">
							<u-upload
								v-if="fileList3.length"
								:fileList="fileList3"
								@delete="deletePic"
								name="3"
								width="70"
								height="70"
							></u-upload>
						</view>
						<view slot="right" name="arrow-right">
							<image src="../../../static/images/home/camera.png" class="form-image" @click="handleOcr(3)"></image>
						</view>
					</u-form-item>
					<u-form-item label="登记证号" :required="true" prop="licensePlateNum" borderBottom>
						<u--input v-model="carForm.licensePlateNum" border="none" placeholder="请输入登记证号"></u--input>
					</u-form-item>
					<u-form-item label="颜色" :required="true" prop="licensePlateNum" borderBottom>
						<u--input v-model="carForm.licensePlateNum" border="none" placeholder="请输入颜色"></u--input>
					</u-form-item>
					<u-form-item label="里程数" :required="true" prop="mileage" borderBottom>
						<u-input v-model="carForm.mileage" border="none" placeholder="请输入里程数">
							<template slot="suffix">
								<view style="color: #fd6601;">万公里</view>
							</template>
						</u-input>
					</u-form-item>
					<u-form-item label="品牌/车型" :required="true" prop="brand" borderBottom>
						<view @click="showModel = true">
							<u--input v-model="carForm.brand" border="none" placeholder="请输入品牌/车型"></u--input>
						</view>
						<u-icon
							slot="right"
							name="arrow-right"
						></u-icon>
					</u-form-item>
					<!-- <u-form-item label="年代" :required="true" prop="year" borderBottom>
						<u--input v-model="carForm.year" border="none" placeholder="请输入年代"></u--input>
					</u-form-item> -->
					<!-- <u-form-item label="型号" :required="true" prop="model" borderBottom>
						<u--input v-model="carForm.model" border="none" placeholder="请输入型号"></u--input>
					</u-form-item> -->
					<u-form-item label="使用年限至" prop="firstRegistDate" borderBottom @click="showDate = true">
						<u--input
							v-model="carForm.firstRegistDate"
							disabled
							disabledColor="#ffffff"
							placeholder="请选择"
							border="none"
						></u--input>
						<u-icon
							slot="right"
							name="arrow-right"
						></u-icon>
					</u-form-item>
					<u-form-item label="年检签证有效期至" prop="firstRegistDate" borderBottom @click="showDate = true">
						<u--input
							v-model="carForm.firstRegistDate"
							disabled
							disabledColor="#ffffff"
							placeholder="请选择"
							border="none"
						></u--input>
						<u-icon
							slot="right"
							name="arrow-right"
						></u-icon>
					</u-form-item>
					<u-form-item label="保险险种" prop="remarks" borderBottom>
						<u--input v-model="carForm.remarks" border="none" placeholder="请输入其他约定"></u--input>
					</u-form-item>
					<u-form-item label="保险期至" prop="firstRegistDate" borderBottom @click="showDate = true">
						<u--input
							v-model="carForm.firstRegistDate"
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
					<u-form-item label="特别约定" prop="remarks" borderBottom>
						<u--input v-model="carForm.remarks" border="none" placeholder="请输入其他约定"></u--input>
					</u-form-item>
				</u--form>
				<!-- 选择登记日期 -->
				<u-datetime-picker
					:show="showDate"
					v-model="carForm.firstRegistDate"
					mode="date"
					:formatter="formatter"
					@cancel="showDate = false"
					@confirm="handleDate"
				></u-datetime-picker>
				<u-popup v-if="showModel" :show="showModel" :customStyle="{ 'width': '240px' }" mode="right" @close="showModel = false">
					<view>
						<model-list :seriesList="seriesList" :title="'宝马'" @handleClose="handleClose" />
					</view>
				</u-popup>
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
					<view style="color: #A6A6A6;position: relative;margin: 0 0 0 26rpx;">
						<view style="position: absolute;top: 3rpx;height: 30rpx;border: 5rpx solid #fa6400;left: -23rpx;"></view>
						<view class="text">车辆价款</view>
					</view>
					<u-form-item label="收车金额" :required="true" prop="vehicleReceiptAmount" borderBottom>
						<u-input v-model="sellerForm.vehicleReceiptAmount" border="none" placeholder="请输入收车金额" @blur="handleBlur" @focus="handleFocus">
							<template slot="suffix">
								<view>元</view>
							</template>
						</u-input>
					</u-form-item>
					<view>
						<u--text style="font-size:12px;" prefixIcon="info-circle" iconStyle="font-size: 16px; color: #e26e1f"
							text="保证金可用余额150000元" color="#e26e1f"></u--text>
					</view>
					<view>
						<u--text style="font-size:12px;" prefixIcon="info-circle" iconStyle="font-size: 16px; color: #e26e1f"
							text="公允值范围：13.19万元-15.20万元" color="#e26e1f"></u--text>
					</view>
					<u-form-item label="付款方式" :required="true" prop="payType" borderBottom>
						<u-radio-group
							v-model="sellerForm.payType"
							placement="row"
							activeColor="#fd6404"
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
					<view style="color: #A6A6A6;position: relative;margin: 0 0 0 26rpx;">
						<view style="position: absolute;top: 3rpx;height: 30rpx;border: 5rpx solid #fa6400;left: -23rpx;"></view>
						<view class="text">卖家信息</view>
					</view>
					<u-form-item label="是否第三方代收" :required="true" prop="collection" borderBottom>
						<u-radio-group v-model="sellerForm.collection" activeColor="#fd6404">
							<u-radio shape="circle" label="否" :name="0"></u-radio>
							<u-radio shape="circle" label="是" :name="1"></u-radio>
						</u-radio-group>
					</u-form-item>
					<u-form-item label="身份证号" :required="true" prop="sellerIdCard" borderBottom>
						<u--input v-model="sellerForm.sellerIdCard" border="none" placeholder="请输入身份证号"></u--input>
						<view slot="right" name="arrow-right">
							<image src="../../../static/images/home/camera.png" class="form-image" @click="handleOcr(4)"></image>
						</view>
					</u-form-item>
					<u-form-item label=" " borderBottom v-if="fileList4.length != 0">
						<view class="image">
							<u-upload
								v-if="fileList4.length"
								:fileList="fileList4"
								@delete="deletePic"
								name="4"
								width="70"
								height="70"
							></u-upload>
						</view>
					</u-form-item>
					<u-form-item label="姓名" :required="true" prop="sellerName" borderBottom>
						<u--input v-model="sellerForm.sellerName" border="none" placeholder="请输入姓名">
						</u--input>
					</u-form-item>
					<u-form-item label="电话" :required="true" prop="sellerTel" borderBottom>
						<u--input v-model="sellerForm.sellerTel" border="none" placeholder="请输入11位手机号"></u--input>
					</u-form-item>
					<u-form-item label="第三方姓名" :required="true" prop="thirdSellerName" borderBottom v-if="sellerForm.collection == 1">
						<u--input v-model="sellerForm.thirdSellerName" border="none" placeholder="请输入第三方姓名"></u--input>
					</u-form-item>
					<u-form-item
						label="收款方式"
						:required="true"
						prop="remitType"
						borderBottom
						@click="showSex = true"
					>
						<u--input
							v-model="sellerForm.remitType"
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
					<u-form-item label="开户行" :required="true" prop="bankName" borderBottom>
						<u--input v-model="sellerForm.bankName" border="none" placeholder="请输入开户行"></u--input>
					</u-form-item>
					<u-form-item label="银行卡号" :required="true" prop="bankCard" borderBottom v-if="sellerForm.collection == 0">
						<u--input v-model="sellerForm.bankCard" border="none" placeholder="请输入银行卡号" @change="handleChange"></u--input>
					</u-form-item>
					<u-form-item label="第三方银行卡号" :required="true" prop="thirdBankCard" borderBottom v-if="sellerForm.collection == 1">
						<u--input v-model="sellerForm.thirdBankCard" border="none" placeholder="请输入银行卡号" @change="handleChange"></u--input>
					</u-form-item>
				</u--form>
				<view style="margin: 20px 0;">
					<u--text style="font-size:12px;" prefixIcon="info-circle" iconStyle="font-size: 16px; color: #e26e1f"
						text="注意:在发起委托合同前，请检查您的相关信息，发起委托合同时会将信息自动带到后方合同作为重要信息使用。" color="#e26e1f"></u--text>
				</view>
				<!-- 收款方式选项 -->
				<u-picker :show="showSex" :columns="range" keyName="label" title="请选择收款方式" @confirm="confirm" @cancel="cancel"></u-picker>
			</view>
		</uni-card>
		<view class="footer">
			<!-- 底部按钮 -->
			<u-grid col="2">
				<u-grid-item>
					<button @click="handleStep" class="button" v-if="vehicleInfor">下一步</button>
					<button @click="handleEntrust" class="button" v-if="sellerInfor">确认发起</button>
				</u-grid-item>
				<u-grid-item>
					<button @click="handleDraft" class="button" v-if="vehicleInfor">保存</button>
					<button @click="handleSubmit" class="button" v-if="sellerInfor">保存</button>
				</u-grid-item>
			</u-grid>
		</view>
		<!-- 遮罩层 -->
		<u-overlay :show="showOverlay">
			<view class="warp">
				<u-loading-icon text="草稿保存中..." textSize="18" color="#fd6601" text-color="#fd6601"></u-loading-icon>
			</view>
		</u-overlay>
	</view>
</template>

<script>
	import config from '@/config'
	import { getAccessToken } from '@/utils/auth'
	import { urlTobase64 } from '@/utils/ruoyi.js'
	import modelList from '@/subPages/home/bycar/modelList.vue'
	import { getIdCard, deleteImage } from '@/api/register'
	import {
		getVehicleLicense,
		setCarInfo,
		setSellerInfo,
		getFairValue,
		getCarSeriesList,
		getCarBrandList
	} from '@/api/home/bycar.js'
	export default {
		components: {
			modelList
		},
		data() {
			return {
				vehicleInfor: true,
				sellerInfor: false,
				active: 0,
				// 行驶证信息
				fileList1: [],
				// 车辆图片信息
				fileList2: [],
				// 机动证书信息
				fileList3: [],
				// 身份证
				fileList4: [],
				// 车辆信息
				carForm: {
					drivingLicenseUrl: [],
					certificateUrl: [],
					carUrl: [],
					vin: '',
					natureOfOperat: '',
					firstRegistDate: uni.$u.timeFormat(Number(new Date()), 'yyyy-mm-dd'),
					licensePlateNum: '',
					engineNum: '',
					brand: '',
					// year: '',
					model: '',
					remarks: '',
					mileage: '',
				},
				// 车辆信息校验规则
				carRules: {
					drivingLicenseUrl: {
						type: 'array',
						required: true,
						message: '请上传传行驶证',
						trigger: ['blur', 'change']
					},
					certificateUrl: {
						type: 'array',
						required: true,
						message: '请上传机动车登记证书',
						trigger: ['blur', 'change']
					},
					carUrl: {
						type: 'array',
						required: true,
						message: '请上传车辆图片',
						trigger: ['blur', 'change']
					},
					vin: {
						type: 'string',
						required: true,
						message: '请填写车架号',
						trigger: ['blur', 'change']
					},
					natureOfOperat: {
						type: 'string',
						required: true,
						message: '请填写使用性质',
						trigger: ['blur', 'change']
					},
					engineNum: {
						type: 'string',
						required: true,
						message: '请填写发动机编号',
						trigger: ['blur', 'change']
					},
					brand: {
						type: 'string',
						required: true,
						message: '请填写品牌',
						trigger: ['blur', 'change']
					},
					year: {
						type: 'string',
						required: true,
						message: '请填写年代',
						trigger: ['blur', 'change']
					},
					model: {
						type: 'string',
						required: true,
						message: '请填写型号',
						trigger: ['blur', 'change']
					},
					mileage: {
						type: 'string',
						required: true,
						message: '请填写里程数',
						trigger: ['blur', 'change']
					},
					firstRegistDate: {
						type: 'string',
						required: true,
						message: '请选择登记日期',
						trigger: ['blur', 'change']
					},
					licensePlateNum: {
						type: 'string',
						required: true,
						message: '请填写车牌号',
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
				// 是否弹出品牌
				showModel: false,
				// 车系
				seriesList: [],
				// 卖家信息
				sellerForm: {
					vehicleReceiptAmount: '',
					payType: 0,
					collection: 0,
					sellerIdCard: '',
					sellerIdCardUrl: [],
					sellerName: '',
					thirdSellerName: '',
					sellerTel: '',
					remitType: '转账',
					bankName: '',
					bankCard: '',
					thirdBankCard: '',
				},
				// 卖家信息校验规则
				sellerRules: {
					vehicleReceiptAmount: {
						type: 'string',
						required: true,
						message: '请填写收车金额',
						trigger: ['blur', 'change']
					},
					payType: {
						type: 'number',
						required: true,
						message: '请选择付款方式',
						trigger: ['blur', 'change']
					},
					sellerIdCard: [{
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
					sellerName: {
						type: 'string',
						required: true,
						message: '请填写姓名',
						trigger: ['blur', 'change']
					},
					sellerTel: [{
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
					remitType: {
						type: 'string',
						required: true,
						message: '请选择收款方式',
						trigger: ['blur', 'change']
					},
					bankName: {
						type: 'string',
						required: true,
						message: '请填写开户行',
						trigger: ['blur', 'change']
					},
					bankCard: [{
						type: 'string',
						required: true,
						message: '请填写银行卡号',
						trigger: ['blur', 'change']
					}, {
						pattern: /^(\d{4}\s){3}\d{4}$|^(\d{4}\s){4}\d{3}$/,
						type: 'string',
						required: true,
						message: '请填写正确的银行卡号',
						trigger: ['blur', 'change']
					}],
					thirdBankCard: [{
						type: 'string',
						required: true,
						message: '请填写第三方收款人银行卡号',
						trigger: ['blur', 'change']
					}, {
						pattern: /^(\d{4}\s){3}\d{4}$|^(\d{4}\s){4}\d{3}$/,
						type: 'string',
						required: true,
						message: '请填写正确的银行卡号',
						trigger: ['blur', 'change']
					}],
					thirdSellerName: {
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
				showOverlay: false,
				carId: null,
				modelId: null,
				modelName: null
			}
		},
		onBackPress(options) {
			if (this.active == 0) {
				this.handleSaveCar();
			} else if (this.active == 1) {
				this.vehicleInfor = true;
				this.sellerInfor = false;
				this.active = 0;
			}
			return true;
		},
		mounted() {
			// this.carBrandList()
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
			handleChange(data) {
				let account = data.replace(/\s/g, '').replace(/[^\d]/g, '').replace(/(\d{4})(?=\d)/g, '$1 ');
				if (this.sellerForm.collection == 0) {
					this.$set(this.sellerForm, 'bankCard', account);
				} else if (this.sellerForm.collection == 1) {
					this.$set(this.sellerForm, 'thirdBankCard', account);
				}
			},
			// 失去焦点
			handleBlur(val) {
				let amount = this.$amount.getComdify(val);
				this.$set(this.sellerForm, 'vehicleReceiptAmount', amount);
			},
			// 聚焦
			handleFocus() {
				let amount = this.$amount.getDelcommafy(this.sellerForm.vehicleReceiptAmount);
				this.$set(this.sellerForm, 'vehicleReceiptAmount', amount);
			},
			// 点击OCR
			handleOcr(index) {
				let _this = this;
				uni.showActionSheet({
					title: "选择类型",
					itemList: ['相册', '拍摄'],
					success: async function(res) {
						_this.chooseImages(index, res.tapIndex);
					}
				})
			},
			// 上传图片
			chooseImages(index, tapIndex) {
				let _this = this;
				uni.chooseImage({
					// count: 1,
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
						if (index == 1) {
							// 识别行驶证
							_this.carForm.drivingLicenseUrl = _this[`fileList${index}`];
							for (let i = 0; i < res.tempFilePaths.length; i++) {
								let str = await urlTobase64(res.tempFilePaths[i]);
								getVehicleLicense({ vehicleLicense: str }).then((ress) => {
									let data = JSON.parse(ress.data);
									if (data.words_result['发动机号码']) {
										_this.carForm.engineNum = data.words_result['发动机号码'].words;
										_this.carForm.vin = data.words_result['车辆识别代号'].words;
										_this.carForm.licensePlateNum = data.words_result['号牌号码'].words;
										_this.carForm.natureOfOperat = data.words_result['使用性质'].words;
										_this.carForm.brand = data.words_result['品牌型号'].words.slice(0, data.words_result['品牌型号'].words.indexOf('牌'));
										_this.carForm.model = data.words_result['品牌型号'].words.slice(data.words_result['品牌型号'].words.indexOf('牌') + 1);
										// _this.carForm.firstRegistDate = data.words_result['注册日期'].words;
										_this.carForm.firstRegistDate = '2019-02-20';
										// 根据品牌查询id
										_this.carBrandList();
									}
									if (i == res.tempFilePaths.length - 1) {
										_this.upload(res, index);
									}
								})
							}
						} else if (index == 2) {
							// 识别车辆图片
							_this.carForm.carUrl = _this[`fileList${index}`];
							_this.upload(res, index);
						} else if (index == 3) {
							// 识别机动车登记证书
							_this.carForm.certificateUrl = _this[`fileList${index}`];
							_this.upload(res, index);
						} else if (index == 4) {
							// 识别身份证
							_this.sellerForm.sellerIdCardUrl = _this[`fileList${index}`];
							for (let i = 0; i < res.tempFilePaths.length; i++) {
								let str = await urlTobase64(res.tempFilePaths[i]);
								getIdCard({ IDCardUrl: str }).then((ress) => {
									let data = JSON.parse(ress.data);
									if (data.words_result['公民身份号码']) {
										_this.sellerForm.sellerIdCard = data.words_result['公民身份号码'].words;
										_this.sellerForm.sellerName = data.words_result['姓名'].words;
									}
									if (i == res.tempFilePaths.length - 1) {
										_this.upload(res, index);
									}
								})
							}
						}
					}
				})
			},
			upload(res, index) {
				let _this = this;
				for (let i = 0; i < res.tempFilePaths.length; i++) {
					uni.uploadFile({
						url: config.uploadUrl, // 仅为示例，非真实的接口地址
						// #ifdef H5
						file: res.tempFiles[i],
						// #endif
						// #ifdef MP-WEIXIN || APP
						filePath: res.tempFilePaths[i],
						// #endif
						name: 'file',
						header: {
							Authorization: 'Bearer ' + getAccessToken()
						},
						success: (ress) => {
							setTimeout(() => {
								let fileListLen = 0;
								let data = JSON.parse(ress.data).data;
								if (data) {
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
								} else {
									_this.$modal.msg("上传失败");
									_this[`fileList${index}`] = [];
									if (index == 1) {
										_this.carForm.drivingLicenseUrl = [];
									} else if (index == 2) {
										_this.carForm.carUrl = [];
									} else if (index == 3) {
										_this.carForm.certificateUrl = [];
									} else if (index == 4) {
										_this.sellerForm.sellerIdCardUrl = [];
									}
								}
							}, 1000)
						}
					});
				}
			},
			// 删除图片
			deletePic(event) {
				deleteImage(event.file.id).then((res) => {
					this.$modal.msg("删除成功");
					this[`fileList${event.name}`].splice(event.index, 1);
				})
			},
			// 查询品牌id
			carBrandList() {
				let data = {
					// brand_name: this.carForm.brand
					brand_name: '宝马'
				}
				getCarBrandList(data).then((res) => {
					// 根据品牌id查询车系
					this.carSeriesList(res.brand_id);
				})
			},
			// 查询车系
			carSeriesList(id) {
				let data = {
					brand_id: id
				}
				this.seriesList = [];
				getCarSeriesList(data).then((res) => {
					this.seriesList = res.series_list;
				})
			},
			// 关闭选择车型
			handleClose(val) {
				if (val) {
					this.modelId = val.model_id;
					this.modelName = val.model_name;
				}
				this.showModel = false;
			},
			// 确认登记日期
			handleDate(e) {
				this.$nextTick(() => {
					const timeFormat = uni.$u.timeFormat;
					this.carForm.firstRegistDate = timeFormat(e.value, 'yyyy-mm-dd');
					this.showDate = false;
				})
			},
			// 下一步
			handleStep() {
				if (this.modelId) {
					this.$refs.carForm.validate().then(res => {
						if (this.carId) {
							this.vehicleInfor = false;
							this.sellerInfor = true;
							this.active = 1;
							return;
						}
						this.handleDraft('step');
					})
				} else {
					this.$modal.msg("请选择车型");
				}
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
			handleDraft(val) {
				this.showOverlay = true;
				let data = {
					carUrl: this.fileList2.map((item) => { return item.id }),
					drivingLicenseUrl: this.fileList1.map((item) => { return item.id }),
					engineNum: this.carForm.engineNum,
					vin: this.carForm.vin,
					natureOfOperat: this.carForm.natureOfOperat,
					firstRegistDate: this.carForm.firstRegistDate,
					plateNum: this.carForm.licensePlateNum,
					certificateUrl: this.fileList3.map((item) => { return item.id }),
					mileage: this.carForm.mileage,
					brand: this.carForm.brand,
					// year: this.carForm.year,
					model: this.carForm.model,
					remarks: this.carForm.remarks
				}
				setCarInfo(data).then((res) => {
					this.showOverlay = false;
					if (val == 'step') {
						// 保存车辆信息并进行下一步
						this.carId = res.data;
						this.vehicleInfor = false;
						this.sellerInfor = true;
						this.active = 1;
						this.fairValue();
					} else {
						// 保存车辆草稿信息返回首页
						this.$modal.msg("保存草稿成功");
						this.$tab.reLaunch('/pages/index');
					}
				})
			},
			// 查询公允价值
			fairValue() {
				let data = {
					carId: this.carId,
					modelId: this.modelId
				}
				getFairValue(data).then((res) => {
					console.log(res)
				})
			},
			// 收款方式选择框确定
			confirm(val) {
				this.sellerForm.remitType = val.value[0].label;
				this.showSex = false;
			},
			// 收款方式选择框取消
			cancel() {
				this.showSex = false;
			},
			// 点击发起委托合同
			handleEntrust() {
				this.$refs.sellerForm.validate().then(res => {
					this.handleSubmit('entrust');
				})
			},
			// 点击卖家信息保存
			handleSubmit(val) {
				this.showOverlay = true;
				let data = {
					id: this.carId,
					vehicleReceiptAmount: this.sellerForm.vehicleReceiptAmount,
					collection: this.sellerForm.collection,
					payType: this.sellerForm.payType,
					sellerIdCard: this.sellerForm.sellerIdCard,
					idCardUrl: this.fileList4.map((item) => { return item.id }),
					sellerName: this.sellerForm.sellerName,
					thirdSellerName: this.sellerForm.collection == 1 ? this.sellerForm.thirdSellerName : null,
					sellerTel: this.sellerForm.sellerTel,
					remitType: this.sellerForm.remitType,
					bankName: this.sellerForm.bankName,
					bankCard: this.sellerForm.collection == 0 ? this.sellerForm.bankCard : null,
					thirdBankCard: this.sellerForm.collection == 1 ? this.sellerForm.thirdBankCard : null,
				}
				setSellerInfo(data).then((res) => {
					this.showOverlay = false;
					if (val == 'entrust') {
						// 保存卖家信息并确认发起
						this.$tab.navigateTo('/subPages/home/bycar/agreement');
					} else {
						// 保存卖家草稿信息返回首页
						this.$modal.msg("保存草稿成功");
						this.$tab.reLaunch('/pages/index');
					}
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.by-car {
		border-top: 1px solid #f3f3f3;
		padding-bottom: 80px;
	}
	
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
	
	/deep/ .image .u-upload__button {
		display: none;
	}
	
	.footer {
		width: 100%;
		position: fixed;
		bottom: 0;
		background-color: #fff;
		padding-bottom: 10px;
		
		.button {
			width: 80%;
			margin-top: 10px;
			background-image: linear-gradient(to right, #fcbb2b,#ed6c21);
			background-color: #50a8bc;
			color: #fff;
		}
	}
	
	.warp {
		display: flex;
		align-items: center;
		justify-content: center;
		height: 100%;
	}
</style>