import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import "../../css/register.css";

function Registration() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const history = useNavigate();
  
    const handleSubmit = async (event) => {
      event.preventDefault();
      const response = await fetch('http://127.0.0.1:8000/api/v1/auth/users/', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          username: username,
          password: password
        })
      });
      const data = await response.json();
      if (response.ok) {
        localStorage.setItem('token', data.token);
        history('/register/success');
      } else {
        if (data.username) { setError(data.username);}
        if (data.password) { setError(data.password);}
      }
    };
  
    return (
        <div className="registration-form-container">
            <form onSubmit={handleSubmit}>
                <h2>Регистрация</h2>
                <div className="form-group">
                    <label htmlFor="name">Имя</label>
                    <input type="text" value={username} onChange={(e) => setUsername(e.target.value)} />
                </div>
                <div className="form-group">
                    <label htmlFor="password">Пароль</label>
                    <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
                </div>
                <button type="submit">Зарегаца</button>
                {error && <p>{error}</p>}
            </form>
        </div>
    );
  }

const Register = () => {
  return (
    <div>
        < Registration />
    </div>
  )
};

export default Register;