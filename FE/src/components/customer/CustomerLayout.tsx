import { Outlet } from 'react-router-dom';
import { CustomerHeader } from './CustomerHeader';
import { CustomerFooter } from './CustomerFooter';

export function CustomerLayout() {
  return (
    <div className="flex min-h-screen flex-col">
      <CustomerHeader />
      <main className="flex-1">
        <Outlet />
      </main>
      <CustomerFooter />
    </div>
  );
}
