import { useParams, Link } from 'react-router-dom';
import { getProductById } from '@/data/mockData';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import { ArrowLeft, ShoppingBag } from 'lucide-react';
import { useState } from 'react';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';

export default function ProductDetailPage() {
  const { id } = useParams();
  const product = getProductById(id || '');
  const [quantity, setQuantity] = useState(1);

  if (!product) {
    return (
      <div className="container py-16 text-center">
        <p className="text-caption">Sản phẩm không tồn tại.</p>
        <Link to="/products"><Button variant="ghost" className="mt-4">← Quay lại</Button></Link>
      </div>
    );
  }

  return (
    <div className="container py-8">
      <Link to="/products" className="mb-6 inline-flex items-center gap-1 text-sm text-caption hover:text-primary transition-colors">
        <ArrowLeft className="h-4 w-4" /> Quay lại danh sách
      </Link>

      <div className="grid gap-8 lg:grid-cols-2">
        {/* Image */}
        <div className="overflow-hidden rounded-2xl bg-secondary aspect-square">
          <img src={product.imageUrl} alt={product.name} className="h-full w-full object-cover" />
        </div>

        {/* Info */}
        <div className="flex flex-col justify-center">
          <Badge variant="secondary" className="mb-3 w-fit text-xs uppercase tracking-wider">
            {product.flowerType || product.material}
          </Badge>
          <h1 className="font-heading text-3xl font-bold text-heading">{product.name}</h1>
          <p className="mt-3 text-2xl font-heading font-semibold text-primary">
            {product.price.toLocaleString('vi-VN')}₫
          </p>
          <p className="mt-4 text-body leading-relaxed">{product.description}</p>

          <div className="mt-6 space-y-3 rounded-xl bg-surface-warm p-4">
            {product.size && <div className="flex justify-between text-sm"><span className="text-caption">Kích thước</span><span className="text-heading font-medium">{product.size}</span></div>}
            {product.material && <div className="flex justify-between text-sm"><span className="text-caption">Chất liệu</span><span className="text-heading font-medium">{product.material}</span></div>}
            {product.flowerType && <div className="flex justify-between text-sm"><span className="text-caption">Loại hoa</span><span className="text-heading font-medium">{product.flowerType}</span></div>}
          </div>

          <div className="mt-6 space-y-4">
            <div>
              <Label className="text-sm text-caption">Số lượng</Label>
              <Input
                type="number"
                min={1}
                value={quantity}
                onChange={e => setQuantity(Math.max(1, parseInt(e.target.value) || 1))}
                className="mt-1 w-24"
              />
            </div>
            <Link to={`/account/orders/create?product=${product.id}&qty=${quantity}`}>
              <Button size="lg" className="w-full gap-2 rounded-full md:w-auto md:px-12">
                <ShoppingBag className="h-4 w-4" /> Đặt hàng ngay
              </Button>
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
}
