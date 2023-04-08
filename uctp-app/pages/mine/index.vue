<template>
	<view class="mine-container" :style="{height: `${windowHeight}px`}">
		<!--顶部个人信息栏-->
		<view class="header-section">
			<view class="flex padding justify-between">
				<view class="flex align-center">
					<view v-if="!avatar" class="cu-avatar xl round bg-white"  @click="handleToAvatar">
						<view class="iconfont icon-people text-gray icon"></view>
					</view>
					<image v-if="avatar" @click="handleToAvatar" :src="avatar" class="cu-avatar xl round"
						mode="widthFix">
					</image>
					<view v-if="!name" @click="handleToLogin" class="login-tip">
						点击登录
					</view>
					<view v-if="name" @click="handleToInfo" class="user-info">
						<view class="u_title">
							用户名：{{ name }}
						</view>
					</view>
				</view>
				<view @click="handleToInfo" class="flex align-center">
					<text>个人信息</text>
					<view class="iconfont icon-right"></view>
				</view>
			</view>
		</view>

		<view class="content-section">
			<view class="mine-actions grid col-2 text-center">
				<view class="action-item" @click="handleCollection">
					<view class="text">收车15辆 ></view>
				</view>
				<view class="action-item" @click="handleSelling">
					<text class="text">卖车10辆 ></text>
				</view>
			</view>

			<view class="menu-list">
				<view class="list-cell list-cell-arrow" @click="handleToInfo">
					<view class="menu-item-box">
						<view class="iconfont icon-user menu-icon"></view>
						<view>个人信息</view>
					</view>
				</view>
				<view class="list-cell list-cell-arrow" @click="handleToStaff">
					<view class="menu-item-box">
						<view class="iconfont icon-user menu-icon"></view>
						<view>员工管理</view>
					</view>
				</view>
				<view class="list-cell list-cell-arrow" @click="handleToStore">
					<view class="menu-item-box">
						<view class="iconfont icon-user menu-icon"></view>
						<view>门店管理</view>
					</view>
				</view>
				<view class="list-cell list-cell-arrow" @click="handleHelp">
					<view class="menu-item-box">
						<view class="iconfont icon-help menu-icon"></view>
						<view>意见反馈</view>
					</view>
				</view>
				<view class="list-cell list-cell-arrow" @click="handleAbout">
					<view class="menu-item-box">
						<view class="iconfont icon-aixin menu-icon"></view>
						<view>系统联系人</view>
					</view>
				</view>
				<view class="list-cell list-cell-arrow" @click="handleToSetting">
					<view class="menu-item-box">
						<view class="iconfont icon-setting menu-icon"></view>
						<view>登录设置</view>
					</view>
				</view>
			</view>
		</view>
		<view class="cu-list menu">
			<view class="cu-item item-box" style="background-color: #68b4c5;">
				<view class="content text-center" @click="handleLogout">
					<text class="text-white">退出登录</text>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import storage from '@/utils/storage'

	export default {
		data() {
			return {
				name: this.$store.state.user.name,
				version: getApp().globalData.config.appInfo.version
			}
		},
		computed: {
			avatar() {
				return this.$store.state.user.avatar
			},
			windowHeight() {
				return uni.getSystemInfoSync().windowHeight - 50
			}
		},
		methods: {
			// 个人信息
			handleToInfo() {
				this.$tab.navigateTo('/subPages/mine/info/index')
			},
			// 员工管理
			handleToStaff() {
				this.$tab.navigateTo('/subPages/mine/staff/index')
			},
			// 门店管理
			handleToStore() {
				this.$tab.navigateTo('/subPages/mine/store/index')
			},
			// 登录设置
			handleToSetting() {
				this.$tab.navigateTo('/subPages/mine/setting/index')
			},
			// 点击登录
			handleToLogin() {
				// #ifndef MP-WEIXIN
				this.$tab.reLaunch('/pages/login')
				// #endif
				// #ifdef MP-WEIXIN
				this.$tab.reLaunch('/pages/wx_login')
				// #endif
			},
			// 修改头像
			handleToAvatar() {
				this.$tab.navigateTo('/subPages/mine/avatar/index')
			},
			// 退出登录
			handleLogout() {
				this.$modal.confirm('确定注销并退出系统吗？').then(() => {
					// // #ifndef MP-WEIXIN
					// this.$tab.reLaunch('/pages/login')
					// // #endif
					// // #ifdef MP-WEIXIN
					// this.$tab.reLaunch('/pages/wx_login')
					// // #endif
					// return
					this.$store.dispatch('LogOut').then(() => {
						this.$tab.reLaunch('/pages/index')
					})
				})
			},
			// 意见反馈
			handleHelp() {
				this.$tab.navigateTo('/subPages/mine/help/index')
			},
			// 系统联系人
			handleAbout() {
				this.$tab.navigateTo('/subPages/mine/about/index')
			},
			// 收车
			handleCollection() {
				this.$tab.navigateTo('/subPages/home/bycar/index')
			},
			// 卖车
			handleSelling() {
				this.$tab.navigateTo('/subPages/home/sellingCar/index')
			}
		}
	}
</script>

<style lang="scss" scoped>
	page {
		background-color: #f5f6f7;
	}
	
	.mine-container {
		width: 100%;
		height: 100%;
		background-color: #f5f6f7;

		.header-section {
			padding: 15px 15px 45px 15px;
			background-color: #3c96f3;
			color: white;

			.login-tip {
				font-size: 18px;
				margin-left: 10px;
			}

			.cu-avatar {
				border: 2px solid #eaeaea;

				.icon {
					font-size: 40px;
				}
			}

			.user-info {
				margin-left: 15px;

				.u_title {
					font-size: 18px;
					line-height: 30px;
				}
			}
		}

		.content-section {
			position: relative;
			top: -50px;

			.mine-actions {
				margin: 15px 15px;
				padding: 20px 0px;
				border-radius: 8px;
				background-color: white;

				.action-item {
					.icon {
						font-size: 28px;
					}

					.text {
						display: block;
						font-size: 16px;
						margin: 8px 0px;
					}
				}
			}
		}
		
		.item-box {
			background-color: #FFFFFF;
			margin: 30rpx;
			display: flex;
			flex-direction: row;
			justify-content: center;
			align-items: center;
			padding: 10rpx;
			border-radius: 8rpx;
			color: #303133;
			font-size: 32rpx;
		}
	}
</style>
