<template>
  <div class="reverse">
    <!-- <XModal
      v-model="dialogVisible"
      :title="dialogTitle"
      :fullscreen="true"
      :showFooter="false"
      @close="handleClose"
    > -->
    <!-- <p style="text-align: center; font-weight: bold; font-size: 24px"
      >收车合同SCHT202303220001开票待办</p
    > -->
    <!-- 发票单号 -->
    <div class="title">
      <p>单号：SCKP202303220001</p>
      <p>商户经办人：XX</p>
      <p>商户电话：XX</p>
      <div style="float: right">
        <!-- 操作按钮 -->
        <!-- <el-button type="danger" @click="handleClose">关闭</el-button> -->
        <el-button type="primary" v-if="type == 'need'" :loading="actionLoading" @click="handlePut"
          >打印/开票</el-button
        >
      </div>
    </div>
    <!-- <el-card class="box-card"> -->
    <p style="font-weight: bold; margin-bottom: 10px">发票信息</p>
    <p
      >收车合同<span style="color: #63b0ff">SCHT202303220001</span
      >已经签约完成并已付款。现需要向卖车方<span style="color: #63b0ff">陈某</span
      >的开具反向二手车通用发票。开票内容如下，请平台方开具反向二手车统一发票！</p
    >
    <div class="content">
      <el-row>
        <el-col :span="4" class="bg-blue">
          <span>买车单位/个人</span>
        </el-col>
        <el-col :span="8">
          <span>市场名称（平台名称）</span>
        </el-col>
        <el-col :span="4" class="bg-blue"><span>单位代码/身份证号码</span></el-col>
        <el-col :span="8"><span>平台开票信息的纳税人识别号</span></el-col>
      </el-row>
      <el-row>
        <el-col :span="4" class="bg-blue">
          <span>买车单位/个人住址</span>
        </el-col>
        <el-col :span="8">
          <span>平台开票信息住址</span>
        </el-col>
        <el-col :span="4" class="bg-blue"><span>电话</span></el-col>
        <el-col :span="8"><span>平台开票信息的电话</span></el-col>
      </el-row>
      <el-row>
        <el-col :span="4" class="bg-blue">
          <span>卖方单位/个人</span>
        </el-col>
        <el-col :span="8">
          <span>卖方信息的姓名</span>
        </el-col>
        <el-col :span="4" class="bg-blue"><span>单位代码/身份证号码</span></el-col>
        <el-col :span="8"><span>卖方信息的身份证号</span></el-col>
      </el-row>
      <el-row>
        <el-col :span="4" class="bg-blue">
          <span>卖车单位/个人住址</span>
        </el-col>
        <el-col :span="8">
          <span>卖方信息的联系地址</span>
        </el-col>
        <el-col :span="4" class="bg-blue"><span>电话</span></el-col>
        <el-col :span="8"><span>卖方信息的联系电话</span></el-col>
      </el-row>
      <el-row>
        <el-col :span="4" class="bg-blue">
          <span>车牌号</span>
        </el-col>
        <el-col :span="4">
          <span><span>车辆信息的车牌号</span></span>
        </el-col>
        <el-col :span="4" class="bg-blue"><span>登记证书</span></el-col>
        <el-col :span="4"><span>车辆信息的登记证号</span></el-col>
        <el-col :span="4" class="bg-blue"><span>车辆类型</span></el-col>
        <el-col :span="4"><span>车辆信息的车辆类型</span></el-col>
      </el-row>
      <el-row>
        <el-col :span="4" class="bg-blue">
          <span>车架号/车辆识别代码</span>
        </el-col>
        <el-col :span="4">
          <span>车辆信息车架号</span>
        </el-col>
        <el-col :span="4" class="bg-blue"><span>厂牌型号</span></el-col>
        <el-col :span="4"><span>车辆信息的品牌型号</span></el-col>
        <el-col :span="4" class="bg-blue"><span>转入地车辆管理所名称</span></el-col>
        <el-col :span="4"><el-input></el-input></el-col>
      </el-row>
      <el-row>
        <el-col :span="4" class="bg-blue">
          <span>车辆价款</span>
        </el-col>
        <el-col :span="20">
          <span>车辆价款及交易方式的收车金额</span>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="4" class="bg-blue">
          <span>二手车市场</span>
        </el-col>
        <el-col :span="8">
          <span>市场名称（平台名称）</span>
        </el-col>
        <el-col :span="4" class="bg-blue"
          ><div class="flex-column">
            <span>纳税人识别号</span>
            <span>地址</span>
          </div></el-col
        >
        <el-col :span="8"
          ><div class="flex-column">
            <span>平台开票信息的纳税人识别号</span>
            <span>平台开票信息的地址</span>
          </div></el-col
        >
      </el-row>
      <el-row>
        <el-col :span="4" class="bg-blue">
          <span>开户行账号</span>
        </el-col>
        <el-col :span="8">
          <span>平台开票信息的开户行及账号</span>
        </el-col>
        <el-col :span="4" class="bg-blue"><span>电话</span></el-col>
        <el-col :span="8"><span>平台开票信息的电话</span></el-col>
      </el-row>
    </div>
    <p style="font-weight: bold; margin-bottom: 10px">合同信息</p>
    <p style="margin-bottom: 10px"
      >XXX收车委托合同 <button class="colr159" @click="viewContract">查看</button></p
    >
    <p style="margin-bottom: 10px"
      >XXX收车合同 <button class="colr159" @click="viewContract">查看</button></p
    >
    <!-- </el-card> -->

    <!-- 查看合同 -->
    <AgreementFrame :visible="contractVisible" @handle-cancel="handleCancel" />
    <!-- </XModal> -->
  </div>
</template>

<script setup lang="ts" name="Reverse">
import { AgreementFrame } from './index'
// import { propTypes } from '@/utils/propTypes'

// const emit = defineEmits(['cancelForm'])

const actionLoading = ref(false) // 遮罩层
// const dialogTitle = ref('反向二手车统一发票待办') // 弹出层标题

// 合同弹框
const contractVisible = ref(false)

// const props = defineProps({
//   // visible: propTypes.bool.def(false),
//   type: propTypes.bool.def(undefined)
// })

// const dialogVisible = computed(() => {
//   const obj = props.visible
//   return obj
// })

// 查看合同
const viewContract = () => {
  contractVisible.value = true
}

// 关闭合同弹框
const handleCancel = () => {
  contractVisible.value = false
}

// 关闭操作
// const handleClose = () => {
//   emit('cancelForm')
// }

// 打印操作
const handlePut = () => {
  actionLoading.value = true
}
</script>

<style lang="scss" scoped>
.reverse {
  .title {
    overflow: hidden;
    margin-bottom: 10px;
    display: flex;
    justify-content: space-around;
    > p {
      margin-right: 10px;
    }
  }
}
p {
  font-size: 16px;
}

// .box-card {
//   height: 80vh;
// }

.colr159 {
  color: #1592c9;
}
.flex-column {
  width: 100%;
  display: flex;
  flex-direction: column;
  > span:nth-child(1) {
    border-bottom: 1px solid #000;
  }
}
:deep(.el-col) {
  border-right: 1px solid #000;
  border-bottom: 1px solid #000;
  display: flex;
  align-items: center;
}
.content .bg-blue {
  background: #ddebf7;
}
.content .el-row:first-child {
  border-top: 1px solid #000;
}
.content .el-row .el-col:nth-child(1) {
  border-left: 1px solid #000;
}
</style>
