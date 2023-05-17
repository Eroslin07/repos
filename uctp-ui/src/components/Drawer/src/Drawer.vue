<template>
  <div class="drawer_self" id="drawerSelf">
    <el-drawer
      v-model="drawerVisible"
      :direction="direction"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :size="sizeWidth"
      append-to-body
      destroy-on-close
      :show-close="false"
      class="drawerSelf"
      :with-header="false"
    >
      <template #header> </template>
      <template #default>
        <div class="header">
          <div
            style="
              font-size: 24px;
              font-weight: bold;
              text-align: center;
              width: 100%;
              color: #000000;
            "
            >{{ baseInfoData.data?.title }}</div
          >
          <div class="btns" id="btns">
            <el-button plain type="primary" @click="submitBtn('同意')" v-if="!completedVisible">{{
              successText(props.status)
            }}</el-button>
            <el-button
              plain
              type="success"
              @click="submitBtn('不同意')"
              v-if="!completedVisible && noVisible"
              >退回并终止</el-button
            >
            <el-button
              plain
              type="success"
              @click="submitBtn('不同意')"
              v-if="props.status == 'SKZH'"
              >提交作废</el-button
            >
            <!-- <el-button plain type="danger">作废</el-button> -->
            <el-button plain type="info" @click="dravwerClose">关闭</el-button>
          </div>
          <!-- <div style="float: left; font-size: 16px">单号：{{ baseInfoData.data?.serialNo }}</div> -->
        </div>

        <div class="drawer_content">
          <el-tabs v-model="activeName" @tab-change="tabChange" class="tabs">
            <el-tab-pane
              v-for="(item, index) in comps"
              :key="index"
              :label="item.label"
              :name="item.name"
            >
              <el-scrollbar class="scrollbar_init" :min-size="5">
                <keep-alive>
                  <component :is="item.compName" :status="props.status"></component>
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
            maxlength="50"
            show-word-limit
            resize="none"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取消</el-button>
          <el-button type="primary" @click="dialogSubmit" :loading="subLoading"> 确定 </el-button>
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
import { baseInfoData, completedVisible, tabName } from '@/views/workbench/basInfoValue'
import { status } from 'nprogress'
import { useAppStore } from '@/store/modules/app'
const appStore = useAppStore()
const collapse = computed(() => appStore.getCollapse)
console.log(status)

console.log(completedVisible, 'drawer')

const message = useMessage()
const emit = defineEmits(['handleCloseDrawer', 'handleUpdateList'])
// const emit = defineEmits(['handleUpdataList'])
const activeName = ref('BaseInfo')
const statusList = ['SCKP', 'MCGH', 'SCGH', 'SKZH', 'MCKP']
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
  status: {
    type: String,
    default: ''
  }
})
const drawerVisible = computed(() => {
  return props.visible
})
const noVisible = computed(() => {
  return !statusList.includes(props.status)
})
function tabChange(name) {
  tabName.value = name
}
// 提交
const submitBtn = (text) => {
  if (
    props.status == 'SCKP' &&
    !baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInvoiceDetailVO
      .transManageName
  ) {
    return message.error('请输入转入地车辆管理所名称！')
  }
  if (
    props.status == 'MCKP' &&
    !baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInvoiceDetailVO
      .transManageName
  ) {
    return message.error('请输入转入地车辆管理所名称！')
  }

  dialogText.value = text
  form.value.reason = '审批' + text
  if (props.status == 'SKZH') {
    if (text == '同意') {
      form.value.reason = '重新支付'
    } else if (text == '不同意') {
      form.value.reason = '合同作废'
    }
  }
  dialogFormVisible.value = true
}

// 审批意见弹框
const subLoading = ref(false)
const dialogFormVisible = ref(false)
const dialogText = ref('')
const form = ref<any>({})
const dialogSubmit = () => {
  if (!form.value.reason) return message.error('请输入审批意见')
  subLoading.value = true
  if ((props.status == 'SKZH' || props.status == 'LRTX') && dialogText.value == '不同意') {
    let params = {
      taskId: baseInfoData.data.taskId,
      reason: form.value.reason
    }
    detailAPI
      .putInvalid(params)
      .then(() => {
        dravwerClose()
        subLoading.value = false
        message.success('作废成功')
        dialogFormVisible.value = false
        emit('handleCloseDrawer')
        emit('handleUpdateList')
      })
      .catch((err) => {
        console.log(err)
        subLoading.value = false
        message.error('提交失败')
      })
  } else {
    let data = {
      id: baseInfoData.data.taskId,
      reason: form.value.reason,
      variables: baseInfoData.data.variables
    }
    data.variables.approvalType = dialogText.value == '同意' ? 'pass' : 'disagree'
    data.variables.nodeId = baseInfoData.data.nodeId
    data.variables = detailAPI
      .putApproveAPI(data)
      .then(() => {
        dravwerClose()
        subLoading.value = false
        message.success('提交成功')
        dialogFormVisible.value = false
        emit('handleCloseDrawer')
        emit('handleUpdateList')
      })
      .catch((err) => {
        console.log(err)
        subLoading.value = false
        message.error('提交失败')
      })
  }
}
const successText = (i) => {
  if (i == 'ZHSQ' || i == 'SGYZ' || i == 'MGYZ') {
    return '同意'
  } else if (i == 'SCKP' || i == 'MCKP') {
    return '开票'
  } else if (i == 'SCGH' || i == 'MCGH') {
    return '确认已过户'
  } else if (i == 'SKZH') {
    return '重新支付'
  } else if (i == 'LRTX') {
    return '在线转账'
  }
}
// 关闭抽屉
const dravwerClose = () => {
  activeName.value = 'BaseInfo'
  emit('handleCloseDrawer')
}
// 监听窗口大小
const screenWidths = ref<number>(
  window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth
)
onMounted(() => {
  window.onresize = () => {
    return (() => {
      screenWidths.value =
        window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth
    })()
  }
})
watch(
  () => screenWidths,
  (val) => {
    screenWidths.value = val
  }
)

const sizeWidth = computed(() => {
  return unref(collapse) ? unref(screenWidths) - 64 : unref(screenWidths) - 200
})
</script>
<style lang="scss" scoped>
.drawer_self {
  :deep(.el-drawer__header) {
    margin-bottom: 0;
  }
  :deep(.drawer_content) {
    height: calc(100% - 200px);

    .el-tabs {
      height: 100%;
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
}
.header {
  display: flex;
}
.btns {
  display: flex;
  justify-content: flex-end;
}

#btns .el-button {
  padding: 5px 10px !important;
}

#drawerSelf .el-drawer .el-drawer__header {
  margin-bottom: 0 !important;
}
.drawer_content {
  height: calc(100% - 40px);
  .tabs {
    height: 100%;
  }
  :deep(.el-tabs__content) {
    height: calc(100% - 40px);
    .el-tab-pane {
      height: 100%;
    }
    .el-scrollbar__view {
      height: calc(100% - 40px) !important;
    }
  }
}
</style>
