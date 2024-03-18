import React, { useState, useContext } from "react";
import { Link, useNavigate } from "react-router-dom";
import { AuthContext } from "../services/AuthContext";

const RegisterClientPage = () => {
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [phoneNumber, setPhoneNumber] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [barberCode, setBarberCode] = useState("");
  const { registerUser } = useContext(AuthContext);
  const navigate = useNavigate();

  const [barberCheckbox, setBarberCheckbox] = useState(false);

  const [focusedField, setFocusedField] = useState("");
  const [isPasswordVisible, setPasswordVisibility] = useState(false);
  const [isConfirmPasswordVisible, setConfirmPasswordVisibility] =
    useState(false);

  const [firstNameError, setFirstNameError] = useState("");
  const [lastNameError, setLastNameError] = useState("");
  const [emailError, setEmailError] = useState("");
  const [phoneNumberError, setPhoneNumberError] = useState("");
  const [passwordError, setPasswordError] = useState("");
  const [confirmPasswordError, setConfirmPasswordError] = useState("");
  const [barberCodeError, setBarberCodeError] = useState(""); //eslint-disable-line


  const handleSubmit = async (event) => {
    event.preventDefault();

    const errors = {};

    if (!/^[a-zA-Zа-яА-Я]{3,255}$/.test(firstName))
      errors.firstName = "Invalid first name";
    if (!/^[a-zA-Zа-яА-Я]{3,255}$/.test(lastName))
      errors.lastName = "Invalid last name";
    if (!/^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)*(\.[a-zA-Z]{2,})$/.test(email))
      errors.email = "Invalid email";
    if (!/^\d{10}$/.test(phoneNumber))
      errors.phoneNumber = "Invalid mobile number";
    if (!/^[a-zA-Z0-9]{6,20}$/.test(password))
      errors.password = "Invalid password";
    if (password !== confirmPassword)
      errors.confirmPassword = "Password don't match";

    setFirstNameError(errors.firstName || "");
    setLastNameError(errors.lastName || "");
    setEmailError(errors.email || "");
    setPhoneNumberError(errors.phoneNumber || "");
    setPasswordError(errors.password || "");
    setConfirmPasswordError(errors.confirmPassword || "");

    if (Object.keys(errors).length === 0) {
      try {
        await registerUser(firstName, lastName, email, phoneNumber, password);
        alert("Successful registration, " `${firstName}`);
        navigate("/login");
      } catch (error) {
        console.error(error);

        if (error.message.includes("Email is already used.")) {
          alert("Email is already used.");
        } else if (
          error.message.includes("Mobile number is already used.")
        ) {
          alert("Mobile number is already used.");
        } else {
          alert("Registration error. Please try again later.");
        }
      }
    }
  };

  const handleInputChange = (setter, errorSetter, value) => {
    setter(value);
    errorSetter("");
  };

  const inputStyle = (error) => ({
    borderColor: error ? "red" : "initial",
  });

  const errorMessages = {
    firstName: "*First name must contain only letters and at least 3 characters",
    lastName: "*Last name must contain only letters and at least 3 characters",
    email: "*Enter a valid email address",
    phoneNumber: "*Mobile number must contain only digits.",
    password:
      "*The password can only contain letters and/or numbers " +  
      "and must be a minimum of 6 characters",
    confirmPassword: "*Must match with password",
  };

  const togglePasswordVisibility = () => {
    setPasswordVisibility(!isPasswordVisible);
  };

  const toggleConfirmPasswordVisibility = () => {
    setConfirmPasswordVisibility(!isConfirmPasswordVisible);
  };

  const toggleBarberCodeCheckbox = () => {
    setBarberCheckbox(!barberCheckbox);
  };

  return (
    <div className="container">
      <div className="card p-5 border-0">
        <div className="d-flex justify-content-center align-items-center">
          <h2>Sign up</h2>
        </div>

        <form onSubmit={handleSubmit}>
        {!barberCheckbox && (
          <>
            <div className="form-group">
              <label htmlFor="firstName">First Name</label>
              <input
                style={inputStyle(firstNameError)}
                type="text"
                className="form-control"
                id="firstName"
                value={firstName}
                onChange={(e) =>
                  handleInputChange(setFirstName, setFirstNameError, e.target.value)
                }
                onFocus={() => setFocusedField("firstName")}
                onBlur={() => setFocusedField("")}
                autoComplete="off"
              />
              <small style={{ color: firstNameError ? "red" : "gray" }}>
                {focusedField === "firstName"
                  ? errorMessages.firstName
                  : firstNameError}
              </small>
            </div>

            <div className="form-group">
              <label htmlFor="lastName">Last Name</label>
              <input
                style={inputStyle(lastNameError)}
                type="text"
                className="form-control"
                id="lastName"
                value={lastName}
                onChange={(e) =>
                  handleInputChange(setLastName, setLastNameError, e.target.value)
                }
                onFocus={() => setFocusedField("lastName")}
                onBlur={() => setFocusedField("")}
                autoComplete="off"
              />
              <small style={{ color: lastNameError ? "red" : "gray" }}>
                {focusedField === "lastName"
                  ? errorMessages.lastName
                  : lastNameError}
              </small>
            </div>

            <div className="form-group">
              <label htmlFor="phoneNumber">Mobile Number</label>
              <input
                style={inputStyle(phoneNumberError)}
                type="text"
                className="form-control"
                id="phoneNumber"
                value={phoneNumber}
                onChange={(e) =>
                  handleInputChange(setPhoneNumber, setPhoneNumberError, e.target.value)
                }
                onFocus={() => setFocusedField("phoneNumber")}
                onBlur={() => setFocusedField("")}
                autoComplete="off"
              />
              <small style={{ color: phoneNumberError ? "red" : "gray" }}>
                {focusedField === "phoneNumber"
                  ? errorMessages.phoneNumber
                  : phoneNumberError}
              </small>
            </div>
          </>
          )} 

          <div className="form-group">
              <label htmlFor="email">Email</label>
              <input
                style={inputStyle(emailError)}
                type="email"
                className="form-control"
                id="email"
                value={email}
                onChange={(e) =>
                  handleInputChange(setEmail, setEmailError, e.target.value)
                }
                onFocus={() => setFocusedField("email")}
                onBlur={() => setFocusedField("")}
                autoComplete="off"
              />
              <small style={{ color: emailError ? "red" : "gray" }}>
                {focusedField === "email" ? errorMessages.email : emailError}
              </small>
            </div>

          <div className="form-group">
            <label htmlFor="password">Password</label>
            <div className="input-group">
              <input
                style={inputStyle(passwordError)}
                type={isPasswordVisible ? "text" : "password"}
                className="form-control"
                id="password"
                value={password}
                onChange={(e) =>
                  handleInputChange(setPassword, setPasswordError, e.target.value)
                }
                onFocus={() => setFocusedField("password")}
                onBlur={() => setFocusedField("")}
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
                    {!isPasswordVisible ? (
                      <i className="fa fa-eye-slash"></i>
                    ) : (
                      <i className="fa fa-eye"></i>
                    )}
                  </button>
                </span>
              </div>
            </div>
            <small style={{ color: passwordError ? "red" : "gray" }}>
              {focusedField === "password"
                ? errorMessages.password
                : passwordError}
            </small>
          </div>

          <div className="form-group">
            <label htmlFor="confirmPassword">Confirm the password</label>
            <div className="input-group">
              <input
                style={inputStyle(confirmPasswordError)}
                type={isConfirmPasswordVisible ? "text" : "password"}
                className="form-control"
                id="confirmPassword"
                value={confirmPassword}
                onChange={(e) => {
                  handleInputChange(
                    setConfirmPassword,
                    setConfirmPasswordError,
                    e.target.value
                  );
                }}
                onFocus={() => setFocusedField("confirmPassword")}
                onBlur={() => setFocusedField("")}
                autoComplete="off"
              />
              <div
                className="input-group-append"
                style={{ height: `38px`, borderColor: "black" }}
              >
                <span className="input-group-text bg-white border-left-0">
                  <button
                    type="button"
                    onClick={toggleConfirmPasswordVisibility}
                    className="btn"
                    style={{
                      textDecoration: `none`,
                      background: "transparent",
                    }}
                  >
                    {!isConfirmPasswordVisible ? (
                      <i className="fa fa-eye-slash"></i>
                    ) : (
                      <i className="fa fa-eye"></i>
                    )}
                  </button>
                </span>
              </div>
            </div>
            <small style={{ color: confirmPasswordError ? "red" : "gray" }}>
              {focusedField === "confirmPassword"
                ? errorMessages.confirmPassword
                : confirmPasswordError}
            </small>
          </div>  

          <div className="form-group form-check">
            <input
              type="checkbox"
              className="form-check-input"
              id="showAdditionalFields"
              onChange={toggleBarberCodeCheckbox}
            />
            <label className="form-check-label" htmlFor="showAdditionalFields">
              Are you a barber?
            </label>
          </div>

          {barberCheckbox && (
            <div className="form-group">
              <label htmlFor="barberCode">Barber Code</label>
              <div className="input-group">
                <input
                  style={inputStyle(barberCodeError)}
                  type="barberCode"
                  className="form-control"
                  id="barberCode"
                  value={barberCode}
                  onChange={(e) => {
                    handleInputChange(setBarberCode, setBarberCodeError, e.target.value);
                  }}
                  onFocus={() => setFocusedField("barberCode")}
                  onBlur={() => setFocusedField("")}
                  autoComplete="off"
                />
              </div>
              <small style={{ color: barberCodeError ? "red" : "gray" }}>
                {focusedField === "barberCode" 
                  ? barberCodeError 
                  : ""}
              </small>
            </div>
          )}        

          <button
            type="submit"
            disabled={
              !firstName ||
              !lastName ||
              !email ||
              !phoneNumber ||
              !password ||
              !confirmPassword ||
              firstNameError ||
              lastNameError ||
              emailError ||
              phoneNumberError ||
              passwordError ||
              confirmPasswordError
            }
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
            Sign up
          </button>

          <p
            className="text-center"
            style={{ marginTop: "10px", fontSize: "20px" }}
          >
            Do you have a registration?
            <Link
              to="/login"
              style={{
                color: "black",
                textDecoration: "underline",
                marginLeft: "5px",
              }}
            >
              Login here
            </Link>
          </p>
        </form>
      </div>
    </div>
  );
};

export default RegisterClientPage;
