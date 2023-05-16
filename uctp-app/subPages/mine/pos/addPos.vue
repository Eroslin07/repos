<template>
	<view class="add-staff">
		<uni-card :is-shadow="false" is-full style="border: none;">
			<u--form labelPosition="left" :model="staffForm" :rules="rules" ref="staffForm" labelWidth="160rpx"
				:labelStyle="{fonsSzie:'28rpx',color:'#222222'}">
				<u-form-item label="POS机名称" prop="posName" borderBottom>
					<u-input v-model="staffForm.posName" border="none" placeholder="请输入POS机名称"></u-input>
				</u-form-item>
				<u-form-item label="POS机编号" prop="posId" borderBottom>
					<u--input v-model="staffForm.posId" type="number" border="none" placeholder="请输入POS机编号"></u--input>
				</u-form-item>
				<u-form-item label="备注" prop="remark" borderBottom>
					<u--input v-model="staffForm.remark" type="idcard" border="none" placeholder="请输入备注"></u--input>
				</u-form-item>
				<u-form-item label="是否停用" prop="status" borderBottom>
					<uni-data-checkbox v-model="staffForm.status" :disabled="type == 'add'" selectedColor="#FA6400"
						:localdata="statusList"></uni-data-checkbox>
				</u-form-item>
			</u--form>
		</uni-card>
		<view>
			<view class="action-btn">
				<button @click="handleSave" class="button">保存</button>
			</view>
		</view>
		
		<!-- 遮罩层 -->
		<u-overlay :show="showOverlay"></u-overlay>
	</view>
</template>

<script>
	import { addPos, getAuth } from "@/api/system/mine"
	export default {
		data() {
			return {
				staffForm: {
					posName: '',
					posId: '',
					remark: '',
					status: '0'
				},
				oldData: {},
				rules: {
					posName: {
						type: 'string',
						required: true,
						message: '请填写POS机名称',
						trigger: ['blur']
					},
					posId: [{
						type: 'string',
						required: true,
						message: '请填写POS机编号',
						trigger: ['blur']
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
				    title: "新增POS机设备"
				});
			} else if (options.type == 'edit') {
				uni.setNavigationBarTitle({
				    title: "修改POS机设备"
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
						posName: _this.staffForm.posName,
						posId: _this.staffForm.posId,
						remark: _this.staffForm.remark,
						status: _this.staffForm.status,
						deptId: _this.type == 'add' ? _this.$store.state.user.deptId : _this.staffForm.deptId,
						tenantId: _this.type == 'add' ? _this.$store.state.user.tenantId : _this.staffForm.tenantId,
					}
					_this.showOverlay = true;
					_this.$modal.loading("数据保存中，请耐心等待...");
					if (_this.type == 'add') {
						addPos(data).then((res) => {
							_this.$modal.closeLoading();
							_this.showOverlay = false;
							if (res.data.code) {
								uni.showModal({
									title: '提示',
									showCancel: false,
									content: res.data.msg,
									confirmText: '知道了',
									confirmColor: '#fa6401',
									success: function (res) {
										if (res.confirm) {
											_this.staffForm = {
												name: '',
												phone: '',
												idCard: '',
												status: '0'
											}
										}
									}
								})
								return
							}
							uni.showModal({
								title: '提示',
								showCancel: false,
								content: '您的POS机设备已新增完成。',
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
						addPos(data).then((res) => {
							_this.$modal.closeLoading();
							_this.showOverlay = false;
							if (res.data.code) {
								uni.showModal({
									title: '提示',
									showCancel: false,
									content: res.data.msg,
									confirmText: '知道了',
									confirmColor: '#fa6401',
									success: function (res) {
										if (res.confirm) {
											_this.staffForm = {
												name: '',
												phone: '',
												idCard: '',
												status: '0'
											}
										}
									}
								})
								return
							}
							uni.showModal({
								title: '提示',
								showCancel: false,
								content: '您的POS机设备已修改完成。',
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