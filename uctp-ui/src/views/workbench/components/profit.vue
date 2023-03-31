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
        <el-header class="header">商户张三收车价格超公允值审批待办</el-header>
        <el-main>
          <div style="overflow: hidden; margin-bottom: 10px">
            <p style="float: left">单号：LRTQ202303220001</p>
            <div class="btn">
              <!-- 操作按钮 -->
              <el-button type="danger" @click="closeDialog">关闭</el-button>
              <el-button type="primary" v-if="type == 'need'" @click="passBtn">在线转账</el-button>
              <el-button v-if="type == 'need'" @click="returnBtn">退回</el-button>
            </div>
          </div>
          <el-card class="content-box">
            <h3 style="font-weight: bold">利润提取信息</h3>
            <el-form>
              <el-row>
                <el-col :span="6">
                  <el-form-item label="商户：">
                    <div> 张三</div>
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item label="手机号：">
                    <div> 15328756760</div>
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item label="提取金额：">
                    <div style="color: #3dacdc"> 5,000.00元</div>
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item label="剩余金额：">
                    <div> 0.00元</div>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-form-item label="提取账号：">
                  <div>66XXXXXXXXXXXXXXX</div>
                </el-form-item>
              </el-row>
              <el-row>
                <el-form-item label="发票照片：">
                  <el-image
                    style="width: 200px; height: 100px; margin-right: 10px"
                    v-for="url in srcList"
                    :key="url"
                    :src="url"
                    :zoom-rate="1.2"
                    :preview-src-list="srcList"
                    fit="cover"
                    :initial-index="0"
                  />
                </el-form-item>
              </el-row>
            </el-form>
            <el-button type="primary" @click="passBtn">利润明细</el-button>
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

const [] = useXTable({
  allSchemas: allSchemas
})

// 预览图片
const srcList = [
  'https://fuss10.elemecdn.com/a/3f/3302e58f9a181d2509f3dc0fa68b0jpeg.jpeg',
  'https://fuss10.elemecdn.com/1/34/19aa98b1fcb2781c4fba33d850549jpeg.jpeg'
]
const dialogTitle = ref('利润提取待办') // 弹出层标题

const emit = defineEmits(['cancelForm'])
const props = defineProps({
  visible: propTypes.bool.def(false),
  type: propTypes.bool.def(undefined)
})

const dialogVisible = computed(() => {
  return props.visible
})

// 关闭弹框
const closeDialog = () => {
  emit('cancelForm')
}

// 通过
const passBtn = () => {
  emit('cancelForm')
}

// 退回
const returnBtn = () => {
  emit('cancelForm')
}
</script>
<style lang="scss" scoped>
.header {
  font-size: 24px;
  font-weight: bold;
  text-align: center;
}

p {
  font-size: 16px;
}

.btn {
  float: right;
}

.content-box {
  line-height: 36px;
}
.carInfo {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.identify {
  display: inline-block;
  width: 136px;
}
</style>
