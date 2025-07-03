import { Calendar } from 'lucide-react';
import { formatDate } from '../utils/dateHelpers';

interface DatePickerProps {
  currentDate: Date;
  onGoToSpecificDate: (dateString: string) => void;
  showDatePicker: boolean;
  onToggleDatePicker: () => void;
}

export default function DatePicker({
  currentDate,
  onGoToSpecificDate,
  showDatePicker,
  onToggleDatePicker,
}: DatePickerProps) {
  const handleDateChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    if (value) {
      onGoToSpecificDate(value);
    }
  };

  const handleGoToToday = () => {
    const today = formatDate(new Date());
    onGoToSpecificDate(today);
    onToggleDatePicker();
  };

  const isToday = formatDate(currentDate) === formatDate(new Date());

  return (
    <div className="relative mb-4">
      <button
        onClick={onToggleDatePicker}
        className="w-full p-3 rounded-xl bg-white bg-opacity-10 hover:bg-opacity-20 transition-all flex items-center justify-center space-x-2"
        aria-label="Open date picker"
      >
        <Calendar className="w-5 h-5 text-white" />
        <span className="text-white font-medium">Select Date</span>
      </button>

      {showDatePicker && (
        <div className="absolute left-0 right-0 top-16 bg-white rounded-xl shadow-2xl p-4 z-50">
          <input
            type="date"
            value={formatDate(currentDate)}
            onChange={handleDateChange}
            className="w-full p-3 border border-gray-300 rounded-lg text-gray-700 focus:outline-none focus:ring-2 focus:ring-purple-500"
            aria-label="Select date"
          />
          
          <div className="flex space-x-2 mt-3">
            <button
              onClick={handleGoToToday}
              disabled={isToday}
              className={`flex-1 py-2 rounded-lg transition-colors ${
                isToday 
                  ? 'bg-gray-200 text-gray-400 cursor-not-allowed' 
                  : 'bg-green-500 text-white hover:bg-green-600'
              }`}
              aria-label="Go to today"
            >
              Today
            </button>
            <button
              onClick={onToggleDatePicker}
              className="flex-1 py-2 bg-purple-600 text-white rounded-lg hover:bg-purple-700 transition-colors"
            >
              Close
            </button>
          </div>
        </div>
      )}
    </div>
  );
} 