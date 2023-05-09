<template>
  <ContentWrap>
    <!-- 列表 -->
    <XTable @register="registerTable">
      <!-- 操作：新增 -->
      <template #toolbar_buttons>
        <!-- 操作：新增 -->
        <XButton type="primary" preIcon="ep:zoom-in" title="新增" @click="handleCreate()" />
        <!-- 操作：导出 -->
        <XButton
          type="warning"
          title="下载"
          preIcon="ep:download"
          @click="exportList('税率配置.xls')"
        />
      </template>
      <template #actionbtns_default="{ row }">
        <!-- 操作：修改 -->
        <XTextButton
          v-hasPermi="['uctp:tax:update']"
          :title="t('action.edit')"
          preIcon="ep:edit"
          @click="handleUpdate(row.id)"
        />
        <!-- 操作：详情 -->
        <XTextButton
          v-hasPermi="['uctp:tax:query']"
          :title="t('action.detail')"
          preIcon="ep:view"
          @click="handleDetail(row.id)"
        />
      </template>
    </XTable>
  </ContentWrap>
  <!-- 弹窗 -->
  <XModal id="taxModel" v-model="dialogVisible" :title="dialogTitle">
    <!-- 对话框(添加 / 修改) -->
    <Form
      v-if="['create', 'update'].includes(actionType)"
      ref="formRef"
      :rules="rules"
      :schema="allSchemas.formSchema"
    />
    <!-- 对话框(详情) -->
    <Descriptions
      v-if="actionType === 'detail'"
      :data="detailData"
      :schema="allSchemas.detailSchema"
    >
      <template #content="{ row }">
        <Editor :model-value="row.content" :readonly="true" />
      </template>
    </Descriptions>
    <template #footer>
      <!-- 按钮：保存 -->
      <XButton
        v-if="['create', 'update'].includes(actionType)"
        :loading="actionLoading"
        :title="t('action.save')"
        type="primary"
        @click="submitForm()"
      />
      <!-- 按钮：关闭 -->
      <XButton :loading="actionLoading" :title="t('dialog.close')" @click="dialogVisible = false" />
    </template>
  </XModal>
</template>
<script lang="ts" name="Tax" setup>
import type { FormExpose } from '@/components/Form'
// 业务相关的 import
import { allSchemas } from './Tax.data'
import * as TaxApi from '@/api/uctp/taxConfig'

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗
// 列表相关的变量
const [registerTable, { reload, exportList }] = useXTable({
  allSchemas: allSchemas,
  getListApi: TaxApi.getTaxPageApi, // 查詢数据的 API
  exportListApi: TaxApi.exportTaxApi // 导出数据的 API
})
// 弹窗相关的变量
const dialogVisible = ref(false) // 是否显示弹出层
const dialogTitle = ref('edit') // 弹出层标题
const actionType = ref('') // 操作按钮的类型
const actionLoading = ref(false) // 按钮 Loading
const formRef = ref<FormExpose>() // 表单 Ref
const detailData = ref() // 详情 Ref

// 设置标题
const setDialogTile = (type: string) => {
  dialogTitle.value = t('action.' + type)
  actionType.value = type
  dialogVisible.value = true
}

// 新增操作
const handleCreate = () => {
  setDialogTile('create')
}

// 修改操作
const handleUpdate = async (rowId: number) => {
  setDialogTile('update')
  // 设置数据
  const res = await TaxApi.getTaxApi(rowId)
  unref(formRef)?.setValues(res)
}

// 详情操作
const handleDetail = async (rowId: number) => {
  setDialogTile('detail')
  // 设置数据
  const res = await TaxApi.getTaxApi(rowId)
  detailData.value = res
}

// 提交新增/修改的表单
const submitForm = async () => {
  const elForm = unref(formRef)?.getElFormRef()
  if (!elForm) return
  elForm.validate(async (valid) => {
    if (valid) {
      actionLoading.value = true
      // 提交请求
      try {
        const data = unref(formRef)?.formModel as TaxApi.TaxVO
        let taxRate = ''
        taxRate = (data.taxRate * 100).toFixed(2) + '%'
        data.taxRate = taxRate
        if (actionType.value === 'create') {
          await TaxApi.createTaxApi(data)
          message.success(t('common.createSuccess'))
        } else {
          await TaxApi.updateTaxApi(data)
          message.success(t('common.updateSuccess'))
        }
        dialogVisible.value = false
      } finally {
        actionLoading.value = false
        await reload()
      }
    }
  })
}
</script>
