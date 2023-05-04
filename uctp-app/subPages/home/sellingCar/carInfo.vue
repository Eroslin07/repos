<template>
	<view class="selling-car">
		<!-- 自定义导航栏 -->
		<u-navbar title="我要卖车" @leftClick="back" safeAreaInsetTop fixed placeholder></u-navbar>
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
			<view v-show="vehicleInfor">
				<view style="color: #A6A6A6;position: relative;margin: 0 0 0 26rpx;">
					<view style="position: absolute;top: 3rpx;height: 30rpx;border: 5rpx solid #fa6400;left: -23rpx;">
					</view>
					<view class="text">车辆基础信息</view>
				</view>
				<view class="look-over">
					<u-grid col="2">
						<u-grid-item>
							<uni-card @click="handleCollection">
								<u--text style="font-size:12px;" prefixIcon="level"
									iconStyle="font-size: 30px; color: #e26e1f" text="收车合同"></u--text>
							</uni-card>
						</u-grid-item>
						<u-grid-item>
							<uni-card @click="handleLookEntrust">
								<u--text style="font-size:12px;" prefixIcon="level"
									iconStyle="font-size: 30px; color: #e26e1f" text="委托合同"></u--text>
							</uni-card>
						</u-grid-item>
					</u-grid>
				</view>
				<u--form labelPosition="left" :model="carForm" :rules="carRules" ref="carForm" labelWidth="120px">
					<u-collapse accordion v-if="carForm.carPicList.length">
						<u-collapse-item title="车辆图片" name="carPicList">
							<u-album :urls="carForm.carPicList" maxCount="4" rowCount="4"></u-album>
						</u-collapse-item>
						<!-- <u-collapse-item
							title="行驶证"
							name="drivingPicList"
						>
							<u-album :urls="carForm.drivingPicList" maxCount="4" rowCount="4"></u-album>
						</u-collapse-item>
						<u-collapse-item
							title="机动车登记证书"
							name="registerPicList"
						>
							<u-album :urls="carForm.registerPicList" maxCount="4" rowCount="4"></u-album>
						</u-collapse-item> -->
					</u-collapse>
					<u-form-item label="发动机编号" prop="engineNum" borderBottom>
						<u--input v-model="carForm.engineNum" readonly border="none" placeholder="请输入发动机编号"></u--input>
					</u-form-item>
					<u-form-item label="车架号(VIN)" prop="vin" borderBottom>
						<u--input v-model="carForm.vin" readonly border="none" placeholder="请输入17位车架号(VIN)"></u--input>
					</u-form-item>
					<u-form-item label="首次登记日期" prop="firstRegistDate" borderBottom>
						<u--input v-model="carForm.firstRegistDate" readonly disabledColor="#ffffff"
							placeholder="请选择登记日期" border="none"></u--input>
						<!-- <u-icon
							slot="right"
							name="arrow-right"
						></u-icon> -->
					</u-form-item>
					<u-form-item label="车牌号" prop="plateNum" borderBottom>
						<u--input v-model="carForm.plateNum" readonly border="none" placeholder="请输入车牌号"></u--input>
					</u-form-item>
					<u-form-item label="使用性质" prop="natureOfOperat" borderBottom>
						<u--input v-model="carForm.natureOfOperat" readonly border="none" placeholder="请输入使用性质">
						</u--input>
					</u-form-item>
					<u-form-item label="车辆类型" prop="carType" borderBottom>
						<u--input v-model="carForm.carType" readonly border="none" placeholder="请输入车辆类型"></u--input>
					</u-form-item>
					<u-form-item label="品牌型号" prop="brandType" borderBottom>
						<u--input v-model="carForm.brandType" readonly border="none" placeholder="请输入品牌型号"></u--input>
					</u-form-item>
					<u-form-item label="品牌/车型" prop="model" borderBottom>
						<u--input v-model="carForm.model" readonly border="none" placeholder="请输入品牌/车型"></u--input>
						<!-- <u-icon
							slot="right"
							name="arrow-right"
						></u-icon> -->
					</u-form-item>
					<u-form-item label="登记证号" prop="certificateNo" borderBottom>
						<u--input v-model="carForm.certificateNo" readonly border="none" placeholder="请输入登记证号">
						</u--input>
					</u-form-item>
					<u-form-item label="颜色" prop="colour" borderBottom>
						<u--input v-model="carForm.colour" readonly border="none" placeholder="请输入颜色"></u--input>
					</u-form-item>
					<u-form-item label="里程数" prop="mileage" borderBottom>
						<u-input v-model="carForm.mileage" readonly border="none" placeholder="请输入里程数">
							<template slot="suffix">
								<view>万公里</view>
							</template>
						</u-input>
					</u-form-item>
					<u-form-item label="使用年限至" prop="scrapDate" borderBottom>
						<u--input v-model="carForm.scrapDate" readonly disabledColor="#ffffff" placeholder="请选择"
							border="none"></u--input>
						<!-- <u-icon slot="right" name="arrow-right"></u-icon> -->
					</u-form-item>
					<u-form-item label="年检签证有效期" prop="annualInspectionDate" borderBottom>
						<u--input v-model="carForm.annualInspectionDate" readonly disabledColor="#ffffff"
							placeholder="请选择" border="none"></u--input>
						<!-- <u-icon slot="right" name="arrow-right"></u-icon> -->
					</u-form-item>
					<u-form-item label="保险险种" prop="insurance" borderBottom>
						<u--input v-model="carForm.insurance" readonly border="none" placeholder="请输入保险险种"></u--input>
					</u-form-item>
					<u-form-item label="保险期至" prop="insuranceEndData" borderBottom>
						<u--input v-model="carForm.insuranceEndData" readonly disabledColor="#ffffff" placeholder="请选择"
							border="none"></u--input>
						<!-- <u-icon slot="right" name="arrow-right"></u-icon> -->
					</u-form-item>
					<u-form-item label="特殊约定" prop="remarks" borderBottom>
						<!-- <u--input v-model="carForm.remarks" maxlength="68" type="text" showWordLimit border="none"
							placeholder="请输入特殊约定"></u--input> -->
						<u--textarea disabledColor="#ffffff" v-model="carForm.remarks" height="24" maxlength="68"
							confirmType="done" count border="none" placeholder="请输入特殊约定"></u--textarea>
					</u-form-item>
				</u--form>
				<!-- 选择登记日期 -->
				<u-datetime-picker :show="showDate" v-model="carForm.firstRegistDate" mode="date" :formatter="formatter"
					@cancel="showDate = false" @confirm="handleDate"></u-datetime-picker>

			</view>
			<!-- 买家信息 -->
			<view v-show="sellerInfor">
				<view class="text">买家信息</view>
				<u--form labelPosition="left" :model="sellerForm" :rules="sellerRules" ref="sellerForm"
					labelWidth="120px">
					<view style="color: #A6A6A6;position: relative;margin: 0 0 0 26rpx;">
						<view
							style="position: absolute;top: 3rpx;height: 30rpx;border: 5rpx solid #fa6400;left: -23rpx;">
						</view>
						<view class="text">车辆价款及交易方式</view>
					</view>
					<u-form-item label="收车金额" borderBottom>
						<u-input v-model="sellerForm.vehicleReceiptAmount" disabled border="none" placeholder="请输入收车金额">
							<template slot="suffix">
								<view>元</view>
							</template>
						</u-input>
					</u-form-item>
					<u-form-item label="卖车金额" :required="true" prop="sellAmount" borderBottom>
						<u-input v-model="sellerForm.sellAmount" type="digit" border="none" @focus="handleFocus"
							@blur="handleBlur" placeholder="请输入卖车金额">
							<template slot="suffix">
								<view>元</view>
							</template>
						</u-input>
					</u-form-item>
					<view>
						<u--text style="font-size:12px;" prefixIcon="info-circle"
							iconStyle="font-size: 16px; color: #e26e1f"
							:text="'公允值范围：'+fairValue.value1+'万元-'+fairValue.value2+'万元'" color="#e26e1f"></u--text>
						<view v-if="fairStatus == '不通过'" style="margin-left: 15px;color: #e26e1f;">公允价值审核-退回 ></view>
						<view style="margin-left: 15px;color: #e26e1f;">
							预计费用{{sellerForm.total}}元，利润{{sellerForm.profit}}元。<text @click="handleDetail">明细请查看
								></text></view>
					</view>
					<u-form-item label="收款方式" :required="true" prop="sellType" borderBottom>
						<u-radio-group v-model="sellerForm.sellType" placement="row">
							<u-radio :customStyle="{marginBottom: '8px'}" v-for="(item, index) in sexs" :key="index"
								:label="item.label" :name="item.value">
							</u-radio>
						</u-radio-group>
					</u-form-item>
					<u-form-item label="定金" :required="true" prop="deposit" borderBottom>
						<u-input v-model="sellerForm.deposit" border="none" placeholder="请输入定金" type="number" @focus="depositFocus" @blur="depositBlur">
							<template slot="suffix">
								<view>元</view>
							</template>
						</u-input>
					</u-form-item>
					<view style="color: #A6A6A6;position: relative;margin: 0 0 0 26rpx;">
						<view
							style="position: absolute;top: 3rpx;height: 30rpx;border: 5rpx solid #fa6400;left: -23rpx;">
						</view>
						<view class="text">买家信息</view>
					</view>
					<u-form-item label="身份证号" :required="true" prop="buyerIdCard" borderBottom>
						<u--input v-model="sellerForm.buyerIdCard" border="none" placeholder="请输入身份证号"></u--input>
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
									<u-upload v-if="fileList5.length" :fileList="fileList5" @delete="deletePic" name="5"
										width="150"></u-upload>
									<image v-else src="/static/images/home/rxm.png" mode="widthFix"
										style="width: 150px;" @click="handleOcr(5)"></image>
									<image v-if="fileList5.length == 0" src="../../../static/images/take.png"
										class="icon-image" @click="handleOcr(5)"></image>
								</u-grid-item>
							</u-grid>
						</view>
					</u-form-item>
					<u-form-item label="姓名" :required="true" prop="buyerName" borderBottom>
						<u--input v-model="sellerForm.buyerName" border="none" placeholder="请输入姓名">
						</u--input>
					</u-form-item>
					<u-form-item label="联系地址" :required="true" prop="buyerAdder" borderBottom>
						<u--input v-model="sellerForm.buyerAdder" border="none" placeholder="请输入联系地址"></u--input>
					</u-form-item>
					<u-form-item label="电话" :required="true" prop="buyerTel" borderBottom>
						<u--input v-model="sellerForm.buyerTel" border="none" placeholder="请输入11位手机号"></u--input>
					</u-form-item>
				</u--form>
				<view style="margin: 20px 0;">
					<u--text style="font-size:12px;" prefixIcon="info-circle"
						iconStyle="font-size: 16px; color: #e26e1f"
						text="注意:在发起委托合同前，请检查您的相关信息,发起委托合同时会将信息自动带到后方合同作为重要信息使用。" color="#e26e1f"></u--text>
				</view>
			</view>
		</uni-card>
		<view class="fenge" v-if="vehicleInfor"></view>
		<uni-card :is-shadow="false" is-full style="border: none;">
			<view v-if="vehicleInfor">
				<view style="color: #A6A6A6;position: relative;margin: 0 0 0 26rpx;">
					<view style="position: absolute;top: 3rpx;height: 30rpx;border: 5rpx solid #fa6400;left: -23rpx;">
					</view>
					<view class="text"><text style="color:#f00">*</text> 车辆手续及备件</view>
				</view>
				<u--form :model="carForm" labelPosition="left" labelWidth="120px">
					<u-checkbox-group v-model="carForm.checkboxValue" placement="column" activeColor="#fd6404"
						@change="changeValue">
						<u-form-item v-for="(item, index) in checkboxList" :key="index" borderBottom>
							<u-checkbox :label="item.label" :name="item.name"></u-checkbox>
							<view style="margin-left: 10px;width: 100%">
								<u-input v-model="carForm.key" type="number" :disabled="isDisabledKey"
									v-if="item.name == 'vehicleKey'" disabledColor="#ffffff" placeholder="请输入"
									border="none">
									<template slot="suffix">
										<view>组</view>
									</template>
								</u-input>
								<u--input type="text" showWordLimit v-model="carForm.otherEvent"
									:disabled="isDisabledAcc" maxlength="10" v-if="item.name == 'accidentVehicle'"
									disabledColor="#ffffff" border="none" placeholder="请输入"></u--input>
							</view>
							</u--text>
						</u-form-item>
					</u-checkbox-group>
				</u--form>
			</view>
			<u-picker :show="showKey" :columns="rangeKey" keyName="label" @confirm="confirmKey"
				@cancel="showKey = false"></u-picker>
		</uni-card>
		<uni-card :is-shadow="false" is-full style="border: none;">
			<view v-if="vehicleInfor">
				<u--form :model="feesForm" :rules="feesFormRules" ref="feesForm" labelPosition="left"
					labelWidth="120px">
					<u-form-item label="车况" :required="true"></u-form-item>
					<u-form-item borderBottom prop="conditionA">
						<u-radio-group v-model="feesForm.conditionA" activeColor="#fd6404" shape="square">
							<text>（</text>
							<u-radio label="确保" :name="true"></u-radio>
							<text style="margin: 0 5px;">/</text>
							<u-radio label="不确保" :name="false"></u-radio>
							<text>）里程表未被蓄意改动</text>
						</u-radio-group>
					</u-form-item>
					<u-form-item borderBottom prop="conditionB">
						<u-radio-group v-model="feesForm.conditionB" activeColor="#fd6404" shape="square">
							<text>（</text>
							<u-radio label="是" :name="true"></u-radio>
							<text style="margin: 0 5px;">/</text>
							<u-radio label="非" :name="false"></u-radio>
							<text>）事故车</text>
						</u-radio-group>
					</u-form-item>
					<u-form-item borderBottom prop="conditionC">
						<u-radio-group v-model="feesForm.conditionC" activeColor="#fd6404" shape="square">
							<text>（</text>
							<u-radio label="是" :name="true"></u-radio>
							<text style="margin: 0 5px;">/</text>
							<u-radio label="非" :name="false"></u-radio>
							<text>）泡水车</text>
						</u-radio-group>
					</u-form-item>
					<u-form-item borderBottom prop="conditionD">
						<u-radio-group v-model="feesForm.conditionD" activeColor="#fd6404" shape="square">
							<text>（</text>
							<u-radio label="是" :name="true"></u-radio>
							<text style="margin: 0 5px;">/</text>
							<u-radio label="非" :name="false"></u-radio>
							<text>）火烧车</text>
						</u-radio-group>
					</u-form-item>

					<view style="color: #A6A6A6;position: relative;margin: 0 0 0 26rpx;">
						<view
							style="position: absolute;top: 3rpx;height: 30rpx;border: 5rpx solid #fa6400;left: -23rpx;">
						</view>
						<view class="text">其他费用及约定</view>
					</view>

					<u-radio-group v-model="feesForm.vehicle" placement="column" activeColor="#fd6404" shape="square">
						<u-form-item label="租金相关" :required="true" prop="rent"></u-form-item>
						<u-form-item borderBottom>
							<u-radio name="vehicleA"></u-radio>
							<text>“轿车200元/天”</text>
						</u-form-item>
						<u-form-item borderBottom style="display:flex">
							<u-radio name="vehicleB"></u-radio>
							<text>“商务车400元/天”</text>
						</u-form-item>
						<u-form-item borderBottom>
							<u-radio name="vehicleC"></u-radio>
							<text>“豪车1000元/天”(本协议第二条车辆价款在50万(含)以上)</text>
						</u-form-item>
						<u-form-item borderBottom>
							<u-radio name="vehicleD"></u-radio>
							<text>平台无需承担</text>
						</u-form-item>
					</u-radio-group>

					<u-radio-group v-model="feesForm.transfer" placement="column" activeColor="#fd6404" shape="square">
						<u-form-item label="交易过户费" :required="true" prop="business"></u-form-item>
						<u-form-item borderBottom>
							<u-radio name="transferA"></u-radio>
							<text>销售车辆首次交易过户费(平台过户买方指定过户人)</text>
						</u-form-item>
						<u-form-item borderBottom>
							<u-radio name="transferB"></u-radio>
							<text>销售车辆二次交易过户费(该车辆现户主过户车商指定过户人)</text>
						</u-form-item>
						<u-form-item borderBottom>
							<u-radio name="transferC"></u-radio>
							<text>平台无需承担</text>
						</u-form-item>
					</u-radio-group>

					<u-radio-group v-model="feesForm.loss" placement="column" activeColor="#fd6404" shape="square">
						<u-form-item label="车辆折损费用" :required="true" prop="damage"></u-form-item>
						<u-form-item borderBottom>
							<u-radio name="lossA"></u-radio>
							<text>依据本协议第二条车辆价款的5%支付车辆折损费用</text>
						</u-form-item>
						<u-form-item borderBottom>
							<u-radio name="lossB"></u-radio>
							<text>平台无需承担</text>
						</u-form-item>
					</u-radio-group>

					<u-radio-group v-model="feesForm.testing" placement="column" activeColor="#fd6404" shape="square">
						<u-form-item label="第三方检测费用" :required="true" prop="check"></u-form-item>
						<u-form-item borderBottom>
							<u-radio name="testingA"></u-radio>
							<text>全车检测费用</text>
						</u-form-item>
						<u-form-item borderBottom>
							<u-radio name="testingB"></u-radio>
							<text>平台无需承担</text>
						</u-form-item>
					</u-radio-group>
					<!-- <u-form-item label="其他"></u-form-item> -->
					<u-form-item label="其他" borderBottom labelWidth="40">
						<!-- <u--input v-model="other" maxlength="18" type="textarea" showWordLimit border="none" placeholder="请输入"></u--input> -->
						<u--textarea v-model="carForm.other" height="24" maxlength="18" confirmType="done" count
							border="none" placeholder="请输入"></u--textarea>
					</u-form-item>
				</u--form>
			</view>
		</uni-card>
		<!-- 费用明细 -->
		<u-modal :show="showDetail" @confirm="showDetail = false">
			<view v-if="!isChildAccount">
				<view>收车金额：{{ amountDetails.vehicleReceiptAmount | filterMoney }}元</view>
				<view>卖车金额：{{ amountDetails.sellAmount | filterMoney }}元</view>
				<!-- <view>过户服务费：{{ amountDetails.transferSell | filterMoney }}元</view> -->
				<view>运营服务费：{{ amountDetails.operation | filterMoney }}元</view>
				<!-- <view>过户服务费（买家）：{{ amountDetails.transferBuy | filterMoney}}元</view> -->
				<view>增值税费用：{{ amountDetails.vat | filterMoney }}元</view>
				<view>杂税费用：{{ amountDetails.tax | filterMoney }}元</view>
				<view>合计费用：{{ amountDetails.total | filterMoney }}元</view>
				<view>利润：{{ amountDetails.profit | filterMoney }}元</view>
			</view>
			<!-- 子账户 -->
			<view v-else>
				<view>运营服务费：****元</view>
				<view>过户服务费：****元</view>
				<view>增值税费用：****元</view>
				<view>杂税费用：****元</view>
				<view>合计费用：****元</view>
				<view>利润：****元</view>
			</view>
		</u-modal>
		<view class="footer">
			<!-- 底部按钮 -->
			<u-grid col="3">
				<u-grid-item>
					<button @click="handleDelete" class="button" style="background-image: none;color: #000;">清空</button>
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

		<!-- 清空弹框 -->
		<u-modal :show="deleteModal" content='确认清空已填数据吗?' showCancelButton :closeOnClickOverlay="false"
			@cancel="deleteModal=false" @confirm="deleteSubmit"></u-modal>
	</view>
</template>

<script>
	let that;
	import config from '@/config'
	import {
		getAccessToken
	} from '@/utils/auth'
	import {
		urlTobase64
	} from '@/utils/ruoyi.js'
	import {
		getIdCard,
		deleteImage
	} from '@/api/register'
	import {
		getSellCarInfo,
		setSellCarInfo,
		getAmount,
		deleteSellDraft
	} from '@/api/home/sellingCar.js'
	import {
		getFairValue
	} from '@/api/home/bycar.js'
	import {
		setCreate
	} from '@/api/home'
	import {
		parseTime
	} from '@/utils/ruoyi.js'
	export default {
		data() {
			return {
				albumWidth: 0,
				otherValue: '',
				showOverlay: false,
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
				fileList5: [],
				// 车辆信息
				carForm: {
					registerPicList: [],
					drivingPicList: [],
					carPicList: [],
					vin: '',
					natureOfOperat: '',
					carType: '',
					engineNum: '',
					firstRegistDate: uni.$u.timeFormat(Number(new Date()), 'yyyy-mm-dd'),
					plateNum: '',
					model: '',
					brand: '',
					licensePlateNum: '',
					colour: '',
					mileage: '',
					scrapDate: '',
					annualInspectionDate: '',
					insurance: '',
					insuranceEndData: '',
					remarks: '',
					checkboxValue: [],
					vehicleKey: '',
					accidentVehicle: '',
					other: '',
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
					}
				},
				amountDetails: {},
				// 车辆手续
				checkboxList: [{
						label: '行驶证正、副本',
						name: 'drivingLicense'
					},
					{
						label: '购车发票',
						name: 'carInvoice'
					},
					{
						label: '机动车登记证',
						name: 'registrationCertificate'
					},
					{
						label: '购置税完税凭证',
						name: 'purchaseTax'
					},
					{
						label: '备胎',
						name: 'spareTire'
					},
					{
						label: '车船使用税完税凭证',
						name: 'carShipTax'
					},
					{
						label: '交强险保单',
						name: 'heavyTrafficInsurance'
					},
					{
						label: '商业险保单',
						name: 'commercialInsurance'
					},
					{
						label: '千斤顶',
						name: 'jack'
					},
					{
						label: '说明书',
						name: 'specification'
					},
					{
						label: '钥匙',
						name: 'vehicleKey'
					},
					{
						label: '其他',
						name: 'accidentVehicle'
					},
				],
				isDisabledKey: true,
				isDisabledAcc: true,
				showKey: false,
				rangeKey: [
					[{
						label: 1,
						id: 1
					}],
				],

				//车况及其他费用及约定
				feesForm: {
					conditionA: true,
					conditionB: false,
					conditionC: false,
					conditionD: false,
					vehicle: '',
					transfer: '',
					loss: '',
					testing: '',
				},
				feesFormRules: {
					conditionA: {
						type: 'boolean',
						required: true,
						message: '请选择车辆情况',
						trigger: ['blur', 'change']
					},
					conditionB: {
						type: 'boolean',
						required: true,
						message: '请选择车辆情况',
						trigger: ['blur', 'change']
					},
					conditionC: {
						type: 'boolean',
						required: true,
						message: '请选择车辆情况',
						trigger: ['blur', 'change']
					},
					conditionD: {
						type: 'boolean',
						required: true,
						message: '请选择车辆情况',
						trigger: ['blur', 'change']
					},
					vehicle: {
						type: 'string',
						required: true,
						message: '请选择租金相关',
						trigger: ['blur', 'change']
					},
					transfer: {
						type: 'string',
						required: true,
						message: '请选择交易过户费',
						trigger: ['blur', 'change']
					},
					loss: {
						type: 'string',
						required: true,
						message: '请选择车辆折损费用',
						trigger: ['blur', 'change']
					},
					testing: {
						type: 'string',
						required: true,
						message: '请选择第三方检测费用',
						trigger: ['blur', 'change']
					}
				},
				//其他
				other: '',
				fairValue: {
					value1: '',
					value2: ''
				},
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
					vehicleReceiptAmount: '',
					total: '',
					profit: '',
					sellAmount: '',
					sellType: 0,
					transManageName: '',
					buyerIdCard: '',
					buyerIdCardUrl: [],
					buyerName: '',
					buyerAdder: '',
					buyerTel: '',
					deposit: '0'
				},
				// 卖家信息校验规则
				sellerRules: {
					// vehicleReceiptAmount: {
					// 	type: 'string',
					// 	required: true,
					// 	message: '请填写收车金额',
					// 	trigger: ['blur', 'change']
					// },
					sellAmount: {
						type: 'string',
						required: true,
						message: '请填写卖车金额',
						trigger: ['blur', 'change']
					},
					sellType: {
						type: 'number',
						required: true,
						message: '请选择卖车方式',
						trigger: ['blur', 'change']
					},
					// transManageName: {
					// 	type: 'string',
					// 	required: true,
					// 	message: '请填写转入地车辆管理所名称',
					// 	trigger: ['blur', 'change']
					// },
					buyerIdCard: [{
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
					deposit: [{
						type: 'string',
						required: true,
						message: '请填写手机号',
						trigger: ['blur', 'change']
					}, {
						validator(rule, value, data, callback) {
							const num = value * 1
							if (num >= 0) {
								return true
							} else {
								return false
							}
						},
					}]
				},
				date: null,
				modelId: null,

				// 删除模态框
				deleteModal: false,

				// 草稿状态
				draftStatus: 0,

				// 是否是子账户
				isChildAccount: false,
				fairStatus: null,
				gxzStatus: true
			}
		},
		onReady() {
			//onReady 为uni-app支持的生命周期之一
			this.$refs.carForm.setRules(this.carRules)
			this.$refs.sellerForm.setRules(this.sellerRules)
			this.$refs.feesForm.setRules(this.feesFormRules)
		},
		beforeCreate() {
			that = this;
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
			this.draftStatus = options.status - 0;
			this.carId = options.id;
			this.showOverlay = true;
			this.$modal.loading("数据加载中，请耐心等待...")
			getSellCarInfo({
				id: options.id
			}).then((res) => {
				this.carForm = res.data;
				this.carForm.scrapDate = parseTime(this.carForm.scrapDate);
				this.carForm.annualInspectionDate = parseTime(this.carForm.annualInspectionDate);
				this.carForm.checkboxValue = [];
				this.modelId = res.data.modelId;
				// 收车金额
				this.sellerForm.sellType = res.data.sellType;
				this.sellerForm.vehicleReceiptAmount = this.$amount.getComdify(res.data.vehicleReceiptAmount);
				this.sellerForm.buyerName = res.data.buyerName
				this.sellerForm.buyerAdder = res.data.buyerAdder
				this.sellerForm.buyerTel = res.data.buyerTel
				this.sellerForm.deposit = res.data.deposit || 0
				this.sellerForm.buyerIdCard = res.data.buyerIdCard
				this.fairStatus = res.data.bpmStatus;

				let obj;
				if (this.draftStatus == 31) {
					obj = res.data.proceduresAndSpareSell;
					// 车况及其他费用及约定
					this.feesForm = {
						...res.data.feesAndCommitments,
						...res.data.vehicleProblem
					}
				} else {
					obj = res.data.proceduresAndSpareParts;
					// 清空特殊约定
					this.carForm.remarks = ''
				}
				for (let key in obj) {
					if (obj[key] === true) {
						this.carForm.checkboxValue.push(key);
					}
					if (key == 'vehicleKey') {
						if (obj[key] != '') {
							this.carForm.vehicleKey = obj[key];
							this.carForm.key = obj[key]
							this.carForm.checkboxValue.push(key);
						}
					}
					if (key == 'accidentVehicle') {
						if (obj[key] != '') {
							this.carForm.accidentVehicle = obj[key];
							this.carForm.otherEvent = obj[key];
							this.carForm.checkboxValue.push(key);
						}
					}
				}
				// 钥匙
				if (obj.vehicleKey) {
					this.$nextTick(() => {
						this.isDisabledKey = false
					})
				} else {
					this.$nextTick(() => {
						this.isDisabledKey = true
					})
				}

				// 其他
				if (obj.accidentVehicle) {
					this.isDisabledAcc = false
				} else {
					this.isDisabledAcc = true
				}

				this.showOverlay = false;
			}).catch((error) => {
				this.$modal.msg("查询失败");
				this.showOverlay = false;
				if (this.draftStatus == 31) {
					this.$tab.switchTab('/pages/index')
					return;
				}
				this.$tab.navigateTo('/subPages/home/sellingCar/index');
			}).finally(() => {
				this.$modal.closeLoading();
			})
		},
		mounted() {
			this.date = uni.$u.timeFormat(Number(new Date()), 'yyyymmdd');
		},
		filters: {
			filterMoney(val) {
				if (val) {
					let money = that.$amount.getComdify(val - 0)
					console.log(money, 'money')
					return money ? money : 0
				} else {
					return 0
				}

			}
		},
		methods: {
			// 钥匙及其他输入框是否禁用
			changeValue(value) {
				if (value.indexOf('vehicleKey') > 0) {
					this.$nextTick(() => {
						this.isDisabledKey = false
					})
				} else {
					this.$nextTick(() => {
						this.isDisabledKey = true
						// this.carForm.key=''
					})
				}
				if (value.indexOf('accidentVehicle') > 0) {
					this.$nextTick(() => {
						this.isDisabledAcc = false
					})
				} else {
					this.$nextTick(() => {
						this.isDisabledAcc = true
						// this.carForm.other=''
					})

				}
			},
			back() {
				if (this.active == 0) {
					this.handleSaveCar();
				} else if (this.active == 1) {
					this.vehicleInfor = true;
					this.sellerInfor = false;
					this.active = 0;
					uni.pageScrollTo({
						scrollTop: 0,
						duration: 300
					});
				}
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
						// 识别身份证
						_this.sellerForm.buyerIdCardUrl = [..._this.sellerForm.buyerIdCardUrl, _this[
							`fileList${index}`]];
						for (let i = 0; i < res.tempFilePaths.length; i++) {
							let str = await urlTobase64(res.tempFilePaths[i]);
							getIdCard({
								IDCardUrl: str
							}).then((ress) => {
								let data = JSON.parse(ress.data);
								if (data.idcard_number_type == -1) {
									_this.$modal.msg("请上传正确且清晰的身份证照照片 ");
									_this[`fileList${index}`] = [];
								} else {
									if (data.words_result['公民身份号码']) {
										_this.sellerForm.buyerAdder = data.words_result['住址'].words;
										_this.sellerForm.buyerIdCard = data.words_result['公民身份号码']
											.words;
										_this.sellerForm.buyerName = data.words_result['姓名'].words;
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
									_this.sellerForm.buyerIdCardUrl = [];
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
				if (!this.sellerForm.sellAmount) {
					return this.$modal.msg('请输入卖车金额！')
				}
				this.showDetail = true;
			},
			// 卖车金额失焦获取公允价值
			handleBlur(val) {
				let data = {
					id: this.carId,
					sellAmount: val
				}
				getAmount(data).then((res) => {
					this.sellerForm.total = res.data.total;
					this.sellerForm.profit = res.data.profit;
					this.amountDetails = res.data;
				})
				let amount = this.$amount.getComdify(this.sellerForm.sellAmount);
				this.$set(this.sellerForm, 'sellAmount', amount);
			},
			//卖车金额获取焦点
			handleFocus() {
				let amount = this.$amount.getDelcommafy(this.sellerForm.sellAmount);
				this.$set(this.sellerForm, 'sellAmount', amount);
			},
			//定金获取焦点
			depositFocus(){
				let amount = this.$amount.getDelcommafy(this.sellerForm.deposit);
				this.$set(this.sellerForm, 'deposit', amount);
			},
			//顶级失焦
			depositBlur(){
				let amount = this.$amount.getComdify(this.sellerForm.deposit);
				this.$set(this.sellerForm, 'deposit', amount);
			},
			// 删除
			handleDelete() {
				this.deleteModal = true;
			},
			// 确认删除
			deleteSubmit() {
				this.deleteModal = false;
				deleteSellDraft(this.carForm.id).then(res => {
					this.$modal.success('清空成功');
					this.$tab.switchTab('/pages/index');
				}).catch(() => {
					console.log('清空失败')
				})
			},
			// 下一步
			handleStep() {
				this.$refs.feesForm.validate().then(res => {
					this.handleDraft('step');
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
				if (this.draftStatus == 31) {
					this.$tab.switchTab('/pages/index')
					return;
				}
				this.$tab.navigateTo('/subPages/home/sellingCar/index');
			},
			// 保存车辆信息草稿
			handleDraft(val) {
				if (!this.carForm.checkboxValue.length) return this.$modal.msgError('车辆手续及备件不能为空！')
				// 车辆手续及备件
				let proceduresAndSpareParts = {};
				let list = [];
				this.checkboxList.forEach((item) => {
					if (this.carForm.checkboxValue.indexOf(item.name) == -1) {
						list.push(item.name);
					}
				})
				this.carForm.checkboxValue.forEach((item) => {
					proceduresAndSpareParts[item] = true;
				})
				list.forEach((item) => {
					proceduresAndSpareParts[item] = false;
				})
				if (proceduresAndSpareParts['vehicleKey'] == true) {
					if (this.carForm.key == '' || !this.carForm.key) {
						this.$modal.msg("请输入钥匙");
						return
					}
					proceduresAndSpareParts['vehicleKey'] = this.carForm.key;
				} else {
					proceduresAndSpareParts['vehicleKey'] = 0;
				}
				if (proceduresAndSpareParts['accidentVehicle'] == true) {
					if (this.carForm.otherEvent == '' || !this.carForm.otherEvent) {
						this.$modal.msg("请输入其他内容");
						return
					}
					proceduresAndSpareParts['accidentVehicle'] = this.carForm.otherEvent;
				} else {
					proceduresAndSpareParts['accidentVehicle'] = '';
				}

				// 车况
				let vehicleProblem = {
					conditionA: this.feesForm.conditionA,
					conditionB: this.feesForm.conditionB,
					conditionC: this.feesForm.conditionC,
					conditionD: this.feesForm.conditionD,
				}

				// 其他费用及约定
				let feesAndCommitments = {
					...this.feesForm
				};
				let data = {
					id: this.carId,
					remarks: this.carForm.remarks,
					sellAmount: this.$amount.getDelcommafy(this.sellerForm.sellAmount),
					transManageName: this.sellerForm.transManageName,
					buyerIdCard: this.sellerForm.buyerIdCard,
					idCardIds: this.fileList4.map((item) => {
						return item.id
					}),
					buyerName: this.sellerForm.buyerName,
					buyerAdder: this.sellerForm.buyerAdder,
					buyerTel: this.sellerForm.buyerTel,
					sellType: this.sellerForm.sellType,
					deposit: this.sellerForm.deposit,
					vehicleProblem, //车况
					feesAndCommitments,
					proceduresAndSpareSell: proceduresAndSpareParts,
					other: this.carForm.other //其他
				}

				this.showOverlay = true;
				this.$modal.loading("提交中，请耐心等待...");
				setSellCarInfo(data).then((res) => {
					if (val == 'step') {
						// 保存车辆信息并进行下一步
						this.vehicleInfor = false;
						this.sellerInfor = true;
						this.active = 1;
						this.$refs.carForm.clearValidate();
						uni.pageScrollTo({
							scrollTop: 0,
							duration: 300
						});
						this.showOverlay = false;
						this.$modal.closeLoading()
						this.getFairValue();
					} else if (val == 'entrust') {
						// 保存买家信息并确认发起
						this.$modal.closeLoading()
						this.showOverlay = false;
						this.$tab.navigateTo(
							`/subPages/home/sellingCar/agreement?carId=${res.data.carInfoDetails.carId}&fairValue=${JSON.stringify(this.fairValue)}&data=${JSON.stringify(res.data)}&gxzStatus=${this.gxzStatus}`
						);
					} else {
						// 保存车辆草稿信息返回首页
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
			// 查询公允价值
			getFairValue() {
				let data = {
					modelId: this.modelId,
					plateNum: this.carForm.plateNum,
					mileage: this.carForm.mileage,
					firstRegistDate: this.carForm.firstRegistDate
				}
				getFairValue(data).then((res) => {
					if (res.msg == 'success') {
						this.fairValue.value1 = res.Recommended_low_sold_price;
						this.fairValue.value2 = res.Recommended_high_sold_price;
					} else {
						this.$modal.msg(res.msg);
					}
				})
			},
			// 点击发起委托合同
			handleEntrust() {
				let _this = this;
				_this.$refs.sellerForm.validate().then(res => {
					let amount = _this.$amount.getDelcommafy(_this.sellerForm.sellAmount);
					amount = amount / 10000;
					if (_this.fairValue.value1 <= amount && amount <= _this.fairValue.value2) {
						_this.gxzStatus = true;
					} else {
						_this.gxzStatus = false;
					}
					_this.handleDraft('entrust');
				})
			},
			// 点击卖家信息保存
			handleSubmit() {
				this.handleDraft();
			},
			// 确认钥匙框
			confirmKey(val) {
				this.carForm.key = val.value[0].label;
				this.showKey = false;
			},
		}
	}
</script>

<style lang="scss" scoped>
	.selling-car {
		border-top: 1px solid #f3f3f3;
		padding-bottom: 80px;

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

		/deep/ .u-form-item__body__right__content__slot {
			flex-direction: row;
			width: 100%;
		}

		/* #endif */

		/deep/ .uni-card--border {
			border: none;
		}

		.text {
			font-size: 16px;
			color: #000;
			margin: 8px 0;
		}

		.look-over {
			color: #000;
		}

		/deep/ .u-cell__body {
			padding: 10px 0;
		}

		.image {
			width: 100%;
		}

		/deep/ .image .u-upload__button {
			display: none;
		}

		.fenge {
			height: 20px;
			background-color: #fafafa;
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

		.footer {
			width: 100%;
			position: fixed;
			bottom: 0;
			background-color: #fff;
			padding-bottom: 20px;
			z-index: 999;

			.button {
				width: 80%;
				margin-top: 10px;
				background-image: linear-gradient(to right, #fcbb2b, #ed6c21);
				background-color: #fff;
				color: #fff;
			}
		}
	}
</style>