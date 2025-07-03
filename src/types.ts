export interface Reflection {
  id: string;
  date: string;
  energyRating: number;
  energyCategory: string;
  notes: string;
  createdAt: Date;
  updatedAt: Date;
}

export interface Task {
  id: string;
  title: string;
  completed: boolean;
  starred: boolean;
  date: string;
  createdAt: Date;
  completedAt?: Date;
} 