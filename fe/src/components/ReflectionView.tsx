import { Reflection } from '../types';
import { getEnergyCategory } from '../utils/dateHelpers';

interface ReflectionViewProps {
  reflection?: Reflection;
  onUpdateReflection: (updates: Partial<Reflection>) => void;
}

export default function ReflectionView({ reflection, onUpdateReflection }: ReflectionViewProps) {
  return (
    <div className="space-y-6">
      <h1 className="text-3xl font-bold text-white text-center mb-2">
        Evening Reflection
      </h1>
      <p className="text-white text-opacity-80 text-center text-sm mb-6">
        Take a moment to reflect on your day
      </p>
      
      <div>
        <p className="text-white text-lg mb-4 text-center">
          How would you rate your energy throughout the day?
        </p>
        
        <div className="flex justify-between items-center mb-6">
          {[1, 2, 3, 4, 5, 6, 7, 8, 9, 10].map(num => (
            <button
              key={num}
              onClick={() => onUpdateReflection({ 
                energyRating: num,
                energyCategory: getEnergyCategory(num)
              })}
              className={`w-8 h-8 rounded-full border-2 border-white font-medium transition-all ${
                reflection?.energyRating === num
                  ? 'bg-white text-purple-900 scale-110 shadow-lg'
                  : 'text-white hover:bg-white hover:bg-opacity-20'
              }`}
              aria-label={`Rate energy as ${num} out of 10`}
            >
              {num}
            </button>
          ))}
        </div>
      </div>

      <div className="bg-white bg-opacity-10 rounded-2xl p-4">
        <label className="text-white text-opacity-80 mb-2 block">Reflection</label>
        <textarea
          value={reflection?.notes || ''}
          onChange={(e) => onUpdateReflection({ notes: e.target.value })}
          placeholder="How did your day go? What did you learn or accomplish?"
          className="w-full h-32 bg-transparent text-white placeholder-white placeholder-opacity-50 resize-none outline-none custom-scrollbar"
          aria-label="Daily reflection notes"
        />
      </div>
    </div>
  );
} 