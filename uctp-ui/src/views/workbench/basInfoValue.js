import { reactive, ref } from 'vue'

// 详情信息
export let baseInfoData = reactive({ data: {} });

// 加载图标
export let infoLoading = ref(false)