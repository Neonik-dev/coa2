import React, { useState, useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { isuAPI } from '../services/api'
import { renderTable } from '../utils/tableUtils'

const ChangeEduFormPage = () => {
    const { groupId, newForm } = useParams()
    const navigate = useNavigate()
    const [result, setResult] = useState(null)
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState('')
    const [selectedGroupId, setSelectedGroupId] = useState(groupId || '1')
    const [selectedForm, setSelectedForm] = useState(newForm || 'FULL_TIME_EDUCATION')

    const changeEduForm = async (groupId = selectedGroupId, form = selectedForm) => {
        setLoading(true)
        setError('')
        try {
            const response = await isuAPI.changeEduForm(groupId, form)
            setResult(response.data)
        } catch (err) {
            setError('Ошибка при выполнении операции: ' + (err.response?.data?.message || err.message))
            console.error(err)
        } finally {
            setLoading(false)
        }
    }

    useEffect(() => {
        if (groupId && newForm) {
            setSelectedGroupId(groupId)
            setSelectedForm(newForm)
            // Автоматически не выполняем операцию, ждем подтверждения пользователя
        }
    }, [groupId, newForm])

    const handleGroupIdChange = (e) => {
        setSelectedGroupId(e.target.value)
    }

    const handleFormChange = (e) => {
        setSelectedForm(e.target.value)
    }

    const handleSubmit = (e) => {
        e.preventDefault()
        if (window.confirm(`Are you sure you want to change the education form of group ${selectedGroupId} to ${selectedForm}?`)) {
            navigate(`/change-edu-form/${selectedGroupId}/${selectedForm}`)
            changeEduForm(selectedGroupId, selectedForm)
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

    const getFormDisplayName = (form) => {
        switch (form) {
            case 'DISTANCE_EDUCATION': return '📚 Distance Education'
            case 'FULL_TIME_EDUCATION': return '🎓 Full Time Education'
            case 'EVENING_CLASSES': return '🌙 Evening Classes'
            default: return form
        }
    }

    return (
        <div className="fade-in">
            <div className="card">
                <div className="card-header">
                    <h1>🔄 Change Education Format</h1>
                    <p className="text-muted">Update the form of education for a specific study group</p>
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

                {/* Форма выбора параметров */}
                <div className="card" style={{ backgroundColor: '#f8f9fa', marginBottom: '2rem' }}>
                    <div className="card-header">
                        <h3>⚙️ Operation Parameters</h3>
                        <p className="text-muted">Select the group and new education format</p>
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
                                    required
                                />
                                <small className="text-muted">ID of the group to update</small>
                            </div>

                            <div className="form-group">
                                <label className="form-label">New Education Format</label>
                                <select
                                    value={selectedForm}
                                    onChange={handleFormChange}
                                    className="form-select"
                                    style={{ minWidth: '250px' }}
                                    required
                                >
                                    <option value="DISTANCE_EDUCATION">📚 Distance Education</option>
                                    <option value="FULL_TIME_EDUCATION">🎓 Full Time Education</option>
                                    <option value="EVENING_CLASSES">🌙 Evening Classes</option>
                                </select>
                                <small className="text-muted">Select the new form of education</small>
                            </div>
                        </div>

                        <div style={{ display: 'flex', gap: '1rem', marginTop: '1.5rem' }}>
                            <button
                                type="submit"
                                disabled={loading}
                                className="btn btn-primary"
                                style={{ minWidth: '200px' }}
                            >
                                {loading ? (
                                    <>
                                        <div className="loading" style={{ width: '16px', height: '16px' }}></div>
                                        Updating...
                                    </>
                                ) : (
                                    '🔄 Change Format'
                                )}
                            </button>

                            <button
                                type="button"
                                onClick={() => {
                                    setSelectedGroupId('1')
                                    setSelectedForm('FULL_TIME_EDUCATION')
                                }}
                                className="btn btn-outline"
                            >
                                🗑️ Reset
                            </button>
                        </div>
                    </form>

                    <div style={{ marginTop: '1rem', padding: '1rem', backgroundColor: '#e8f4fd', borderRadius: '8px', border: '1px solid #b3d9ff' }}>
                        <h4 style={{ color: '#3498db', marginBottom: '0.5rem' }}>💡 Current Selection</h4>
                        <p style={{ margin: 0, color: '#2c3e50' }}>
                            Changing group <strong>#{selectedGroupId}</strong> to <strong>{getFormDisplayName(selectedForm)}</strong>
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
                            <h3>✅ Operation Completed</h3>
                            <p className="text-muted">Education format successfully updated</p>
                        </div>

                        {renderTable(tableData, columns)}

                        <div style={{ marginTop: '2rem', padding: '1.5rem', backgroundColor: '#e8f6f3', borderRadius: '8px', border: '1px solid #c8e6c9' }}>
                            <div style={{ display: 'flex', alignItems: 'center', gap: '1rem', marginBottom: '1.5rem' }}>
                                <div style={{ fontSize: '2rem' }}>✅</div>
                                <div>
                                    <h4 style={{ color: '#27ae60', margin: 0 }}>Update Successful</h4>
                                    <p style={{ color: '#27ae60', margin: 0 }}>
                                        Group education format has been successfully changed
                                    </p>
                                </div>
                            </div>

                            <div className="grid grid-2">
                                <div className="card text-center" style={{ backgroundColor: 'white' }}>
                                    <div style={{ fontSize: '2rem', marginBottom: '0.5rem' }}>🎓</div>
                                    <h4 style={{ color: '#2c3e50', marginBottom: '0.5rem' }}>Group ID</h4>
                                    <p style={{ fontSize: '1.5rem', fontWeight: 'bold', color: '#3498db', margin: 0 }}>
                                        {result.groupId}
                                    </p>
                                </div>

                                <div className="card text-center" style={{ backgroundColor: 'white' }}>
                                    <div style={{ fontSize: '2rem', marginBottom: '0.5rem' }}>
                                        {result.formOfEducation === 'DISTANCE_EDUCATION' ? '📚' :
                                            result.formOfEducation === 'FULL_TIME_EDUCATION' ? '🎓' : '🌙'}
                                    </div>
                                    <h4 style={{ color: '#2c3e50', marginBottom: '0.5rem' }}>New Format</h4>
                                    <p style={{
                                        fontSize: '1.1rem',
                                        fontWeight: 'bold',
                                        color: '#27ae60',
                                        padding: '0.5rem',
                                        backgroundColor:
                                            result.formOfEducation === 'DISTANCE_EDUCATION' ? '#e8f4fd' :
                                                result.formOfEducation === 'FULL_TIME_EDUCATION' ? '#e8f6f3' : '#fef9e7',
                                        borderRadius: '8px',
                                        margin: 0
                                    }}>
                                        {getFormDisplayName(result.formOfEducation)}
                                    </p>
                                </div>
                            </div>
                        </div>

                        <div style={{ display: 'flex', gap: '1rem', marginTop: '2rem', flexWrap: 'wrap' }}>
                            <button
                                onClick={() => navigate(`/studygroups/${result.groupId}`)}
                                className="btn btn-primary"
                            >
                                👁️ View Group
                            </button>
                            <button
                                onClick={() => navigate('/studygroups')}
                                className="btn btn-outline"
                            >
                                📋 All Groups
                            </button>
                            <button
                                onClick={() => {
                                    setResult(null)
                                    setSelectedGroupId('1')
                                    setSelectedForm('FULL_TIME_EDUCATION')
                                }}
                                className="btn btn-success"
                            >
                                🆕 New Operation
                            </button>
                        </div>
                    </div>
                )}

                {/* Информационная панель */}
                {!result && (
                    <div className="card mt-3" style={{ backgroundColor: '#fff3cd', borderColor: '#ffeaa7' }}>
                        <div style={{ padding: '1.5rem' }}>
                            <div style={{ display: 'flex', alignItems: 'flex-start', gap: '1rem' }}>
                                <div style={{ fontSize: '1.5rem' }}>ℹ️</div>
                                <div>
                                    <h4 style={{ color: '#856404', marginBottom: '0.5rem' }}>About This Operation</h4>
                                    <p style={{ color: '#856404', marginBottom: '0.5rem' }}>
                                        This operation allows you to change the form of education for any study group in the system.
                                        The change will affect all students in the selected group.
                                    </p>
                                    <div style={{ display: 'flex', gap: '2rem', flexWrap: 'wrap' }}>
                                        <div>
                                            <strong style={{ color: '#856404' }}>Available Formats:</strong>
                                            <ul style={{ color: '#856404', margin: '0.5rem 0 0 0', paddingLeft: '1.5rem' }}>
                                                <li>📚 Distance Education</li>
                                                <li>🎓 Full Time Education</li>
                                                <li>🌙 Evening Classes</li>
                                            </ul>
                                        </div>
                                        <div>
                                            <strong style={{ color: '#856404' }}>Usage Tips:</strong>
                                            <ul style={{ color: '#856404', margin: '0.5rem 0 0 0', paddingLeft: '1.5rem' }}>
                                                <li>Enter a valid group ID</li>
                                                <li>Select the desired education format</li>
                                                <li>Confirm the operation when prompted</li>
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                )}
            </div>
        </div>
    )
}

export default ChangeEduFormPage