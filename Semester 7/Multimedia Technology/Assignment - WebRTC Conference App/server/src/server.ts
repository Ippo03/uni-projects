import * as fs from 'fs';
import express from 'express';
import {createServer} from 'https';
import {Server} from 'socket.io';

import {RoomManager} from "./handlers/RoomManager";
import {handleConnection} from "./handlers/socketHandler";
import {mediasoupConfig} from "./util/mediasoup/mediasoupConfig";
import {serverConfig} from "./util/configLoader";
import {logger} from "./util/logger";
import {WorkerManager} from "./handlers/WorkerManager";


async function startServer() {
    logger.info('Configuring server...');
    const app = express();
    const allowedOrigins = serverConfig.corsOrigin.split(',');
    const options = {
        key: fs.readFileSync('keys/server.key'),
        cert: fs.readFileSync('keys/server.crt'),
    };

    const httpServer = createServer(options, app);
    const io = new Server(httpServer, {
        cors: {
            origin: (origin, callback) => {
                if (!origin || allowedOrigins.includes(origin)) {
                    callback(null, true);
                } else {
                    logger.error(`CORS error: ${origin} not allowed`);
                    callback(new Error('Not allowed by CORS'));
                }
            },
            methods: ['GET', 'POST'],
        },
    });
    logger.info("Server configuration successful.");

    /* Create workers for this server instance */
    const workerManager = new WorkerManager(mediasoupConfig.mediasoup.worker.numWorkers);
    await workerManager.initializeWorkers();

    /* Create room manager that will handle room creation, closing, and peer management */
    const roomManager = new RoomManager(workerManager);

    /* Handle socket connections - on connection of user, delegate to handleConnection */
    io.on('connection', (socket) => {
        logger.info('Client connected:', socket.id);
        handleConnection(serverConfig, socket, roomManager);
    });

    const port = serverConfig.port;
    const host = serverConfig.announcedAddress
    httpServer.listen(Number(port), host, () => {
        logger.info(`Server running on https://${host}:${port}`);
    });
}

startServer().catch(error => {
    logger.error('Failed to start server:', error);
    process.exit(1);
});
