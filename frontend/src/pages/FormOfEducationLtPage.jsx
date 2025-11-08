import React, { useState, useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { studyGroupsAPI } from '../services/api'
import { renderTable } from '../utils/tableUtils'

const FormOfEducationLtPage = () => {
    const { value } = useParams()
    const navigate = useNavigate()
    const [studyGroups, setStudyGroups] = useState([])
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState('')
    const [selectedValue, setSelectedValue] = useState(value || 'DISTANCE_EDUCATION')

    const fetchFormOfEducationLt = async (formValue = selectedValue) => {
        setLoading(true)
        setError('')
        try {
            const response = await studyGroupsAPI.getFormOfEducationLt(formValue)
            setStudyGroups(response.data)
        } catch (err) {
            setError('Ошибка при загрузке данных')
            console.error(err)
        } finally {
            setLoading(false)
        }
    }

    useEffect(() => {
        if (value) {
            setSelectedValue(value)
            fetchFormOfEducationLt(value)
        } else {
            fetchFormOfEducationLt()
        }
    }, [value])

    const handleValueChange = (e) => {
        const newValue = e.target.value
        setSelectedValue(newValue)
        navigate(`/form-of-education-lt/${newValue}`)
    }

    const handleSubmit = (e) => {
        e.preventDefault()
        fetchFormOfEducationLt(selectedValue)
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
            key: 'studentsCount',
            title: 'Students Count',
            dataIndex: 'studentsCount',
            align: 'center'
        },
        {
            key: 'creationDate',
            title: 'Creation Date',
            dataIndex: 'creationDate',
            align: 'center'
        },
    ]

    const formOrder = {
        'DISTANCE_EDUCATION': 1,
        'FULL_TIME_EDUCATION': 2,
        'EVENING_CLASSES': 3
    }

    const comparisonText = {
        'DISTANCE_EDUCATION': 'less than DISTANCE_EDUCATION (no groups)',
        'FULL_TIME_EDUCATION': 'less than FULL_TIME_EDUCATION (only DISTANCE_EDUCATION)',
        'EVENING_CLASSES': 'less than EVENING_CLASSES (DISTANCE_EDUCATION and FULL_TIME_EDUCATION)'
    }

    return (
        <div className="fade-in">
            <div className="card">
                <div className="card-header">
                    <h1>🎓 Filter by Education Form</h1>
                    <p className="text-muted">Find study groups with form of education less than specified value</p>
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

                {/* Форма выбора */}
                <div className="card" style={{ backgroundColor: '#f8f9fa', marginBottom: '2rem' }}>
                    <div className="card-header">
                        <h3>🔍 Select Comparison Value</h3>
                        <p className="text-muted">Choose the form of education to compare against</p>
                    </div>

                    <form onSubmit={handleSubmit}>
                        <div className="grid grid-2">
                            <div className="form-group">
                                <label className="form-label">Form of Education</label>
                                <select
                                    value={selectedValue}
                                    onChange={handleValueChange}
                                    className="form-select"
                                    style={{ minWidth: '200px' }}
                                >
                                    <option value="DISTANCE_EDUCATION">📚 Distance Education</option>
                                    <option value="FULL_TIME_EDUCATION">🎓 Full Time Education</option>
                                    <option value="EVENING_CLASSES">🌙 Evening Classes</option>
                                </select>
                            </div>

                            <div style={{ display: 'flex', alignItems: 'end' }}>
                                <button type="submit" disabled={loading} className="btn btn-primary">
                                    {loading ? (
                                        <>
                                            <div className="loading" style={{ width: '16px', height: '16px' }}></div>
                                            Searching...
                                        </>
                                    ) : (
                                        '🔍 Find Groups'
                                    )}
                                </button>
                            </div>
                        </div>
                    </form>

                    <div style={{ marginTop: '1rem', padding: '1rem', backgroundColor: '#e8f4fd', borderRadius: '8px' }}>
                        <h4 style={{ color: '#3498db', marginBottom: '0.5rem' }}>ℹ️ Comparison Order</h4>
                        <p style={{ margin: 0, color: '#2c3e50' }}>
                            <strong>DISTANCE_EDUCATION</strong> {'<'} <strong>EVENING_CLASSES</strong> {'<'} <strong>FULL_TIME_EDUCATION</strong>
                        </p>
                        <p style={{ margin: '0.5rem 0 0 0', color: '#2c3e50' }}>
                            <strong>Currently showing:</strong> Groups with form of education {comparisonText[selectedValue]}
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

                <div className="card-header">
                    <h3>📋 Matching Groups</h3>
                    <p className="text-muted">
                        {studyGroups.length > 0
                            ? `Found ${studyGroups.length} groups with form of education less than ${selectedValue}`
                            : 'No groups match the selected criteria'
                        }
                    </p>
                </div>

                {studyGroups.length === 0 && !loading ? (
                    <div style={{ textAlign: 'center', padding: '3rem', color: '#7f8c8d' }}>
                        <div style={{ fontSize: '4rem', marginBottom: '1rem' }}>🔍</div>
                        <h3>No Groups Found</h3>
                        <p>No study groups match the selected filter criteria.</p>
                        <button
                            onClick={() => navigate('/create-studygroups')}
                            className="btn btn-success"
                            style={{ marginTop: '1rem' }}
                        >
                            🎓 Create New Group
                        </button>
                    </div>
                ) : (
                    renderTable(studyGroups, columns)
                )}

                {studyGroups.length > 0 && (
                    <div style={{ display: 'flex', gap: '1rem', alignItems: 'center', marginTop: '2rem' }}>
                        <button onClick={() => fetchFormOfEducationLt(selectedValue)} disabled={loading} className="btn btn-primary">
                            🔄 Refresh Results
                        </button>
                        <span className="text-muted">
              {studyGroups.length} groups displayed
            </span>
                    </div>
                )}
            </div>
        </div>
    )
}

export default FormOfEducationLtPage