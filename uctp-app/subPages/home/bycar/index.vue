<template>
	<page-meta :page-style="'overflow:' + (showModel || brandShow ? 'hidden' : 'visible')"></page-meta>
	<view class="by-car">
		<!-- 自定义导航栏 -->
		<u-navbar title="我要收车" @leftClick="back" safeAreaInsetTop fixed placeholder></u-navbar>
		<view class="headers">
			<u-grid col="2" :border="true">
				<u-grid-item @click="handleCar">
					<image v-show="active == 0" src="../../../static/images/bycar/car1.svg" class="form-image"></image>
					<image v-show="active == 1" src="../../../static/images/bycar/car.svg" class="form-image"></image>
					<text class="grid-text" :style="{'color': active == 0 ? '#fd6601' : ''}">车辆信息</text>
				</u-grid-item>
				<u-grid-item @click="handleStep">
					<image v-show="active == 0" src="../../../static/images/bycar/car2.svg" class="form-image"></image>
					<image v-show="active == 1" src="../../../static/images/bycar/car3.svg" class="form-image"></image>
					<text class="grid-text" :style="{'color': active == 1 ? '#fd6601' : ''}">交易信息</text>
				</u-grid-item>
			</u-grid>
		</view>
		<view style="height: 136rpx;"></view>
		<uni-card :is-shadow="false" is-full style="border: none;">
			<!-- 车辆信息 -->
			<view v-show="vehicleInfor">
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
									<image v-else src="/static/images/bycar/headstock.png" mode="widthFix"
										style="width: 150px;" @click="handleOcr(2)"></image>
									<image v-if="fileList2.length == 0" src="../../../static/images/take.png"
										class="icon-image" @click="handleOcr(2)"></image>
								</u-grid-item>
								<u-grid-item>
									<u-upload v-if="fileList5.length" :fileList="fileList5" @delete="deletePic" name="5"
										width="150"></u-upload>
									<image v-else src="/static/images/bycar/tailstock.png" mode="widthFix"
										style="width: 150px;" @click="handleOcr(5)"></image>
									<image v-if="fileList5.length == 0" src="../../../static/images/take.png"
										class="icon-image" @click="handleOcr(5)"></image>
								</u-grid-item>
								<u-grid-item>
									<u-upload v-if="fileList6.length" :fileList="fileList6" @delete="deletePic" name="6"
										width="150"></u-upload>
									<image v-else src="/static/images/bycar/car_left.png" mode="widthFix"
										style="width: 150px;" @click="handleOcr(6)"></image>
									<image v-if="fileList6.length == 0" src="../../../static/images/take.png"
										class="icon-image" @click="handleOcr(6)"></image>
								</u-grid-item>
								<u-grid-item>
									<u-upload v-if="fileList7.length" :fileList="fileList7" @delete="deletePic" name="7"
										width="150"></u-upload>
									<image v-else src="/static/images/bycar/car_right.png" mode="widthFix"
										style="width: 150px;" @click="handleOcr(7)"></image>
									<image v-if="fileList7.length == 0" src="../../../static/images/take.png"
										class="icon-image" @click="handleOcr(7)"></image>
								</u-grid-item>
							</u-grid>
						</view>
					</u-form-item>
					<u-form-item label="上传行驶证" :required="true">
						<view class="drivingLicenseUrl"></view>
					</u-form-item>
					<u-form-item label=" " borderBottom prop="drivingLicenseUrl">
						<view class="image" style="position: relative;">
							<u-upload v-if="fileList1.length" :fileList="fileList1" @delete="deletePic" name="1"
								width="150"></u-upload>
							<image v-else src="/static/images/bycar/business.png" mode="widthFix" style="width: 150px;"
								@click="handleOcr(1)"></image>
							<image v-if="fileList1.length == 0" src="../../../static/images/take.png" class="icon-image"
								style="left: 75px;" @click="handleOcr(1)"></image>
						</view>
					</u-form-item>
					<u-form-item label="发动机编号" :required="true" prop="engineNum" borderBottom>
						<view class="engineNum">
							<u--input v-model="carForm.engineNum" border="none" placeholder="请输入发动机编号"></u--input>
						</view>
					</u-form-item>
					<u-form-item label="车架号(VIN)" :required="true" prop="vin" borderBottom>
						<view class="vin">
							<u--input v-model="carForm.vin" border="none" placeholder="请输入17位车架号(VIN)"></u--input>
						</view>
					</u-form-item>
					<u-form-item label="首次登记日期" :required="true" prop="firstRegistDate" borderBottom
						@click="getDate(carForm.firstRegistDate, 1)">
						<view class="firstRegistDate">
							<u--input v-model="carForm.firstRegistDate" disabled disabledColor="#ffffff"
								placeholder="请选择登记日期" border="none"></u--input>
						</view>
						<u-icon slot="right" name="arrow-right"></u-icon>
					</u-form-item>
					<u-form-item label="车牌号" :required="true" prop="licensePlateNum" borderBottom>
						<view class="licensePlateNum">
							<u--input v-model="carForm.licensePlateNum" border="none" placeholder="请输入车牌号"></u--input>
						</view>
					</u-form-item>
					<u-form-item label="使用性质" :required="true" prop="natureOfOperat" borderBottom>
						<view class="natureOfOperat">
							<u--input v-model="carForm.natureOfOperat" border="none" placeholder="请输入使用性质"></u--input>
						</view>
					</u-form-item>
					<u-form-item label="车辆类型" :required="true" prop="carType" borderBottom>
						<view class="carType">
							<u--input v-model="carForm.carType" border="none" placeholder="请输入车辆类型"></u--input>
						</view>
					</u-form-item>
					<u-form-item label="品牌型号" :required="true" prop="brandType" borderBottom>
						<view class="brandType">
							<u--input v-model="carForm.brandType" border="none" placeholder="请输入品牌型号"></u--input>
						</view>
					</u-form-item>
					<u-form-item label="品牌/车型" :required="true" prop="model" borderBottom>
						<view class="model">
							<!-- <u--input v-model="carForm.model" border="none" readonly placeholder="请输入品牌/车系/车型"></u--input> -->
							<u-textarea v-model="carForm.model" disabledColor="#ffffff" border="none" confirmType="done"
								placeholder="请输入品牌/车系/车型" :autoHeight="true"></u-textarea>
						</view>
						<u-icon slot="right" name="arrow-right" @click="handleShow()"></u-icon>
					</u-form-item>
					<u-form-item label="上传机动车登记证书" :required="true" labelWidth="150px">
						<view class="certificateUrl"></view>
					</u-form-item>
					<u-form-item label=" " borderBottom prop="certificateUrl">
						<view class="image" style="position: relative;">
							<u-upload v-if="fileList3.length" :fileList="fileList3" @delete="deletePic" name="3"
								width="150"></u-upload>
							<image v-else src="/static/images/bycar/registration.png" mode="widthFix"
								style="width: 150px;" @click="handleOcr(3)"></image>
							<image v-if="fileList3.length == 0" src="../../../static/images/take.png" class="icon-image"
								style="left: 75px;" @click="handleOcr(3)"></image>
						</view>
					</u-form-item>
					<u-form-item label="登记证号" :required="true" prop="certificateNo" borderBottom>
						<view class="certificateNo">
							<u--input v-model="carForm.certificateNo" type="number" border="none"
								placeholder="请输入登记证号"></u--input>
						</view>
					</u-form-item>
					<u-form-item label="颜色" :required="true" prop="colour" borderBottom>
						<view class="colour">
							<u--input v-model="carForm.colour" border="none" placeholder="请输入颜色"></u--input>
						</view>
					</u-form-item>
					<u-form-item label="里程数" :required="true" prop="mileage" borderBottom>
						<view class="mileage">
							<u-input v-model="carForm.mileage" type="number" border="none" placeholder="请输入里程数">
								<template slot="suffix">
									<view style="color: #fd6601;">万公里</view>
								</template>
							</u-input>
						</view>
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
						<u--textarea v-model="carForm.remarks" border="none" disabledColor="#ffffff"
							placeholder="请输入特别约定" maxlength="68" :count="true" confirmType="done"
							:autoHeight="true"></u--textarea>
					</u-form-item>
				</u--form>
				<!-- 选择登记日期 -->
				<u-datetime-picker v-if="showDate" :show="showDate" v-model="showDateTime" mode="date"
					:formatter="formatter" @cancel="showDate = false" @confirm="handleDate"></u-datetime-picker>
				<u-popup v-if="brandShow" :show="brandShow" safeAreaInsetTop :customStyle="{ 'width': '240px' }"
					mode="right" @close="brandShow = false">
					<u-index-list :index-list="indexList">
						<template v-for="(item, index) in barandList">
							<!-- #ifdef APP-NVUE -->
							<u-index-anchor :text="indexList[index]"></u-index-anchor>
							<!-- #endif -->
							<u-index-item>
								<!-- #ifndef APP-NVUE -->
								<u-index-anchor :text="indexList[index]"></u-index-anchor>
								<!-- #endif -->
								<view class="list-cell" v-for="(cell, index1) in item" :key="index1"
									@click="handleBrand(cell)">
									{{cell.name}}
								</view>
							</u-index-item>
						</template>
					</u-index-list>
				</u-popup>
				<u-popup v-if="showModel" :show="showModel" safeAreaInsetTop :customStyle="{ 'width': '240px' }"
					mode="right" @close="showModel = false">
					<view>
						<model-list :seriesList="seriesList" :title="carForm.brand" @handleClose="handleClose" />
					</view>
				</u-popup>
			</view>
			<!-- 卖家信息 -->
			<view v-show="sellerInfor">
				<u--form labelPosition="left" :model="sellerForm" :rules="sellerRules" ref="sellerForm"
					labelWidth="120px">
					<view style="color: #A6A6A6;position: relative;margin: 0 0 0 26rpx;">
						<view
							style="position: absolute;top: 3rpx;height: 30rpx;border: 5rpx solid #fa6400;left: -23rpx;">
						</view>
						<view class="text">车辆价款及交易方式</view>
					</view>
					<view style="height: 30rpx;padding-left: 120px;">{{amountText}}</view>
					<u-form-item label="收车金额" :required="true" prop="vehicleReceiptAmount" borderBottom>
						<u-input v-model="sellerForm.vehicleReceiptAmount" type="digit" border="none"
							placeholder="请输入收车金额" @input="handleInput" @blur="handleBlur" @focus="handleFocus">
							<template slot="suffix">
								<view>元</view>
							</template>
						</u-input>
					</u-form-item>
					<view>
						<u--text style="font-size:12px;" prefixIcon="info-circle"
							iconStyle="font-size: 16px; color: #e26e1f"
							:text="'保证金可用余额'+$amount.getComdify(available)+'元'" color="#e26e1f"></u--text>
						<view style="margin-left: 15px;color: #e26e1f;">
							公允值范围：{{fairValue.value1}}万元-{{fairValue.value2}}万元</view>
						<view style="margin-left: 15px;color: #e26e1f;" v-if="fairStatus == '不通过'">公允价值审核-退回 ></view>
					</view>
					<u-form-item label="付款方式" :required="true" prop="payType" borderBottom>
						<u-radio-group v-model="sellerForm.payType" placement="row" activeColor="#fd6404">
							<u-radio shape="circle" label="全款" :name="0"></u-radio>
							<text style="margin: 0 5px;"></text>
							<!-- <u-radio shape="circle" label="定金+尾款" :name="1"></u-radio> -->
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
						<u--input v-model="sellerForm.sellerIdCard" type="idcard" border="none"
							placeholder="请输入身份证号"></u--input>
					</u-form-item>
					<u-form-item borderBottom>
						<view class="image">
							<u-grid col="2">
								<u-grid-item>
									<u-upload v-if="fileList4.length" :fileList="fileList4" @delete="deletePic" name="4"
										width="150"></u-upload>
									<image v-else src="/static/images/home/ghm.png" mode="widthFix"
										style="width: 150px;" @click="handleOcr(4)"></image>
									<image v-if="fileList4.length == 0" src="../../../static/images/take.png"
										class="icon-image" @click="handleOcr(4)"></image>
								</u-grid-item>
								<u-grid-item>
									<u-upload v-if="fileList8.length" :fileList="fileList8" @delete="deletePic" name="8"
										width="150"></u-upload>
									<image v-else src="/static/images/home/rxm.png" mode="widthFix"
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
						<u--input v-model="sellerForm.sellerTel" type="number" border="none"
							placeholder="请输入11位手机号" @change="handleChange1"></u--input>
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
						<u--input v-model="sellerForm.bankCard" type="number" border="none" placeholder="请输入银行卡号"
							@change="handleChange"></u--input>
					</u-form-item>
					<u-form-item label="第三方银行卡号" :required="true" prop="thirdBankCard" borderBottom
						v-if="sellerForm.collection == 1">
						<u--input v-model="sellerForm.thirdBankCard" type="number" border="none" placeholder="请输入银行卡号"
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
							<text style="color: #fa6400;margin-right: 1px;">*</text>
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
					<u-checkbox-group v-model="carForm.checkboxValue" placement="column" activeColor="#fd6404"
						@change="handelKey">
						<u-form-item v-for="(item, index) in checkboxList" :key="index" borderBottom @click="handleCheckBox(item.name)">
							<u-checkbox :label="item.label" :name="item.name"></u-checkbox>
							<view style="margin-left: 10px;width: 100%" v-if="item.name == 'vehicleKey'">
								<u-input v-model="carForm.key" type="number" :disabled="disabledKey"
									disabledColor="#ffffff" placeholder="请输入" border="none">
									<template slot="suffix">
										<view>组</view>
									</template>
								</u-input>
							</view>
							<!-- <u--textarea v-model="carForm.other" :disabled="disabledOther"
								v-if="item.name == 'accidentVehicle'" border="none" disabledColor="#ffffff"
								placeholder="请输入内容" :maxlength="10" count confirmType="done"
								:autoHeight="true"></u--textarea> -->
							<input style="margin-left:20rpx" v-if="item.name == 'accidentVehicle'" type="text"
								v-model="carForm.other" placeholder="最大输入长度为10" :disabled="disabledOther"
								placeholder-style="color:#c0c4cc"
								@input="otherInput" />
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
					<button @click="handleSubmit1" class="button" v-if="sellerInfor">保存</button>
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
	const bankLenght = [8, 9, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 23, 25, 26, 27]
	import config from '@/config'
	import { getAccessToken } from '@/utils/auth'
	import { urlTobase64 } from '@/utils/ruoyi.js'
	import { getDetail } from '@/api/account/bond.js'
	import modelList from '@/subPages/home/bycar/modelList.vue'
	import { getIdCard, deleteImage } from '@/api/register'
	import { setCreate } from '@/api/home'
	import {
		getVehicleLicense,
		setCarInfo,
		setSellerInfo,
		getFairValue,
		getCarSeriesList,
		getCarBrandList,
		getCarInfo,
		delCarInfoWithCollect,
		getCarInfoDetail,
		getVehicleRegistrationCertificate,
		getAllCarBrandList
	} from '@/api/home/bycar.js'
	const dateTime = uni.$u.timeFormat(Number(new Date()), 'yyyy-mm-dd');
	export default {
		components: {
			modelList
		},
		data() {
			return {
				// 可用余额
				available: 0,
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
					scrapDate: '',
					annualInspectionDate: '',
					insuranceEndData: '',
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
				barandList: [],
				indexList: [],
				brandShow: false,
				brandStatus: true,
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
							let str = value.replace(/\s*/g, "")
							if (!iphoneReg.test(str)) {
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
					thirdBankCard: [{
						type: 'string',
						required: true,
						message: '请填写第三方收款人银行卡号',
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
					thirdSellerName: {
						type: 'string',
						required: true,
						message: '请填写第三方收款人姓名',
						trigger: ['blur', 'change']
					}
				},
				fairValue: {
					value1: 10,
					value2: 20
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
				detailId: null,
				modelId: null,
				modelName: null,
				date: null,
				fairStatus: null,
				fairVisible: 1,
				amountText: '',
			}
		},
		onReady() {
			//onReady 为uni-app支持的生命周期之一
			this.$refs.carForm.setRules(this.carRules)
			this.$refs.sellerForm.setRules(this.sellerRules)
		},
		onBackPress(options) {
			if (this.active == 0) {
				this.handleSaveCar();
			} else if (this.active == 1) {
				this.vehicleInfor = true;
				this.sellerInfor = false;
				this.active = 0;
				this.$refs.sellerForm.clearValidate();
			}
			return true;
		},
		onLoad(options) {
			if (options.id) {
				this.showOverlay = true;
				this.$modal.loading("数据加载中，请耐心等待...")
				getCarInfoDetail({ ID: options.id }).then((res) => {
					this.$modal.closeLoading()
					this.showOverlay = false;
					// 数据回显
					this.getInfo(res.data);
					// 根据品牌查询id
					this.carBrandList();
				}).catch((error) => {
					this.$modal.closeLoading()
					this.showOverlay = false;
				})
			}
		},
		mounted() {
			this.date = uni.$u.timeFormat(Number(new Date()), 'yyyymmdd');
			this.getAllBrand();
			this.getAvailableCash();
		},
		methods: {
			back() {
				if (this.active == 0) {
					this.handleSaveCar();
				} else if (this.active == 1) {
					this.vehicleInfor = true;
					this.sellerInfor = false;
					this.active = 0;
					this.$refs.sellerForm.clearValidate();
					uni.pageScrollTo({
						scrollTop: 0,
						duration: 300
					});
				}
			},
			// 点击车辆信息图片
			handleCar() {
				this.vehicleInfor = true;
				this.sellerInfor = false;
				this.active = 0;
				this.$refs.sellerForm.clearValidate();
			},
			getAvailableCash() {
				getDetail({
					accountNo: this.$store.state.user.accountNo
				}).then((res) => {
					this.available = res.data.availableCash / 100;
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
			handleChange1(data) {
				let phone = '';
				if (data.length > 3 && data.length < 8) {
					phone = data.replace(/\s/g, '').replace(/[^\d]/g, '').replace(/^(\d{3})/g, '$1 ')
				} else if (data.length >= 8) {
					phone = data.replace(/\s/g, '').replace(/[^\d]/g, '').replace(/^(\d{3})(\d{4})/g, '$1 $2 ')
				}
				this.$set(this.sellerForm, 'sellerTel', phone);
			},
			// 失去焦点
			handleBlur(val) {
				if (this.sellerForm.vehicleReceiptAmount == '') return
				let amount = this.$amount.getComdify(val);
				this.$set(this.sellerForm, 'vehicleReceiptAmount', amount);
			},
			// 聚焦
			handleFocus() {
				if (this.sellerForm.vehicleReceiptAmount == '') return
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
								getVehicleLicense({ vehicleLicense: str }).then((ress) => {
									let data = JSON.parse(ress.data);
									if (data.error_msg) {
										_this.$modal.msg("请上传正确且清晰的行驶证照片");
										_this[`fileList${index}`] = [];
									} else {
										if (data.words_result['发动机号码']) {
											let vin = data.words_result['车辆识别代号'].words;
											getCarInfo({ VIN: vin, deptId: _this.$store.state.user.deptId }).then((result) => {
												_this.$modal.closeLoading();
												if (result.data['1']) {
													// 数据回显
													_this.getInfo(result.data['1']);
													if (result.data['1'].fileB.length == 0) {
														if (i == res.tempFilePaths.length - 1) {
															_this.upload(res, index);
														}
													}
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
													if (_this.carForm.model.indexOf('汽车') > - 1) {
														_this.carForm.model = _this.carForm.model.slice(0, _this.carForm.model.indexOf('汽车'));
													}
													_this.carForm.brand = data.words_result['品牌型号'].words.slice(0, data.words_result['品牌型号'].words.indexOf('牌'));
													if (_this.carForm.brand.indexOf('汽车') > - 1) {
														_this.carForm.brand = _this.carForm.brand.slice(0, _this.carForm.brand.indexOf('汽车'));
													}
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
											}).catch((error) => {
												_this[`fileList${index}`] = [];
												_this.carForm.drivingLicenseUrl = [];
											})
										}
									}
								}).catch((error) => {
									_this[`fileList${index}`] = [];
									_this.carForm.drivingLicenseUrl = [];
								})
							}
						} else if (index == 2 || index == 5 || index == 6 || index == 7) {
							// 识别车辆图片
							_this.carForm.carUrl = [..._this.carForm.carUrl, ..._this[`fileList${index}`]];
							_this.upload(res, index);
						} else if (index == 3) {
							// 识别机动车登记证书
							_this.carForm.certificateUrl = _this[`fileList${index}`];
							// for (let i = 0; i < res.tempFilePaths.length; i++) {
							// 	let str = await urlTobase64(res.tempFilePaths[i]);
							// 	getVehicleRegistrationCertificate({ operationId: str }).then((ress) => {
							// 		let data = JSON.parse(ress.data);
							// 		if (data.error_msg) {
							// 			_this.$modal.msg("上传模板不正确，请重新上传");
							// 			_this[`fileList${index}`] = [];
							// 		} else {
							// 			if (i == res.tempFilePaths.length - 1) {
							// 				_this.upload(res, index);
							// 			}
							// 		}
							// 	})
							// }
							_this.upload(res, index);
						} else if (index == 4 || index == 8) {
							// 识别身份证
							_this.sellerForm.sellerIdCardUrl = [..._this.sellerForm.sellerIdCardUrl, ..._this[
								`fileList${index}`]];
							for (let i = 0; i < res.tempFilePaths.length; i++) {
								let str = await urlTobase64(res.tempFilePaths[i]);
								getIdCard({ IDCardUrl: str }).then((ress) => {
									let data = JSON.parse(ress.data);
									if (data.idcard_number_type == -1) {
										_this.$modal.msg("请上传正确且清晰的身份证照照片");
										_this[`fileList${index}`] = [];
									} else {
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
									}
								}).catch((error) => {
									_this[`fileList${index}`] = [];
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
				deleteImage({ id: event.file.id }).then((res) => {
					this.$modal.msg("删除成功");
					this[`fileList${event.name}`].splice(event.index, 1);
					if (event.name == 1) {
						this.carForm.vin = '';
						this.carForm.carType = '';
						this.carForm.engineNum = '';
						this.carForm.licensePlateNum = '';
						this.carForm.natureOfOperat = '';
						this.carForm.model = '';
						this.carForm.brand = '';
						this.carForm.brandType = '';
						this.carForm.firstRegistDate = dateTime;
						this.carForm.drivingLicenseUrl = [];
					}
					if (this.fileList4.length == 0 && this.fileList8.length == 0) {
						this.sellerForm.sellerIdCard = '';
						this.sellerForm.sellerAdder = '';
						this.sellerForm.sellerName = '';
						this.sellerForm.sellerIdCardUrl = [];
					}
				})
			},
			// 数据回显
			getInfo(data) {
				this.carId = data.carInfoDetails.carId;
				this.detailId = data.carInfoDetails.id;
				this.modelId = data.carInfo.modelId;
				this.carForm = {
					drivingLicenseUrl: data.fileB,
					certificateUrl: data.fileC,
					carUrl: data.fileA,
					vin: data.carInfo.vin,
					natureOfOperat: data.carInfoDetails.natureOfOperat,
					carType: data.carInfo.carType,
					firstRegistDate: data.carInfoDetails.firstRegistDate,
					scrapDate: uni.$u.timeFormat(data.carInfo.scrapDate, 'yyyy-mm-dd'),
					annualInspectionDate: uni.$u.timeFormat(data.carInfo.annualInspectionDate, 'yyyy-mm-dd'),
					insuranceEndData: data.carInfo.insuranceEndData,
					licensePlateNum: data.carInfo.plateNum,
					certificateNo: data.carInfoDetails.certificateNo,
					colour: data.carInfoDetails.colour,
					engineNum: data.carInfo.engineNum,
					brandType: data.carInfo.brandType,
					model: data.carInfo.model,
					brand: data.carInfo.brand,
					remarks: data.carInfo.remarks,
					insurance: data.carInfo.insurance,
					mileage: data.carInfoDetails.mileage ? data.carInfoDetails.mileage.toString() : '',
					checkboxValue: [],
					key: data.carInfoDetails.proceduresAndSpareParts.vehicleKey,
					other: data.carInfoDetails.proceduresAndSpareParts.accidentVehicle,
				}
				data.fileA.forEach((item, index) => {
					if (index == 0) {
						this.fileList2 = [item];
					} else if (index == 1) {
						this.fileList5 = [item];
					} else if (index == 2) {
						this.fileList6 = [item];
					} else if (index == 3) {
						this.fileList7 = [item];
					}
				})
				if (data.fileB.length != 0) {
					this.fileList1 = data.fileB;
					this.fileList1.status = 'success';
				}
				this.fileList3 = data.fileC;
				let obj = data.carInfoDetails.proceduresAndSpareParts;
				for (let key in obj) {
					if (obj[key] == true) {
						this.carForm.checkboxValue.push(key);
					}
					if (key == 'vehicleKey') {
						if (obj[key] != 0 && obj[key]) {
							this.carForm.checkboxValue.push(key);
							this.disabledKey = false;
						}
					}
					if (key == 'accidentVehicle') {
						if (obj[key] != '') {
							this.carForm.checkboxValue.push(key);
							this.disabledOther = false;
						}
					}
				}
				// 卖家信息
				const texts = ['百', '千', '万', '十万', '百万', '千万', '亿', '十亿', '百亿', '千亿']
				if (data.carInfo.vehicleReceiptAmount) {
					const vehicleReceiptAmount = data.carInfo.vehicleReceiptAmount + ''
					if (vehicleReceiptAmount.indexOf('.') > -1) {
						let arr = vehicleReceiptAmount.split('.');
						if (arr[0].length > 2) {
							this.amountText = texts[arr[0].length - 3]
						} else {
							this.amountText = ''
						}
					} else {
						if (vehicleReceiptAmount.length > 2) {
							this.amountText = texts[vehicleReceiptAmount.length - 3]
						} else {
							this.amountText = ''
						}
					}
				} else {
					this.amountText = ''
				}
				this.sellerForm = {
					vehicleReceiptAmount: this.$amount.getComdify(data.carInfo.vehicleReceiptAmount),
					payType: data.carInfoDetails.payType ? Number(data.carInfoDetails.payType) : 0,
					transManageName: data.carInfoDetails.transManageName,
					collection: data.carInfoDetails.collection ? Number(data.carInfoDetails.collection) : 0,
					sellerIdCard: data.carInfoDetails.sellerIdCard,
					sellerIdCardUrl: data.fileD,
					sellerName: data.carInfoDetails.sellerName,
					thirdSellerName: data.carInfoDetails.thirdSellerName,
					sellerTel: data.carInfoDetails.sellerTel,
					sellerAdder: data.carInfoDetails.sellerAdder,
					remitType: data.carInfoDetails.remitType ? data.carInfoDetails.remitType : '转账',
					bankName: data.carInfoDetails.bankName,
					bankCard: data.carInfoDetails.bankCard,
					thirdBankCard: data.carInfoDetails.thirdBankCard,
				}
				if (this.sellerForm.collection == 0) {
					this.handleChange(this.sellerForm.bankCard);
				} else if (this.sellerForm.collection == 1) {
					this.handleChange(this.sellerForm.thirdBankCard);
				}
				if (this.sellerForm.sellerTel) {
					this.handleChange1(this.sellerForm.sellerTel);
				}
				this.fairStatus = data.carInfo.bpmStatus;
				data.fileD.forEach((item, index) => {
					if (index == 0) {
						this.fileList4 = [item];
					} else if (index == 1) {
						this.fileList8 = [item];
					}
				})
			},
			handleShow() {
				if (this.brandStatus) {
					this.brandShow = true;
				} else {
					this.showModel = true;
				}
			},
			// 查询品牌id
			carBrandList() {
				let data = {
					brand_name: this.carForm.brand
				}
				getCarBrandList(data).then((res) => {
					if (res.brand_id) {
						// 根据品牌id查询车系
						this.carSeriesList(res.brand_id);
						this.brandStatus = false;
						this.brandShow = false;
					} else {
						this.brandStatus = true;
					}
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
			// 查询所有品牌
			getAllBrand() {
				this.barandList = [];
				this.indexList = [];
				getAllCarBrandList().then((res) => {
					res.forEach((item) => {
						if (this.indexList.indexOf(item.initial) == -1) {
							this.indexList.push(item.initial);
						}
					})
					this.indexList.forEach((i, index) => {
						let list = res.filter((m) => {
							return m.initial == i
						});
						let arr = [];
						list.forEach((l) => {
							arr.push({
								name: l.brand_name,
								id: l.brand_id
							});
						})
						if (index == this.indexList.length - 1) {
							for (let j = 0; j < 2; j++) {
								arr.push({
									name: '',
									id: null
								})
							}
						}
						this.barandList.push(arr);
					})
				})
			},
			handleBrand(val) {
				this.carForm.brand = val.name;
				// 根据品牌id查询车系
				let data1 = {
					brand_id: val.id
				}
				this.seriesList = [];
				getCarSeriesList(data1).then((res) => {
					this.seriesList = res.series_list;
					this.showModel = true;
					this.brandShow = false;
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
				this.brandShow = false;
			},
			// 点击日期下拉框
			getDate(date, i) {
				if (!date) {
					this.$nextTick(() => {
						this.showDateTime = dateTime;
					})
				}
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
					if (this.chebi == false) {
						this.handleDraft('step');
					}
					// if (this.modelId) {
					// 	if (this.chebi == false) {
					// 		this.getFairValue();
					// 	}
					// } else {
					// 	this.$modal.msg("请选择车型");
					// }
				}).catch((error) => {
					let key = '.' + error[0].field;
					const query = uni.createSelectorQuery()
					query.select(key).boundingClientRect((data) => {
						let pageScrollTop = Math.round(data.top)
						uni.pageScrollTo({
							scrollTop: pageScrollTop - 70, //滚动的距离
							duration: 300, //过渡时间
						})
					}).exec()
				})
			},
			// 点击车辆信息保存
			handleSaveCar() {
				let _this = this;
				uni.showActionSheet({
					itemList: ['保存草稿', '放弃编辑'],
					success: (res) => {
						if (res.tapIndex == 0) {
							_this.handleDraft()
						} else {
							_this.handleGive()
						}
					}
				})
			},
			// 放弃编辑
			handleGive() {
				this.$tab.switchTab('/pages/index');
			},
			// 保存车辆信息草稿
			handleDraft(val) {
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
					if (this.carForm.key == '' || !this.carForm.key) {
						this.$modal.msg("请输入钥匙");
						return
					}
					proceduresAndSpareParts['vehicleKey'] = this.carForm.key;
				} else {
					proceduresAndSpareParts['vehicleKey'] = '';
				}
				if (proceduresAndSpareParts['accidentVehicle'] == true) {
					if (this.carForm.other == '' || !this.carForm.other) {
						this.$modal.msg("请输入其他内容");
						return
					}
					proceduresAndSpareParts['accidentVehicle'] = this.carForm.other;
				} else {
					proceduresAndSpareParts['accidentVehicle'] = '';
				}

				let list = [...this.fileList2, ...this.fileList5, ...this.fileList6, ...this.fileList7];
				let data = {
					id: this.carId,
					deptId: this.$store.state.user.deptId,
					tenantId: this.$store.state.user.tenantId,
					carUrl: list.map((item) => { return item.id }),
					drivingLicenseUrl: this.fileList1.map((item) => { return item.id }),
					engineNum: this.carForm.engineNum,
					vin: this.carForm.vin,
					natureOfOperat: this.carForm.natureOfOperat,
					firstRegistDate: this.carForm.firstRegistDate,
					plateNum: this.carForm.licensePlateNum,
					certificateUrl: this.fileList3.map((item) => { return item.id }),
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
				this.showOverlay = true;
				this.$modal.loading("提交中，请耐心等待...")
				setCarInfo(data).then((res) => {
					this.$modal.closeLoading()
					this.showOverlay = false;
					if (val == 'step') {
						// 保存车辆信息并进行下一步
						this.detailId = res.data.carInfoDetails.id;
						this.vehicleInfor = false;
						this.sellerInfor = true;
						this.active = 1;
						this.$refs.carForm.clearValidate();
						uni.pageScrollTo({
							scrollTop: 0,
							duration: 300
						});
					} else {
						// 保存车辆草稿信息返回首页
						this.$modal.msg("保存草稿成功");
						this.$tab.switchTab('/pages/index');
					}
				}).catch((error) => {
					this.$modal.closeLoading()
					this.showOverlay = false;
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
			handleSubmit(val) {
				let _this = this;
				let amount = _this.$amount.getDelcommafy(_this.sellerForm.vehicleReceiptAmount);
				amount = amount / 10000;
				if (_this.fairValue.value1 <= amount && amount <= _this.fairValue.value2) {
					_this.fairVisible = 1
				} else {
					_this.fairVisible = 0

				}
				_this.saveSellerInfo(val);
			},
			// 点击交易信息保存
			handleSubmit1() {
				this.saveSellerInfo(1);
			},
			saveSellerInfo(val) {
				this.showOverlay = true;
				let list = [...this.fileList4, ...this.fileList8];
				let data = {
					id: this.detailId,
					vehicleReceiptAmount: this.$amount.getDelcommafy(this.sellerForm.vehicleReceiptAmount),
					collection: this.sellerForm.collection,
					payType: this.sellerForm.payType,
					transManageName: this.sellerForm.transManageName,
					sellerIdCard: this.sellerForm.sellerIdCard,
					idCardUrl: list.map((item) => { return item.id }),
					sellerName: this.sellerForm.sellerName,
					thirdSellerName: this.sellerForm.collection == 1 ? this.sellerForm.thirdSellerName : null,
					sellerAdder: this.sellerForm.sellerAdder,
					sellerTel: this.sellerForm.sellerTel.replace(/\s*/g, ""),
					remitType: this.sellerForm.remitType,
					bankName: this.sellerForm.bankName,
					bankCard: this.sellerForm.collection == 0 ? this.sellerForm.bankCard.replace(/\s*/g, "") : null,
					thirdBankCard: this.sellerForm.collection == 1 ? this.sellerForm.thirdBankCard.replace(/\s*/g, "") : null,
					buttonSaveOrSubmit: val == 'entrust' ? '1' : '2',
					buyCarFair: `${this.fairValue.value1}万元-${this.fairValue.value2}万元`
				}
				this.$modal.loading("提交中，请耐心等待...");
				setSellerInfo(data).then((res) => {
					if (val == 'entrust') {
						// 保存卖家信息并确认发起
						this.$modal.closeLoading()
						this.showOverlay = false;
						this.$tab.navigateTo(
							`/subPages/home/bycar/agreement?carId=${res.data.carInfoDetails.carId}&data=${JSON.stringify(res.data)}&fairValue=${JSON.stringify(this.fairValue)}&available=${this.available}&fairVisible=${this.fairVisible}`
						);
					} else {
						// 保存卖家草稿信息返回首页
						this.$modal.closeLoading()
						this.showOverlay = false;
						this.$modal.msg("保存草稿成功");
						this.$tab.switchTab('/pages/index');
					}
				}).catch((error) => {
					this.showOverlay = false;
					this.$modal.closeLoading()
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
			},
			// 点击车辆手续及备件
			handleCheckBox(name) {
				if (name != 'vehicleKey' && name != 'accidentVehicle') {
					let index = this.carForm.checkboxValue.indexOf(name);
					if (index == -1) {
						this.carForm.checkboxValue.push(name)
					} else {
						this.carForm.checkboxValue.splice(index, 1);
					}
				} else {
					let index = this.carForm.checkboxValue.indexOf(name);
					if (index == -1) {
						this.carForm.checkboxValue.push(name);
						if (this.carForm.checkboxValue.indexOf('vehicleKey') > -1) {
							this.disabledKey = false;
						}
						if (this.carForm.checkboxValue.indexOf('accidentVehicle') > -1) {
							this.disabledOther = false;
						}
					}
				}
			},
			otherInput(event) {
				let str = event.detail.value.substring(0, 10)
				this.$nextTick(() => {
					this.carForm.other = str;
				})

			},
			// 输入金额回调
			handleInput(val) {
				const texts = ['百', '千', '万', '十万', '百万', '千万', '亿', '十亿', '百亿', '千亿']
				if (val) {
					this.$nextTick(() => {
						if (val.indexOf('.') > -1) {
							let arr = val.split('.');
							if (arr[0].length > 2) {
								this.amountText = texts[arr[0].length - 3]
							} else {
								this.amountText = ''
							}
						} else {
							if (val.length > 2) {
								this.amountText = texts[val.length - 3]
							} else {
								this.amountText = ''
							}
						}
					})
				} else {
					this.amountText = ''
				}
			},
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

	/deep/ .u-form-item__body__right__content__slot {
		flex-direction: row;
		width: 100%;
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

	.headers {
		width: 100%;
		position: fixed;
		background-color: #fff;
		margin-top: -1px;
		z-index: 999;
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
			background-image: linear-gradient(to right, #fcbb2b, #ed6c21);
			background-color: #fff;
			color: #fff;
			font-size: 30rpx;
		}
	}

	.warp {
		display: flex;
		align-items: center;
		justify-content: center;
		height: 100%;
	}

	/* #ifdef H5 */
	/deep/ .u-form-item__body__right__content__slot>.model {
		flex: 1;
	}

	/* #endif */


	/* #ifdef MP-WEIXIN */
	/deep/ .u-form-item__body__right__content__slot>.model {
		flex: 1;
	}

	/* #endif */
</style>