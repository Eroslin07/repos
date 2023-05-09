import type { VxeCrudSchema } from '@/hooks/web/useVxeCrudSchemas'

// 增删改查 CrudSchema 配置
const crudSchemas = reactive<VxeCrudSchema>({
  primaryKey: 'id',
  primaryType: 'id',
  primaryTitle: '序号',
  action: true,
  columns: [
    {
      title: '税费类型',
      field: 'taxType',
      dictType: DICT_TYPE.TAX_TYP,
      isSearch: true
    },
    {
      title: '税率（%）',
      field: 'taxRate'
    },
    {
      title: '税率生效日期始',
      field: 'effectiveData',
      formatter: 'formatDate',
      form: {
        component: 'DatePicker',
        componentProps: {
          type: 'datetime',
          valueFormat: 'x'
        }
      }
    },
    {
      title: '税率生效日期止',
      field: 'expirationData',
      formatter: 'formatDate',
      form: {
        component: 'DatePicker',
        componentProps: {
          type: 'datetime',
          valueFormat: 'x'
        }
      }
    },
    {
      title: '操作人',
      field: 'createdBy',
      isSearch: true,
      isForm: false
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
