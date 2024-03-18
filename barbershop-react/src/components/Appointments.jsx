import React, { useState, useEffect, useContext } from "react";
import { AuthContext } from "../services/AuthContext";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const Appointments = () => {
  const { user } = useContext(AuthContext);
  const navigate = useNavigate();
  
  const [appointments, setAppointments] = useState([]);

  useEffect(() => {
    if (!user) {
      navigate("/login");
    }
  }, [user, navigate]);

  const fetchData = async () => {
    try {
      const response = await axios.get(
        `http://localhost:8080/api/users/appointments`,
        {
          headers: {
            Authorization: "Bearer " + user.accessToken,
          },
        }
      );
      setAppointments(response.data);
    } catch (error) {
      console.error("Error fetching appointments:", error);
    }
  };

  useEffect(() => {
    if (user) {
      fetchData();
    }
  }, [user]); // eslint-disable-line react-hooks/exhaustive-deps

  const getAppointmentStatus = (appointmentDate, appointmentTime) => {
    const now = new Date();
    const appointmentDateTime = new Date(
      `${appointmentDate}T${appointmentTime}.000Z`
    );
    appointmentDateTime.setHours(appointmentDateTime.getHours() - 2);
    let twoHoursBeforeAppointment = appointmentDateTime;
    if (now >= appointmentDateTime) {
      return "past";
    } else if (
      now >=
      twoHoursBeforeAppointment.setHours(appointmentDateTime.getHours() - 2)
    ) {
      return "imminent";
    } else {
      return "future";
    }
  };

  const cancelAppointment = async (appointmentId, status) => {
    if (status === "past") {
      alert("You can't change previous appointment.");
      return;
    }

    if (status === "imminent") {
      alert("You can't change an appointment if there is less than 2 hours.");
      return;
    }

    const confirmed = window.confirm(
      "Are you sure you want to cancel this appointment?"
    );

    if (confirmed) {
      try {
        await axios.delete(
          `http://localhost:8080/api/users/appointments/${appointmentId}`,
          {
            headers: {
              Authorization: "Bearer " + user.accessToken,
            },
          }
        );
        fetchData();
        alert("Your appointment is successfully canceled.");
      } catch (error) {
        if (error.response) {
          switch (error.response.status) {
            case 409:
              alert(error.response.data);
              break;
            case 403:
              alert(
                "You can't change a appointment if there is less than 2 hours."
              );
              break;
            case 401:
              alert("Unauthorized user.");
              break;
            default:
              console.error("Failed to cancel appointment:", error);
              alert("An unexpected error occurred. Please try again later.");
          }
        } else {
          console.error("Failed to cancel appointment:", error);
        }
      }
    }
  };

  const formatTime = (timeString) => {
    const [hour, minute] = timeString.split(":");
    return `${String(hour).padStart(2, "0")}:${String(minute).padStart(
      2,
      "0"
    )}`;
  };

  const getLocalizedService = (serviceType) => {
    switch (serviceType) {
      case "HAIR":
        return "Haircut";
      case "BEARD":
        return "Beard";
      case "HAIR_AND_BEARD":
        return "Haircut and Beard";
      default:
        return serviceType;
    }
  };

  const renderCancelButton = (appointment) => {
    const status = getAppointmentStatus(appointment.date, appointment.time);
    if (status === "past") {
      return (
        <button
          className="btn btn-dark w-100"
          style={{
            fontWeight: "bold",
            backgroundColor: "green",
            borderColor: "#212529",
            borderWidth: "3px",
          }}
        >
          Past Appointment{" "}
        </button>
      );
    } else if (status === "imminent") {
      return (
        <button
          className="btn btn-dark w-100"
          style={{
            fontWeight: "bold",
            backgroundColor: "dark",
            borderColor: "#212529",
            borderWidth: "3px",
          }}
        >
          Less than 2 hours{" "}
        </button>
      );
    } else {
      return (
        <button
          onClick={() =>
            cancelAppointment(
              appointment.id,
              appointment.date,
              appointment.time
            )
          }
          className="btn btn-dark w-100"
          style={{
            fontWeight: "bold",
            backgroundColor: "#dc3545",
            borderColor: "#212529",
            borderWidth: "3px",
          }}
        >
          Cancel{" "}
        </button>
      );
    }
  };

  return (
    <div>
      <h1>History of appointments:</h1>{" "}
      <div className="card p-4 border-0">
        <div className="row">
          <div className="col-md-12">
            <p className="border p-3">
              <i className="fas fa-info-circle"></i> You can cancel your
              appointment{" "}
              <strong style={{ textDecoration: `underline` }}>
                at the latest 2 hours before the reserved time.
              </strong>{" "}
              In most cases, professionals work for themselves and undergo
              serious losses with every untimely cancellation or no-show of a
              client.
            </p>{" "}
          </div>
        </div>

        <table className="table" style={{ border: "2px solid black" }}>
          <thead>
            <tr style={{ borderBottom: "2px solid black" }}>
              <th
                style={{
                  textAlign: "center",
                  borderRight: "2px solid black",
                  borderBottom: "2px solid black",
                }}
              >
                Type of haircut:{" "}
              </th>
              <th
                style={{
                  textAlign: "center",
                  borderRight: "2px solid black",
                  borderBottom: "2px solid black",
                }}
              >
                Date:{" "}
              </th>{" "}
              <th
                style={{
                  textAlign: "center",
                  borderRight: "2px solid black",
                  borderBottom: "2px solid black",
                }}
              >
                Time:{" "}
              </th>{" "}
              <th
                style={{ textAlign: "center", borderBottom: "2px solid black" }}
              ></th>
            </tr>{" "}
          </thead>{" "}

          <tbody>
            {Array.isArray(appointments) && appointments.length > 0 ? (
              appointments.map((appointment) => {
                const status = getAppointmentStatus(
                  appointment.date,
                  appointment.time
                );
                const isPastAppointment = status === "past";
                return (
                  <tr
                    key={appointment.id}
                    style={{
                      borderBottom: "1px solid lightgrey",
                      backgroundColor: isPastAppointment ? "grey" : "white",
                      textAlign: "center",
                    }}
                  >
                    <td style={{ borderBottom: "1px solid lightgrey" }}>
                      {" "}
                      {appointment.service &&
                        getLocalizedService(appointment.service)}{" "}
                    </td>
                    <td style={{ borderBottom: "1px solid lightgrey" }}>
                      {new Date(appointment.date).toLocaleDateString(`en-GB`)}
                    </td>
                    <td style={{ borderBottom: "1px solid lightgrey" }}>
                      {appointment.time ? formatTime(appointment.time) : `N/A`}{" "}
                    </td>
                    <td>{renderCancelButton(appointment)} </td>
                  </tr>
                );
              })
            ) : (
              <tr>
                <td
                  colSpan="4"
                  style={{
                    textAlign: "center",
                    fontWeight: "bold",
                    fontSize: "20px",
                  }}
                >
                  There are no appointments.{" "}
                </td>
              </tr>
            )}
          </tbody>{" "}
        </table>
      </div>{" "}
    </div>
  );
};

export default Appointments;
