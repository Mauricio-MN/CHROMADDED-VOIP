package pasaud.voip;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigReader {

	private final Map<String, String> configMap = new HashMap<>();

    public ConfigReader(String filePath) throws IOException {
        loadConfig(filePath);
    }

    // Método que carrega as configurações do arquivo
    private void loadConfig(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Ignora comentários e linhas vazias
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                // Separa a linha em chave e valor
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    configMap.put(parts[0].trim(), parts[1].trim());
                } else {
                    System.err.println("Linha de configuração inválida: " + line);
                }
            }
        }
    }

    // Método para obter uma string
    public String getString(String key) {
    	String result = configMap.get(key);
    	if(result == null) result = "";
        return result;
    }

    // Método para obter um valor int
    public int getInt(String key) {
        String value = configMap.get(key);
        if(value == null) value = "";
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("O valor para a chave '" + key + "' não é um inteiro válido.");
        }
    }

    // Método para obter um valor booleano
    public boolean getBoolean(String key) {
        String value = configMap.get(key);
        if(value == null) value = "";
        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
            if(value.equalsIgnoreCase("true")) {
            	return true;
            } else {
            	return false;
            }
        } else {
            throw new IllegalArgumentException("O valor para a chave '" + key + "' não é um booleano válido.");
        }
    }

    // Método para obter um valor long, caso seja necessário
    public long getLong(String key) {
        String value = configMap.get(key);
        if(value == null) value = "";
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("O valor para a chave '" + key + "' não é um long válido.");
        }
    }

    // Método para obter o mapa de configuração completo (se for necessário)
    public Map<String, String> getAllConfigs() {
        return configMap;
    }
}
