import type { VxeCrudSchema } from '@/hooks/web/useVxeCrudSchemas'
// 国际化
const { t } = useI18n()

// CrudSchema
const crudSchemas = reactive<VxeCrudSchema>({
  primaryKey: 'id',
  // primaryTitle: '角色编号',
  primaryType: 'seq',
  action: false,
  actionWidth: '120px',
  columns: [
    {
      title: '发票类型',
      field: 'type',
      dictType: DICT_TYPE.SETTLEMENT_INVOICE_TYPE,
      dictClass: 'number',
      isSearch: true
    },
    {
      title: '商户',
      field: 'merchant',
      isSearch: true
    },
    {
      title: '合同名称',
      field: 'name'
    },
    {
      title: '合同金额',
      field: 'amount'
    },
    {
      title: '税额',
      field: 'tax'
    },
    {
      title: t('common.status'),
      field: 'status',
      dictType: DICT_TYPE.SETTLEMENT_INVOICE_STATUS,
      dictClass: 'number',
      isSearch: true
    },
    {
      title: '操作人',
      field: 'founder'
    },
    {
      title: t('common.createTime'),
      field: 'creationTime',
      formatter: 'formatDate',
      isForm: false
    }
  ]
})
export const { allSchemas } = useVxeCrudSchemas(crudSchemas)
