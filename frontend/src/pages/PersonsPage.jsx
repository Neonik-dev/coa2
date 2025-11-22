import React, { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { personsAPI } from '../services/api'
import { renderTable } from '../utils/tableUtils'

const PersonsPage = () => {
    const navigate = useNavigate()
    const [persons, setPersons] = useState([])
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState('')
    const [pagination, setPagination] = useState({
        currentPage: 0,
        totalPages: 0,
        totalElements: 0
    })
    const [filters, setFilters] = useState({
        name: '',
        passportId: '',
        birthday: '',
        weight: '',
        nationality: '',
        sort: '',
        page: '0',
        size: '20'
    })

    const fetchPersons = async (filterParams = filters) => {
        setLoading(true)
        setError('')
        try {
            const params = Object.fromEntries(
                Object.entries(filterParams).filter(([_, value]) => value !== '')
            )

            const response = await personsAPI.getAll(params)
            setPersons(response.data.data || [])

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

    useEffect(() => {
        fetchPersons()
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
        fetchPersons({ ...filters, page: '0' })
    }

    const handleReset = () => {
        const resetFilters = {
            name: '',
            passportId: '',
            birthday: '',
            weight: '',
            nationality: '',
            sort: '',
            page: '0',
            size: '20'
        }
        setFilters(resetFilters)
        fetchPersons(resetFilters)
    }

    const handlePageChange = (newPage) => {
        if (newPage >= 0 && newPage < pagination.totalPages) {
            setFilters(prev => ({ ...prev, page: newPage.toString() }))
            fetchPersons({ ...filters, page: newPage.toString() })
        }
    }

    const handleSizeChange = (e) => {
        const newSize = e.target.value
        setFilters(prev => ({ ...prev, size: newSize, page: '0' }))
        fetchPersons({ ...filters, size: newSize, page: '0' })
    }

    const columns = [
        {
            key: 'name',
            title: 'Name',
            dataIndex: 'name',
            align: 'left'
        },
        {
            key: 'birthday',
            title: 'Birthday',
            dataIndex: 'birthday',
            align: 'center'
        },
        {
            key: 'weight',
            title: 'Weight',
            dataIndex: 'weight',
            align: 'center'
        },
        {
            key: 'passportID',
            title: 'Passport ID',
            dataIndex: 'passportId',
            align: 'center'
        },
        {
            key: 'nationality',
            title: 'Nationality',
            dataIndex: 'nationality',
            align: 'center'
        },
    ]

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

    return (
        <div className="fade-in">
            <div className="card">
                <div className="card-header">
                    <h1>👥 Persons Management</h1>
                    <p className="text-muted">View and manage all persons in the system</p>
                </div>

                <div style={{ display: 'flex', gap: '1rem', marginBottom: '1.5rem', flexWrap: 'wrap' }}>
                    <button
                        onClick={() => navigate('/create-person')}
                        className="btn btn-success"
                    >
                        👤 Create New Person
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
                        <p className="text-muted">Apply filters to find specific persons</p>
                    </div>

                    <form onSubmit={handleSubmit}>
                        <div className="grid grid-3">
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
                                <label className="form-label">Passport ID</label>
                                <input
                                    type="text"
                                    name="passportId"
                                    value={filters.passportId}
                                    onChange={handleFilterChange}
                                    className="form-input"
                                    placeholder="Exact match"
                                />
                            </div>

                            <div className="form-group">
                                <label className="form-label">Birthday</label>
                                <input
                                    type="date"
                                    name="birthday"
                                    value={filters.birthday}
                                    onChange={handleFilterChange}
                                    className="form-input"
                                />
                            </div>

                            <div className="form-group">
                                <label className="form-label">Weight</label>
                                <input
                                    type="number"
                                    name="weight"
                                    value={filters.weight}
                                    onChange={handleFilterChange}
                                    className="form-input"
                                    min="1"
                                    placeholder="Exact match"
                                />
                            </div>

                            <div className="form-group">
                                <label className="form-label">Nationality</label>
                                <select name="nationality" value={filters.nationality} onChange={handleFilterChange} className="form-select">
                                    <option value="">All nationalities</option>
                                    <option value="FRANCE">🇫🇷 FRANCE</option>
                                    <option value="CHINA">🇨🇳 CHINA</option>
                                    <option value="INDIA">🇮🇳 INDIA</option>
                                </select>
                            </div>

                            <div className="form-group">
                                <label className="form-label">Sort</label>
                                <input
                                    type="text"
                                    name="sort"
                                    value={filters.sort}
                                    onChange={handleFilterChange}
                                    className="form-input"
                                    placeholder="name:desc,weight"
                                />
                                <small className="text-muted">Format: field:direction,field2</small>
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
                    <h3>📊 Persons List</h3>
                    <p className="text-muted">
                        Page {pagination.currentPage + 1} of {pagination.totalPages}
                    </p>
                </div>

                {renderTable(persons, columns)}

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
                                    Total: <strong>{pagination.totalElements}</strong> persons
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
                    <button onClick={fetchPersons} disabled={loading} className="btn btn-primary">
                        🔄 Refresh Data
                    </button>
                    <span className="text-muted">
                        {persons.length} persons displayed on this page
                    </span>
                </div>
            </div>
        </div>
    )
}

export default PersonsPage