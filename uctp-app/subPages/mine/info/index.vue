<template>
	<view class="container">
		<uni-list>
			<uni-list-item showExtraIcon="true" :extraIcon="{type: 'person-filled'}" title="头像">
				<template v-slot:footer>
					<view v-if="!avatar" class="cu-avatar xl round bg-white" @click="handleToAvatar">
						<view class="iconfont icon-people text-gray icon"></view>
					</view>
					<image v-if="avatar" @click="handleToAvatar" :src="avatar" class="cu-avatar xl round"
						mode="widthFix">
					</image>
				</template>
			</uni-list-item>
			<uni-list-item showExtraIcon="true" :extraIcon="{type: 'person-filled'}" title="姓名" :rightText="user.nickName" />
			<uni-list-item showExtraIcon="true" :extraIcon="{type: 'person-filled'}" title="身份证号" :rightText="user.nickName" />
			<uni-list-item showExtraIcon="true" :extraIcon="{type: 'phone-filled'}" title="手机号" :rightText="user.phonenumber" />
			<!-- <uni-list-item showExtraIcon="true" :extraIcon="{type: 'email-filled'}" title="邮箱" :rightText="user.email" />
      <uni-list-item showExtraIcon="true" :extraIcon="{type: 'auth-filled'}" title="岗位" :rightText="postGroup" />
      <uni-list-item showExtraIcon="true" :extraIcon="{type: 'staff-filled'}" title="角色" :rightText="roleGroup" />
			<uni-list-item showExtraIcon="true" :extraIcon="{type: 'calendar-filled'}" title="创建日期" :rightText="user.createTime" /> -->
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
				postGroup: ""
			}
		},
		computed: {
			avatar() {
				return this.$store.state.user.avatar
			}
		},
		onLoad() {
			// this.getUser()
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
</style>
