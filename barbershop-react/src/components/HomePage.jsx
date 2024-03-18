import React, { useContext, useState, useEffect, useRef } from "react";
import { AuthContext } from "../services/AuthContext";
import { useNavigate } from "react-router-dom";
import img1 from "../assets/img-1.jpeg";
import img2 from "../assets/img-2.jpeg";
import img3 from "../assets/img-3.jpeg";

const images = [img1, img2, img3];

const HomePage = () => {
  const { user } = useContext(AuthContext);
  const navigate = useNavigate();
  const [activeImageIndex, setActiveImageIndex] = useState(0);
  const autoPlayRef = useRef();
  const phoneRef = useRef(null);
  const googleMapsURL = 
    "https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d356.7660545601036!2d23.34326329170094!3d42.65353885085638!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x40aa85ea2a8cb5c1%3A0xd6a52825c554f55e!2sStudio%20The%20One!5e0!3m2!1sen!2sus!4v1693747645537!5m2!1sen!2sus";

  useEffect(() => {
    autoPlayRef.current = nextSlide;
  });

  useEffect(() => {
    const play = () => {
      autoPlayRef.current();
    };

    const interval = setInterval(play, 5000);
    return () => clearInterval(interval);
  }, []);

  const handleAppointmentClick = () => {
    navigate("/makeAppointment");
  };

  const nextSlide = () => {
    setActiveImageIndex((prevIndex) => (prevIndex + 1) % images.length);
  };

  return (
    <div
      className="container-flex"
      style={{
        maxWidth: "1600px",
        width: "100%",
        margin: "0 auto",
        backgroundColor: "white",
      }}
    >
      <div
        style={{
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
        }}
      >
        <img
          style={{
            width: "100%",
            maxWidth: "1600px",
            height: "100%",
            maxHeight: "500px",
            objectFit: "cover",
          }}
          src={images[activeImageIndex]}
          alt=""
        />
        <div
          style={{
            display: "flex",
            justifyContent: "center",
            gap: "10px",
            marginTop: "10px",
          }}
        >
          {images.map((_, index) => (
            <div
              key={index}
              onClick={() => setActiveImageIndex(index)}
              style={{
                height: "5px",
                width: "100px",
                background: activeImageIndex === index ? "#333" : "#ccc",
                cursor: "pointer",
              }}
            />
          ))}
        </div>
      </div>
      <div className="row justify-content-center">
        <div className="col-lg-5 text-center">
          {!user || !user.roles.includes("ROLE_ADMIN") ? (
            <button
              className="btn btn-primary mt-3"
              onClick={handleAppointmentClick}
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
              Make an appointment
            </button>
          ) : null}
        </div>
      </div>
      <div
        className="row justify-content-center"
        style={{ maxWidth: "1600px", margin: "0 auto", marginTop: "20px" }}
      >
        <div className="col-xl-3 col-md-6">
          <div
            className="card border-0 d-flex flex-column justify-content-center align-items-center p-3"
            style={{ width: "400px" }}
          >
            <h4>For Studio The One</h4>
            <p
              style={{
                textAlign: "justify",
                marginTop: "10px",
              }}
            >
              Studio The One offers innovations in barbering and men's
              styling for you. We have created a place with a very modern look
              and a captivating environment, with your unforgettable
              experience. Our team of specialists works professionally and
              precise, according to your taste. Any vision you desire is completely
              achievable with us. We look forward to introducing you to our wide
              range of services and techniques selected for you. We work with high-end
              products from reputable brands that we trust. Don't
              hesitate and book your appointment with us!
            </p>
          </div>
        </div>
        <div className="col-xl-3 col-md-6">
          <div
            className="card border-0 d-flex flex-column justify-content-center align-items-center p-3"
            style={{ width: "400px", backgroundColor: "transparent" }}
          >
            <h4>Working Time</h4>
            <div
              className="working-time d-flex justify-content-start"
              style={{ marginTop: "10px" }}
            >
              <div className="text-left mr-5">
                <p>Monday</p>
                <p>Tuesday</p>
                <p>Wednesday</p>
                <p>Thursday</p>
                <p>Friday</p>
                <p>Saturday</p>
                <p>Sunday</p>
              </div>

              <div className="text-right">
                <p>09:00 - 19:00</p>
                <p>09:00 - 19:00</p>
                <p>09:00 - 19:00</p>
                <p>09:00 - 19:00</p>
                <p>09:00 - 19:00</p>
                <p>09:00 - 19:00</p>
                <p>Closed</p>
              </div>
            </div>
          </div>
        </div>

        <div className="col-xl-3 col-md-6 mt-md-3">
          <div
            className="card border-0 d-flex flex-column justify-content-center align-items-left p-3"
            style={{ width: "400px" }}
          >
            <h4 className="text-center" style={{ marginTop: "-15px" }}>
              Contacts
            </h4>
            <div className="text-left" style={{ marginTop: "10px" }}>
              <div className="row align-items-center mb-3">
                <div className="col-1">
                  <i className="fas fa-phone-alt"></i>
                </div>
                <div className="col-11" id="phone" ref={phoneRef}>
                  0987 654 321
                </div>
              </div>
              <div className="row align-items-center mb-3">
                <div className="col-1">
                  <i className="fas fa-envelope"></i>
                </div>
                <div className="col-11">info@studio.theone.com</div>
              </div>
              <div className="row align-items-center mb-3">
                <div className="col-1">
                  <i className="fas fa-map-marker-alt"></i>{" "}
                </div>
                <div className="col-11">
                "Visarion Belinski" 2b Str., 1700 Studentski Kompleks, Sofia, Bulgaria
                </div>
              </div>
            </div>
            <div className="text-center">
              <button
                className="btn btn-dark"
                onClick={() => window.open(googleMapsURL, "_blank")}
                style={{
                  fontSize: "20px",
                  borderRadius: "50px",
                  width: "200px",
                  height: "50px",
                  backgroundColor: "#343a40",
                  borderColor: "#212529",
                  borderWidth: "4px",
                  lineHeight: "20px",
                  textAlign: `center`,
                  justifyContent: "center",
                }}
              >
                Take me there
              </button>
            </div>
          </div>
        </div>
        <div
          className="col-xl-3 col-md-6 mt-md-3 d-flex justify-content-right align-items-right"
          style={{ marginTop: "17px" }}
        >
          <iframe
            title="googleMaps"
            src={googleMapsURL}
            width="400"
            height="250"
            style={{ border: 0 }}
            allowFullScreen=""
            loading="lazy"
          ></iframe>
        </div>
      </div>
      <div style={{ height: "100px" }}></div>
    </div>
  );
};

export default HomePage;
