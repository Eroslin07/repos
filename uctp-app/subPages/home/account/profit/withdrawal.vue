<template>
	<view class="withdrawal">
		<uni-card>
			<view style="margin-bottom: 10px;">到账银行卡</view>
			<view>{{ bankName || '--' }}（{{ bankNo || '--' }}）</view>
		</uni-card>
		<uni-card>
			<view>
				<view>提现金额</view>
				<view style="height: 30rpx;">{{amountText}}</view>
				<u-input border="none" v-model="amount" type="digit" :focus="true" clearable
					:customStyle="{'height': '50px'}" @input="handleInput" fontSize="24px" :maxlength='maxlength'>
					<u--text text="￥" slot="prefix" margin="0 3px 0 0" type="tips"></u--text>
					<template slot="suffix">
						<view style="color: #fa6401;" @click="handleQuanbu">全部提现</view>
					</template>
				</u-input>
			</view>
			<view style="height: 30rpx;color: red;font-size: 20rpx;">{{amountVisible?'输入金额超过利润余额':''}}</view>
			<view>可用利润余额{{ $amount.getComdify(allAmount / 100) }}元。</view>
			<view style="margin-top: 10px;">上传利润发票</view>
			<view style="margin-top: 10px;">
				<u-upload :fileList="fileList1" @afterRead="afterRead" @delete="deletePic" name="1" multiple
					:maxCount="3"></u-upload>
			</view>
		</uni-card>
		<view style="padding: 20px;">
			<button class="button" @click="handleSubmit">提交申请</button>
		</view>
	</view>
</template>

<script>
	import config from '@/config'
	import {
		deleteImage
	} from '@/api/register'
	import {
		getPresent,
		getBankInfo
	} from '@/api/account/profit.js'
	export default {
		data() {
			return {
				bankName: '',
				bankNo: '',
				// 商户账户号
				accountNo: this.$store.state.user.accountNo,
				fileList1: [],
				amount: '',
				allAmount: 0,
				amountText: '',
				amountVisible: false,
				maxlength: 12
			}
		},
		onBackPress(options) {
			this.$tab.redirectTo('/subPages/home/account/profit/profit');
			return true;
		},
		onLoad(options) {
			this.getBank();
			this.allAmount = options.amount;
		},
		methods: {
			// 获取利润提取银行账户
			getBank() {
				let data = {
					accountNo: this.accountNo,
					busType: '2'
				}
				getBankInfo(data).then((res) => {
					this.bankName = res.data.bankName;
					this.bankNo = res.data.bankNo;
				})
			},
			// 输入金额回调
			handleInput(val) {
				const texts = ['百', '千', '万', '十万', '百万', '千万', '亿', '十亿', '百亿', '千亿']
				if (val) {
					this.$nextTick(() => {
						if (parseFloat(val) > parseFloat(this.$amount.getDelcommafy(this.allAmount))) {
							this.amountVisible = true
						} else {
							this.amountVisible = false
						}
						if (val.indexOf('.') > -1) {
							let arr = val.split('.');
							if (arr[0].length > 2) {
								this.amountText = texts[arr[0].length - 3]
							} else {
								this.amountText = ''
							}
							if (arr[0].length > 9) {
								this.maxlength = 12
							} else {
								this.maxlength = arr[0].length + 3
							}
						} else {
							if (val.length > 2) {
								this.amountText = texts[val.length - 3]
							} else {
								this.amountText = ''
							}
							this.maxlength = 12
						}
					})
				} else {
					this.amountText = ''
					this.amountVisible = false
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
					// merchantBankId: 2,
					amount: Number(this.amount * 100),
					invoiceFiles: this.fileList1.map((item) => {
						return {
							fileId: item.id,
							fileUrl: item.url
						}
					})
				}
				getPresent(data).then((res) => {
					this.$modal.msg("利润提现流程发起成功");
					uni.$emit('refresh', {
						refresh: true
					});
					this.$tab.navigateBack();
					// this.$tab.redirectTo('/subPages/home/account/profit/progress');
				})
			},
			// 点击全部提现
			handleQuanbu() {
				this.amount = this.allAmount / 100;
				const texts = ['百', '千', '万', '十万', '百万', '千万', '亿', '十亿', '百亿', '千亿']
				if (this.amount) {
					if (this.amount.indexOf('.') > -1) {
						let arr = this.amount.split('.')
						if (arr[0].length > 2) {
							this.amountText = texts[arr[0].length - 3]
						} else {
							this.amountText = ''
						}
					} else {
						if (this.amount.length > 2) {
							this.amountText = texts[this.amount.length - 3]
						} else {
							this.amountText = ''
						}
					}
				} else {
					this.amountText = ''
				}

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