import React, { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { studyGroupsAPI } from '../services/api'
import { renderTable } from '../utils/tableUtils'

const StudyGroupsPage = () => {
    const navigate = useNavigate()
    const [studyGroups, setStudyGroups] = useState([])
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState('')
    const [pagination, setPagination] = useState({
        currentPage: 0,
        totalPages: 0,
        totalElements: 0
    })
    const [filters, setFilters] = useState({
        id: '',
        name: '',
        studentsCount: '',
        formOfEducation: '',
        semesterEnum: '',
        creationDate: '',
        adminName: '',
        nationality: '',
        sort: '',
        page: '0',
        size: '20'
    })

    const fetchStudyGroups = async (filterParams = filters) => {
        setLoading(true)
        setError('')
        try {
            const params = Object.fromEntries(
                Object.entries(filterParams)
                    .filter(([_, value]) => value !== '')
                    .map(([key, value]) => {
                        if (['id', 'studentsCount', 'page', 'size'].includes(key)) {
                            return [key, parseInt(value)]
                        }
                        return [key, value]
                    })
            )

            const response = await studyGroupsAPI.getAll(params)
            setStudyGroups(response.data.data || [])

            // Обновляем пагинацию из ответа
            setPagination({
                currentPage: parseInt(filterParams.page) || 0,
                totalPages: response.data.totalPages || 0,
                totalElements: response.data.totalElements || 0
            })
        } catch (err) {
            setError('Ошибка при загрузке данных')
            console.error(err)
        } finally {
            setLoading(false)
        }
    }

    const handleDelete = async (id) => {
        if (!window.confirm('Are you sure you want to delete this group?')) return

        try {
            await studyGroupsAPI.delete(id)
            fetchStudyGroups()
        } catch (err) {
            alert('Error deleting group')
            console.error(err)
        }
    }

    useEffect(() => {
        fetchStudyGroups()
    }, [])

    const handleFilterChange = (e) => {
        const { name, value } = e.target
        setFilters(prev => ({
            ...prev,
            [name]: value
        }))
    }

    const handleSubmit = (e) => {
        e.preventDefault()
        // Сбрасываем на первую страницу при применении новых фильтров
        setFilters(prev => ({ ...prev, page: '0' }))
        fetchStudyGroups({ ...filters, page: '0' })
    }

    const handleReset = () => {
        const resetFilters = {
            id: '',
            name: '',
            studentsCount: '',
            formOfEducation: '',
            semesterEnum: '',
            creationDate: '',
            adminName: '',
            nationality: '',
            sort: '',
            page: '0',
            size: '20'
        }
        setFilters(resetFilters)
        fetchStudyGroups(resetFilters)
    }

    const handlePageChange = (newPage) => {
        if (newPage >= 0 && newPage < pagination.totalPages) {
            setFilters(prev => ({ ...prev, page: newPage.toString() }))
            fetchStudyGroups({ ...filters, page: newPage.toString() })
        }
    }

    const handleSizeChange = (e) => {
        const newSize = e.target.value
        setFilters(prev => ({ ...prev, size: newSize, page: '0' }))
        fetchStudyGroups({ ...filters, size: newSize, page: '0' })
    }

    // Генерация номеров страниц для пагинации
    const getPageNumbers = () => {
        const pages = []
        const totalPages = pagination.totalPages
        const currentPage = pagination.currentPage

        if (totalPages <= 7) {
            // Показываем все страницы
            for (let i = 0; i < totalPages; i++) {
                pages.push(i)
            }
        } else {
            // Сложная логика для большого количества страниц
            if (currentPage <= 3) {
                // В начале
                for (let i = 0; i < 5; i++) pages.push(i)
                pages.push('...')
                pages.push(totalPages - 1)
            } else if (currentPage >= totalPages - 4) {
                // В конце
                pages.push(0)
                pages.push('...')
                for (let i = totalPages - 5; i < totalPages; i++) pages.push(i)
            } else {
                // В середине
                pages.push(0)
                pages.push('...')
                for (let i = currentPage - 1; i <= currentPage + 1; i++) pages.push(i)
                pages.push('...')
                pages.push(totalPages - 1)
            }
        }
        return pages
    }

    const columns = [
        {
            key: 'id',
            title: 'ID',
            dataIndex: 'id',
            align: 'center'
        },
        {
            key: 'name',
            title: 'Name',
            dataIndex: 'name',
            align: 'left'
        },
        {
            key: 'studentsCount',
            title: 'Students Count',
            dataIndex: 'studentsCount',
            align: 'center'
        },
        {
            key: 'formOfEducation',
            title: 'Form of Education',
            dataIndex: 'formOfEducation',
            align: 'center',
            render: (value) => (
                <span style={{
                    padding: '0.25rem 0.75rem',
                    borderRadius: '20px',
                    fontSize: '0.8rem',
                    fontWeight: '500',
                    backgroundColor:
                        value === 'DISTANCE_EDUCATION' ? '#e8f4fd' :
                            value === 'FULL_TIME_EDUCATION' ? '#e8f6f3' : '#fef9e7',
                    color:
                        value === 'DISTANCE_EDUCATION' ? '#3498db' :
                            value === 'FULL_TIME_EDUCATION' ? '#27ae60' : '#f39c12'
                }}>
                    {value === 'DISTANCE_EDUCATION' ? '📚 Distance' :
                        value === 'FULL_TIME_EDUCATION' ? '🎓 Full Time' : '🌙 Evening'}
                </span>
            )
        },
        {
            key: 'semesterEnum',
            title: 'Semester',
            dataIndex: 'semesterEnum',
            align: 'center'
        },
        {
            key: 'creationDate',
            title: 'Creation Date',
            dataIndex: 'creationDate',
            align: 'center',
            render: (date) => date ? new Date(date).toLocaleDateString() : '-'
        },
        {
            key: 'actions',
            title: 'Actions',
            dataIndex: 'id',
            align: 'center',
            render: (id, record) => (
                <div className="actions-cell">
                    <button
                        onClick={() => navigate(`/studygroups/${id}`)}
                        className="btn btn-outline"
                        style={{ padding: '0.4rem 0.8rem', fontSize: '0.8rem' }}
                    >
                        👁️ View
                    </button>
                    <button
                        onClick={() => navigate(`/update-studygroups/${id}`)}
                        className="btn btn-outline"
                        style={{ padding: '0.4rem 0.8rem', fontSize: '0.8rem' }}
                    >
                        ✏️ Edit
                    </button>
                    <button
                        onClick={() => handleDelete(id)}
                        className="btn btn-danger"
                        style={{ padding: '0.4rem 0.8rem', fontSize: '0.8rem' }}
                    >
                        🗑️ Delete
                    </button>
                </div>
            )
        },
    ]

    return (
        <div className="fade-in">
            <div className="card">
                <div className="card-header">
                    <h1>🎓 Study Groups Management</h1>
                    <p className="text-muted">View and manage all study groups in the system</p>
                </div>

                <div style={{ display: 'flex', gap: '1rem', marginBottom: '1.5rem', flexWrap: 'wrap' }}>
                    <button
                        onClick={() => navigate('/create-studygroups')}
                        className="btn btn-success"
                    >
                        🎓 Create New Group
                    </button>
                    <button
                        onClick={() => navigate('/')}
                        className="btn btn-secondary"
                    >
                        🏠 Back to Home
                    </button>
                </div>

                {/* Фильтры */}
                <div className="card" style={{ backgroundColor: '#f8f9fa', marginBottom: '2rem' }}>
                    <div className="card-header">
                        <h3>🔍 Filter and Sort</h3>
                        <p className="text-muted">Apply filters to find specific study groups</p>
                    </div>

                    <form onSubmit={handleSubmit}>
                        <div className="grid grid-3">
                            <div className="form-group">
                                <label className="form-label">ID</label>
                                <input
                                    type="number"
                                    name="id"
                                    value={filters.id}
                                    onChange={handleFilterChange}
                                    className="form-input"
                                    min="1"
                                    placeholder="Exact match"
                                />
                            </div>

                            <div className="form-group">
                                <label className="form-label">Name</label>
                                <input
                                    type="text"
                                    name="name"
                                    value={filters.name}
                                    onChange={handleFilterChange}
                                    className="form-input"
                                    placeholder="Exact match"
                                />
                            </div>

                            <div className="form-group">
                                <label className="form-label">Students Count</label>
                                <input
                                    type="number"
                                    name="studentsCount"
                                    value={filters.studentsCount}
                                    onChange={handleFilterChange}
                                    className="form-input"
                                    min="1"
                                    placeholder="Exact match"
                                />
                            </div>

                            <div className="form-group">
                                <label className="form-label">Form of Education</label>
                                <select name="formOfEducation" value={filters.formOfEducation} onChange={handleFilterChange} className="form-select">
                                    <option value="">All forms</option>
                                    <option value="DISTANCE_EDUCATION">📚 Distance Education</option>
                                    <option value="FULL_TIME_EDUCATION">🎓 Full Time Education</option>
                                    <option value="EVENING_CLASSES">🌙 Evening Classes</option>
                                </select>
                            </div>

                            <div className="form-group">
                                <label className="form-label">Semester</label>
                                <select name="semesterEnum" value={filters.semesterEnum} onChange={handleFilterChange} className="form-select">
                                    <option value="">All semesters</option>
                                    <option value="FIRST">1st Semester</option>
                                    <option value="THIRD">3rd Semester</option>
                                    <option value="SIXTH">6th Semester</option>
                                    <option value="SEVENTH">7th Semester</option>
                                    <option value="EIGHTH">8th Semester</option>
                                </select>
                            </div>

                            <div className="form-group">
                                <label className="form-label">Creation Date</label>
                                <input
                                    type="datetime-local"
                                    name="creationDate"
                                    value={filters.creationDate}
                                    onChange={handleFilterChange}
                                    className="form-input"
                                />
                            </div>

                            {/* Параметры пагинации */}
                            <div className="form-group">
                                <label className="form-label">Page Size</label>
                                <select
                                    name="size"
                                    value={filters.size}
                                    onChange={handleSizeChange}
                                    className="form-select"
                                >
                                    <option value="10">10 per page</option>
                                    <option value="20">20 per page</option>
                                    <option value="50">50 per page</option>
                                    <option value="100">100 per page</option>
                                </select>
                            </div>

                            <div className="form-group">
                                <label className="form-label">Current Page</label>
                                <input
                                    type="number"
                                    name="page"
                                    value={filters.page}
                                    onChange={handleFilterChange}
                                    className="form-input"
                                    min="0"
                                    max={pagination.totalPages - 1}
                                />
                                <small className="text-muted">
                                    Page {parseInt(filters.page) + 1} of {pagination.totalPages}
                                </small>
                            </div>
                        </div>

                        <div style={{ display: 'flex', gap: '1rem', marginTop: '1.5rem' }}>
                            <button type="submit" disabled={loading} className="btn btn-primary">
                                {loading ? (
                                    <>
                                        <div className="loading" style={{ width: '16px', height: '16px' }}></div>
                                        Applying...
                                    </>
                                ) : (
                                    '🔍 Apply Filters'
                                )}
                            </button>
                            <button type="button" onClick={handleReset} className="btn btn-outline">
                                🗑️ Reset
                            </button>
                        </div>
                    </form>
                </div>

                {error && (
                    <div style={{
                        padding: '1rem',
                        backgroundColor: '#fee',
                        border: '1px solid #e74c3c',
                        borderRadius: '8px',
                        marginBottom: '1.5rem',
                        color: '#c0392b'
                    }}>
                        ❌ {error}
                    </div>
                )}

                <div className="card-header">
                    <h3>📊 Study Groups List</h3>
                    <p className="text-muted">
                        Showing {studyGroups.length} of {pagination.totalElements} groups
                        (Page {pagination.currentPage + 1} of {pagination.totalPages})
                    </p>
                </div>

                {renderTable(studyGroups, columns)}

                {/* Пагинация */}
                {pagination.totalPages > 1 && (
                    <div className="card" style={{ backgroundColor: '#f8f9fa', marginTop: '2rem' }}>
                        <div className="card-header">
                            <h4>📄 Pagination</h4>
                        </div>
                        <div style={{
                            display: 'flex',
                            justifyContent: 'space-between',
                            alignItems: 'center',
                            padding: '1rem',
                            flexWrap: 'wrap',
                            gap: '1rem'
                        }}>
                            {/* Информация о странице */}
                            <div style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
                                <span className="text-muted">
                                    Page <strong>{pagination.currentPage + 1}</strong> of <strong>{pagination.totalPages}</strong>
                                </span>
                                <span className="text-muted">
                                    Total: <strong>{pagination.totalElements}</strong> groups
                                </span>
                            </div>

                            {/* Навигация по страницам */}
                            <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', flexWrap: 'wrap' }}>
                                {/* Кнопка первой страницы */}
                                <button
                                    onClick={() => handlePageChange(0)}
                                    disabled={pagination.currentPage === 0}
                                    className="btn btn-outline"
                                    style={{ padding: '0.5rem 0.75rem' }}
                                >
                                    ⏮️ First
                                </button>

                                {/* Кнопка предыдущей страницы */}
                                <button
                                    onClick={() => handlePageChange(pagination.currentPage - 1)}
                                    disabled={pagination.currentPage === 0}
                                    className="btn btn-outline"
                                    style={{ padding: '0.5rem 0.75rem' }}
                                >
                                    ◀️ Prev
                                </button>

                                {/* Номера страниц */}
                                {getPageNumbers().map((page, index) => (
                                    page === '...' ? (
                                        <span key={`ellipsis-${index}`} style={{ padding: '0.5rem' }}>...</span>
                                    ) : (
                                        <button
                                            key={page}
                                            onClick={() => handlePageChange(page)}
                                            className={page === pagination.currentPage ? 'btn btn-primary' : 'btn btn-outline'}
                                            style={{
                                                padding: '0.5rem 0.75rem',
                                                minWidth: '40px'
                                            }}
                                        >
                                            {page + 1}
                                        </button>
                                    )
                                ))}

                                {/* Кнопка следующей страницы */}
                                <button
                                    onClick={() => handlePageChange(pagination.currentPage + 1)}
                                    disabled={pagination.currentPage >= pagination.totalPages - 1}
                                    className="btn btn-outline"
                                    style={{ padding: '0.5rem 0.75rem' }}
                                >
                                    Next ▶️
                                </button>

                                {/* Кнопка последней страницы */}
                                <button
                                    onClick={() => handlePageChange(pagination.totalPages - 1)}
                                    disabled={pagination.currentPage >= pagination.totalPages - 1}
                                    className="btn btn-outline"
                                    style={{ padding: '0.5rem 0.75rem' }}
                                >
                                    Last ⏭️
                                </button>
                            </div>

                            {/* Выбор размера страницы */}
                            <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                                <span className="text-muted">Show:</span>
                                <select
                                    value={filters.size}
                                    onChange={handleSizeChange}
                                    className="form-select"
                                    style={{ width: 'auto', padding: '0.25rem 0.5rem' }}
                                >
                                    <option value="10">10</option>
                                    <option value="20">20</option>
                                    <option value="50">50</option>
                                    <option value="100">100</option>
                                </select>
                                <span className="text-muted">per page</span>
                            </div>
                        </div>
                    </div>
                )}

                <div style={{ display: 'flex', gap: '1rem', alignItems: 'center', marginTop: '2rem' }}>
                    <button onClick={fetchStudyGroups} disabled={loading} className="btn btn-primary">
                        🔄 Refresh Data
                    </button>
                    <span className="text-muted">
                        {studyGroups.length} groups displayed on this page
                    </span>
                </div>
            </div>
        </div>
    )
}

export default StudyGroupsPage