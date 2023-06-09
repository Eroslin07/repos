import config from '@/config'
import storage from '@/utils/storage'
import constant from '@/utils/constant'
import { login, wxLogin, logout, getInfo } from '@/api/login'
import { setToken, removeToken } from '@/utils/auth'
import tarBarUserType from '@/utils/tabBar.js';

const baseUrl = config.baseUrl

const user = {
  state: {
    id: uni.getStorageSync('SET_ID') || storage.get(constant.id), // 用户编号
    name: storage.get(constant.name),
    avatar: storage.get(constant.avatar),
    roles: storage.get(constant.roles),
    permissions: storage.get(constant.permissions),
    phone: uni.getStorageSync('PHONE') || storage.get(constant.phone),
    deptId: uni.getStorageSync('DEPT_ID') || storage.get(constant.deptId),
    tenantId: uni.getStorageSync('TENANT_ID') || storage.get(constant.tenantId),
    deptName: uni.getStorageSync('SET_DEPTNAME') || storage.get(constant.deptName),
    tenantName: uni.getStorageSync('SET_TENANTNAME') || storage.get(constant.tenantName),
    accountNo: uni.getStorageSync('ACCOUNT_NO') || storage.get(constant.accountNo),
    staffType: uni.getStorageSync('SET_STAFFTYPE') || storage.get(constant.staffType),
    loginStatus: true,
    registerType: uni.getStorageSync('SET_REGISTERTYPE') || storage.get(constant.registerType),
    paymentType: uni.getStorageSync('PAYMENT_TYPE') || storage.get(constant.paymentType)
  },

  mutations: {
    SET_ID: (state, id) => {
      state.id = id
      storage.set(constant.id, id)
      uni.setStorageSync('SET_ID', id)
    },
    SET_NAME: (state, name) => {
      state.name = name
      storage.set(constant.name, name)
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
      storage.set(constant.avatar, avatar)
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
      storage.set(constant.roles, roles)
    },
    SET_PERMISSIONS: (state, permissions) => {
      state.permissions = permissions
      storage.set(constant.permissions, permissions)
    },
    SET_PHONE: (state, phone) => {
      state.phone = phone
      storage.set(constant.phone, phone)
      uni.setStorageSync('PHONE', phone)
    },
    SET_DEPTID: (state, deptId) => {
      state.deptId = deptId
      storage.set(constant.deptId, deptId)
      uni.setStorageSync('DEPT_ID', deptId)
    },
    SET_DEPTNAME: (state, deptName) => {
      state.deptName = deptName
      storage.set(constant.deptName, deptName)
      uni.setStorageSync('SET_DEPTNAME', deptName)
    },
    SET_TENANTID: (state, tenantId) => {
      state.tenantId = tenantId
      storage.set(constant.tenantId, tenantId)
      uni.setStorageSync('TENANT_ID', tenantId)
    },
    SET_TENANTNAME: (state, tenantName) => {
      state.tenantName = tenantName
      storage.set(constant.tenantName, tenantName)
      uni.setStorageSync('SET_TENANTNAME', tenantName)
    },
    SET_STAFFTYPE: (state, staffType) => {
      state.staffType = staffType
      storage.set(constant.staffType, staffType)
      uni.setStorageSync('SET_STAFFTYPE', staffType)
    },
    SET_REGISTERTYPE: (state, registerType) => {
      state.registerType = registerType
      storage.set(constant.registerType, registerType)
      uni.setStorageSync('SET_REGISTERTYPE', registerType)
    },
    LOGIN_STATUS: (state, loginStatus) => {
      state.loginStatus = loginStatus
    },
    ACCOUNT_NO: (state, accountNo) => {
      state.accountNo = accountNo
      storage.set(constant.accountNo, accountNo)
      uni.setStorageSync('ACCOUNT_NO', accountNo)
    },
    PAYMENT_TYPE: (state, paymentType) => {
      state.paymentType = paymentType
      storage.set(constant.paymentType, paymentType)
      uni.setStorageSync('PAYMENT_TYPE', paymentType)
    }
  },

  actions: {
    // 登录
    Login({ commit }, userInfo) {
      const username = userInfo.username.trim()
      const password = userInfo.password
      const captchaVerification = userInfo.captchaVerification
      return new Promise((resolve, reject) => {
        login(username, password, captchaVerification).then(res => {
          res = res.data;
          // 设置 token
          setToken(res)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },
    
    // 微信小程序一键登录
    phoneLogin({ commit }, userInfo) {
      return new Promise((resolve, reject) => {
        wxLogin(userInfo).then(res => {
          res = res.data;
          // 设置 token
          setToken(res)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 获取用户信息
    GetInfo({ commit, state }) {
      return new Promise((resolve, reject) => {
        getInfo().then(res => {
          res = res.data; // 读取 data 数据
          const user = res.user
          const avatar = (user == null || user.avatar === "" || user.avatar == null) ? require("@/static/images/profile.jpg") : user.avatar
          const nickname = (user == null || user.nickname === "" || user.nickname == null) ? "" : user.nickname
          const deptId = user.deptId
          const tenantId = user.tenantId
          const accountNo = user.accountNo || '55555555'
          if (res.roles && res.roles.length > 0) {
            commit('SET_ROLES', res.roles)
            commit('SET_PERMISSIONS', res.permissions)
          } else {
            commit('SET_ROLES', ['ROLE_DEFAULT'])
          }
          commit('SET_NAME', nickname)
          commit('SET_AVATAR', avatar)
          commit('SET_DEPTID', deptId)
          commit('SET_DEPTNAME', user.deptName)
          commit('SET_TENANTID', tenantId)
          commit('SET_TENANTNAME', user.tenantName)
          commit('SET_ID', user.id)
          commit('SET_STAFFTYPE', user.staffType)
          commit('SET_REGISTERTYPE', user.registerType)
          commit('ACCOUNT_NO', accountNo)
          commit('PAYMENT_TYPE', user.paymentType)
          resolve(res)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 退出系统(旧)
    // LogOut({ commit, state }) {
    //   return new Promise((resolve, reject) => {
    //     logout(state.token).then(() => {
    //       commit('SET_ROLES', [])
    //       commit('SET_PERMISSIONS', [])
    //       removeToken()
    //       storage.clean()
    //       resolve()
    //     }).catch(error => {
    //       reject(error)
    //     })
    //   })
    // }

    // 退出系统(新)
    LogOut({ commit, state }) {
      return new Promise((resolve, reject) => {
        wx.login({
          success(res) {
            logout({token: state.token, wxCode: res.code}).then(() => {
              commit('SET_ROLES', [])
              commit('SET_PERMISSIONS', [])
              commit('LOGIN_STATUS', false)
              removeToken()
              storage.clean()
              resolve()
            }).catch(error => {
              reject(error)
            })
          }
        })
      })
    }
  }
}

export default user
