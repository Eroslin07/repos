<template>
  <ContentWrap>
    <el-container>
      <el-main>
        <div>
          <div style="font-size: 16px" class="title">
            <span>单号：{{ baseInfoData.data.serialNo }}</span>
            <span>商户经办人：{{ baseInfoData.data.variables.startUserName }}</span>
            <span>商户电话：{{ baseInfoData.data.variables.startUserMobile }}</span>
          </div>
        </div>
        <div class="content-box">
          <el-row>
            <el-col :span="2" class="bg-yell">姓名：</el-col>
            <el-col :span="4"> {{ mainValue.formDataJson.name || '暂无数据' }}</el-col>
            <el-col :span="2" class="bg-yell">手机号：</el-col>
            <el-col :span="4">{{ mainValue.formDataJson.phone || '暂无数据' }}</el-col>
            <el-col :span="2" class="bg-yell">身份证号： </el-col>
            <el-col :span="10">{{ mainValue.formDataJson.idCard || '暂无数据' }}</el-col>
          </el-row>
          <el-row>
            <el-col :span="2" class="bg-yell">营业执照号： </el-col>
            <el-col :span="4">{{ mainValue.formDataJson.taxNum || '暂无数据' }}</el-col>
            <el-col :span="2" class="bg-yell">公司名称： </el-col>
            <el-col :span="4">{{ mainValue.formDataJson.businessName || '暂无数据' }}</el-col>
            <el-col :span="2" class="bg-yell">法定代表人： </el-col>
            <el-col :span="4">{{
              mainValue.formDataJson.legal_representative || '暂无数据'
            }}</el-col>
            <el-col :span="2" class="bg-yell">市场所在地： </el-col>
            <el-col :span="4">{{
              mainValue.formDataJson.marketLocationValue || '暂无数据'
            }}</el-col>
          </el-row>
          <el-row>
            <el-col :span="2" class="bg-yell">开户行：</el-col>
            <el-col :span="4">
              <div>{{ mainValue.formDataJson.bankName || '暂无数据' }}</div>
            </el-col>
            <el-col :span="2" class="bg-yell">对公银行账号：</el-col>
            <el-col :span="4">
              <div>{{ mainValue.formDataJson.bankNumber || '暂无数据' }}</div>
            </el-col>
            <el-col :span="2" class="bg-yell">保证金充值卡号：</el-col>
            <el-col :span="10">
              <div>{{ mainValue.formDataJson.bondBankAccount || '暂无数据' }}</div>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="2" class="bg-yell">联系地址： </el-col>
            <el-col :span="22">{{ mainValue.formDataJson.address || '暂无数据' }}</el-col>
          </el-row>
          <div class="image">
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
      </el-main>
    </el-container>
  </ContentWrap>
</template>
<script lang="ts" setup name="MerchantApprovalPending">
import { allSchemas } from '../toDoList/toDoList.data'
import { defineProps } from 'vue'
import { propTypes } from '@/utils/propTypes'
// 详情
import { baseInfoData } from '@/views/workbench/basInfoValue'
console.log(baseInfoData.data)
const { t } = useI18n() // 国际化

const [] = useXTable({
  allSchemas: allSchemas
})

const actionType = ref('detail') // 操作按钮的类型
const dialogTitle = ref('商户主账号审批待办') // 弹出层标题

// 设置标题
const setDialogTile = (type: string) => {
  dialogTitle.value = t('action.' + type)
  actionType.value = type
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

const props = defineProps({
  type: propTypes.bool.def(undefined)
})

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
.title {
  > span {
    margin-right: 20px;
    font-weight: 600;
    color: #333333;
  }
  margin-bottom: 16px;
}
.header {
  font-size: 24px;
  font-weight: bold;
  text-align: center;
}
.btn {
  text-align: right;
}
.content-box {
  color: #606266;
  .bg-yell {
    background: #f5f5f5;
    display: flex;
    text-align: right;
    justify-content: flex-end;
    padding-right: 5px;
  }
}
:deep(.el-main) {
  padding: 0;
}
:deep(.el-col) {
  border-right: 1px solid #eaeaea;
  border-bottom: 1px solid #eaeaea;
  display: flex;
  height: 40px;
  align-items: center;
}
.content-box .el-row:first-child {
  border-top: 1px solid #eaeaea;
}
.content-box .el-row .el-col:nth-child(1) {
  border-left: 1px solid #eaeaea;
}
.content-box .el-row .el-col:nth-child(even) {
  padding-left: 15px;
}
.image {
  padding: 16px 0 20px 18px;
  border-left: 1px solid #eaeaea;
  border-right: 1px solid #eaeaea;
  border-bottom: 1px solid #eaeaea;
}
</style>
