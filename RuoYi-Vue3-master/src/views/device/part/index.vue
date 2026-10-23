<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="备件编号" prop="partCode">
        <el-input v-model="queryParams.partCode" placeholder="请输入备件编号" clearable style="width: 200px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="备件名称" prop="partName">
        <el-input v-model="queryParams.partName" placeholder="请输入备件名称" clearable style="width: 200px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="供应商" prop="supplier">
        <el-input v-model="queryParams.supplier" placeholder="请输入供应商" clearable style="width: 200px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['device:part:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate" v-hasPermi="['device:part:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['device:part:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['device:part:export']">导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-alert
      v-if="warningCount > 0"
      :title="'库存预警：有 ' + warningCount + ' 种备件库存已低于安全库存，请及时采购补充'"
      type="warning"
      :closable="false"
      show-icon
      style="margin-bottom: 12px"
    />

    <el-table v-loading="loading" :data="partList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="备件编号" align="center" prop="partCode" width="110" />
      <el-table-column label="备件名称" align="center" prop="partName" show-overflow-tooltip />
      <el-table-column label="规格型号" align="center" prop="spec" show-overflow-tooltip />
      <el-table-column label="单位" align="center" prop="unit" width="70" />
      <el-table-column label="库存" align="center" prop="stock" width="110">
        <template #default="scope">
          <el-tag v-if="scope.row.stock <= scope.row.safetyStock" type="danger" effect="dark">
            {{ scope.row.stock }} 预警
          </el-tag>
          <span v-else>{{ scope.row.stock }}</span>
        </template>
      </el-table-column>
      <el-table-column label="安全库存" align="center" prop="safetyStock" width="100" />
      <el-table-column label="单价(元)" align="center" prop="unitPrice" width="110" />
      <el-table-column label="供应商" align="center" prop="supplier" show-overflow-tooltip />
      <el-table-column label="状态" align="center" prop="status" width="90">
        <template #default="scope">
          <el-tag :type="scope.row.status === '0' ? 'success' : 'info'">{{ scope.row.status === '0' ? '正常' : '停用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['device:part:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['device:part:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form ref="partRef" :model="form" :rules="rules" label-width="90px">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="备件编号" prop="partCode">
              <el-select
                v-model="form.partCode"
                filterable
                allow-create
                default-first-option
                clearable
                placeholder="选择已有编号或输入新编号"
                style="width: 100%"
                @change="onPartCodeChange"
              >
                <el-option v-for="item in partCodeOptions" :key="item.partId" :label="item.partCode" :value="item.partCode">
                  <span class="opt-row"><span>{{ item.partCode }}</span><span class="opt-sub">{{ item.partName }}</span></span>
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="备件名称" prop="partName">
              <el-select
                v-model="form.partName"
                filterable
                allow-create
                default-first-option
                clearable
                placeholder="选择已有名称或输入新名称"
                style="width: 100%"
                @change="onPartNameChange"
              >
                <el-option v-for="item in partNameOptions" :key="item.partId" :label="item.partName" :value="item.partName">
                  <span class="opt-row"><span>{{ item.partName }}</span><span class="opt-sub">{{ item.spec }}</span></span>
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="规格型号" prop="spec">
              <el-select
                v-model="form.spec"
                filterable
                allow-create
                default-first-option
                clearable
                placeholder="选择已有规格或输入新规格"
                style="width: 100%"
                @change="onSpecChange"
              >
                <el-option v-for="item in specOptions" :key="item.partId" :label="item.spec" :value="item.spec">
                  <span class="opt-row"><span>{{ item.spec }}</span><span class="opt-sub">{{ item.partName }}</span></span>
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="单位" prop="unit">
              <el-input v-model="form.unit" placeholder="个/台/条" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="库存数量" prop="stock">
              <el-input-number v-model="form.stock" :min="0" style="width: 100%" controls-position="right" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="安全库存" prop="safetyStock">
              <el-input-number v-model="form.safetyStock" :min="0" style="width: 100%" controls-position="right" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="单价(元)" prop="unitPrice">
              <el-input-number v-model="form.unitPrice" :min="0" :precision="2" style="width: 100%" controls-position="right" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="供应商" prop="supplier">
              <el-input v-model="form.supplier" placeholder="请输入供应商" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio value="0">正常</el-radio>
                <el-radio value="1">停用</el-radio>
              </el-radio-group>
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

<style scoped>
.opt-row { display: flex; justify-content: space-between; gap: 10px; align-items: center; }
.opt-sub { color: #8492a6; font-size: 13px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
</style>

<script setup name="Part">
import { listPart, getPart, delPart, addPart, updatePart } from '@/api/device/part'

const { proxy } = getCurrentInstance()

const partList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref('')
const warningCount = ref(0)
const partOptions = ref([])

/** 已有备件（用于编号/名称/规格下拉联想） */
const partCodeOptions = computed(() => partOptions.value)
const partNameOptions = computed(() => {
  const map = new Map()
  partOptions.value.forEach(p => { if (p.partName && !map.has(p.partName)) map.set(p.partName, p) })
  return [...map.values()]
})
const specOptions = computed(() => {
  const map = new Map()
  partOptions.value.forEach(p => { if (p.spec && !map.has(p.spec)) map.set(p.spec, p) })
  return [...map.values()]
})

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    partCode: undefined,
    partName: undefined,
    supplier: undefined
  },
  rules: {
    partCode: [{ required: true, message: '备件编号不能为空', trigger: 'blur' }],
    partName: [{ required: true, message: '备件名称不能为空', trigger: 'blur' }],
    stock: [{ required: true, message: '库存数量不能为空', trigger: 'blur' }],
    safetyStock: [{ required: true, message: '安全库存不能为空', trigger: 'blur' }]
  }
})

const { queryParams, form, rules } = toRefs(data)

function getList() {
  loading.value = true
  listPart(queryParams.value).then(res => {
    partList.value = res.rows
    total.value = res.total
    loading.value = false
  })
}

/** 统计全量低于安全库存的备件数量（不受分页影响） */
function loadWarningCount() {
  listPart({ pageNum: 1, pageSize: 9999 }).then(res => {
    warningCount.value = (res.rows || []).filter(r => Number(r.stock) <= Number(r.safetyStock)).length
  })
}

/** 加载全部备件，供编号/名称/规格下拉联想 */
function loadPartOptions() {
  listPart({ pageNum: 1, pageSize: 9999 }).then(res => {
    partOptions.value = res.rows || []
  })
}

/** 选中已有备件时，把名称/规格/单位/供应商/单价一起带出来 */
function applyPart(hit) {
  form.value.partCode = hit.partCode
  form.value.partName = hit.partName
  form.value.spec = hit.spec
  form.value.unit = hit.unit
  form.value.supplier = hit.supplier
  form.value.unitPrice = hit.unitPrice
}

function onPartCodeChange(val) {
  const hit = partOptions.value.find(p => p.partCode === val)
  if (hit && hit.partId !== form.value.partId) applyPart(hit)
}

function onPartNameChange(val) {
  const hit = partOptions.value.find(p => p.partName === val)
  if (hit && hit.partId !== form.value.partId) applyPart(hit)
}

function onSpecChange(val) {
  const hit = partOptions.value.find(p => p.spec === val)
  if (hit && hit.partId !== form.value.partId) applyPart(hit)
}

function cancel() {
  open.value = false
  reset()
}

function reset() {
  form.value = {
    partId: null,
    partCode: null,
    partName: null,
    spec: null,
    unit: '个',
    stock: 0,
    safetyStock: 0,
    unitPrice: 0,
    supplier: null,
    status: '0',
    remark: null
  }
  proxy.resetForm('partRef')
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
  ids.value = selection.map(item => item.partId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

function handleAdd() {
  reset()
  open.value = true
  title.value = '添加备件'
}

function handleUpdate(row) {
  reset()
  const _partId = row.partId || ids.value
  getPart(_partId).then(res => {
    form.value = res.data
    open.value = true
    title.value = '修改备件'
  })
}

function submitForm() {
  proxy.$refs['partRef'].validate(valid => {
    if (!valid) return
    if (form.value.partId == null) {
      const dup = partOptions.value.find(p => p.partCode === form.value.partCode)
      if (dup) {
        proxy.$modal.msgWarning('备件编号 ' + form.value.partCode + ' 已存在，请直接修改该备件或换一个新编号')
        return
      }
    }
    if (form.value.partId != null) {
      updatePart(form.value).then(() => {
        proxy.$modal.msgSuccess('修改成功')
        open.value = false
        getList()
        loadPartOptions()
      })
    } else {
      addPart(form.value).then(() => {
        proxy.$modal.msgSuccess('新增成功')
        open.value = false
        getList()
        loadPartOptions()
      })
    }
  })
}

function handleDelete(row) {
  const _partIds = row.partId || ids.value
  proxy.$modal.confirm('是否确认删除备件编号为"' + _partIds + '"的数据项？').then(() => {
    return delPart(_partIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess('删除成功')
  }).catch(() => {})
}

function handleExport() {
  proxy.download('device/part/export', {
    ...queryParams.value
  }, `part_${new Date().getTime()}.xlsx`)
}

getList()
loadWarningCount()
loadPartOptions()
</script>
