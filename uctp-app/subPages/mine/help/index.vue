<template>
	<view class="help-container">
		<uni-card :is-shadow="false" is-full style="border: none;">
			<u--text text="请描述具体内容"></u--text>
			<u--textarea v-model="value" placeholder="请输入内容" count :maxlength="200" :confirmType="confirmType"></u--textarea>
			<u--text text="图片（选填，提供反馈相关截图）"></u--text>
			<u-upload :fileList="fileList1" :maxCount="4" @afterRead="afterRead" @delete="deletePic" name="1" multiple></u-upload>
		</uni-card>
		
		<view class="footer">
			<button @click="handleVerify" class="button">提交反馈</button>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				value: '',
				confirmType: 'done',
				fileList1: []
			}
		},
		onNavigationBarButtonTap(e) {
			if (e.index == 0) {
				// 查看反馈结果
				this.$tab.navigateTo('/subPages/mine/help/result')
			}
		},
		methods: {
			// 删除图片
			deletePic(event) {
				this[`fileList${event.name}`].splice(event.index, 1)
			},
			// 新增图片
			async afterRead(event) {
				// 当设置 multiple 为 true 时, file 为数组格式，否则为对象格式
				let lists = [].concat(event.file)
				let fileListLen = this[`fileList${event.name}`].length
				lists.map((item) => {
					this[`fileList${event.name}`].push({
						...item,
						status: 'uploading',
						message: '上传中'
					})
				})
				for (let i = 0; i < lists.length; i++) {
					const result = await this.uploadFilePromise(lists[i].url)
					let item = this[`fileList${event.name}`][fileListLen]
					this[`fileList${event.name}`].splice(fileListLen, 1, Object.assign(item, {
						status: 'success',
						message: '',
						url: result
					}))
					fileListLen++
				}
			},
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
			// 提交反馈
			handleVerify() {
				this.$tab.navigateTo('/subPages/mine/help/detail');
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
		
		.button {
			background-color: #68b4c5;
			color: #fff;
		}
	}
</style>
