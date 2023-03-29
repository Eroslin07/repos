<template>
	<view>
		<uni-list>
			<uni-list-item title="市场所在地">
				<template v-slot:footer>
					<view>{{ location }}</view>
				</template>
			</uni-list-item>
			<uni-list-item title="市场位置">
				<template v-slot:footer>
					<view>{{ seat }}</view>
				</template>
			</uni-list-item>
		</uni-list>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				location: '山西万国',
				seat: '市场A区1#-5#'
			}
		}
	}
</script>

<style lang="scss" scoped>
	
</style>