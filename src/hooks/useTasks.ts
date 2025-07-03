import { Task } from '../types';
import { useLocalStorage } from './useLocalStorage';

export function useTasks() {
  const [tasks, setTasks] = useLocalStorage<Task[]>('wellnessTasks', []);

  const getTasksByDate = (date: string) => {
    return tasks.filter(t => t.date === date);
  };

  const addTask = (date: string, title: string) => {
    if (!title.trim()) return;

    const newTask: Task = {
      id: Date.now().toString(),
      title: title.trim(),
      completed: false,
      starred: false,
      date,
      createdAt: new Date()
    };

    setTasks([...tasks, newTask]);
  };

  const editTask = (taskId: string, newTitle: string) => {
    if (!newTitle.trim()) return;

    setTasks(tasks.map(task => 
      task.id === taskId ? { ...task, title: newTitle.trim() } : task
    ));
  };

  const deleteTask = (taskId: string) => {
    setTasks(tasks.filter(task => task.id !== taskId));
  };

  const toggleTask = (taskId: string) => {
    setTasks(tasks.map(task => 
      task.id === taskId ? { 
        ...task, 
        completed: !task.completed,
        completedAt: !task.completed ? new Date() : undefined
      } : task
    ));
  };

  const toggleStar = (taskId: string) => {
    setTasks(tasks.map(task => 
      task.id === taskId ? { ...task, starred: !task.starred } : task
    ));
  };

  return {
    tasks,
    getTasksByDate,
    addTask,
    editTask,
    deleteTask,
    toggleTask,
    toggleStar
  };
} 