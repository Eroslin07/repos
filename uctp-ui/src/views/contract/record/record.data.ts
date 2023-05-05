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
      title: '合同编号',
      field: 'number',
      isSearch: false
    },
    {
      title: '合同名称',
      field: 'name',
      isSearch: true
    },
    {
      title: '甲方',
      field: 'first',
      isSearch: false
    },
    {
      title: '乙方',
      field: 'second',
      isSearch: false
    },
    {
      title: '商户',
      field: 'merchant',
      isSearch: true
    },
    {
      title: '合同类型',
      field: 'type',
      dictType: DICT_TYPE.CONTRACT_TEMP_TYPE,
      dictClass: 'number',
      isSearch: true
    },
    {
      title: '签约金额',
      field: 'amount',
      isSearch: false
    },
    {
      title: t('common.status'),
      field: 'status',
      dictType: DICT_TYPE.CONTRACT_CONTRACT_STATUS,
      dictClass: 'number',
      isSearch: true
    },
    {
      title: '发起时间',
      field: 'initiationTime',
      formatter: 'formatDate',
      isForm: true
    },
    {
      title: '签约时间',
      field: 'signTime',
      formatter: 'formatDate',
      isForm: false
    }
  ]
})
export const { allSchemas } = useVxeCrudSchemas(crudSchemas)
