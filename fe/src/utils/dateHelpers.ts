export const formatDate = (date: Date): string => {
  return date.toISOString().split('T')[0];
};

export const getDateLabel = (date: Date): string => {
  const month = date.toLocaleDateString('en-US', { month: 'short' });
  const day = date.getDate();
  return `${month} ${day}`;
};

export const getEnergyCategory = (rating: number): string => {
  if (rating <= 2) return 'Very low';
  if (rating <= 4) return 'Low';
  if (rating <= 6) return 'Moderate';
  if (rating <= 8) return 'High';
  return 'Very high';
}; 