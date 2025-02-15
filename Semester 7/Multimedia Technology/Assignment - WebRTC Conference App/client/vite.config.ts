import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import fs from 'fs';
import path from 'path';
import dotenv from 'dotenv';

dotenv.config(); // Load .env variables

export default defineConfig({
  plugins: [react()],
  define: {
    'process.env.REACT_APP_SERVER_URL': JSON.stringify(process.env.REACT_APP_SERVER_URL),
  },
  server: {
    https: {
      key: fs.readFileSync(path.resolve(__dirname, 'keys/server.key')),
      cert: fs.readFileSync(path.resolve(__dirname, 'keys/server.crt')),
    },
    host: '0.0.0.0',
  },
});