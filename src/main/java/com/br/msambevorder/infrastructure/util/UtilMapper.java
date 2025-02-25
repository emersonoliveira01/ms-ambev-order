package com.br.msambevorder.infrastructure.util;

import com.br.msambevorder.infrastructure.exception.JsonMappingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Classe utilitária para mapeamento de objetos para JSON e vice-versa.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UtilMapper {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Converte um objeto em sua representação JSON.
     *
     * @param object o objeto a ser convertido
     * @return a string JSON representando o objeto
     * @throws JsonMappingException em caso de erro na conversão para JSON
     */
    public static String toJson(Object object) {
        try {
            return MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new JsonMappingException("Error converting object to JSON", e);
        }
    }

    /**
     * Converte uma string JSON em um objeto do tipo especificado.
     *
     * @param <T>     o tipo do objeto de retorno
     * @param message a string JSON a ser convertida
     * @param clazz   a classe do tipo de retorno
     * @return o objeto resultante da conversão do JSON
     * @throws JsonMappingException em caso de erro na conversão do JSON
     */
    public static <T> T fromJson(String message, Class<T> clazz) {
        try {
            return MAPPER.readValue(message, clazz);
        } catch (JsonProcessingException e) {
            throw new JsonMappingException("Error parsing JSON message", e);
        }
    }
}
