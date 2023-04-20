import type { VxeCrudSchema } from '@/hooks/web/useVxeCrudSchemas'

// 表单校验
export const rules = reactive({
  name: [required]
})

// CrudSchema
const crudSchemas = reactive<VxeCrudSchema>({
  // primaryKey: 'id',
  // primaryType: 'seq',
  action: false,
  columns: [
    {
      title: '申请单号',
      field: 'serialNo',
      isSearch: true,
      table: {
        minWidth: 180,
        slots: {
          default: 'application_default'
        }
      }
    },
    {
      title: '事项',
      field: 'title',
      isSearch: true,
      dictClass: 'number',
      table: {
        minWidth: 180
      }
    },
    {
      title: '商户',
      field: 'merchantName',
      table: {
        minWidth: 180
      }
    },
    {
      title: '申请人',
      field: 'startUserName',
      isSearch: true,
      table: {
        minWidth: 180
      }
    },
    {
      title: '申请时间',
      field: 'submitTime',
      isSearch: true,
      formatter: 'formatDate',
      table: {
        minWidth: 180
      }
    },
    {
      title: '到达时间',
      field: 'arrivalTime',
      formatter: 'formatDate',
      table: {
        minWidth: 180
      }
    },
    {
      title: '审批人',
      field: 'assigneeUserName',
      table: {
        minWidth: 180
      }
    }
  ]
})
export const { allSchemas } = useVxeCrudSchemas(crudSchemas)
