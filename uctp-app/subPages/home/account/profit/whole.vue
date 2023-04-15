<template>
	<view class="whole">
		<u-sticky bgColor="#fff">
			<view style="border-bottom: 1px solid #f5f5f5;">
				<u-tabs :list="list" lineColor="#FA6400" @change="handleChange"></u-tabs>
			</view>
			
			<view style="padding: 10px;">
				<u-list style="height: 100%;">
					<u-list-item v-for="(item, index) in 10" :key="index">
						<view style="line-height: 30px;">
							<u-row justify="space-between" customStyle="margin-bottom: 10px;border-bottom: 1px solid #f5f5f5;">
								<u-col span="4">
									<view class="title">保证金提现</view>
									<view class="note">2023-03-17</view>
								</u-col>
								<u-col span="4">
									<view class="title" style="text-align: right;">+100,000 ></view>
								</u-col>
							</u-row>
						</view>
					</u-list-item>
				</u-list>
			</view>
		</u-sticky>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				list: [{
					name: '全部',
				}, {
					name: '支出',
				}, {
					name: '收入'
				}],
				index: 0
			}
		},
		methods: {
			handleChange(val) {
				this.index = val.index;
			}
		}
	}
</script>

<style lang="scss" scoped>
	.whole {
		border-top: 1px solid #f5f5f5;
	}
	
	/deep/ .u-tabs__wrapper__nav__item  {
		width: 33%;
	}
</style>