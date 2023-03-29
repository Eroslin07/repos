<template>
	<view>
		<uni-card :is-shadow="false" is-full style="border: none;">
			<u--form
				labelPosition="left"
				:model="staffForm"
				:rules="rules"
				ref="staffForm"
				labelWidth="60px"
			>
				<u-form-item label="姓名" prop="name" borderBottom>
					<u-input v-model="staffForm.name" border="none" placeholder="请输入姓名"></u-input>
				</u-form-item>
				<u-form-item label="手机号" prop="phone" borderBottom>
					<u--input v-model="staffForm.phone" border="none" placeholder="请输入11位手机号"></u--input>
				</u-form-item>
				<u-form-item label="密码" prop="password" borderBottom>
					<u--input v-model="staffForm.password" password border="none" placeholder="请输入密码"></u--input>
				</u-form-item>
			</u--form>
		</uni-card>
		<view class="footer">
			<view class="action-btn">
			  <button @click="handleSave" class="button">保存</button>
			</view>
			<view class="action-btn">
			  <button @click="handleCancel" class="button">取消</button>
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
					password: ''
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
					password: {
						type: 'string',
						required: true,
						message: '请填写密码',
						trigger: ['blur']
					},
				}
			}
		},
		methods: {
			// 保存
			handleSave() {
				this.$refs.staffForm.validate().then(res => {
					
				})
			},
			//取消
			handleCancel() {
				
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
	
	.footer {
		width: 100%;
		position: fixed;
		bottom: 0;
		
		.action-btn {
			text-align: center;
			margin-top: 20px;
		}
		
		.button {
			background-color: #68b4c5;
			color: #fff;
		}
	}
</style>