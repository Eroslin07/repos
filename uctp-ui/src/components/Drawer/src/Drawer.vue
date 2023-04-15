<template>
  <div class="drawer_self">
    <el-drawer
      v-model="drawerVisible"
      :direction="direction"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      size="80%"
      append-to-body
      destroy-on-close
      :show-close="false"
    >
      <template #header="{ titleId, titleClass }">
        <h3 :id="titleId" :class="titleClass">{{ drawerTitle }}</h3>
        <div class="btns" id="btns">
          <el-button plain type="primary" @click="submitBtn">提交</el-button>
          <el-button plain type="success">退回</el-button>
          <el-button plain type="danger">作废</el-button>
          <el-button plain type="info" @click="dravwerClose">关闭</el-button>
        </div>
      </template>
      <template #default>
        <div class="drawer_content">
          <el-tabs v-model="activeName" @tab-change="tabChange">
            <el-tab-pane
              v-for="(item, index) in comps"
              :key="index"
              :label="item.label"
              :name="item.name"
            >
              <el-scrollbar class="scrollbar_init" :min-size="5">
                <keep-alive>
                  <component :is="item.compName" :status="status"></component>
                </keep-alive>
              </el-scrollbar>
            </el-tab-pane>
          </el-tabs>
        </div>
      </template>
    </el-drawer>

    <!-- 审批意见弹框 -->
    <el-dialog v-model="dialogFormVisible" title="" draggable lock-scroll destroy-on-close>
      <el-form :model="form">
        <el-form-item label="审批意见:" label-width="100" required>
          <el-input
            v-model="form.reason"
            autocomplete="off"
            :rows="4"
            type="textarea"
            placeholder="请输入审批意见"
            maxlength="520"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取消</el-button>
          <el-button type="primary" @click="dialogSubmit"> 确定 </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import * as detailAPI from '@/api/workbench/toDoList/index'
import BaseInfo from '@/views/ApprovalProcess/BaseInfo.vue'
import BaseHistory from '@/views/ApprovalProcess/BaseHistory.vue'
import BaseFlowChart from '@/views/ApprovalProcess/BaseFlowChart.vue'
import { ref, shallowRef } from 'vue'
import { baseInfoData } from '@/views/workbench/basInfoValue'

const message = useMessage()
const emit = defineEmits(['handleCloseDrawer', 'handleUpdataList'])
// const emit = defineEmits(['handleUpdataList'])
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

// 提交
const submitBtn = () => {
  console.log('提交')
  dialogFormVisible.value = true
}

// 审批意见弹框
const dialogFormVisible = ref(false)
const form = ref<any>({})
const dialogSubmit = () => {
  if (!form.value.reason) return message.error('请输入审批意见')
  let data = {
    id: baseInfoData.data.taskId,
    reason: form.value.reason,
    variables: baseInfoData.data.variables
  }
  detailAPI
    .putApproveAPI(data)
    .then((res) => {
      console.log(res)
      message.success('提交成功')
      dialogFormVisible.value = false
      emit('handleCloseDrawer')
      emit('handleUpdataList')
    })
    .catch((err) => {
      console.log(err)
      message.error('提交失败')
    })
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
    height: calc(100% - 200px);

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
  // :deep(.el-button.is-plain) {
  //   padding: 5px 10px !important;
  // }
  .title {
    font-size: 18px;
    font-weight: bold;
  }
  .btns {
    display: flex;
    justify-content: flex-end;
  }
}

#btns .el-button {
  padding: 5px 10px !important;
}
</style>
