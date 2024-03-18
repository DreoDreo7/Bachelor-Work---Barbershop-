import React from "react";
import "./App.css";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import HomePage from "./components/HomePage";
import LoginPage from "./components/LoginPage";
import RegisterPage from "./components/RegisterPage";
import Navbar from "./components/NavBar";
import AdminDashboard from "./components/AdminDashboard";
import Appointments from "./components/Appointments";
import MakeAppointments from "./components/MakeAppointment";
import Settings from "./components/Settings";
import { AuthContextProvider } from "./services/AuthContext";
import "bootstrap/dist/js/bootstrap.bundle.min";
import ErrorPage from "./components/ErrorPage";

function App() {
  return (
    <AuthContextProvider>
      <Router>
        <Navbar />
        <div
          className="container-flex"
          style={{
            marginTop: "10px",
          }}
        >
          <Routes>
            <Route exact path="/" element={<HomePage />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />
            <Route path="/admin" element={<AdminDashboard />} />
            <Route path="/appointments" element={<Appointments />} />
            <Route path="/makeAppointment" element={<MakeAppointments />} />
            <Route path="/settings" element={<Settings />} />
            <Route path="/error" element={<ErrorPage />} />
          </Routes>
        </div>
      </Router>
    </AuthContextProvider>
  );
}

export default App;
