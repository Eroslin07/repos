<template>
	<view class="container">
		<uni-list>
			<uni-list-item>
				<template v-slot:body>
					<view class="center">
						<view v-if="!avatar" class="cu-avatar xl round bg-white" @click="handleToAvatar">
							<view class="iconfont icon-people text-gray icon"></view>
						</view>
						<image v-if="avatar" @click="handleToAvatar" :src="avatar" class="cu-avatar xl round"
							mode="widthFix">
						</image>
					</view>
				</template>
			</uni-list-item>
			<uni-list-item>
				<template v-slot:body>
					<view class="list-item">
						<text>姓名</text>
						<text class="slot-box slot-text">{{user.nickname || ''}}</text>
					</view>
				</template>
			</uni-list-item>
			<uni-list-item>
				<template v-slot:body>
					<view class="list-item">
						<text>身份证号码</text>
						<text>{{(!eyeIsShow1 ? user.idCard.replace(/^(.{1})(?:\d+)(.{1})$/, "$1***********$2") : user.idCard) || ''}}</text>
					</view>
				</template>
				<template v-slot:footer>
					<view class="right">
						<text v-if="eyeIsShow1" class="iconfont icon-open-eye" @click="eyeIsShow1=!eyeIsShow1"></text>
						<text v-else class="iconfont icon-close-eye" @click="eyeIsShow1=!eyeIsShow1"></text>
					</view>
				</template>
			</uni-list-item>
			<uni-list-item>
				<template v-slot:body>
					<view class="list-item">
						<text>手机号</text>
						<text class="slot-box slot-text">{{user.phone || ''}}</text>
					</view>
				</template>
			</uni-list-item>
			<uni-list-item v-if="type=='0'">
				<template v-slot:body>
					<view class="list-item">
						<text>营业执照号</text>
						<text class="slot-box slot-text">{{user.taxNum || ''}}</text>
					</view>
				</template>
			</uni-list-item>
			<uni-list-item>
				<template v-slot:body>
					<view class="list-item">
						<text>公司名称</text>
						<text class="slot-box slot-text">{{user.deptName || ''}}</text>
					</view>
				</template>
			</uni-list-item>
			<uni-list-item v-if="type=='0'">
				<template v-slot:body>
					<view class="list-item">
						<text>法定代表人</text>
						<text class="slot-box slot-text">{{user.legalRepresentative || ''}}</text>
					</view>
				</template>
			</uni-list-item>
			<uni-list-item v-if="type=='0'">
				<template v-slot:body>
					<view class="list-item">
						<text>市场所在地</text>
						<text class="slot-box slot-text">{{user.tenantName || ''}}</text>
					</view>
				</template>
			</uni-list-item>
			<uni-list-item v-if="type=='0'">
				<template v-slot:body>
					<view class="list-item">
						<text>开户行</text>
						<text>{{user.bankName || ''}}</text>
					</view>
				</template>
			</uni-list-item>
			<uni-list-item v-if="type=='0'">
				<template v-slot:body>
					<view class="list-item">
						<text>对公银行账号</text>
						<text>{{(!eyeIsShow2?user.bankAccount.replace(/\s*/g,"").replace(/^(.{1})(?:\d+)(.{1})$/, "$1***********$2"):user.bankAccount) || ''}}</text>
					</view>
				</template>
				<template v-slot:footer>
					<view class="right">
						<text v-if="eyeIsShow2" class="iconfont icon-open-eye" @click="eyeIsShow2=!eyeIsShow2"></text>
						<text v-else class="iconfont icon-close-eye" @click="eyeIsShow2=!eyeIsShow2"></text>
					</view>
				</template>
			</uni-list-item>
			<uni-list-item v-if="type=='0'">
				<template v-slot:body>
					<view class="list-item">
						<text>保证金充值卡</text>
						<text class="slot-box slot-text">{{(!eyeIsShow3?user.bondBankAccount.replace(/\s*/g,"").replace(/^(.{1})(?:\d+)(.{1})$/, "$1***********$2"):user.bondBankAccount) || ''}}</text>
					</view>
				</template>
				<template v-slot:footer>
					<view class="right">
						<text v-if="eyeIsShow3" class="iconfont icon-open-eye" @click="eyeIsShow3=!eyeIsShow3"></text>
						<text v-else class="iconfont icon-close-eye" @click="eyeIsShow3=!eyeIsShow3"></text>
					</view>
				</template>
			</uni-list-item>
			<uni-list-item v-if="type=='0'">
				<template v-slot:body>
					<view class="list-item">
						<text>联系地址</text>
						<text class="slot-box slot-text">{{ user.address || '' }}</text>
					</view>
				</template>
			</uni-list-item>
		</uni-list>
	</view>
</template>

<script>
	import { getUserInfo } from "@/api/system/mine"
	export default {
		data() {
			return {
				user: {
					idCard: '',
					bankAccount: '',
					bondBankAccount: ''
				},
				eyeIsShow1: false,
				eyeIsShow2: false,
				eyeIsShow3: false,
				type: '0'
			}
		},
		computed: {
			avatar() {
				return this.$store.state.user.avatar
			}
		},
		onLoad(options) {
			this.type = options.type
			this.getUser()
		},
		methods: {
			getUser() {
				getUserInfo({ userId: this.$store.state.user.id }).then(response => {
					this.user = response.data
				})
			},
			// 修改头像
			handleToAvatar() {
				this.$tab.navigateTo('/subPages/mine/avatar/index')
			},
		}
	}
</script>

<style lang="scss" scoped>
	page {
		background-color: #ffffff;
	}

	.cu-avatar {
		border: 2px solid #1296db;

		.icon {
			font-size: 40px;
		}
	}

	.list-item {
		width: 100%;
		display: flex;
		align-items: center;
		font-size: 28rpx;

		>text:first-child {
			width: 238rpx;
		}
	}
	.center{
		width: 100%;
		display: flex;
		justify-content: center;
		align-items: center;
	}
	.right {
		float: right;
		width: 36rpx;
	}
</style>