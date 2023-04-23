<template>
	<view class="container">
		<view class="xinzeng" @click="handleAdd">
			<u-icon class="" color="#FA6400" name="plus" style="font-size: 16px;"></u-icon>
			<text>新增</text>
		</view>
		<view class="user_list">
			<u-swipe-action>
				<u-swipe-action-item v-for="(item, index) in list" :key="index" :options="options1"
					@click="handleClick(index)">
					<view class="user flex">
						<view>
							<text :class="item.status ? 'ren' : 'wei'">认</text>
							<text>{{ item.name }}</text>
							<text>{{ item.phone }}</text>
						</view>
						<view >
							<text v-if="item.status" class="zhengchang">正常</text>
							<text v-else class="tingyong">停用</text>
						</view>
					</view>
				</u-swipe-action-item>
			</u-swipe-action>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				options1: [{
					text: '删除',
					style: {
						backgroundColor: '#f56c6c'
					}
				}],
				list: [{
					phone: '15328756761',
					name: '李四',
					time: '2023-03-15',
					status: true
				}, {
					phone: '15328756761',
					name: '李四',
					time: '2023-03-15',
					status: true
				}, {
					phone: '15328756761',
					name: '李四',
					time: '2023-03-15',
					status: true
				}, {
					phone: '15328756761',
					name: '李四',
					time: '2023-03-15',
					status: false
				}]
			}
		},
		methods: {
			handleAdd() {
				// 新增员工
				this.$tab.navigateTo('/subPages/mine/staff/addStaff')
			},
			handleClick(i) {

			}
		}
	}
</script>

<style lang="scss" scoped>
	.container {
		overflow: hidden;
		border-top: 1px solid #F5F5F5;

		.xinzeng {
			display: flex;
			justify-content: center;
			align-items: center;
			height: 45px;
			line-height: 45px;
			color: #FA6400;
			 text{
				 padding-left: 12rpx;
			 }
		}

		.user_list {
			border-top: 1px solid #F5F5F5;

			.user {
				margin: 0 15px;
				padding: 15px 0;
				overflow: hidden;
				border-bottom: 1px solid #F5F5F5;
				justify-content: space-between;
				align-items: center;
				text {
					margin-right: 10px;
				}

				.ren {
					box-sizing: border-box;
					margin-left: 10px;
					display: inline-block;
					line-height: 44rpx;
					width: 44rpx;
					height: 44rpx;
					background: rgba(250, 100, 0, 0.2);
					text-align: center;
					border-radius: 22px;
					color: #FA6400;
					border: 2rpx solid #FA6400;
					font-size: 24rpx;
				}

				.wei {
					margin-left: 10px;
					display: inline-block;
					line-height: 44rpx;
					width: 44rpx;
					height: 44rpx;
					text-align: center;
					border-radius: 22px;
					background: rgba(0, 0, 0, 0.1);
					color: #555555;
					font-size: 24rpx;
				}

				.zhengchang {
					display: inline-block;
					line-height: 40rpx;
					font-size: 24rpx;
					width: 88rpx;
					height: 40rpx;
					text-align: center;
					border-radius: 20rpx;
					background-color: #fee0cc;
					color: #FA6400;
				}

				.tingyong {
					display: inline-block;
					line-height: 40rpx;
					font-size: 24rpx;
					width: 88rpx;
					height: 40rpx;
					text-align: center;
					border-radius: 20rpx;
					background-color: #e5e5e5;
					color: #555555;
				}
			}
		}
	}

	.footer {
		line-height: 36px;
	}
</style>