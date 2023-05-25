import React from 'react';
import { Link } from 'react-router-dom';
import '../../css/registrationSuccess.css';
import cat from "../../assets/img/cat.gif"

const RegistrationSuccess = () => {
  return (
    <div className="RegistrationSuccessPage">
      <h2>Успешная регистрация ❤️</h2>
      <img src={cat} alt="cat" />
      <p>Ваш аккаунт создан. Добро пожаловать!</p>
      <Link to="/" className="btn">Вернуться на главную.</Link>
    </div>
  );
};

export default RegistrationSuccess;
