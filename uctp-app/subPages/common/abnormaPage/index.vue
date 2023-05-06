<template>
	<view class="abnormal">
		<view v-if="isSHowTip=='onLoading'" class="tips">
			<image src="/static/images/home/car-loading2.gif" mode=""></image>
			<view>正在加载...</view>
		</view>
		<view v-if="isSHowTip=='noData'" class="tips">
			<image src="/static/images/error/noData.png" mode=""></image>
			<view>暂无数据</view>
		</view>
		<view v-if="isSHowTip=='noMsg'" class="tips">
			<image src="/static/images/error/noMsg.png" mode=""></image>
			<view>暂无消息</view>
		</view>
		<view v-if="isSHowTip=='webError'" class="tips">
			<image src="/static/images/error/webError.png" mode=""></image>
			<view>网络异常</view>
		</view>
		<view v-if="isSHowTip=='sysError'" class="tips">
			<image src="/static/images/error/error.png" mode=""></image>
			<view>系统异常</view>
		</view>
		<view v-if="isSHowTip=='signing'" class="tips">
			<image src="@/subPages/static/images/bycar/signing.gif" mode=""></image>
			<view class="contract-tips">合同签署中...</view>
		</view>
		<view v-if="isSHowTip=='creating'" class="tips">
			<image src="@/subPages/static/images/bycar/creating.gif" mode="widthFix" ></image>
			<view class="contract-tips">合同生成中...</view>
		</view>
		<view v-if="isSHowTip=='createFail'" class="tips">
			<image src="@/subPages/static/images/bycar/createFail.ico" mode="widthFix" ></image>
			<view class="contract-tips">合同生成失败 ！</view>
		</view>
		<view v-if="isSHowTip=='signFail'" class="tips">
			<image src="@/subPages/static/images/bycar/signFail.ico" mode="widthFix" ></image>
			<view class="contract-tips">抱歉,电子合同签属失败 ！</view>
		</view>
		<view v-if="isSHowTip=='signSuccess'" class="tips">
			<image src="@/subPages/static/images/bycar/signSuccess.ico" mode="widthFix" ></image>
			<view class="contract-tips suss-tips">恭喜您，电子合同签属成功！</view>
		</view>
	</view>
</template>

<script>
	export default{
		props:{
			isSHowTip:{
				type:String,
				default:''
			}
		}
	}
</script>

<style lang="scss" scoped>
	.abnormal{
		width:100%;
		height:100%;
		.tips{
			margin-top:40%;
			margin-left:50%;
			text-align: center;
			transform: translateX(-50%);
			image{
				width: 224rpx;
				height:208rpx;
			}
			view{
				font-size: 28rpx;
				color: #999999;
				line-height: 40rpx;
			}
			.contract-tips{
				padding-top:30rpx;
			}
			.suss-tips{
				color:#E9B856;
			}
		}
	}	
</style>