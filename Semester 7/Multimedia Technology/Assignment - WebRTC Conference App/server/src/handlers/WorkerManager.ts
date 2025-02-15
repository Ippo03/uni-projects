import * as mediasoup from 'mediasoup';
import * as os from 'os';
import { mediasoupConfig } from "../util/mediasoup/mediasoupConfig";
import { logger } from "../util/logger";

export class WorkerManager {
    private workers: mediasoup.types.Worker[] = [];
    private workerRoomCounts: Map<string, number> = new Map();

    constructor(private maxWorkers?: number) {}

    async initializeWorkers(): Promise<void> {
        const numCPUs = os.cpus().length;
        const workerCount = Math.min(numCPUs, this.maxWorkers || numCPUs);
        logger.info(`Creating ${workerCount} mediasoup workers...`);

        for (let i = 0; i < workerCount; i++) {
            const worker = await this.createWorker();
            this.workers.push(worker);
            this.workerRoomCounts.set(String(worker.pid), 0);
        }

        logger.info("Worker creation successful.");
    }

    private async createWorker(): Promise<mediasoup.types.Worker> {
        const worker = await mediasoup.createWorker({
            rtcMinPort: mediasoupConfig.mediasoup.worker.rtcMinPort,
            rtcMaxPort: mediasoupConfig.mediasoup.worker.rtcMaxPort,
            logLevel: mediasoupConfig.mediasoup.worker.logLevel,
            logTags: mediasoupConfig.mediasoup.worker.logTags,
        });

        logger.info(`Mediasoup [${worker.pid}] worker created.`);

        worker.on('died', () => {
            logger.error(`Mediasoup worker [${worker.pid}] died, exiting in 2 seconds...`);
            setTimeout(() => process.exit(1), 2000);
        });

        return worker;
    }

    getLeastLoadedWorker(): mediasoup.types.Worker {
        const workerEntries = Array.from(this.workerRoomCounts.entries());
        const [leastLoadedPid] = workerEntries.reduce((min, current) =>
            current[1] < min[1] ? current : min
        );

        return this.workers.find(() => String(leastLoadedPid))!;
    }

    incrementWorkerLoad(worker: mediasoup.types.Worker): void {
        const currentCount = this.workerRoomCounts.get(String(worker.pid)) || 0;
        this.workerRoomCounts.set(String(worker.pid), currentCount + 1);
        logger.debug(`Worker ${worker.pid} assigned to room.`);
    }

    decrementWorkerLoad(worker: mediasoup.types.Worker): void {
        const currentCount = this.workerRoomCounts.get(String(worker.pid)) || 0;
        if (currentCount > 0) {
            this.workerRoomCounts.set(String(worker.pid), currentCount - 1);
        }
        logger.debug(`Worker ${worker.pid} un-assigned from room.`);
    }

    getWorkerCount(): number {
        return this.workers.length;
    }

    getWorkerLoad(worker: mediasoup.types.Worker): number {
        return this.workerRoomCounts.get(String(worker.pid)) || 0;
    }
}