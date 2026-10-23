import request from '@/utils/request'

// 查询报修工单列表
export function listRepairorder(query) {
  return request({
    url: '/repairorder/repairorder/list',
    method: 'get',
    params: query
  })
}

// 查询报修工单详细
export function getRepairorder(orderId) {
  return request({
    url: '/repairorder/repairorder/' + orderId,
    method: 'get'
  })
}

// 新增报修工单
export function addRepairorder(data) {
  return request({
    url: '/repairorder/repairorder',
    method: 'post',
    data: data
  })
}

// 修改报修工单
export function updateRepairorder(data) {
  return request({
    url: '/repairorder/repairorder',
    method: 'put',
    data: data
  })
}

// 受理工单
export function acceptRepairorder(orderId) {
  return request({
    url: '/repairorder/repairorder/accept/' + orderId,
    method: 'put'
  })
}

// 办结工单（提交维修记录）
export function finishRepairorder(orderId, data) {
  return request({
    url: '/repairorder/repairorder/finish/' + orderId,
    method: 'put',
    data: data
  })
}

// 删除报修工单
export function delRepairorder(orderId) {
  return request({
    url: '/repairorder/repairorder/' + orderId,
    method: 'delete'
  })
}
