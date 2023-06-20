package gg.code.productapi.dto.response;

import gg.code.productapi.models.Category;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class CategoryResponse {

    private Integer id;
    private String description;

    /*
        Método que transforma o conteúdo da Model para o tipo CategoryResponse
        A classe util 'BeanUtils' realiza através do metodo copyProperties o 'parse'.
     */
    public static CategoryResponse of(Category category){
        var response = new CategoryResponse();
        BeanUtils.copyProperties(category, response);
        return response;
    }
}
