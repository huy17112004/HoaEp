import { useState } from 'react';
import { mockProducts, mockCategories } from '@/data/mockData';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Badge } from '@/components/ui/badge';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import { Plus, Search, Pencil } from 'lucide-react';
import type { ProductKind } from '@/types';

export default function AdminProductList() {
  const [search, setSearch] = useState('');
  const [kindFilter, setKindFilter] = useState<string>('all');

  const filtered = mockProducts.filter(p => {
    const matchSearch = !search || p.name.toLowerCase().includes(search.toLowerCase());
    const matchKind = kindFilter === 'all' || p.productKind === kindFilter;
    return matchSearch && matchKind;
  });

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="font-heading text-2xl font-bold text-heading">Quản lý mặt hàng</h1>
        <Button className="gap-2 rounded-full"><Plus className="h-4 w-4" /> Thêm mặt hàng</Button>
      </div>

      <Card>
        <CardHeader>
          <div className="flex flex-col gap-3 md:flex-row md:items-center">
            <div className="relative flex-1">
              <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-caption" />
              <Input placeholder="Tìm kiếm..." value={search} onChange={e => setSearch(e.target.value)} className="pl-10" />
            </div>
            <Select value={kindFilter} onValueChange={setKindFilter}>
              <SelectTrigger className="w-48"><SelectValue /></SelectTrigger>
              <SelectContent>
                <SelectItem value="all">Tất cả loại</SelectItem>
                <SelectItem value="standard_product">Sản phẩm thường</SelectItem>
                <SelectItem value="frame_option">Khung tranh</SelectItem>
              </SelectContent>
            </Select>
          </div>
        </CardHeader>
        <CardContent>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Mặt hàng</TableHead>
                <TableHead>Loại</TableHead>
                <TableHead>Giá</TableHead>
                <TableHead>Bán trực tiếp</TableHead>
                <TableHead>Custom</TableHead>
                <TableHead>Trạng thái</TableHead>
                <TableHead></TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {filtered.map(p => (
                <TableRow key={p.id}>
                  <TableCell>
                    <div className="flex items-center gap-3">
                      <div className="h-10 w-10 rounded-lg bg-secondary overflow-hidden shrink-0">
                        <img src={p.imageUrl} alt="" className="h-full w-full object-cover" />
                      </div>
                      <div>
                        <span className="font-medium text-heading">{p.name}</span>
                        <p className="text-xs text-caption">{p.size}</p>
                      </div>
                    </div>
                  </TableCell>
                  <TableCell>
                    <Badge variant={p.productKind === 'frame_option' ? 'outline' : 'secondary'} className="text-xs">
                      {p.productKind === 'standard_product' ? 'Sản phẩm' : 'Khung tranh'}
                    </Badge>
                  </TableCell>
                  <TableCell className="font-medium">{p.price.toLocaleString('vi-VN')}₫</TableCell>
                  <TableCell>{p.isSellableDirectly ? '✓' : '—'}</TableCell>
                  <TableCell>{p.isCustomSelectable ? '✓' : '—'}</TableCell>
                  <TableCell>
                    <Badge variant={p.status === 'active' ? 'default' : 'secondary'} className="text-xs">
                      {p.status === 'active' ? 'Hoạt động' : 'Ẩn'}
                    </Badge>
                  </TableCell>
                  <TableCell>
                    <Button variant="ghost" size="icon"><Pencil className="h-4 w-4" /></Button>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </CardContent>
      </Card>
    </div>
  );
}
