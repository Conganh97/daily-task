interface ViewToggleProps {
  activeView: string;
  onViewChange: (view: string) => void;
}

const views = [
  { key: 'energy', label: 'Morning' },
  { key: 'tasks', label: 'Work' },
  { key: 'reflection', label: 'Evening' }
];

export default function ViewToggle({ activeView, onViewChange }: ViewToggleProps) {
  return (
    <div className="flex justify-center mb-6">
      <div className="bg-white bg-opacity-10 rounded-full p-1 flex" role="tablist">
        {views.map(({ key, label }) => (
          <button
            key={key}
            onClick={() => onViewChange(key)}
            className={`px-4 py-2 rounded-full text-sm font-medium transition-all ${
              activeView === key
                ? 'bg-white text-purple-700 shadow-lg'
                : 'text-white hover:bg-white hover:bg-opacity-10'
            }`}
            role="tab"
            aria-selected={activeView === key}
            aria-label={`Switch to ${label} view`}
          >
            {label}
          </button>
        ))}
      </div>
    </div>
  );
} 