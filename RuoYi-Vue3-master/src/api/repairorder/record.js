import request from '@/utils/request'

// 查询维修记录列表
export function listRecord(query) {
  return request({
    url: '/repairorder/record/list',
    method: 'get',
    params: query
  })
}

// 查询维修记录详细
export function getRecord(recordId) {
  return request({
    url: '/repairorder/record/' + recordId,
    method: 'get'
  })
}

// 新增维修记录
export function addRecord(data) {
  return request({
    url: '/repairorder/record',
    method: 'post',
    data: data
  })
}

// 修改维修记录
export function updateRecord(data) {
  return request({
    url: '/repairorder/record',
    method: 'put',
    data: data
  })
}

// 删除维修记录
export function delRecord(recordId) {
  return request({
    url: '/repairorder/record/' + recordId,
    method: 'delete'
  })
}
