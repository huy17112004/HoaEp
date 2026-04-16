import { Badge } from '@/components/ui/badge';
import type { AvailableOrderStatus, CustomOrderStatus, PaymentStatus, FlowerEvaluationStatus, DemoResponseStatus } from '@/types';
import { cn } from '@/lib/utils';

const availableOrderLabels: Record<AvailableOrderStatus, { label: string; className: string }> = {
  received: { label: 'Đã tiếp nhận', className: 'bg-blue-100 text-blue-800 border-blue-200' },
  processing: { label: 'Đang xử lý', className: 'bg-amber-100 text-amber-800 border-amber-200' },
  shipping: { label: 'Đang giao', className: 'bg-purple-100 text-purple-800 border-purple-200' },
  completed: { label: 'Hoàn thành', className: 'bg-green-100 text-green-800 border-green-200' },
  canceled: { label: 'Đã hủy', className: 'bg-red-100 text-red-800 border-red-200' },
};

const customOrderLabels: Record<CustomOrderStatus, { label: string; className: string }> = {
  deposited: { label: 'Đã đặt cọc', className: 'bg-blue-100 text-blue-800 border-blue-200' },
  waiting_flower_review: { label: 'Chờ đánh giá hoa', className: 'bg-amber-100 text-amber-800 border-amber-200' },
  in_progress: { label: 'Đang thực hiện', className: 'bg-indigo-100 text-indigo-800 border-indigo-200' },
  waiting_demo_feedback: { label: 'Chờ duyệt demo', className: 'bg-purple-100 text-purple-800 border-purple-200' },
  waiting_remaining_payment: { label: 'Chờ thanh toán', className: 'bg-orange-100 text-orange-800 border-orange-200' },
  completed: { label: 'Hoàn thành', className: 'bg-green-100 text-green-800 border-green-200' },
  canceled: { label: 'Đã hủy', className: 'bg-red-100 text-red-800 border-red-200' },
};

const paymentLabels: Record<PaymentStatus, { label: string; className: string }> = {
  unpaid: { label: 'Chưa thanh toán', className: 'bg-gray-100 text-gray-800 border-gray-200' },
  paid: { label: 'Đã thanh toán', className: 'bg-green-100 text-green-800 border-green-200' },
  partial: { label: 'Đã cọc', className: 'bg-amber-100 text-amber-800 border-amber-200' },
  refunded: { label: 'Đã hoàn tiền', className: 'bg-red-100 text-red-800 border-red-200' },
};

const flowerEvalLabels: Record<FlowerEvaluationStatus, { label: string; className: string }> = {
  pending: { label: 'Chờ đánh giá', className: 'bg-gray-100 text-gray-800 border-gray-200' },
  approved: { label: 'Đạt yêu cầu', className: 'bg-green-100 text-green-800 border-green-200' },
  rejected: { label: 'Không đạt', className: 'bg-red-100 text-red-800 border-red-200' },
};

const demoResponseLabels: Record<DemoResponseStatus, { label: string; className: string }> = {
  pending: { label: 'Chờ phản hồi', className: 'bg-amber-100 text-amber-800 border-amber-200' },
  approved: { label: 'Đã duyệt', className: 'bg-green-100 text-green-800 border-green-200' },
  revision_requested: { label: 'Yêu cầu chỉnh sửa', className: 'bg-orange-100 text-orange-800 border-orange-200' },
};

type StatusType = 'availableOrder' | 'customOrder' | 'payment' | 'flowerEval' | 'demoResponse';

interface StatusBadgeProps {
  type: StatusType;
  status: string;
}

export function StatusBadge({ type, status }: StatusBadgeProps) {
  let config: { label: string; className: string } | undefined;

  switch (type) {
    case 'availableOrder': config = availableOrderLabels[status as AvailableOrderStatus]; break;
    case 'customOrder': config = customOrderLabels[status as CustomOrderStatus]; break;
    case 'payment': config = paymentLabels[status as PaymentStatus]; break;
    case 'flowerEval': config = flowerEvalLabels[status as FlowerEvaluationStatus]; break;
    case 'demoResponse': config = demoResponseLabels[status as DemoResponseStatus]; break;
  }

  if (!config) return <Badge variant="secondary">{status}</Badge>;

  return (
    <Badge variant="outline" className={cn('font-medium', config.className)}>
      {config.label}
    </Badge>
  );
}
