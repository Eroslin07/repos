<template>
  <ContentWrap style="height: 100%">
    <XModal
      v-model="visible"
      :title="dialogTitle"
      :fullscreen="true"
      :showFooter="false"
      @close="closeDialog"
    >
      <el-container>
        <el-header class="header">商户张三卖车价格超公允值审批待办</el-header>
        <el-main>
          <div class="btn">
            <el-button type="danger" @click="closeDialog">关闭</el-button>
            <el-button type="primary" @click="passBtn">通过</el-button>
            <el-button @click="returnBtn">退回</el-button>
          </div>
          <el-card class="content-box">
            <div>单号：GZSH202303220001</div>
            <h3 style="font-weight: bold" class="mb20">车辆基础信息</h3>
            <el-form>
              <el-row>
                <el-col :span="6">
                  <el-form-item label="行驶证：">
                    <div>
                      <span>2张</span>
                      <el-image
                        v-for="url in urls"
                        :key="url"
                        style="width: 40px; height: 40px; margin: 0 5px; vertical-align: top"
                        :src="url"
                        fit="fill"
                        :zoom-rate="1.2"
                        :preview-src-list="urls"
                      />
                    </div>
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item label="机动车登记证：">
                    <div>
                      <span>2张</span>
                      <el-image
                        v-for="url in urls"
                        :key="url"
                        style="width: 40px; height: 40px; margin: 0 5px; vertical-align: top"
                        :src="url"
                        fit="fill"
                        :zoom-rate="1.2"
                        :preview-src-list="urls"
                      />
                    </div>
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item label="车辆图片：">
                    <div>
                      <span>2张</span>
                      <el-image
                        v-for="url in urls"
                        :key="url"
                        style="width: 40px; height: 40px; margin: 0 5px; vertical-align: top"
                        :src="url"
                        fit="fill"
                        :zoom-rate="1.2"
                        :preview-src-list="urls"
                      />
                    </div>
                  </el-form-item>
                </el-col>
                <el-col :span="6"><div></div></el-col>
              </el-row>
              <el-row>
                <el-col :span="6">
                  <el-form-item label="车架号：">
                    <div> LE4TG4DB1JL199517</div>
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item label="发动机编号：">
                    <div> 98596482</div>
                  </el-form-item>
                </el-col>
                <el-col :span="6" v-if="false">
                  <el-form-item label="首次登记日期：">
                    <div> 2013-09-08</div>
                  </el-form-item>
                </el-col>
                <el-col :span="6" style="padding-right: 10px">
                  <el-form-item label="品牌/年代/型号：">
                    <el-tooltip
                      class="box-item"
                      effect="dark"
                      content="宝马-宝马X1 2021款 sDrive20Li 时尚型"
                      placement="top-start"
                    >
                      <div class="carInfo"> 宝马-宝马X1 2021款 sDrive20Li 时尚型</div>
                    </el-tooltip>
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item label="里程数：">
                    <div>200公里</div>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="6" v-if="false">
                  <el-form-item label="里程数：">
                    <div>200公里</div>
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item label="其它信息：">
                    <div>甲方XXXX，乙方XXXX</div>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="6">
                  <el-form-item label="收车金额：">
                    <div>30,000.00元</div>
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item label="卖车金额：">
                    <div>120,000.00元</div>
                    <el-tag class="tag-class" size="small" type="danger"
                      >公允值范围：80,000元—100,000元</el-tag
                    >
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-form-item label="收车方式 ：">
                  <div>全款</div>
                </el-form-item>
              </el-row>

              <h3 style="font-weight: bold" class="mb20">卖家信息</h3>
              <el-row>
                <el-form-item label="是否第三方代收： ">
                  <div>否</div>
                </el-form-item>
              </el-row>
              <el-row>
                <el-col :span="6">
                  <el-form-item label="身份证">
                    <span class="identify" v-if="identifyShow"> 5*****************1</span>
                    <span class="identify" v-else>512345678990102345</span>
                    <Icon
                      style="margin: 0 6px"
                      icon="ep:view"
                      v-if="identifyShow"
                      @click="identifyShow = !identifyShow"
                    />
                    <Icon
                      style="margin: 0 6px"
                      icon="ep:hide"
                      v-else
                      @click="identifyShow = !identifyShow"
                    />
                    <span @click="viewIdCard" class="colr159">查看</span>
                    <div v-if="idCardShow">
                      <el-image
                        v-for="item in 2"
                        :key="item"
                        style="width: 70px; height: 70px; margin-right: 5px"
                        src="https://fuss10.elemecdn.com/a/3f/3302e58f9a181d2509f3dc0fa68b0jpeg.jpeg"
                        fit="cover"
                        :zoom-rate="1.2"
                        :preview-src-list="srcList"
                        :initial-index="0"
                      />
                    </div>
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item label="姓名：">
                    <div>陈某</div>
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item label="电话：">
                    <div>15328756760</div>
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item label="收款方式：">
                    <div>转账</div>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-form-item label="收款信息：">
                  <div>68XXXXXXXXXXXXXXXX</div>
                </el-form-item>
              </el-row>

              <h3 style="font-weight: bold" class="mb20">合同信息</h3>
              <el-row>
                <el-form-item label="XXX收车委托合同">
                  <div class="colr159" @click="viewContract">查看</div>
                </el-form-item>
              </el-row>
              <el-row>
                <el-form-item label="XXX收车合同">
                  <button type="button" class="colr159" @click="viewContract">查看</button>
                </el-form-item>
              </el-row>
            </el-form>
          </el-card>

          <XModal
            v-model="contractVisible"
            title="合同"
            width="60%"
            :showFooter="false"
            style="height: 100%"
          >
            <iframe
              src="https://element-plus.org/zh-CN/component/form.html#%E5%85%B8%E5%9E%8B%E8%A1%A8%E5%8D%95"
              frameborder="0"
              style="width: 100%; height: 100vh"
            ></iframe>
          </XModal>
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

// 预览图片
const srcList = [
  'https://fuss10.elemecdn.com/a/3f/3302e58f9a181d2509f3dc0fa68b0jpeg.jpeg',
  'https://fuss10.elemecdn.com/1/34/19aa98b1fcb2781c4fba33d850549jpeg.jpeg'
]
const actionType = ref('detail') // 操作按钮的类型
// const dialogVisible = ref(true) // 是否显示弹出层
const dialogTitle = ref('卖车价格超公允值待办') // 弹出层标题
// const formRef = ref<FormExpose>() // 表单 Ref

// 设置标题
const setDialogTile = (type: string) => {
  dialogTitle.value = t('action.' + type)
  actionType.value = type
  // dialogVisible.value = true
}

const urls = [
  'https://fuss10.elemecdn.com/a/3f/3302e58f9a181d2509f3dc0fa68b0jpeg.jpeg',
  'https://fuss10.elemecdn.com/1/34/19aa98b1fcb2781c4fba33d850549jpeg.jpeg'
]

const identifyShow = ref(false)
const idCardShow = ref(false)

// 合同弹框
const contractVisible = ref(false)
const emit = defineEmits(['cancleSellCar'])
const props = defineProps({
  visible: propTypes.bool.def(false)
})

const visible = computed(() => {
  return props.visible
})

console.log(setDialogTile)

// 查看身份证
const viewIdCard = () => {
  idCardShow.value = !idCardShow.value
}

// 查看合同
const viewContract = () => {
  console.log(1111)
  contractVisible.value = true
}
// 关闭弹框
const closeDialog = () => {
  emit('cancleSellCar')
}

// 通过
const passBtn = () => {
  emit('cancleSellCar')
}

// 退回
const returnBtn = () => {
  emit('cancleSellCar')
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
  margin-bottom: 20px;
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
.tag-class {
  position: relative;
  left: 0;
}
.mb20 {
  margin-bottom: 20px;
}

.colr159 {
  color: #1592c9;
}
</style>
