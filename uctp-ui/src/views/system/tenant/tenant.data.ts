import type { VxeCrudSchema } from '@/hooks/web/useVxeCrudSchemas'
import { getTenantPackageByTypeList, TenantPackageVO } from '@/api/system/tenantPackage'
import { ComponentOptions } from '@/types/components'

const { t } = useI18n() // 国际化

export const tenantPackageOption: ComponentOptions[] = []
const getTenantPackageOptions = async () => {
  const res = await getTenantPackageByTypeList(1)
  res.forEach((tenantPackage: TenantPackageVO) => {
    tenantPackageOption.push({
      key: tenantPackage.id,
      value: tenantPackage.id,
      label: tenantPackage.name
    })
  })

  return tenantPackageOption
}
getTenantPackageOptions()

// 表单校验
export const rules = reactive({
  name: [required],
  type: [required],
  packageId: [required],
  contactName: [required],
  contactMobile: [required],
  accountCount: [required],
  expireTime: [required],
  username: [
    required,
    {
      min: 4,
      max: 30,
      trigger: 'blur',
      message: '用户名称长度为 4-30 个字符'
    }
  ],
  password: [
    required,
    {
      min: 4,
      max: 16,
      trigger: 'blur',
      message: '密码长度为 4-16 位'
    }
  ],
  domain: [required],
  status: [required]
})

// CrudSchema.
const crudSchemas = reactive<VxeCrudSchema>({
  primaryKey: 'id',
  primaryTitle: '租户编号',
  primaryType: 'seq',
  action: true,
  columns: [
    {
      title: '租户名称',
      field: 'name',
      isSearch: true
    },
    {
      title: '租户状态',
      field: 'status',
      dictType: DICT_TYPE.COMMON_STATUS,
      dictClass: 'number',
      isSearch: true
    },
    {
      title: '租户属性',
      field: 'type',
      dictType: DICT_TYPE.TENANT_PACKAGE_TYPE,
      dictClass: 'number',
      isSearch: true
    },
    {
      title: '租户套餐',
      field: 'packageId',
      table: {
        slots: {
          default: 'packageId_default'
        }
      },
      form: {
        component: 'Select',
        componentProps: {
          options: tenantPackageOption
        }
      }
    },
    {
      title: '联系人',
      field: 'contactName',
      isSearch: true
    },
    {
      title: '联系手机',
      field: 'contactMobile',
      isSearch: true
    },
    {
      title: '用户名称',
      field: 'username',
      isTable: false,
      isDetail: false
    },
    {
      title: '用户密码',
      field: 'password',
      isTable: false,
      isDetail: false,
      form: {
        component: 'InputPassword'
      }
    },
    {
      title: '账号额度',
      field: 'accountCount',
      table: {
        slots: {
          default: 'accountCount_default'
        }
      },
      form: {
        component: 'InputNumber'
      }
    },
    {
      title: '过期时间',
      field: 'expireTime',
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
      title: '绑定域名',
      field: 'domain'
    },
    {
      title: t('table.createTime'),
      field: 'createTime',
      formatter: 'formatDate',
      isForm: false,
      search: {
        show: true,
        itemRender: {
          name: 'XDataTimePicker'
        }
      }
    }
  ]
})
export const { allSchemas } = useVxeCrudSchemas(crudSchemas)
