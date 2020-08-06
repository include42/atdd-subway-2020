import ApiService from '@/api'

const BASE_URL = '/paths'

const PathService = {
  get(params) {
    return ApiService.getWithParams(`${BASE_URL}`, params)
  },
  getWithLogin(params) {
    return ApiService.getWithParams(`${BASE_URL}/login`, params)
  }
}

export default PathService
