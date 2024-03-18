import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

const ErrorPage = () => {
  const navigate = useNavigate();
  const [timeRemaining, setTimeRemaining] = useState(5);
  
  useEffect(() => {
    if (timeRemaining > 0) {
      const timerId = setTimeout(() => {
        setTimeRemaining((prevTime) => prevTime - 1);
      }, 1000);
      return () => clearTimeout(timerId);
    } else {
      navigate("/");
    }
  }, [navigate, timeRemaining]);

  return (
    <div
      className="container d-flex flex-column justify-content-center align-items-center"
      style={{ minHeight: "80vh" }}
    >
      <p
        style={{
          fontSize: "2em",
          textAlign: "center",
          fontWeight: "bold",
          marginBottom: "2em",
        }}
      >
        You have no access to this page{" "}
      </p>

      <p
        style={{
          fontSize: "1em",
          textAlign: "center",
          marginBottom: "2em",
        }}
      >
        You will be forwarded to the start page.{" "}
      </p>

      <p
        style={{
          fontSize: "1.2em",
          textAlign: "center",
          marginBottom: "2em",
          border: "2px solid black",
          padding: "10px",
          borderRadius: "5px",
        }}
      >
        Remaining Time: {timeRemaining} sec.{" "}
      </p>
    </div>
  );
};

export default ErrorPage;
