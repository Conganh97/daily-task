import { Reflection } from '../types';

interface EnergyDisplayProps {
  reflection?: Reflection;
}

export default function EnergyDisplay({ reflection }: EnergyDisplayProps) {
  if (!reflection) return null;

  return (
    <div className="mt-4 text-center">
      <p className="text-white text-opacity-80">
        Today's Energy: <span className="font-semibold">{reflection.energyCategory}</span> ({reflection.energyRating}/10)
      </p>
    </div>
  );
} 