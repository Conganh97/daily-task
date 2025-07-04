# Daily Task

A modern productivity app focused on daily task management and reflection, designed to help you stay organized and track your daily accomplishments with a beautiful purple gradient design.

## Features

- **Task Management**: Add, edit, delete, complete, and star tasks with daily organization
- **Daily Reflection System**: Rate your energy (1-10 scale) and write daily reflections
- **Energy Rating Interface**: Quick 5-point energy assessment
- **Date Navigation**: Navigate between days to view historical data
- **Toast Notifications**: Beautiful notifications for all actions (create, edit, delete, complete)
- **Local Storage**: All data persists locally in your browser
- **Responsive Design**: Works on mobile and desktop

## Tech Stack

- **Frontend**: React 18 with TypeScript
- **Styling**: Tailwind CSS with custom purple gradient theme
- **Icons**: Lucide React
- **Notifications**: React Hot Toast
- **Build Tool**: Vite
- **Date Handling**: date-fns library

## Getting Started

1. Install dependencies:
   ```bash
   npm install
   ```

2. Start the development server:
   ```bash
   npm run dev
   ```

3. Open your browser and navigate to `http://localhost:5173`

## Available Scripts

- `npm run dev` - Start development server
- `npm run build` - Build for production
- `npm run preview` - Preview production build
- `npm run lint` - Run ESLint

## Project Structure

```
daily-focus/
├── src/
│   ├── components/          # React components
│   ├── utils/              # Utility functions
│   ├── hooks/              # Custom React hooks
│   ├── types.ts            # TypeScript interfaces
│   ├── App.tsx             # Main App component
│   ├── main.tsx            # Entry point
│   └── index.css           # Global styles
├── public/                 # Static assets
├── package.json           # Dependencies
└── README.md              # This file
```

## Usage

1. **Task Management**: Add tasks, edit them inline, delete with confirmation, mark complete, or star important ones
2. **Daily Reflection**: Use the 1-10 scale to rate your energy and write reflections
3. **Energy Assessment**: Quick 5-point energy rating system
4. **Date Navigation**: Click date buttons or use the calendar picker to navigate

## Data Storage

All data is stored locally in your browser's localStorage. Your tasks and reflections will persist between sessions but are not synced across devices.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

## License

This project is for educational purposes. 