import Vue from 'vue'
import Vuex from 'vuex'
import user from '@/store/modules/user'
import allStatus from '@/store/modules/allStatus'
import getters from './getters'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    user,
	allStatus
  },
  getters
})

export default store
