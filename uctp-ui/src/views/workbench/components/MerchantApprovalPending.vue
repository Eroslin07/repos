<template>
  <ContentWrap>
    <XModal
      v-model="dialogVisible"
      :title="dialogTitle"
      :fullscreen="true"
      :showFooter="false"
      @close="closeDialog"
    >
      <el-container>
        <el-header class="header">山西万国市场商户张三账号申请待办</el-header>
        <el-main>
          <div class="btn">
            <el-button size="small" type="danger" @click="closeDialog">关闭</el-button>
            <el-button size="small" type="primary" @submit="submitBtn">提交</el-button>
            <el-button size="small" @click="terminationBtn">终止</el-button>
          </div>
          <div>单号：GZSH202303220001</div>
          <el-card class="content-box">
            <div>商户信息</div>
            <el-row :gutter="20">
              <el-col :span="6">手机号： 1538756769</el-col>
              <el-col :span="6">姓名： 张兰</el-col>
              <el-col :span="6">身份证号： 5****************1</el-col>
              <el-col :span="6">市场所在地： 山西万国</el-col>
            </el-row>
            <div>对公银行账号：66XXXXXXXXXXXXXXX</div>
            <div>
              <div>身份证图片</div>
              <el-image
                style="width: 300px; height: 100px"
                src="https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg"
                fit="fill"
              />
            </div>
            <div>
              <div>营业执照图片</div>
              <el-image
                style="width: 300px; height: 100px"
                src="https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg"
                fit="fill"
              />
            </div>
          </el-card>
        </el-main>
      </el-container>
    </XModal>
  </ContentWrap>
</template>
<script lang="ts" setup name="MerchantApprovalPending">
import { allSchemas } from '../toDoList/toDoList.data'
import { defineProps } from 'vue'
import { propTypes } from '@/utils/propTypes'
// import type { FormExpose } from '@/components/Form'
const { t } = useI18n() // 国际化
// const message = useMessage() // 消息弹窗

const [] = useXTable({
  allSchemas: allSchemas
})
const actionType = ref('detail') // 操作按钮的类型
// const dialogVisible = ref(true) // 是否显示弹出层
const dialogTitle = ref('商户主账号审批待办') // 弹出层标题
// const formRef = ref<FormExpose>() // 表单 Ref

// 设置标题
const setDialogTile = (type: string) => {
  dialogTitle.value = t('action.' + type)
  actionType.value = type
  // dialogVisible.value = true
}

const emit = defineEmits(['closeDialog'])

const props = defineProps({
  dialogVisible: propTypes.bool.def(false)
})

const dialogVisible = computed(() => {
  return props.dialogVisible
})
console.log(props)
console.log(setDialogTile)

// 关闭弹框
const closeDialog = () => {
  emit('closeDialog')
}
// 提交
const submitBtn = () => {
  emit('closeDialog')
}
// 终止
const terminationBtn = () => {
  emit('closeDialog')
}
</script>
<style lang="scss" scoped>
.header {
  font-size: 24px;
  font-weight: bold;
  text-align: center;
}
.btn {
  text-align: right;
}
.content-box {
  line-height: 36px;
}
</style>
