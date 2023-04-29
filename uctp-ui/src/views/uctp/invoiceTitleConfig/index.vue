<template>
  <div>
    <el-row class="mb-4">
      <el-button type="primary" @click="handle()" plain>编辑</el-button>
      <el-button type="success" @click="submitForm()" plain>保存</el-button>
    </el-row>
    <p style="font-size: 14px">开票信息配置</p>
    <p style="font-size: 14px">市场方开票信息</p>
    <p style="font-size: 14px">开票信息</p>
    <br />
    <el-form id="invoice" :title="title" ref="formRef" :model="form" label-width="200px">
      <el-form-item label="名称:">
        <el-input v-model="form.companyName" placeholder="请输入名称" />
      </el-form-item>
      <el-form-item label="纳税人识别号/营业执照号:">
        <el-input v-model="form.taxNum" placeholder="请输入纳税人识别号" />
      </el-form-item>
      <el-form-item label="法定代表人:">
        <el-input v-model="form.represent" placeholder="请输入法定代表人" />
      </el-form-item>
      <el-form-item label="地址:">
        <el-input v-model="form.address" placeholder="请输入地址" />
      </el-form-item>
      <el-form-item label="电话:">
        <el-input v-model="form.tel" placeholder="请输入电话" />
      </el-form-item>
      <el-form-item label="开户行及账号:">
        <el-input v-model="form.opening" placeholder="请输入开户行及账号" />
      </el-form-item>
      <br />
      <p style="font-size: 14px">银行信息配置</p>
      <p style="font-size: 14px">市场方服务费收款银行</p>
      <p style="font-size: 14px">银行信息</p>
      <br />
      <el-form-item label="姓名:">
        <el-input v-model="form.name" placeholder="请输入银行卡姓名" />
      </el-form-item>
      <el-form-item label="开户行:">
        <el-input v-model="form.bank" placeholder="请输入银行卡开户行" />
      </el-form-item>
      <el-form-item label="银行卡号:">
        <el-input v-model="form.bankAccount" placeholder="请输入收款银行卡号" />
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts" name="configuration">
import {
  createMakeInvoicePageApi,
  getMakeInvoiceApi,
  updateMakeInvoiceApi
} from '@/api/configuration/invoice'

const title = ref('')

const data = reactive({
  form: {},
  queryParams: {
    id: '',
    companyName: '',
    taxNum: '',
    represent: '',
    address: '',
    tel: '',
    opening: '',
    name: '',
    bank: '',
    bankAccount: '',
    tenantId: 1
  }
})
const { form } = toRefs(data)
import { reactive, ref } from 'vue'
import { FormExpose } from '@/components/Form'
const formRef = ref<FormExpose>()
const message = useMessage() // 消息弹窗

/** 查询项目列表 */
const getList = async () => {
  const res = await getMakeInvoiceApi(150)
  console.log(res)
  if (res != null) {
    form.value = res
  }
}

/** 编辑按钮操作 */
const handle = async () => {
  if (form.value.id == null) {
    console.log('xz')
    title.value = 'edit'
  } else {
    const id = form.value.id
    console.log('id = ', id)
    const res = await getMakeInvoiceApi(id)
    console.log(res)
    console.log('修改配置信息')
  }
}

/** 保存按钮 */
const submitForm = async () => {
  /** 修改判断 */
  if (form.value.id == null) {
    console.log('xz')
    title.value = 'edit'
  } else {
    const id = form.value.id
    console.log('id = ', id)
    const res = await getMakeInvoiceApi(id)
    console.log('修改配置信息')
    console.log(res)
  }
  /** 提交保存 */
  const data1 = form.value
  console.log(data1)
  if (title.value === 'edit') {
    console.log('0000')
    await createMakeInvoicePageApi(data1)
    message.success('添加成功')
  } else {
    await updateMakeInvoiceApi(data1)
    message.success('修改成功')
  }
}
getList()
</script>
