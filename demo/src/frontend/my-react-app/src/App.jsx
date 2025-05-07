import { AuthProvider } from './context/LoginContext';
import Menubar from './pages/layout/Menubar';
import MenuRouter from './pages/routes/MenuRouter';
import { BrowserRouter } from 'react-router-dom';

import './App.css'

function App() {


  return (
    <>
      <AuthProvider>
        <BrowserRouter>
          <div className="App">
            <header className="App-header">
              <Menubar />
            </header>
            <MenuRouter />
          </div>
        </BrowserRouter>
      </AuthProvider>
    </>
  )
}

export default App;
