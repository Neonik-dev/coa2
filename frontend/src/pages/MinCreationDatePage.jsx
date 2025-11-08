import React, { useState, useEffect } from 'react'
import { studyGroupsAPI } from '../services/api'
import { renderTable } from '../utils/tableUtils'
import { useNavigate } from 'react-router-dom'

const MinCreationDatePage = () => {
    const navigate = useNavigate()
    const [studyGroup, setStudyGroup] = useState(null)
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState('')

    const fetchMinCreationDate = async () => {
        setLoading(true)
        setError('')
        try {
            const response = await studyGroupsAPI.getMinCreationDate()
            setStudyGroup(response.data)
        } catch (err) {
            setError('Ошибка при загрузке данных')
            console.error(err)
        } finally {
            setLoading(false)
        }
    }

    useEffect(() => {
        fetchMinCreationDate()
    }, [])

    const columns = [
        { key: 'field', title: 'Field', dataIndex: 'field', align: 'left' },
        { key: 'value', title: 'Value', dataIndex: 'value', align: 'left' },
    ]

    const tableData = studyGroup ? Object.entries(studyGroup).map(([key, value]) => ({
        field: key,
        value: typeof value === 'object' ? JSON.stringify(value, null, 2) : String(value),
    })) : []

    return (
        <div className="fade-in">
            <div className="card">
                <div className="card-header">
                    <h1>📅 Oldest Study Group</h1>
                    <p className="text-muted">The study group with the earliest creation date in the system</p>
                </div>

                <div style={{ display: 'flex', gap: '1rem', marginBottom: '2rem', flexWrap: 'wrap' }}>
                    <button
                        onClick={() => navigate('/')}
                        className="btn btn-secondary"
                    >
                        🏠 Back to Home
                    </button>
                    <button
                        onClick={() => navigate('/studygroups')}
                        className="btn btn-outline"
                    >
                        📋 All Groups
                    </button>
                    <button
                        onClick={fetchMinCreationDate}
                        disabled={loading}
                        className="btn btn-primary"
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
                        <p className="text-muted">Searching for the oldest group...</p>
                    </div>
                )}

                {!studyGroup && !loading && (
                    <div style={{ textAlign: 'center', padding: '3rem', color: '#7f8c8d' }}>
                        <div style={{ fontSize: '4rem', marginBottom: '1rem' }}>⏰</div>
                        <h3>No Groups Found</h3>
                        <p>There are no study groups in the system yet.</p>
                        <button
                            onClick={() => navigate('/create-studygroups')}
                            className="btn btn-success"
                            style={{ marginTop: '1rem' }}
                        >
                            🎓 Create First Group
                        </button>
                    </div>
                )}

                {studyGroup && (
                    <div>
                        <div className="card" style={{ backgroundColor: '#fff3cd', borderColor: '#ffeaa7', marginBottom: '2rem' }}>
                            <div style={{ padding: '1.5rem', textAlign: 'center' }}>
                                <div style={{ fontSize: '3rem', marginBottom: '1rem' }}>🥇</div>
                                <h3 style={{ color: '#856404', marginBottom: '0.5rem' }}>Oldest Group Found!</h3>
                                <p style={{ color: '#856404', margin: 0 }}>
                                    This group was created on <strong>{studyGroup.creationDate}</strong>
                                </p>
                            </div>
                        </div>

                        <div className="card-header">
                            <h3>📊 Group Details</h3>
                        </div>
                        {renderTable(tableData, columns)}

                        <div style={{ display: 'flex', gap: '1rem', marginTop: '2rem' }}>
                            <button
                                onClick={() => navigate(`/studygroups/${studyGroup.id}`)}
                                className="btn btn-primary"
                            >
                                👁️ View Full Details
                            </button>
                            <button
                                onClick={() => navigate(`/update-studygroups/${studyGroup.id}`)}
                                className="btn btn-outline"
                            >
                                ✏️ Edit Group
                            </button>
                        </div>
                    </div>
                )}
            </div>
        </div>
    )
}

export default MinCreationDatePage