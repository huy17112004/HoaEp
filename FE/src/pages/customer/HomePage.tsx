import { Link } from 'react-router-dom';
import { Button } from '@/components/ui/button';
import { ProductCard } from '@/components/shared/ProductCard';
import { getSellableProducts } from '@/data/mockData';
import { ArrowRight, Flower2, Heart, Sparkles } from 'lucide-react';

export default function HomePage() {
  const featuredProducts = getSellableProducts().slice(0, 4);

  return (
    <div>
      {/* Hero */}
      <section className="relative overflow-hidden bg-secondary/30">
        <div className="container flex flex-col items-center py-20 text-center md:py-32">
          <div className="mb-6 inline-flex items-center gap-2 rounded-full bg-primary/10 px-4 py-1.5 text-sm font-medium text-primary">
            <Flower2 className="h-4 w-4" />
            Nghệ thuật hoa ép khô thủ công
          </div>
          <h1 className="max-w-3xl font-heading text-4xl font-bold leading-tight text-heading md:text-6xl">
            Lưu giữ vẻ đẹp <br />
            <span className="text-primary">vĩnh cửu</span> của thiên nhiên
          </h1>
          <p className="mt-6 max-w-xl text-lg text-body leading-relaxed">
            Mỗi tác phẩm của Dear Floral là một câu chuyện được kể bằng hoa — ép khô, sắp đặt và lưu giữ
            trong khung tranh thủ công tinh tế.
          </p>
          <div className="mt-8 flex flex-wrap gap-4">
            <Link to="/products">
              <Button size="lg" className="gap-2 rounded-full px-8">
                Khám phá sản phẩm <ArrowRight className="h-4 w-4" />
              </Button>
            </Link>
            <Link to="/custom-order">
              <Button size="lg" variant="outline" className="gap-2 rounded-full px-8">
                Đặt hàng Custom <Sparkles className="h-4 w-4" />
              </Button>
            </Link>
          </div>
        </div>
      </section>

      {/* Features */}
      <section className="container py-16">
        <div className="grid gap-8 md:grid-cols-3">
          {[
            { icon: Flower2, title: 'Thủ công 100%', desc: 'Mỗi sản phẩm được làm hoàn toàn bằng tay bởi nghệ nhân' },
            { icon: Heart, title: 'Hoa thật tự nhiên', desc: 'Sử dụng hoa ép khô tự nhiên, giữ nguyên màu sắc và hình dáng' },
            { icon: Sparkles, title: 'Cá nhân hóa', desc: 'Dịch vụ custom theo yêu cầu với hoa của riêng bạn' },
          ].map(feature => (
            <div key={feature.title} className="rounded-2xl bg-card p-8 text-center border border-border/50">
              <div className="mx-auto mb-4 flex h-14 w-14 items-center justify-center rounded-full bg-sage-light">
                <feature.icon className="h-6 w-6 text-primary" />
              </div>
              <h3 className="font-heading text-lg font-semibold text-heading">{feature.title}</h3>
              <p className="mt-2 text-sm text-body leading-relaxed">{feature.desc}</p>
            </div>
          ))}
        </div>
      </section>

      {/* Featured products */}
      <section className="bg-surface-warm py-16">
        <div className="container">
          <div className="mb-8 flex items-end justify-between">
            <div>
              <h2 className="font-heading text-2xl font-bold text-heading md:text-3xl">Sản phẩm nổi bật</h2>
              <p className="mt-2 text-body">Những tác phẩm được yêu thích nhất</p>
            </div>
            <Link to="/products">
              <Button variant="ghost" className="gap-2 text-primary">
                Xem tất cả <ArrowRight className="h-4 w-4" />
              </Button>
            </Link>
          </div>
          <div className="grid gap-6 sm:grid-cols-2 lg:grid-cols-4">
            {featuredProducts.map(product => (
              <ProductCard key={product.id} product={product} />
            ))}
          </div>
        </div>
      </section>

      {/* CTA Custom */}
      <section className="container py-16">
        <div className="overflow-hidden rounded-3xl bg-primary/5 border border-primary/10">
          <div className="flex flex-col items-center px-8 py-16 text-center">
            <Sparkles className="mb-4 h-10 w-10 text-accent" />
            <h2 className="max-w-lg font-heading text-2xl font-bold text-heading md:text-3xl">
              Tạo tác phẩm riêng với hoa của bạn
            </h2>
            <p className="mt-4 max-w-md text-body leading-relaxed">
              Gửi cho chúng tôi bó hoa kỷ niệm, hoa cưới hay bất kỳ loại hoa nào bạn muốn giữ mãi —
              chúng tôi sẽ biến chúng thành nghệ thuật.
            </p>
            <Link to="/custom-order" className="mt-6">
              <Button size="lg" className="gap-2 rounded-full px-8">
                Bắt đầu đặt hàng Custom <ArrowRight className="h-4 w-4" />
              </Button>
            </Link>
          </div>
        </div>
      </section>
    </div>
  );
}
