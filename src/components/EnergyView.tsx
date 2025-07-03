import { Reflection } from '../types';

interface EnergyViewProps {
  reflection?: Reflection;
  onUpdateReflection: (updates: Partial<Reflection>) => void;
}

const energyLevels = [
  { label: 'Very low', value: 1 },
  { label: 'Low', value: 3 },
  { label: 'Moderate', value: 5 },
  { label: 'High', value: 7 },
  { label: 'Very high', value: 9 }
];

export default function EnergyView({ reflection, onUpdateReflection }: EnergyViewProps) {
  return (
    <div className="space-y-6">
      <h2 className="text-2xl font-bold text-white text-center">
        How would you rate your energy today?
      </h2>
      <p className="text-white text-opacity-80 text-center text-sm">
        Start your day by checking in with your energy level
      </p>
      
      <div className="space-y-4">
        {energyLevels.map(({ label, value }) => (
          <button
            key={label}
            onClick={() => onUpdateReflection({ 
              energyRating: value,
              energyCategory: label
            })}
            className="w-full flex items-center space-x-4 p-4 rounded-xl bg-white bg-opacity-5 hover:bg-opacity-10 transition-all"
            aria-label={`Set energy level to ${label}`}
          >
            <div className={`w-6 h-6 rounded-full border-2 border-white transition-all ${
              reflection?.energyCategory === label
                ? 'bg-white'
                : ''
            }`}>
              {reflection?.energyCategory === label && (
                <div className="w-full h-full flex items-center justify-center">
                  <div className="w-2 h-2 bg-purple-700 rounded-full"></div>
                </div>
              )}
            </div>
            <span className="text-white text-lg">{label}</span>
          </button>
        ))}
      </div>
    </div>
  );
} 