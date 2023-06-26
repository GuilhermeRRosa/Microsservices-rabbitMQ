package gg.code.productapi.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gg.code.productapi.dto.rabbit.ProductStockDTO;
import gg.code.productapi.models.Product;
import gg.code.productapi.services.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductStockListener {

    @Autowired
    private ProductService productService;

    @RabbitListener(queues = "${app-config.rabbit.queue.product-stock}")
    public void receiveProductStockMessage(ProductStockDTO product) throws JsonProcessingException {
        log.info("Recebendo Mensagem: {}", new ObjectMapper().writeValueAsString(product));
        productService.updateProductStock(product);
    }

}
