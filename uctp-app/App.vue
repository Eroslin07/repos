<script>
  import config from './config'
  import store from '@/store'
  import { getAccessToken } from '@/utils/auth'

  export default {
    onLaunch: function() {
      this.initApp()
    },
    methods: {
      // 初始化应用
      initApp() {
        // 初始化应用配置
        this.initConfig()
        // 检查用户登录状态
        this.checkLogin()
      },
      initConfig() {
        this.globalData.config = config
      },
      checkLogin() {
        if (!getAccessToken()) {
          // #ifndef MP-WEIXIN
          this.$tab.reLaunch('/pages/login')
          // #endif
          // #ifdef MP-WEIXIN
          this.$tab.reLaunch('/pages/wx_login')
          // #endif
        }
      }
    }
  }
</script>

<style lang="scss">
  @import '@/static/scss/index.scss'
</style>
