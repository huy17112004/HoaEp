import { useState, useMemo } from 'react';
import { ProductCard } from '@/components/shared/ProductCard';
import { getSellableProducts, mockCategories } from '@/data/mockData';
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';
import { Search } from 'lucide-react';

export default function ProductListPage() {
  const [search, setSearch] = useState('');
  const [selectedCat, setSelectedCat] = useState<string | null>(null);
  const products = getSellableProducts();

  const categories = mockCategories.filter(c => c.status === 'active' && c.id !== 'cat-3');

  const filtered = useMemo(() => {
    return products.filter(p => {
      const matchSearch = !search || p.name.toLowerCase().includes(search.toLowerCase());
      const matchCat = !selectedCat || p.categoryId === selectedCat;
      return matchSearch && matchCat;
    });
  }, [products, search, selectedCat]);

  return (
    <div className="container py-8">
      <div className="mb-8">
        <h1 className="font-heading text-3xl font-bold text-heading">Sản phẩm</h1>
        <p className="mt-2 text-body">Khám phá bộ sưu tập hoa ép khô thủ công của Dear Floral</p>
      </div>

      {/* Filters */}
      <div className="mb-8 flex flex-col gap-4 md:flex-row md:items-center">
        <div className="relative max-w-sm flex-1">
          <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-caption" />
          <Input
            placeholder="Tìm kiếm sản phẩm..."
            value={search}
            onChange={e => setSearch(e.target.value)}
            className="pl-10"
          />
        </div>
        <div className="flex flex-wrap gap-2">
          <Button
            variant={selectedCat === null ? 'default' : 'outline'}
            size="sm"
            onClick={() => setSelectedCat(null)}
            className="rounded-full"
          >
            Tất cả
          </Button>
          {categories.map(cat => (
            <Button
              key={cat.id}
              variant={selectedCat === cat.id ? 'default' : 'outline'}
              size="sm"
              onClick={() => setSelectedCat(cat.id)}
              className="rounded-full"
            >
              {cat.name}
            </Button>
          ))}
        </div>
      </div>

      {/* Products grid */}
      {filtered.length > 0 ? (
        <div className="grid gap-6 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
          {filtered.map(product => (
            <ProductCard key={product.id} product={product} />
          ))}
        </div>
      ) : (
        <div className="py-16 text-center">
          <p className="text-caption">Không tìm thấy sản phẩm phù hợp.</p>
        </div>
      )}
    </div>
  );
}
