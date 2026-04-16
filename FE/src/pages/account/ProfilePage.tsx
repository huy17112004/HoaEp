import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { mockUsers } from '@/data/mockData';

export default function ProfilePage() {
  const user = mockUsers[0]; // mock current user

  return (
    <div className="container max-w-2xl py-8">
      <h1 className="mb-6 font-heading text-2xl font-bold text-heading">Hồ sơ cá nhân</h1>

      <Card>
        <CardHeader>
          <CardTitle className="font-heading text-lg">Thông tin cá nhân</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div>
            <Label>Họ tên</Label>
            <Input defaultValue={user.fullName} className="mt-1" />
          </div>
          <div>
            <Label>Email</Label>
            <Input type="email" defaultValue={user.email} className="mt-1" />
          </div>
          <div>
            <Label>Số điện thoại</Label>
            <Input defaultValue={user.phone} className="mt-1" />
          </div>
          <Button className="rounded-full">Cập nhật</Button>
        </CardContent>
      </Card>
    </div>
  );
}
