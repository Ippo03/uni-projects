import winston from "winston";

const level: string = process.argv.indexOf('-d') > -1 ? 'debug' : 'info';
const consoleTransport = new winston.transports.Console({ level });

export const logger = winston.createLogger({
    format: winston.format.combine(
        winston.format.timestamp(),
        winston.format.printf(({timestamp, level, message}) => {
                return `[${timestamp}] ${level}: ${message}`;
            }
        ),
        winston.format.colorize({ all: true }),
    ),
    transports: [
        consoleTransport
    ]
});

logger.warn(`Logger set to level ${level}.`)

