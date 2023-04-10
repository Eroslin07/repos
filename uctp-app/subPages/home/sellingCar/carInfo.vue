<template>
	<view class="selling-car">
		<!-- 自定义导航栏 -->
		<!-- <u-navbar title="我要卖车" leftText="返回" @leftClick="back" safeAreaInsetTop fixed placeholder></u-navbar> -->
		<uni-card :is-shadow="false" is-full>
			<view class="text">卖车信息录入</view>
			<!-- 步骤条 -->
			<u-steps :current="active">
				<u-steps-item title="车辆信息"></u-steps-item>
				<u-steps-item title="买家信息"></u-steps-item>
			</u-steps>
			<!-- 车辆信息 -->
			<view v-if="vehicleInfor">
				<view class="text">车辆基础信息</view>
				<view class="look-over">
					<text @click="handleCollection" style="margin-right: 5px;">查看收车合同</text>
					<text @click="handleLookEntrust" style="margin-left: 5px;">查看委托合同</text>
				</view>
				<u--form
					labelPosition="left"
					:model="carForm"
					:rules="carRules"
					ref="carForm"
					labelWidth="120px"
				>
					<u-collapse accordion>
						<u-collapse-item
							title="车辆图片"
							name="carPicList"
						>
							<text slot="value" class="u-page__item__title__slot-title" style="color: #50a8bc;">查看</text>
							<u-album :urls="carForm.carPicList" maxCount="4" rowCount="4"></u-album>
						</u-collapse-item>
						<u-collapse-item
							title="行驶证"
							name="drivingPicList"
						>
							<text slot="value" class="u-page__item__title__slot-title" style="color: #50a8bc;">查看</text>
							<u-album :urls="carForm.drivingPicList" maxCount="4" rowCount="4"></u-album>
						</u-collapse-item>
						<u-collapse-item
							title="机动车登记证书"
							name="registerPicList"
						>
							<text slot="value" class="u-page__item__title__slot-title" style="color: #50a8bc;">查看</text>
							<u-album :urls="carForm.registerPicList" maxCount="4" rowCount="4"></u-album>
						</u-collapse-item>
					</u-collapse>
					<u-form-item label="发动机编号" prop="engineNum" borderBottom>
						<u--input v-model="carForm.engineNum" border="none" placeholder="请输入发动机编号"></u--input>
					</u-form-item>
					<u-form-item label="车架号(VIN)" prop="vin" borderBottom>
						<u--input v-model="carForm.vin" border="none" placeholder="请输入17位车架号(VIN)"></u--input>
					</u-form-item>
					<u-form-item label="使用性质" prop="natureOfOperat" borderBottom>
						<u--input v-model="carForm.natureOfOperat" border="none" placeholder="请输入使用性质"></u--input>
					</u-form-item>
					<u-form-item label="首次登记日期" prop="firstRegistDate" borderBottom @click="showDate = true">
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
					<u-form-item label="车牌号" prop="plateNum" borderBottom>
						<u--input v-model="carForm.plateNum" border="none" placeholder="请输入车牌号"></u--input>
					</u-form-item>
					<u-form-item label="品牌" prop="brand" borderBottom>
						<u--input v-model="carForm.brand" border="none" placeholder="请输入品牌"></u--input>
					</u-form-item>
					<u-form-item label="年代" prop="year" borderBottom>
						<u--input v-model="carForm.year" border="none" placeholder="请输入年代"></u--input>
					</u-form-item>
					<u-form-item label="型号" prop="model" borderBottom>
						<u--input v-model="carForm.model" border="none" placeholder="请输入型号"></u--input>
					</u-form-item>
					<u-form-item label="里程数" prop="mileage" borderBottom>
						<u-input v-model="carForm.mileage" border="none" placeholder="请输入里程数">
							<template slot="suffix">
								<view>万公里</view>
							</template>
						</u-input>
					</u-form-item>
					<u-form-item label="特殊约定" prop="remarks" borderBottom>
						<u--input v-model="carForm.remarks" border="none" placeholder="请输入特殊约定"></u--input>
					</u-form-item>
					<u-form-item label="收车金额" prop="vehicleReceiptAmount" borderBottom>
						<u-input v-model="carForm.vehicleReceiptAmount" border="none" placeholder="请输入收车金额">
							<template slot="suffix"><view>元</view></template>
						</u-input>
					</u-form-item>
					<u-form-item label="卖车金额" :required="true" prop="sellAmount" borderBottom>
						<u-input v-model="carForm.sellAmount" border="none" @blur="handleBlur" placeholder="请输入卖车金额">
							<template slot="suffix"><view>元</view></template>
						</u-input>
					</u-form-item>
					<view @click="handleDetail">预计产生{{ carForm.total }}元费用，利润{{ carForm.profit }}元。明细请查看。</view>
					<u-form-item label="收款方式" :required="true" prop="sellType" borderBottom>
						<u-radio-group
							v-model="carForm.sellType"
							placement="row"
						>
							<u-radio
								:customStyle="{marginBottom: '8px'}"
								v-for="(item, index) in sexs"
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
					v-model="carForm.firstRegistDate"
					mode="date"
					:formatter="formatter"
					@cancel="showDate = false"
					@confirm="handleDate"
				></u-datetime-picker>
				<!-- 费用明细 -->
				<u-modal :show="showDetail" @confirm="showDetail = false">
					<view>
						<view>收车金额：{{ amountDetails.vehicleReceiptAmount }}元</view>
						<view>卖车金额：{{ amountDetails.sellAmount }}元</view>
						<view>过户服务费：{{ amountDetails.transferSell }}元</view>
						<view>运营服务费（卖家）：{{ amountDetails.operation }}元</view>
						<view>过户服务费（买家）：{{ amountDetails.transferBuy }}元</view>
						<view>增值税费用：{{ amountDetails.vat }}元</view>
						<view>杂税费用：{{ amountDetails.tax }}元</view>
						<view>合计费用：{{ amountDetails.total }}元</view>
						<view>利润：{{ amountDetails.profit }}元</view>
					</view>
				</u-modal>
				<!-- 底部按钮 -->
				<button @click="handleStep" class="button" v-if="vehicleInfor">下一步</button>
				<button @click="handleDraft" class="button" v-if="vehicleInfor">保存</button>
			</view>
			<!-- 买家信息 -->
			<view v-if="sellerInfor">
				<view class="text">买家信息</view>
				<u--form
					labelPosition="left"
					:model="sellerForm"
					:rules="sellerRules"
					ref="sellerForm"
					labelWidth="120px"
				>
					<u-form-item label="身份证号" :required="true" prop="buyerIdCard" borderBottom>
						<u--input v-model="sellerForm.buyerIdCard" border="none" placeholder="请输入身份证号"></u--input>
						<view slot="right" name="arrow-right">
							<text style="color: #50a8bc;" @click="handleOcr(4)">上传图片</text>
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
					<u-form-item label="姓名" :required="true" prop="buyerName" borderBottom>
						<u--input v-model="sellerForm.buyerName" border="none" placeholder="请输入姓名">
						</u--input>
					</u-form-item>
					<u-form-item label="电话" :required="true" prop="buyerTel" borderBottom>
						<u--input v-model="sellerForm.buyerTel" border="none" placeholder="请输入11位手机号"></u--input>
					</u-form-item>
				</u--form>
				<!-- 底部按钮 -->
				<button @click="handleEntrust" class="button" v-if="sellerInfor">确认发起</button>
				<button @click="handleSubmit" class="button" v-if="sellerInfor">保存</button>
			</view>
		</uni-card>
		<!-- 遮罩层 -->
		<u-overlay :show="showOverlay">
			<view class="warp">
				<u-loading-icon :text="textOverlay" textSize="18" color="#fd6601" text-color="#fd6601"></u-loading-icon>
			</view>
		</u-overlay>
	</view>
</template>

<script>
	import config from '@/config'
	import { getAccessToken } from '@/utils/auth'
	import { urlTobase64 } from '@/utils/ruoyi.js'
	import { getIdCard } from '@/api/register'
	import { getSellCarInfo, setSellCarInfo, getAmount } from '@/api/home/sellingCar.js'
	export default {
		data() {
			return {
				showOverlay: false,
				textOverlay: '',
				carId: null,
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
					registerPicList: [],
					drivingPicList: [],
					carPicList: [],
					vin: '',
					natureOfOperat: '',
					engineNum: '',
					firstRegistDate: uni.$u.timeFormat(Number(new Date()), 'yyyy-mm-dd'),
					plateNum: '',
					model: '',
					brand: '',
					year: '',
					mileage: '',
					remarks: '',
					vehicleReceiptAmount: '',
					total: '',
					profit: '',
					sellAmount: '',
					sellType: 0
				},
				// 车辆信息校验规则
				carRules: {
					drivingPicList: {
						type: 'array',
						required: true,
						message: '请上传传行驶证',
						trigger: ['blur', 'change']
					},
					registerPicList: {
						type: 'array',
						required: true,
						message: '请上传机动车登记证书',
						trigger: ['blur', 'change']
					},
					carPicList: {
						type: 'array',
						required: true,
						message: '请上传车辆图片',
						trigger: ['blur', 'change']
					},
					engineNum: {
						type: 'string',
						required: true,
						message: '请填写发动机编号',
						trigger: ['blur', 'change']
					},
					vin: {
						type: 'string',
						required: true,
						message: '请填写车架号',
						trigger: ['blur', 'change']
					},
					firstRegistDate: {
						type: 'string',
						required: true,
						message: '请选择登记日期',
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
						type: 'number',
						required: true,
						message: '请填写里程数',
						trigger: ['blur', 'change']
					},
					vehicleReceiptAmount: {
						type: 'number',
						required: true,
						message: '请填写收车金额',
						trigger: ['blur', 'change']
					},
					sellAmount: {
						type: 'number',
						required: true,
						message: '请填写卖车金额',
						trigger: ['blur', 'change']
					},
					sellType: {
						type: 'number',
						required: true,
						message: '请选择卖车方式',
						trigger: ['blur', 'change']
					}
				},
				amountDetails: {},
				// 卖车方式
				sexs: [{
					label: '全款',
					value: 0
				}, {
					label: '分期',
					value: 1
				}],
				// 是否弹出登记日期
				showDate: false,
				// 费用明细
				showDetail: false,
				// 卖家信息
				sellerForm: {
					buyerIdCard: '',
					buyerIdCardUrl: [],
					buyerName: '',
					buyerTel: ''
				},
				// 卖家信息校验规则
				sellerRules: {
					buyerIdCard: [{
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
						trigger: ['blur', 'change']
					}],
					buyerName: {
						type: 'string',
						required: true,
						message: '请填写姓名',
						trigger: ['blur', 'change']
					},
					buyerTel: [{
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
					}]
				}
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
		onLoad(options) {
			this.carId = options.id;
			this.textOverlay = '车辆详情加载中...';
			this.showOverlay = true;
			getSellCarInfo({ id: options.id }).then((res) => {
				this.carForm = res.data;
				this.carForm.sellType = 0;
				this.showOverlay = false;
				if (this.carForm.sellAmount) {
					this.handleBlur(this.carForm.sellAmount);
				}
			}).catch((error) => {
				this.$modal.msg("查询失败");
				this.$tab.navigateTo('/subPages/home/sellingCar/index');
			})
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
			// 查看收车合同
			handleCollection() {
				
			},
			// 查看委托合同
			handleLookEntrust() {
				
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
						// 识别身份证
						_this.sellerForm.buyerIdCardUrl = _this[`fileList${index}`];
						for (let i = 0; i < res.tempFilePaths.length; i++) {
							let str = await urlTobase64(res.tempFilePaths[i]);
							getIdCard({ IDCardUrl: str }).then((ress) => {
								let data = JSON.parse(ress.data);
								if (data.words_result['公民身份号码']) {
									_this.sellerForm.buyerIdCard = data.words_result['公民身份号码'].words;
									_this.sellerForm.buyerName = data.words_result['姓名'].words;
								}
								if (i == res.tempFilePaths.length - 1) {
									_this.upload(res, index);
								}
							})
						}
					}
				})
			},
			upload(res, index) {
				let _this = this;
				for (let i = 0; i < res.tempFilePaths.length; i++) {
					uni.uploadFile({
						url: config.uploadUrl, // 仅为示例，非真实的接口地址
						file: res.tempFiles[i],
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
									_this.sellerForm.buyerIdCardUrl = [];
								}
							}, 1000)
						}
					});
				}
			},
			// 删除图片
			deletePic(event) {
				this[`fileList${event.name}`].splice(event.index, 1)
			},
			// 确认登记日期
			handleDate(e) {
				this.$nextTick(() => {
					const timeFormat = uni.$u.timeFormat;
					this.carForm.firstRegistDate = timeFormat(e.value, 'yyyy-mm-dd');
					this.showDate = false;
				})
			},
			// 点击费用明细
			handleDetail() {
				this.showDetail = true;
			},
			handleBlur(val) {
				let data = {
					id: this.carId,
					sellAmount: val
				}
				getAmount(data).then((res) => {
					this.carForm.total = res.data.total;
					this.carForm.profit = res.data.profit;
					this.amountDetails = res.data;
				})
			},
			// 下一步
			handleStep() {
				this.$refs.carForm.validate().then(res => {
					this.handleDraft('step');
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
				this.$tab.navigateTo('/subPages/home/sellingCar/index');
			},
			// 保存车辆信息草稿
			handleDraft(val) {
				this.textOverlay = '保存中...';
				this.showOverlay = true;
				let data = {
					id: this.carId,
					remarks: this.carForm.remarks,
					sellAmount: this.carForm.sellAmount,
					buyerIdCard: this.sellerForm.buyerIdCard,
					idCardIds: this.fileList4.map((item) => { return item.id }),
					buyerName: this.sellerForm.buyerName,
					buyerTel: this.sellerForm.buyerTel,
					sellType: this.carForm.sellType
				}
				setSellCarInfo(data).then((res) => {
					this.showOverlay = false;
					if (val == 'step') {
						// 保存车辆信息并进行下一步
						this.vehicleInfor = false;
						this.sellerInfor = true;
						this.active = 1;
					} else if (val == 'entrust') {
						// 保存买家信息并确认发起
						this.$tab.navigateTo('/subPages/home/sellingCar/agreement');
					} else {
						// 保存车辆草稿信息返回首页
						this.$modal.msg("保存草稿成功");
						this.$tab.reLaunch('/pages/index');
					}
				})
			},
			// 点击发起委托合同
			handleEntrust() {
				this.$refs.sellerForm.validate().then(res => {
					this.handleDraft('entrust');
				})
			},
			// 点击卖家信息保存
			handleSubmit() {
				this.handleDraft();
			}
		}
	}
</script>

<style lang="scss" scoped>
	.selling-car {
		.warp {
			display: flex;
			align-items: center;
			justify-content: center;
			height: 100%;
		}
		
		/* #ifdef MP-WEIXIN */
		/deep/ .uni-card--border {
			border-bottom: none;
		}
		
		/deep/ .u-cell__body {
			padding: 10px 0;
		}
		/* #endif */
		
		.uni-card--border {
			border: none;
		}
		
		.text {
			font-size: 16px;
			color: #000;
			margin: 8px 0;
		}
		
		.look-over {
			color: #50a8bc;
			text-align: center;
		}
		
		/deep/ .u-cell__body {
			padding: 10px 0;
		}
		
		/deep/ .image .u-upload__button {
			display: none;
		}
		
		.form-item {
			margin-top: 10px;
			.uni-forms-item {
				border-bottom: 1px solid #ddd;
				uni-input {
					display: inline;
				}
			}
		}
		
		.button {
			background-color: #50a8bc;
			color: #fff;
			margin-top: 10px;
		}
	}
</style>