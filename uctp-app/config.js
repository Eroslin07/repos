// 应用全局配置
module.exports = {
  // baseUrl: 'http://api-dashboard.uctp.cn',
  // baseUrl: 'https://miniapp.wanguo.net:28001/',
  // baseUrl: 'http://172.17.10.154:48080',
  baseUrl: 'http://localhost:48080',
  
  // 上传路径
  // uploadUrl: 'https://miniapp.wanguo.net:28001/app-api/infra/file/upload',
  uploadUrl: 'http://localhost:48080/app-api/infra/file/upload',
  
  // baseApi: '/admin-api',
  baseApi: '/app-api',
  // 应用信息
  appInfo: {
    // 应用名称
    name: "uctp-app",
    // 应用版本
    version: "1.0.0",
    // 应用logo
    logo: "/static/logo.png",
    // 官方网站
    site_url: "https://iocoder.cn"
  }
}
