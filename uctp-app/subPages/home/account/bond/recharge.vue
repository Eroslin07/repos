<template>
	<view class="recharge">
		<!-- 自定义导航栏 -->
		<u-navbar title="保证金充值" @leftClick="back" border safeAreaInsetTop fixed placeholder></u-navbar>
		<uni-card>
			<view>
				<view>充值金额</view>
				<view style="height: 30rpx;">{{amountText}}</view>
				<u-input border="none" v-model="amount" type="digit" :focus="true" clearable
					:customStyle="{'height': '50px'}" @input="handleInput" fontSize="24px" :maxlength='maxlength'>
					<u--text text="￥" slot="prefix" margin="0 3px 0 0" type="tips"></u--text>
				</u-input>
			</view>
			<view style="margin-top: 10px;" v-if="type == 2">上传银行电子回单</view>
			<view style="margin-top: 10px;" v-if="type == 2">
				<u-upload :fileList="fileList1" @afterRead="afterRead" @delete="deletePic" name="1" multiple
					:maxCount="3"></u-upload>
			</view>
		</uni-card>
		<u-popup :show="show" @close="close" :customStyle="{ 'height': '240px' }">
			<view style="position: absolute;top: 10px;left: 10px;" @click="close"><u-icon name="close"></u-icon></view>
			<view style="text-align: center;margin: 10px 0;">请选择充值方式</view>
			<uni-card>
				<view style="overflow: hidden;padding: 10px 0;border-bottom: 1px solid #f5f5f5;" @click="handleWeixin">
					<view style="float: left;">
						<image src="../../../../static/images/account/wxzf.png" class="form-image"
							style="width: 26px;height: 26px;"></image>
						<text style="margin-left: 10px;">微信支付</text>
					</view>
					<view style="float: right;padding-top: 5px;">
						<u--text prefixIcon="arrow-right" iconStyle="font-size: 19px"></u--text>
					</view>
				</view>
				<view style="overflow: hidden;padding: 10px 0;" @click="handleYunshanfu">
					<view style="float: left;">
						<image src="../../../../static/images/account/ysf.png" class="form-image"
							style="width: 26px;height: 26px;"></image>
						<text style="margin-left: 10px;">云闪付</text>
					</view>
					<view style="float: right">
						<u--text prefixIcon="arrow-right" iconStyle="font-size: 19px"></u--text>
					</view>
				</view>
			</uni-card>
		</u-popup>
		<view style="padding: 20px;">
			<button class="button" @click="handleDefine">充值</button>
		</view>
	</view>
</template>

<script>
	import config from '@/config'
	import {
		deleteImage
	} from '@/api/register'
	import {
		getRecharge,
		getDetail
	} from '@/api/account/bond.js'
	export default {
		data() {
			return {
				amount: '',
				show: false,
				revision: 0,
				amountText: '',
				maxlength: 12,
				fileList1: [],
			}
		},
		onLoad(options) {
			if (options.revision) {
				this.revision = options.revision;
			} else {
				getDetail({ accountNo: this.$store.state.user.accountNo }).then((res) => {
					this.revision = res.data.revision;
				})
			}
		},
		computed: {
			type() {
				return this.$store.state.user.paymentType
			}
		},
		methods: {
			// 页面返回
			back() {
				// this.$tab.redirectTo('/subPages/home/account/bond/bond');
				this.$tab.navigateBack();
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
				}
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
			// 确定
			handleDefine() {
				if (this.amount == '' || !this.amount) {
					this.$modal.msg("请输入需要充值的金额");
				} else {
					if (this.fileList1.length == 0 && this.type == 2) {
						this.$modal.msg("请上传银行电子回单");
						return
					}
					let file = this.fileList1.map((item) => {
						return {
							fileId: item.id,
							fileUrl: item.url
						}
					})
					let data = {
						accountNo: this.$store.state.user.accountNo,
						tranAmount: Number(this.amount * 100),
						revision: this.revision,
						invoiceFiles: this.type == 2 ? file : []
					}
					getRecharge(data).then((res) => {
						this.$modal.msg("充值成功");
						uni.$emit('refresh', {
							refresh: true
						});
						
						// 从收车合同签章进入需要调用
						let pages = getCurrentPages();
						let prevPage = pages[pages.length - 2];//上一个页面
						//直接调用上一个页面的setData()方法，把数据存到上一个页面中去
						prevPage.setData({
							isRefresh: 1 
						})
						
						this.$tab.navigateBack();
					})
					// this.show = true;
				}
			},
			// 关闭弹框
			close() {
				this.show = false;
			},
			// 微信支付
			handleWeixin() {
				this.$modal.msg("微信支付");
				let data = {
					accountNo: this.$store.state.user.accountNo,
					tranAmount: Number(this.amount * 100),
					revision: this.revision
				}
				getRecharge(data).then((res) => {
					uni.$emit('refresh', {
						refresh: true
					});
					this.$tab.navigateBack();
				})
			},
			// 云闪付
			handleYunshanfu() {
				this.$modal.msg("云闪付");
				let data = {
					accountNo: this.$store.state.user.accountNo,
					tranAmount: Number(this.amount * 100),
					revision: this.revision
				}
				getRecharge(data).then((res) => {
					uni.$emit('refresh', {
						refresh: true
					});
					this.$tab.navigateBack();
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.recharge {
		border-top: 1px solid #f5f5f5;

		.button {
			background-color: #fa6401;
			color: #fff;
			margin: 10px 0;
		}
	}
</style>