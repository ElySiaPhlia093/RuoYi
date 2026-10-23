<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="巡检编号" prop="inspectNo">
        <el-input v-model="queryParams.inspectNo" placeholder="请输入巡检编号" clearable style="width: 200px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="设备编号" prop="deviceCode">
        <el-input v-model="queryParams.deviceCode" placeholder="请输入设备编号" clearable style="width: 200px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="巡检类型" prop="inspectType">
        <el-select v-model="queryParams.inspectType" placeholder="请选择巡检类型" clearable style="width: 200px">
          <el-option v-for="dict in inspect_type" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="巡检结果" prop="result">
        <el-select v-model="queryParams.result" placeholder="请选择巡检结果" clearable style="width: 200px">
          <el-option v-for="dict in inspect_result" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="巡检时间">
        <el-date-picker
          v-model="dateRange"
          value-format="YYYY-MM-DD"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          style="width: 240px"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['device:inspect:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate" v-hasPermi="['device:inspect:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['device:inspect:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['device:inspect:export']">导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="inspectList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="巡检编号" align="center" prop="inspectNo" width="130" />
      <el-table-column label="设备编号" align="center" prop="deviceCode" width="110" />
      <el-table-column label="设备名称" align="center" prop="deviceName" min-width="150" show-overflow-tooltip />
      <el-table-column label="安装位置" align="center" prop="location" min-width="130" show-overflow-tooltip />
      <el-table-column label="巡检类型" align="center" prop="inspectType" width="100">
        <template #default="scope">
          <dict-tag :options="inspect_type" :value="scope.row.inspectType" />
        </template>
      </el-table-column>
      <el-table-column label="巡检结果" align="center" prop="result" width="100">
        <template #default="scope">
          <dict-tag :options="inspect_result" :value="scope.row.result" />
        </template>
      </el-table-column>
      <el-table-column label="巡检人" align="center" prop="inspectUserName" width="100" />
      <el-table-column label="巡检时间" align="center" prop="inspectTime" width="160">
        <template #default="scope">
          <span>{{ parseTime(scope.row.inspectTime, '{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="检查项" align="center" prop="checkItems" min-width="150" show-overflow-tooltip />
      <el-table-column label="问题描述" align="center" prop="problemDesc" min-width="160" show-overflow-tooltip />
      <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['device:inspect:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['device:inspect:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form ref="inspectRef" :model="form" :rules="rules" label-width="90px">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="巡检编号" prop="inspectNo">
              <el-input v-model="form.inspectNo" placeholder="请输入巡检编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="巡检设备" prop="deviceId">
              <el-select v-model="form.deviceId" placeholder="请选择设备" filterable style="width: 100%">
                <el-option v-for="item in deviceOptions" :key="item.deviceId" :label="item.deviceCode + ' ' + item.deviceName" :value="item.deviceId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="巡检类型" prop="inspectType">
              <el-select v-model="form.inspectType" placeholder="请选择巡检类型" style="width: 100%">
                <el-option v-for="dict in inspect_type" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="巡检结果" prop="result">
              <el-select v-model="form.result" placeholder="请选择巡检结果" style="width: 100%">
                <el-option v-for="dict in inspect_result" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="巡检时间" prop="inspectTime">
              <el-date-picker v-model="form.inspectTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择巡检时间" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="巡检人" prop="inspectUserId">
              <el-input v-model="form.inspectUserId" placeholder="请输入巡检人ID" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="检查项" prop="checkItems">
              <el-input v-model="form.checkItems" placeholder="如：压力/温度/振动/润滑/防护" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="问题描述" prop="problemDesc">
              <el-input v-model="form.problemDesc" type="textarea" :rows="2" placeholder="异常时填写具体问题" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入内容" />
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

<script setup name="Inspect">
import { listInspect, getInspect, delInspect, addInspect, updateInspect } from '@/api/device/inspect'
import { listDevice } from '@/api/device/device'

const { proxy } = getCurrentInstance()
const { inspect_type, inspect_result } = useDict('inspect_type', 'inspect_result')

const inspectList = ref([])
const deviceOptions = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref('')
const dateRange = ref([])

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    inspectNo: undefined,
    deviceCode: undefined,
    inspectType: undefined,
    result: undefined
  },
  rules: {
    inspectNo: [{ required: true, message: '巡检编号不能为空', trigger: 'blur' }],
    deviceId: [{ required: true, message: '巡检设备不能为空', trigger: 'change' }],
    inspectType: [{ required: true, message: '巡检类型不能为空', trigger: 'change' }],
    result: [{ required: true, message: '巡检结果不能为空', trigger: 'change' }],
    inspectTime: [{ required: true, message: '巡检时间不能为空', trigger: 'change' }]
  }
})

const { queryParams, form, rules } = toRefs(data)

function getDeviceOptions() {
  listDevice({ pageNum: 1, pageSize: 1000 }).then(res => {
    deviceOptions.value = res.rows || []
  })
}

function getList() {
  loading.value = true
  listInspect(proxy.addDateRange(queryParams.value, dateRange.value)).then(res => {
    inspectList.value = res.rows
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
    inspectId: null,
    inspectNo: null,
    deviceId: null,
    inspectUserId: null,
    inspectType: 'daily',
    inspectTime: null,
    checkItems: '压力/温度/振动/润滑/防护',
    result: 'normal',
    problemDesc: null,
    orderId: null,
    remark: null
  }
  proxy.resetForm('inspectRef')
}

function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

function resetQuery() {
  dateRange.value = []
  proxy.resetForm('queryRef')
  handleQuery()
}

function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.inspectId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

function handleAdd() {
  reset()
  open.value = true
  title.value = '添加设备巡检'
}

function handleUpdate(row) {
  reset()
  const _inspectId = row.inspectId || ids.value
  getInspect(_inspectId).then(res => {
    form.value = res.data
    open.value = true
    title.value = '修改设备巡检'
  })
}

function submitForm() {
  proxy.$refs['inspectRef'].validate(valid => {
    if (!valid) return
    if (form.value.inspectId != null) {
      updateInspect(form.value).then(() => {
        proxy.$modal.msgSuccess('修改成功')
        open.value = false
        getList()
      })
    } else {
      addInspect(form.value).then(() => {
        proxy.$modal.msgSuccess('新增成功')
        open.value = false
        getList()
      })
    }
  })
}

function handleDelete(row) {
  const _inspectIds = row.inspectId || ids.value
  proxy.$modal.confirm('是否确认删除巡检记录编号为"' + _inspectIds + '"的数据项？').then(() => {
    return delInspect(_inspectIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess('删除成功')
  }).catch(() => {})
}

function handleExport() {
  proxy.download('device/inspect/export', {
    ...queryParams.value
  }, `inspect_${new Date().getTime()}.xlsx`)
}

getList()
getDeviceOptions()
</script>
