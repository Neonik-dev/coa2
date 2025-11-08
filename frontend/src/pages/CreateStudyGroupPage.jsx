import React, { useState, useEffect } from 'react'
import { studyGroupsAPI, personsAPI } from '../services/api'
import { useNavigate } from 'react-router-dom'

export const CreateStudyGroupPage = () => {
    const navigate = useNavigate()
    const [formData, setFormData] = useState({
        name: '',
        coordinates: { x: '', y: '' },
        studentsCount: '',
        formOfEducation: '',
        semesterEnum: '',
        groupAdmin: ''
    })
    const [persons, setPersons] = useState([])
    const [loading, setLoading] = useState(false)
    const [personsLoading, setPersonsLoading] = useState(false)
    const [error, setError] = useState('')

    const fetchPersons = async () => {
        setPersonsLoading(true)
        try {
            const response = await personsAPI.getAll()
            setPersons(response.data.data || [])
        } catch (err) {
            console.error('Ошибка при загрузке persons:', err)
        } finally {
            setPersonsLoading(false)
        }
    }

    useEffect(() => {
        fetchPersons()
    }, [])

    const handleChange = (e) => {
        const { name, value } = e.target
        if (name.startsWith('coordinates.')) {
            const coordField = name.split('.')[1]
            setFormData(prev => ({
                ...prev,
                coordinates: {
                    ...prev.coordinates,
                    [coordField]: value
                }
            }))
        } else {
            setFormData(prev => ({
                ...prev,
                [name]: value
            }))
        }
    }

    const handleSubmit = async (e) => {
        e.preventDefault()
        setLoading(true)
        setError('')

        try {
            const payload = {
                name: formData.name,
                coordinates: {
                    x: formData.coordinates.x ? parseInt(formData.coordinates.x) : undefined,
                    y: parseInt(formData.coordinates.y)
                },
                studentsCount: formData.studentsCount ? parseInt(formData.studentsCount) : null,
                formOfEducation: formData.formOfEducation || null,
                semesterEnum: formData.semesterEnum || null,
                groupAdmin: formData.groupAdmin ? formData.groupAdmin : null
            }

            await studyGroupsAPI.create(payload)
            navigate('/studygroups')
        } catch (err) {
            setError('Ошибка при создании StudyGroup: ' + (err.response?.data?.message || err.message))
            console.error('Error details:', err)
        } finally {
            setLoading(false)
        }
    }

    return (
        <div className="fade-in">
            <div className="card">
                <div className="card-header">
                    <h1>🎓 Create New StudyGroup</h1>
                    <p className="text-muted">Fill in the details to create a new study group</p>
                </div>

                <div style={{ display: 'flex', gap: '1rem', marginBottom: '1.5rem' }}>
                    <button
                        onClick={() => navigate('/studygroups')}
                        className="btn btn-secondary"
                    >
                        ← Back to List
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
                        {error}
                    </div>
                )}

                <form onSubmit={handleSubmit}>
                    <div className="grid grid-2">
                        {/* Основная информация */}
                        <div>
                            <h3 style={{ marginBottom: '1rem', color: '#2c3e50' }}>📋 Basic Information</h3>

                            <div className="form-group">
                                <label className="form-label">Group Name *</label>
                                <input
                                    type="text"
                                    name="name"
                                    value={formData.name}
                                    onChange={handleChange}
                                    className="form-input"
                                    placeholder="Enter group name"
                                    required
                                />
                            </div>

                            <div className="form-group">
                                <label className="form-label">Coordinates</label>
                                <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
                                    <div>
                                        <label className="form-label">X</label>
                                        <input
                                            type="number"
                                            name="coordinates.x"
                                            value={formData.coordinates.x}
                                            onChange={handleChange}
                                            className="form-input"
                                            placeholder="X coordinate"
                                        />
                                    </div>
                                    <div>
                                        <label className="form-label">Y *</label>
                                        <input
                                            type="number"
                                            name="coordinates.y"
                                            value={formData.coordinates.y}
                                            onChange={handleChange}
                                            className="form-input"
                                            placeholder="Y coordinate"
                                            required
                                        />
                                    </div>
                                </div>
                            </div>

                            <div className="form-group">
                                <label className="form-label">Students Count</label>
                                <input
                                    type="number"
                                    name="studentsCount"
                                    value={formData.studentsCount}
                                    onChange={handleChange}
                                    className="form-input"
                                    placeholder="Number of students"
                                    min="1"
                                />
                            </div>
                        </div>

                        {/* Дополнительная информация */}
                        <div>
                            <h3 style={{ marginBottom: '1rem', color: '#2c3e50' }}>🎯 Additional Details</h3>

                            <div className="form-group">
                                <label className="form-label">Form of Education</label>
                                <select
                                    name="formOfEducation"
                                    value={formData.formOfEducation}
                                    onChange={handleChange}
                                    className="form-select"
                                >
                                    <option value="">Select education form</option>
                                    <option value="DISTANCE_EDUCATION">📚 Distance Education</option>
                                    <option value="FULL_TIME_EDUCATION">🎓 Full Time Education</option>
                                    <option value="EVENING_CLASSES">🌙 Evening Classes</option>
                                </select>
                            </div>

                            <div className="form-group">
                                <label className="form-label">Semester</label>
                                <select
                                    name="semesterEnum"
                                    value={formData.semesterEnum}
                                    onChange={handleChange}
                                    className="form-select"
                                >
                                    <option value="">Select semester</option>
                                    <option value="FIRST">1st Semester</option>
                                    <option value="THIRD">3rd Semester</option>
                                    <option value="SIXTH">6th Semester</option>
                                    <option value="SEVENTH">7th Semester</option>
                                    <option value="EIGHTH">8th Semester</option>
                                </select>
                            </div>

                            <div className="form-group">
                                <label className="form-label">Group Admin</label>
                                {personsLoading ? (
                                    <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                                        <div className="loading"></div>
                                        <span>Loading persons...</span>
                                    </div>
                                ) : (
                                    <select
                                        name="groupAdmin"
                                        value={formData.groupAdmin}
                                        onChange={handleChange}
                                        className="form-select"
                                    >
                                        <option value="">No administrator</option>
                                        {persons.map(person => (
                                            <option key={person.id} value={person.id}>
                                                👤 {person.name}
                                                {person.passportId && ` (${person.passportId})`}
                                                {person.nationality && ` - ${person.nationality}`}
                                            </option>
                                        ))}
                                    </select>
                                )}
                                {persons.length === 0 && !personsLoading && (
                                    <p className="text-muted mt-1">
                                        No persons available. <a href="/create-person" style={{ color: '#3498db' }}>Create one first</a>.
                                    </p>
                                )}
                            </div>
                        </div>
                    </div>

                    <div style={{ display: 'flex', gap: '1rem', marginTop: '2rem' }}>
                        <button
                            type="submit"
                            disabled={loading}
                            className="btn btn-primary"
                            style={{ minWidth: '200px' }}
                        >
                            {loading ? (
                                <>
                                    <div className="loading" style={{ width: '16px', height: '16px' }}></div>
                                    Creating...
                                </>
                            ) : (
                                '🎓 Create StudyGroup'
                            )}
                        </button>

                        <button
                            type="button"
                            onClick={() => navigate('/studygroups')}
                            className="btn btn-outline"
                        >
                            Cancel
                        </button>
                    </div>
                </form>
            </div>

            {/* Ссылка для создания нового Person если нет существующих */}
            {persons.length === 0 && !personsLoading && (
                <div className="card mt-3" style={{ backgroundColor: '#e8f4fd', borderColor: '#3498db' }}>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
                        <div style={{ fontSize: '2rem' }}>💡</div>
                        <div>
                            <h4 style={{ color: '#2c3e50', marginBottom: '0.5rem' }}>No Persons Available</h4>
                            <p style={{ color: '#5a6c7d', marginBottom: '1rem' }}>
                                You need to create at least one Person before you can assign them as a group administrator.
                            </p>
                            <button
                                onClick={() => navigate('/create-person')}
                                className="btn btn-success"
                            >
                                👤 Create New Person
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    )
}