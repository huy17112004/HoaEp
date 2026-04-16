import { Card, CardContent } from '@/components/ui/card';
import { cn } from '@/lib/utils';
import type { LucideIcon } from 'lucide-react';

interface StatisticCardProps {
  title: string;
  value: string | number;
  icon: LucideIcon;
  description?: string;
  trend?: 'up' | 'down' | 'neutral';
  className?: string;
}

export function StatisticCard({ title, value, icon: Icon, description, className }: StatisticCardProps) {
  return (
    <Card className={cn('bg-card', className)}>
      <CardContent className="p-6">
        <div className="flex items-start justify-between">
          <div>
            <p className="text-sm font-medium text-caption">{title}</p>
            <p className="mt-2 text-2xl font-heading font-bold text-heading">{value}</p>
            {description && <p className="mt-1 text-xs text-caption">{description}</p>}
          </div>
          <div className="rounded-lg bg-primary/10 p-3">
            <Icon className="h-5 w-5 text-primary" />
          </div>
        </div>
      </CardContent>
    </Card>
  );
}
