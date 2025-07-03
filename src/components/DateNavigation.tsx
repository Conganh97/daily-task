import { ChevronLeft, ChevronRight } from 'lucide-react';
import { getDateLabel } from '../utils/dateHelpers';

interface DateNavigationProps {
  currentDate: Date;
  onNavigateDate: (direction: 'prev' | 'next') => void;
  onGoToDate: (offset: number) => void;
}

export default function DateNavigation({
  currentDate,
  onNavigateDate,
  onGoToDate,
}: DateNavigationProps) {
  const getDateRange = () => {
    const dates = [];
    for (let i = -2; i <= 2; i++) {
      const date = new Date(currentDate);
      date.setDate(currentDate.getDate() + i);
      dates.push({ date, offset: i });
    }
    return dates;
  };

  return (
    <div className="flex justify-between items-center mb-6">
      <button
        onClick={() => onNavigateDate('prev')}
        className="p-2 rounded-full bg-white bg-opacity-10 hover:bg-opacity-20 transition-all"
        aria-label="Previous day"
      >
        <ChevronLeft className="w-5 h-5 text-white" />
      </button>

      <div className="flex items-center space-x-1">
        {getDateRange().map(({ date, offset }) => (
          <button
            key={offset}
            onClick={() => onGoToDate(offset)}
            className={`w-16 py-2 rounded-lg text-white font-medium transition-all text-base whitespace-nowrap ${
              offset === 0 
                ? 'bg-white bg-opacity-20 scale-105' 
                : 'bg-white bg-opacity-10 hover:bg-opacity-15'
            }`}
            aria-label={`Go to ${getDateLabel(date)}`}
          >
            {getDateLabel(date)}
          </button>
        ))}
      </div>

      <button
        onClick={() => onNavigateDate('next')}
        className="p-2 rounded-full bg-white bg-opacity-10 hover:bg-opacity-20 transition-all"
        aria-label="Next day"
      >
        <ChevronRight className="w-5 h-5 text-white" />
      </button>
    </div>
  );
} 