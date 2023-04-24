<template>
	<view class="add-staff">
		<uni-card :is-shadow="false" is-full style="border: none;">
			<u--form labelPosition="left" :model="staffForm" :rules="rules" ref="staffForm" labelWidth="140rpx"
				:labelStyle="{fonsSzie:'28rpx',color:'#222222'}">
				<u-form-item label="姓名" prop="name" borderBottom>
					<u-input v-model="staffForm.name" border="none" placeholder="请输入姓名"></u-input>
				</u-form-item>
				<u-form-item label="手机号" prop="phone" borderBottom>
					<u--input v-model="staffForm.phone" border="none" placeholder="请输入11位手机号"></u--input>
				</u-form-item>
				<u-form-item label="身份证号" prop="IDNumber" borderBottom>
					<u--input v-model="staffForm.IDNumber" border="none" placeholder="请输入身份证号"></u--input>
				</u-form-item>
				<u-form-item label="是否停用" prop="status" borderBottom>
					<uni-data-checkbox v-model="staffForm.status" selectedColor="#FA6400"
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
	</view>
</template>

<script>
	export default {
		data() {
			return {
				staffForm: {
					name: '',
					phone: '',
					IDNumber: '',
					status: '0'
				},
				rules: {
					name: {
						type: 'string',
						required: true,
						message: '请填写姓名',
						trigger: ['blur']
					},
					phone: {
						type: 'string',
						required: true,
						message: '请填写手机号',
						trigger: ['blur']
					},
					IDNumber: {
						type: 'string',
						required: true,
						message: '请填写密码',
						trigger: ['blur']
					},
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
				type: ''
			}
		},
		onLoad(options) {
			console.log(options)
			this.type = options.type
		},
		methods: {
			// 保存
			handleSave() {
				this.$refs.staffForm.validate().then(res => {
					if (this.type == 'add') {
						uni.showModal({
							title: '提示',
							showCancel: false,
							content: '您的员工已新增完成并已触发实名认证短信，请及时提醒您的员工进行认证，认证时效为15分钟。',
							confirmText: '知道了',
							confirmColor: '#fa6401'
						})
					}else{
						uni.showModal({
							title: '提示',
							showCancel: false,
							content: '您的员工已修改完成。',
							confirmText: '知道了',
							confirmColor: '#fa6401'
						})
					}
				})
			},
			//重新认证
			handleauthentication() {
				uni.showModal({
					title: '提示',
					showCancel: false,
					content: '您已重新触发实名认证短信，请及时提醒您的员工进行认证，认证时效为15分钟。',
					confirmText: '知道了',
					confirmColor: '#fa6401'
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