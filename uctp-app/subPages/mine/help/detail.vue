<template>
	<view class="content">
		<view class="steps">
			<view style="color: #000;margin-bottom: 10px;">处理进度</view>
			<u-steps current="0" direction="column" dot activeColor="#50a8bc">
				<u-steps-item title="发起反馈" desc="2023-03-24 15:09:18">
				</u-steps-item>
				<u-steps-item title="收到反馈" desc="2023-03-24 15:09:18">
				</u-steps-item>
			</u-steps>
		</view>
		<view>
			<view style="color: #000;margin-bottom: 10px;">反馈描述</view>
			<view class="title">系统方向是否可以更优的操作。</view>
		</view>
	</view>
</template>

<script>
</script>

<style lang="scss" scoped>
	.content {
		padding: 10px;
		
		.steps {
			border-bottom: 1px solid #ddd;
			margin-bottom: 10px;
		}
	}
</style>