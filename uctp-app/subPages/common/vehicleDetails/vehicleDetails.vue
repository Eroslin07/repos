<template>
	<view class="car-info">
		<uni-card :is-shadow="false" is-full padding="0" spacing="0" style="height:100%">
			<view class="banner">
				<image class="image" src="@/static/images/car.jpg" :webp="true" mode="widthFix">
				</image>

			</view>
			<h3 class="car-title">宝马-宝马X1 2021款 sDrive20Li 时尚型</h3>
			<view class="car-status">
				<text>车辆状态：</text>
				<text @click="showAndHide">折叠/展开</text>
			</view>
			<view class="info-box">
				<!-- 折叠信息 -->
				<view v-if="!infoIsSHow" class="info-list" v-for="(item,index) in listData" :key="index">
					<view class="info-img">
						<image class="left-image" src="@/static/images/car.jpg" mode="widthFix"></image>
						<text class="title">{{item.title}}</text>
					</view>
					<view class="baseInfo">
						{{item.status}}
					</view>
				</view>

				<!-- 展开详情 -->
				<!-- 车辆信息 -->
				<view v-if="infoIsSHow" class="show-info-list">
					<view class="displayF">
						<text class="fontW">车辆信息</text>
						<text>信息已完善</text>
					</view>
					<view class="">
						车架号：
					</view>
					<view class="">
						收车时间：：
					</view>
					<view class="">
						里程数：
					</view>
					<view class="">
						<view class="displayF">
							<text>机动车登记证书：</text>
							<text class="mr30" @click="isImgShow=!isImgShow">{{isImgShow===false?'查看':'隐藏'}}</text>
						</view>

						<view class="img-list" v-show="isImgShow">
							<u-album singleSize="70" rowCount="4" :urls="urls1" keyName="src2">
							</u-album>
						</view>
					</view>
					<view class="">
						<view class="displayF">
							<text>行驶证：</text>
							<text class="mr30"
								@click="isDriverShow=!isDriverShow">{{isDriverShow===false?'查看':'隐藏'}}</text>
						</view>
						<view class="img-list" v-show="isDriverShow">
							<u-album singleSize="70" rowCount="4" :urls="urls1" keyName="src2">
							</u-album>
						</view>
					</view>
					<view class="" style="display: flex;">
						<text>检测信息：</text>
						<u-upload v-if="fileList4.length>0" width="70" height="70" :fileList="fileList4" name="4" @afterRead="afterRead" @delete="deletePic" multiple
							:maxCount="2"></u-upload>
							
						<u--image v-else :showLoading="true" src="https://cdn.uviewui.com/uview/album/1.jpg" width="70px" height="70px"></u--image>
					</view>
				</view>
				<!-- 资金信息 -->
				<view v-if="infoIsSHow" class="show-info-list">
					<view class="displayF">
						<text class="fontW">资金信息</text>
						<text>已支出：200,000元</text>
					</view>
					<view class="">
						收车价：
					</view>
					<view class="">
						检测服务费：
					</view>
					<view class="">
						运营服务费：
					</view>
					<view class="">
						过户服务费：

					</view>
					<view class="">
						增值税费用：
					</view>
					<view class="">
						杂税费用：
					</view>
					<view class="">
						卖车价：
					</view>
					<view class="">
						利润：
					</view>
				</view>
				<!-- 合同信息 -->
				<view v-if="infoIsSHow" class="show-info-list">
					<view class="displayF">
						<text class="fontW">合同信息</text>
						<text>已签署XX份</text>
					</view>
					<view class="">
						收车委托合同：
					</view>
					<view class="">
						收车合同：
					</view>
					<view class="">
						卖车委托合同：
					</view>
					<view class="">
						卖车合同：
					</view>
					<view class="color40Ad textAlignR" >
						批量下载
					</view>
					<view class="">
						卖方信息
					</view>
					<view class="">
						买方信息
					</view>
				</view>
				<!-- 发票信息 -->
				<view v-if="infoIsSHow" class="show-info-list">
					<view class="displayF">
						<text class="fontW">发票信息</text>
						<text>已开具XX份</text>
					</view>
					<view class="">
						反向二手车销售统一发票
					</view>
					<view class="">
						正向二手车销售统一发票
					</view>
					<view class="displayF">
						<text>增值税发票</text>
						<text class="color40Ad">下载</text>
					</view>

				</view>
			</view>
		</uni-card>





	</view>
</template>

<script>
	export default {
		data() {
			return {
				listData: [{
						title: '车辆信息',
						status: '信息已完善'
					},
					{
						title: '资金信息',
						status: '已支出：200,000元'
					},
					{
						title: '合同信息',
						status: '已签署XX份'
					},
					{
						title: '发票信息',
						status: '已开具XX份'
					},
				],
				infoIsSHow: false,
				urls1: ['https://cdn.uviewui.com/uview/album/1.jpg',
					'https://cdn.uviewui.com/uview/album/2.jpg',
					'https://cdn.uviewui.com/uview/album/3.jpg',
					'https://cdn.uviewui.com/uview/album/3.jpg',
				],

				isImgShow: false,
				isDriverShow: false,

				fileList4: [{
						url: 'https://cdn.uviewui.com/uview/swiper/1.jpg',
					},
					{
						url: 'https://cdn.uviewui.com/uview/swiper/1.jpg',
					}
				],

			};
		},
		methods: {
			showAndHide() {
				this.infoIsSHow = !this.infoIsSHow
			},

			// 删除图片
			deletePic(event) {
				this[`fileList${event.name}`].splice(event.index, 1)
			},
			// 新增图片
			async afterRead(event) {
				// 当设置 multiple 为 true 时, file 为数组格式，否则为对象格式
				let lists = [].concat(event.file)
				let fileListLen = this[`fileList${event.name}`].length
				lists.map((item) => {
					this[`fileList${event.name}`].push({
						...item,
						status: 'uploading',
						message: '上传中'
					})
				})
				for (let i = 0; i < lists.length; i++) {
					const result = await this.uploadFilePromise(lists[i].url)
					let item = this[`fileList${event.name}`][fileListLen]
					this[`fileList${event.name}`].splice(fileListLen, 1, Object.assign(item, {
						status: 'success',
						message: '',
						url: result
					}))
					fileListLen++
				}
			},
		}
	}
</script>

<style lang="scss" scoped>
	.car-info {
		height: 100vh;
		overflow: hidden;
	}

	.info-box {
		height: calc(100vh - 350px);
		overflow-y: scroll;
	}

	.banner {
		width: 100vw;
		height: 200px;
		overflow-y: hidden;

		.image {
			width: 100vw;
			height: 100vh;
		}

	}

	.car-title {
		box-sizing: border-box;
		margin: 20px auto;
		padding: 0 10px;
	}

	.car-status {
		padding: 0 10px;
		display: flex;
		justify-content: space-between;
		color: #40ADDD;
	}

	.info-list {
		padding: 0 10px;
		display: flex;
		justify-content: flex-start;
		align-items: center;
		margin: 20px auto;

		.info-img {
			margin-right: 40px;

			.left-image {
				width: 80px;
				height: 80px;
				vertical-align: middle;
				margin-right: 10px;
			}

			.title {
				font-weight: bold;
				font-size: 14px;
			}

		}

		.baseInfo {
			padding-bottom: 2px;
			font-size: 14px;
		}
	}

	.show-info-list {
		padding: 10px;
		margin: 10px;
		border: 1px solid #ddd;
		line-height: 25px;

		.img-list {
			margin: 10px auto;
		}
	}

	.displayF {
		display: flex;
		justify-content: space-between;
	}

	.fontW {
		font-weight: bold;
	}

	.mr30 {
		margin-right: 30px;
	}
	.color40Ad{
		color: #40ADDD;
	}
	.textAlignR{
		text-align: right;
	}
	::v-deep .uni-card--border {
		border-bottom: none;
	}
</style>
