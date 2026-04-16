import { PackageOpen } from 'lucide-react';

interface EmptyStateProps {
  title?: string;
  description?: string;
  action?: React.ReactNode;
}

export function EmptyState({
  title = 'Không có dữ liệu',
  description = 'Chưa có dữ liệu nào để hiển thị.',
  action,
}: EmptyStateProps) {
  return (
    <div className="flex flex-col items-center justify-center py-16 text-center">
      <PackageOpen className="mb-4 h-12 w-12 text-muted-foreground/50" />
      <h3 className="font-heading text-lg font-semibold text-heading">{title}</h3>
      <p className="mt-1 text-sm text-caption">{description}</p>
      {action && <div className="mt-4">{action}</div>}
    </div>
  );
}
