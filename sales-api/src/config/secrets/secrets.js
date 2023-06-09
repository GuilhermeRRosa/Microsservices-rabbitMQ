const env = process.env

export const MONGO_DB_URL = env.MONGO_DB_URL ? env.MONGO_DB_URL : "mongodb://localhost:27017/sales-db"
export const API_SECRET = env.API_SECRET ? encodeURI.API_SECRET : "bXktYXBpLWF1dGgtbm9kZS1zZWNyZXQtMTYwNjIwMjM=";
export const RABBIT_MQ_URL = env.RABBIT_MQ_URL ? env.RABBIT_MQ_URL : "amqp://localhost:5672";