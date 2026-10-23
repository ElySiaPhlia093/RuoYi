<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="设备编号" prop="deviceCode">
        <el-input
          v-model="queryParams.deviceCode"
          placeholder="请输入设备编号"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="设备名称" prop="deviceName">
        <el-input
          v-model="queryParams.deviceName"
          placeholder="请输入设备名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="设备型号" prop="model">
        <el-input
          v-model="queryParams.model"
          placeholder="请输入设备型号"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="生产厂商" prop="manufacturer">
        <el-input
          v-model="queryParams.manufacturer"
          placeholder="请输入生产厂商"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="安装位置" prop="location">
        <el-input
          v-model="queryParams.location"
          placeholder="请输入安装位置"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="设备状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择设备状态" clearable>
          <el-option
            v-for="dict in device_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="采购日期" prop="purchaseDate">
        <el-date-picker clearable
          v-model="queryParams.purchaseDate"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择采购日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="保修到期日" prop="warrantyExpire">
        <el-date-picker clearable
          v-model="queryParams.warrantyExpire"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择保修到期日">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="负责人" prop="ownerUserId">
        <el-input
          v-model="queryParams.ownerUserId"
          placeholder="请输入负责人"
          clearable
          @keyup.enter="handleQuery"
        />
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
          v-hasPermi="['device:device:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['device:device:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['device:device:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['device:device:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="Upload"
          @click="handleImport"
          v-hasPermi="['device:device:import']"
        >导入</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="deviceList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键" align="center" prop="deviceId" />
      <el-table-column label="设备编号" align="center" prop="deviceCode" />
      <el-table-column label="设备名称" align="center" prop="deviceName" />
      <el-table-column label="设备类型" align="center" prop="deviceType" >
        <template #default="scope">
          <dict-tag :options="device_type" :value="scope.row.deviceType"/>
        </template>
      </el-table-column>
        <el-table-column label="设备型号" align="center" prop="model" />
      <el-table-column label="生产厂商" align="center" prop="manufacturer" />
      <el-table-column label="安装位置" align="center" prop="location" />
      <el-table-column label="设备状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :options="device_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="采购日期" align="center" prop="purchaseDate" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.purchaseDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="保修到期日" align="center" prop="warrantyExpire" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.warrantyExpire, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="负责人ID" align="center" prop="ownerUserId" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['device:device:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['device:device:remove']">删除</el-button>
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

    <!-- 添加或修改设备台账对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="deviceRef" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="设备编号" prop="deviceCode">
              <el-input v-model="form.deviceCode" placeholder="请输入设备编号" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="设备名称" prop="deviceName">
              <el-input v-model="form.deviceName" placeholder="请输入设备名称" />
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="设备类型" prop="deviceType">
              <el-select v-model="form.deviceType" placeholder="请输入设备类型" >
              <el-option
               v-for = "d in device_type"
               :key = "d.key"
               :label = "d.label"
               :value = "d.value"
               />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="设备型号" prop="model">
              <el-input v-model="form.model" placeholder="请输入设备型号" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="生产厂商" prop="manufacturer">
              <el-input v-model="form.manufacturer" placeholder="请输入生产厂商" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="安装位置" prop="location">
              <el-input v-model="form.location" placeholder="请输入安装位置" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="设备状态" prop="status">
              <el-select v-model="form.status">
                <el-option
                  v-for="dict in device_status"
                  :key="dict.value"
                  :label="dict.value"
                >{{dict.label}}</el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="采购日期" prop="purchaseDate">
              <el-date-picker clearable
                v-model="form.purchaseDate"
                type="date"
                value-format="YYYY-MM-DD"
                placeholder="请选择采购日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="保修到期日" prop="warrantyExpire">
              <el-date-picker clearable
                v-model="form.warrantyExpire"
                type="date"
                value-format="YYYY-MM-DD"
                placeholder="请选择保修到期日">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="负责人ID" prop="ownerUserId">
              <el-input v-model="form.ownerUserId" placeholder="请输入负责人用户ID（sys_user）" />
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

    <!-- 设备台账导入 -->
    <el-dialog :title="upload.title" v-model="upload.open" width="420px" append-to-body>
      <el-upload
        ref="uploadRef"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <template #tip>
          <div class="el-upload__tip text-center">
            <div class="el-upload__tip">
              <el-checkbox v-model="upload.updateSupport" />
              是否更新已存在的设备数据
            </div>
            <span>仅允许导入 xls、xlsx 格式文件。</span>
            <el-link type="primary" :underline="false" style="font-size: 12px; vertical-align: baseline" @click="importTemplate">
              下载模板
            </el-link>
          </div>
        </template>
      </el-upload>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitFileForm">确 定</el-button>
          <el-button @click="upload.open = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Device">
import { listDevice, getDevice, delDevice, addDevice, updateDevice } from "@/api/device/device"
import { getToken } from "@/utils/auth"

/** 导入参数 */
const upload = reactive({
  open: false,
  title: "设备数据导入",
  isUploading: false,
  updateSupport: false,
  headers: { Authorization: "Bearer " + getToken() },
  url: import.meta.env.VITE_APP_BASE_API + "/device/device/importData"
})

const { proxy } = getCurrentInstance()
const { device_status } = useDict('device_status')
const { device_type } = proxy.useDict("device_type")
const deviceList = ref([])
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
    deviceCode: undefined,
    deviceName: undefined,
    deviceType: undefined,
    model: undefined,
    manufacturer: undefined,
    location: undefined,
    status: undefined,
    purchaseDate: undefined,
    warrantyExpire: undefined,
    ownerUserId: undefined,
  },
  rules: {
    deviceCode: [
      { required: true, message: "设备编号不能为空", trigger: "blur" }
    ],
    deviceName: [
      { required: true, message: "设备名称不能为空", trigger: "blur" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询设备台账列表 */
function getList() {
  loading.value = true
  listDevice(queryParams.value).then(response => {
    deviceList.value = response.rows
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
    deviceId: null,
    deviceCode: null,
    deviceName: null,
    deviceType: null,
    model: null,
    manufacturer: null,
    location: null,
    status: null,
    purchaseDate: null,
    warrantyExpire: null,
    ownerUserId: null,
    remark: null,
    delFlag: null,
    createBy: null,
    createTime: null,
    updateBy: null,
    updateTime: null
  }
  proxy.resetForm("deviceRef")
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
  ids.value = selection.map(item => item.deviceId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加设备台账"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _deviceId = row.deviceId || ids.value
  getDevice(_deviceId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改设备台账"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["deviceRef"].validate(valid => {
    if (valid) {
      if (form.value.deviceId != null) {
        updateDevice(form.value).then(() => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addDevice(form.value).then(() => {
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
  const _deviceIds = row.deviceId || ids.value
  proxy.$modal.confirm('是否确认删除设备台账编号为"' + _deviceIds + '"的数据项？').then(function() {
    return delDevice(_deviceIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('device/device/export', {
    ...queryParams.value
  }, `device_${new Date().getTime()}.xlsx`)
}

/** 导入按钮操作 */
function handleImport() {
  upload.open = true
  upload.isUploading = false
  upload.updateSupport = false
}

/** 文件上传中处理 */
function handleFileUploadProgress(event, file, fileList) {
  upload.isUploading = true
}

/** 文件上传成功处理 */
function handleFileSuccess(response, file, fileList) {
  upload.open = false
  upload.isUploading = false
  proxy.$refs["uploadRef"].handleRemove(file)
  proxy.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true })
  getList()
}

/** 提交上传文件 */
function submitFileForm() {
  proxy.$refs["uploadRef"].submit()
}

/** 下载导入模板 */
function importTemplate() {
  proxy.download('device/device/importTemplate', {}, `device_template_${new Date().getTime()}.xlsx`)
}

getList()
</script>
