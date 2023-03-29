<template>
	<view class="about-container">
		<uni-card :is-shadow="false" is-full style="border: none;">
			<u--text text="业务问题联系热线：XXXXXXXXXX"></u--text>
			<u--text text="系统问题联系热线：XXXXXXXXXX"></u--text>
			<u--text text="运维QQ群：XXXXXXXX"></u--text>
		</uni-card>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				version: getApp().globalData.config.appInfo.version
			}
		}
	}
</script>

<style lang="scss">
	/* #ifdef MP-WEIXIN */
	/deep/ .uni-card--border {
		border-bottom: none;
	}
	/* #endif */
</style>
