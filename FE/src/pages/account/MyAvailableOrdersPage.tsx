import { Link } from 'react-router-dom';
import { mockAvailableOrders } from '@/data/mockData';
import { StatusBadge } from '@/components/shared/StatusBadge';
import { Card, CardContent } from '@/components/ui/card';
import { ArrowRight } from 'lucide-react';

export default function MyAvailableOrdersPage() {
  const orders = mockAvailableOrders;

  return (
    <div className="container max-w-3xl py-8">
      <h1 className="mb-6 font-heading text-2xl font-bold text-heading">Đơn hàng của tôi</h1>

      <div className="space-y-4">
        {orders.map(order => (
          <Link key={order.id} to={`/account/orders/${order.id}`}>
            <Card className="transition-all hover:shadow-md">
              <CardContent className="flex items-center justify-between p-5">
                <div>
                  <div className="flex items-center gap-3">
                    <span className="font-heading font-semibold text-heading">{order.orderCode}</span>
                    <StatusBadge type="availableOrder" status={order.orderStatus} />
                  </div>
                  <p className="mt-1 text-sm text-caption">
                    {new Date(order.orderedAt).toLocaleDateString('vi-VN')} · {order.totalAmount.toLocaleString('vi-VN')}₫
                  </p>
                </div>
                <ArrowRight className="h-5 w-5 text-caption" />
              </CardContent>
            </Card>
          </Link>
        ))}
      </div>
    </div>
  );
}
