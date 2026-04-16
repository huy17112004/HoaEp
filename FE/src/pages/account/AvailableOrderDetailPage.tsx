import { useParams, Link } from 'react-router-dom';
import { mockAvailableOrders, getProductById } from '@/data/mockData';
import { StatusBadge } from '@/components/shared/StatusBadge';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { ArrowLeft } from 'lucide-react';

export default function AvailableOrderDetailPage() {
  const { id } = useParams();
  const order = mockAvailableOrders.find(o => o.id === id);

  if (!order) {
    return <div className="container py-16 text-center text-caption">Đơn hàng không tồn tại.</div>;
  }

  return (
    <div className="container max-w-3xl py-8">
      <Link to="/account/orders" className="mb-6 inline-flex items-center gap-1 text-sm text-caption hover:text-primary">
        <ArrowLeft className="h-4 w-4" /> Quay lại
      </Link>

      <div className="mb-6 flex items-center gap-3">
        <h1 className="font-heading text-2xl font-bold text-heading">{order.orderCode}</h1>
        <StatusBadge type="availableOrder" status={order.orderStatus} />
      </div>

      <div className="space-y-6">
        <Card>
          <CardHeader><CardTitle className="font-heading text-base">Sản phẩm</CardTitle></CardHeader>
          <CardContent className="space-y-3">
            {order.items?.map(item => {
              const product = getProductById(item.productId);
              return (
                <div key={item.id} className="flex items-center justify-between rounded-lg bg-surface-warm p-3">
                  <div>
                    <span className="font-medium text-heading">{product?.name || item.productId}</span>
                    <span className="ml-2 text-sm text-caption">x{item.quantity}</span>
                  </div>
                  <span className="font-medium text-heading">{item.subtotal.toLocaleString('vi-VN')}₫</span>
                </div>
              );
            })}
          </CardContent>
        </Card>

        <Card>
          <CardHeader><CardTitle className="font-heading text-base">Thanh toán</CardTitle></CardHeader>
          <CardContent className="space-y-2">
            <div className="flex justify-between text-sm"><span className="text-caption">Tổng cộng</span><span className="font-semibold text-heading">{order.totalAmount.toLocaleString('vi-VN')}₫</span></div>
            <div className="flex justify-between text-sm"><span className="text-caption">Trạng thái</span><StatusBadge type="payment" status={order.paymentStatus} /></div>
            {order.paymentMethod && <div className="flex justify-between text-sm"><span className="text-caption">Phương thức</span><span className="text-heading">{order.paymentMethod}</span></div>}
          </CardContent>
        </Card>

        {order.note && (
          <Card>
            <CardHeader><CardTitle className="font-heading text-base">Ghi chú</CardTitle></CardHeader>
            <CardContent><p className="text-sm text-body">{order.note}</p></CardContent>
          </Card>
        )}
      </div>
    </div>
  );
}
