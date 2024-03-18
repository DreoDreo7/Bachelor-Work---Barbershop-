import React, { createContext, useState, useEffect } from "react";
import axios from "axios";

export const AuthContext = createContext();

export const AuthContextProvider = (props) => {
  const storedUser = JSON.parse(localStorage.getItem("user") || "null");
  const [user, setUser] = useState(storedUser);

  useEffect(() => {
    if (user && user.accessToken) {
      localStorage.setItem("user", JSON.stringify(user));
      axios.defaults.headers.common["Authorization"] =
        "Bearer " + user.accessToken;
    } else {
      localStorage.removeItem("user");
      delete axios.defaults.headers.common["Authorization"];
    }
  }, [user]);

  const registerUser = async (
    firstName,
    lastName,
    email,
    phoneNumber,
    password
  ) => {
    try {
      await axios.post("http://localhost:8080/api/users/registration", {
        firstName,
        lastName,
        email,
        phoneNumber,
        password,
      });
    } catch (error) {
      console.error("Failed to register a client:", error);
      throw new Error(
        error.response?.data?.message || "An unknown error occurred"
      );
    }
  };

  // const registerBarber = async (
  //   password,
  //   barberCode
  // ) => {
  //   try {
  //     await axios.post("http://localhost:8080/api/users/registrationBarber", {
  //       password,
  //       barberCode,
  //     });
  //   } catch (error) {
  //     console.error("Failed to register a barber:", error);
  //     throw new Error(
  //       error.response?.data?.message || "An unknown error occurred"
  //     );
  //   }
  // };

  const loginUser = async (email, password) => {
    try {
      const response = await axios.post(
        "http://localhost:8080/api/users/login",
        {
          email,
          password,
        }
      );
      setUser(response.data);
      return response.data;
    } catch (error) {
      if (
        error.response &&
        error.response.data === "Invalid email and/or password"
      ) {
        return "Invalid email and/or password";
      } else {
        console.error("Unexpected error:", error);
      }
    }
  };

  const logoutUser = () => {
    setUser(null);
  };

  const getUser = async (email) => {
    try {
      const response = await axios.get(
        `http://localhost:8080/api/users/${email}`
      );
      if (response.status === 200) {
        setUser(response.data);
      }
    } catch (error) {
      console.error("Failed to get user:", error);
    }
  };

  return (
    <AuthContext.Provider
      value={{ user, registerUser, loginUser, getUser, logoutUser }}
    >
      {props.children}
    </AuthContext.Provider>
  );
};
