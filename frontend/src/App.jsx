import React from 'react'
import { Routes, Route, Link } from 'react-router-dom'
import Navigation from './components/Navigation'
import PersonsPage from './pages/PersonsPage'
import StudyGroupsPage from './pages/StudyGroupsPage'
import StudyGroupDetailPage from './pages/StudyGroupDetailPage'
import MinCreationDatePage from './pages/MinCreationDatePage'
import GroupByIdPage from './pages/GroupByIdPage'
import FormOfEducationLtPage from './pages/FormOfEducationLtPage'
import ExpelAllPage from './pages/ExpelAllPage'
import ChangeEduFormPage from './pages/ChangeEduFormPage'
import {CreatePersonPage} from "./pages/CreatePersonPage.jsx";
import {CreateStudyGroupPage} from "./pages/CreateStudyGroupPage.jsx";
import {UpdateStudyGroupPage} from "./pages/UpdateStudyGroupPage.jsx";

function App() {
    return (
        <div style={{minHeight: '100vh', background: 'linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%)'}}>
            <Navigation/>
            <div className="container" style={{paddingTop: '2rem', paddingBottom: '2rem'}}>
                <Routes>
                    <Route path="/" element={<HomePage/>}/>
                    <Route path="/persons" element={<PersonsPage/>}/>
                    <Route path="/studygroups" element={<StudyGroupsPage/>}/>
                    <Route path="/studygroups/:id" element={<StudyGroupDetailPage/>}/>
                    <Route path="/min-creation-date" element={<MinCreationDatePage/>}/>
                    <Route path="/group-by-id" element={<GroupByIdPage/>}/>

                    <Route path="/form-of-education-lt" element={<FormOfEducationLtPage/>}/>
                    <Route path="/form-of-education-lt/:value" element={<FormOfEducationLtPage/>}/>

                    <Route path="/expel-all" element={<ExpelAllPage/>}/>
                    <Route path="/expel-all/:groupId" element={<ExpelAllPage/>}/>

                    <Route path="/change-edu-form" element={<ChangeEduFormPage/>}/>
                    <Route path="/change-edu-form/:groupId/:newForm" element={<ChangeEduFormPage/>}/>

                    <Route path="/create-person" element={<CreatePersonPage/>}/>
                    <Route path="/create-studygroups" element={<CreateStudyGroupPage/>}/>
                    <Route path="/update-studygroups" element={<UpdateStudyGroupPage/>}/>
                    <Route path="/update-studygroups/:id" element={<UpdateStudyGroupPage/>}/>
                </Routes>
            </div>
        </div>
    )
}

function HomePage() {
    return (
        <div className="fade-in">
            <div className="card text-center">
                <h1 style={{ color: '#2c3e50', marginBottom: '1rem', fontSize: '2.5rem' }}>
                    🎓 StudyGroup Collection API
                </h1>
                <p style={{ color: '#7f8c8d', fontSize: '1.2rem', marginBottom: '2rem' }}>
                    Добро пожаловать в систему управления учебными группами
                </p>

                <div className="grid grid-3 mt-3">
                    <div className="card text-center">
                        <div style={{ fontSize: '3rem', marginBottom: '1rem' }}>👥</div>
                        <h3>Persons Management</h3>
                        <p className="text-muted">Управление студентами и администраторами</p>
                    </div>

                    <div className="card text-center">
                        <div style={{ fontSize: '3rem', marginBottom: '1rem' }}>🎓</div>
                        <h3>Study Groups</h3>
                        <p className="text-muted">CRUD операции с учебными группами</p>
                    </div>

                    <div className="card text-center">
                        <div style={{ fontSize: '3rem', marginBottom: '1rem' }}>📊</div>
                        <h3>Analytics</h3>
                        <p className="text-muted">Статистика и аналитика данных</p>
                    </div>
                </div>

                <div style={{ marginTop: '2rem', padding: '2rem', backgroundColor: '#f8f9fa', borderRadius: '12px' }}>
                    <h3 style={{ marginBottom: '1rem', color: '#2c3e50' }}>Возможности системы:</h3>
                    <div className="grid grid-2">
                        <ul style={{ textAlign: 'left', color: '#5a6c7d' }}>
                            <li>✅ Полное управление учебными группами</li>
                            <li>✅ Управление персонами (студенты, администраторы)</li>
                            <li>✅ Расширенная фильтрация и сортировка</li>
                        </ul>
                        <ul style={{ textAlign: 'left', color: '#5a6c7d' }}>
                            <li>✅ Специальные операции с группами</li>
                            <li>✅ Статистика и группировка данных</li>
                            <li>✅ Адаптивный современный интерфейс</li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default App