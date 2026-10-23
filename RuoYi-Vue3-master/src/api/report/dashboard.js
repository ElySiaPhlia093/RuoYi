import request from '@/utils/request'

// 看板概览指标
export function getSummary() {
  return request({
    url: '/report/dashboard/summary',
    method: 'get'
  })
}

// 设备状态分布
export function getDeviceStatus() {
  return request({
    url: '/report/dashboard/deviceStatus',
    method: 'get'
  })
}

// 设备类型分布
export function getDeviceType() {
  return request({
    url: '/report/dashboard/deviceType',
    method: 'get'
  })
}

// 报修工单趋势
export function getOrderTrend(days) {
  return request({
    url: '/report/dashboard/orderTrend',
    method: 'get',
    params: { days: days }
  })
}

// 故障类型分布
export function getFaultType() {
  return request({
    url: '/report/dashboard/faultType',
    method: 'get'
  })
}

// 维修费用 TOP
export function getCostTop(limit) {
  return request({
    url: '/report/dashboard/costTop',
    method: 'get',
    params: { limit: limit }
  })
}

// 巡检趋势
export function getInspectTrend(days) {
  return request({
    url: '/report/dashboard/inspectTrend',
    method: 'get',
    params: { days: days }
  })
}
