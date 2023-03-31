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
      title: '申请单号',
      field: 'name',
      isSearch: true,
      dictClass: 'number',
      table: {
        minWidth: 180
      }
    },
    {
      title: '事项',
      field: 'item',
      isSearch: true,
      dictClass: 'number',
      table: {
        minWidth: 180
      }
    },
    {
      title: '商户',
      field: 'status',
      table: {
        minWidth: 180
      }
    },
    {
      title: '申请人',
      field: 'applicant',
      isSearch: true,
      table: {
        minWidth: 180
      }
    },
    {
      title: '申请时间',
      field: 'createTime',
      isSearch: true,
      dictClass: 'number',
      dictType: DICT_TYPE.COMMON_STATUS,
      table: {
        minWidth: 180
      }
    },
    {
      title: '到达时间',
      field: 'arrivalTime',
      table: {
        minWidth: 180
      }
    },
    {
      title: '审批人',
      field: 'approver',
      table: {
        minWidth: 180
      }
    }
  ]
})
export const { allSchemas } = useVxeCrudSchemas(crudSchemas)
