<template>
	<view class="work-container">
		<!-- 加载异常组件 -->
		<AbnormalPage v-if="isSHowTip" :isSHowTip="isSHowTip" />
		<u-swipe-action v-else>
			<u-swipe-action-item :options="item.options" v-for="(item,index) in listData" :key="item.id"
				@click="handleSwipe(item,index)">
				<uni-list-chat :avatar-circle="true" :badgeText="item.status=='1'?'':'dot'" badgePositon="left"
					:title="item.title" :avatar="msgAvatar(item)" :note="item.content" :time="item.createTime" clickable
					@click="handleListItem(item,index)">
				</uni-list-chat>
			</u-swipe-action-item>
		</u-swipe-action>

		<u-modal :show="modalShow" :title="title" :content='content' showCancelButton @cancel="modalShow=false"
			@confirm="handleConfirm"></u-modal>
	</view>
</template>

<script>
	import {
		getNoticesApi,
		deleteNoticeApi,
		updateAllNoticeApi,
		updateNoticeApi
	} from '@/api/work/message.js'
	import {
		parseTime
	} from '@/utils/ruoyi.js'
	import AbnormalPage from '@/subPages/common/abnormaPage/index.vue'
	export default {
		data() {
			return {
				businessId: this.$store.state.user.deptId,
				// 列表
				listData: [],
				imageArr: [{
						url: '/static/images/message/sendBack.png',
						label: '公允价值退回'
					},
					{
						url: '/static/images/message/collectSucess.png',
						label: '公允价值通过'
					},
					{
						url: '/static/images/message/collectSucess.png',
						label: '收车交易成功'
					},
					{
						url: '/static/images/message/collectFail.png',
						label: '收车交易异常'
					},
					{
						url: '/static/images/message/cashDeposit.png',
						label: '保证金充值'
					},
					{
						url: '/static/images/message/cashDeposit.png',
						label: '保证金扣减'
					},
					{
						url: '/static/images/message/profit.png',
						label: '卖车利润'
					},
					{
						url: '/static/images/message/profit.png',
						label: '利润提现中'
					},
					{
						url: '/static/images/message/profit.png',
						label: '利润提现'
					},
					{
						url: '/static/images/message/profit.png',
						label: '利润扣减补保证金'
					},

				],

				imgObj: {
					'公允价值退回': '/static/images/message/profit.png',
					'保证金扣减': '/static/images/message/cashDeposit.png'
				},

				// 删除弹框
				modalShow: false,
				title: '',
				content: '此操作将删除该条数据，是否确认？',
				// 删除的值
				deleteItem: {},

				isSHowTip: '',
			}
		},
		components: {
			AbnormalPage,
		},
		onLoad() {
			this.getListData()
		},
		onPullDownRefresh() {
			this.getListData()
		},
		methods: {
			// 获取消息列表
			getListData() {
				this.isSHowTip = "onLoading"
				let options = [{
					text: '删除',
					style: {
						backgroundColor: '#f56c6c'
					}
				}]
				getNoticesApi(this.businessId).then(res => {
					if (res.data.length) {
						this.isSHowTip = '';
						this.listData = res.data.map(item => {
							this.$set(item, 'options', options);
							this.$set(item, 'swipeShow', false);
							item.createTime = parseTime(item.createTime);
							return item
						})
					} else {
						this.isSHowTip = 'noData'
					}
				}).catch(err => {
					console.log(err, 'err')
					if (err == '后端接口连接异常' || err == '系统接口请求超时') {
						this.isSHowTip = 'webError'
					} else {
						this.isSHowTip = 'sysError'
					}

				}).finally(() => {
					uni.stopPullDownRefresh()
				})
			},

			// 图标
			msgAvatar(item) {
				let obj = this.imageArr.find(v => v.label == item.title)
				if (obj && obj.url) {
					return obj.url
				} else {
					return ''
				}
			},

			// 左滑删除
			handleSwipe(item, index) {
				this.deleteItem = {}
				this.modalShow = true;
				this.deleteItem = item
			},
			handleConfirm() {
				this.modalShow = false;
				let data = [this.deleteItem.id]
				deleteNoticeApi(data).then(res => {
					uni.showToast({
						title: '删除成功',
						icon: 'none'
					})
					this.getListData()
				})
			},
			// 点击消息列表
			handleListItem(item, index) {
				let data = {
					id: item.id
				}
				updateNoticeApi(data).then(res => {
					this.$tab.navigateTo('/subPages/work/infoDetail?item=' + encodeURIComponent(JSON
						.stringify(
							item)))
				}).catch(err => {
					console.log(err)
				})

			},
		}
	}
</script>

<style lang="scss">
	.work-container {
		height: 100%;
	}

	/deep/ .uni-list-chat__header {
		border: none !important;
	}
</style>
