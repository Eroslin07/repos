<template>
	<view class="info-detail-container">
		<!-- 标题 -->
		<view class="text-title">{{detailObj.title}}</view>
		<view class="content-box">
			<rich-text :nodes="detailObj.content"></rich-text>
		</view>
		<view class="footer">
			{{detailObj.createTime}}
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				detailObj: {},
			}
		},
		onLoad(options) {
			console.log(JSON.parse(decodeURIComponent(options.item)))
			this.detailObj = JSON.parse(decodeURIComponent(options.item))
		}
	}
</script>

<style lang="scss" scoped>
	.info-detail-container {
		width: 100%;
		padding: 10px 15px;

		.content-box {
			margin-top: 20px;
			text-indent:28px;
			color:#626262;
		}

		.text-title {
			text-align: center;
			font-size: 16px;
		}
		
		.footer{
			margin-top:20px;
			text-align:right;
			color:#626262;
		}
	}
</style>
