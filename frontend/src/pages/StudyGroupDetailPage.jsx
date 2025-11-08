import React, { useState, useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { studyGroupsAPI } from '../services/api'
import { renderTable } from '../utils/tableUtils'

const StudyGroupDetailPage = () => {
    const { id } = useParams()
    const navigate = useNavigate()
    const [studyGroup, setStudyGroup] = useState(null)
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState('')

    const fetchStudyGroup = async () => {
        setLoading(true)
        setError('')
        try {
            const response = await studyGroupsAPI.getById(id)
            setStudyGroup(response.data)
        } catch (err) {
            setError('Ошибка при загрузке данных')
            console.error(err)
        } finally {
            setLoading(false)
        }
    }

    useEffect(() => {
        if (id) {
            fetchStudyGroup()
        }
    }, [id])

    const columns = [
        { key: 'field', title: 'Field', dataIndex: 'field', align: 'left' },
        { key: 'value', title: 'Value', dataIndex: 'value', align: 'left' },
    ]

    const tableData = studyGroup ? Object.entries(studyGroup)
        .filter(([key, _]) => key !== 'groupAdmin') // Исключаем groupAdmin
        .map(([key, value]) => ({
            field: key,
            value: typeof value === 'object' ? JSON.stringify(value, null, 2) : String(value),
        })) : []

    return (
        <div className="fade-in">
            <div className="card">
                <div className="card-header">
                    <h1>👁️ Study Group Details</h1>
                    <p className="text-muted">Detailed information about study group #{id}</p>
                </div>

                <div style={{ display: 'flex', gap: '1rem', marginBottom: '2rem', flexWrap: 'wrap' }}>
                    <button
                        onClick={() => navigate('/studygroups')}
                        className="btn btn-secondary"
                    >
                        📋 Back to List
                    </button>
                    <button
                        onClick={() => navigate(`/update-studygroups/${id}`)}
                        className="btn btn-primary"
                    >
                        ✏️ Edit Group
                    </button>
                    <button
                        onClick={fetchStudyGroup}
                        disabled={loading}
                        className="btn btn-outline"
                    >
                        🔄 Refresh
                    </button>
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

                {loading && (
                    <div style={{ textAlign: 'center', padding: '3rem' }}>
                        <div className="loading" style={{ width: '40px', height: '40px', margin: '0 auto 1rem' }}></div>
                        <p className="text-muted">Loading group details...</p>
                    </div>
                )}

                {studyGroup && (
                    <div>
                        <div className="card-header">
                            <h3>📊 Group Information</h3>
                        </div>
                        {renderTable(tableData, columns)}

                        {/* Красивое отображение администратора */}
                        {studyGroup.groupAdmin && (
                            <div className="card mt-3" style={{ backgroundColor: '#e8f4fd' }}>
                                <div className="card-header">
                                    <h4>👤 Group Administrator</h4>
                                </div>
                                <div style={{ padding: '1rem' }}>
                                    <div className="grid grid-3">
                                        <div>
                                            <strong>Id:</strong>
                                            <p>{studyGroup.groupAdmin.id}</p>
                                        </div>
                                        <div>
                                            <strong>Name:</strong>
                                            <p>{studyGroup.groupAdmin.name}</p>
                                        </div>
                                        <div>
                                            <strong>Passport:</strong>
                                            <p>{studyGroup.groupAdmin.passportId || 'N/A'}</p>
                                        </div>
                                        <div>
                                            <strong>Nationality:</strong>
                                            <p>{studyGroup.groupAdmin.nationality || 'N/A'}</p>
                                        </div>
                                        <div>
                                            <strong>Weight:</strong>
                                            <p>{studyGroup.groupAdmin.weight || 'N/A'}</p>
                                        </div>
                                        <div>
                                            <strong>Birthday:</strong>
                                            <p>{studyGroup.groupAdmin.birthday || 'N/A'}</p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        )}
                    </div>
                )}
            </div>
        </div>
    )
}

export default StudyGroupDetailPage