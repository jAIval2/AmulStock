import Image from 'next/image';
import type { Product } from '@/lib/types';
import { Card, CardContent } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Dumbbell, Feather, MilkOff, Snowflake, Package, Coffee, IceCream, Leaf, XCircle, Flame, Star } from 'lucide-react';
import { cn } from '@/lib/utils';

const featureIcons: { [key: string]: React.ReactNode } = {
  'High Protein': <Dumbbell className="h-6 w-6 text-muted-foreground" />,
  'Low Fat': <Feather className="h-6 w-6 text-muted-foreground" />,
  'No Added Sugar': <XCircle className="h-6 w-6 text-muted-foreground" />,
  'Low Calorie': <Flame className="h-6 w-6 text-muted-foreground" />,
  'Lactose Free': <MilkOff className="h-6 w-6 text-muted-foreground" />,
  'Ready to Drink': <Coffee className="h-6 w-6 text-muted-foreground" />,
  'Convenient Packaging': <Package className="h-6 w-6 text-muted-foreground" />,
  'Refreshing / Chilled': <Snowflake className="h-6 w-6 text-muted-foreground" />,
  'Delicious Flavor': <IceCream className="h-6 w-6 text-muted-foreground" />,
  'Plant Based': <Leaf className="h-6 w-6 text-muted-foreground" />,
  
  // Keep the old icons as fallback
  'Handcrafted': <Dumbbell className="h-6 w-6 text-muted-foreground" />,
  'Soy Free': <XCircle className="h-6 w-6 text-muted-foreground" />,
  'Pesticide Free': <Leaf className="h-6 w-6 text-muted-foreground" />,
  'Plants Trees': <Leaf className="h-6 w-6 text-muted-foreground" />,
  'No GMO': <XCircle className="h-6 w-6 text-muted-foreground" />,
  'Gluten Free': <XCircle className="h-6 w-6 text-muted-foreground" />,
  'Animal Free': <XCircle className="h-6 w-6 text-muted-foreground" />,
};

export function ProductCard({ product, isActive }: { product: Product, isActive: boolean }) {
  const hints: { [key: string]: string } = {
    '1': 'amber pendant',
    '2': 'amber ring',
    '3': 'amber earrings',
    '4': 'amber necklace',
    '5': 'amber bracelet',
  };

  return (
    <div className={cn("relative transition-transform duration-300", isActive ? "scale-105" : "scale-90 opacity-60")}>
      <Card className={cn(
        "w-full max-w-sm mx-auto rounded-3xl shadow-2xl p-8 relative overflow-hidden",
        isActive ? "animated-border-card" : "bg-card/80 backdrop-blur-md"
      )}>
        <CardContent className="relative grid grid-cols-1 md:grid-cols-2 gap-8 items-center z-10 p-0">
          
          <div className="md:order-2 flex justify-center items-center">
            <Image
              src={product.image}
              alt={product.name}
              width={300}
              height={300}
              className="object-contain w-full max-w-xs"
              data-ai-hint={hints[product.id]}
            />
          </div>

          <div className="md:order-1 flex flex-col items-start text-left">
            <h2 className="text-2xl md:text-2xl font-bold text-foreground leading-tight mb-4">{product.name}</h2>
            <p className="text-muted-foreground mb-6">{product.details}</p>
            <p className="text-xl font-medium text-foreground mb-6">₹{product.price.toFixed(2)}</p>
            <Button variant="outline" className="border-2 border-primary text-primary font-bold hover:bg-primary hover:text-primary-foreground transition-colors duration-300">
              ADD TO CART
            </Button>.
          </div>

          <div className="md:col-span-2 md:order-3 pt-8">
              <div className="grid grid-cols-3 sm:grid-cols-6 gap-y-8 gap-x-4 text-center">
              {(product.features ?? []).map((feature) => (
                <div key={feature} className="flex flex-col items-center">
                  {featureIcons[feature] || <Star className="w-8 h-8 text-muted-foreground" />}
                  <span className="mt-2 text-xs text-muted-foreground">{feature}</span>
                </div>
              ))}
              </div>
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
