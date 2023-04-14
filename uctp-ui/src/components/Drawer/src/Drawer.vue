<template>
  <div class="drawer_self">
    <el-drawer
      v-model="drawerVisible"
      :direction="direction"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      size="80%"
      @close="dravwerClose"
    >
      <template #title>
        <h3 class="title">{{ drawerTitle }}</h3>
      </template>
      <template #default>
        <div class="btns">
          <el-button type="primary">提交</el-button>
          <el-button type="success">退回</el-button>
          <el-button type="danger">作废</el-button>
        </div>
        <div class="drawer_content">
          <el-tabs v-model="activeName" @tab-change="tabChange">
            <el-tab-pane
              v-for="(item, index) in comps"
              :key="index"
              :label="item.label"
              :name="item.name"
            >
              <el-scrollbar class="scrollbar_init">
                <keep-alive>
                  <component :is="item.compName" :status="status"></component>
                </keep-alive>
              </el-scrollbar>
            </el-tab-pane>
          </el-tabs>
        </div>
      </template>
    </el-drawer>
  </div>
</template>
<script setup lang="ts">
import BaseInfo from '@/views/ApprovalProcess/BaseInfo.vue'
import BaseHistory from '@/views/ApprovalProcess/BaseHistory.vue'
import BaseFlowChart from '@/views/ApprovalProcess/BaseFlowChart.vue'
import { ref, shallowRef } from 'vue'
const emit = defineEmits(['handleCloseDrawer'])
const activeName = ref('BaseInfo')
const comps = shallowRef([
  {
    label: '基本信息',
    name: 'BaseInfo',
    compName: BaseInfo
  },
  {
    label: '审批历史',
    name: 'BaseHistory',
    compName: BaseHistory
  },
  {
    label: '流程图',
    name: 'BaseFlowChart',
    compName: BaseFlowChart
  }
])
const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  direction: {
    type: String,
    default: 'rtl'
  },
  drawerTitle: {
    type: String,
    default: '收车合同审批-节点3'
  },
  status: {
    type: String,
    default: ''
  }
})
const drawerVisible = computed(() => {
  return props.visible
})
function tabChange(name) {
  console.log(name)
}
// 关闭抽屉
const dravwerClose = () => {
  emit('handleCloseDrawer')
}
</script>
<style lang="scss" scoped>
.drawer_self {
  :deep(.el-drawer__header) {
    margin-bottom: 0;
    > :first-child {
      margin: 0;
    }
  }

  :deep(.drawer_content) {
    height: 100%;

    .el-tabs {
      height: 100%;
    }

    .scrollbar_init {
      height: calc(100vh - 52px - 55px - 40px) !important;
    }

    .el-card:not(:last-child) {
      margin-bottom: 15px;
    }
  }
  .title {
    font-size: 18px;
    font-weight: bold;
  }
  .btns {
    display: flex;
    justify-content: flex-end;
  }
}
</style>
