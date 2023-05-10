<template>
  <ContentWrap>
    <Drawer
      :visible="drawerVisible"
      :status="status"
      @handle-close-drawer="handleCloseDrawer"
      @handle-update-list="handleUpdateList"
    />
    <!-- 列表 -->
    <XTable @register="registerTable" @current-change="currentChange">
      <!-- 操作：审批 -->
      <template #toolbar_buttons>
        <XButton
          type="primary"
          preIcon="ep:cherry"
          title="审批"
          v-hasPermi="['workbench:toDoList:examineApprove']"
          @click="handleApproval()"
        />
        <!-- <XButton
          type="primary"
          preIcon="ep:zoom-in"
          title="流程跟踪"
          v-hasPermi="['system:post:create']"
          @click="handleProcessTrace()"
        /> -->
        <!-- 操作：导出 -->
        <XButton
          type="primary"
          preIcon="ep:download"
          :title="t('action.export')"
          v-hasPermi="['workbench:toDoList:export']"
          @click="handleExport()"
        />
      </template>
      <template #application_default="{ row }">
        <div class="application" @click="handleApplication(row)">{{ row.serialNo }}</div>
      </template>
    </XTable>
  </ContentWrap>
</template>
<script setup lang="ts" name="ToDoList">
import { ElLoading } from 'element-plus'
import { ref } from 'vue'
import { allSchemas } from './toDoList.data'
import { Drawer } from '@/components/Drawer'
import * as ToDoList from '@/api/workbench/toDoList'
// import * as RoleApi from '@/api/system/role'
import { baseInfoData, completedVisible } from '../basInfoValue'
// 列表相关的变量
const message = useMessage()
const [registerTable, { reload }] = useXTable({
  allSchemas: allSchemas,
  getListApi: ToDoList.getToDoList
  // getListApi: RoleApi.getRolePageApi
})
const { t } = useI18n() // 国际化

// const dialogVisible = ref(false)
const status = ref('')

// 抽屉
const drawerVisible = ref(false)
const handleCloseDrawer = () => {
  drawerVisible.value = false
}
// 审批
const handleApproval = () => {
  if (!Object.keys(rowData.value).length) {
    return message.error('请先选中数据！')
  }
  handleApplication(rowData.value)
}

// 流程跟踪
// const handleProcessTrace = () => {
//   console.log('流程跟踪')
// }

//导出
const handleExport = () => {
  console.log('导出')
}
const rowData = ref<any>({})
const currentChange = (row) => {
  rowData.value = row.row
}
// 点击申请单号
const handleApplication = (row) => {
  const loadingInstance = ElLoading.service({ fullscreen: true })
  // drawerVisible.value = true //打开抽屉
  // console.log(infoLoading, 'infoLoading')
  const params = {
    taskId: row.taskId,
    businessKey: row.businessKey
  }

  ToDoList.getTaskFormInfoAPI(params)
    .then((response) => {
      completedVisible.value = false
      baseInfoData.data = { ...response }
      loadingInstance.close()
      // ZHSQ SGYZ SCKP SCGH SKZH MCKP MGYZ MCKP LRTX MCGH
      status.value = response.busiType
      drawerVisible.value = true //打开抽屉
    })
    .catch((err) => {
      console.log(err)
      loadingInstance.close()
    })
}
// 刷新列表数据
const handleUpdateList = () => {
  reload()
}
onActivated(() => {
  handleUpdateList()
})
// 关闭弹框
// const closeDialog = () => {
//   dialogVisible.value = false
//   status.value = ''
// }
</script>

<style scoped lang="scss">
.application {
  color: #51b5e0;
  cursor: pointer;
}
</style>
