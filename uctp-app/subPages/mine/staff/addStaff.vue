<template>
	<view class="add-staff">
		<uni-card :is-shadow="false" is-full style="border: none;">
			<u--form labelPosition="left" :model="staffForm" :rules="rules" ref="staffForm" labelWidth="140rpx"
				:labelStyle="{fonsSzie:'28rpx',color:'#222222'}">
				<u-form-item label="姓名" prop="name" borderBottom>
					<u-input v-model="staffForm.name" border="none" placeholder="请输入姓名"></u-input>
				</u-form-item>
				<u-form-item label="手机号" prop="phone" borderBottom>
					<u--input v-model="staffForm.phone" type="number" border="none" placeholder="请输入11位手机号"></u--input>
				</u-form-item>
				<u-form-item label="身份证号" prop="idCard" borderBottom>
					<u--input v-model="staffForm.idCard" type="idcard" border="none" placeholder="请输入身份证号"></u--input>
				</u-form-item>
				<u-form-item label="是否停用" prop="status" borderBottom>
					<uni-data-checkbox v-model="staffForm.status" :disabled="type == 'add'" selectedColor="#FA6400"
						:localdata="statusList"></uni-data-checkbox>
				</u-form-item>
			</u--form>
		</uni-card>
		<view class="prompt-box">
			<view class="title">
				<u-icon name="info-circle" color="#CCCCCC"></u-icon>
				<text>提示:</text>
			</view>
			<view class="flex prompt-content">
				<text>1、手机号或者身份证号有变动，将重新触发个人实名认证。</text>
				<text>2、短信实名认证时效为15分钟，请提醒员工在时效内认证完成， 避免造成业务无法正常处理。</text>
			</view>
		</view>
		<view>
			<view class="action-btn">
				<button @click="handleSave" class="button">{{type=='add'?'保存并认证':'保存'}}</button>
			</view>
			<view class="action-btn" v-if="type=='edit'">
				<button @click="handleauthentication" class="button edit-button">重新认证</button>
			</view>
		</view>
		
		<!-- 遮罩层 -->
		<u-overlay :show="showOverlay"></u-overlay>
	</view>
</template>

<script>
	import { setAccount, getAuth } from "@/api/system/mine"
	export default {
		data() {
			return {
				staffForm: {
					name: '',
					phone: '',
					idCard: '',
					status: '0'
				},
				oldData: {},
				rules: {
					name: {
						type: 'string',
						required: true,
						message: '请填写姓名',
						trigger: ['blur']
					},
					phone: [{
						type: 'string',
						required: true,
						message: '请填写手机号',
						trigger: ['blur']
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
					idCard: [{
						type: 'string',
						required: true,
						message: '请填写身份证号',
						trigger: ['blur']
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
					status: {
						type: 'string',
						required: true,
						message: '请选择是否停用',
						trigger: ["blur"]
					}
				},
				statusList: [{
					text: '否',
					value: '0'
				}, {
					text: '是',
					value: '1'
				}],
				type: '',
				showOverlay: false,
			}
		},
		onReady() {
			//onReady 为uni-app支持的生命周期之一
			this.$refs.staffForm.setRules(this.rules)
		},
		onLoad(options) {
			// console.log(options)
			if (options.data) {
				this.oldData = {};
				this.staffForm = JSON.parse(decodeURIComponent(options.data));
				this.oldData = JSON.parse(decodeURIComponent(options.data));
			}
			if (options.type == 'add') {
				uni.setNavigationBarTitle({
				    title: "新增员工"
				});
			} else if (options.type == 'edit') {
				uni.setNavigationBarTitle({
				    title: "修改员工"
				});
			}
			this.type = options.type
		},
		methods: {
			// 保存
			handleSave() {
				let _this = this;
				this.$refs.staffForm.validate().then(res => {
					let data = {
						id: _this.type == 'add' ? null : _this.staffForm.id,
						name: _this.staffForm.name,
						phone: _this.staffForm.phone,
						idCard: _this.staffForm.idCard,
						status: _this.staffForm.status,
						deptId: _this.type == 'add' ? _this.$store.state.user.deptId : _this.staffForm.deptId,
						tenantId: _this.type == 'add' ? _this.$store.state.user.tenantId : _this.staffForm.tenantId,
					}
					_this.showOverlay = true;
					_this.$modal.loading("数据保存中，请耐心等待...");
					if (_this.type == 'add') {
						setAccount(data).then((res) => {
							_this.$modal.closeLoading();
							_this.showOverlay = false;
							uni.showModal({
								title: '提示',
								showCancel: false,
								content: '您的员工已新增完成并已触发实名认证短信，请及时提醒您的员工进行认证，认证时效为15分钟。',
								confirmText: '知道了',
								confirmColor: '#fa6401',
								success: function (res) {
									uni.$emit('refresh', { refresh: true })
									_this.$tab.navigateBack()
								}
							})
						}).catch((error) => {
							_this.$modal.closeLoading();
							_this.showOverlay = false;
						}).finally(() => {
							_this.$modal.closeLoading();
							_this.showOverlay = false;
						})
					} else {
						setAccount(data).then((res) => {
							_this.$modal.closeLoading();
							_this.showOverlay = false;
							if (_this.oldData.phone != data.phone || _this.oldData.idCard != data.idCard) {
								uni.showModal({
									title: '提示',
									showCancel: false,
									content: '您的员工经检测改动了手机号和身份证号信息，现已重新触发实名认证短信，认证时效为15分钟。',
									confirmText: '知道了',
									confirmColor: '#fa6401',
									success: function (res) {
										uni.$emit('refresh', { refresh: true })
										_this.$tab.navigateBack()
									}
								})
							} else {
								uni.showModal({
									title: '提示',
									showCancel: false,
									content: '您的员工已修改完成。',
									confirmText: '知道了',
									confirmColor: '#fa6401',
									success: function (res) {
										uni.$emit('refresh', { refresh: true })
										_this.$tab.navigateBack()
									}
								})
							}
						}).catch((error) => {
							_this.$modal.closeLoading();
							_this.showOverlay = false;
						}).finally(() => {
							_this.$modal.closeLoading();
							_this.showOverlay = false;
						})
					}
				})
			},
			//重新认证
			handleauthentication() {
				let _this = this;
				let data = {
					userId: _this.staffForm.id
				}
				_this.showOverlay = true;
				_this.$modal.loading("重新认证中，请耐心等待...");
				getAuth(data).then((res) => {
					_this.$modal.closeLoading();
					_this.showOverlay = false;
					uni.showModal({
						title: '提示',
						showCancel: false,
						content: '您已重新触发实名认证短信，请及时提醒您的员工进行认证，认证时效为15分钟。',
						confirmText: '知道了',
						confirmColor: '#fa6401',
						success: function (res) {
							uni.$emit('refresh', { refresh: true })
							_this.$tab.navigateBack()
						}
					})
				}).catch((error) => {
					_this.$modal.closeLoading();
					_this.showOverlay = false;
				}).finally(() => {
					_this.$modal.closeLoading();
					_this.showOverlay = false;
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	/* #ifdef MP-WEIXIN */
	/deep/ .uni-card--border {
		border-bottom: none;
	}

	/* #endif */
	.add-staff {
		font-size: 28rpx;
	}

	.prompt-box {
		font-size: 24rpx;
		padding: 0 32rpx;

		.prompt-content {
			margin-top: 14rpx;
			flex-direction: column;
			color: #999999;

			text {
				text-indent: -1rem;
				margin-left: 1rem;
				line-height: 34rpx;
			}
		}
	}

	.title {
		display: flex;
		align-items: center;

		>text {
			color: #999999;
		}
	}

	.action-btn {
		text-align: center;
		width: 696rpx;
		margin: 0 auto;
		margin-top: 24rpx;
	}

	.button {
		background: #FA6400;
		color: #fff;
		font-size: 32rpx;
	}

	.edit-button {
		background: #FFFFFF;
		color: #333333;
	}
</style>