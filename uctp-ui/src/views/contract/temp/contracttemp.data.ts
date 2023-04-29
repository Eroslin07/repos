import type { VxeCrudSchema } from '@/hooks/web/useVxeCrudSchemas'
// 国际化
const { t } = useI18n()

// 表单校验
export const rules = reactive({
  title: [required],
  type: [required]
})

// CrudSchema
const crudSchemas = reactive<VxeCrudSchema>({
  primaryKey: 'id',
  primaryType: 'seq',
  action: true,
  columns: [
    {
      title: '模板编号',
      field: 'number',
      isSearch: false
    },
    {
      title: '模板名称',
      field: 'name',
      isSearch: true
    },
    {
      title: '合同类型',
      field: 'type',
      dictType: DICT_TYPE.CONTRACT_TEMP_TYPE,
      dictClass: 'number'
    },
    {
      title: '说明',
      field: 'remark',
      isSearch: false
    },
    {
      title: t('common.status'),
      field: 'status',
      dictType: DICT_TYPE.UCTP_STATUS,
      dictClass: 'number'
    },
    {
      title: '操作人',
      field: 'createdBy',
      isSearch: true
    },
    {
      title: '操作时间',
      field: 'createdTime',
      formatter: 'formatDate',
      isForm: false
    }
  ]
})
export const { allSchemas } = useVxeCrudSchemas(crudSchemas)
