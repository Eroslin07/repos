<template>
	<view>
		<u-tabbar :value="name" activeColor="#fa6400" @change="handleChange" v-if="type == 1">
			<u-tabbar-item text="首页">
				<image
					class="form-image"
					slot="active-icon"
					src="/static/images/tabbar/homepage.png"
				></image>
				<image
					class="form-image"
					slot="inactive-icon"
					src="/static/images/tabbar/homepage_.png"
				></image>
			</u-tabbar-item>
			<u-tabbar-item text="费用分析">
				<image
					class="form-image"
					slot="active-icon"
					src="/static/images/tabbar/feiyong_.png"
				></image>
				<image
					class="form-image"
					slot="inactive-icon"
					src="/static/images/tabbar/feiyong.png"
				></image>
			</u-tabbar-item>
			<u-tabbar-item text="资金管理">
				<image
					class="form-image"
					slot="active-icon"
					src="/static/images/tabbar/message_.png"
				></image>
				<image
					class="form-image"
					slot="inactive-icon"
					src="/static/images/tabbar/message.png"
				></image>
			</u-tabbar-item>
			<u-tabbar-item text="我的">
				<image
					class="form-image"
					slot="active-icon"
					src="/static/images/tabbar/mine_.png"
				></image>
				<image
					class="form-image"
					slot="inactive-icon"
					src="/static/images/tabbar/mine.png"
				></image>
			</u-tabbar-item>
		</u-tabbar>
		<u-tabbar :value="name" activeColor="#fa6400" @change="handleChange" v-else>
			<u-tabbar-item text="首页">
				<image
					class="form-image"
					slot="active-icon"
					src="/static/images/tabbar/homepage.png"
				></image>
				<image
					class="form-image"
					slot="inactive-icon"
					src="/static/images/tabbar/homepage_.png"
				></image>
			</u-tabbar-item>
			<u-tabbar-item text="我的">
				<image
					class="form-image"
					slot="active-icon"
					src="/static/images/tabbar/mine_.png"
				></image>
				<image
					class="form-image"
					slot="inactive-icon"
					src="/static/images/tabbar/mine.png"
				></image>
			</u-tabbar-item>
		</u-tabbar>
	</view>
</template>

<script>
	export default {
		props: ['name', 'type'],
		data() {
			return {
				
			}
		},
		methods: {
			handleChange(index) {
				if (this.type == 1) {
					if (index == 0) {
						this.$tab.switchTab('/pages/index');
					} else if (index == 1) {
						this.$modal.msg("费用分析开发中");
						// this.$tab.switchTab('/pages/cost/index');
					} else if (index == 2) {
						this.$tab.switchTab('/pages/account/index');
					} else if (index == 3) {
						this.$tab.switchTab('/pages/mine/index');
					}
				} else {
					if (index == 0) {
						this.$tab.switchTab('/pages/index');
					} else if (index == 1) {
						this.$tab.switchTab('/pages/mine/index');
					}
				}
			}
		}
	}
</script>

<style>
</style>