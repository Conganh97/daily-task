import { Plus, Star, Edit2, Trash2, Check, X, MoreVertical } from 'lucide-react';
import { Task } from '../types';
import { useState, useRef, useEffect } from 'react';
import { showToast } from '../utils/toast';

interface TasksViewProps {
  tasks: Task[];
  newTaskTitle: string;
  onNewTaskTitleChange: (title: string) => void;
  onAddTask: () => void;
  onToggleTask: (taskId: string) => void;
  onToggleStar: (taskId: string) => void;
  onEditTask: (taskId: string, newTitle: string) => void;
  onDeleteTask: (taskId: string) => void;
}

export default function TasksView({
  tasks,
  newTaskTitle,
  onNewTaskTitleChange,
  onAddTask,
  onToggleTask,
  onToggleStar,
  onEditTask,
  onDeleteTask
}: TasksViewProps) {
  const [editingTaskId, setEditingTaskId] = useState<string | null>(null);
  const [editingTitle, setEditingTitle] = useState('');
  const [openMenuId, setOpenMenuId] = useState<string | null>(null);
  const menuRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (menuRef.current && !menuRef.current.contains(event.target as Node)) {
        setOpenMenuId(null);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  const handleKeyPress = (e: React.KeyboardEvent) => {
    if (e.key === 'Enter') {
      e.preventDefault();
      handleAddTask();
    }
  };

  const handleAddTask = () => {
    if (!newTaskTitle.trim()) {
      showToast.warning('Please enter a task title before adding');
      return;
    }
    onAddTask();
    showToast.success('Task created successfully!');
  };

  const handleEditKeyPress = (e: React.KeyboardEvent) => {
    if (e.key === 'Enter') {
      e.preventDefault();
      handleSaveEdit();
    }
    if (e.key === 'Escape') {
      handleCancelEdit();
    }
  };

  const handleStartEdit = (task: Task) => {
    setEditingTaskId(task.id);
    setEditingTitle(task.title);
    setOpenMenuId(null);
  };

  const handleSaveEdit = () => {
    if (editingTaskId && editingTitle.trim()) {
      onEditTask(editingTaskId, editingTitle);
      setEditingTaskId(null);
      setEditingTitle('');
      showToast.success('Task updated successfully!');
    } else {
      showToast.warning('Please enter a valid task title');
    }
  };

  const handleCancelEdit = () => {
    setEditingTaskId(null);
    setEditingTitle('');
  };

  const handleDeleteTask = (taskId: string) => {
    setOpenMenuId(null);
    showToast.confirm('Are you sure you want to delete this task?', () => {
      onDeleteTask(taskId);
      showToast.success('Task deleted successfully!');
    });
  };

  const handleToggleTask = (taskId: string) => {
    onToggleTask(taskId);
    const task = tasks.find(t => t.id === taskId);
    if (task) {
      if (task.completed) {
        showToast.success('Task marked as incomplete!');
      } else {
        showToast.success('Task completed! 🎉');
      }
    }
  };

  const handleToggleStar = (taskId: string) => {
    onToggleStar(taskId);
    const task = tasks.find(t => t.id === taskId);
    if (task) {
      if (task.starred) {
        showToast.success('Removed from favorites');
      } else {
        showToast.success('Added to favorites ⭐');
      }
    }
  };

  const toggleMenu = (taskId: string) => {
    setOpenMenuId(openMenuId === taskId ? null : taskId);
  };

  return (
    <div className="space-y-4">
      <div className="bg-white bg-opacity-10 rounded-2xl p-4">
        <div className="flex items-center space-x-3">
          <Plus className="w-5 h-5 text-white" />
          <input
            type="text"
            value={newTaskTitle}
            onChange={(e) => onNewTaskTitleChange(e.target.value)}
            onKeyDown={handleKeyPress}
            placeholder="Add work task..."
            className="flex-1 bg-transparent text-white placeholder-white placeholder-opacity-50 outline-none text-lg"
            aria-label="New task title"
          />
          <button
            onClick={handleAddTask}
            className="text-white hover:bg-white hover:bg-opacity-20 p-1 rounded transition-all"
            aria-label="Add new task"
          >
            <Plus className="w-4 h-4" />
          </button>
        </div>
      </div>

      <div className="space-y-3">
        {tasks.map(task => (
          <div key={task.id} className="flex items-center space-x-3 bg-white bg-opacity-5 rounded-xl p-4">
            <button
              onClick={() => handleToggleTask(task.id)}
              className={`w-4 h-4 rounded-full border border-white transition-all flex items-center justify-center ${
                task.completed
                  ? 'bg-white'
                  : 'hover:bg-white hover:bg-opacity-20'
              }`}
              aria-label={task.completed ? 'Mark task as incomplete' : 'Mark task as complete'}
            >
              {task.completed && (
                <Check className="w-2.5 h-2.5 text-purple-700" />
              )}
            </button>
            
            {editingTaskId === task.id ? (
              <div className="flex-1 flex items-center space-x-2">
                <input
                  type="text"
                  value={editingTitle}
                  onChange={(e) => setEditingTitle(e.target.value)}
                  onKeyDown={handleEditKeyPress}
                  className="flex-1 bg-white bg-opacity-10 text-white px-3 py-2 rounded-lg outline-none focus:ring-2 focus:ring-white focus:ring-opacity-50"
                  autoFocus
                />
                <button
                  onClick={handleSaveEdit}
                  className="text-green-400 hover:text-green-300 transition-colors"
                  aria-label="Save edit"
                >
                  <Check className="w-4 h-4" />
                </button>
                <button
                  onClick={handleCancelEdit}
                  className="text-red-400 hover:text-red-300 transition-colors"
                  aria-label="Cancel edit"
                >
                  <X className="w-4 h-4" />
                </button>
              </div>
            ) : (
              <>
                <div className={`flex-1 min-w-0 ${
                  task.completed ? 'line-through opacity-60' : ''
                }`}>
                  <div 
                    className="text-white text-lg leading-relaxed break-words cursor-pointer task-text-container group"
                    style={{
                      display: '-webkit-box',
                      WebkitLineClamp: 2,
                      WebkitBoxOrient: 'vertical',
                      overflow: 'hidden',
                      wordBreak: 'break-word'
                    }}
                  >
                    <div className="task-text-content group-hover:animate-scroll">
                      {task.title}
                    </div>
                  </div>
                </div>
                
                <div className="flex items-center space-x-2">
                  <button
                    onClick={() => handleToggleStar(task.id)}
                    className="text-white hover:scale-110 transition-transform"
                    aria-label={task.starred ? 'Remove from favorites' : 'Add to favorites'}
                  >
                    <Star 
                      className={`w-5 h-5 ${
                        task.starred ? 'fill-current' : ''
                      }`} 
                    />
                  </button>
                  
                  <div className="relative" ref={openMenuId === task.id ? menuRef : null}>
                    <button
                      onClick={() => toggleMenu(task.id)}
                      className="text-white hover:bg-white hover:bg-opacity-20 p-1 rounded transition-all"
                      aria-label="More options"
                    >
                      <MoreVertical className="w-4 h-4" />
                    </button>
                    
                    {openMenuId === task.id && (
                      <div className="absolute right-0 top-10 transform translate-x-full -translate-y-1/2 bg-white rounded-lg shadow-lg z-10 min-w-[120px] overflow-hidden ml-2">
                        <button
                          onClick={() => handleStartEdit(task)}
                          className="w-full px-4 py-2 text-left text-gray-700 hover:bg-gray-100 flex items-center space-x-2 transition-colors"
                        >
                          <Edit2 className="w-4 h-4" />
                          <span>Edit</span>
                        </button>
                        <button
                          onClick={() => handleDeleteTask(task.id)}
                          className="w-full px-4 py-2 text-left text-red-600 hover:bg-red-50 flex items-center space-x-2 transition-colors"
                        >
                          <Trash2 className="w-4 h-4" />
                          <span>Delete</span>
                        </button>
                      </div>
                    )}
                  </div>
                </div>
              </>
            )}
          </div>
        ))}
        
        {tasks.length === 0 && (
          <p className="text-white text-opacity-60 text-center py-8">
            No work tasks planned for today. What would you like to accomplish?
          </p>
        )}
      </div>
    </div>
  );
} 