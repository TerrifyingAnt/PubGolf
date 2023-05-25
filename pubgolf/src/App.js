import './App.css';
import { Route, Routes } from 'react-router-dom';
import Header from './components/base/Header';
import Footer from './components/base/Footer';
import Main from './components/Main';

function App() {
  return (
    <div className="App">
      <Header />
      <div>
        <Routes>
          <Route path="/" element={<Main />}/>
        </Routes>
      </div>
      <Footer />
    </div>
  );
}

export default App;
