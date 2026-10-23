<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="工单编号" prop="orderNo">
        <el-input v-model="queryParams.orderNo" placeholder="请输入工单编号" clearable style="width: 200px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="设备编号" prop="deviceCode">
        <el-input v-model="queryParams.deviceCode" placeholder="请输入设备编号" clearable style="width: 200px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="维修类型" prop="repairType">
        <el-select v-model="queryParams.repairType" placeholder="请选择维修类型" clearable style="width: 200px">
          <el-option v-for="dict in repair_type" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="维修结果" prop="result">
        <el-select v-model="queryParams.result" placeholder="请选择维修结果" clearable style="width: 200px">
          <el-option v-for="dict in repair_result" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['repairorder:record:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate" v-hasPermi="['repairorder:record:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['repairorder:record:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['repairorder:record:export']">导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="recordList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="工单编号" align="center" prop="orderNo" width="120" />
      <el-table-column label="设备编号" align="center" prop="deviceCode" width="110" />
      <el-table-column label="设备名称" align="center" prop="deviceName" min-width="150" show-overflow-tooltip />
      <el-table-column label="维修类型" align="center" prop="repairType" width="100">
        <template #default="scope">
          <dict-tag :options="repair_type" :value="scope.row.repairType" />
        </template>
      </el-table-column>
      <el-table-column label="故障原因" align="center" prop="faultCause" min-width="150" show-overflow-tooltip />
      <el-table-column label="处理过程" align="center" prop="handleDesc" min-width="160" show-overflow-tooltip />
      <el-table-column label="使用备件" align="center" prop="partName" width="150" show-overflow-tooltip>
        <template #default="scope">
          <span v-if="scope.row.partName">{{ scope.row.partName }} x{{ scope.row.partQty }}</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="费用(元)" align="center" prop="cost" width="100" />
      <el-table-column label="耗时(分)" align="center" prop="durationMin" width="90" />
      <el-table-column label="维修结果" align="center" prop="result" width="100">
        <template #default="scope">
          <dict-tag :options="repair_result" :value="scope.row.result" />
        </template>
      </el-table-column>
      <el-table-column label="维修人" align="center" prop="handlerName" width="100" />
      <el-table-column label="维修时间" align="center" prop="recordTime" width="160">
        <template #default="scope">
          <span>{{ parseTime(scope.row.recordTime, '{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['repairorder:record:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['repairorder:record:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" v-model="open" width="640px" append-to-body>
      <el-form ref="recordRef" :model="form" :rules="rules" label-width="90px">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="工单编号" prop="orderNo">
              <el-input v-model="form.orderNo" placeholder="请输入工单编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备" prop="deviceId">
              <el-select v-model="form.deviceId" placeholder="请选择设备" filterable style="width: 100%">
                <el-option v-for="item in deviceOptions" :key="item.deviceId" :label="item.deviceCode + ' ' + item.deviceName" :value="item.deviceId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="维修类型" prop="repairType">
              <el-select v-model="form.repairType" placeholder="请选择维修类型" style="width: 100%">
                <el-option v-for="dict in repair_type" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="维修结果" prop="result">
              <el-select v-model="form.result" placeholder="请选择维修结果" style="width: 100%">
                <el-option v-for="dict in repair_result" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="故障原因" prop="faultCause">
              <el-input v-model="form.faultCause" placeholder="请输入故障原因" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="处理过程" prop="handleDesc">
              <el-input v-model="form.handleDesc" type="textarea" :rows="2" placeholder="请输入处理过程" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="使用备件" prop="partId">
              <el-select v-model="form.partId" placeholder="可不选" filterable clearable style="width: 100%">
                <el-option v-for="item in partOptions" :key="item.partId" :label="item.partCode + ' ' + item.partName" :value="item.partId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="备件数量" prop="partQty">
              <el-input-number v-model="form.partQty" :min="0" style="width: 100%" controls-position="right" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="费用(元)" prop="cost">
              <el-input-number v-model="form.cost" :min="0" :precision="2" style="width: 100%" controls-position="right" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="耗时(分)" prop="durationMin">
              <el-input-number v-model="form.durationMin" :min="0" style="width: 100%" controls-position="right" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="维修时间" prop="recordTime">
              <el-date-picker v-model="form.recordTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择时间" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="维修人" prop="handlerUserId">
              <el-input v-model="form.handlerUserId" placeholder="默认当前登录用户" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Record">
import { listRecord, getRecord, delRecord, addRecord, updateRecord } from '@/api/repairorder/record'
import { listDevice } from '@/api/device/device'
import { listPart } from '@/api/device/part'

const { proxy } = getCurrentInstance()
const { repair_type, repair_result } = useDict('repair_type', 'repair_result')

const recordList = ref([])
const deviceOptions = ref([])
const partOptions = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref('')

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    orderNo: undefined,
    deviceCode: undefined,
    repairType: undefined,
    result: undefined
  },
  rules: {
    handleDesc: [{ required: true, message: '处理过程不能为空', trigger: 'blur' }],
    repairType: [{ required: true, message: '维修类型不能为空', trigger: 'change' }],
    result: [{ required: true, message: '维修结果不能为空', trigger: 'change' }]
  }
})

const { queryParams, form, rules } = toRefs(data)

function getDeviceOptions() {
  listDevice({ pageNum: 1, pageSize: 1000 }).then(res => {
    deviceOptions.value = res.rows || []
  })
}

function getPartOptions() {
  listPart({ pageNum: 1, pageSize: 1000 }).then(res => {
    partOptions.value = res.rows || []
  })
}

function getList() {
  loading.value = true
  listRecord(queryParams.value).then(res => {
    recordList.value = res.rows
    total.value = res.total
    loading.value = false
  })
}

function cancel() {
  open.value = false
  reset()
}

function reset() {
  form.value = {
    recordId: null,
    orderId: null,
    deviceId: null,
    handlerUserId: null,
    repairType: 'repair',
    faultCause: null,
    handleDesc: null,
    partId: null,
    partQty: 0,
    cost: 0,
    durationMin: 0,
    result: 'repaired',
    recordTime: null,
    remark: null
  }
  proxy.resetForm('recordRef')
}

function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

function resetQuery() {
  proxy.resetForm('queryRef')
  handleQuery()
}

function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.recordId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

function handleAdd() {
  reset()
  open.value = true
  title.value = '添加维修记录'
}

function handleUpdate(row) {
  reset()
  const _recordId = row.recordId || ids.value
  getRecord(_recordId).then(res => {
    form.value = res.data
    open.value = true
    title.value = '修改维修记录'
  })
}

function submitForm() {
  proxy.$refs['recordRef'].validate(valid => {
    if (!valid) return
    if (form.value.recordId != null) {
      updateRecord(form.value).then(() => {
        proxy.$modal.msgSuccess('修改成功')
        open.value = false
        getList()
      })
    } else {
      addRecord(form.value).then(() => {
        proxy.$modal.msgSuccess('新增成功')
        open.value = false
        getList()
      })
    }
  })
}

function handleDelete(row) {
  const _recordIds = row.recordId || ids.value
  proxy.$modal.confirm('是否确认删除维修记录编号为"' + _recordIds + '"的数据项？').then(() => {
    return delRecord(_recordIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess('删除成功')
  }).catch(() => {})
}

function handleExport() {
  proxy.download('repairorder/record/export', {
    ...queryParams.value
  }, `record_${new Date().getTime()}.xlsx`)
}

getList()
getDeviceOptions()
getPartOptions()
</script>
