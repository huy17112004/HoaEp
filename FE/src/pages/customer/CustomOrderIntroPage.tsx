import { Link } from 'react-router-dom';
import { Button } from '@/components/ui/button';
import { ArrowRight, Sparkles, Upload, Eye, MessageSquare, CreditCard } from 'lucide-react';

const steps = [
  { icon: Sparkles, title: 'Chọn khung tranh', desc: 'Chọn khung tranh bạn yêu thích từ bộ sưu tập của chúng tôi' },
  { icon: Upload, title: 'Gửi hoa & yêu cầu', desc: 'Upload ảnh hoa thật và mô tả thiết kế mong muốn' },
  { icon: Eye, title: 'Xem demo', desc: 'Nhận bản demo và phản hồi chỉnh sửa cho đến khi hài lòng' },
  { icon: CreditCard, title: 'Thanh toán & nhận hàng', desc: 'Thanh toán phần còn lại và nhận tác phẩm hoàn thiện' },
];

export default function CustomOrderIntroPage() {
  return (
    <div className="container py-12">
      <div className="mx-auto max-w-3xl text-center">
        <div className="mb-6 inline-flex items-center gap-2 rounded-full bg-accent/10 px-4 py-1.5 text-sm font-medium text-accent">
          <Sparkles className="h-4 w-4" />
          Dịch vụ Custom
        </div>
        <h1 className="font-heading text-3xl font-bold text-heading md:text-4xl">
          Biến bó hoa kỷ niệm thành tác phẩm nghệ thuật
        </h1>
        <p className="mt-4 text-lg text-body leading-relaxed">
          Hoa cưới, hoa sinh nhật, hay bất kỳ bó hoa nào bạn muốn giữ mãi — chúng tôi sẽ ép khô
          và sắp đặt chúng trong khung tranh thủ công tinh tế.
        </p>
      </div>

      {/* Steps */}
      <div className="mx-auto mt-16 max-w-4xl">
        <div className="grid gap-8 md:grid-cols-2">
          {steps.map((step, i) => (
            <div key={step.title} className="flex gap-4 rounded-2xl border border-border/50 bg-card p-6">
              <div className="flex h-12 w-12 shrink-0 items-center justify-center rounded-full bg-primary/10 text-primary font-heading font-bold text-lg">
                {i + 1}
              </div>
              <div>
                <h3 className="font-heading text-base font-semibold text-heading">{step.title}</h3>
                <p className="mt-1 text-sm text-body leading-relaxed">{step.desc}</p>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Pricing note */}
      <div className="mx-auto mt-12 max-w-2xl rounded-2xl bg-surface-warm p-8 text-center">
        <MessageSquare className="mx-auto mb-3 h-8 w-8 text-accent" />
        <h3 className="font-heading text-lg font-semibold text-heading">Chính sách đặt cọc</h3>
        <p className="mt-2 text-sm text-body leading-relaxed">
          Bạn sẽ đặt cọc trước khi bắt đầu. Sau khi demo được duyệt, thanh toán phần còn lại để nhận sản phẩm.
          Miễn phí chỉnh sửa demo đến 3 lần.
        </p>
      </div>

      <div className="mt-12 text-center">
        <Link to="/account/custom-orders/create">
          <Button size="lg" className="gap-2 rounded-full px-10">
            Bắt đầu đặt hàng Custom <ArrowRight className="h-4 w-4" />
          </Button>
        </Link>
      </div>
    </div>
  );
}
