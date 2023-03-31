<template>
  <div class="reverse">
    <XModal
      v-model="dialogVisible"
      :title="dialogTitle"
      :fullscreen="true"
      :showFooter="false"
      @close="handleClose"
    >
      <!-- 发票单号 -->
      <div style="overflow: hidden; margin-bottom: 10px">
        <p style="float: left">单号：SCKP202303220001</p>
        <div style="float: right">
          <!-- 操作按钮 -->
          <el-button type="danger" @click="handleClose">关闭</el-button>
          <el-button
            type="primary"
            v-if="type == 'need'"
            :loading="actionLoading"
            @click="handlePut"
            >打印/开票</el-button
          >
        </div>
      </div>
      <el-card class="box-card">
        <p style="text-align: center; font-weight: bold; font-size: 24px"
          >收车合同SCHT202303220001开票待办</p
        >
        <p style="font-weight: bold; margin-bottom: 10px">发票信息</p>
        <p
          >收车合同<span style="color: #63b0ff">SCHT202303220001</span
          >已经签约完成并已付款。现需要向卖车方<span style="color: #63b0ff">陈某</span
          >的开具反向二手车通用发票。开票内容如下，请平台方开具反向二手车统一发票！</p
        >
        <div class="content">
          <p>开票内容</p>
          <p>1、</p>
          <p>2、</p>
          <p>3、</p>
          <p>4、</p>
        </div>
        <p style="font-weight: bold; margin-bottom: 10px">合同信息</p>
        <p style="margin-bottom: 10px">XXX收车委托合同 <span style="color: #33a8db">查看</span></p>
        <p style="margin-bottom: 10px">XXX收车合同 <span style="color: #33a8db">查看</span></p>
      </el-card>
    </XModal>
  </div>
</template>

<script setup lang="ts" name="Reverse">
import { propTypes } from '@/utils/propTypes'

const emit = defineEmits(['cancelForm'])

const actionLoading = ref(false) // 遮罩层
const dialogTitle = ref('反向二手车统一发票待办') // 弹出层标题

const props = defineProps({
  visible: propTypes.bool.def(false),
  type: propTypes.bool.def(undefined)
})

const dialogVisible = computed(() => {
  const obj = props.visible
  return obj
})

// 关闭操作
const handleClose = () => {
  emit('cancelForm')
}

// 打印操作
const handlePut = () => {
  actionLoading.value = true
}
</script>

<style lang="scss" scoped>
p {
  font-size: 16px;
}

// .box-card {
//   height: 80vh;
// }

.content {
  border: 1px solid #000;
  padding: 10px;
  margin: 10px 0;
}
</style>
