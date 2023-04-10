// 应用全局配置
module.exports = {
  // baseUrl: 'http://api-dashboard.uctp.cn',
  baseUrl: 'http://localhost:48080',
	// 上传路径
	uploadUrl: 'http://172.17.10.127:48080/app-api/infra/file/upload',
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
    site_url: "https://iocoder.cn",
    // 政策协议
    agreements: [{
        title: "隐私政策",
        url: "/static/隐私政策.docx"
      },
      {
        title: "用户服务协议",
        url: "/static/用户协议.docx"
      }
    ]
  }
}
