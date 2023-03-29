<template>
	<view class="result">
		<uni-card
			:is-shadow="false"
			is-full
			title="意见反馈"
			extra="已收到 >"
			@click="handleCard"
		>
			<view class="title">系统方向是否可以更优的操作。</view>
			<view class="note">反馈时间：2023-03-24</view>
		</uni-card>
	</view>
</template>

<script>
	export default {
		methods: {
			handleCard() {
				this.$tab.navigateTo('/subPages/mine/help/detail');
			}
		}
	}
</script>

<style lang="scss" scoped>
	.title {
		color: #000;
		font-size: 16px;
	}
	
	.note {
		color: #a19d9d;
	}
	
	/deep/ .uni-card .uni-card__header {
		border-bottom: none;
	}
	
	/deep/ .uni-card__header-extra-text {
		color: #169bd5 !important;
		font-size: 14px !important;
	}
</style>