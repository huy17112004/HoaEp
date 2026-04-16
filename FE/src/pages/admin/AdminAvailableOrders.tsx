import { mockAvailableOrders, getProductById } from '@/data/mockData';
import { StatusBadge } from '@/components/shared/StatusBadge';
import { Card, CardContent, CardHeader } from '@/components/ui/card';
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table';
import { Input } from '@/components/ui/input';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import { Button } from '@/components/ui/button';
import { Search, Eye } from 'lucide-react';
import { useState } from 'react';
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from '@/components/ui/dialog';

export default function AdminAvailableOrders() {
  const [search, setSearch] = useState('');
  const [statusFilter, setStatusFilter] = useState('all');

  const filtered = mockAvailableOrders.filter(o => {
    const matchSearch = !search || o.orderCode.toLowerCase().includes(search.toLowerCase());
    const matchStatus = statusFilter === 'all' || o.orderStatus === statusFilter;
    return matchSearch && matchStatus;
  });

  return (
    <div className="space-y-6">
      <h1 className="font-heading text-2xl font-bold text-heading">Đơn hàng thường</h1>

      <Card>
        <CardHeader>
          <div className="flex flex-col gap-3 md:flex-row md:items-center">
            <div className="relative flex-1">
              <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-caption" />
              <Input placeholder="Tìm mã đơn..." value={search} onChange={e => setSearch(e.target.value)} className="pl-10" />
            </div>
            <Select value={statusFilter} onValueChange={setStatusFilter}>
              <SelectTrigger className="w-48"><SelectValue /></SelectTrigger>
              <SelectContent>
                <SelectItem value="all">Tất cả</SelectItem>
                <SelectItem value="received">Đã tiếp nhận</SelectItem>
                <SelectItem value="processing">Đang xử lý</SelectItem>
                <SelectItem value="shipping">Đang giao</SelectItem>
                <SelectItem value="completed">Hoàn thành</SelectItem>
                <SelectItem value="canceled">Đã hủy</SelectItem>
              </SelectContent>
            </Select>
          </div>
        </CardHeader>
        <CardContent>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Mã đơn</TableHead>
                <TableHead>Ngày đặt</TableHead>
                <TableHead>Tổng tiền</TableHead>
                <TableHead>Thanh toán</TableHead>
                <TableHead>Trạng thái</TableHead>
                <TableHead></TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {filtered.map(o => (
                <TableRow key={o.id}>
                  <TableCell className="font-medium text-heading">{o.orderCode}</TableCell>
                  <TableCell className="text-body">{new Date(o.orderedAt).toLocaleDateString('vi-VN')}</TableCell>
                  <TableCell className="font-medium">{o.totalAmount.toLocaleString('vi-VN')}₫</TableCell>
                  <TableCell><StatusBadge type="payment" status={o.paymentStatus} /></TableCell>
                  <TableCell><StatusBadge type="availableOrder" status={o.orderStatus} /></TableCell>
                  <TableCell>
                    <Dialog>
                      <DialogTrigger asChild>
                        <Button variant="ghost" size="icon"><Eye className="h-4 w-4" /></Button>
                      </DialogTrigger>
                      <DialogContent className="max-w-lg">
                        <DialogHeader>
                          <DialogTitle className="font-heading">{o.orderCode}</DialogTitle>
                        </DialogHeader>
                        <div className="space-y-4">
                          <div className="flex gap-2">
                            <StatusBadge type="availableOrder" status={o.orderStatus} />
                            <StatusBadge type="payment" status={o.paymentStatus} />
                          </div>
                          <div className="space-y-2">
                            {o.items?.map(item => {
                              const prod = getProductById(item.productId);
                              return (
                                <div key={item.id} className="flex justify-between rounded-lg bg-surface-warm p-3 text-sm">
                                  <span>{prod?.name} x{item.quantity}</span>
                                  <span className="font-medium">{item.subtotal.toLocaleString('vi-VN')}₫</span>
                                </div>
                              );
                            })}
                          </div>
                          <div className="flex justify-between border-t pt-3 font-medium">
                            <span>Tổng</span>
                            <span>{o.totalAmount.toLocaleString('vi-VN')}₫</span>
                          </div>
                          <Select>
                            <SelectTrigger><SelectValue placeholder="Cập nhật trạng thái" /></SelectTrigger>
                            <SelectContent>
                              <SelectItem value="processing">Đang xử lý</SelectItem>
                              <SelectItem value="shipping">Đang giao</SelectItem>
                              <SelectItem value="completed">Hoàn thành</SelectItem>
                              <SelectItem value="canceled">Hủy</SelectItem>
                            </SelectContent>
                          </Select>
                          <Button className="w-full rounded-full">Cập nhật</Button>
                        </div>
                      </DialogContent>
                    </Dialog>
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
