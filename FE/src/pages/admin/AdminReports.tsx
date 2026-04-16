import { mockReportOverview, mockRevenueReport } from '@/data/mockData';
import { StatisticCard } from '@/components/shared/StatisticCard';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Package, ShoppingCart, DollarSign, Warehouse } from 'lucide-react';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, LineChart, Line } from 'recharts';

export default function AdminReports() {
  const overview = mockReportOverview;

  return (
    <div className="space-y-6">
      <h1 className="font-heading text-2xl font-bold text-heading">Báo cáo & Thống kê</h1>

      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
        <StatisticCard title="Tổng sản phẩm" value={overview.totalProducts} icon={Package} />
        <StatisticCard title="Tổng đơn hàng" value={overview.totalOrders} icon={ShoppingCart} />
        <StatisticCard title="Doanh thu" value={`${(overview.totalRevenue / 1000000).toFixed(1)}M₫`} icon={DollarSign} />
        <StatisticCard title="Đang xử lý" value={overview.processingOrders} icon={Warehouse} />
      </div>

      <div className="grid gap-6 lg:grid-cols-2">
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

        <Card>
          <CardHeader><CardTitle className="font-heading text-base">Số đơn hàng theo tháng</CardTitle></CardHeader>
          <CardContent>
            <div className="h-64">
              <ResponsiveContainer width="100%" height="100%">
                <LineChart data={mockRevenueReport}>
                  <CartesianGrid strokeDasharray="3 3" stroke="hsl(35 20% 88%)" />
                  <XAxis dataKey="period" tick={{ fontSize: 12 }} />
                  <YAxis tick={{ fontSize: 12 }} />
                  <Tooltip />
                  <Line type="monotone" dataKey="orderCount" stroke="hsl(30 35% 65%)" strokeWidth={2} dot={{ fill: 'hsl(30 35% 65%)' }} />
                </LineChart>
              </ResponsiveContainer>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
