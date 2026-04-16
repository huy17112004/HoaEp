import { mockCustomOrders, getProductById } from '@/data/mockData';
import { StatusBadge } from '@/components/shared/StatusBadge';
import { Card, CardContent, CardHeader } from '@/components/ui/card';
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table';
import { Input } from '@/components/ui/input';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import { Button } from '@/components/ui/button';
import { Search, Eye } from 'lucide-react';
import { useState } from 'react';
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from '@/components/ui/dialog';

export default function AdminCustomOrders() {
  const [search, setSearch] = useState('');
  const [statusFilter, setStatusFilter] = useState('all');

  const filtered = mockCustomOrders.filter(o => {
    const matchSearch = !search || o.orderCode.toLowerCase().includes(search.toLowerCase());
    const matchStatus = statusFilter === 'all' || o.orderStatus === statusFilter;
    return matchSearch && matchStatus;
  });

  return (
    <div className="space-y-6">
      <h1 className="font-heading text-2xl font-bold text-heading">Đơn hàng Custom</h1>

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
                <SelectItem value="deposited">Đã đặt cọc</SelectItem>
                <SelectItem value="waiting_flower_review">Chờ đánh giá hoa</SelectItem>
                <SelectItem value="in_progress">Đang thực hiện</SelectItem>
                <SelectItem value="waiting_demo_feedback">Chờ duyệt demo</SelectItem>
                <SelectItem value="waiting_remaining_payment">Chờ thanh toán</SelectItem>
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
                <TableHead>Loại hoa</TableHead>
                <TableHead>Khung tranh</TableHead>
                <TableHead>Tổng tiền</TableHead>
                <TableHead>Thanh toán</TableHead>
                <TableHead>Trạng thái</TableHead>
                <TableHead></TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {filtered.map(o => {
                const frame = getProductById(o.selectedFrameProductId);
                return (
                  <TableRow key={o.id}>
                    <TableCell className="font-medium text-heading">{o.orderCode}</TableCell>
                    <TableCell className="text-body">{o.flowerType}</TableCell>
                    <TableCell className="text-body">{frame?.name || '—'}</TableCell>
                    <TableCell className="font-medium">{o.totalAmount.toLocaleString('vi-VN')}₫</TableCell>
                    <TableCell><StatusBadge type="payment" status={o.paymentStatus} /></TableCell>
                    <TableCell><StatusBadge type="customOrder" status={o.orderStatus} /></TableCell>
                    <TableCell>
                      <Dialog>
                        <DialogTrigger asChild>
                          <Button variant="ghost" size="icon"><Eye className="h-4 w-4" /></Button>
                        </DialogTrigger>
                        <DialogContent className="max-w-lg max-h-[80vh] overflow-y-auto">
                          <DialogHeader>
                            <DialogTitle className="font-heading">{o.orderCode}</DialogTitle>
                          </DialogHeader>
                          <div className="space-y-4">
                            <div className="flex flex-wrap gap-2">
                              <StatusBadge type="customOrder" status={o.orderStatus} />
                              <StatusBadge type="payment" status={o.paymentStatus} />
                              <StatusBadge type="flowerEval" status={o.flowerEvaluationStatus} />
                            </div>
                            <div className="space-y-2 text-sm">
                              <div className="flex justify-between"><span className="text-caption">Khung</span><span>{frame?.name}</span></div>
                              <div className="flex justify-between"><span className="text-caption">Loại hoa</span><span>{o.flowerType}</span></div>
                              <div className="flex justify-between"><span className="text-caption">Yêu cầu</span><span className="text-right max-w-[60%]">{o.personalizationContent}</span></div>
                              <div className="flex justify-between"><span className="text-caption">Đặt cọc</span><span>{o.depositAmount.toLocaleString('vi-VN')}₫</span></div>
                              <div className="flex justify-between"><span className="text-caption">Còn lại</span><span>{o.remainingAmount.toLocaleString('vi-VN')}₫</span></div>
                              <div className="flex justify-between font-medium border-t pt-2"><span>Tổng</span><span>{o.totalAmount.toLocaleString('vi-VN')}₫</span></div>
                              <div className="flex justify-between"><span className="text-caption">Lần chỉnh demo</span><span>{o.demoRevisionCount}</span></div>
                            </div>
                            {o.demos && o.demos.length > 0 && (
                              <div>
                                <p className="text-sm font-medium mb-2">Demo ({o.demos.length})</p>
                                {o.demos.map(d => (
                                  <div key={d.id} className="mb-2 rounded-lg bg-surface-warm p-3 text-sm">
                                    <div className="flex justify-between">
                                      <span>V{d.versionNo}</span>
                                      <StatusBadge type="demoResponse" status={d.customerResponseStatus} />
                                    </div>
                                  </div>
                                ))}
                              </div>
                            )}
                            <Select>
                              <SelectTrigger><SelectValue placeholder="Cập nhật trạng thái" /></SelectTrigger>
                              <SelectContent>
                                <SelectItem value="in_progress">Đang thực hiện</SelectItem>
                                <SelectItem value="waiting_demo_feedback">Chờ duyệt demo</SelectItem>
                                <SelectItem value="waiting_remaining_payment">Chờ thanh toán</SelectItem>
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
                );
              })}
            </TableBody>
          </Table>
        </CardContent>
      </Card>
    </div>
  );
}
