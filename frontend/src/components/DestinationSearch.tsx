import React, { useState } from 'react';
import { Search, MapPin, X } from 'lucide-react';
import { motion, AnimatePresence } from 'framer-motion';

interface DestinationSearchProps {
  onSelect?: (destination: string) => void;
  onClose?: () => void;
}

const DestinationSearch: React.FC<DestinationSearchProps> = ({ onSelect, onClose }) => {
  const [query, setQuery] = useState('');
  const [suggestions, setSuggestions] = useState<string[]>([]);

  const handleSearch = (value: string) => {
    setQuery(value);
    setSuggestions(
      value
        ? [
            `${value} Mall`,
            `${value} Park`,
            `${value} Station`,
            `${value} Hospital`,
          ]
        : []
    );
  };

  const handleSelect = (s: string) => {
    onSelect?.(s);
    onClose?.();
  };

  return (
    <div className="fixed inset-0 z-[9999] bg-black/50 flex items-start justify-center p-4">
      <div className="w-full max-w-lg bg-white rounded-2xl shadow-2xl p-4 pointer-events-auto">
        <div className="flex items-center justify-between mb-3">
          <h3 className="text-lg font-semibold">Choose destination</h3>
          {onClose && (
            <button onClick={onClose} className="p-2 rounded hover:bg-gray-100">
              <X className="w-5 h-5" />
            </button>
          )}
        </div>
        <div className="flex items-center bg-white rounded-full shadow-md px-4 py-2 border border-gray-200">
          <Search className="w-5 h-5 text-gray-400" />
          <input
            autoFocus
            type="text"
            value={query}
            onChange={(e) => handleSearch(e.target.value)}
            placeholder="Where to?"
            className="ml-2 flex-1 bg-transparent outline-none"
          />
        </div>

        <AnimatePresence>
          {suggestions.length > 0 && (
            <motion.ul
              initial={{ opacity: 0, y: -8 }}
              animate={{ opacity: 1, y: 0 }}
              exit={{ opacity: 0, y: -8 }}
              className="mt-3 w-full bg-white rounded-xl shadow-lg overflow-hidden z-[10000] border border-gray-100"
            >
              {suggestions.map((s) => (
                <li
                  key={s}
                  onClick={() => handleSelect(s)}
                  className="flex items-center gap-3 p-3 hover:bg-gray-50 cursor-pointer"
                >
                  <MapPin className="w-4 h-4 text-teal-600" />
                  <span>{s}</span>
                </li>
              ))}
            </motion.ul>
          )}
        </AnimatePresence>
      </div>
    </div>
  );
};

export default DestinationSearch;