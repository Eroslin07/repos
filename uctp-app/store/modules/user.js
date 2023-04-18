import config from '@/config'
import storage from '@/utils/storage'
import constant from '@/utils/constant'
import { login, wxLogin, logout, getInfo } from '@/api/login'
import { setToken, removeToken } from '@/utils/auth'

const baseUrl = config.baseUrl

const user = {
  state: {
    id: storage.get(constant.id), // 用户编号
    name: storage.get(constant.name),
    avatar: storage.get(constant.avatar),
    roles: storage.get(constant.roles),
    permissions: storage.get(constant.permissions),
    phone: uni.getStorageSync('PHONE'),
    deptId: uni.getStorageSync('DEPT_ID') || storage.get(constant.deptId),
    tenantId: uni.getStorageSync('TENANT_ID') || storage.get(constant.tenantId),
    deptName: uni.getStorageSync('SET_DEPTNAME') || storage.get(constant.deptName),
    tenantName: uni.getStorageSync('SET_TENANTNAME') || storage.get(constant.tenantName),
    accountNo: '22222222'
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
          resolve(res)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 退出系统
    LogOut({ commit, state }) {
      return new Promise((resolve, reject) => {
        logout(state.token).then(() => {
          commit('SET_ROLES', [])
          commit('SET_PERMISSIONS', [])
          removeToken()
          storage.clean()
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 保存手机号
    GetPhone({ commit, state }) {
      commit('SET_PHONE', state)
    }
  }
}

export default user
