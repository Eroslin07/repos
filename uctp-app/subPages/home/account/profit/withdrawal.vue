<template>
	<view class="withdrawal">
		<uni-card>
			<view style="margin-bottom: 10px;">到账银行卡</view>
			<view>兴业银行（***1167）</view>
		</uni-card>
		<uni-card>
			<view>
				<view>提现金额</view>
				<u-input
					border="none"
					clearable
					:customStyle="{'height': '50px'}"
					fontSize="24px"
				>
					<u--text
						text="￥"
						slot="prefix"
						margin="0 3px 0 0"
						type="tips"
					></u--text>
					<template slot="suffix">
						<view style="color: #fa6401;">全部提现</view>
					</template>
				</u-input>
			</view>
			<view>可用利润余额13,000.00元。</view>
		</uni-card>
		<view style="padding: 20px;">
			<view v-if="type == 2">
				<u-upload
					:fileList="fileList1"
					@afterRead="afterRead"
					@delete="deletePic"
					name="1"
					multiple
					:maxCount="10"
				></u-upload>
			</view>
			<button v-if="type == 1" class="button" @click="handleDefine">提现</button>
			<button v-if="type == 2" class="button" @click="handleSubmit">提交申请</button>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				type: null,
				fileList1: []
			}
		},
		onLoad(options) {
			this.type = options.type;
			if (options.type == 1) {
				uni.setNavigationBarTitle({
					title: '保证金提现'
				});
			} else if (options.type == 2) {
				uni.setNavigationBarTitle({
					title: '利润提现'
				});
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
			// 确定
			handleDefine() {
				this.$tab.navigateTo('/subPages/home/account/progress?type=' + 1);
			},
			// 提交申请
			handleSubmit() {
				this.$tab.navigateTo('/subPages/home/account/progress?type=' + 2);
			}
		}
	}
</script>

<style lang="scss" scoped>
	.withdrawal {
		border-top: 1px solid #f5f5f5;
		
		.button {
			background-color: #fa6401;
			color: #fff;
			margin: 10px 0;
		}
	}
</style>