import { Reflection } from '../types';
import { useLocalStorage } from './useLocalStorage';

export function useReflections() {
  const [reflections, setReflections] = useLocalStorage<Reflection[]>('wellnessReflections', []);

  const getReflection = (date: string) => {
    return reflections.find(r => r.date === date);
  };

  const updateReflection = (date: string, updates: Partial<Reflection>) => {
    const existingIndex = reflections.findIndex(r => r.date === date);
    const existingReflection = reflections[existingIndex];
    
    const baseReflection: Reflection = {
      id: existingReflection?.id || Date.now().toString(),
      date,
      energyRating: existingReflection?.energyRating || 5,
      energyCategory: existingReflection?.energyCategory || 'Moderate',
      notes: existingReflection?.notes || '',
      createdAt: existingReflection?.createdAt || new Date(),
      updatedAt: new Date(),
      ...updates
    };

    if (existingIndex >= 0) {
      const updatedReflections = [...reflections];
      updatedReflections[existingIndex] = baseReflection;
      setReflections(updatedReflections);
    } else {
      setReflections([...reflections, baseReflection]);
    }
  };

  return {
    reflections,
    getReflection,
    updateReflection
  };
} 