import type { VxeCrudSchema } from '@/hooks/web/useVxeCrudSchemas'

// 表单校验
export const rules = reactive({
  name: [required]
})

// CrudSchema
const crudSchemas = reactive<VxeCrudSchema>({
  primaryKey: 'id',
  primaryType: 'seq',
  action: true,
  columns: [
    {
      title: '发票类型',
      field: 'name',
      isSearch: true,
      dictType: DICT_TYPE.COMMON_STATUS,
      dictClass: 'number',
      table: {
        minWidth: 180
      }
    },
    {
      title: '商户',
      field: 'status',
      isSearch: true,
      table: {
        minWidth: 180
      }
    },
    {
      title: '合同名称',
      field: 'remark',
      table: {
        minWidth: 180
      }
    },
    {
      title: '合同金额',
      field: 'createTime',
      table: {
        minWidth: 180
      }
    },
    {
      title: '税额',
      field: 'createTime',
      table: {
        minWidth: 180
      }
    },
    {
      title: '状态',
      field: 'createTime',
      isSearch: true,
      dictType: DICT_TYPE.COMMON_STATUS,
      dictClass: 'number',
      table: {
        minWidth: 180
      }
    },
    {
      title: '操作人',
      field: 'createTime',
      table: {
        minWidth: 180
      }
    },
    {
      title: '操作时间',
      field: 'createTime',
      table: {
        minWidth: 180
      }
    }
  ]
})
export const { allSchemas } = useVxeCrudSchemas(crudSchemas)
