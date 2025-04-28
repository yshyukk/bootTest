
import Menubar from './pages/layout/Menubar';
import MenuRouter from './pages/routes/MenuRouter';
import { BrowserRouter } from 'react-router-dom';

import './App.css'

function App() {


  return (
    <>
      <BrowserRouter>
        <div className="App">
          <header className="App-header">
            <Menubar />
          </header>
          <MenuRouter />
        </div>
      </BrowserRouter>
    </>
  )
}

export default App;
