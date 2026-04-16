import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Textarea } from '@/components/ui/textarea';
import { Label } from '@/components/ui/label';
import { Mail, MapPin, Phone } from 'lucide-react';

export default function ContactPage() {
  return (
    <div className="container py-12">
      <div className="mx-auto max-w-4xl">
        <div className="text-center mb-12">
          <h1 className="font-heading text-3xl font-bold text-heading">Liên hệ</h1>
          <p className="mt-2 text-body">Chúng tôi luôn sẵn sàng lắng nghe bạn</p>
        </div>

        <div className="grid gap-12 md:grid-cols-2">
          <div className="space-y-6">
            <div className="flex items-start gap-4">
              <div className="rounded-lg bg-primary/10 p-3"><Mail className="h-5 w-5 text-primary" /></div>
              <div>
                <h3 className="font-heading font-semibold text-heading">Email</h3>
                <p className="text-sm text-body">hello@dearfloral.vn</p>
              </div>
            </div>
            <div className="flex items-start gap-4">
              <div className="rounded-lg bg-primary/10 p-3"><Phone className="h-5 w-5 text-primary" /></div>
              <div>
                <h3 className="font-heading font-semibold text-heading">Điện thoại</h3>
                <p className="text-sm text-body">0901 234 567</p>
              </div>
            </div>
            <div className="flex items-start gap-4">
              <div className="rounded-lg bg-primary/10 p-3"><MapPin className="h-5 w-5 text-primary" /></div>
              <div>
                <h3 className="font-heading font-semibold text-heading">Địa chỉ</h3>
                <p className="text-sm text-body">TP. Hồ Chí Minh, Việt Nam</p>
              </div>
            </div>
          </div>

          <form className="space-y-4 rounded-2xl border bg-card p-6">
            <div>
              <Label>Họ tên</Label>
              <Input placeholder="Nhập họ tên" className="mt-1" />
            </div>
            <div>
              <Label>Email</Label>
              <Input type="email" placeholder="email@example.com" className="mt-1" />
            </div>
            <div>
              <Label>Nội dung</Label>
              <Textarea placeholder="Bạn muốn hỏi gì?" rows={4} className="mt-1" />
            </div>
            <Button className="w-full rounded-full">Gửi tin nhắn</Button>
          </form>
        </div>
      </div>
    </div>
  );
}
