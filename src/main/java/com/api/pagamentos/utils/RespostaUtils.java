package com.api.pagamentos.utils;

import java.util.HashMap;
import java.util.Map;

public class RespostaUtils {
    public static <T> Map<String, T> padronizaRespostas(String chave, T valor){
        Map<String, T> respostaPadrao = new HashMap<>();
        respostaPadrao.put(chave, valor);
        return respostaPadrao;
    }
}
