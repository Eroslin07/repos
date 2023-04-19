<template>
	<view class="by-car">
		<!-- 自定义导航栏 -->
		<!-- <u-navbar title="我要收车" leftText="返回" @leftClick="back" safeAreaInsetTop fixed placeholder></u-navbar> -->
		<u-grid col="2" :border="true" style="margin-top: 10px;">
			<u-grid-item>
				<image v-show="active == 0" src="../../../static/images/bycar/car.png" class="form-image"></image>
				<image v-show="active == 1" src="../../../static/images/bycar/car1.png" class="form-image"></image>
				<text class="grid-text" :style="{'color': active == 0 ? '#fd6601' : ''}">车辆信息</text>
			</u-grid-item>
			<u-grid-item>
				<image v-show="active == 0" src="../../../static/images/bycar/car3.png" class="form-image"></image>
				<image v-show="active == 1" src="../../../static/images/bycar/car2.png" class="form-image"></image>
				<text class="grid-text" :style="{'color': active == 1 ? '#fd6601' : ''}">交易信息</text>
			</u-grid-item>
		</u-grid>
		<uni-card :is-shadow="false" is-full style="border: none;">
			<!-- 车辆信息 -->
			<view v-if="vehicleInfor">
				<u--form labelPosition="left" :model="carForm" :rules="carRules" ref="carForm" labelWidth="120px">
					<view style="color: #A6A6A6;position: relative;margin: 0 0 0 26rpx;">
						<view
							style="position: absolute;top: 3rpx;height: 30rpx;border: 5rpx solid #fa6400;left: -23rpx;">
						</view>
						<view class="text">车辆基础信息</view>
					</view>
					<u-form-item label="上传车辆图片">
					</u-form-item>
					<u-form-item borderBottom prop="carUrl">
						<view class="image">
							<u-grid col="2">
								<u-grid-item>
									<u-upload v-if="fileList2.length" :fileList="fileList2" @delete="deletePic" name="2"
										width="150"></u-upload>
									<image v-else src=".../../../static/images/bycar/headstock.png" mode="widthFix"
										style="width: 150px;" @click="handleOcr(2)"></image>
									<image v-if="fileList2.length == 0" src="../../../static/images/take.png"
										class="icon-image" @click="handleOcr(2)"></image>
								</u-grid-item>
								<u-grid-item>
									<u-upload v-if="fileList5.length" :fileList="fileList5" @delete="deletePic" name="5"
										width="150"></u-upload>
									<image v-else src=".../../../static/images/bycar/tailstock.png" mode="widthFix"
										style="width: 150px;" @click="handleOcr(5)"></image>
									<image v-if="fileList5.length == 0" src="../../../static/images/take.png"
										class="icon-image" @click="handleOcr(5)"></image>
								</u-grid-item>
								<u-grid-item>
									<u-upload v-if="fileList6.length" :fileList="fileList6" @delete="deletePic" name="6"
										width="150"></u-upload>
									<image v-else src=".../../../static/images/bycar/car_left.png" mode="widthFix"
										style="width: 150px;" @click="handleOcr(6)"></image>
									<image v-if="fileList6.length == 0" src="../../../static/images/take.png"
										class="icon-image" @click="handleOcr(6)"></image>
								</u-grid-item>
								<u-grid-item>
									<u-upload v-if="fileList7.length" :fileList="fileList7" @delete="deletePic" name="7"
										width="150"></u-upload>
									<image v-else src=".../../../static/images/bycar/car_right.png" mode="widthFix"
										style="width: 150px;" @click="handleOcr(7)"></image>
									<image v-if="fileList7.length == 0" src="../../../static/images/take.png"
										class="icon-image" @click="handleOcr(7)"></image>
								</u-grid-item>
							</u-grid>
						</view>
					</u-form-item>
					<u-form-item label="上传行驶证" :required="true">
					</u-form-item>
					<u-form-item label=" " borderBottom prop="drivingLicenseUrl">
						<view class="image" style="position: relative;">
							<u-upload v-if="fileList1.length" :fileList="fileList1" @delete="deletePic" name="1"
								width="150"></u-upload>
							<image v-else src=".../../../static/images/bycar/business.png" mode="widthFix"
								style="width: 150px;" @click="handleOcr(1)"></image>
							<image v-if="fileList1.length == 0" src="../../../static/images/take.png" class="icon-image"
								style="left: 75px;" @click="handleOcr(1)"></image>
						</view>
					</u-form-item>
					<u-form-item label="发动机编号" :required="true" prop="engineNum" borderBottom>
						<u--input v-model="carForm.engineNum" border="none" placeholder="请输入发动机编号"></u--input>
					</u-form-item>
					<u-form-item label="车架号(VIN)" :required="true" prop="vin" borderBottom>
						<u--input v-model="carForm.vin" border="none" placeholder="请输入17位车架号(VIN)"></u--input>
					</u-form-item>
					<u-form-item label="首次登记日期" :required="true" prop="firstRegistDate" borderBottom
						@click="getDate(carForm.firstRegistDate, 1)">
						<u--input v-model="carForm.firstRegistDate" disabled disabledColor="#ffffff"
							placeholder="请选择登记日期" border="none"></u--input>
						<u-icon slot="right" name="arrow-right"></u-icon>
					</u-form-item>
					<u-form-item label="车牌号" :required="true" prop="licensePlateNum" borderBottom>
						<u--input v-model="carForm.licensePlateNum" border="none" placeholder="请输入车牌号"></u--input>
					</u-form-item>
					<u-form-item label="使用性质" :required="true" prop="natureOfOperat" borderBottom>
						<u--input v-model="carForm.natureOfOperat" border="none" placeholder="请输入使用性质"></u--input>
					</u-form-item>
					<u-form-item label="车辆类型" :required="true" prop="carType" borderBottom>
						<u--input v-model="carForm.carType" border="none" placeholder="请输入车辆类型"></u--input>
					</u-form-item>
					<u-form-item label="品牌型号" :required="true" prop="brandType" borderBottom>
						<u--input v-model="carForm.brandType" border="none" placeholder="请输入品牌型号"></u--input>
					</u-form-item>
					<u-form-item label="品牌/车型" :required="true" prop="model" borderBottom>
						<view @click="showModel = true">
							<u--input v-model="carForm.model" border="none" placeholder="请输入品牌/车系/车型"></u--input>
						</view>
						<u-icon slot="right" name="arrow-right"></u-icon>
					</u-form-item>
					<u-form-item label="上传机动车登记证书" :required="true" labelWidth="150px">
					</u-form-item>
					<u-form-item label=" " borderBottom prop="certificateUrl">
						<view class="image" style="position: relative;">
							<u-upload v-if="fileList3.length" :fileList="fileList3" @delete="deletePic" name="3"
								width="150"></u-upload>
							<image v-else src=".../../../static/images/bycar/registration.png" mode="widthFix"
								style="width: 150px;" @click="handleOcr(3)"></image>
							<image v-if="fileList3.length == 0" src="../../../static/images/take.png" class="icon-image"
								style="left: 75px;" @click="handleOcr(3)"></image>
						</view>
					</u-form-item>
					<u-form-item label="登记证号" :required="true" prop="certificateNo" borderBottom>
						<u--input v-model="carForm.certificateNo" border="none" placeholder="请输入登记证号"></u--input>
					</u-form-item>
					<u-form-item label="颜色" :required="true" prop="colour" borderBottom>
						<u--input v-model="carForm.colour" border="none" placeholder="请输入颜色"></u--input>
					</u-form-item>
					<u-form-item label="里程数" :required="true" prop="mileage" borderBottom>
						<u-input v-model="carForm.mileage" border="none" placeholder="请输入里程数">
							<template slot="suffix">
								<view style="color: #fd6601;">万公里</view>
							</template>
						</u-input>
					</u-form-item>
					<!-- <u-form-item label="年代" :required="true" prop="year" borderBottom>
						<u--input v-model="carForm.year" border="none" placeholder="请输入年代"></u--input>
					</u-form-item> -->
					<!-- <u-form-item label="型号" :required="true" prop="model" borderBottom>
						<u--input v-model="carForm.model" border="none" placeholder="请输入型号"></u--input>
					</u-form-item> -->
					<u-form-item label="使用年限至" prop="scrapDate" borderBottom @click="getDate(carForm.scrapDate, 2)">
						<u--input v-model="carForm.scrapDate" disabled disabledColor="#ffffff" placeholder="请选择"
							border="none"></u--input>
						<u-icon slot="right" name="arrow-right"></u-icon>
					</u-form-item>
					<u-form-item label="年检签证有效期" prop="annualInspectionDate" borderBottom
						@click="getDate(carForm.annualInspectionDate, 3)">
						<u--input v-model="carForm.annualInspectionDate" disabled disabledColor="#ffffff"
							placeholder="请选择" border="none"></u--input>
						<u-icon slot="right" name="arrow-right"></u-icon>
					</u-form-item>
					<u-form-item label="保险险种" prop="insurance" borderBottom>
						<u--input v-model="carForm.insurance" border="none" placeholder="请输入保险险种"></u--input>
					</u-form-item>
					<u-form-item label="保险期至" prop="insuranceEndData" borderBottom
						@click="getDate(carForm.insuranceEndData, 4)">
						<u--input v-model="carForm.insuranceEndData" disabled disabledColor="#ffffff" placeholder="请选择"
							border="none"></u--input>
						<u-icon slot="right" name="arrow-right"></u-icon>
					</u-form-item>
					<u-form-item label="特别约定" prop="remarks" borderBottom>
						<u--input v-model="carForm.remarks" border="none" placeholder="请输入其他约定"></u--input>
					</u-form-item>
				</u--form>
				<!-- 选择登记日期 -->
				<u-datetime-picker v-if="showDate" :show="showDate" v-model="showDateTime" mode="date"
					:formatter="formatter" @cancel="showDate = false" @confirm="handleDate"></u-datetime-picker>
				<u-popup v-if="showModel" :show="showModel" :customStyle="{ 'width': '240px' }" mode="right"
					@close="showModel = false">
					<view>
						<model-list :seriesList="seriesList" :title="'宝马'" @handleClose="handleClose" />
					</view>
				</u-popup>
			</view>
			<!-- 卖家信息 -->
			<view v-if="sellerInfor">
				<view class="text">卖家信息</view>
				<u--form labelPosition="left" :model="sellerForm" :rules="sellerRules" ref="sellerForm"
					labelWidth="120px">
					<view style="color: #A6A6A6;position: relative;margin: 0 0 0 26rpx;">
						<view
							style="position: absolute;top: 3rpx;height: 30rpx;border: 5rpx solid #fa6400;left: -23rpx;">
						</view>
						<view class="text">车辆价款及交易方式</view>
					</view>
					<u-form-item label="收车金额" :required="true" prop="vehicleReceiptAmount" borderBottom>
						<u-input v-model="sellerForm.vehicleReceiptAmount" border="none" placeholder="请输入收车金额"
							@blur="handleBlur" @focus="handleFocus">
							<template slot="suffix">
								<view>元</view>
							</template>
						</u-input>
					</u-form-item>
					<view>
						<u--text style="font-size:12px;" prefixIcon="info-circle"
							iconStyle="font-size: 16px; color: #e26e1f" text="保证金可用余额150000元" color="#e26e1f"></u--text>
						<view style="margin-left: 15px;color: #e26e1f;">
							公允值范围：{{fairValue.value1}}万元-{{fairValue.value2}}万元</view>
						<view style="margin-left: 15px;color: #e26e1f;" v-if="fairStatus != 0">公允价值审核-退回 ></view>
					</view>
					<u-form-item label="付款方式" :required="true" prop="payType" borderBottom>
						<u-radio-group v-model="sellerForm.payType" placement="row" activeColor="#fd6404">
							<u-radio shape="circle" label="全款" :name="0"></u-radio>
							<text style="margin: 0 5px;"></text>
							<u-radio shape="circle" label="定金+尾款" :name="1"></u-radio>
						</u-radio-group>
					</u-form-item>
					<!-- <u-form-item label="转入地车辆管理所名称" :required="true" prop="transManageName" borderBottom>
						<u--input v-model="sellerForm.transManageName" border="none" placeholder="请输入转入地车辆管理所名称"></u--input>
					</u-form-item> -->
					<view style="color: #A6A6A6;position: relative;margin: 0 0 0 26rpx;">
						<view
							style="position: absolute;top: 3rpx;height: 30rpx;border: 5rpx solid #fa6400;left: -23rpx;">
						</view>
						<view class="text">卖家信息</view>
					</view>
					<u-form-item label="是否第三方代收" :required="true" prop="collection" borderBottom>
						<u-radio-group v-model="sellerForm.collection" activeColor="#fd6404">
							<u-radio shape="circle" label="否" :name="0"></u-radio>
							<text style="margin: 0 5px;"></text>
							<u-radio shape="circle" label="是" :name="1"></u-radio>
						</u-radio-group>
					</u-form-item>
					<u-form-item label="身份证号" :required="true" prop="sellerIdCard" borderBottom>
						<u--input v-model="sellerForm.sellerIdCard" border="none" placeholder="请输入身份证号"></u--input>
					</u-form-item>
					<u-form-item borderBottom>
						<view class="image">
							<u-grid col="2">
								<u-grid-item>
									<u-upload v-if="fileList4.length" :fileList="fileList4" @delete="deletePic" name="4"
										width="150"></u-upload>
									<image v-else src=".../../../static/images/home/ghm.png" mode="widthFix"
										style="width: 150px;" @click="handleOcr(4)"></image>
									<image v-if="fileList4.length == 0" src="../../../static/images/take.png"
										class="icon-image" @click="handleOcr(4)"></image>
								</u-grid-item>
								<u-grid-item>
									<u-upload v-if="fileList8.length" :fileList="fileList8" @delete="deletePic" name="8"
										width="150"></u-upload>
									<image v-else src=".../../../static/images/home/rxm.png" mode="widthFix"
										style="width: 150px;" @click="handleOcr(8)"></image>
									<image v-if="fileList8.length == 0" src="../../../static/images/take.png"
										class="icon-image" @click="handleOcr(8)"></image>
								</u-grid-item>
							</u-grid>
						</view>
					</u-form-item>
					<u-form-item label="姓名" :required="true" prop="sellerName" borderBottom>
						<u--input v-model="sellerForm.sellerName" border="none" placeholder="请输入姓名">
						</u--input>
					</u-form-item>
					<u-form-item label="联系地址" :required="true" prop="sellerAdder" borderBottom>
						<u--input v-model="sellerForm.sellerAdder" border="none" placeholder="请输入联系地址"></u--input>
					</u-form-item>
					<u-form-item label="电话" :required="true" prop="sellerTel" borderBottom>
						<u--input v-model="sellerForm.sellerTel" border="none" placeholder="请输入11位手机号"></u--input>
					</u-form-item>
					<u-form-item label="第三方姓名" :required="true" prop="thirdSellerName" borderBottom
						v-if="sellerForm.collection == 1">
						<u--input v-model="sellerForm.thirdSellerName" border="none" placeholder="请输入第三方姓名"></u--input>
					</u-form-item>
					<u-form-item label="收款方式" :required="true" prop="remitType" borderBottom @click="showSex = true">
						<u--input v-model="sellerForm.remitType" disabled disabledColor="#ffffff" placeholder="请选择收款方式"
							border="none"></u--input>
						<u-icon slot="right" name="arrow-right"></u-icon>
					</u-form-item>
					<u-form-item label="开户行" :required="true" prop="bankName" borderBottom>
						<u--input v-model="sellerForm.bankName" border="none" placeholder="请输入开户行"></u--input>
					</u-form-item>
					<u-form-item label="银行卡号" :required="true" prop="bankCard" borderBottom
						v-if="sellerForm.collection == 0">
						<u--input v-model="sellerForm.bankCard" border="none" placeholder="请输入银行卡号"
							@change="handleChange"></u--input>
					</u-form-item>
					<u-form-item label="第三方银行卡号" :required="true" prop="thirdBankCard" borderBottom
						v-if="sellerForm.collection == 1">
						<u--input v-model="sellerForm.thirdBankCard" border="none" placeholder="请输入银行卡号"
							@change="handleChange"></u--input>
					</u-form-item>
				</u--form>
				<view style="margin: 20px 0;">
					<u--text style="font-size:12px;" prefixIcon="info-circle"
						iconStyle="font-size: 16px; color: #e26e1f"
						text="注意:在发起委托合同前，请检查您的相关信息，发起委托合同时会将信息自动带到后方合同作为重要信息使用。" color="#e26e1f"></u--text>
				</view>
				<!-- 收款方式选项 -->
				<u-picker :show="showSex" :columns="range" keyName="label" title="请选择收款方式" @confirm="confirm"
					@cancel="cancel"></u-picker>
			</view>
		</uni-card>
		<view class="fenge" v-if="vehicleInfor"></view>
		<uni-card :is-shadow="false" is-full style="border: none;">
			<view v-if="vehicleInfor">
				<view style="color: #A6A6A6;position: relative;margin: 0 0 0 26rpx;">
					<view style="position: absolute;top: 3rpx;height: 30rpx;border: 5rpx solid #fa6400;left: -23rpx;">
					</view>
					<view class="text" style="overflow: hidden;">
						<view style="float: left;margin-right: 10px;">
							<text style="color: #fa6400;">*</text>
							车辆手续及备件
						</view>
						<!-- <view style="float: left;">
							<u-checkbox-group @change="handleAll" activeColor="#fd6404">
								<u-checkbox name="true"></u-checkbox>
							</u-checkbox-group>
						</view> -->
					</view>
				</view>
				<view style="color: #f56c6c;" v-if="chebi">请选择车辆手续及备件</view>
				<u--form labelPosition="left" labelWidth="120px">
					<u-checkbox-group v-model="carForm.checkboxValue" placement="column" activeColor="#fd6404" @change="handelKey">
						<u-form-item v-for="(item, index) in checkboxList" :key="index" borderBottom>
							<u-checkbox :label="item.label" :name="item.name"></u-checkbox>
							<view style="margin-left: 10px;">
								<u--input v-model="carForm.key" :disabled="disabledKey" v-if="item.name == 'vehicleKey'"
									disabledColor="#ffffff" placeholder="请输入" border="none"></u--input>
								<u--input v-model="carForm.other" :disabled="disabledOther" v-if="item.name == 'accidentVehicle'" border="none"
									disabledColor="#ffffff" placeholder="请输入"></u--input>
							</view>
							<u--text slot="right" v-if="item.name == 'vehicleKey'" text="组">
							</u--text>
						</u-form-item>
					</u-checkbox-group>
				</u--form>
			</view>
		</uni-card>
		<view class="footer">
			<!-- 底部按钮 -->
			<u-grid col="3">
				<u-grid-item>
					<button @click="handleDelete" class="button" style="background-image: none;color: #000;">删除</button>
				</u-grid-item>
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
		<u-overlay :show="showOverlay"></u-overlay>

		<!-- 底部删除弹框 -->
		<u-modal :show="deleteModal" content='确认删除该条数据吗?' showCancelButton :closeOnClickOverlay="false"
			@cancel="deleteModal=false" @confirm="deleteSubmit"></u-modal>
	</view>
</template>

<script>
	import config from '@/config'
	import {
		getAccessToken
	} from '@/utils/auth'
	import {
		urlTobase64
	} from '@/utils/ruoyi.js'
	import modelList from '@/subPages/home/bycar/modelList.vue'
	import {
		getIdCard,
		deleteImage
	} from '@/api/register'
	import {
		setCreate
	} from '@/api/home'
	import {
		getVehicleLicense,
		setCarInfo,
		setSellerInfo,
		getFairValue,
		getCarSeriesList,
		getCarBrandList,
		getCarInfo,
		delCarInfoWithCollect
	} from '@/api/home/bycar.js'
	const dateTime = uni.$u.timeFormat(Number(new Date()), 'yyyy-mm-dd');
	export default {
		components: {
			modelList
		},
		data() {
			return {
				// 删除弹框
				deleteModal: false,
				vehicleInfor: true,
				sellerInfor: false,
				active: 0,
				// 行驶证信息
				fileList1: [],
				// 车辆图片信息
				fileList2: [],
				fileList5: [],
				fileList6: [],
				fileList7: [],
				// 机动证书信息
				fileList3: [],
				// 身份证
				fileList4: [],
				fileList8: [],
				// 车辆信息
				carForm: {
					drivingLicenseUrl: [],
					certificateUrl: [],
					carUrl: [],
					vin: '',
					natureOfOperat: '',
					carType: '',
					firstRegistDate: dateTime,
					scrapDate: dateTime,
					annualInspectionDate: dateTime,
					insuranceEndData: dateTime,
					licensePlateNum: '',
					certificateNo: '',
					colour: '',
					engineNum: '',
					brandType: '',
					model: '',
					brand: '',
					remarks: '',
					insurance: '',
					mileage: '',
					checkboxValue: [],
					key: '',
					other: '',
				},
				disabledKey: true,
				disabledOther: true,
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
					carType: {
						type: 'string',
						required: true,
						message: '请填写车辆类型',
						trigger: ['blur', 'change']
					},
					brandType: {
						type: 'string',
						required: true,
						message: '请填写品牌型号',
						trigger: ['blur', 'change']
					},
					engineNum: {
						type: 'string',
						required: true,
						message: '请填写发动机编号',
						trigger: ['blur', 'change']
					},
					model: {
						type: 'string',
						required: true,
						message: '请选择品牌/车系/车型',
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
					},
					certificateNo: {
						type: 'string',
						required: true,
						message: '请填写登记证号',
						trigger: ['blur', 'change']
					},
					colour: {
						type: 'string',
						required: true,
						message: '请填写车辆颜色',
						trigger: ['blur', 'change']
					},
				},
				chebi: false,
				// 是否弹出登记日期
				showDate: false,
				showDateTime: dateTime,
				dateStatus: 0,
				// 是否弹出品牌
				showModel: false,
				// 车系
				seriesList: [],
				// 车辆手续
				checkboxList: [
					{ label: '行驶证正、副本', name: 'drivingLicense' },
					{ label: '购车发票', name: 'carInvoice' },
					{ label: '机动车登记证', name: 'registrationCertificate' },
					{ label: '购置税完税凭证', name: 'purchaseTax' },
					{ label: '备胎', name: 'spareTire' },
					{ label: '车船使用税完税凭证', name: 'carShipTax' },
					{ label: '交强险保单', name: 'heavyTrafficInsurance' },
					{ label: '商业险保单', name: 'commercialInsurance' },
					{ label: '千斤顶', name: 'jack' },
					{ label: '说明书', name: 'specification' },
					{ label: '钥匙', name: 'vehicleKey' },
					{ label: '其他', name: 'accidentVehicle' },
				],
				// 卖家信息
				sellerForm: {
					vehicleReceiptAmount: '',
					payType: 0,
					transManageName: '',
					collection: 0,
					sellerIdCard: '',
					sellerIdCardUrl: [],
					sellerName: '',
					thirdSellerName: '',
					sellerTel: '',
					sellerAdder: '',
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
					transManageName: {
						type: 'string',
						required: true,
						message: '请填写转入地车辆管理所名称',
						trigger: ['blur', 'change']
					},
					sellerIdCard: [{
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
						trigger: ['blur', 'change'],
					}],
					sellerName: {
						type: 'string',
						required: true,
						message: '请填写姓名',
						trigger: ['blur', 'change']
					},
					sellerAdder: {
						type: 'string',
						required: true,
						message: '请填写联系地址',
						trigger: ['blur', 'change']
					},
					sellerTel: [{
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
				fairValue: {
					value1: '',
					value2: ''
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
				modelName: null,
				date: null,
				fairStatus: 0
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
			this.date = uni.$u.timeFormat(Number(new Date()), 'yyyymmdd');
			// this.carBrandList()
		},
		methods: {
			back() {
				uni.navigateBack({
					delta: 1
				})
			},
			handelKey(value) {
				if (value.length != 0) {
					this.chebi = false;
				}
				if (value.indexOf('vehicleKey') > -1) {
					this.disabledKey = false;
				} else {
					this.disabledKey = true;
					this.carForm.key = '';
				}
				if (value.indexOf('accidentVehicle') > -1) {
					this.disabledOther = false;
				} else {
					this.disabledOther = true;
					this.carForm.other = '';
				}
			},
			handleAll(value) {
				this.checkboxList.forEach((item) => {
					if (this.carForm.checkboxValue.indexOf(item) == -1) {
						this.carForm.checkboxValue.push(item.name);
						if (item.name == 'vehicleKey') {
							this.disabledKey = false;
						}
						if (item.name == 'accidentVehicle') {
							this.disabledOther = false;
						}
					}
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
					// title: "选择类型",
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
						if (index == 1) {
							// 识别行驶证
							_this.carForm.drivingLicenseUrl = _this[`fileList${index}`];
							_this.$modal.loading("行驶证识别中，请耐心等待...")
							for (let i = 0; i < res.tempFilePaths.length; i++) {
								let str = await urlTobase64(res.tempFilePaths[i]);
								getVehicleLicense({
									vehicleLicense: str
								}).then((ress) => {
									let data = JSON.parse(ress.data);
									if (data.words_result['发动机号码']) {
										let vin = data.words_result['车辆识别代号'].words;
										getCarInfo({
											VIN: vin
										}).then((result) => {
											_this.$modal.closeLoading();
											if (result.data['1']) {
												// 数据回显
												_this.carId = result.data['1'].carInfoDetails.carId;
												_this.modelId = result.data['1'].carInfo.modelId;
												_this.carForm = {
													drivingLicenseUrl: result.data['1'].fileB.length == 0 ? _this[`fileList${index}`] : result.data['1'].fileB,
													certificateUrl: result.data['1'].fileC,
													carUrl: result.data['1'].fileA,
													vin: result.data['1'].carInfo.vin,
													natureOfOperat: result.data['1'].carInfoDetails.natureOfOperat,
													carType: result.data['1'].carInfo.carType,
													firstRegistDate: result.data['1'].carInfoDetails.firstRegistDate,
													scrapDate: uni.$u.timeFormat(result.data['1'].carInfo.scrapDate,'yyyy-mm-dd'),
													annualInspectionDate: uni.$u.timeFormat(result.data['1'].carInfo.annualInspectionDate,'yyyy-mm-dd'),
													insuranceEndData: result.data['1'].carInfo.insuranceEndData,
													licensePlateNum: result.data['1'].carInfo.plateNum,
													certificateNo: result.data['1'].carInfoDetails.certificateNo,
													colour: result.data['1'].carInfoDetails.colour,
													engineNum: result.data['1'].carInfo.engineNum,
													brandType: result.data['1'].carInfo.brandType,
													model: result.data['1'].carInfo.model,
													brand: result.data['1'].carInfo.brand,
													remarks: result.data['1'].carInfo.remarks,
													insurance: result.data['1'].carInfo.insurance,
													mileage: result.data['1'].carInfoDetails.mileage.toString(),
													checkboxValue: [],
													key: result.data['1'].carInfoDetails.proceduresAndSpareParts.vehicleKey,
													other: result.data['1'].carInfoDetails.proceduresAndSpareParts.accidentVehicle,
												}
												result.data['1'].fileA.forEach((item,index) => {
													if (index == 0) {
														_this.fileList2 = [item];
													} else if (index == 1) {
														_this.fileList5 = [item];
													} else if (index == 2) {
														_this.fileList6 = [item];
													} else if (index == 3) {
														_this.fileList7 = [item];
													}
												})
												if (result.data['1'].fileB.length != 0) {
													_this.fileList1 = result.data['1'].fileB;
													_this.fileList1.status = 'success';
												} else {
													if (i == res.tempFilePaths.length - 1) {
														_this.upload(res, index);
													}
												}
												_this.fileList3 = result.data['1'].fileC;
												let obj = result.data['1'].carInfoDetails.proceduresAndSpareParts
												for (let key in obj) {
													if (obj[key] == true) {
														_this.carForm.checkboxValue.push(key);
													}
													if (key == 'vehicleKey') {
														if (obj[key] != 0) {
															_this.carForm.checkboxValue.push(
																key);
														}
													}
													if (key == 'accidentVehicle') {
														if (obj[key] != '') {
															_this.carForm.checkboxValue.push(
																key);
														}
													}
												}
												// 卖家信息
												_this.sellerForm = {
													vehicleReceiptAmount: _this.$amount.getComdify(result.data['1'].carInfo.vehicleReceiptAmount),
													payType: result.data['1'].carInfoDetails.payType ? Number(result.data['1'].carInfoDetails.payType) : 0,
													transManageName: result.data['1'].carInfoDetails.transManageName,
													collection: result.data['1'].carInfoDetails.collection ?Number(result.data['1'].carInfoDetails.collection) : 0,
													sellerIdCard: result.data['1'].carInfoDetails.sellerIdCard,
													sellerIdCardUrl: result.data['1'].fileD,
													sellerName: result.data['1'].carInfoDetails.sellerName,
													thirdSellerName: result.data['1'].carInfoDetails.thirdSellerName,
													sellerTel: result.data['1'].carInfoDetails.sellerTel,
													sellerAdder: result.data['1'].carInfoDetails.sellerAdder,
													remitType: result.data['1'].carInfoDetails.remitType ? result.data['1'].carInfoDetails.remitType : '转账',
													bankName: result.data['1'].carInfoDetails.bankName,
													bankCard: result.data['1'].carInfoDetails.bankCard,
													thirdBankCard: result.data['1'].carInfoDetails.thirdBankCard,
												}
												result.data['1'].fileD.forEach((item,index) => {
													if (index == 0) {
														_this.fileList4 = [item];
													} else if (index == 1) {
														_this.fileList8 = [item];
													}
												})
											} else if (result.data['2']) {
												_this.$modal.msg("车辆已存在");
												_this[`fileList${index}`] = [];
												_this.carForm.drivingLicenseUrl = [];
											} else if (result.data['3']) {
												_this.carForm.vin = vin;
												_this.carForm.carType = data.words_result['车辆类型'].words;
												_this.carForm.engineNum = data.words_result['发动机号码'].words;
												_this.carForm.licensePlateNum = data.words_result['号牌号码'].words;
												_this.carForm.natureOfOperat = data.words_result['使用性质'].words;
												_this.carForm.model = data.words_result['品牌型号'].words.slice(0, data.words_result['品牌型号'].words.indexOf('牌'));
												_this.carForm.brand = data.words_result['品牌型号'].words.slice(0, data.words_result['品牌型号'].words.indexOf('牌'));
												_this.carForm.brandType = data.words_result['品牌型号'].words.slice(data.words_result['品牌型号'].words.indexOf('牌') + 1);
												let rdate = data.words_result['注册日期'].words;
												let y = rdate.slice(0, 4);
												let m = rdate.slice(4, 6);
												let d = rdate.slice(6);
												_this.carForm.firstRegistDate = y + '-' + m + '-' + d;

												if (i == res.tempFilePaths.length - 1) {
													_this.upload(res, index);
												}
											}
											// 根据品牌查询id
											_this.carBrandList();
										})
									}
								})
							}
						} else if (index == 2 || index == 5 || index == 6 || index == 7) {
							// 识别车辆图片
							_this.carForm.carUrl = [..._this.carForm.carUrl, ..._this[`fileList${index}`]];
							_this.upload(res, index);
						} else if (index == 3) {
							// 识别机动车登记证书
							_this.carForm.certificateUrl = _this[`fileList${index}`];
							_this.upload(res, index);
						} else if (index == 4 || index == 8) {
							// 识别身份证
							_this.sellerForm.sellerIdCardUrl = [..._this.sellerForm.sellerIdCardUrl, ..._this[
								`fileList${index}`]];
							for (let i = 0; i < res.tempFilePaths.length; i++) {
								let str = await urlTobase64(res.tempFilePaths[i]);
								getIdCard({
									IDCardUrl: str
								}).then((ress) => {
									let data = JSON.parse(ress.data);
									if (data.words_result['公民身份号码']) {
										_this.sellerForm.sellerIdCard = data.words_result['公民身份号码'].words;
										_this.sellerForm.sellerAdder = data.words_result['住址'].words;
										_this.sellerForm.sellerName = data.words_result['姓名'].words;
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
										_this[`fileList${index}`].splice(fileListLen, 1, Object.assign(
											item, {
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
				deleteImage({
					id: event.file.id
				}).then((res) => {
					this.$modal.msg("删除成功");
					this[`fileList${event.name}`].splice(event.index, 1);
				})
			},
			// 查询品牌id
			carBrandList() {
				let data = {
					brand_name: this.carForm.brand
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
					this.carForm.model = this.carForm.brand + '-' + val.model_name;
				}
				this.showModel = false;
			},
			// 点击日期下拉框
			getDate(date, i) {
				this.showDateTime = date;
				this.dateStatus = i;
				this.showDate = true;
			},
			// 确认登记日期
			handleDate(e) {
				this.$nextTick(() => {
					const timeFormat = uni.$u.timeFormat;
					if (this.dateStatus == 1) {
						this.carForm.firstRegistDate = timeFormat(e.value, 'yyyy-mm-dd');
					} else if (this.dateStatus == 2) {
						this.carForm.scrapDate = timeFormat(e.value, 'yyyy-mm-dd');
					} else if (this.dateStatus == 3) {
						this.carForm.annualInspectionDate = timeFormat(e.value, 'yyyy-mm-dd');
					} else if (this.dateStatus == 4) {
						this.carForm.insuranceEndData = timeFormat(e.value, 'yyyy-mm-dd');
					}
					this.showDate = false;
				})
			},
			// 下一步
			handleStep() {
				if (this.carForm.checkboxValue.length == 0) {
					this.chebi = true;
				} else {
					this.chebi = false;
				}
				this.$refs.carForm.validate().then(res => {
					if (this.modelId) {
						if (this.chebi == false) {
							this.getFairValue();
						}
					} else {
						this.$modal.msg("请选择车型");
					}
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
			handleDraft(val) {
				this.showOverlay = true;
				// 车辆手续及备件
				let proceduresAndSpareParts = {};
				let list1 = [];
				this.checkboxList.forEach((item) => {
					if (this.carForm.checkboxValue.indexOf(item.name) == -1) {
						list1.push(item.name);
					}
				})
				this.carForm.checkboxValue.forEach((item) => {
					proceduresAndSpareParts[item] = true;
				})
				list1.forEach((item) => {
					proceduresAndSpareParts[item] = false;
				})
				if (proceduresAndSpareParts['vehicleKey'] == true) {
					proceduresAndSpareParts['vehicleKey'] = this.carForm.key;
				} else {
					proceduresAndSpareParts['vehicleKey'] = 0;
				}
				if (proceduresAndSpareParts['accidentVehicle'] == true) {
					proceduresAndSpareParts['accidentVehicle'] = this.carForm.other;
				} else {
					proceduresAndSpareParts['accidentVehicle'] = '';
				}

				let list = [...this.fileList2, ...this.fileList5, ...this.fileList6, ...this.fileList7];
				let data = {
					id: this.carId,
					deptId: this.$store.state.user.deptId,
					tenantId: this.$store.state.user.tenantId,
					carUrl: list.map((item) => {
						return item.id
					}),
					drivingLicenseUrl: this.fileList1.map((item) => {
						return item.id
					}),
					engineNum: this.carForm.engineNum,
					vin: this.carForm.vin,
					natureOfOperat: this.carForm.natureOfOperat,
					firstRegistDate: this.carForm.firstRegistDate,
					plateNum: this.carForm.licensePlateNum,
					certificateUrl: this.fileList3.map((item) => {
						return item.id
					}),
					certificateNo: this.carForm.certificateNo,
					mileage: this.carForm.mileage,
					model: this.carForm.model,
					modelId: this.modelId,
					carType: this.carForm.carType,
					brandType: this.carForm.brandType,
					brand: this.carForm.brand,
					colour: this.carForm.colour,
					scrapDate: this.carForm.scrapDate,
					annualInspectionDate: this.carForm.annualInspectionDate,
					insuranceEndData: this.carForm.insuranceEndData,
					insurance: this.carForm.insurance,
					remarks: this.carForm.remarks,
					proceduresAndSpareParts
				}
				this.$modal.loading("提交中，请耐心等待...")
				setCarInfo(data).then((res) => {
					this.$modal.closeLoading()
					this.showOverlay = false;
					if (val == 'step') {
						// 保存车辆信息并进行下一步
						this.carId = res.data.carInfoDetails.id;
						this.vehicleInfor = false;
						this.sellerInfor = true;
						this.active = 1;
					} else {
						// 保存车辆草稿信息返回首页
						this.$modal.msg("保存草稿成功");
						this.$tab.reLaunch('/pages/index');
					}
				})
			},
			// 查询公允价值
			getFairValue() {
				let data = {
					modelId: this.modelId,
					plateNum: this.carForm.licensePlateNum,
					mileage: this.carForm.mileage,
					firstRegistDate: this.carForm.firstRegistDate
				}
				getFairValue(data).then((res) => {
					if (res.msg == 'success') {
						this.fairValue.value1 = res.Recommended_low_sold_price;
						this.fairValue.value2 = res.Recommended_high_sold_price;
						this.handleDraft('step');
					} else {
						this.$modal.msg(res.msg);
					}
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
			// 点击交易信息保存
			handleSubmit(val) {
				let _this = this;
				let amount = _this.$amount.getDelcommafy(_this.sellerForm.vehicleReceiptAmount);
				amount = amount / 10000;
				if (_this.fairValue.value1 <= amount && amount <= _this.fairValue.value2) {
					_this.saveSellerInfo(val);
				} else {
					uni.showModal({
						title: '提示',
						content: '您的收车价格偏离了市场公允价值，若是继续则会提交市场，由市场方介入审核。是否继续发起。',
						confirmText: '是',
						cancelText: '否',
						confirmColor: '#fa6401',
						success(ress) {
							if (ress.confirm) {
								_this.saveSellerInfo(val);
							}
						}
					})
				}
			},
			saveSellerInfo(val) {
				this.showOverlay = true;
				let list = [...this.fileList4, ...this.fileList8];
				let data = {
					id: this.carId,
					vehicleReceiptAmount: this.$amount.getDelcommafy(this.sellerForm.vehicleReceiptAmount),
					collection: this.sellerForm.collection,
					payType: this.sellerForm.payType,
					transManageName: this.sellerForm.transManageName,
					sellerIdCard: this.sellerForm.sellerIdCard,
					idCardUrl: list.map((item) => {
						return item.id
					}),
					sellerName: this.sellerForm.sellerName,
					thirdSellerName: this.sellerForm.collection == 1 ? this.sellerForm.thirdSellerName : null,
					sellerAdder: this.sellerForm.sellerAdder,
					sellerTel: this.sellerForm.sellerTel,
					remitType: this.sellerForm.remitType,
					bankName: this.sellerForm.bankName,
					bankCard: this.sellerForm.collection == 0 ? this.sellerForm.bankCard : null,
					thirdBankCard: this.sellerForm.collection == 1 ? this.sellerForm.thirdBankCard : null,
				}
				this.$modal.loading("提交中，请耐心等待...");
				setSellerInfo(data).then((res) => {
					if (val == 'entrust') {
						// 保存卖家信息并确认发起
						let amount = data.vehicleReceiptAmount / 10000;
						if (this.fairValue.value1 <= amount && amount <= this.fairValue.value2) {
							this.$modal.closeLoading()
							this.showOverlay = false;
							this.$tab.navigateTo('/subPages/home/bycar/agreement');
						} else {
							// 发起公允值审批流程
							let procDefKey = "SGYZ";
							res.data.fairValue = this.fairValue;
							let variables = {
								marketName: this.$store.state.user.tenantName,
								merchantName: this.$store.state.user.deptName,
								startUserId: this.$store.state.user.id,
								formDataJson: {
									formMain: {
										merchantId: res.data.carInfoDetails.carId,
										thirdId: res.data.carInfoDetails.carId,
										// formDataJson: {
										// 	carInfo: res.data.carInfo,
										// 	carInfoDetails: res.data.carInfoDetails
										// },
										formDataJson: res.data
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
								this.$modal.msg("已提交审核");
								this.$tab.reLaunch('/pages/index');
							}).catch((error) => {
								this.$modal.closeLoading()
								this.showOverlay = false;
								this.$modal.msgError("发起流程失败");
							})
						}
					} else {
						// 保存卖家草稿信息返回首页
						this.$modal.closeLoading()
						this.showOverlay = false;
						this.$modal.msg("保存草稿成功");
						this.$tab.reLaunch('/pages/index');
					}
				}).catch((error) => {
					this.showOverlay = false;
				})
			},
			// 收车删除
			handleDelete() {
				this.deleteModal = true;
			},
			// 删除确认
			async deleteSubmit() {
				this.deleteModal = false;
				if (this.carId) {
					delCarInfoWithCollect({
						id: this.carId
					}).then((res) => {
						this.$modal.msg("车辆信息已删除");
						this.$tab.switchTab('/pages/index')
					})
				} else {
					let imgArr = [...this.fileList1, ...this.fileList2, ...this.fileList3, ...this.fileList5, ...this
						.fileList6, ...this.fileList7, ...this.fileList4, ...this.fileList8
					]
					await imgArr.map((val) => {
						deleteImage({
							id: val.id
						}).then((res) => {
							this.$modal.msg("删除成功");
						})
					})

					for (let i = 1; i < 9; i++) {
						this[`fileList${i}`] = []
					}
					this.$tab.switchTab('/pages/index')
				}
			}
		}
	}
</script>

<style lang="scss" scoped>
	.by-car {
		border-top: 1px solid #f3f3f3;
		padding-bottom: 80px;
	}

	.grid-text {
		margin-top: 6px;
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

	.image {
		width: 100%;
	}

	/deep/ .image .u-upload__button {
		display: none;
	}

	/deep/ .u-grid-item {
		margin-bottom: 10px;
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

		.button {
			width: 80%;
			margin-top: 10px;
			background-image: linear-gradient(to right, #fcbb2b, #ed6c21);
			background-color: #fff;
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
