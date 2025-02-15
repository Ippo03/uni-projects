import * as dotenv from 'dotenv';

dotenv.config();

export interface IServerConfig {
    corsOrigin: string;
    announcedAddress: string;
    port: number;
}

export const serverConfig: IServerConfig = {
    corsOrigin: process.env.CORS_ORIGIN ?? 'http://localhost',
    announcedAddress: process.env.ANNOUNCED_ADDRESS ?? '127.0.0.1',
    port: parseInt(process.env.PORT ?? '3030', 10)
};