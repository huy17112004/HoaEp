import { Link } from 'react-router-dom';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Flower2 } from 'lucide-react';

export default function RegisterPage() {
  return (
    <div className="flex min-h-[80vh] items-center justify-center py-12">
      <div className="mx-auto w-full max-w-md">
        <div className="text-center mb-8">
          <Flower2 className="mx-auto mb-3 h-10 w-10 text-primary" />
          <h1 className="font-heading text-2xl font-bold text-heading">Đăng ký</h1>
          <p className="mt-1 text-sm text-body">Tạo tài khoản Dear Floral của bạn</p>
        </div>

        <form className="space-y-4 rounded-2xl border bg-card p-8">
          <div>
            <Label>Họ tên</Label>
            <Input placeholder="Nguyễn Văn A" className="mt-1" />
          </div>
          <div>
            <Label>Số điện thoại</Label>
            <Input placeholder="0901234567" className="mt-1" />
          </div>
          <div>
            <Label>Email</Label>
            <Input type="email" placeholder="email@example.com" className="mt-1" />
          </div>
          <div>
            <Label>Mật khẩu</Label>
            <Input type="password" placeholder="••••••••" className="mt-1" />
          </div>
          <Button className="w-full rounded-full">Đăng ký</Button>
          <p className="text-center text-sm text-body">
            Đã có tài khoản?{' '}
            <Link to="/auth/login" className="text-primary hover:underline font-medium">Đăng nhập</Link>
          </p>
        </form>
      </div>
    </div>
  );
}
