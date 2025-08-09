export interface Product {
  id: string;
  name: string;
  price: number;
  details: string;
  image: string;
  features: string[];
  quantity?: number; // Optional quantity for cart functionality
}
