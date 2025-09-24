import React from 'react';
import { motion } from 'framer-motion';

interface InputProps {
  type: string;
  placeholder: string;
  value: string;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  icon?: React.ReactNode;
  maxLength?: number;
  className?: string;
}

const Input: React.FC<InputProps> = ({
  type,
  placeholder,
  value,
  onChange,
  icon,
  maxLength,
  className = '',
}) => {
  return (
    <motion.div
      className={`relative ${className}`}
      initial={{ opacity: 0, y: 10 }}
      animate={{ opacity: 1, y: 0 }}
    >
      {icon && (
        <div className="absolute left-4 top-1/2 transform -translate-y-1/2 text-gray-500">
          {icon}
        </div>
      )}
      <input
        type={type}
        placeholder={placeholder}
        value={value}
        onChange={onChange}
        maxLength={maxLength}
        className={`pill-input ${icon ? 'pl-12' : ''}`}
      />
    </motion.div>
  );
};

export default Input;