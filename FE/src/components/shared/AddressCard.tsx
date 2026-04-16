import type { CustomerAddress } from '@/types';
import { Card, CardContent } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { MapPin, Phone, User } from 'lucide-react';

interface AddressCardProps {
  address: CustomerAddress;
  onEdit?: () => void;
  onDelete?: () => void;
  onSetDefault?: () => void;
  selected?: boolean;
  selectable?: boolean;
  onSelect?: () => void;
}

export function AddressCard({ address, onEdit, onDelete, onSetDefault, selected, selectable, onSelect }: AddressCardProps) {
  return (
    <Card
      className={`cursor-${selectable ? 'pointer' : 'default'} transition-all ${
        selected ? 'border-primary ring-2 ring-primary/20' : 'border-border'
      }`}
      onClick={selectable ? onSelect : undefined}
    >
      <CardContent className="p-4">
        <div className="flex items-start justify-between">
          <div className="space-y-2">
            <div className="flex items-center gap-2">
              <User className="h-4 w-4 text-caption" />
              <span className="text-sm font-medium text-heading">{address.receiverName}</span>
              {address.isDefault && <Badge variant="secondary" className="text-xs">Mặc định</Badge>}
            </div>
            <div className="flex items-center gap-2">
              <Phone className="h-4 w-4 text-caption" />
              <span className="text-sm text-body">{address.receiverPhone}</span>
            </div>
            <div className="flex items-start gap-2">
              <MapPin className="mt-0.5 h-4 w-4 text-caption shrink-0" />
              <span className="text-sm text-body">
                {address.addressLine}, {address.ward}, {address.district}, {address.province}
              </span>
            </div>
          </div>
        </div>
        {(onEdit || onDelete || onSetDefault) && (
          <div className="mt-3 flex gap-2 border-t pt-3">
            {onEdit && <button onClick={onEdit} className="text-xs text-primary hover:underline">Sửa</button>}
            {!address.isDefault && onSetDefault && (
              <button onClick={onSetDefault} className="text-xs text-primary hover:underline">Đặt mặc định</button>
            )}
            {onDelete && <button onClick={onDelete} className="text-xs text-destructive hover:underline">Xóa</button>}
          </div>
        )}
      </CardContent>
    </Card>
  );
}
