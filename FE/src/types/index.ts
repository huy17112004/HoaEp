// ============ Enums ============

export type UserRole = 'admin' | 'staff' | 'customer';

export type ProductKind = 'standard_product' | 'frame_option';

export type ProductStatus = 'active' | 'inactive';

export type AvailableOrderStatus = 'received' | 'processing' | 'shipping' | 'completed' | 'canceled';

export type CustomOrderStatus =
  | 'deposited'
  | 'waiting_flower_review'
  | 'in_progress'
  | 'waiting_demo_feedback'
  | 'waiting_remaining_payment'
  | 'completed'
  | 'canceled';

export type PaymentStatus = 'unpaid' | 'paid' | 'partial' | 'refunded';

export type PaymentMethod = 'bank_transfer' | 'cash' | 'momo' | 'zalo_pay';

export type DemoResponseStatus = 'pending' | 'approved' | 'revision_requested';

export type FlowerEvaluationStatus = 'pending' | 'approved' | 'rejected';

export type DeliveryStatus = 'pending' | 'shipped' | 'delivered' | 'failed';

export type InventoryTransactionType = 'import' | 'reserve' | 'export' | 'adjust';

// ============ Entities ============

export interface Role {
  id: string;
  code: UserRole;
  name: string;
  description?: string;
}

export interface User {
  id: string;
  fullName: string;
  phone: string;
  email: string;
  roleId: string;
  role?: Role;
  status: 'active' | 'inactive';
  createdAt: string;
  updatedAt: string;
}

export interface CustomerProfile {
  id: string;
  userId: string;
  note?: string;
}

export interface CustomerAddress {
  id: string;
  customerUserId: string;
  receiverName: string;
  receiverPhone: string;
  addressLine: string;
  ward: string;
  district: string;
  province: string;
  isDefault: boolean;
  note?: string;
  createdAt: string;
  updatedAt: string;
}

export interface ProductCategory {
  id: string;
  name: string;
  description?: string;
  status: 'active' | 'inactive';
}

export interface Product {
  id: string;
  categoryId: string;
  category?: ProductCategory;
  name: string;
  slug: string;
  description: string;
  price: number;
  productKind: ProductKind;
  isSellableDirectly: boolean;
  isCustomSelectable: boolean;
  imageUrl: string;
  images?: ProductImage[];
  size?: string;
  material?: string;
  flowerType?: string;
  status: ProductStatus;
  createdBy?: string;
  createdAt: string;
  updatedAt: string;
}

export interface ProductImage {
  id: string;
  productId: string;
  imageUrl: string;
  sortOrder: number;
}

// Available Orders
export interface AvailableOrder {
  id: string;
  orderCode: string;
  customerUserId: string;
  customer?: User;
  shippingAddressId: string;
  shippingAddress?: CustomerAddress;
  orderStatus: AvailableOrderStatus;
  paymentStatus: PaymentStatus;
  paymentMethod?: PaymentMethod;
  transactionRef?: string;
  paymentProofUrl?: string;
  paidAt?: string;
  totalAmount: number;
  items?: AvailableOrderItem[];
  orderedAt: string;
  completedAt?: string;
  canceledAt?: string;
  note?: string;
}

export interface AvailableOrderItem {
  id: string;
  availableOrderId: string;
  productId: string;
  product?: Product;
  quantity: number;
  unitPrice: number;
  subtotal: number;
}

// Custom Orders
export interface CustomOrder {
  id: string;
  orderCode: string;
  customerUserId: string;
  customer?: User;
  shippingAddressId: string;
  shippingAddress?: CustomerAddress;
  selectedFrameProductId: string;
  selectedFrame?: Product;
  orderStatus: CustomOrderStatus;
  paymentStatus: PaymentStatus;
  depositAmount: number;
  remainingAmount: number;
  totalAmount: number;
  depositPaymentMethod?: PaymentMethod;
  depositTransactionRef?: string;
  depositPaymentProofUrl?: string;
  depositPaidAt?: string;
  remainingPaymentMethod?: PaymentMethod;
  remainingTransactionRef?: string;
  remainingPaymentProofUrl?: string;
  remainingPaidAt?: string;
  flowerType: string;
  personalizationContent: string;
  requestedDeliveryDate?: string;
  flowerInputImageUrl?: string;
  flowerEvaluationStatus: FlowerEvaluationStatus;
  flowerEvaluationNote?: string;
  demoRevisionCount: number;
  extraRevisionFeeRate?: number;
  demos?: CustomDemo[];
  orderedAt: string;
  completedAt?: string;
  canceledAt?: string;
  note?: string;
}

export interface CustomDemo {
  id: string;
  customOrderId: string;
  versionNo: number;
  demoImageUrl: string;
  demoDescription?: string;
  customerResponseStatus: DemoResponseStatus;
  customerFeedback?: string;
  uploadedBy: string;
  uploadedAt: string;
  respondedAt?: string;
}

// Inventory
export interface PurchaseReceipt {
  id: string;
  receiptCode: string;
  receiptDate: string;
  createdBy: string;
  note?: string;
  items?: PurchaseReceiptItem[];
  createdAt: string;
}

export interface PurchaseReceiptItem {
  id: string;
  purchaseReceiptId: string;
  productId: string;
  product?: Product;
  quantity: number;
  unitCost: number;
  subtotal: number;
}

export interface InventoryItem {
  id: string;
  productId: string;
  product?: Product;
  quantityOnHand: number;
  updatedAt: string;
}

export interface InventoryTransaction {
  id: string;
  productId: string;
  product?: Product;
  transactionType: InventoryTransactionType;
  quantityChange: number;
  referenceType?: string;
  referenceId?: string;
  note?: string;
  createdBy: string;
  createdAt: string;
}

// Reports
export interface ReportOverview {
  totalProducts: number;
  totalOrders: number;
  processingOrders: number;
  completedOrders: number;
  totalRevenue: number;
  totalCustomOrders: number;
}

export interface RevenueReportItem {
  period: string;
  revenue: number;
  orderCount: number;
}

// API Response
export interface ApiResponse<T> {
  success: boolean;
  code?: string;
  message: string;
  data: T;
  meta?: {
    page: number;
    limit: number;
    total: number;
    totalPages: number;
  };
}
