<template>
  <div class="dash-page">
    <!-- 看板头部：后续要加背景板，直接替换 .dash-hero 的 background 即可 -->
    <div class="dash-hero">
      <div class="hero-bg"></div>
      <div class="hero-content">
        <div class="hero-left">
          <div class="hero-title">设备运维数据看板</div>
          <div class="hero-sub">工业互联网设备台账 · 巡检 · 报修一体化平台</div>
          <div class="hero-tags">
            <span>实时统计</span>
            <span>近 30 天趋势</span>
            <span>数据源：MySQL 业务库</span>
          </div>
        </div>
        <div class="hero-right">
          <div class="hero-clock">{{ clock }}</div>
          <div class="hero-date">{{ today }}</div>
        </div>
      </div>
    </div>

    <!-- 指标卡 -->
    <div class="kpi-row">
      <div class="kpi-card" v-for="item in kpis" :key="item.label">
        <div class="kpi-icon" :style="{ background: item.bg, color: item.color }">
          <el-icon><component :is="item.icon" /></el-icon>
        </div>
        <div class="kpi-body">
          <div class="kpi-value">{{ item.value }}<span class="kpi-unit">{{ item.unit }}</span></div>
          <div class="kpi-label">{{ item.label }}</div>
        </div>
      </div>
    </div>

    <!-- 图表区 -->
    <div class="chart-grid">
      <div class="chart-card">
        <div class="chart-head">
          <span class="chart-title">设备状态分布</span>
          <span class="chart-tip">共 {{ summary.deviceTotal || 0 }} 台</span>
        </div>
        <div ref="statusRef" class="chart-body"></div>
      </div>

      <div class="chart-card">
        <div class="chart-head">
          <span class="chart-title">近 30 天报修趋势</span>
          <span class="chart-tip">单位：单</span>
        </div>
        <div ref="orderTrendRef" class="chart-body"></div>
      </div>

      <div class="chart-card">
        <div class="chart-head">
          <span class="chart-title">故障类型分布</span>
          <span class="chart-tip">共 {{ summary.orderTotal || 0 }} 张工单</span>
        </div>
        <div ref="faultRef" class="chart-body"></div>
      </div>

      <div class="chart-card">
        <div class="chart-head">
          <span class="chart-title">设备类型分布</span>
          <span class="chart-tip">按类型统计</span>
        </div>
        <div ref="typeRef" class="chart-body"></div>
      </div>

      <div class="chart-card">
        <div class="chart-head">
          <span class="chart-title">近 30 天巡检趋势</span>
          <span class="chart-tip">正常 / 异常</span>
        </div>
        <div ref="inspectRef" class="chart-body"></div>
      </div>

      <div class="chart-card">
        <div class="chart-head">
          <span class="chart-title">维修费用 TOP 5 设备</span>
          <span class="chart-tip">累计 {{ summary.totalCost || 0 }} 元</span>
        </div>
        <div ref="costRef" class="chart-body"></div>
      </div>
    </div>
  </div>
</template>

<script setup name="Report">
import * as echarts from 'echarts'
import { getSummary, getDeviceStatus, getDeviceType, getOrderTrend, getFaultType, getCostTop, getInspectTrend } from '@/api/report/dashboard'

const { proxy } = getCurrentInstance()

const summary = ref({})
const kpis = ref([])
const clock = ref('')
const today = ref('')

const statusRef = ref(null)
const typeRef = ref(null)
const orderTrendRef = ref(null)
const faultRef = ref(null)
const costRef = ref(null)
const inspectRef = ref(null)

let charts = []
let timer = null

const PALETTE = ['#3b82f6', '#22c55e', '#f59e0b', '#ef4444', '#8b5cf6', '#06b6d4', '#ec4899', '#84cc16']

function initChart(el) {
  if (!el) return null
  const chart = echarts.init(el)
  charts.push(chart)
  return chart
}

function buildKpis(s) {
  kpis.value = [
    { label: '设备总数', value: s.deviceTotal || 0, unit: ' 台', icon: 'Monitor', color: '#3b82f6', bg: 'rgba(59,130,246,.12)' },
    { label: '运行中', value: s.runningCount || 0, unit: ' 台', icon: 'CircleCheck', color: '#22c55e', bg: 'rgba(34,197,94,.12)' },
    { label: '待受理工单', value: s.pendingCount || 0, unit: ' 单', icon: 'Bell', color: '#f59e0b', bg: 'rgba(245,158,11,.12)' },
    { label: '处理中工单', value: s.processingCount || 0, unit: ' 单', icon: 'Tools', color: '#8b5cf6', bg: 'rgba(139,92,246,.12)' },
    { label: '本月维修费用', value: '¥' + formatMoney(s.monthCost), unit: '', icon: 'Money', color: '#ef4444', bg: 'rgba(239,68,68,.12)' },
    { label: '巡检合格率', value: s.inspectPassRate || 0, unit: ' %', icon: 'DataAnalysis', color: '#06b6d4', bg: 'rgba(6,182,212,.12)' }
  ]
}

function formatMoney(v) {
  const n = Number(v || 0)
  return n.toLocaleString('zh-CN')
}

/** 设备状态：环形图 */
function renderStatus(data) {
  const chart = initChart(statusRef.value)
  if (!chart) return
  chart.setOption({
    color: PALETTE,
    tooltip: { trigger: 'item', formatter: '{b}<br/>{c} 台（{d}%）' },
    legend: { bottom: 0, itemWidth: 10, itemHeight: 10, textStyle: { color: '#606266' } },
    series: [{
      type: 'pie',
      radius: ['45%', '68%'],
      center: ['50%', '45%'],
      avoidLabelOverlap: true,
      itemStyle: { borderRadius: 6, borderColor: '#fff', borderWidth: 2 },
      label: { show: true, formatter: '{b}\n{c} 台' },
      data: (data || []).map(d => ({ name: d.name, value: Number(d.value) }))
    }]
  })
}

/** 工单趋势：面积折线 */
function renderOrderTrend(data) {
  const chart = initChart(orderTrendRef.value)
  if (!chart) return
  const names = (data || []).map(d => d.name)
  const values = (data || []).map(d => Number(d.value))
  chart.setOption({
    grid: { left: 40, right: 20, top: 30, bottom: 30 },
    tooltip: { trigger: 'axis' },
    xAxis: {
      type: 'category',
      data: names,
      boundaryGap: false,
      axisLine: { lineStyle: { color: '#dcdfe6' } },
      axisLabel: { color: '#909399', interval: Math.max(0, Math.ceil(names.length / 8) - 1) }
    },
    yAxis: { type: 'value', splitLine: { lineStyle: { color: '#f0f2f5' } }, axisLabel: { color: '#909399' } },
    series: [{
      type: 'line',
      smooth: true,
      symbol: 'circle',
      symbolSize: 5,
      data: values,
      lineStyle: { width: 2.5, color: '#3b82f6' },
      itemStyle: { color: '#3b82f6' },
      areaStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: 'rgba(59,130,246,.35)' },
          { offset: 1, color: 'rgba(59,130,246,.02)' }
        ])
      }
    }]
  })
}

/** 故障类型：玫瑰图 */
function renderFault(data) {
  const chart = initChart(faultRef.value)
  if (!chart) return
  chart.setOption({
    color: ['#ef4444', '#f59e0b', '#3b82f6', '#8b5cf6', '#06b6d4', '#22c55e'],
    tooltip: { trigger: 'item', formatter: '{b}<br/>{c} 单（{d}%）' },
    legend: { bottom: 0, itemWidth: 10, itemHeight: 10 },
    series: [{
      type: 'pie',
      radius: ['20%', '70%'],
      center: ['50%', '45%'],
      roseType: 'radius',
      itemStyle: { borderRadius: 5 },
      label: { formatter: '{b} {c}' },
      data: (data || []).map(d => ({ name: d.name, value: Number(d.value) }))
    }]
  })
}

/** 设备类型：柱状图 */
function renderType(data) {
  const chart = initChart(typeRef.value)
  if (!chart) return
  const names = (data || []).map(d => d.name)
  const values = (data || []).map(d => Number(d.value))
  chart.setOption({
    grid: { left: 40, right: 20, top: 30, bottom: 40 },
    tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
    xAxis: {
      type: 'category',
      data: names,
      axisLabel: { color: '#606266', interval: 0, rotate: names.join('').length > 20 ? 20 : 0 },
      axisLine: { lineStyle: { color: '#dcdfe6' } }
    },
    yAxis: { type: 'value', splitLine: { lineStyle: { color: '#f0f2f5' } }, axisLabel: { color: '#909399' } },
    series: [{
      type: 'bar',
      barWidth: 18,
      data: values,
      itemStyle: {
        borderRadius: [6, 6, 0, 0],
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: '#60a5fa' },
          { offset: 1, color: '#2563eb' }
        ])
      }
    }]
  })
}

/** 巡检趋势：双折线 */
function renderInspect(data) {
  const chart = initChart(inspectRef.value)
  if (!chart) return
  const names = (data || []).map(d => d.name)
  const normal = (data || []).map(d => Number(d.normal))
  const abnormal = (data || []).map(d => Number(d.abnormal))
  chart.setOption({
    grid: { left: 40, right: 20, top: 40, bottom: 30 },
    tooltip: { trigger: 'axis' },
    legend: { top: 0, itemWidth: 12, itemHeight: 8, textStyle: { color: '#606266' } },
    xAxis: {
      type: 'category',
      data: names,
      boundaryGap: false,
      axisLabel: { color: '#909399', interval: Math.max(0, Math.ceil(names.length / 8) - 1) },
      axisLine: { lineStyle: { color: '#dcdfe6' } }
    },
    yAxis: { type: 'value', splitLine: { lineStyle: { color: '#f0f2f5' } }, axisLabel: { color: '#909399' } },
    series: [
      { name: '正常', type: 'line', smooth: true, symbol: 'none', data: normal, lineStyle: { width: 2.5, color: '#22c55e' }, itemStyle: { color: '#22c55e' } },
      { name: '异常', type: 'line', smooth: true, symbol: 'none', data: abnormal, lineStyle: { width: 2.5, color: '#ef4444' }, itemStyle: { color: '#ef4444' } }
    ]
  })
}

/** 维修费用：横向柱状 */
function renderCost(data) {
  const chart = initChart(costRef.value)
  if (!chart) return
  const names = (data || []).map(d => d.name)
  const values = (data || []).map(d => Number(d.value))
  chart.setOption({
    grid: { left: 120, right: 40, top: 20, bottom: 20 },
    tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' }, formatter: p => p[0].name + '<br/>维修费用：' + formatMoney(p[0].value) + ' 元' },
    xAxis: { type: 'value', splitLine: { lineStyle: { color: '#f0f2f5' } }, axisLabel: { color: '#909399' } },
    yAxis: {
      type: 'category',
      data: names,
      inverse: true,
      axisLabel: { color: '#606266', width: 110, overflow: 'truncate' },
      axisLine: { lineStyle: { color: '#dcdfe6' } }
    },
    series: [{
      type: 'bar',
      barWidth: 14,
      data: values,
      itemStyle: {
        borderRadius: [0, 6, 6, 0],
        color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
          { offset: 0, color: '#f59e0b' },
          { offset: 1, color: '#ef4444' }
        ])
      },
      label: { show: true, position: 'right', color: '#606266', formatter: p => formatMoney(p.value) }
    }]
  })
}

function loadData() {
  getSummary().then(res => {
    summary.value = res.data || {}
    buildKpis(summary.value)
  })
  getDeviceStatus().then(res => renderStatus(res.data))
  getDeviceType().then(res => renderType(res.data))
  getOrderTrend(30).then(res => renderOrderTrend(res.data))
  getFaultType().then(res => renderFault(res.data))
  getCostTop(5).then(res => renderCost(res.data))
  getInspectTrend(30).then(res => renderInspect(res.data))
}

function tick() {
  const now = new Date()
  const pad = n => String(n).padStart(2, '0')
  clock.value = pad(now.getHours()) + ':' + pad(now.getMinutes()) + ':' + pad(now.getSeconds())
  today.value = now.getFullYear() + ' 年 ' + (now.getMonth() + 1) + ' 月 ' + now.getDate() + ' 日'
}

function resize() {
  charts.forEach(c => c.resize())
}

onMounted(() => {
  tick()
  timer = setInterval(tick, 1000)
  nextTick(() => {
    loadData()
    window.addEventListener('resize', resize)
  })
})

onUnmounted(() => {
  clearInterval(timer)
  window.removeEventListener('resize', resize)
  charts.forEach(c => c.dispose())
  charts = []
})
</script>

<style scoped>
.dash-page {
  padding: 16px;
  background: #f4f6fa;
  min-height: calc(100vh - 84px);
}

/* 头部：背景板预留，换一张图直接改 background-image */
.dash-hero {
  position: relative;
  overflow: hidden;
  border-radius: 14px;
  padding: 26px 30px;
  color: #fff;
  background: linear-gradient(120deg, #0f2027 0%, #203a43 55%, #2c5364 100%);
  box-shadow: 0 10px 26px rgba(15, 32, 39, .28);
}
.hero-bg {
  position: absolute;
  inset: 0;
  opacity: .16;
  background-image: linear-gradient(rgba(255, 255, 255, .5) 1px, transparent 1px),
    linear-gradient(90deg, rgba(255, 255, 255, .5) 1px, transparent 1px);
  background-size: 34px 34px;
  pointer-events: none;
}
.hero-content {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.hero-title { font-size: 26px; font-weight: 700; letter-spacing: 2px; }
.hero-sub { margin-top: 8px; font-size: 13px; color: rgba(255, 255, 255, .72); letter-spacing: 1px; }
.hero-tags { margin-top: 14px; display: flex; gap: 10px; }
.hero-tags span {
  font-size: 12px;
  padding: 3px 10px;
  border-radius: 20px;
  color: #9fe3ff;
  border: 1px solid rgba(159, 227, 255, .35);
  background: rgba(159, 227, 255, .08);
}
.hero-right { text-align: right; }
.hero-clock { font-size: 34px; font-weight: 600; font-family: Consolas, Monaco, monospace; letter-spacing: 2px; }
.hero-date { margin-top: 4px; font-size: 13px; color: rgba(255, 255, 255, .68); }

.kpi-row {
  margin-top: 16px;
  display: grid;
  grid-template-columns: repeat(6, minmax(0, 1fr));
  gap: 14px;
}
.kpi-card {
  background: #fff;
  border-radius: 12px;
  padding: 16px;
  display: flex;
  align-items: center;
  gap: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, .05);
  transition: transform .2s, box-shadow .2s;
}
.kpi-card:hover { transform: translateY(-3px); box-shadow: 0 8px 20px rgba(0, 0, 0, .1); }
.kpi-icon {
  width: 44px; height: 44px; border-radius: 12px;
  display: flex; align-items: center; justify-content: center;
  font-size: 22px;
}
.kpi-value { font-size: 22px; font-weight: 700; color: #303133; line-height: 1.2; }
.kpi-unit { font-size: 12px; font-weight: 400; color: #909399; }
.kpi-label { margin-top: 4px; font-size: 12px; color: #909399; }

.chart-grid {
  margin-top: 16px;
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 14px;
}
.chart-card {
  background: #fff;
  border-radius: 12px;
  padding: 14px 16px 6px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, .05);
}
.chart-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 6px;
}
.chart-title { font-size: 15px; font-weight: 600; color: #303133; position: relative; padding-left: 10px; }
.chart-title::before {
  content: ''; position: absolute; left: 0; top: 3px; bottom: 3px;
  width: 3px; border-radius: 2px; background: linear-gradient(#3b82f6, #06b6d4);
}
.chart-tip { font-size: 12px; color: #a8abb2; }
.chart-body { width: 100%; height: 280px; }

@media (max-width: 1280px) {
  .kpi-row { grid-template-columns: repeat(3, minmax(0, 1fr)); }
}
@media (max-width: 900px) {
  .chart-grid { grid-template-columns: 1fr; }
  .kpi-row { grid-template-columns: repeat(2, minmax(0, 1fr)); }
}
</style>
