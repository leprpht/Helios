import Navbar from './components/Navbar/Navbar'
import { Suspense } from 'react';
import { Route, Routes } from 'react-router';
import MainPage from './pages/MainPage/MainPage';
import './App.scss'
import AuthPage from './pages/AuthPage/AuthPage';

function App() {


  return (
    <>
      <Navbar />
      <Suspense fallback={<div>Loading...</div>}>
        <Routes>
          <Route path="/" element={<MainPage />} />
          <Route path="/auth" element={<AuthPage />} />
        </Routes>
      </Suspense>
    </>
  )
}

export default App
