<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="工单编号" prop="orderNo">
        <el-input
          v-model="queryParams.orderNo"
          placeholder="请输入工单编号"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="报修设备 " prop="deviceId">
        <el-select v-model="queryParams.deviceId" placeholder="请选择报修设备" filterable clearable>
          <el-option
            v-for="item in deviceOptions"
            :key="item.deviceId"
            :label="item.deviceCode + ' ' + item.deviceName"
            :value="item.deviceId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="故障类型" prop="faultType">
        <el-select v-model="queryParams.faultType" placeholder="请选择故障类型" clearable>
          <el-option
            v-for="dict in fault_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="报修人 " prop="reportUserId">
        <el-input
          v-model="queryParams.reportUserId"
          placeholder="请输入报修人 "
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="报修时间" prop="reportTime">
        <el-date-picker clearable
          v-model="queryParams.reportTime"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择报修时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="工单状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择工单状态" clearable>
          <el-option
            v-for="dict in order_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="处理人 " prop="handlerUserId">
        <el-input
          v-model="queryParams.handlerUserId"
          placeholder="请输入处理人 "
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="受理时间" prop="handleTime">
        <el-date-picker clearable
          v-model="queryParams.handleTime"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择受理时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="完成时间" prop="finishTime">
        <el-date-picker clearable
          v-model="queryParams.finishTime"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择完成时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['repairorder:repairorder:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['repairorder:repairorder:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['repairorder:repairorder:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['repairorder:repairorder:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="repairorderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键" align="center" prop="orderId" />
      <el-table-column label="工单编号" align="center" prop="orderNo" />
      <el-table-column label="设备ID " align="center" prop="deviceId" />
      <el-table-column label="设备编号" align="center" prop="deviceCode" />
      <el-table-column label="设备名称" align="center" prop="deviceName" />
      <el-table-column label="安装位置" align="center" prop="location" />
      <el-table-column label="故障类型" align="center" prop="faultType">
        <template #default="scope">
          <dict-tag :options="fault_type" :value="scope.row.faultType"/>
        </template>
      </el-table-column>
      <el-table-column label="故障描述" align="center" prop="faultDesc" />
      <el-table-column label="报修人 " align="center" prop="reportUserId">
        <template #default="scope">
          <span>{{ scope.row.reportUserName || scope.row.reportUserId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="报修时间" align="center" prop="reportTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.reportTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="工单状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :options="order_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="处理人 " align="center" prop="handlerUserId">
        <template #default="scope">
          <span>{{ scope.row.handlerName || scope.row.handlerUserId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="受理时间" align="center" prop="handleTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.handleTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="处理结果" align="center" prop="handleResult" />
      <el-table-column label="完成时间" align="center" prop="finishTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.finishTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button v-if="scope.row.status === '0'" link type="success" icon="Check" @click="handleAccept(scope.row)" v-hasPermi="['repairorder:repairorder:edit']">受理</el-button>
          <el-button v-if="scope.row.status === '1'" link type="primary" icon="Finished" @click="handleFinish(scope.row)" v-hasPermi="['repairorder:repairorder:edit']">办结</el-button>
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['repairorder:repairorder:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['repairorder:repairorder:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改报修工单对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="repairorderRef" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="工单编号" prop="orderNo">
              <el-input v-model="form.orderNo" placeholder="请输入工单编号" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="报修设备 " prop="deviceId">
              <el-select v-model="form.deviceId" placeholder="请选择报修设备" filterable clearable style="width: 100%">
                <el-option
                  v-for="item in deviceOptions"
                  :key="item.deviceId"
                  :label="item.deviceCode + ' ' + item.deviceName"
                  :value="item.deviceId"
                >
                  <span style="float: left">{{ item.deviceCode }}</span>
                  <span style="float: right; color: #8492a6; font-size: 13px">{{ item.deviceName }}</span>
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="故障类型" prop="faultType">
              <el-select v-model="form.faultType" placeholder="请选择故障类型">
                <el-option
                  v-for="dict in fault_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="故障描述" prop="faultDesc">
              <el-input v-model="form.faultDesc" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="报修人 " prop="reportUserId">
              <el-input v-model="form.reportUserId" placeholder="请输入报修人 " />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="报修时间" prop="reportTime">
              <el-date-picker clearable
                v-model="form.reportTime"
                type="date"
                value-format="YYYY-MM-DD"
                placeholder="请选择报修时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="受理时间" prop="handleTime">
              <el-date-picker clearable
                v-model="form.handleTime"
                type="date"
                value-format="YYYY-MM-DD"
                placeholder="请选择受理时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
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

    <!-- 办结工单对话框：落维修记录 + 扣减备件库存 -->
    <el-dialog title="办结工单" v-model="finishOpen" width="640px" append-to-body>
      <el-alert
        :title="'工单 ' + finishInfo.orderNo + ' / ' + (finishInfo.deviceCode || '') + ' ' + (finishInfo.deviceName || '')"
        type="info"
        :closable="false"
        style="margin-bottom: 16px"
      />
      <el-form ref="finishRef" :model="finishForm" :rules="finishRules" label-width="100px">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="维修类型" prop="repairType">
              <el-select v-model="finishForm.repairType" placeholder="请选择维修类型" style="width: 100%">
                <el-option v-for="dict in repair_type" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="维修结果" prop="result">
              <el-select v-model="finishForm.result" placeholder="请选择维修结果" style="width: 100%">
                <el-option v-for="dict in repair_result" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="故障原因" prop="faultCause">
              <el-input v-model="finishForm.faultCause" placeholder="请输入故障原因" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="处理过程" prop="handleDesc">
              <el-input v-model="finishForm.handleDesc" type="textarea" :rows="3" placeholder="请输入处理过程/维修说明" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="使用备件" prop="partId">
              <el-select v-model="finishForm.partId" placeholder="请选择备件（可不选）" filterable clearable style="width: 100%">
                <el-option
                  v-for="item in partOptions"
                  :key="item.partId"
                  :label="item.partCode + ' ' + item.partName"
                  :value="item.partId"
                >
                  <span style="float: left">{{ item.partCode }} {{ item.partName }}</span>
                  <span style="float: right; color: #8492a6; font-size: 13px">库存 {{ item.stock }}</span>
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="备件数量" prop="partQty">
              <el-input-number v-model="finishForm.partQty" :min="0" :max="99" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="维修费用" prop="cost">
              <el-input-number v-model="finishForm.cost" :min="0" :precision="2" :step="10" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="耗时(分钟)" prop="durationMin">
              <el-input-number v-model="finishForm.durationMin" :min="0" :step="10" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" :loading="finishLoading" @click="submitFinish">确认办结</el-button>
          <el-button @click="finishOpen = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Repairorder">
import { listRepairorder, getRepairorder, delRepairorder, addRepairorder, updateRepairorder, acceptRepairorder, finishRepairorder } from "@/api/repairorder/repairorder"
import { listDevice } from "@/api/device/device"
import { listPart } from "@/api/device/part"

const { proxy } = getCurrentInstance()
const { order_status, fault_type, repair_type, repair_result } = useDict('order_status', 'fault_type', 'repair_type', 'repair_result')

const repairorderList = ref([])
const deviceOptions = ref([])
const partOptions = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    orderNo: undefined,
    deviceId: undefined,
    faultType: undefined,
    faultDesc: undefined,
    reportUserId: undefined,
    reportTime: undefined,
    status: undefined,
    handlerUserId: undefined,
    handleTime: undefined,
    handleResult: undefined,
    finishTime: undefined,
  },
  rules: {
    orderNo: [
      { required: true, message: "工单编号不能为空", trigger: "blur" }
    ],
    deviceId: [
      { required: true, message: "报修设备 不能为空", trigger: "change" }
    ],
    faultDesc: [
      { required: true, message: "故障描述不能为空", trigger: "blur" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 办结弹窗 */
const finishOpen = ref(false)
const finishLoading = ref(false)
const finishInfo = ref({})
const finishRef = ref(null)

const finishForm = ref({})

const finishRules = {
  repairType: [{ required: true, message: "请选择维修类型", trigger: "change" }],
  result: [{ required: true, message: "请选择维修结果", trigger: "change" }],
  handleDesc: [{ required: true, message: "请填写处理过程", trigger: "blur" }]
}

/** 加载备件下拉数据 */
function getPartOptions() {
  listPart({ pageNum: 1, pageSize: 1000 }).then(response => {
    partOptions.value = response.rows || []
  })
}

/** 加载设备台账下拉数据（报修设备选择、搜索共用） */
function getDeviceOptions() {
  listDevice({ pageNum: 1, pageSize: 1000 }).then(response => {
    deviceOptions.value = response.rows || []
  })
}

/** 查询报修工单列表 */
function getList() {
  loading.value = true
  listRepairorder(queryParams.value).then(response => {
    repairorderList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

/** 取消按钮 */
function cancel() {
  open.value = false
  reset()
}

/** 表单重置 */
function reset() {
  form.value = {
    orderId: null,
    orderNo: null,
    deviceId: null,
    faultType: null,
    faultDesc: null,
    reportUserId: null,
    reportTime: null,
    status: null,
    handlerUserId: null,
    handleTime: null,
    handleResult: null,
    finishTime: null,
    remark: null,
    delFlag: null,
    createBy: null,
    createTime: null,
    updateBy: null,
    updateTime: null
  }
  proxy.resetForm("repairorderRef")
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef")
  handleQuery()
}

/** 多选框选中数据 */
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.orderId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加报修工单"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _orderId = row.orderId || ids.value
  getRepairorder(_orderId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改报修工单"
  })
}

/** 受理工单：待受理 -> 处理中，处理人记成当前登录用户 */
function handleAccept(row) {
  proxy.$modal.confirm('确认受理工单「' + row.orderNo + '」吗？受理后处理人将记录为当前登录用户，状态变为处理中。').then(function() {
    return acceptRepairorder(row.orderId)
  }).then(() => {
    proxy.$modal.msgSuccess("受理成功")
    getList()
  }).catch(() => {})
}

/** 办结工单：处理中 -> 已完成，同时生成维修记录、扣减备件库存 */
function handleFinish(row) {
  finishInfo.value = row
  finishForm.value = {
    repairType: "repair",
    result: "repaired",
    faultCause: "",
    handleDesc: "",
    partId: null,
    partQty: 0,
    cost: 0,
    durationMin: 30
  }
  finishOpen.value = true
  if (partOptions.value.length === 0) {
    getPartOptions()
  }
}

/** 提交办结 */
function submitFinish() {
  proxy.$refs["finishRef"].validate(valid => {
    if (!valid) return
    finishLoading.value = true
    const payload = { ...finishForm.value }
    if (payload.partId == null || payload.partQty <= 0) {
      delete payload.partId
      delete payload.partQty
    }
    finishRepairorder(finishInfo.value.orderId, payload).then(() => {
      proxy.$modal.msgSuccess("办结成功，已生成维修记录")
      finishOpen.value = false
      finishLoading.value = false
      getList()
    }).catch(() => {
      finishLoading.value = false
    })
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["repairorderRef"].validate(valid => {
    if (valid) {
      if (form.value.orderId != null) {
        updateRepairorder(form.value).then(() => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addRepairorder(form.value).then(() => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _orderIds = row.orderId || ids.value
  proxy.$modal.confirm('是否确认删除报修工单编号为"' + _orderIds + '"的数据项？').then(function() {
    return delRepairorder(_orderIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('repairorder/repairorder/export', {
    ...queryParams.value
  }, `repairorder_${new Date().getTime()}.xlsx`)
}

getList()
getDeviceOptions()
</script>
