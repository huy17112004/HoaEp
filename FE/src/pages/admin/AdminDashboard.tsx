import { StatisticCard } from '@/components/shared/StatisticCard';
import { mockReportOverview, mockRevenueReport, mockAvailableOrders, mockCustomOrders } from '@/data/mockData';
import { Package, ShoppingCart, DollarSign, Warehouse, Palette, TrendingUp } from 'lucide-react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { StatusBadge } from '@/components/shared/StatusBadge';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';

export default function AdminDashboard() {
  const overview = mockReportOverview;
  const recentAvailable = mockAvailableOrders.slice(0, 3);
  const recentCustom = mockCustomOrders.slice(0, 3);

  return (
    <div className="space-y-6">
      <h1 className="font-heading text-2xl font-bold text-heading">Dashboard</h1>

      {/* Stats */}
      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-6">
        <StatisticCard title="Sản phẩm" value={overview.totalProducts} icon={Package} />
        <StatisticCard title="Đơn hàng" value={overview.totalOrders} icon={ShoppingCart} />
        <StatisticCard title="Đang xử lý" value={overview.processingOrders} icon={TrendingUp} />
        <StatisticCard title="Hoàn thành" value={overview.completedOrders} icon={ShoppingCart} />
        <StatisticCard title="Đơn Custom" value={overview.totalCustomOrders} icon={Palette} />
        <StatisticCard title="Doanh thu" value={`${(overview.totalRevenue / 1000000).toFixed(1)}M`} icon={DollarSign} />
      </div>

      {/* Revenue chart */}
      <Card>
        <CardHeader><CardTitle className="font-heading text-base">Doanh thu theo tháng</CardTitle></CardHeader>
        <CardContent>
          <div className="h-64">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={mockRevenueReport}>
                <CartesianGrid strokeDasharray="3 3" stroke="hsl(35 20% 88%)" />
                <XAxis dataKey="period" tick={{ fontSize: 12 }} />
                <YAxis tick={{ fontSize: 12 }} tickFormatter={v => `${v / 1000}k`} />
                <Tooltip formatter={(value: number) => [`${value.toLocaleString('vi-VN')}₫`, 'Doanh thu']} />
                <Bar dataKey="revenue" fill="hsl(90 15% 55%)" radius={[6, 6, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </CardContent>
      </Card>

      {/* Recent orders */}
      <div className="grid gap-6 lg:grid-cols-2">
        <Card>
          <CardHeader><CardTitle className="font-heading text-base">Đơn hàng thường gần đây</CardTitle></CardHeader>
          <CardContent className="space-y-3">
            {recentAvailable.map(o => (
              <div key={o.id} className="flex items-center justify-between rounded-lg bg-surface-warm p-3">
                <div>
                  <span className="text-sm font-medium text-heading">{o.orderCode}</span>
                  <p className="text-xs text-caption">{o.totalAmount.toLocaleString('vi-VN')}₫</p>
                </div>
                <StatusBadge type="availableOrder" status={o.orderStatus} />
              </div>
            ))}
          </CardContent>
        </Card>

        <Card>
          <CardHeader><CardTitle className="font-heading text-base">Đơn hàng custom gần đây</CardTitle></CardHeader>
          <CardContent className="space-y-3">
            {recentCustom.map(o => (
              <div key={o.id} className="flex items-center justify-between rounded-lg bg-surface-warm p-3">
                <div>
                  <span className="text-sm font-medium text-heading">{o.orderCode}</span>
                  <p className="text-xs text-caption">{o.flowerType}</p>
                </div>
                <StatusBadge type="customOrder" status={o.orderStatus} />
              </div>
            ))}
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
