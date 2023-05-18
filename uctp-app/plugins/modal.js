export default {
  // 消息提示
  msg(content) {
    uni.showToast({
      title: content,
      icon: 'none',
	  duration:2000
    })
  },
  // 错误消息
  msgError(content) {
    uni.showToast({
      title: content,
      icon: 'error',
	  duration: 2000
    })
  },
  // 成功消息
  msgSuccess(content) {
    uni.showToast({
      title: content,
      icon: 'success',
	  duration: 2000
    })
  },
  // 隐藏消息
  hideMsg(content) {
    uni.hideToast()
  },
  // 弹出提示
  alert(content) {
    uni.showModal({
      title: '提示',
      content: content,
      showCancel: false
    })
  },
  // 确认窗体
  confirm(content) {
    return new Promise((resolve, reject) => {
      uni.showModal({
        title: '系统提示',
        content: content,
        cancelText: '取消',
        confirmText: '确定',
        success: function (res) {
          if (res.confirm) {
            resolve(res.confirm)
          }
        }
      })
    })
  },
  // 提示信息
  showToast(option) {
    if (typeof option === "object") {
      uni.showToast(option)
    } else {
      uni.showToast({
        title: option,
        icon: "none",
        duration: 2500
      })
    }
  },
  // 打开遮罩层
  loading(content) {
    uni.showLoading({
      title: content,
      icon: 'none',
      mask: true
    })
  },
  // 关闭遮罩层
  closeLoading() {
    setTimeout(function () {
      uni.hideLoading();
    }, 2000);
  },
  closeLoading2() {
    uni.hideLoading();
  }
}
