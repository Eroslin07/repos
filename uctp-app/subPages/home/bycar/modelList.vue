<template>
	<view class="add-sku">
		<view class="return" @click="handleReurn">返回</view>
		<u-index-list :index-list="[]">
			<template v-for="(item, index) in indexList">
				<!-- #ifdef APP-NVUE -->
				<u-index-anchor :text="item"></u-index-anchor>
				<!-- #endif -->
				<u-index-item>
					<!-- #ifndef APP-NVUE -->
					<u-index-anchor :text="item"></u-index-anchor>
					<!-- #endif -->
					<view class="list-cell" v-for="(cell, index1) in itemArr" :key="index1" @click="handleClick(cell)">
						<text v-if="status">{{cell.series_name}}</text>
						<text v-else>{{cell.model_name}}</text>
					</view>
				</u-index-item>
			</template>
		</u-index-list>
	</view>
</template>

<script>
	import { getCarModelList } from '@/api/home/bycar.js'
	export default {
		props: ['seriesList', 'title'],
		data() {
			return {
				status: true,
				indexList: [],
				itemArr: [],
				modelList: [],
				value: ''
			}
		},
		watch: {
			status() {
				if (this.status == true) {
					this.itemArr = this.seriesList;
					this.indexList = [];
					this.indexList.push(this.title);
				} else {
					this.indexList = [];
					this.indexList.push(this.value);
					this.itemArr = this.modelList;
				}
			}
		},
		mounted() {
			this.indexList = [];
			this.indexList.push(this.title);
			this.itemArr = this.seriesList;
		},
		methods: {
			handleReurn() {
				if (this.status == false) {
					this.status = true;
				} else {
					this.$emit('handleClose');
				}
			},
			handleClick(val) {
				if (this.status) {
					this.value = val.series_name;
					let data = {
						seriesId: val.series_id
					}
					getCarModelList(data).then((res) => {
						this.status = false;
						this.modelList = res.model_list;
					})
				} else {
					this.$emit('handleClose', val);
				}
			}
		}
	}
</script>

<style lang="scss" scoped>
	.add-sku {
		height: 100vh;
	}
	
	.return {
		padding: 10px;
	}
	
	.list-cell {
		display: flex;
		box-sizing: border-box;
		width: 100%;
		padding: 10px 24rpx;
		overflow: hidden;
		color: #323233;
		font-size: 14px;
		line-height: 24px;
		background-color: #fff;
	}
	
	/deep/ .u-index-list__letter {
		display: none;
	}
</style>