<template>
  <ContentWrap>
    <!-- <XModal
      v-model="dialogVisible"
      :title="dialogTitle"
      :fullscreen="true"
      :showFooter="false"
      @close="closeDialog"
    > -->
    <el-container>
      <!-- <el-header class="header">山西万国市场商户张三账号申请待办</el-header> -->
      <!-- <el-header class="header">{{
        (baseInfoData.data.variables.marketName || '') +
        (baseInfoData.data.variables.merchantName || '') +
        '账号申请流程'
      }}</el-header> -->
      <el-main>
        <div style="overflow: hidden; margin-bottom: 10px">
          <div style="float: left; font-size: 16px">单号：{{ baseInfoData.data.serialNo }}</div>
          <div class="btn">
            <!-- <el-button type="danger" @click="closeDialog">关闭</el-button> -->
            <!-- <el-button type="primary" v-if="type == 'need'" @submit="submitBtn">提交</el-button>
            <el-button v-if="type == 'need'" @click="terminationBtn">终止</el-button> -->
          </div>
        </div>
        <!-- <el-card class="content-box"> -->
        <div class="content-box">
          <div>商户信息</div>
          <el-row>
            <el-col :span="6">姓名： {{ mainValue.formDataJson.name || '暂无数据' }}</el-col>
            <el-col :span="6">手机号： {{ mainValue.formDataJson.phone || '暂无数据' }}</el-col>
            <el-col :span="6">身份证号： {{ mainValue.formDataJson.idCard || '暂无数据' }}</el-col>
          </el-row>
          <div>
            <el-image
              v-for="(img, index) in mainValue.formDataJson.idCardUrl"
              :key="img.id"
              style="width: 200px; height: 100px; margin-right: 10px"
              :src="img.url"
              :zoom-rate="1.2"
              :preview-src-list="idcardUrlArr"
              fit="cover"
              :initial-index="index"
            />
          </div>
          <el-row>
            <el-col :span="6"
              >营业执照号： {{ mainValue.formDataJson.taxNum || '暂无数据' }}</el-col
            >
            <el-col :span="6"
              >公司名称： {{ mainValue.formDataJson.businessName || '暂无数据' }}</el-col
            >
            <el-col :span="6"
              >法定代表人： {{ mainValue.formDataJson.legal_representative || '暂无数据' }}</el-col
            >
            <el-col :span="6"
              >市场所在地： {{ mainValue.formDataJson.marketLocationValue || '暂无数据' }}</el-col
            >
          </el-row>
          <el-row>
            <el-col :span="6">
              <div>开户行：{{ mainValue.formDataJson.bankName || '暂无数据' }}</div>
            </el-col>
            <el-col :span="6">
              <div>对公银行账号：{{ mainValue.formDataJson.bankNumber || '暂无数据' }}</div>
            </el-col>
            <el-col :span="6">
              <div>保证金充值卡号：{{ mainValue.formDataJson.bondBankAcconut || '暂无数据' }}</div>
            </el-col>
          </el-row>
          <el-row>
            <el-col>联系地址： {{ mainValue.formDataJson.address || '暂无数据' }}</el-col>
          </el-row>
          <div>
            <el-image
              v-for="item in mainValue.formDataJson.businessLicense"
              :key="item.id"
              style="width: 200px; height: 100px"
              :src="item.url"
              :zoom-rate="1.2"
              :preview-src-list="busUrlArr"
              fit="cover"
              :initial-index="0"
            />
          </div>
        </div>
        <!-- </el-card> -->
      </el-main>
    </el-container>
    <!-- </XModal> -->
  </ContentWrap>
</template>
<script lang="ts" setup name="MerchantApprovalPending">
import { allSchemas } from '../toDoList/toDoList.data'
import { defineProps } from 'vue'
import { propTypes } from '@/utils/propTypes'
// import type { FormExpose } from '@/components/Form'
// 详情
import { baseInfoData } from '@/views/workbench/basInfoValue'
console.log(baseInfoData.data)
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

// 详情
let mainValue = reactive({
  formDataJson: { idCardUrl: [{ url: '' }], businessLicense: [{ url: '' }] }
})

nextTick(() => {
  mainValue.formDataJson = { ...baseInfoData.data.variables.formDataJson.formMain.formDataJson }
})

const idcardUrlArr = computed(() => {
  return mainValue.formDataJson.idCardUrl.map((item) => item.url)
})

const busUrlArr = computed(() => {
  return mainValue.formDataJson.businessLicense.map((item) => item.url)
})

// const emit = defineEmits(['closeDialog'])

const props = defineProps({
  // dialogVisible: propTypes.bool.def(false),
  type: propTypes.bool.def(undefined)
})

// const dialogVisible = computed(() => {
//   return props.dialogVisible
// })
console.log(props)
console.log(setDialogTile)

// 关闭弹框
// const closeDialog = () => {
//   emit('closeDialog')
// }
// 提交
// const submitBtn = () => {
//   emit('closeDialog')
// }
// 终止
// const terminationBtn = () => {
//   emit('closeDialog')
// }
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
