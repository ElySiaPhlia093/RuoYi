<template>
  <div class="home-page">
    <!-- 背景层：原神风景背景图（public/home-bg.jpg） -->
    <div class="bg-layer"></div>
    <div class="bg-mask"></div>

    <div class="home-inner">
      <!-- 顶部横幅 -->
      <section class="hero">
        <div class="hero-left">
          <div class="hero-badge">工业互联网 · 设备运维</div>
          <h1 class="hero-title">设备运维管理系统</h1>
          <p class="hero-sub">台账 · 巡检 · 报修 · 维修 · 备件，一条数据链管到底</p>
          <div class="hero-tags">
            <span class="tag">{{ greeting }}，{{ nickName }}</span>
            <span class="tag">设备 {{ summary.deviceTotal || 0 }} 台</span>
            <span class="tag">运行 {{ summary.runningCount || 0 }} 台</span>
          </div>
        </div>
        <div class="hero-right">
          <div class="clock">{{ clockTime }}</div>
          <div class="date">{{ today }}</div>
        </div>
      </section>

      <!-- KPI -->
      <section class="kpi-row">
        <div class="kpi" v-for="item in kpiList" :key="item.label" :class="item.cls">
          <div class="kpi-icon"><el-icon :size="22"><component :is="item.icon" /></el-icon></div>
          <div class="kpi-body">
            <div class="kpi-value">{{ item.value }}</div>
            <div class="kpi-label">{{ item.label }}</div>
          </div>
        </div>
      </section>

      <!-- 快捷入口 -->
      <section class="panel">
        <div class="panel-head"><span class="bar"></span>快捷入口</div>
        <div class="quick-row">
          <div class="quick" v-for="q in quickLinks" :key="q.path" @click="go(q.path)">
            <el-icon :size="24"><component :is="q.icon" /></el-icon>
            <div class="quick-name">{{ q.name }}</div>
            <div class="quick-desc">{{ q.desc }}</div>
          </div>
        </div>
      </section>

      <!-- 待处理工单 + 库存预警 -->
      <section class="two-col">
        <div class="panel">
          <div class="panel-head">
            <span class="bar"></span>待受理工单
            <span class="more" @click="go('/repairorder')">查看全部 ›</span>
          </div>
          <el-table :data="pendingOrders" size="small" v-loading="orderLoading" style="width: 100%">
            <el-table-column label="工单编号" prop="orderNo" width="110" />
            <el-table-column label="设备" min-width="130" show-overflow-tooltip>
              <template #default="scope">{{ scope.row.deviceCode }} {{ scope.row.deviceName }}</template>
            </el-table-column>
            <el-table-column label="故障类型" prop="faultType" width="90">
              <template #default="scope">
                <dict-tag :options="fault_type" :value="scope.row.faultType" />
              </template>
            </el-table-column>
            <el-table-column label="报修时间" prop="reportTime" width="110" />
            <el-table-column label="状态" width="80" align="center">
              <template #default="scope">
                <el-tag size="small" type="warning">待受理</el-tag>
              </template>
            </el-table-column>
            <template #empty><span class="empty-text">暂无待受理工单，设备状态良好 👍</span></template>
          </el-table>
        </div>

        <div class="panel">
          <div class="panel-head">
            <span class="bar warn"></span>备件库存预警
            <span class="more" @click="go('/device/part')">去补货 ›</span>
          </div>
          <div class="warn-list" v-loading="partLoading">
            <div class="warn-item" v-for="p in warningParts" :key="p.partId">
              <div class="warn-name">
                <span class="code">{{ p.partCode }}</span>
                <span>{{ p.partName }}</span>
              </div>
              <div class="warn-num">
                <span class="stock">{{ p.stock }}</span>
                <span class="slash">/</span>
                <span class="safe">{{ p.safetyStock }}</span>
              </div>
            </div>
            <div v-if="!partLoading && warningParts.length === 0" class="empty-text">备件库存充足，无需补货</div>
          </div>
        </div>
      </section>

      <div class="foot">工业互联网设备运维管理系统 · 数据每 30 秒自动刷新</div>
    </div>
  </div>
</template>

<script setup name="Index">
import { getSummary } from '@/api/report/dashboard'
import { listRepairorder } from '@/api/repairorder/repairorder'
import { listPart } from '@/api/device/part'
import { useDict } from '@/utils/dict'
import useUserStore from '@/store/modules/user'

const { fault_type } = useDict('fault_type')
const userStore = useUserStore()
const router = useRouter()

const nickName = computed(() => userStore.nickName || '工程师')
const summary = ref({})
const pendingOrders = ref([])
const warningParts = ref([])
const orderLoading = ref(false)
const partLoading = ref(false)

const clockTime = ref('')
const today = ref('')
let clockTimer = null
let refreshTimer = null

const greeting = computed(() => {
  const h = new Date().getHours()
  if (h < 6) return '夜深了'
  if (h < 11) return '早上好'
  if (h < 14) return '中午好'
  if (h < 18) return '下午好'
  return '晚上好'
})

const kpiList = computed(() => [
  { label: '设备总数', value: `${summary.value.deviceTotal || 0} 台`, icon: 'Monitor', cls: 'c-blue' },
  { label: '运行中', value: `${summary.value.runningCount || 0} 台`, icon: 'CircleCheck', cls: 'c-green' },
  { label: '待受理工单', value: `${summary.value.pendingCount || 0} 单`, icon: 'Bell', cls: 'c-orange' },
  { label: '处理中工单', value: `${summary.value.processingCount || 0} 单`, icon: 'Tools', cls: 'c-purple' },
  { label: '本月维修费用', value: `¥${Number(summary.value.monthCost || 0).toLocaleString()}`, icon: 'Money', cls: 'c-red' },
  { label: '巡检合格率', value: `${summary.value.inspectPassRate || 0}%`, icon: 'DataLine', cls: 'c-cyan' }
])

const quickLinks = [
  { name: '数据看板', desc: '6 个维度可视化', icon: 'DataAnalysis', path: '/report' },
  { name: '设备台账', desc: '设备档案与导入', icon: 'Monitor', path: '/device/ledger' },
  { name: '设备巡检', desc: '日常/周检/专项', icon: 'View', path: '/device/inspect' },
  { name: '备件库存', desc: '库存与预警', icon: 'ShoppingCart', path: '/device/part' },
  { name: '报修工单', desc: '受理与办结', icon: 'Tickets', path: '/repairorder' },
  { name: '维修记录', desc: '费用与工时', icon: 'Document', path: '/repairrecord' }
]

/* ---------------- 数据 ---------------- */
function loadSummary() {
  getSummary().then(res => {
    summary.value = res.data || {}
  })
}

function loadPendingOrders() {
  orderLoading.value = true
  listRepairorder({ pageNum: 1, pageSize: 5, status: '0' }).then(res => {
    pendingOrders.value = res.rows || []
    orderLoading.value = false
  }).catch(() => { orderLoading.value = false })
}

function loadWarningParts() {
  partLoading.value = true
  listPart({ pageNum: 1, pageSize: 9999 }).then(res => {
    warningParts.value = (res.rows || [])
      .filter(p => Number(p.stock) <= Number(p.safetyStock))
      .sort((a, b) => a.stock - b.stock)
      .slice(0, 5)
    partLoading.value = false
  }).catch(() => { partLoading.value = false })
}

function go(path) {
  router.push(path)
}

function tick() {
  const d = new Date()
  const pad = n => String(n).padStart(2, '0')
  clockTime.value = `${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`
  const week = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'][d.getDay()]
  today.value = `${d.getFullYear()} 年 ${d.getMonth() + 1} 月 ${d.getDate()} 日 ${week}`
}

onMounted(() => {
  tick()
  clockTimer = setInterval(tick, 1000)
  loadSummary()
  loadPendingOrders()
  loadWarningParts()
  refreshTimer = setInterval(() => {
    loadSummary()
    loadPendingOrders()
    loadWarningParts()
  }, 30000)
})

onUnmounted(() => {
  if (clockTimer) clearInterval(clockTimer)
  if (refreshTimer) clearInterval(refreshTimer)
})
</script>

<style scoped>
.home-page {
  position: relative;
  min-height: calc(100vh - 84px);
  overflow: hidden;
  border-radius: 8px;
}
.bg-layer {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-image: url('/home-bg.jpg');
  background-size: cover;
  background-position: center center;
  background-repeat: no-repeat;
  background-color: #0b2233;
  z-index: 0;
}
.bg-mask {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(180deg, rgba(6, 14, 26, 0.62) 0%, rgba(8, 18, 34, 0.34) 46%, rgba(4, 10, 20, 0.72) 100%);
  z-index: 1;
}
.home-inner {
  position: relative;
  z-index: 2;
  padding: 18px;
}

/* 顶部横幅 */
.hero {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  color: #eaf4ff;
  padding: 8px 6px 22px;
}
.hero-badge {
  display: inline-block;
  font-size: 12px;
  letter-spacing: 2px;
  padding: 3px 10px;
  border: 1px solid rgba(120, 200, 255, 0.5);
  border-radius: 12px;
  color: #8fd3ff;
  background: rgba(30, 120, 180, 0.18);
}
.hero-title {
  margin: 12px 0 6px;
  font-size: 30px;
  letter-spacing: 2px;
  font-weight: 700;
  text-shadow: 0 2px 12px rgba(0, 0, 0, 0.35);
}
.hero-sub {
  margin: 0 0 12px;
  font-size: 13px;
  color: rgba(220, 238, 255, 0.75);
  letter-spacing: 1px;
}
.hero-tags .tag {
  display: inline-block;
  margin-right: 8px;
  font-size: 12px;
  padding: 3px 10px;
  border-radius: 10px;
  background: rgba(255, 255, 255, 0.12);
  color: #d6ecff;
}
.hero-right {
  text-align: right;
}
.clock {
  font-size: 34px;
  font-weight: 700;
  letter-spacing: 3px;
  font-variant-numeric: tabular-nums;
  text-shadow: 0 2px 16px rgba(0, 180, 255, 0.35);
}
.date {
  font-size: 12px;
  color: rgba(210, 232, 255, 0.75);
  margin-top: 2px;
}
/* KPI */
.kpi-row {
  display: grid;
  grid-template-columns: repeat(6, minmax(0, 1fr));
  gap: 12px;
  margin-bottom: 14px;
}
.kpi {
  display: flex;
  align-items: center;
  gap: 10px;
  background: rgba(255, 255, 255, 0.12);
  border: 1px solid rgba(255, 255, 255, 0.22);
  border-radius: 10px;
  padding: 14px 12px;
  box-shadow: 0 6px 18px rgba(0, 10, 25, 0.25);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
}
.kpi-icon {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  flex-shrink: 0;
}
.c-blue .kpi-icon { background: linear-gradient(135deg, #409eff, #2b7cd3); }
.c-green .kpi-icon { background: linear-gradient(135deg, #67c23a, #4a9e2b); }
.c-orange .kpi-icon { background: linear-gradient(135deg, #e6a23c, #cf8a1f); }
.c-purple .kpi-icon { background: linear-gradient(135deg, #9254de, #7b3fc4); }
.c-red .kpi-icon { background: linear-gradient(135deg, #f56c6c, #d9534f); }
.c-cyan .kpi-icon { background: linear-gradient(135deg, #17c0c8, #0f9aa1); }
.kpi-value {
  font-size: 20px;
  font-weight: 700;
  color: #f4f9ff;
  line-height: 1.2;
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.35);
}
.kpi-label {
  font-size: 12px;
  color: rgba(220, 238, 255, 0.75);
  margin-top: 2px;
}

/* 面板 */
.panel {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.18);
  border-radius: 10px;
  padding: 14px 16px 16px;
  margin-bottom: 14px;
  box-shadow: 0 8px 22px rgba(0, 10, 25, 0.25);
  backdrop-filter: blur(14px);
  -webkit-backdrop-filter: blur(14px);
}
.panel-head {
  display: flex;
  align-items: center;
  font-size: 15px;
  font-weight: 600;
  color: #eef6ff;
  margin-bottom: 12px;
}
.panel-head .bar {
  width: 4px;
  height: 15px;
  border-radius: 2px;
  background: #409eff;
  margin-right: 8px;
}
.panel-head .bar.warn { background: #e6a23c; }
.panel-head .more {
  margin-left: auto;
  font-size: 12px;
  font-weight: normal;
  color: #8fd3ff;
  cursor: pointer;
}
.panel-head .more:hover { text-decoration: underline; }

.quick-row {
  display: grid;
  grid-template-columns: repeat(6, minmax(0, 1fr));
  gap: 12px;
}
.quick {
  border: 1px solid rgba(255, 255, 255, 0.18);
  border-radius: 10px;
  padding: 14px 10px;
  text-align: center;
  cursor: pointer;
  transition: all 0.2s;
  color: #9fd6ff;
  background: rgba(255, 255, 255, 0.07);
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
}
.quick:hover {
  transform: translateY(-3px);
  border-color: rgba(143, 211, 255, 0.55);
  background: rgba(255, 255, 255, 0.16);
  box-shadow: 0 8px 18px rgba(64, 158, 255, 0.25);
}
.quick-name {
  margin-top: 8px;
  font-size: 14px;
  font-weight: 600;
  color: #f1f8ff;
}
.quick-desc {
  font-size: 11px;
  color: rgba(210, 232, 255, 0.65);
  margin-top: 3px;
}

.two-col {
  display: grid;
  grid-template-columns: minmax(0, 1.55fr) minmax(0, 1fr);
  gap: 14px;
  align-items: start;
}
.two-col > .panel { min-width: 0; }
.warn-list { min-height: 60px; }
.warn-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 9px 12px;
  border-radius: 8px;
  background: rgba(12, 24, 38, 0.32);
  border: 1px solid rgba(230, 162, 60, 0.42);
  border-left: 3px solid rgba(230, 162, 60, 0.85);
  margin-bottom: 8px;
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
}
.warn-name { font-size: 13px; color: #f2f7ff; }
.warn-name .code {
  display: inline-block;
  min-width: 74px;
  color: #ffcf7a;
  font-family: Consolas, monospace;
}
.warn-num { font-size: 13px; }
.warn-num .stock { color: #f56c6c; font-weight: 700; font-size: 15px; }
.warn-num .slash { color: #c8d2dd; margin: 0 2px; }
.warn-num .safe { color: rgba(210, 232, 255, 0.65); }
.empty-text { color: rgba(210, 232, 255, 0.6); font-size: 13px; }


/* 面板内表格透明化，让背景图透出来 */
.panel :deep(.el-table) {
  --el-table-bg-color: transparent;
  --el-table-tr-bg-color: transparent;
  --el-table-header-bg-color: rgba(255, 255, 255, 0.06);
  --el-table-border-color: rgba(255, 255, 255, 0.12);
  --el-table-text-color: #e9f3ff;
  --el-table-header-text-color: #cfe6ff;
  --el-table-row-hover-bg-color: rgba(255, 255, 255, 0.09);
  --el-table-current-row-bg-color: rgba(255, 255, 255, 0.12);
  background-color: transparent;
}
.panel :deep(.el-table__inner-wrapper::before) { background-color: transparent; }
.panel :deep(.el-table__header-wrapper),
.panel :deep(.el-table__header),
.panel :deep(.el-table__body-wrapper) { background-color: transparent; }
.panel :deep(.el-table__header-wrapper th.el-table__cell) {
  background-color: rgba(255, 255, 255, 0.08) !important;
  border-bottom: 1px solid rgba(255, 255, 255, 0.14) !important;
  color: #d8ecff !important;
}
.panel :deep(.el-table__header-wrapper th.el-table__cell .cell) {
  color: #d8ecff !important;
  font-weight: 600;
}
.panel :deep(.el-table__body-wrapper td.el-table__cell .cell) { color: #e9f3ff; }
.panel :deep(.el-table tr),
.panel :deep(.el-table th.el-table__cell),
.panel :deep(.el-table td.el-table__cell) { background-color: transparent; }
.panel :deep(.el-table__empty-text) { color: rgba(210, 232, 255, 0.65); }
.panel :deep(.el-loading-mask) { background-color: rgba(10, 22, 36, 0.35); }
.panel :deep(.el-table__body tr:hover > td.el-table__cell) { background-color: rgba(255, 255, 255, 0.09); }

.foot {
  text-align: center;
  color: rgba(220, 238, 255, 0.5);
  font-size: 12px;
  padding: 6px 0 10px;
}

@media (max-width: 1280px) {
  .kpi-row { grid-template-columns: repeat(3, minmax(0, 1fr)); }
  .quick-row { grid-template-columns: repeat(3, minmax(0, 1fr)); }
}
@media (max-width: 1100px) {
  .two-col { grid-template-columns: 1fr; }
  .hero { flex-direction: column; gap: 12px; }
  .hero-right { text-align: left; }
}
</style>
