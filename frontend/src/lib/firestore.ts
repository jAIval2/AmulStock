import { getFirestore, doc, setDoc, deleteDoc, collection, getDocs } from 'firebase/firestore';
import { auth } from './firebase';
import { Product } from '@/lib/types';

const db = getFirestore();

export const cartService = {
  async addToCart(product: Product) {
    const user = auth.currentUser;
    if (!user) throw new Error('User not authenticated');
    
    await setDoc(doc(db, `users/${user.uid}/cart/${product.id}`), {
      ...product,
      addedAt: new Date().toISOString(),
      quantity: 1
    }, { merge: true });
  },

  async removeFromCart(productId: string) {
    const user = auth.currentUser;
    if (!user) throw new Error('User not authenticated');
    
    await deleteDoc(doc(db, `users/${user.uid}/cart/${productId}`));
  },

  async getCart() {
    const user = auth.currentUser;
    if (!user) return [];
    
    const cartSnapshot = await getDocs(collection(db, `users/${user.uid}/cart`));
    return cartSnapshot.docs.map(doc => ({
      id: doc.id,
      ...doc.data()
    }));
  }
};
