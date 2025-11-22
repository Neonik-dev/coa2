import React, { useState } from 'react'
import { personsAPI } from '../services/api'
import { useNavigate } from 'react-router-dom'

export const CreatePersonPage = () => {
    const navigate = useNavigate()
    const [formData, setFormData] = useState({
        name: '',
        birthday: '',
        weight: '',
        passportId: '',
        nationality: ''
    })
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState('')

    const handleChange = (e) => {
        const { name, value } = e.target
        setFormData(prev => ({
            ...prev,
            [name]: value
        }))
    }

    const handleSubmit = async (e) => {
        e.preventDefault()
        setLoading(true)
        setError('')

        try {
            await personsAPI.create({
                ...formData,
                weight: formData.weight ? parseInt(formData.weight) : undefined,
                birthday: formData.birthday || null,
                passportId: formData.passportId.trim() || null,
                nationality: formData.nationality.trim() || null
            })
            navigate('/persons')
        } catch (err) {
            setError('Ошибка при создании Person: ' + (err.response?.data?.message || err.message))
            console.error(err)
        } finally {
            setLoading(false)
        }
    }

    return (
        <div className="fade-in">
            <div className="card">
                <div className="card-header">
                    <h1>👤 Create New Person</h1>
                    <p className="text-muted">Fill in the details to create a new person</p>
                </div>

                <div style={{ display: 'flex', gap: '1rem', marginBottom: '1.5rem' }}>
                    <button
                        onClick={() => navigate('/persons')}
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
                        <div>
                            <h3 style={{ marginBottom: '1rem', color: '#2c3e50' }}>📋 Basic Information</h3>

                            <div className="form-group">
                                <label className="form-label">Full Name *</label>
                                <input
                                    type="text"
                                    name="name"
                                    value={formData.name}
                                    onChange={handleChange}
                                    className="form-input"
                                    placeholder="Enter full name"
                                    required
                                />
                            </div>

                            <div className="form-group">
                                <label className="form-label">Birthday</label>
                                <input
                                    type="date"
                                    name="birthday"
                                    value={formData.birthday}
                                    onChange={handleChange}
                                    className="form-input"
                                />
                            </div>

                            <div className="form-group">
                                <label className="form-label">Weight *</label>
                                <input
                                    type="number"
                                    name="weight"
                                    value={formData.weight}
                                    onChange={handleChange}
                                    className="form-input"
                                    placeholder="Enter weight"
                                    min="1"
                                    required
                                />
                            </div>
                        </div>

                        <div>
                            <h3 style={{ marginBottom: '1rem', color: '#2c3e50' }}>🎫 Additional Details</h3>

                            <div className="form-group">
                                <label className="form-label">Passport ID</label>
                                <input
                                    type="text"
                                    name="passportId"
                                    value={formData.passportId}
                                    onChange={handleChange}
                                    className="form-input"
                                    placeholder="Enter passport ID"
                                />
                            </div>

                            <div className="form-group">
                                <label className="form-label">Nationality</label>
                                <select
                                    name="nationality"
                                    value={formData.nationality}
                                    onChange={handleChange}
                                    className="form-select"
                                >
                                    <option value="">Select nationality</option>
                                    <option value="FRANCE">🇫🇷 FRANCE</option>
                                    <option value="CHINA">🇨🇳 CHINA</option>
                                    <option value="INDIA">🇮🇳 INDIA</option>
                                </select>
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
                                '👤 Create Person'
                            )}
                        </button>

                        <button
                            type="button"
                            onClick={() => navigate('/persons')}
                            className="btn btn-outline"
                        >
                            Cancel
                        </button>
                    </div>
                </form>
            </div>
        </div>
    )
}