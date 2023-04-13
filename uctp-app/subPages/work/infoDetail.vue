<template>
	<view class="info-detail-container">
		<!-- 标题 -->
		<view class="text-title">{{detailObj.title}}</view>
		<view class="content-box">
			<view>
				<text>{{detailObj.content}}</text>
				<text v-if="detailObj.url" class="navigator-box"
					@click="handleNavigate(detailObj.url)">{{ detailObj.url }}</text>
			</view>
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
			this.detailObj = JSON.parse(decodeURIComponent(options.item))
		},
		methods: {
			//跳转
			handleNavigate(url) {
				let index = url.indexOf('/')
				this.$tab.navigateTo(url.substring(index))
			}
		}
	}
</script>

<style lang="scss" scoped>
	.info-detail-container {
		width: 100%;
		padding: 10px 15px;

		.content-box {
			width: 100%;
			margin-top: 20px;
			text-indent: 28px;
			color: #626262;
			word-wrap: break-word;
			word-break: break-all;
			white-space: normal;
		}

		.text-title {
			text-align: center;
			font-size: 16px;
		}

		.footer {
			margin-top: 20px;
			text-align: right;
			color: #626262;
		}

		.navigator-box {
			color: #007BFF;
			text-decoration: underline;
		}
	}
</style>
