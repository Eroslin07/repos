<template>
  <el-card class="box-card" v-loading="tasksLoad">
    <el-col :span="24">
      <div class="block">
        <el-timeline class="titmline-box">
          <el-timeline-item
            v-for="(item, index) in tasks"
            :key="index"
            :icon="getTimelineItemIcon(item)"
            :type="getTimelineItemType(item)"
          >
            <p style="font-weight: 700">任务：{{ item.name }}</p>
            <el-card :body-style="{ padding: '10px' }">
              <label v-if="item.assigneeUser" style="font-weight: normal; margin-right: 30px">
                审批人：{{ item.assigneeUser.nickname }}
                <el-tag type="info" size="small">{{ item.assigneeUser.deptName }}</el-tag>
              </label>
              <label style="font-weight: normal" v-if="item.createTime">创建时间：</label>
              <label style="color: #8a909c; font-weight: normal">
                {{ dayjs(item?.createTime).format('YYYY-MM-DD HH:mm:ss') }}
              </label>
              <label v-if="item.endTime" style="margin-left: 30px; font-weight: normal">
                审批时间：
              </label>
              <label v-if="item.endTime" style="color: #8a909c; font-weight: normal">
                {{ dayjs(item?.endTime).format('YYYY-MM-DD HH:mm:ss') }}
              </label>
              <label v-if="item.durationInMillis" style="margin-left: 30px; font-weight: normal">
                耗时：
              </label>
              <label v-if="item.durationInMillis" style="color: #8a909c; font-weight: normal">
                {{ formatPast2(item?.durationInMillis) }}
              </label>
              <p v-if="item.reason">
                <el-tag :type="getTimelineItemType(item)">{{ item.reason }}</el-tag>
              </p>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </div>
    </el-col>
  </el-card>
</template>
<script setup lang="ts">
import dayjs from 'dayjs'
import { formatPast2 } from '@/utils/formatTime'
import * as TaskApi from '@/api/bpm/task'
import { useUserStore } from '@/store/modules/user'
import { onMounted, ref } from 'vue'

import { baseInfoData } from '@/views/workbench/basInfoValue'

const id = baseInfoData.data.procInstId
const tasksLoad = ref(true)
const tasks = ref<any[]>([])
const runningTasks = ref<any[]>([]) // 运行中的任务
const auditForms = ref<any[]>([]) // 审批任务的表单
const userId = useUserStore().getUser.id // 当前登录的编号
onMounted(() => {
  getDetail()
})
const getDetail = () => {
  tasksLoad.value = true
  runningTasks.value = []
  auditForms.value = []
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
        // 2. 添加到处理任务
        runningTasks.value.push({ ...task })
        auditForms.value.push({
          reason: ''
        })
      })
    })
    .finally(() => {
      tasksLoad.value = false
    })
}

const getTimelineItemIcon = (item) => {
  if (item.result === 1) {
    return 'el-icon-time'
  }
  if (item.result === 2) {
    return 'el-icon-check'
  }
  if (item.result === 3) {
    return 'el-icon-close'
  }
  if (item.result === 4) {
    return 'el-icon-remove-outline'
  }
  return ''
}
const getTimelineItemType = (item) => {
  if (item.result === 1) {
    return 'primary'
  }
  if (item.result === 2) {
    return 'success'
  }
  if (item.result === 3) {
    return 'danger'
  }
  if (item.result === 4) {
    return 'info'
  }
  return ''
}
</script>

<style lang="scss" scoped>
.box-card {
  width: 100%;
  margin-bottom: 20px;
  border: none;
  box-shadow: none;
}
</style>
