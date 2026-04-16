import { Link, useLocation } from 'react-router-dom';
import { useState } from 'react';
import { Menu, X, User, ShoppingBag, Flower2 } from 'lucide-react';
import { Button } from '@/components/ui/button';

const navLinks = [
  { label: 'Trang chủ', path: '/' },
  { label: 'Sản phẩm', path: '/products' },
  { label: 'Đặt hàng Custom', path: '/custom-order' },
  { label: 'Về chúng tôi', path: '/about' },
  { label: 'Liên hệ', path: '/contact' },
];

export function CustomerHeader() {
  const location = useLocation();
  const [mobileOpen, setMobileOpen] = useState(false);

  return (
    <header className="sticky top-0 z-50 border-b bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/80">
      <div className="container flex h-16 items-center justify-between md:h-20">
        <Link to="/" className="flex items-center gap-2">
          <Flower2 className="h-7 w-7 text-primary" />
          <span className="font-heading text-xl font-semibold text-heading">Dear Floral</span>
        </Link>

        {/* Desktop nav */}
        <nav className="hidden items-center gap-8 md:flex">
          {navLinks.map(link => (
            <Link
              key={link.path}
              to={link.path}
              className={`text-sm font-medium transition-colors hover:text-primary ${
                location.pathname === link.path ? 'text-primary' : 'text-body'
              }`}
            >
              {link.label}
            </Link>
          ))}
        </nav>

        <div className="flex items-center gap-3">
          <Link to="/account/orders">
            <Button variant="ghost" size="icon" className="text-foreground">
              <ShoppingBag className="h-5 w-5" />
            </Button>
          </Link>
          <Link to="/account/profile">
            <Button variant="ghost" size="icon" className="text-foreground">
              <User className="h-5 w-5" />
            </Button>
          </Link>
          <Button
            variant="ghost"
            size="icon"
            className="text-foreground md:hidden"
            onClick={() => setMobileOpen(!mobileOpen)}
          >
            {mobileOpen ? <X className="h-5 w-5" /> : <Menu className="h-5 w-5" />}
          </Button>
        </div>
      </div>

      {/* Mobile nav */}
      {mobileOpen && (
        <nav className="border-t bg-background p-4 md:hidden">
          {navLinks.map(link => (
            <Link
              key={link.path}
              to={link.path}
              onClick={() => setMobileOpen(false)}
              className={`block py-3 text-sm font-medium transition-colors ${
                location.pathname === link.path ? 'text-primary' : 'text-body'
              }`}
            >
              {link.label}
            </Link>
          ))}
          <div className="mt-3 flex gap-2 border-t pt-3">
            <Link to="/auth/login" className="flex-1" onClick={() => setMobileOpen(false)}>
              <Button variant="outline" className="w-full">Đăng nhập</Button>
            </Link>
            <Link to="/auth/register" className="flex-1" onClick={() => setMobileOpen(false)}>
              <Button className="w-full">Đăng ký</Button>
            </Link>
          </div>
        </nav>
      )}
    </header>
  );
}
