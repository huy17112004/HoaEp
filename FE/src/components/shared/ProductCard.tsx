import { Link } from 'react-router-dom';
import type { Product } from '@/types';
import { Card, CardContent } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';

interface ProductCardProps {
  product: Product;
  linkPrefix?: string;
}

export function ProductCard({ product, linkPrefix = '/products' }: ProductCardProps) {
  return (
    <Link to={`${linkPrefix}/${product.id}`}>
      <Card className="group overflow-hidden border-border/50 bg-card transition-all hover:shadow-lg hover:-translate-y-1">
        <div className="aspect-[4/5] overflow-hidden bg-secondary">
          <img
            src={product.imageUrl}
            alt={product.name}
            className="h-full w-full object-cover transition-transform duration-500 group-hover:scale-105"
          />
        </div>
        <CardContent className="p-4">
          {product.category && (
            <span className="text-xs font-medium text-caption uppercase tracking-wider">
              {product.category.name}
            </span>
          )}
          <h3 className="mt-1 font-heading text-base font-semibold text-heading line-clamp-2">
            {product.name}
          </h3>
          <div className="mt-2 flex items-center justify-between">
            <span className="text-sm font-semibold text-primary">
              {product.price.toLocaleString('vi-VN')}₫
            </span>
            {product.size && (
              <Badge variant="secondary" className="text-xs">
                {product.size}
              </Badge>
            )}
          </div>
        </CardContent>
      </Card>
    </Link>
  );
}
