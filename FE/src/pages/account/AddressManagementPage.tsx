import { useState } from 'react';
import { Button } from '@/components/ui/button';
import { AddressCard } from '@/components/shared/AddressCard';
import { mockAddresses } from '@/data/mockData';
import { Plus } from 'lucide-react';
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from '@/components/ui/dialog';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';

export default function AddressManagementPage() {
  const [addresses] = useState(mockAddresses);

  return (
    <div className="container max-w-2xl py-8">
      <div className="mb-6 flex items-center justify-between">
        <h1 className="font-heading text-2xl font-bold text-heading">Địa chỉ giao hàng</h1>
        <Dialog>
          <DialogTrigger asChild>
            <Button size="sm" className="gap-2 rounded-full">
              <Plus className="h-4 w-4" /> Thêm địa chỉ
            </Button>
          </DialogTrigger>
          <DialogContent>
            <DialogHeader>
              <DialogTitle className="font-heading">Thêm địa chỉ mới</DialogTitle>
            </DialogHeader>
            <form className="space-y-4">
              <div><Label>Tên người nhận</Label><Input className="mt-1" /></div>
              <div><Label>Số điện thoại</Label><Input className="mt-1" /></div>
              <div><Label>Địa chỉ</Label><Input className="mt-1" /></div>
              <div className="grid grid-cols-3 gap-3">
                <div><Label>Phường/Xã</Label><Input className="mt-1" /></div>
                <div><Label>Quận/Huyện</Label><Input className="mt-1" /></div>
                <div><Label>Tỉnh/TP</Label><Input className="mt-1" /></div>
              </div>
              <Button className="w-full rounded-full">Lưu địa chỉ</Button>
            </form>
          </DialogContent>
        </Dialog>
      </div>

      <div className="space-y-4">
        {addresses.map(addr => (
          <AddressCard
            key={addr.id}
            address={addr}
            onEdit={() => {}}
            onDelete={() => {}}
            onSetDefault={() => {}}
          />
        ))}
      </div>
    </div>
  );
}
