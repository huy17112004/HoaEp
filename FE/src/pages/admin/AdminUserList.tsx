import { mockUsers } from '@/data/mockData';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader } from '@/components/ui/card';
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table';
import { Badge } from '@/components/ui/badge';
import { Input } from '@/components/ui/input';
import { Plus, Search, Pencil } from 'lucide-react';
import { useState } from 'react';

const roleLabels: Record<string, string> = { 'role-1': 'Admin', 'role-2': 'Nhân viên', 'role-3': 'Khách hàng' };

export default function AdminUserList() {
  const [search, setSearch] = useState('');
  const filtered = mockUsers.filter(u => !search || u.fullName.toLowerCase().includes(search.toLowerCase()) || u.email.includes(search));

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="font-heading text-2xl font-bold text-heading">Quản lý người dùng</h1>
        <Button className="gap-2 rounded-full"><Plus className="h-4 w-4" /> Thêm người dùng</Button>
      </div>

      <Card>
        <CardHeader>
          <div className="relative max-w-sm">
            <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-caption" />
            <Input placeholder="Tìm kiếm..." value={search} onChange={e => setSearch(e.target.value)} className="pl-10" />
          </div>
        </CardHeader>
        <CardContent>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Họ tên</TableHead>
                <TableHead>Email</TableHead>
                <TableHead>SĐT</TableHead>
                <TableHead>Vai trò</TableHead>
                <TableHead>Trạng thái</TableHead>
                <TableHead></TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {filtered.map(u => (
                <TableRow key={u.id}>
                  <TableCell className="font-medium text-heading">{u.fullName}</TableCell>
                  <TableCell className="text-body">{u.email}</TableCell>
                  <TableCell className="text-body">{u.phone}</TableCell>
                  <TableCell><Badge variant="secondary" className="text-xs">{roleLabels[u.roleId] || u.roleId}</Badge></TableCell>
                  <TableCell>
                    <Badge variant={u.status === 'active' ? 'default' : 'secondary'} className="text-xs">
                      {u.status === 'active' ? 'Hoạt động' : 'Khóa'}
                    </Badge>
                  </TableCell>
                  <TableCell><Button variant="ghost" size="icon"><Pencil className="h-4 w-4" /></Button></TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </CardContent>
      </Card>
    </div>
  );
}
