import React, { useState, useContext } from "react";
import { Link, useNavigate } from "react-router-dom";
import { AuthContext } from "../services/AuthContext";

const LoginPage = () => {
  const [formData, setFormData] = useState({
    email: "",
    password: "",
  });
  const [error, setError] = useState(null);
  const { loginUser } = useContext(AuthContext);
  const navigate = useNavigate();

  const [isPasswordVisible, setPasswordVisibility] = useState(false);

  const handleSubmit = async (event) => {
    event.preventDefault();

    setError(null);
    try {
      const response = await loginUser(formData.email, formData.password);
      if (response && response.status === "string") {
        setError(response);
      } else if (response && response === "Invalid email and/or password") {
        alert("Invalid email and/or password");
      } else {
        alert(`Welcome, ${response.firstName} ${response.lastName}!`);
        navigate("/");
      }
    } catch (error) {
      setError("An error occurred while logging. Please try again.");
      console.error(error);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const togglePasswordVisibility = () => {
    setPasswordVisibility((prevVisibility) => !prevVisibility);
  };

  return (
    <div className="container">
      <div className="card p-5 border-0">
        <div className="d-flex justify-content-center align-items-center">
          <h2>Login</h2>
        </div>

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Email</label>
            <input
              type="email"
              name="email"
              className="form-control"
              value={formData.email}
              onChange={handleChange}
              required
              autoComplete="off"
            />
          </div>
          {error && <p className="text-danger">{error}</p>}
          <div className="form-group">
            <label>Password</label>
            <div className="input-group">
              <input
                type={isPasswordVisible ? "text" : "password"}
                name="password"
                className="form-control"
                value={formData.password}
                onChange={handleChange}
                required
                autoComplete="off"
              />
              <div
                className="input-group-append"
                style={{ height: `38px`, borderColor: "black" }}
              >
                <span className="input-group-text bg-white border-left-0">
                  <button
                    type="button"
                    onClick={togglePasswordVisibility}
                    className="btn"
                    style={{
                      textDecoration: `none`,
                      background: "transparent",
                    }}
                  >
                    {isPasswordVisible ? (
                      <i className="fa fa-eye-slash"></i>
                    ) : (
                      <i className="fa fa-eye"></i>
                    )}
                  </button>
                </span>
              </div>
            </div>
          </div>

          <button
            type="submit"
            disabled={!formData.email || !formData.password}
            className="btn btn-primary"
            style={{
              fontSize: "25px",
              borderRadius: "50px",
              padding: "10px 20px",
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
              backgroundColor: "#343a40",
              borderColor: "#212529",
              borderWidth: "5px",
            }}
          >
            Login
          </button>
        </form>
        
        <p
          className="text-center"
          style={{ marginTop: "10px", fontSize: "20px" }}
        >
          Still not registered?
          <Link
            to="/register"
            style={{
              color: "black",
              textDecoration: "underline",
              marginLeft: "5px",
            }}
          >
            Sign up here
          </Link>
        </p>
      </div>
    </div>
  );
};

export default LoginPage;
