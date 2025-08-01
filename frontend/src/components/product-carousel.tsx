"use client";

import * as React from "react";
import type { Product } from "@/lib/types";
import { type EmblaCarouselType } from 'embla-carousel-react';
import {
  Carousel,
  CarouselApi,
  CarouselContent,
  CarouselItem,
} from "@/components/ui/carousel";
import { ProductCard } from "./product-card";
import { cn } from "@/lib/utils";

export function ProductCarousel({ products }: { products: Product[] }) {
  const [api, setApi] = React.useState<CarouselApi>()
  const [current, setCurrent] = React.useState(0)

  React.useEffect(() => {
    if (!api) {
      return
    }

    setCurrent(api.selectedScrollSnap())

    const onSelect = (api: EmblaCarouselType) => {
      setCurrent(api.selectedScrollSnap())
    }

    api.on("select", onSelect)

    return () => {
      api.off("select", onSelect)
    }
  }, [api])

  // Default product to show when there are no products
  const defaultProduct: Product = {
    id: 'default',
    name: 'No Products Available',
    price: 0,
    details: 'Check back later for our amazing products!',
    image: 'https://placehold.co/400x400?text=Coming+Soon',
    features: ['New Arrivals', 'Coming Soon']
  };

  // Use default product if no products are available
  const displayProducts = products.length > 0 ? products : [defaultProduct];
  const isDefaultCard = products.length === 0;

  return (
    <Carousel
      setApi={setApi}
      className="w-full h-full"
      opts={{
        loop: products.length > 1, // Only loop if there are multiple products
        align: 'center',
      }}
    >
      <CarouselContent className="h-full">
        {displayProducts.map((product, index) => {
          return (
            <CarouselItem
              key={`${product.id}-${index}`}
              className={cn(
                "basis-full md:basis-1/3 flex items-center justify-center"
              )}
            >
              <ProductCard 
                product={product} 
                isActive={isDefaultCard ? true : current === index} 
              />
            </CarouselItem>
          );
        })}
      </CarouselContent>
    </Carousel>
  );
}
