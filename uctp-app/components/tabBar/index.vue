<template>
	<u-tabbar :value="name" activeColor="#fa6400" @change="handleChange">
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
		<u-tabbar-item text="费用分析" v-if="$store.state.user.staffType == 1">
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
		<u-tabbar-item text="资金管理" v-if="$store.state.user.staffType == 1">
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
</template>

<script>
	export default {
		props: ['name'],
		data() {
			return {
				
			}
		},
		methods: {
			handleChange(name) {
				if (this.$store.state.user.staffType == 1) {
					if (name == 0) {
						this.$tab.switchTab('/pages/index');
					} else if (name == 1) {
						this.$tab.switchTab('/pages/cost/index');
					} else if (name == 2) {
						this.$tab.switchTab('/pages/account/index');
					} else if (name == 3) {
						this.$tab.switchTab('/pages/mine/index');
					}
				} else {
					if (name == 0) {
						this.$tab.switchTab('/pages/index');
					} else if (name == 1) {
						this.$tab.switchTab('/pages/mine/index');
					}
				}
			}
		}
	}
</script>

<style>
</style>