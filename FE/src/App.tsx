import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import { Toaster as Sonner } from "@/components/ui/sonner";
import { Toaster } from "@/components/ui/toaster";
import { TooltipProvider } from "@/components/ui/tooltip";

// Layouts
import { CustomerLayout } from "@/components/customer/CustomerLayout";
import { AdminLayout } from "@/components/admin/AdminLayout";

// Customer pages
import HomePage from "@/pages/customer/HomePage";
import ProductListPage from "@/pages/customer/ProductListPage";
import ProductDetailPage from "@/pages/customer/ProductDetailPage";
import CustomOrderIntroPage from "@/pages/customer/CustomOrderIntroPage";
import AboutPage from "@/pages/customer/AboutPage";
import ContactPage from "@/pages/customer/ContactPage";

// Auth pages
import LoginPage from "@/pages/auth/LoginPage";
import RegisterPage from "@/pages/auth/RegisterPage";

// Account pages
import ProfilePage from "@/pages/account/ProfilePage";
import AddressManagementPage from "@/pages/account/AddressManagementPage";
import MyAvailableOrdersPage from "@/pages/account/MyAvailableOrdersPage";
import MyCustomOrdersPage from "@/pages/account/MyCustomOrdersPage";
import AvailableOrderDetailPage from "@/pages/account/AvailableOrderDetailPage";
import CustomOrderDetailPage from "@/pages/account/CustomOrderDetailPage";
import CreateCustomOrderPage from "@/pages/account/CreateCustomOrderPage";

// Admin pages
import AdminDashboard from "@/pages/admin/AdminDashboard";
import AdminProductList from "@/pages/admin/AdminProductList";
import AdminUserList from "@/pages/admin/AdminUserList";
import AdminAvailableOrders from "@/pages/admin/AdminAvailableOrders";
import AdminCustomOrders from "@/pages/admin/AdminCustomOrders";
import AdminPurchaseReceipts from "@/pages/admin/AdminPurchaseReceipts";
import AdminInventory from "@/pages/admin/AdminInventory";
import AdminReports from "@/pages/admin/AdminReports";

import NotFound from "./pages/NotFound";

const queryClient = new QueryClient();

const App = () => (
  <QueryClientProvider client={queryClient}>
    <TooltipProvider>
      <Toaster />
      <Sonner />
      <BrowserRouter>
        <Routes>
          {/* Customer site */}
          <Route element={<CustomerLayout />}>
            <Route path="/" element={<HomePage />} />
            <Route path="/products" element={<ProductListPage />} />
            <Route path="/products/:id" element={<ProductDetailPage />} />
            <Route path="/custom-order" element={<CustomOrderIntroPage />} />
            <Route path="/about" element={<AboutPage />} />
            <Route path="/contact" element={<ContactPage />} />
            <Route path="/auth/login" element={<LoginPage />} />
            <Route path="/auth/register" element={<RegisterPage />} />
            <Route path="/account/profile" element={<ProfilePage />} />
            <Route path="/account/addresses" element={<AddressManagementPage />} />
            <Route path="/account/orders" element={<MyAvailableOrdersPage />} />
            <Route path="/account/orders/:id" element={<AvailableOrderDetailPage />} />
            <Route path="/account/custom-orders" element={<MyCustomOrdersPage />} />
            <Route path="/account/custom-orders/create" element={<CreateCustomOrderPage />} />
            <Route path="/account/custom-orders/:id" element={<CustomOrderDetailPage />} />
          </Route>

          {/* Admin site */}
          <Route element={<AdminLayout />}>
            <Route path="/admin/dashboard" element={<AdminDashboard />} />
            <Route path="/admin/users" element={<AdminUserList />} />
            <Route path="/admin/products" element={<AdminProductList />} />
            <Route path="/admin/orders/available" element={<AdminAvailableOrders />} />
            <Route path="/admin/orders/custom" element={<AdminCustomOrders />} />
            <Route path="/admin/purchase-receipts" element={<AdminPurchaseReceipts />} />
            <Route path="/admin/inventory" element={<AdminInventory />} />
            <Route path="/admin/reports" element={<AdminReports />} />
          </Route>

          <Route path="*" element={<NotFound />} />
        </Routes>
      </BrowserRouter>
    </TooltipProvider>
  </QueryClientProvider>
);

export default App;
