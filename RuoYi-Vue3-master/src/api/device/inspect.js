import request from '@/utils/request'

// 查询设备巡检列表
export function listInspect(query) {
  return request({
    url: '/device/inspect/list',
    method: 'get',
    params: query
  })
}

// 查询设备巡检详细
export function getInspect(inspectId) {
  return request({
    url: '/device/inspect/' + inspectId,
    method: 'get'
  })
}

// 新增设备巡检
export function addInspect(data) {
  return request({
    url: '/device/inspect',
    method: 'post',
    data: data
  })
}

// 修改设备巡检
export function updateInspect(data) {
  return request({
    url: '/device/inspect',
    method: 'put',
    data: data
  })
}

// 删除设备巡检
export function delInspect(inspectId) {
  return request({
    url: '/device/inspect/' + inspectId,
    method: 'delete'
  })
}
