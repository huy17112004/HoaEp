import { Link } from 'react-router-dom';
import { Flower2 } from 'lucide-react';

export function CustomerFooter() {
  return (
    <footer className="border-t bg-secondary/50">
      <div className="container py-12">
        <div className="grid gap-8 md:grid-cols-4">
          <div>
            <div className="mb-4 flex items-center gap-2">
              <Flower2 className="h-6 w-6 text-primary" />
              <span className="font-heading text-lg font-semibold text-heading">Dear Floral</span>
            </div>
            <p className="text-sm text-body leading-relaxed">
              Nghệ thuật hoa ép khô — Lưu giữ vẻ đẹp vĩnh cửu của thiên nhiên trong từng tác phẩm thủ công.
            </p>
          </div>

          <div>
            <h4 className="mb-3 font-heading text-sm font-semibold text-heading">Sản phẩm</h4>
            <ul className="space-y-2 text-sm">
              <li><Link to="/products" className="text-body hover:text-primary transition-colors">Tranh hoa ép</Link></li>
              <li><Link to="/products" className="text-body hover:text-primary transition-colors">Bookmark hoa</Link></li>
              <li><Link to="/products" className="text-body hover:text-primary transition-colors">Phụ kiện hoa ép</Link></li>
              <li><Link to="/custom-order" className="text-body hover:text-primary transition-colors">Đặt hàng Custom</Link></li>
            </ul>
          </div>

          <div>
            <h4 className="mb-3 font-heading text-sm font-semibold text-heading">Hỗ trợ</h4>
            <ul className="space-y-2 text-sm">
              <li><Link to="/about" className="text-body hover:text-primary transition-colors">Về chúng tôi</Link></li>
              <li><Link to="/contact" className="text-body hover:text-primary transition-colors">Liên hệ</Link></li>
            </ul>
          </div>

          <div>
            <h4 className="mb-3 font-heading text-sm font-semibold text-heading">Liên hệ</h4>
            <ul className="space-y-2 text-sm text-body">
              <li>📧 hello@dearfloral.vn</li>
              <li>📱 0901 234 567</li>
              <li>📍 TP. Hồ Chí Minh, Việt Nam</li>
            </ul>
          </div>
        </div>

        <div className="mt-8 border-t pt-6 text-center text-xs text-caption">
          © 2024 Dear Floral. Tất cả quyền được bảo lưu.
        </div>
      </div>
    </footer>
  );
}
