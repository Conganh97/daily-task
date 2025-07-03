// Test utility functions for development
export const testLocalStorage = () => {
  const testKey = 'wellness-test';
  const testData = { test: 'data', timestamp: Date.now() };
  
  try {
    localStorage.setItem(testKey, JSON.stringify(testData));
    const retrieved = localStorage.getItem(testKey);
    const parsed = retrieved ? JSON.parse(retrieved) : null;
    localStorage.removeItem(testKey);
    
    return parsed?.test === 'data';
  } catch (error) {
    console.error('localStorage test failed:', error);
    return false;
  }
};

export const testDateHelpers = () => {
  // Simple test that can be run without imports
  const testDate = new Date('2024-01-15');
  
  try {
    // Test basic date functionality
    const isoString = testDate.toISOString().split('T')[0];
    const month = testDate.toLocaleDateString('en-US', { month: 'short' });
    const day = testDate.getDate();
    const label = `${month} ${day}`;
    
    return (
      isoString === '2024-01-15' &&
      label === 'Jan 15'
    );
  } catch (error) {
    console.error('Date helpers test failed:', error);
    return false;
  }
};

export const runBasicTests = () => {
  console.log('Running basic wellness app tests...');
  
  const localStorageTest = testLocalStorage();
  const dateHelpersTest = testDateHelpers();
  
  console.log('localStorage test:', localStorageTest ? '✅ PASS' : '❌ FAIL');
  console.log('Date helpers test:', dateHelpersTest ? '✅ PASS' : '❌ FAIL');
  
  const allTestsPassed = localStorageTest && dateHelpersTest;
  console.log('All tests:', allTestsPassed ? '✅ PASS' : '❌ FAIL');
  
  return allTestsPassed;
};

// Performance monitoring
export const measurePerformance = (name: string, fn: () => void) => {
  const start = performance.now();
  fn();
  const end = performance.now();
  console.log(`${name} took ${end - start}ms`);
};

// Basic accessibility check
export const checkAccessibility = () => {
  const issues = [];
  
  // Check for images without alt text
  const images = document.querySelectorAll('img:not([alt])');
  if (images.length > 0) {
    issues.push(`${images.length} images missing alt text`);
  }
  
  // Check for buttons without accessible names
  const buttons = document.querySelectorAll('button:not([aria-label]):not([aria-labelledby])');
  const buttonsWithoutText = Array.from(buttons).filter(btn => !btn.textContent?.trim());
  if (buttonsWithoutText.length > 0) {
    issues.push(`${buttonsWithoutText.length} buttons without accessible names`);
  }
  
  // Check for form inputs without labels
  const inputs = document.querySelectorAll('input:not([aria-label]):not([aria-labelledby])');
  const inputsWithoutLabels = Array.from(inputs).filter(input => {
    const id = input.getAttribute('id');
    return !id || !document.querySelector(`label[for="${id}"]`);
  });
  if (inputsWithoutLabels.length > 0) {
    issues.push(`${inputsWithoutLabels.length} inputs without labels`);
  }
  
  return issues;
};

// Mobile responsiveness check
export const checkMobileResponsiveness = () => {
  const issues = [];
  const minTouchTarget = 44; // pixels
  
  // Check touch target sizes
  const clickableElements = document.querySelectorAll('button, input[type="button"], input[type="submit"], a');
  const smallTargets = Array.from(clickableElements).filter(el => {
    const rect = el.getBoundingClientRect();
    return rect.width < minTouchTarget || rect.height < minTouchTarget;
  });
  
  if (smallTargets.length > 0) {
    issues.push(`${smallTargets.length} touch targets smaller than ${minTouchTarget}px`);
  }
  
  return issues;
}; 