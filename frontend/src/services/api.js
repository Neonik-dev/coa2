import axios from 'axios'

const API_SERVICE_1_BASE_URL = 'http://localhost:8081/api'

const apiService1 = axios.create({
    baseURL: API_SERVICE_1_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
})

const API_SERVICE_2_BASE_URL = 'http://localhost:8080/service2-1.0-SNAPSHOT/api'
const apiService2 = axios.create({
    baseURL: API_SERVICE_2_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
})

export const personsAPI = {
    create: (data) => apiService1.post('/persons', data),
    getAll: (params = {}) => apiService1.get('/persons', { params }),
}

export const studyGroupsAPI = {
    create: (data) => apiService1.post('/studygroups', data),
    getAll: (params = {}) => apiService1.get('/studygroups', { params }),
    getById: (id) => apiService1.get(`/studygroups/${id}`),
    update: (id, data) => apiService1.put(`/studygroups/${id}`, data),
    delete: (id) => apiService1.delete(`/studygroups/${id}`),
    getMinCreationDate: () => apiService1.get('/studygroups/min-creation-date'),
    getGroupById: () => apiService1.get('/studygroups/group-by-form-of-education'),
    getFormOfEducationLt: (value) => apiService1.get(`/studygroups/form-of-education/lt/${value}`),
}

export const isuAPI = {
    expelAll: (groupId) => apiService2.post(`/isu/group/${groupId}/expel-all`),
    changeEduForm: (groupId, newForm) => apiService2.post(`/isu/group/${groupId}/change-edu-form/${newForm}`),
}