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
import { AuthCard } from "./auth-card";
import { cn } from "@/lib/utils";
import { useAuth } from "@/contexts/auth-context";
import { Button } from "@/components/ui/button";

export function ProductCarousel({ products }: { products: Product[] }) {
  const [api, setApi] = React.useState<CarouselApi>();
  const [current, setCurrent] = React.useState(0);
  const { user, logout } = useAuth();

  React.useEffect(() => {
    if (!api) {
      return;
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

  // Create an array with products and auth card
  const carouselItems = [
    ...displayProducts,
    { id: 'auth', isAuthCard: true } as any
  ];

  return (
    <Carousel
      setApi={setApi}
      className="w-full h-full"
      opts={{
        loop: carouselItems.length > 1,
        align: 'center',
      }}
    >
      <CarouselContent className="h-full">
        {carouselItems.map((item, index) => {
          if (item.isAuthCard) {
            return (
              <CarouselItem
                key="auth-card"
                className={cn(
                  "basis-full md:basis-1/2 lg:basis-1/3 flex items-center justify-center"
                )}
              >
                <div className="w-full max-w-md">
                  {!user ? (
                    <AuthCard />
                  ) : (
                    <div className="bg-white rounded-2xl shadow-xl p-8 text-center">
                      <h3 className="text-xl font-bold mb-2">Welcome back!</h3>
                      <p className="text-muted-foreground mb-6">
                        You're signed in as {user.email}
                      </p>
                      <Button 
                        onClick={logout}
                        variant="outline"
                        className="w-full"
                      >
                        Sign Out
                      </Button>
                    </div>
                  )}
                </div>
              </CarouselItem>
            );
          }

          return (
            <CarouselItem
              key={`${item.id}-${index}`}
              className={cn(
                "basis-full md:basis-1/2 lg:basis-1/3 flex items-center justify-center"
              )}
            >
              <ProductCard 
                product={item} 
                isActive={isDefaultCard ? true : current === index} 
              />
            </CarouselItem>
          );
        })}
      </CarouselContent>
    </Carousel>
  );
}
