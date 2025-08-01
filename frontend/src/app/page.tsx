import { ProductCarousel } from '@/components/product-carousel';
import type { Product } from '@/lib/types';

// Fetch products from backend REST endpoint. If env var is not set, default to localhost:8080
async function getProducts(): Promise<Product[]> {
  const baseUrl = process.env.NEXT_PUBLIC_BACKEND_URL ?? 'http://localhost:8080';
  const controller = new AbortController();
  const timeoutId = setTimeout(() => controller.abort(), 5000); // 5 second timeout
  const endpoint = `${baseUrl}/hehe`;

  console.log(`Fetching products from: ${endpoint}`); // Log the URL being called

  try {
    const res = await fetch(endpoint, {
      signal: controller.signal,
      cache: 'no-store',
      headers: {
        'Authorization': 'Basic ' + Buffer.from('admin:admin').toString('base64'),
        'Content-Type': 'application/json',
      },
    });
    
    clearTimeout(timeoutId);

    if (!res.ok) {
      console.error('Failed to fetch products', {
        status: res.status,
        statusText: res.statusText,
        headers: Object.fromEntries(res.headers.entries())
      });
      return [];
    }
    const raw = await res.json();
    
    // Log the raw response for debugging
    console.log('Raw API response:', JSON.stringify(raw, null, 2));
    
    const products = raw.map((p: any) => {
      // Safely extract the first image URL
      const imageUrl = p.images?.[0]?.image 
        ? `https://shop.amul.com/s/62fa94df8c13af2e242eba16/${p.images[0].image}`
        : 'https://placehold.co/400x400?text=No+Image';
        
      // Log each product's data for debugging
      console.log(`Processing product: ${p.name}`, {
        id: p.id,
        hasBenefits: !!p.benefits,
        benefits: p.benefits,
        hasIngredients: !!p.ingredients,
        ingredients: p.ingredients,
        available: p.available
      });
      
      return {
        id: p.id || 'unknown',
        name: p.name || 'Unnamed Product',
        price: p.price || 0,
        details: p.ingredients ? (Array.isArray(p.ingredients) ? p.ingredients.join(', ') : p.ingredients) : (p.alias || ''),
        image: imageUrl,
        features: [
          ...(p.available ? ['In Stock'] : ['Out of Stock']),
          ...(Array.isArray(p.benefits) ? p.benefits : [])
        ]
      };
    });
    
    console.log('Processed products:', products);
    return products;
  } catch (err) {
    clearTimeout(timeoutId);
    console.error('Network error when fetching products:', {
      error: err,
      endpoint,
      message: err instanceof Error ? err.message : 'Unknown error'
    });
    return [];
  }
}

export default async function Home() {
  const products = await getProducts();
  return (
    <main className="relative flex min-h-screen w-full flex-col items-center justify-center bg-background">
      <div className="absolute -top-16 right-1/4 h-64 w-64 bg-primary/20 rounded-full blur-3xl animate-blob" />
      <div className="absolute bottom-1/4 left-1/4 h-64 w-64 bg-primary/20 rounded-full blur-3xl animate-blob animation-delay-2000" />
      <div className="absolute bottom-1/2 right-1/3 h-64 w-64 bg-primary/20 rounded-full blur-3xl animate-blob animation-delay-4000" />
      <div className="absolute inset-0 h-full w-full bg-gradient-to-r from-background to-background/80" />
      <div className="z-10 w-full h-full flex items-center justify-center">
        <ProductCarousel products={products} />
      </div>
    </main>
  );
}
