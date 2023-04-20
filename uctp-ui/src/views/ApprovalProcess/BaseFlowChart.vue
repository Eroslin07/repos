<template>
  <el-card class="box-card" v-loading="processInstanceLoading">
    <!-- <template #header>
      <span class="el-icon-picture-outline">流程图</span>
    </template> -->
    <my-process-viewer
      key="designer"
      v-model="bpmnXML"
      :value="bpmnXML"
      v-bind="bpmnControlForm"
      :prefix="bpmnControlForm.prefix"
      :activityData="activityList"
      :processInstanceData="processInstance"
      :taskData="tasks"
    />
  </el-card>
</template>
<script setup lang="ts">
import * as ProcessInstanceApi from '@/api/bpm/processInstance'
import * as DefinitionApi from '@/api/bpm/definition'
import * as ActivityApi from '@/api/bpm/activity'
import * as TaskApi from '@/api/bpm/task'
import { setConfAndFields2 } from '@/utils/formCreate'
import type { ApiAttrs } from '@form-create/element-ui/types/config'
import { baseInfoData } from '@/views/workbench/basInfoValue'
import { onMounted, ref } from 'vue'
import { useUserStore } from '@/store/modules/user'
const message = useMessage() // 消息弹窗
const processInstance = ref<any>({}) // 流程实例
const userId = useUserStore().getUser.id // 当前登录的编号
const processInstanceLoading = ref(false)

const id = baseInfoData.data.procInstId

const fApi = ref<ApiAttrs>()
// 流程表单详情
const detailForm = ref({
  rule: [],
  option: {},
  value: {}
})
// ========== 高亮流程图 ==========
const bpmnXML = ref(null)
const bpmnControlForm = ref({
  prefix: 'flowable'
})
const activityList = ref([])
const tasks = ref<any[]>([])

onMounted(() => {
  console.log('流程图')
  getDetail()
})
const getDetail = () => {
  // 1. 获得流程实例相关
  processInstanceLoading.value = true
  ProcessInstanceApi.getProcessInstanceApi(id)
    .then((data) => {
      if (!data) {
        message.error('查询不到流程信息！')
        return
      }
      processInstance.value = data

      // 设置表单信息
      const processDefinition = data.processDefinition
      if (processDefinition.formType === 10) {
        setConfAndFields2(
          detailForm,
          processDefinition.formConf,
          processDefinition.formFields,
          data.formVariables
        )
        nextTick().then(() => {
          fApi.value?.btn.show(false)
          fApi.value?.resetBtn.show(false)
          fApi.value?.btn.disabled(true)
        })
      }

      // 加载流程图
      DefinitionApi.getProcessDefinitionBpmnXMLApi(processDefinition.id).then((data) => {
        bpmnXML.value = data
      })

      // 加载活动列表
      ActivityApi.getActivityList({
        processInstanceId: data.id
      }).then((data) => {
        activityList.value = data
      })
    })
    .finally(() => {
      processInstanceLoading.value = false
    })
  // 2. 获得流程任务列表（审批记录）
  TaskApi.getTaskListByProcessInstanceId(id)
    .then((data) => {
      // 审批记录
      tasks.value = []
      // 移除已取消的审批
      data.forEach((task) => {
        if (task.result !== 4) {
          tasks.value.push(task)
        }
      })
      // 排序，将未完成的排在前面，已完成的排在后面；
      tasks.value.sort((a, b) => {
        // 有已完成的情况，按照完成时间倒序
        if (a.endTime && b.endTime) {
          return b.endTime - a.endTime
        } else if (a.endTime) {
          return 1
        } else if (b.endTime) {
          return -1
          // 都是未完成，按照创建时间倒序
        } else {
          return b.createTime - a.createTime
        }
      })

      // 需要审核的记录
      tasks.value.forEach((task) => {
        // 1.1 只有待处理才需要
        if (task.result !== 1) {
          return
        }
        // 1.2 自己不是处理人
        if (!task.assigneeUser || task.assigneeUser.id !== userId) {
          return
        }
      })
    })
    .finally(() => {})
}
</script>
<style lang="scss" scoped>
.my-process-designer {
  height: calc(100vh - 200px);
}

.box-card {
  width: 100%;
  margin-bottom: 20px;
  border: none;
  box-shadow: none;
}
</style>
