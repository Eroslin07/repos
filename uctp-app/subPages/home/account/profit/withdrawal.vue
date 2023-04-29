<template>
	<view class="withdrawal">
		<uni-card>
			<view style="margin-bottom: 10px;">到账银行卡</view>
			<view>浦发银行（***1167）</view>
		</uni-card>
		<uni-card>
			<view>
				<view>提现金额</view>
				<u-input
					border="none"
					v-model="amount"
					type="digit"
					:focus="true"
					clearable
					:customStyle="{'height': '50px'}"
					@input="handleInput"
					fontSize="24px"
				>
					<u--text
						text="￥"
						slot="prefix"
						margin="0 3px 0 0"
						type="tips"
					></u--text>
					<template slot="suffix">
						<view style="color: #fa6401;" @click="handleQuanbu">全部提现</view>
					</template>
				</u-input>
			</view>
			<view>可用利润余额{{ $amount.getComdify(allAmount / 100) }}元。</view>
			<view style="margin-top: 10px;">上传利润发票</view>
			<view style="margin-top: 10px;">
				<u-upload
					:fileList="fileList1"
					@afterRead="afterRead"
					@delete="deletePic"
					name="1"
					multiple
					:maxCount="3"
				></u-upload>
			</view>
		</uni-card>
		<view style="padding: 20px;">
			<button class="button" @click="handleSubmit">提交申请</button>
		</view>
	</view>
</template>

<script>
	import config from '@/config'
	import { deleteImage } from '@/api/register'
	import { getPresent } from '@/api/account/profit.js'
	export default {
		data() {
			return {
				// 商户账户号
				accountNo: this.$store.state.user.accountNo,
				fileList1: [],
				amount: '',
				allAmount: 0
			}
		},
		onBackPress(options) {
			this.$tab.redirectTo('/subPages/home/account/profit/profit');
			return true;
		},
		onLoad(options) {
			this.allAmount = options.amount;
		},
		methods: {
			// 输入金额回调
			handleInput(val) {
				if (val) {
					this.$nextTick(() => {
						if (val.indexOf('.') > -1) {
							let arr = val.split('.');
							arr[1] = arr[1].slice(0, 2);
							this.amount = arr.join('.');
						}
					})
				}
			},
			// 删除图片
			deletePic(event) {
				deleteImage({ id: event.file.id }).then((res) => {
					this.$modal.msg("删除成功");
					this[`fileList${event.name}`].splice(event.index, 1);
				})
			},
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
						url: result[0].url,
						id: result[0].id
					}))
					fileListLen++
				}
			},
			uploadFilePromise(url) {
				return new Promise((resolve, reject) => {
					let a = uni.uploadFile({
						url: config.uploadUrl, // 仅为示例，非真实的接口地址
						filePath: url,
						name: 'file',
						success: (res) => {
							setTimeout(() => {
								let data = JSON.parse(res.data).data
								resolve(data)
							}, 1000)
						}
					});
				})
			},
			// 提交申请
			handleSubmit() {
				if (this.amount == "" || !this.amount) {
					this.$modal.msg("请输入需要提现的金额");
					return
				}
				if (this.fileList1.length == 0) {
					this.$modal.msg("请上传利润发票");
					return
				}
				if (parseFloat(this.amount) > parseFloat(this.allAmount / 100)) {
					this.$modal.msg("提现金额大于可用利润余额，请重新输入提现金额");
					return
				}
				let data = {
					accountNo: this.accountNo,
					merchantBankId: 2,
					amount: Number(this.amount * 100),
					invoiceFiles: this.fileList1.map((item) => { return {fileId: item.id, fileUrl: item.url} })
				}
				getPresent(data).then((res) => {
					this.$modal.msg("利润提现流程发起成功");
					this.$tab.navigateBack();
					// this.$tab.redirectTo('/subPages/home/account/profit/progress');
				})
			},
			// 点击全部提现
			handleQuanbu() {
				this.amount = this.allAmount / 100;
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