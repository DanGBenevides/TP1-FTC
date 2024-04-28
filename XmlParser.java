import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class XmlParser {
    public static Automato leAutomato(String path) throws Exception {
        ArrayList<String> estados = new ArrayList<String>();
        ArrayList<String> finais = new ArrayList<String>();
        int inicial = 0;
        ArrayList<String> transicoes = new ArrayList<String>();
        ArrayList<String> alfabeto = new ArrayList<String>();
  
        BufferedReader reader = new BufferedReader(new FileReader(path));
        
        String line = reader.readLine();

        while (line != null) {
            if (line.contains("<state")) {
                estados.add(getNome(line));

                for (int i = 0; i < 3; i++) line = reader.readLine(); 
                if (line.contains("initial")) inicial = estados.size() - 1;
                if (line.contains("final")) finais.add(estados.get(estados.size() - 1));
            }
            if (line.contains("final")) finais.add(estados.get(estados.size() - 1));

            if (line.contains("<transition")) {
                line = reader.readLine();
                String from = getTransicoes(line);
                line = reader.readLine();
                String to = getTransicoes(line);
                line = reader.readLine();
                String read = getTransicoes(line);

                if (!alfabeto.contains(read)) alfabeto.add(read);

                String transicao = from + " " + to + " " + read;
                transicoes.add(transicao);
            }

            line = reader.readLine();
        }

        Automato automato = new Automato(estados.size(), alfabeto.size(), alfabeto);
        automato.preencheMatriz(estados, inicial, finais, transicoes);
        automato.completaAutomato();
        reader.close();
        System.out.println("Automato lido com sucesso!: " + path + "\n");
        return automato;
    }



    public static String getTransicoes(String line) {
        String resp = "";
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == '>') {
                for (int j = i + 1; line.charAt(j) != '<'; j++) {
                    resp += line.charAt(j);
                }
                i = line.length();
            }
        }
        return resp.trim();
    }

    public static String getNome(String line) {
        String resp = "";

        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == 'n') {
                for (int j = i + 6; line.charAt(j) != '"'; j++) {
                    resp += line.charAt(j);
                }
            }
        }
        return resp;
    }
}