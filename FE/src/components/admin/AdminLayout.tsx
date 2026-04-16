import { Link, useLocation, Outlet } from 'react-router-dom';
import { useState } from 'react';
import {
  LayoutDashboard, Users, Package, ShoppingCart, Palette, FileText,
  Warehouse, BarChart3, Flower2, Menu, ChevronLeft, LogOut
} from 'lucide-react';
import { Button } from '@/components/ui/button';
import { cn } from '@/lib/utils';

const sidebarLinks = [
  { label: 'Dashboard', path: '/admin/dashboard', icon: LayoutDashboard },
  { label: 'Người dùng', path: '/admin/users', icon: Users },
  { label: 'Mặt hàng', path: '/admin/products', icon: Package },
  { label: 'Đơn hàng thường', path: '/admin/orders/available', icon: ShoppingCart },
  { label: 'Đơn hàng custom', path: '/admin/orders/custom', icon: Palette },
  { label: 'Phiếu nhập hàng', path: '/admin/purchase-receipts', icon: FileText },
  { label: 'Tồn kho', path: '/admin/inventory', icon: Warehouse },
  { label: 'Báo cáo', path: '/admin/reports', icon: BarChart3 },
];

export function AdminLayout() {
  const [collapsed, setCollapsed] = useState(false);
  const location = useLocation();

  return (
    <div className="flex min-h-screen">
      {/* Sidebar */}
      <aside
        className={cn(
          'fixed left-0 top-0 z-40 flex h-screen flex-col border-r bg-sidebar transition-all duration-300',
          collapsed ? 'w-16' : 'w-64'
        )}
      >
        <div className="flex h-16 items-center justify-between border-b border-sidebar-border px-4">
          {!collapsed && (
            <div className="flex items-center gap-2">
              <Flower2 className="h-6 w-6 text-sidebar-primary" />
              <span className="font-heading text-lg font-semibold text-sidebar-foreground">Dear Floral</span>
            </div>
          )}
          {collapsed && <Flower2 className="mx-auto h-6 w-6 text-sidebar-primary" />}
          <Button
            variant="ghost"
            size="icon"
            onClick={() => setCollapsed(!collapsed)}
            className="h-8 w-8 text-sidebar-foreground hover:bg-sidebar-accent"
          >
            {collapsed ? <Menu className="h-4 w-4" /> : <ChevronLeft className="h-4 w-4" />}
          </Button>
        </div>

        <nav className="flex-1 overflow-y-auto p-2">
          {sidebarLinks.map(link => {
            const isActive = location.pathname.startsWith(link.path);
            return (
              <Link
                key={link.path}
                to={link.path}
                className={cn(
                  'mb-1 flex items-center gap-3 rounded-lg px-3 py-2.5 text-sm font-medium transition-colors',
                  isActive
                    ? 'bg-sidebar-primary text-sidebar-primary-foreground'
                    : 'text-sidebar-foreground hover:bg-sidebar-accent hover:text-sidebar-accent-foreground'
                )}
              >
                <link.icon className="h-5 w-5 shrink-0" />
                {!collapsed && <span>{link.label}</span>}
              </Link>
            );
          })}
        </nav>

        <div className="border-t border-sidebar-border p-2">
          <Link
            to="/"
            className="flex items-center gap-3 rounded-lg px-3 py-2.5 text-sm font-medium text-sidebar-foreground hover:bg-sidebar-accent transition-colors"
          >
            <LogOut className="h-5 w-5 shrink-0" />
            {!collapsed && <span>Về trang chủ</span>}
          </Link>
        </div>
      </aside>

      {/* Main content */}
      <div className={cn('flex-1 transition-all duration-300', collapsed ? 'ml-16' : 'ml-64')}>
        {/* Topbar */}
        <header className="sticky top-0 z-30 flex h-16 items-center border-b bg-background/95 px-6 backdrop-blur">
          <h2 className="font-heading text-lg font-semibold text-heading">
            {sidebarLinks.find(l => location.pathname.startsWith(l.path))?.label || 'Admin'}
          </h2>
        </header>

        <main className="p-6">
          <Outlet />
        </main>
      </div>
    </div>
  );
}
