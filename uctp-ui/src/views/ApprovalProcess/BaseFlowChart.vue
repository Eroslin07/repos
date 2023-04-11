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
import { setConfAndFields2 } from '@/utils/formCreate'
import type { ApiAttrs } from '@form-create/element-ui/types/config'
import { onMounted, ref } from 'vue'

const { query } = useRoute() // 查询参数
const message = useMessage() // 消息弹窗
const processInstance = ref<any>({}) // 流程实例

const processInstanceLoading = ref(false)

const id = (query.id as unknown as number) || '3052e817-d521-11ed-93fa-005056c00008'

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
