import { useParams, Link } from 'react-router-dom';
import { mockCustomOrders, getProductById } from '@/data/mockData';
import { StatusBadge } from '@/components/shared/StatusBadge';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Textarea } from '@/components/ui/textarea';
import { ArrowLeft } from 'lucide-react';

export default function CustomOrderDetailPage() {
  const { id } = useParams();
  const order = mockCustomOrders.find(o => o.id === id);

  if (!order) {
    return <div className="container py-16 text-center text-caption">Đơn hàng không tồn tại.</div>;
  }

  const frame = getProductById(order.selectedFrameProductId);

  return (
    <div className="container max-w-3xl py-8">
      <Link to="/account/custom-orders" className="mb-6 inline-flex items-center gap-1 text-sm text-caption hover:text-primary">
        <ArrowLeft className="h-4 w-4" /> Quay lại
      </Link>

      <div className="mb-6 flex items-center gap-3">
        <h1 className="font-heading text-2xl font-bold text-heading">{order.orderCode}</h1>
        <StatusBadge type="customOrder" status={order.orderStatus} />
      </div>

      <div className="space-y-6">
        {/* Order info */}
        <Card>
          <CardHeader><CardTitle className="font-heading text-base">Thông tin đơn hàng</CardTitle></CardHeader>
          <CardContent className="space-y-3 text-sm">
            <div className="flex justify-between"><span className="text-caption">Khung tranh</span><span className="text-heading font-medium">{frame?.name || '—'}</span></div>
            <div className="flex justify-between"><span className="text-caption">Loại hoa</span><span className="text-heading">{order.flowerType}</span></div>
            <div className="flex justify-between"><span className="text-caption">Yêu cầu</span><span className="text-heading text-right max-w-[60%]">{order.personalizationContent}</span></div>
            {order.requestedDeliveryDate && (
              <div className="flex justify-between"><span className="text-caption">Ngày mong muốn</span><span className="text-heading">{new Date(order.requestedDeliveryDate).toLocaleDateString('vi-VN')}</span></div>
            )}
            <div className="flex justify-between"><span className="text-caption">Đánh giá hoa</span><StatusBadge type="flowerEval" status={order.flowerEvaluationStatus} /></div>
          </CardContent>
        </Card>

        {/* Payment */}
        <Card>
          <CardHeader><CardTitle className="font-heading text-base">Thanh toán</CardTitle></CardHeader>
          <CardContent className="space-y-2 text-sm">
            <div className="flex justify-between"><span className="text-caption">Đặt cọc</span><span className="text-heading font-medium">{order.depositAmount.toLocaleString('vi-VN')}₫</span></div>
            <div className="flex justify-between"><span className="text-caption">Còn lại</span><span className="text-heading font-medium">{order.remainingAmount.toLocaleString('vi-VN')}₫</span></div>
            <div className="flex justify-between border-t pt-2"><span className="font-medium text-heading">Tổng cộng</span><span className="font-bold text-heading">{order.totalAmount.toLocaleString('vi-VN')}₫</span></div>
            <div className="flex justify-between"><span className="text-caption">Trạng thái</span><StatusBadge type="payment" status={order.paymentStatus} /></div>
          </CardContent>
        </Card>

        {/* Demos */}
        {order.demos && order.demos.length > 0 && (
          <Card>
            <CardHeader><CardTitle className="font-heading text-base">Demo ({order.demos.length} phiên bản)</CardTitle></CardHeader>
            <CardContent className="space-y-4">
              {order.demos.map(demo => (
                <div key={demo.id} className="rounded-xl border bg-surface-warm p-4">
                  <div className="flex items-center justify-between mb-3">
                    <span className="font-medium text-heading">Phiên bản {demo.versionNo}</span>
                    <StatusBadge type="demoResponse" status={demo.customerResponseStatus} />
                  </div>
                  <div className="aspect-video rounded-lg bg-secondary overflow-hidden mb-3">
                    <img src={demo.demoImageUrl} alt={`Demo v${demo.versionNo}`} className="h-full w-full object-cover" />
                  </div>
                  {demo.demoDescription && <p className="text-sm text-body mb-3">{demo.demoDescription}</p>}
                  {demo.customerFeedback && (
                    <div className="rounded-lg bg-clay-light p-3">
                      <p className="text-xs text-caption mb-1">Phản hồi của bạn:</p>
                      <p className="text-sm text-heading">{demo.customerFeedback}</p>
                    </div>
                  )}
                  {demo.customerResponseStatus === 'pending' && (
                    <div className="mt-4 space-y-3">
                      <Textarea placeholder="Nhập phản hồi chỉnh sửa (nếu có)..." />
                      <div className="flex gap-2">
                        <Button className="rounded-full">Duyệt demo</Button>
                        <Button variant="outline" className="rounded-full">Yêu cầu chỉnh sửa</Button>
                      </div>
                    </div>
                  )}
                </div>
              ))}
            </CardContent>
          </Card>
        )}

        {/* Remaining payment */}
        {order.orderStatus === 'waiting_remaining_payment' && (
          <Card className="border-accent">
            <CardHeader><CardTitle className="font-heading text-base text-accent">Thanh toán phần còn lại</CardTitle></CardHeader>
            <CardContent>
              <p className="mb-4 text-sm text-body">
                Số tiền cần thanh toán: <strong className="text-heading">{order.remainingAmount.toLocaleString('vi-VN')}₫</strong>
              </p>
              <Button className="rounded-full">Thanh toán ngay</Button>
            </CardContent>
          </Card>
        )}
      </div>
    </div>
  );
}
