import type { VxeCrudSchema } from '@/hooks/web/useVxeCrudSchemas'
// 国际化
const { t } = useI18n()
// 表单校验
export const rules = reactive({
  name: [required],
  code: [required],
  sort: [required]
})
// CrudSchema
const crudSchemas = reactive<VxeCrudSchema>({
  primaryKey: 'id',
  // primaryTitle: '角色编号',
  primaryType: 'seq',
  action: true,
  actionWidth: '120px',
  columns: [
    {
      title: '发票类型',
      field: 'name',
      dictType: DICT_TYPE.COMMON_STATUS,
      dictClass: 'number',
      isSearch: true
    },
    {
      title: '商户',
      field: 'code',
      isSearch: true
    },
    {
      title: '合同名称',
      field: 'contractName'
    },
    {
      title: '合同金额',
      field: 'sort'
    },
    {
      title: '税额',
      field: 'amountTax'
    },
    {
      title: t('common.status'),
      field: 'status',
      dictType: DICT_TYPE.COMMON_STATUS,
      dictClass: 'number',
      isSearch: true
    },
    {
      title: '操作人',
      field: 'operator'
    },
    {
      title: t('common.createTime'),
      field: 'createTime',
      formatter: 'formatDate',
      isForm: false
    }
  ]
})
export const { allSchemas } = useVxeCrudSchemas(crudSchemas)
