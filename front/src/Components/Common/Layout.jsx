import { Outlet } from 'react-router-dom';
import Navigator from './Navigator';
export default function Layout() {
  return (
    <>
      <div className='bg-back-image bg-cover bg-no-repeat bg-center bg-fixed h-screen'>
        <nav className='h-[5vh] f-c-c'>
          <div className='container h-full my-auto f-b-c px-2 text-blue-300'>
            <Navigator />
          </div>
        </nav>
        <div className='h-[95vh] f-c-c'>
          <Outlet />
        </div>
      </div>
    </>
  );
}
