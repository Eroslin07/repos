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
						<text class="slot-box slot-text">{{user.name}}</text>
					</view>
				</template>
			</uni-list-item>
			<uni-list-item>
				<template v-slot:body>
					<view class="list-item">
						<text>身份证号码</text>
						<text>{{!eyeIsShow1?'32*****************1':user.name}}</text>
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
						<text class="slot-box slot-text">{{user.name}}</text>
					</view>
				</template>
			</uni-list-item>
			<uni-list-item v-if="type=='0'">
				<template v-slot:body>
					<view class="list-item">
						<text>营业执照号</text>
						<text class="slot-box slot-text">{{user.name}}</text>
					</view>
				</template>
			</uni-list-item>
			<uni-list-item>
				<template v-slot:body>
					<view class="list-item">
						<text>公司名称</text>
						<text class="slot-box slot-text">{{user.name}}</text>
					</view>
				</template>
			</uni-list-item>
			<uni-list-item v-if="type=='0'">
				<template v-slot:body>
					<view class="list-item">
						<text>法定代表人</text>
						<text class="slot-box slot-text">{{user.name}}</text>
					</view>
				</template>
			</uni-list-item>
			<uni-list-item v-if="type=='0'">
				<template v-slot:body>
					<view class="list-item">
						<text>市场所在地</text>
						<text class="slot-box slot-text">{{user.name}}</text>
					</view>
				</template>
			</uni-list-item>
			<uni-list-item v-if="type=='0'">
				<template v-slot:body>
					<view class="list-item">
						<text>开户行</text>
						<text>{{user.name}}</text>
					</view>
				</template>
			</uni-list-item>
			<uni-list-item v-if="type=='0'">
				<template v-slot:body>
					<view class="list-item">
						<text>对公银行账号</text>
						<text>{{!eyeIsShow2?'****9855':user.name}}</text>
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
						<text class="slot-box slot-text">{{!eyeIsShow3?'****9855':user.name}}</text>
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
						<text class="slot-box slot-text">太原市惠城区河南岸街道冷水坑村委会 楼下</text>
					</view>
				</template>
			</uni-list-item>
		</uni-list>
	</view>
</template>

<script>
	import {
		getUserProfile
	} from "@/api/system/user"

	export default {
		data() {
			return {
				user: {},
				roleGroup: "",
				postGroup: "",
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
			console.log(options.type == '0')
			this.type = options.type
			this.user = this.$store.state.user
		},
		methods: {
			getUser() {
				getUserProfile().then(response => {
					this.user = response.data
					this.roleGroup = response.roleGroup
					this.postGroup = response.postGroup
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