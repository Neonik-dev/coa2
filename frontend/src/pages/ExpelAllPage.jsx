import React, { useState, useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { isuAPI } from '../services/api'
import { renderTable } from '../utils/tableUtils'

const ExpelAllPage = () => {
    const { groupId } = useParams()
    const navigate = useNavigate()
    const [result, setResult] = useState(null)
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState('')
    const [selectedGroupId, setSelectedGroupId] = useState(groupId || '1')

    const expelAll = async (groupId = selectedGroupId) => {
        setLoading(true)
        setError('')
        try {
            const response = await isuAPI.expelAll(groupId)
            setResult(response.data)
        } catch (err) {
            setError('Ошибка при выполнении операции: ' + (err.response?.data?.message || err.message))
            console.error(err)
        } finally {
            setLoading(false)
        }
    }

    useEffect(() => {
        if (groupId) {
            setSelectedGroupId(groupId)
            // Автоматически не выполняем операцию, ждем подтверждения пользователя
        }
    }, [groupId])

    const handleGroupIdChange = (e) => {
        setSelectedGroupId(e.target.value)
    }

    const handleSubmit = (e) => {
        e.preventDefault()
        if (window.confirm(`Are you sure you want to expel all students from group ${selectedGroupId}? This action cannot be undone.`)) {
            navigate(`/expel-all/${selectedGroupId}`)
            expelAll(selectedGroupId)
        }
    }

    const columns = [
        { key: 'field', title: 'Field', dataIndex: 'field', align: 'left' },
        { key: 'value', title: 'Value', dataIndex: 'value', align: 'left' },
    ]

    const tableData = result ? Object.entries(result).map(([key, value]) => ({
        field: key,
        value: String(value),
    })) : []

    return (
        <div className="fade-in">
            <div className="card">
                <div className="card-header">
                    <h1>🎯 Expel All Students</h1>
                    <p className="text-muted">Remove all students from a specific study group</p>
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
                </div>

                {/* Форма выбора группы */}
                <div className="card" style={{ backgroundColor: '#f8f9fa', marginBottom: '2rem' }}>
                    <div className="card-header">
                        <h3>👥 Select Group</h3>
                        <p className="text-muted">Choose the group from which to expel all students</p>
                    </div>

                    <form onSubmit={handleSubmit}>
                        <div className="grid grid-2">
                            <div className="form-group">
                                <label className="form-label">Group ID</label>
                                <input
                                    type="number"
                                    value={selectedGroupId}
                                    onChange={handleGroupIdChange}
                                    className="form-input"
                                    min="1"
                                    style={{ width: '150px' }}
                                    placeholder="Enter group ID"
                                />
                                <small className="text-muted">Enter the ID of the group to expel students from</small>
                            </div>

                            <div style={{ display: 'flex', alignItems: 'end' }}>
                                <button
                                    type="submit"
                                    disabled={loading}
                                    className="btn btn-danger"
                                    style={{ minWidth: '200px' }}
                                >
                                    {loading ? (
                                        <>
                                            <div className="loading" style={{ width: '16px', height: '16px', borderTopColor: 'white' }}></div>
                                            Expelling...
                                        </>
                                    ) : (
                                        '🎯 Expel All Students'
                                    )}
                                </button>
                            </div>
                        </div>
                    </form>

                    <div style={{ marginTop: '1rem', padding: '1rem', backgroundColor: '#fde8e8', borderRadius: '8px', border: '1px solid #f5c6cb' }}>
                        <h4 style={{ color: '#c0392b', marginBottom: '0.5rem' }}>⚠️ Important Notice</h4>
                        <p style={{ margin: 0, color: '#c0392b' }}>
                            This action will permanently remove all students from the selected group. This operation cannot be undone.
                        </p>
                    </div>
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

                {result && (
                    <div>
                        <div className="card-header">
                            <h3>✅ Operation Result</h3>
                            <p className="text-muted">Expulsion operation completed successfully</p>
                        </div>

                        {renderTable(tableData, columns)}

                        <div style={{ marginTop: '2rem', padding: '1.5rem', backgroundColor: '#e8f6f3', borderRadius: '8px', border: '1px solid #c8e6c9' }}>
                            <div style={{ display: 'flex', alignItems: 'center', gap: '1rem', marginBottom: '1rem' }}>
                                <div style={{ fontSize: '2rem' }}>✅</div>
                                <div>
                                    <h4 style={{ color: '#27ae60', margin: 0 }}>Operation Successful</h4>
                                    <p style={{ color: '#27ae60', margin: 0 }}>
                                        Successfully expelled {result.expelled} students from group {result.groupId}
                                    </p>
                                </div>
                            </div>

                            <div className="grid grid-2">
                                <div className="card text-center" style={{ backgroundColor: 'white' }}>
                                    <div style={{ fontSize: '2rem', marginBottom: '0.5rem' }}>👥</div>
                                    <h4 style={{ color: '#2c3e50', marginBottom: '0.5rem' }}>Group ID</h4>
                                    <p style={{ fontSize: '1.5rem', fontWeight: 'bold', color: '#3498db', margin: 0 }}>
                                        {result.groupId}
                                    </p>
                                </div>

                                <div className="card text-center" style={{ backgroundColor: 'white' }}>
                                    <div style={{ fontSize: '2rem', marginBottom: '0.5rem' }}>🚶‍♂️</div>
                                    <h4 style={{ color: '#2c3e50', marginBottom: '0.5rem' }}>Students Expelled</h4>
                                    <p style={{ fontSize: '1.5rem', fontWeight: 'bold', color: '#e74c3c', margin: 0 }}>
                                        {result.expelled}
                                    </p>
                                </div>
                            </div>
                        </div>

                        <div style={{ display: 'flex', gap: '1rem', marginTop: '2rem' }}>
                            <button
                                onClick={() => navigate(`/studygroups/${result.groupId}`)}
                                className="btn btn-primary"
                            >
                                👁️ View Group
                            </button>
                            <button
                                onClick={() => setResult(null)}
                                className="btn btn-outline"
                            >
                                🆕 New Operation
                            </button>
                        </div>
                    </div>
                )}
            </div>
        </div>
    )
}

export default ExpelAllPage