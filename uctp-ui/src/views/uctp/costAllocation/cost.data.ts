import type { VxeCrudSchema } from '@/hooks/web/useVxeCrudSchemas'

// 增删改查 CrudSchema 配置
const crudSchemas = reactive<VxeCrudSchema>({
  primaryKey: 'id',
  primaryType: 'id',
  primaryTitle: '序号',
  action: true,
  columns: [
    {
      title: '费用类型',
      field: 'costType',
      dictType: DICT_TYPE.COST_TYPE,
      dictClass: 'number',
      isSearch: true
    },
    {
      title: '费用（元）',
      dictClass: 'number',
      field: 'cost'
    },
    {
      title: '车辆类型（未定义）',
      field: 'vehicleType'
    },
    {
      title: '费用生效日期始',
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
      title: '费用生效日期止',
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
