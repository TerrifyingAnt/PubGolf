import React, {useState, useEffect} from 'react';
import { Link } from 'react-router-dom';
import jwtDecode from 'jwt-decode';
import '../../css/header.css';

const Header = () => {

  const [isAuthenticated, setIsAuthenticated] = useState(false);

  useEffect(() => {
    const token = localStorage.getItem('access');
    if (token) {
      const decodedToken = jwtDecode(token);
      console.log(decodedToken)
      if (decodedToken.exp * 1000 < Date.now()) {
        localStorage.removeItem('access');
      } else {
        setIsAuthenticated(true);
      }
    }
  }, []);

  return (
    <>
      <header className="header">
        <div className="header__logo">
          <Link className="header__link" to="/"> PubGolf </Link>
        </div>
        <nav className="header__nav">
          <ul className="header__list">
            <li className="header__item"><Link className="header__link" to="/profile_create">Бары</Link></li>
            {isAuthenticated ? (
              <>
              <li className="header__item"><Link className="header__link" to="/about">О проекте</Link></li>
              <li className="header__item"><Link className="header__link" to="/account">Аккаунт</Link></li>
              </>
            ) : (
              <>
              <li className="header__item"><Link className="header__link" to="/about">О проекте</Link></li>
              <li className="header__item"><Link className="header__link" to="/login">Войти</Link></li>
              <li className="header__item"><Link className="header__link" to="/register">Регистрация</Link></li>
              </>
            )}
          </ul>
        </nav>
      </header>
    </>
  );
};

export default Header;
