<template>
	<view>
		{{ type }}
	</view>
</template>

<script>
	export default {
		data() {
			return {
				type: null
			}
		},
		onLoad(options) {
			this.type = options.type;
			if (options.type == '收车') {
				uni.setNavigationBarTitle({
					title: '收车合同'
				});
			} else if (options.type == '卖车') {
				uni.setNavigationBarTitle({
					title: '卖车合同'
				});
			}
		}
	}
</script>

<style>
</style>