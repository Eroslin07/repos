<template>
	<view class="container">
		<uni-list>
			<u-swipe-action>
				<u-swipe-action-item
					v-for="(item, index) in list"
					:key="index"
					:options="options1"
					@click="handleClick(index)"
				>
					<uni-list-item
						:title="item.phone + item.name"
						:note="item.time"
					>
						<template v-slot:footer>
							<u-switch v-model="item.status" activeColor="#5bbed5" @change="change"></u-switch>
						</template>
					</uni-list-item>
				</u-swipe-action-item>
			</u-swipe-action>
		</uni-list>
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
					status: false
				}, {
					phone: '15328756761',
					name: '李四',
					time: '2023-03-15',
					status: false
				}, {
					phone: '15328756761',
					name: '李四',
					time: '2023-03-15',
					status: false
				}, {
					phone: '15328756761',
					name: '李四',
					time: '2023-03-15',
					status: false
				}]
			}
		},
		onNavigationBarButtonTap(e) {
			if (e.index == 0) {
				// 新增员工
				this.$tab.navigateTo('/subPages/mine/staff/addStaff')
			}
		},
		methods: {
			change() {
				
			},
			handleClick(i) {
				
			}
		}
	}
</script>

<style lang="scss" scoped>
	.footer {
		line-height: 36px;
	}
</style>