import { useState } from 'react';
import { mockInventory, getProductById } from '@/data/mockData';
import { Card, CardContent, CardHeader } from '@/components/ui/card';
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table';
import { Badge } from '@/components/ui/badge';
import { Input } from '@/components/ui/input';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import { Search } from 'lucide-react';

export default function AdminInventory() {
  const [search, setSearch] = useState('');
  const [kindFilter, setKindFilter] = useState('all');

  const items = mockInventory.map(inv => ({
    ...inv,
    product: getProductById(inv.productId),
  })).filter(inv => {
    const matchSearch = !search || inv.product?.name.toLowerCase().includes(search.toLowerCase());
    const matchKind = kindFilter === 'all' || inv.product?.productKind === kindFilter;
    return matchSearch && matchKind;
  });

  return (
    <div className="space-y-6">
      <h1 className="font-heading text-2xl font-bold text-heading">Tồn kho</h1>

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
                <SelectItem value="all">Tất cả</SelectItem>
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
                <TableHead>Tồn kho</TableHead>
                <TableHead>Cập nhật</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {items.map(inv => (
                <TableRow key={inv.id}>
                  <TableCell>
                    <div className="flex items-center gap-3">
                      <div className="h-10 w-10 rounded-lg bg-secondary overflow-hidden shrink-0">
                        <img src={inv.product?.imageUrl || ''} alt="" className="h-full w-full object-cover" />
                      </div>
                      <span className="font-medium text-heading">{inv.product?.name}</span>
                    </div>
                  </TableCell>
                  <TableCell>
                    <Badge variant="secondary" className="text-xs">
                      {inv.product?.productKind === 'standard_product' ? 'Sản phẩm' : 'Khung tranh'}
                    </Badge>
                  </TableCell>
                  <TableCell>
                    <span className={`font-semibold ${inv.quantityOnHand <= 5 ? 'text-destructive' : 'text-heading'}`}>
                      {inv.quantityOnHand}
                    </span>
                  </TableCell>
                  <TableCell className="text-caption text-sm">{new Date(inv.updatedAt).toLocaleDateString('vi-VN')}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </CardContent>
      </Card>
    </div>
  );
}
