import type {
  Product, ProductCategory, User, CustomerAddress,
  AvailableOrder, AvailableOrderItem, CustomOrder, CustomDemo,
  PurchaseReceipt, PurchaseReceiptItem, InventoryItem, ReportOverview, RevenueReportItem
} from '@/types';

// Categories
export const mockCategories: ProductCategory[] = [
  { id: 'cat-1', name: 'Tranh hoa ép', description: 'Tranh làm từ hoa ép khô tự nhiên', status: 'active' },
  { id: 'cat-2', name: 'Bookmark hoa', description: 'Bookmark hoa ép handmade', status: 'active' },
  { id: 'cat-3', name: 'Khung tranh', description: 'Khung tranh dùng cho đơn custom', status: 'active' },
  { id: 'cat-4', name: 'Phụ kiện hoa ép', description: 'Phụ kiện trang trí từ hoa ép', status: 'active' },
];

// Products
export const mockProducts: Product[] = [
  {
    id: 'prod-1', categoryId: 'cat-1', name: 'Tranh Hoa Hồng Vintage',
    slug: 'tranh-hoa-hong-vintage', description: 'Tranh hoa hồng ép khô phong cách vintage, mỗi bức là một tác phẩm nghệ thuật độc nhất.',
    price: 450000, productKind: 'standard_product', isSellableDirectly: true, isCustomSelectable: false,
    imageUrl: '/placeholder.svg', size: '20x30cm', material: 'Gỗ sồi + kính', flowerType: 'Hoa hồng',
    status: 'active', createdAt: '2024-01-15', updatedAt: '2024-01-15',
  },
  {
    id: 'prod-2', categoryId: 'cat-1', name: 'Tranh Hoa Cúc Họa Mi',
    slug: 'tranh-hoa-cuc-hoa-mi', description: 'Tranh hoa cúc họa mi trong suốt, mang vẻ đẹp mộc mạc của đồng quê.',
    price: 380000, productKind: 'standard_product', isSellableDirectly: true, isCustomSelectable: false,
    imageUrl: '/placeholder.svg', size: '15x20cm', material: 'Gỗ thông + kính', flowerType: 'Hoa cúc họa mi',
    status: 'active', createdAt: '2024-01-20', updatedAt: '2024-01-20',
  },
  {
    id: 'prod-3', categoryId: 'cat-2', name: 'Bookmark Lavender Dream',
    slug: 'bookmark-lavender-dream', description: 'Bookmark hoa lavender ép khô, món quà hoàn hảo cho người yêu sách.',
    price: 85000, productKind: 'standard_product', isSellableDirectly: true, isCustomSelectable: false,
    imageUrl: '/placeholder.svg', size: '5x15cm', material: 'Nhựa resin + hoa thật', flowerType: 'Lavender',
    status: 'active', createdAt: '2024-02-01', updatedAt: '2024-02-01',
  },
  {
    id: 'prod-4', categoryId: 'cat-1', name: 'Tranh Hoa Dại Mùa Xuân',
    slug: 'tranh-hoa-dai-mua-xuan', description: 'Bức tranh với những bông hoa dại nhiều màu sắc, gợi nhớ mùa xuân ấm áp.',
    price: 520000, productKind: 'standard_product', isSellableDirectly: true, isCustomSelectable: false,
    imageUrl: '/placeholder.svg', size: '25x35cm', material: 'Gỗ sồi + kính', flowerType: 'Hoa dại',
    status: 'active', createdAt: '2024-02-10', updatedAt: '2024-02-10',
  },
  {
    id: 'prod-5', categoryId: 'cat-4', name: 'Ốp Điện Thoại Hoa Ép',
    slug: 'op-dien-thoai-hoa-ep', description: 'Ốp điện thoại trong suốt với hoa ép thật bên trong.',
    price: 250000, productKind: 'standard_product', isSellableDirectly: true, isCustomSelectable: false,
    imageUrl: '/placeholder.svg', size: 'iPhone 15', material: 'Nhựa trong + hoa thật', flowerType: 'Hoa baby',
    status: 'active', createdAt: '2024-02-15', updatedAt: '2024-02-15',
  },
  {
    id: 'prod-6', categoryId: 'cat-2', name: 'Bookmark Sakura',
    slug: 'bookmark-sakura', description: 'Bookmark với cánh hoa anh đào ép tinh tế.',
    price: 95000, productKind: 'standard_product', isSellableDirectly: true, isCustomSelectable: false,
    imageUrl: '/placeholder.svg', size: '5x15cm', material: 'Nhựa resin + hoa thật', flowerType: 'Hoa anh đào',
    status: 'active', createdAt: '2024-03-01', updatedAt: '2024-03-01',
  },
  // Frame options for custom orders
  {
    id: 'frame-1', categoryId: 'cat-3', name: 'Khung Gỗ Sồi Classic',
    slug: 'khung-go-soi-classic', description: 'Khung tranh gỗ sồi tự nhiên phong cách cổ điển.',
    price: 200000, productKind: 'frame_option', isSellableDirectly: false, isCustomSelectable: true,
    imageUrl: '/placeholder.svg', size: '20x30cm', material: 'Gỗ sồi', flowerType: undefined,
    status: 'active', createdAt: '2024-01-01', updatedAt: '2024-01-01',
  },
  {
    id: 'frame-2', categoryId: 'cat-3', name: 'Khung Gỗ Thông Natural',
    slug: 'khung-go-thong-natural', description: 'Khung tranh gỗ thông màu tự nhiên, nhẹ nhàng và thanh lịch.',
    price: 150000, productKind: 'frame_option', isSellableDirectly: false, isCustomSelectable: true,
    imageUrl: '/placeholder.svg', size: '15x20cm', material: 'Gỗ thông', flowerType: undefined,
    status: 'active', createdAt: '2024-01-01', updatedAt: '2024-01-01',
  },
  {
    id: 'frame-3', categoryId: 'cat-3', name: 'Khung Acrylic Trong Suốt',
    slug: 'khung-acrylic-trong-suot', description: 'Khung acrylic trong suốt hiện đại, tôn lên vẻ đẹp hoa ép.',
    price: 280000, productKind: 'frame_option', isSellableDirectly: false, isCustomSelectable: true,
    imageUrl: '/placeholder.svg', size: '25x35cm', material: 'Acrylic', flowerType: undefined,
    status: 'active', createdAt: '2024-01-01', updatedAt: '2024-01-01',
  },
];

// Users
export const mockUsers: User[] = [
  { id: 'user-1', fullName: 'Nguyễn Thị Mai', phone: '0901234567', email: 'mai@example.com', roleId: 'role-3', status: 'active', createdAt: '2024-01-10', updatedAt: '2024-01-10' },
  { id: 'user-2', fullName: 'Trần Văn Hùng', phone: '0912345678', email: 'hung@example.com', roleId: 'role-2', status: 'active', createdAt: '2024-01-05', updatedAt: '2024-01-05' },
  { id: 'user-3', fullName: 'Lê Thị Hoa', phone: '0923456789', email: 'hoa@example.com', roleId: 'role-1', status: 'active', createdAt: '2024-01-01', updatedAt: '2024-01-01' },
  { id: 'user-4', fullName: 'Phạm Minh Anh', phone: '0934567890', email: 'minhanh@example.com', roleId: 'role-3', status: 'active', createdAt: '2024-02-01', updatedAt: '2024-02-01' },
];

// Addresses
export const mockAddresses: CustomerAddress[] = [
  { id: 'addr-1', customerUserId: 'user-1', receiverName: 'Nguyễn Thị Mai', receiverPhone: '0901234567', addressLine: '123 Nguyễn Huệ', ward: 'Phường Bến Nghé', district: 'Quận 1', province: 'TP. Hồ Chí Minh', isDefault: true, createdAt: '2024-01-10', updatedAt: '2024-01-10' },
  { id: 'addr-2', customerUserId: 'user-1', receiverName: 'Nguyễn Văn An', receiverPhone: '0909876543', addressLine: '456 Lê Lợi', ward: 'Phường 6', district: 'Quận 3', province: 'TP. Hồ Chí Minh', isDefault: false, createdAt: '2024-01-15', updatedAt: '2024-01-15' },
];

// Available Orders
export const mockAvailableOrders: AvailableOrder[] = [
  {
    id: 'ao-1', orderCode: 'DF-2024-001', customerUserId: 'user-1',
    shippingAddressId: 'addr-1', orderStatus: 'completed', paymentStatus: 'paid',
    paymentMethod: 'bank_transfer', totalAmount: 535000, orderedAt: '2024-02-10',
    completedAt: '2024-02-18', note: 'Gói quà đẹp giúp em',
    items: [
      { id: 'aoi-1', availableOrderId: 'ao-1', productId: 'prod-3', quantity: 1, unitPrice: 85000, subtotal: 85000 },
      { id: 'aoi-2', availableOrderId: 'ao-1', productId: 'prod-1', quantity: 1, unitPrice: 450000, subtotal: 450000 },
    ],
  },
  {
    id: 'ao-2', orderCode: 'DF-2024-002', customerUserId: 'user-4',
    shippingAddressId: 'addr-1', orderStatus: 'processing', paymentStatus: 'paid',
    paymentMethod: 'momo', totalAmount: 380000, orderedAt: '2024-03-05',
    items: [
      { id: 'aoi-3', availableOrderId: 'ao-2', productId: 'prod-2', quantity: 1, unitPrice: 380000, subtotal: 380000 },
    ],
  },
  {
    id: 'ao-3', orderCode: 'DF-2024-003', customerUserId: 'user-1',
    shippingAddressId: 'addr-2', orderStatus: 'shipping', paymentStatus: 'paid',
    paymentMethod: 'bank_transfer', totalAmount: 250000, orderedAt: '2024-03-10',
    items: [
      { id: 'aoi-4', availableOrderId: 'ao-3', productId: 'prod-5', quantity: 1, unitPrice: 250000, subtotal: 250000 },
    ],
  },
];

// Custom Orders
export const mockCustomOrders: CustomOrder[] = [
  {
    id: 'co-1', orderCode: 'DF-C-2024-001', customerUserId: 'user-1',
    shippingAddressId: 'addr-1', selectedFrameProductId: 'frame-1',
    orderStatus: 'waiting_demo_feedback', paymentStatus: 'partial',
    depositAmount: 150000, remainingAmount: 350000, totalAmount: 500000,
    depositPaymentMethod: 'bank_transfer', depositPaidAt: '2024-02-20',
    flowerType: 'Hoa hồng', personalizationContent: 'Tranh kỷ niệm ngày cưới, thêm chữ "Forever & Always"',
    requestedDeliveryDate: '2024-03-20', flowerInputImageUrl: '/placeholder.svg',
    flowerEvaluationStatus: 'approved', demoRevisionCount: 1,
    orderedAt: '2024-02-20',
    demos: [
      {
        id: 'demo-1', customOrderId: 'co-1', versionNo: 1,
        demoImageUrl: '/placeholder.svg', demoDescription: 'Demo lần 1 - layout cơ bản',
        customerResponseStatus: 'revision_requested', customerFeedback: 'Muốn thay đổi vị trí chữ sang góc phải',
        uploadedBy: 'user-2', uploadedAt: '2024-03-01', respondedAt: '2024-03-02',
      },
      {
        id: 'demo-2', customOrderId: 'co-1', versionNo: 2,
        demoImageUrl: '/placeholder.svg', demoDescription: 'Demo lần 2 - đã chỉnh vị trí chữ',
        customerResponseStatus: 'pending', uploadedBy: 'user-2', uploadedAt: '2024-03-05',
      },
    ],
  },
  {
    id: 'co-2', orderCode: 'DF-C-2024-002', customerUserId: 'user-4',
    shippingAddressId: 'addr-1', selectedFrameProductId: 'frame-3',
    orderStatus: 'in_progress', paymentStatus: 'partial',
    depositAmount: 200000, remainingAmount: 480000, totalAmount: 680000,
    depositPaymentMethod: 'momo', depositPaidAt: '2024-03-01',
    flowerType: 'Hoa cúc + Hoa baby', personalizationContent: 'Tranh tặng sinh nhật mẹ',
    requestedDeliveryDate: '2024-04-01', flowerInputImageUrl: '/placeholder.svg',
    flowerEvaluationStatus: 'approved', demoRevisionCount: 0,
    orderedAt: '2024-03-01',
  },
];

// Purchase Receipts
export const mockPurchaseReceipts: PurchaseReceipt[] = [
  {
    id: 'pr-1', receiptCode: 'PN-2024-001', receiptDate: '2024-01-15',
    createdBy: 'user-2', note: 'Nhập khung tranh đợt 1', createdAt: '2024-01-15',
    items: [
      { id: 'pri-1', purchaseReceiptId: 'pr-1', productId: 'frame-1', quantity: 20, unitCost: 120000, subtotal: 2400000 },
      { id: 'pri-2', purchaseReceiptId: 'pr-1', productId: 'frame-2', quantity: 30, unitCost: 90000, subtotal: 2700000 },
    ],
  },
  {
    id: 'pr-2', receiptCode: 'PN-2024-002', receiptDate: '2024-02-01',
    createdBy: 'user-2', note: 'Nhập sản phẩm hoàn thiện', createdAt: '2024-02-01',
    items: [
      { id: 'pri-3', purchaseReceiptId: 'pr-2', productId: 'prod-1', quantity: 10, unitCost: 250000, subtotal: 2500000 },
      { id: 'pri-4', purchaseReceiptId: 'pr-2', productId: 'prod-3', quantity: 50, unitCost: 40000, subtotal: 2000000 },
    ],
  },
];

// Inventory
export const mockInventory: InventoryItem[] = [
  { id: 'inv-1', productId: 'prod-1', quantityOnHand: 8, updatedAt: '2024-03-10' },
  { id: 'inv-2', productId: 'prod-2', quantityOnHand: 12, updatedAt: '2024-03-10' },
  { id: 'inv-3', productId: 'prod-3', quantityOnHand: 45, updatedAt: '2024-03-10' },
  { id: 'inv-4', productId: 'prod-4', quantityOnHand: 5, updatedAt: '2024-03-10' },
  { id: 'inv-5', productId: 'prod-5', quantityOnHand: 15, updatedAt: '2024-03-10' },
  { id: 'inv-6', productId: 'prod-6', quantityOnHand: 30, updatedAt: '2024-03-10' },
  { id: 'inv-7', productId: 'frame-1', quantityOnHand: 18, updatedAt: '2024-03-10' },
  { id: 'inv-8', productId: 'frame-2', quantityOnHand: 25, updatedAt: '2024-03-10' },
  { id: 'inv-9', productId: 'frame-3', quantityOnHand: 10, updatedAt: '2024-03-10' },
];

// Report
export const mockReportOverview: ReportOverview = {
  totalProducts: 9,
  totalOrders: 5,
  processingOrders: 2,
  completedOrders: 1,
  totalRevenue: 1665000,
  totalCustomOrders: 2,
};

export const mockRevenueReport: RevenueReportItem[] = [
  { period: 'T1/2024', revenue: 0, orderCount: 0 },
  { period: 'T2/2024', revenue: 685000, orderCount: 2 },
  { period: 'T3/2024', revenue: 980000, orderCount: 3 },
];

// Helpers
export function getProductById(id: string): Product | undefined {
  return mockProducts.find(p => p.id === id);
}

export function getProductsByKind(kind: Product['productKind']): Product[] {
  return mockProducts.filter(p => p.productKind === kind);
}

export function getCustomSelectableProducts(): Product[] {
  return mockProducts.filter(p => p.isCustomSelectable && p.status === 'active');
}

export function getSellableProducts(): Product[] {
  return mockProducts.filter(p => p.isSellableDirectly && p.status === 'active');
}
