import React from 'react'
import { Link } from 'react-router-dom'

const Navigation = () => {
    return (
        <nav className="navbar">
            <div className="container">
                <div className="nav-content">
                    <div className="nav-links">
                        <Link to="/" className="nav-link" style={{ fontSize: '1.1rem', fontWeight: 'bold' }}>
                            🏠 StudyGroup System
                        </Link>

                        <div className="nav-section">
                            <strong>👥 Persons</strong>
                            <Link to="/persons" className="nav-link">📋 List Persons</Link>
                            <Link to="/create-person" className="nav-link">➕ Create Person</Link>
                        </div>

                        <div className="nav-section">
                            <strong>🎓 StudyGroups</strong>
                            <Link to="/studygroups" className="nav-link">📋 List Groups</Link>
                            <Link to="/create-studygroups" className="nav-link">➕ Create Group</Link>
                        </div>

                        <div className="nav-section">
                            <strong>📊 Analytics</strong>
                            <Link to="/min-creation-date" className="nav-link">📅 Oldest Group</Link>
                            <Link to="/group-by-id" className="nav-link">📈 Group Stats</Link>
                            <Link to="/form-of-education-lt" className="nav-link">🎓 Filter by Form</Link>
                        </div>

                        <div className="nav-section">
                            <strong>⚡ Operations</strong>
                            <Link to="/expel-all" className="nav-link">🎯 Expel Students</Link>
                            <Link to="/change-edu-form" className="nav-link">🔄 Change Format</Link>
                        </div>
                    </div>
                </div>

                <div className="quick-links">
                    <strong>Quick Actions:</strong>
                    <div className="quick-links-content">
                        <Link to="/create-person" className="btn btn-outline" style={{ color: 'white', borderColor: 'white' }}>
                            ➕ New Person
                        </Link>
                        <Link to="/create-studygroups" className="btn btn-outline" style={{ color: 'white', borderColor: 'white' }}>
                            ➕ New Group
                        </Link>
                        <Link to="/studygroups" className="btn btn-outline" style={{ color: 'white', borderColor: 'white' }}>
                            📋 All Groups
                        </Link>
                        <Link to="/persons" className="btn btn-outline" style={{ color: 'white', borderColor: 'white' }}>
                            👥 All Persons
                        </Link>
                    </div>
                </div>
            </div>
        </nav>
    )
}

export default Navigation