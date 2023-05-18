<template>
	<view class="mine-container">
		<!-- 自定义导航栏 -->
		<u-navbar title="我的" bgColor="rgba(0, 0, 0, 0)" :titleStyle="{'color': '#000'}" safeAreaInsetTop fixed>
			<view class="u-nav-slot" slot="left">
				<view class="bank-logo"></view>
			</view>
		</u-navbar>
		<!--顶部个人信息栏-->
		<view class="header-section" style="position: relative;">
			<image src="../../static/images/mine/my-bg.png" class="my_image"></image>
			<view class="flex padding justify-between my-title">
				<view class="flex align-center">
					<view v-if="!avatar" class="cu-avatar xl round bg-white" @click="handleToAvatar">
						<view class="iconfont icon-people text-gray icon"></view>
					</view>
					<!-- <image v-if="avatar" @click="handleToAvatar" :src="avatar" class="cu-avatar xl round"
						mode="widthFix">
					</image> -->
					<view v-if="!user.name" @click="handleToLogin" class="login-tip">
						点击登录
					</view>
					<view v-if="user.name" @click="handleToInfo" class="user-info">
						<view class="u_title">
							用户名：{{ user.name }}
						</view>
					</view>
				</view>
				<view class="flex authentication-box">
					<view class="flex authentication" v-if="type=='1'" :class="registerType=='0'?'active':''">
						<image
							:src="registerType=='0'?'../../static/images/mine/slices-active.png':'../../static/images/mine/slices.png'"
							mode="" style="width: 32rpx;height: 32rpx;">
						</image>
						<text>企业</text>
					</view>
					<view class=" flex authentication" v-if="type=='2'" :class="registerType=='0'?'active':''">
						<image
							:src="registerType=='0'?'../../static/images/mine/slices-active.png':'../../static/images/mine/slices.png'"
							mode="" style="width: 32rpx;height: 32rpx;"></image>
						<text>个人</text>
					</view>
				</view>
			</view>
		</view>
		<view class="content-section">
			<view class="menu-list">
				<view class="list-cell list-cell-arrow" @click="handleToInfo">
					<view class="menu-item-box">
						<image src="../../static/images/mine/information.png"
							style="width: 30rpx;height: 30rpx;margin-right: 20rpx;"></image>
						<view>个人信息</view>
					</view>
				</view>
				<view class="list-cell list-cell-arrow" @click="handleToStaff" v-if="type=='1'">
					<view class="menu-item-box">
						<image src="../../static/images/mine/staff.png"
							style="width: 30rpx;height: 30rpx;margin-right: 20rpx;"></image>
						<view>员工管理</view>
					</view>
				</view>
				<view class="list-cell list-cell-arrow" @click="handleToPos" v-if="type=='1'">
					<view class="menu-item-box">
						<image src="../../static/images/mine/pos.png"
							style="width: 30rpx;height: 30rpx;margin-right: 20rpx;"></image>
						<view>POS机管理</view>
					</view>
				</view>
				<!-- <view class="list-cell list-cell-arrow" @click="handleToStore">
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
				</view> -->
			</view>
		</view>
		<view class="cu-list menu">
			<view class="cu-item item-box" style="background-color: red;">
				<view class="content text-center" @click="handleLogout">
					<text class="text-white">退出登录</text>
				</view>
			</view>
		</view>
		<!-- 自定义tabbar -->
		<tab-bar :name="type == 1 ? 3 : 1" :type="type"></tab-bar>
	</view>
</template>

<script>
	import storage from '@/utils/storage'

	export default {
		data() {
			return {
				user: this.$store.state.user,
				version: getApp().globalData.config.appInfo.version
			}
		},
		computed: {
			avatar() {
				return this.$store.state.user.avatar
			},
			windowHeight() {
				return uni.getSystemInfoSync().windowHeight - 50
			},
			type() {
				return this.$store.state.user.staffType
			},
			registerType() {
				return this.$store.state.user.registerType
			}
		},
		methods: {
			// 个人信息
			handleToInfo() {
				this.$tab.navigateTo(`/subPages/mine/info/index`)
			},
			// 员工管理
			handleToStaff() {
				this.$tab.navigateTo('/subPages/mine/staff/index')
			},
			// pos机设备
			handleToPos() {
				this.$tab.navigateTo('/subPages/mine/pos/index')
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
				this.$modal.confirm('确定退出系统吗？').then(() => {
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
		}
	}
</script>

<style lang="scss" scoped>
	.mine-container {
		.my_image {
			width: 100%;
			height: 524rpx;
		}

		.header-section {
			.my-title {
				position: absolute;
				top: 188rpx;
				width: 694rpx;
				left: 50%;
				transform: translateX(-50%);
				background: linear-gradient(180deg, rgba(255, 255, 255, 0.4) 0%, #FFFFFF 100%);
				border-radius: 24rpx;
				border: 2rpx solid #FFFFFF;

				>view {
					justify-content: center;
				}

				flex-direction: column;

				.authentication-box {
					align-items: center;
					justify-content: center;
					margin-top: 12rpx;

					.authentication {
						background: rgba(255, 255, 255, 0.2);
						border-radius: 21rpx;
						align-items: center;
						font-size: 20rpx;
						width: 116rpx;
						height: 40rpx;
						color: #999999;
						justify-content: center;
						border: 2rpx solid #DBDBDB;

						>text {
							margin-left: 4rpx;
						}

						&:first-child {
							margin-right: 24rpx;
						}

						&.active {

							color: #FA6400;
							background: rgba(250, 100, 0, 0.2);
							border: 2rpx solid rgba(250, 100, 0, 0.5);
						}
					}
				}
			}

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
			margin-top: -136rpx;

			.menu-list {
				width: 694rpx;
				background: linear-gradient(180deg, rgba(255, 255, 255, 0.4) 0%, #FFFFFF 100%);
				border-radius: 24rpx;
				border: 2rpx solid #F7F7F7;
				padding-left: 36rpx;

				.list-cell {
					padding-left: 0;
					border-bottom: 2rpx solid #F5F5F5;

					:last-child {
						border: none;
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