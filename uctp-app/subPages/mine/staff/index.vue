<template>
	<view class="container">
		<view class="xinzeng" @click="handleAdd">
			<u--text style="font-size:12px;" prefixIcon="plus"
				iconStyle="font-size: 16px; color: #FA6400; margin-right: 5px"
				text="新增" color="#FA6400"></u--text>
		</view>
		<view class="user_list">
			<u-swipe-action>
				<u-swipe-action-item
					v-for="(item, index) in list"
					:key="index"
					:options="options1"
					@click="handleClick(index)"
				>
					<view class="user">
						<view style="float: left;">
							<text :class="item.status ? 'ren' : 'wei'">认</text>
							<text>{{ item.name }}</text>
							<text>{{ item.phone }}</text>
						</view>
						<view style="float: right;">
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
			float: right;
			margin-right: 27px;
			height: 45px;
			line-height: 45px;
		}
		
		.user_list {
			margin-top: 45px;
			border-top: 1px solid #F5F5F5;
			
			.user {
				margin: 0 15px;
				padding: 15px 0;
				overflow: hidden;
				border-bottom: 1px solid #F5F5F5;
				
				text {
					margin-right: 10px;
				}
				
				.ren {
					margin-left: 10px;
					display: inline-block;
					height: 24px;
					line-height: 24px;
					width: 24px;
					text-align: center;
					border-radius: 12px;
					background-color: #fee0cc;
					color: #FA6400;
				}
				
				.wei {
					margin-left: 10px;
					display: inline-block;
					height: 24px;
					line-height: 24px;
					width: 24px;
					text-align: center;
					border-radius: 12px;
					background-color: #e5e5e5;
					color: #000;
				}
				
				.zhengchang {
					display: inline-block;
					height: 24px;
					line-height: 24px;
					width: 48px;
					text-align: center;
					border-radius: 13px;
					background-color: #fee0cc;
					color: #FA6400;
				}
				
				.tingyong {
					display: inline-block;
					height: 24px;
					line-height: 24px;
					width: 48px;
					text-align: center;
					border-radius: 13px;
					background-color: #e5e5e5;
					color: #000;
				}
			}
		}
	}
	.footer {
		line-height: 36px;
	}
</style>