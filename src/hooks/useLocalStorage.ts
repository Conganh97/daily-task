import { useState, useEffect } from 'react';

export function useLocalStorage<T>(key: string, initialValue: T) {
  const [storedValue, setStoredValue] = useState<T>(() => {
    try {
      const item = window.localStorage.getItem(key);
      return item ? JSON.parse(item) : initialValue;
    } catch (error) {
      console.error(`Error reading localStorage key "${key}":`, error);
      return initialValue;
    }
  });

  const setValue = (value: T | ((val: T) => T)) => {
    try {
      const valueToStore = value instanceof Function ? value(storedValue) : value;
      setStoredValue(valueToStore);
      
      const serializedValue = JSON.stringify(valueToStore);
      
      // Check if we're approaching localStorage limits
      if (serializedValue.length > 5 * 1024 * 1024) { // 5MB warning
        console.warn(`Large localStorage entry for key "${key}": ${serializedValue.length} bytes`);
      }
      
      window.localStorage.setItem(key, serializedValue);
    } catch (error) {
      console.error(`Error setting localStorage key "${key}":`, error);
      
      // If quota exceeded, try to clear old data
      if (error instanceof DOMException && error.name === 'QuotaExceededError') {
        console.warn('localStorage quota exceeded, attempting to clear old data');
        try {
          // Get all wellness app keys and remove oldest entries
          const keys = Object.keys(localStorage).filter(k => k.startsWith('wellness'));
          if (keys.length > 10) {
            keys.slice(0, keys.length - 10).forEach(k => localStorage.removeItem(k));
            // Retry the operation
            window.localStorage.setItem(key, JSON.stringify(value instanceof Function ? value(storedValue) : value));
          }
        } catch (retryError) {
          console.error('Failed to recover from localStorage quota error:', retryError);
        }
      }
    }
  };

  // Sync with localStorage changes from other tabs
  useEffect(() => {
    const handleStorageChange = (e: StorageEvent) => {
      if (e.key === key && e.newValue !== null) {
        try {
          setStoredValue(JSON.parse(e.newValue));
        } catch (error) {
          console.error(`Error parsing localStorage change for key "${key}":`, error);
        }
      }
    };

    window.addEventListener('storage', handleStorageChange);
    return () => window.removeEventListener('storage', handleStorageChange);
  }, [key]);

  return [storedValue, setValue] as const;
} 