import amqp from "amqplib/callback_api.js";
import * as rabbitConst from "../../config/rabbitmq/queue.js"
import { RABBIT_MQ_URL } from "../../config/secrets/secrets.js";

const HALF_SECOND = 500;

/*
    Esta função cria a conexão com o RabbitMQ e cria as filas
    PRODUCT_STOCK_UPDATE_QUEUE
    SALES_CONFIRMATION_ROUTING_QUEUE

    Nestas filas a função createQueue, estabelece a ligação delas com as respectivas routingKeys na Exchange(PRODUCT_TOPIC)

    Portanto, mensagem que tiverem a rotingKey nela contida, o RabbitMQ fará o envio desta para as filas do tópico.

    Lembre-se: tópico é um tipo de exchange.
*/

export async function connectRabbitMq() {
    amqp.connect(RABBIT_MQ_URL, (error, connection) => {
        if(error) {
            throw error;
        }

        createQueue(
            connection,
            rabbitConst.PRODUCT_STOCK_UPDATE_QUEUE,
            rabbitConst.PRODUCT_STOCK_UPDATE_ROUTING_KEY,
            rabbitConst.PRODUCT_TOPIC
        );

        createQueue(
            connection,
            rabbitConst.SALES_CONFIRMATION_ROUTING_QUEUE,
            rabbitConst.SALES_CONFIRMATION_ROUTING_KEY,
            rabbitConst.PRODUCT_TOPIC
        );

        setTimeout(function() {
            connection.close();
        }, HALF_SECOND);
    });

    function createQueue(connection, queue, routingKey, topic){
        connection.createChannel((error, channel) => {
            channel.assertExchange(topic, "topic", {durable: true});
            channel.assertQueue(queue, {durable: true});
            channel.bindQueue(queue, topic, routingKey);
        });
    }
}

