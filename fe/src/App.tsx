import { useState } from 'react';
import { Toaster } from 'react-hot-toast';
import { formatDate } from './utils/dateHelpers';
import { useReflections, useTasks } from './hooks';
import {
  DateNavigation,
  DatePicker,
  ViewToggle,
  ReflectionView,
  TasksView,
  EnergyView,
  EnergyDisplay
} from './components';

export default function App() {
  const [currentDate, setCurrentDate] = useState(new Date());
  const [activeView, setActiveView] = useState('energy');
  const [showDatePicker, setShowDatePicker] = useState(false);
  const [newTaskTitle, setNewTaskTitle] = useState('');

  const { getReflection, updateReflection } = useReflections();
  const { getTasksByDate, addTask, toggleTask, toggleStar, editTask, deleteTask } = useTasks();

  const currentDateStr = formatDate(currentDate);
  const currentReflection = getReflection(currentDateStr);
  const currentTasks = getTasksByDate(currentDateStr);

  const handleNavigateDate = (direction: 'prev' | 'next') => {
    const newDate = new Date(currentDate);
    newDate.setDate(currentDate.getDate() + (direction === 'next' ? 1 : -1));
    setCurrentDate(newDate);
  };

  const handleGoToDate = (offset: number) => {
    const newDate = new Date(currentDate);
    newDate.setDate(currentDate.getDate() + offset);
    setCurrentDate(newDate);
  };

  const handleGoToSpecificDate = (dateString: string) => {
    if (dateString) {
      setCurrentDate(new Date(dateString));
      setShowDatePicker(false);
    }
  };

  const handleToggleDatePicker = () => {
    setShowDatePicker(!showDatePicker);
  };

  const handleUpdateReflection = (updates: Parameters<typeof updateReflection>[1]) => {
    updateReflection(currentDateStr, updates);
  };

  const handleAddTask = () => {
    if (newTaskTitle.trim()) {
      addTask(currentDateStr, newTaskTitle);
      setNewTaskTitle('');
    }
  };

  const renderActiveView = () => {
    switch (activeView) {
      case 'energy':
        return (
          <div className="animate-fade-in">
            <EnergyView
              reflection={currentReflection}
              onUpdateReflection={handleUpdateReflection}
            />
          </div>
        );
    
      case 'tasks':
        return (
          <div className="animate-fade-in">
            <TasksView
              tasks={currentTasks}
              newTaskTitle={newTaskTitle}
              onNewTaskTitleChange={setNewTaskTitle}
              onAddTask={handleAddTask}
              onToggleTask={toggleTask}
              onToggleStar={toggleStar}
              onEditTask={editTask}
              onDeleteTask={deleteTask}
            />
          </div>
        );

        case 'reflection':
          return (
            <div className="animate-fade-in">
              <ReflectionView
                reflection={currentReflection}
                onUpdateReflection={handleUpdateReflection}
              />
            </div>
          );
      default:
        return null;
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-purple-900 via-purple-950 to-indigo-950">
      <div className="container mx-auto px-4 py-4 sm:py-6 max-w-md">
        <div className="animate-slide-up">
          <DatePicker
            currentDate={currentDate}
            onGoToSpecificDate={handleGoToSpecificDate}
            showDatePicker={showDatePicker}
            onToggleDatePicker={handleToggleDatePicker}
          />
          <DateNavigation
            currentDate={currentDate}
            onNavigateDate={handleNavigateDate}
            onGoToDate={handleGoToDate}
          />
        </div>

        <div className="bg-white bg-opacity-10 backdrop-blur-lg rounded-3xl p-4 sm:p-6 shadow-2xl animate-scale-in">
          <ViewToggle
            activeView={activeView}
            onViewChange={setActiveView}
          />

          <div className="min-h-[300px] sm:min-h-[400px]">
            {renderActiveView()}
          </div>
        </div>

        <div className="animate-fade-in">
          <EnergyDisplay reflection={currentReflection} />
        </div>
      </div>
      
      <Toaster
        position="top-center"
        reverseOrder={false}
        gutter={8}
        containerClassName=""
        containerStyle={{}}
        toastOptions={{
          className: '',
          duration: 4000,
          style: {
            background: '#fff',
            color: '#333',
            borderRadius: '12px',
            boxShadow: '0 10px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04)',
            fontSize: '14px',
            fontWeight: '500',
            padding: '12px 16px',
          },
          success: {
            iconTheme: {
              primary: '#10b981',
              secondary: '#fff',
            },
          },
          error: {
            iconTheme: {
              primary: '#ef4444',
              secondary: '#fff',
            },
          },
        }}
      />
    </div>
  );
} 