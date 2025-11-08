import React, { useState, useEffect } from 'react'
import { studyGroupsAPI } from '../services/api'
import { renderTable } from '../utils/tableUtils'
import { useNavigate } from 'react-router-dom'

const GroupByIdPage = () => {
    const navigate = useNavigate()
    const [groups, setGroups] = useState([])
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState('')

    const fetchGroupById = async () => {
        setLoading(true)
        setError('')
        try {
            const response = await studyGroupsAPI.getGroupById()
            setGroups(response.data)
        } catch (err) {
            setError('Ошибка при загрузке данных')
            console.error(err)
        } finally {
            setLoading(false)
        }
    }

    useEffect(() => {
        fetchGroupById()
    }, [])

    const columns = [
        {
            key: 'id',
            title: 'Form of education',
            dataIndex: 'formOfEducation',
            align: 'center'
        },
        {
            key: 'count',
            title: 'Count',
            dataIndex: 'count',
            align: 'center',
            render: (count) => (
                <span style={{
                    fontWeight: 'bold',
                    color: count > 0 ? '#27ae60' : '#e74c3c'
                }}>
          {count}
        </span>
            )
        },
    ]

    const totalGroups = groups.reduce((sum, group) => sum + group.count, 0)

    return (
        <div className="fade-in">
            <div className="card">
                <div className="card-header">
                    <h1>📈 Group Statistics</h1>
                    <p className="text-muted">Distribution of study groups by their IDs</p>
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
                        onClick={fetchGroupById}
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
                        <p className="text-muted">Calculating group statistics...</p>
                    </div>
                )}

                {/* Статистика */}
                {groups.length > 0 && (
                    <div className="grid grid-3" style={{ marginBottom: '2rem' }}>
                        <div className="card text-center" style={{ backgroundColor: '#e8f4fd' }}>
                            <div style={{ fontSize: '2rem', marginBottom: '0.5rem' }}>📊</div>
                            <h3 style={{ color: '#2c3e50', marginBottom: '0.5rem' }}>Total Groups</h3>
                            <p style={{ fontSize: '2rem', fontWeight: 'bold', color: '#3498db', margin: 0 }}>
                                {groups.length}
                            </p>
                        </div>

                        <div className="card text-center" style={{ backgroundColor: '#e8f6f3' }}>
                            <div style={{ fontSize: '2rem', marginBottom: '0.5rem' }}>👥</div>
                            <h3 style={{ color: '#2c3e50', marginBottom: '0.5rem' }}>Total Records</h3>
                            <p style={{ fontSize: '2rem', fontWeight: 'bold', color: '#27ae60', margin: 0 }}>
                                {totalGroups}
                            </p>
                        </div>

                        <div className="card text-center" style={{ backgroundColor: '#fef9e7' }}>
                            <div style={{ fontSize: '2rem', marginBottom: '0.5rem' }}>📅</div>
                            <h3 style={{ color: '#2c3e50', marginBottom: '0.5rem' }}>Average per Group</h3>
                            <p style={{ fontSize: '2rem', fontWeight: 'bold', color: '#f39c12', margin: 0 }}>
                                {(totalGroups / groups.length).toFixed(1)}
                            </p>
                        </div>
                    </div>
                )}

                <div className="card-header">
                    <h3>📋 Group Distribution</h3>
                    <p className="text-muted">
                        {groups.length > 0
                            ? `Showing distribution across ${groups.length} unique group IDs`
                            : 'No group data available'
                        }
                    </p>
                </div>

                {groups.length === 0 && !loading ? (
                    <div style={{ textAlign: 'center', padding: '3rem', color: '#7f8c8d' }}>
                        <div style={{ fontSize: '4rem', marginBottom: '1rem' }}>📊</div>
                        <h3>No Group Data</h3>
                        <p>There are no study groups to analyze yet.</p>
                        <button
                            onClick={() => navigate('/create-studygroups')}
                            className="btn btn-success"
                            style={{ marginTop: '1rem' }}
                        >
                            🎓 Create First Group
                        </button>
                    </div>
                ) : (
                    renderTable(groups, columns)
                )}

                {groups.length > 0 && (
                    <div style={{ marginTop: '2rem', padding: '1.5rem', backgroundColor: '#f8f9fa', borderRadius: '8px' }}>
                        <h4 style={{ marginBottom: '1rem', color: '#2c3e50' }}>📝 Analysis Summary</h4>
                        <div className="grid grid-2">
                            <div>
                                <p><strong>Most frequent group ID:</strong> {Math.max(...groups.map(g => g.count))} occurrences</p>
                                <p><strong>Least frequent group ID:</strong> {Math.min(...groups.map(g => g.count))} occurrences</p>
                            </div>
                            <div>
                                <p><strong>Unique group IDs:</strong> {groups.length}</p>
                                <p><strong>Total data points:</strong> {totalGroups}</p>
                            </div>
                        </div>
                    </div>
                )}
            </div>
        </div>
    )
}

export default GroupByIdPage