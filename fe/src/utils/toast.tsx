import toast from 'react-hot-toast';

export const showToast = {
  success: (message: string) => {
    toast.success(message, {
      duration: 3000,
      position: 'top-right',
    });
  },
  
  error: (message: string) => {
    toast.error(message, {
      duration: 4000,
      position: 'top-center',
    });
  },
  
  warning: (message: string) => {
    toast(message, {
      duration: 4000,
      position: 'top-center',
      icon: '⚠️',
      style: {
        background: '#fef3c7',
        color: '#92400e',
        border: '1px solid #f59e0b',
      },
    });
  },
  
  confirm: (message: string, onConfirm: () => void) => {
    toast((t) => (
      <div className="flex flex-col space-y-3">
        <div className="text-sm font-medium text-gray-900">{message}</div>
        <div className="flex space-x-2">
          <button
            onClick={() => {
              onConfirm();
              toast.dismiss(t.id);
            }}
            className="px-3 py-1 bg-red-500 text-white rounded text-sm hover:bg-red-600 transition-colors"
          >
            Delete
          </button>
          <button
            onClick={() => toast.dismiss(t.id)}
            className="px-3 py-1 bg-gray-300 text-gray-700 rounded text-sm hover:bg-gray-400 transition-colors"
          >
            Cancel
          </button>
        </div>
      </div>
    ), {
      duration: Infinity,
      position: 'top-center',
      style: {
        background: '#fff',
        padding: '16px',
        borderRadius: '8px',
        boxShadow: '0 10px 25px -5px rgba(0, 0, 0, 0.1)',
        marginTop: '30vh',
        transform: 'translateY(-50%)',
      },
    });
  },
}; 