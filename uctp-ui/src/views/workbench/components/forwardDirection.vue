<template>
  <div class="reverse">
    <div style="font-size: 16px" class="title">
      <span>单号：{{ baseInfoData.data.serialNo }}</span>
      <span>商户经办人：{{ '张三' }}</span>
      <span>商户电话：{{ '13333333333' }}</span>
      <div style="float: right"> </div>
    </div>
    <p style="font-weight: bold; margin-bottom: 10px">发票信息</p>
    <div class="title-bg-box">
      <icon icon="svg-icon:notice" class="notice"></icon>
      <div class="title-box">
        卖车合同
        <span style="color: #63b0ff">SCHT202303220001</span>
        已经签约完成并已付款。现需要向卖车方
        <span style="color: #63b0ff">陈某</span>
        的开具正向二手车通用发票。开票内容如下，请平台方开具反向二手车统一发票！
      </div>
    </div>
    <div class="content">
      <el-row>
        <el-col :span="4" class="bg-blue">
          <span>买车单位/个人</span>
        </el-col>
        <el-col :span="8">
          <span>买方信息的姓名</span>
        </el-col>
        <el-col :span="4" class="bg-blue"><span>单位代码/身份证号码</span></el-col>
        <el-col :span="8"><span>买方信息的身份证号</span></el-col>
      </el-row>
      <el-row>
        <el-col :span="4" class="bg-blue">
          <span>买车单位/个人住址</span>
        </el-col>
        <el-col :span="8">
          <span>买方信息的联系地址</span>
        </el-col>
        <el-col :span="4" class="bg-blue"><span>电话</span></el-col>
        <el-col :span="8"><span>买方信息的联系电话</span></el-col>
      </el-row>
      <el-row>
        <el-col :span="4" class="bg-blue">
          <span>卖方单位/个人</span>
        </el-col>
        <el-col :span="8">
          <span>市场名称（平台名称）</span>
        </el-col>
        <el-col :span="4" class="bg-blue"><span>单位代码/身份证号码</span></el-col>
        <el-col :span="8"><span>平台开票信息的纳税人识别号</span></el-col>
      </el-row>
      <el-row>
        <el-col :span="4" class="bg-blue">
          <span>卖车单位/个人住址</span>
        </el-col>
        <el-col :span="8">
          <span>平台开票信息住址</span>
        </el-col>
        <el-col :span="4" class="bg-blue"><span>电话</span></el-col>
        <el-col :span="8"><span>平台开票信息的电话</span></el-col>
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
        <el-col :span="4"><el-input class="car-input"></el-input></el-col>
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
          ><div class="flex-column flex-content">
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
    <p style="font-weight: bold; margin: 10px 0">增值税通用发票</p>
    <div class="prompt">
      <div>
        <span>个人开票:</span>
        <span>买方信息的姓名</span>
      </div>
      <div>
        <span
          >提示：开票的时候请先咨询对方开票抬头，如果个人开票确认下姓名，进行开票，如果公司抬头开票，请线下询问开票内容进行开票。</span
        >
      </div>
    </div>
    <p style="font-weight: bold; margin: 10px 0">合同信息</p>
    <div class="content">
      <el-row>
        <el-col :span="4" class="bg-blue">
          <span>XXX收车委托合同</span>
        </el-col>
        <el-col :span="4">
          <span><button class="colr159" @click="viewContract">查看</button></span>
        </el-col>
        <el-col :span="4" class="bg-blue">
          <span>XXX收车合同</span>
        </el-col>
        <el-col :span="12">
          <span>
            <button class="colr159" @click="viewContract">查看</button>
          </span>
        </el-col>
      </el-row>
    </div>
    <AgreementFrame :visible="contractVisible" @handle-cancel="handleCancel" />
  </div>
</template>

<script setup lang="ts" name="Reverse">
import { AgreementFrame } from './index'
import { baseInfoData } from '@/views/workbench/basInfoValue'
// 合同弹框
const contractVisible = ref(false)
// 查看合同
const viewContract = () => {
  contractVisible.value = true
}

// 关闭合同弹框
const handleCancel = () => {
  contractVisible.value = false
}
</script>

<style lang="scss" scoped>
.reverse {
  .title {
    > span {
      margin-right: 20px;
      font-weight: 600;
      color: #333333;
    }
    margin-bottom: 16px;
  }
  padding-bottom: 50px;
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
  span {
    width: 100%;
    text-align: right;
  }
  > span:nth-child(1) {
    border-bottom: 1px solid #eaeaea;
  }
}
:deep(.el-col) {
  box-sizing: border-box;
  border-right: 1px solid #eaeaea;
  border-bottom: 1px solid #eaeaea;
  display: flex;
  height: 40px;
  align-items: center;
  span {
    padding-left: 15px;
  }
}
.flex-content {
  padding: 0;
  span {
    text-align: left;
  }
}
.content .bg-blue {
  background: #f5f5f5;
  display: flex;
  justify-content: flex-end;
  span {
    padding-right: 14px;
  }
}
.content .el-row:first-child {
  border-top: 1px solid #eaeaea;
}
.content .el-row .el-col:nth-child(1) {
  border-left: 1px solid #eaeaea;
}
.title-bg-box {
  margin: 14px 0 16px;
  width: 100%;
  overflow: hidden;
  height: 34px;
  background: rgba(250, 100, 0, 0.1);
  border-radius: 2px;
  padding-top: 8px;
  align-items: center;
  padding-left: 40px;
  position: relative;
  .notice {
    position: absolute;
    left: 20px;
    top: 8px;
  }
  .title-box {
    width: 200%;
    display: flex;
    color: #fa6400;
  }
}
.car-input {
  height: 100%;
}
.prompt {
  border: 1px solid #eaeaea;
  padding: 20px;
  > div:first-child {
    margin-bottom: 20px;
  }
  > div:last-child {
    color: #f59a23;
  }
}
</style>
