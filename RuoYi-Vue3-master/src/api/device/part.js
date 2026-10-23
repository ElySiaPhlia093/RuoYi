import request from '@/utils/request'

// 查询备件库存列表
export function listPart(query) {
  return request({
    url: '/device/part/list',
    method: 'get',
    params: query
  })
}

// 查询备件库存详细
export function getPart(partId) {
  return request({
    url: '/device/part/' + partId,
    method: 'get'
  })
}

// 新增备件库存
export function addPart(data) {
  return request({
    url: '/device/part',
    method: 'post',
    data: data
  })
}

// 修改备件库存
export function updatePart(data) {
  return request({
    url: '/device/part',
    method: 'put',
    data: data
  })
}

// 删除备件库存
export function delPart(partId) {
  return request({
    url: '/device/part/' + partId,
    method: 'delete'
  })
}
