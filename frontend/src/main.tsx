import './index.css'
import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import Dashboard from './page/Dashboard/Dashboard'
import { AuthServiceImpl } from './service/impl/auth_service_impl'
import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import AdminDashboard from './page/AdminDashboard/AdminDashboard'
import { Paths } from './util/paths'
import LoginPage from './page/LoginPage/LoginPage'
import { UserRole } from './domain/UserRole'
import { UserServiceImpl } from './service/impl/user_service_impl'
import { TransactionServiceImpl } from './service/impl/transaction_service_impl'

const rootElement = document.getElementById('root');

//IMPROVEMENT: Dependency injection could be better
//IMPROVEMENT: Use a configuration file or environment variable
let authService = new AuthServiceImpl();
let userService = new UserServiceImpl(authService);
let transactionService = new TransactionServiceImpl(authService);

const router = createBrowserRouter([
  {
    path: Paths.loginPath,
    element: <LoginPage authService={authService} />
  },
  {
    path: Paths.adminDashboardPath,
    element: <AdminDashboard authService={authService} userService={userService} transactionService={transactionService} />
  },
  {
    path: Paths.dashboardPath,
    element: <Dashboard authService={authService} userService={userService} transactionService={transactionService} />
  }
]);

router.navigate(Paths.loginPath)

if (rootElement) {
  createRoot(rootElement).render(
    <StrictMode>
      <RouterProvider router={router} />
    </StrictMode>)
}
