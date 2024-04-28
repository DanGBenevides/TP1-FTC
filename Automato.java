import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Automato {
    private String[][] matriz;
    private String[] alfabeto;

    Automato(int numEstados, int tamanhoAlfabeto, ArrayList<String> alfabeto) {
        matriz = new String[numEstados][tamanhoAlfabeto + 1];
        this.alfabeto = new String[tamanhoAlfabeto + 1];
        for (int i = 0; i < tamanhoAlfabeto; i++) {
            this.alfabeto[i] = alfabeto.get(i);
        }
    }

    Automato(String[][] matriz, String[] alfabeto) {
        this.matriz = matriz;
        this.alfabeto = alfabeto;
    }

    // Preenche a matriz com os estados, transições e estados iniciais e finais
    public void preencheMatriz(ArrayList<String> estados, int estadoInicial, ArrayList<String> estadosFinais,
            ArrayList<String> transicoes) {
        for (int i = 0; i < estados.size(); i++) {
            for (int j = 0; j < alfabeto.length; j++) {
                matriz[i][j] = "-";
            }
        }

        for (int i = 0; i < estados.size(); i++) {
            if (i == estadoInicial)
                if (estadosFinais.contains(estados.get(i)))
                    matriz[i][0] = "->*" + estados.get(i);
                else
                    matriz[i][0] = "->" + estados.get(i);
            else if (estadosFinais.contains(estados.get(i)))
                matriz[i][0] = "*" + estados.get(i);
            else
                matriz[i][0] = estados.get(i);
        }

        for (String key : transicoes) {
            String[] transicao = key.split(" ");
            int i = Integer.parseInt(transicao[0]);
            int j = 0;
            for (int k = 0; k < alfabeto.length; k++) {
                if (alfabeto[k].equals(transicao[2])) {
                    j = k;
                    matriz[i][j + 1] = transicao[1];
                    break;
                }
            }
        }
    }

    // printa a matriz
    public void printMatriz() {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < alfabeto.length; j++) {
                System.out.print(matriz[i][j] + " ");
            }
            System.out.println();
        }
    }

    // Retorna o automato em formato de string
    public String automatoToString() {
        String resp = "";
        resp += "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!--Created with JFLAP 6.4.--><structure>&#13;\n\t<type>fa</type>&#13;\n\t<automaton>&#13;\n\t\t<!--The list of states.-->&#13;\n";

        for (int i = 0; i < matriz.length; i++) {
            resp += "\t\t<state id=\"" + i + "\" name=\"" + matriz[i][0].replace("->", "").replace("*", "")
                    + "\">&#13;\n";
            resp += "\t\t\t<x>0</x>&#13;\n\t\t\t<y>0</y>&#13;\n";
            if (matriz[i][0].contains("*"))
                resp += "\t\t\t<final/>&#13;\n";
            if (matriz[i][0].contains("->"))
                resp += "\t\t\t<initial/>&#13;\n";
            resp += "\t\t</state>&#13;\n";
        }
        resp += "\t\t<!--The list of transitions.-->&#13;\n";

        for (int i = 0; i < matriz.length; i++) {
            for (int j = 1; j < matriz[i].length; j++) {
                if (!matriz[i][j].equals("-")) {
                    resp += "\t\t<transition>&#13;\n";
                    resp += "\t\t\t<from>" + i + "</from>&#13;\n";
                    resp += "\t\t\t<to>" + matriz[i][j] + "</to>&#13;\n";
                    resp += "\t\t\t<read>" + alfabeto[j - 1] + "</read>&#13;\n";
                    resp += "\t\t</transition>&#13;\n";
                }
            }
        }

        resp += "\t</automaton>&#13;\n</structure>";
        return resp;
    }

    // Completa o automato
    public void completaAutomato() {
        Boolean incompleto = false;

        // Percorre a matriz e verifica se existe algum estado que não possui transição
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 1; j < matriz[i].length; j++) {
                if (matriz[i][j].equals("-")) {
                    incompleto = true;
                    break;
                }
            }
        }

        if (incompleto) {
            String[][] novaMatriz = new String[matriz.length + 1][alfabeto.length];
            novaMatriz[novaMatriz.length - 1][0] = "qE";
            
            for (int i = 1; i < novaMatriz[0].length; i++) {
                novaMatriz[novaMatriz.length - 1][i] = Integer.toString(novaMatriz.length - 1);
            }

            for (int j = 0; j < matriz.length; j++) {
                for (int k = 0; k < matriz[j].length; k++) {
                    if (matriz[j][k].equals("-")) {
                        novaMatriz[j][k] = Integer.toString(novaMatriz.length - 1);
                    } else {
                        novaMatriz[j][k] = matriz[j][k];
                    }
                }
            }

            this.matriz = novaMatriz;
        }
    }

    /*
    * -~-~-~-~-~-~-~-~-~-~ MÉTODO O(N^2) PARA GERAR O AUTOMATO MINIMIZAD -~-~-~-~-~-~-~-~-~-~
    */

    public void minimizaAutomatoComum() {
        ArrayList<ArrayList<Estado>> particoes = new ArrayList<ArrayList<Estado>>();
        Boolean mudou = true;

        // Inicializa a primeira partição
        ArrayList<Estado> particao = new ArrayList<Estado>();
        for (int i = 0; i < matriz.length; i++) {
            
            if (matriz[i][0].contains("*")) {
                particao.add(new Estado(matriz[i][0]));
            }
        }
        particoes.add(particao);

        // Inicializa a segunda partição
        particao = new ArrayList<Estado>();
        for (int i = 0; i < matriz.length; i++) {
            
            if (!matriz[i][0].contains("*")) {
                particao.add(new Estado(matriz[i][0]));
            }
        }
        particoes.add(particao);

        HashMap<Integer, Integer> partition = new HashMap<Integer, Integer>();
        HashMap<Integer, Integer> memoriaPartition = new HashMap<Integer, Integer>();
        while (mudou) {
            partition.clear();
            for (int i = 0; i < particoes.size(); i++) {
                for (Estado estado : particoes.get(i)) {
                    for (int j = 0; j < matriz.length; j++) {
                        
                        if (estado.getNome().equals(matriz[j][0])) {
                            partition.put(j, i);
                        }
                    }
                }
            }
            
            String[][] matrizParticao = cloneMatriz(matriz);
            for (int i = 0; i < matrizParticao.length; i++) {
                for (int j = 1; j < matrizParticao[i].length; j++) {
                    matrizParticao[i][j] = Integer.toString(partition.get(Integer.parseInt(matrizParticao[i][j])));
                }
            }

            String[] todasAsLinhas = new String[matrizParticao.length];
            String linhaDoEstado = "";
            for (int i = 0; i < matrizParticao.length; i++) {
                linhaDoEstado = "";
                for (int j = 1; j < matrizParticao[i].length; j++) {
                    linhaDoEstado += matrizParticao[i][j];
                    todasAsLinhas[i] = linhaDoEstado;
                }
            }

            Boolean[] adicionados = new Boolean[todasAsLinhas.length];
            Arrays.fill(adicionados, false);
            particoes.clear();
            for (int i = 0; i < todasAsLinhas.length; i++) {
                particao = new ArrayList<Estado>();

                
                if (!adicionados[i]) {
                    particao.add(new Estado(matriz[i][0]));
                    adicionados[i] = true;
                }
                for (int j = i + 1; j < todasAsLinhas.length; j++) {
                    
                    if (todasAsLinhas[i].equals(todasAsLinhas[j]) && !adicionados[j]) {
                        if (matriz[i][0].contains("*") == matriz[j][0].contains("*")) {
                            
                            particao.add(new Estado(matriz[j][0]));
                            adicionados[j] = true;
                        } else if (!matriz[i][0].contains("*") == !matriz[j][0].contains("*")) {
                            particao.add(new Estado(matriz[j][0]));
                            adicionados[j] = true;
                        }
                    }

                }
                
                if (particao.size() > 0)
                    particoes.add(particao);

            }
            
            
            if (memoriaPartition.equals(partition)) {
                mudou = false;
            } else {
                memoriaPartition = partition;
            }
        }

        // refaz a matriz a partir das particoes
        String[][] novaMatriz = new String[particoes.size()][alfabeto.length];
        for (int i = 0; i < particoes.size(); i++) {
            novaMatriz[i][0] = particoes.get(i).get(0).getNome();
        }

        String transicao = "";
        int posTransicao = 0;
        for (int i = 0; i < particoes.size(); i++) {
            for (int j = 1; j < alfabeto.length; j++) {
                transicao = testaTransicao(particoes.get(i).get(0).getNome(), j-1);
                posTransicao = Integer.parseInt(transicao);
                transicao = matriz[posTransicao][0];
    
                // verifica se a partição contém o estado e adiciona o índice da partição à nova matriz
                for (int k = 0; k < particoes.size(); k++) {
                    for (Estado estado : particoes.get(k)) {
                        
                        if (estado.getNome().equals(transicao)) {
                            novaMatriz[i][j] = Integer.toString(k);
                            break;
                        }
                    }
                }
            }
        }
         
        matriz = novaMatriz;    
    }

    /*
    * -~-~-~-~-~-~-~-~-~-~ MÉTODO DE HOPCROFT O(n log n) PARA GERAR O AUTOMATO MINIMIZADO -~-~-~-~-~-~-~-~-~-~
    */

    public void minimizaAutomatoHopcroft() {
        ArrayList<ArrayList<Estado>> P = new ArrayList<ArrayList<Estado>>();
        ArrayList<ArrayList<Estado>> W = new ArrayList<ArrayList<Estado>>();

        // Inicializa a primeira partição
        ArrayList<Estado> particao = new ArrayList<Estado>();
        for (int i = 0; i < matriz.length; i++) {
            
            if (matriz[i][0].contains("*")) {
                particao.add(new Estado(matriz[i][0]));
            }
        }
        P.add(particao);
        W.add(particao);

        // Inicializa a segunda partição
        particao = new ArrayList<Estado>();
        for (int i = 0; i < matriz.length; i++) {
            
            if (!matriz[i][0].contains("*")) {
                particao.add(new Estado(matriz[i][0]));
            }
        }
        P.add(particao);
        W.add(particao);


        // Enquanto W não estiver vazio
        while (!W.isEmpty()) {
            // Pega uma partição A de W
            ArrayList<Estado> A = W.get(0);
            W.remove(0);
            

            // Para cada simbolo do alfabeto
            for (int i = 0; i < alfabeto.length-1; i++) {
                ArrayList<Estado> X = new ArrayList<Estado>();
                // preenche X com os estados que chegam em A pelo simbolo i
                for (int j = 0; j < matriz.length; j++) {
                    String transicao = testaTransicao(matriz[j][0], i);
                    transicao = getNomeEstado(Integer.parseInt(transicao));

                    for (Estado estado : A) {
                        // verifica se o estado chega em A pelo simbolo i
                        if (estado.getNome().equals(transicao)) {
                            X.add(new Estado(matriz[j][0]));
                        }
                    }
                }

                // Para cada partição Y de P
                for (int j = 0; j < P.size(); j++) {
                    ArrayList<Estado> Y = new ArrayList<Estado>();
                    Y.addAll(P.get(j));
                    
                    // Se a interseção de X e Y não for vazia e a diferença de Y e X não for vazia
                    if (!intersecao(X, Y).isEmpty() && !diferenca(Y, X).isEmpty()) {
                        int indiceYemP = getIndiceParticao(P, Y);
                        P.remove(indiceYemP);
                        P.add(intersecao(X, Y));
                        P.add(diferenca(Y, X));
                        
                        // Se Y estiver em W
                        if (W.size() > 0 && possui(W, Y)) {
                            int indice = getIndiceParticao(W, Y);
                            W.remove(indice);

                            W.add(intersecao(X, Y));
                            W.add(diferenca(Y, X));
                        } else {
                            if (intersecao(X, Y).size() <= diferenca(Y, X).size()) {
                                W.add(intersecao(X, Y));
                            } else {
                                W.add(diferenca(Y, X));
                            }
                        }
                    }
                    Y.clear();
                }
              
            }
        }

        // refaz a matriz a partir das particoes
        String[][] novaMatriz = new String[P.size()][alfabeto.length];
        for (int i = 0; i < P.size(); i++) {
            novaMatriz[i][0] = P.get(i).get(0).getNome();
        }

        String transicao = "";
        int posTransicao = 0;
        for (int i = 0; i < P.size(); i++) {
            for (int j = 1; j < alfabeto.length; j++) {
                transicao = testaTransicao(P.get(i).get(0).getNome(), j-1);
                posTransicao = Integer.parseInt(transicao);
                transicao = matriz[posTransicao][0];
    
                // verifica se a partição contém o estado e adiciona o índice da partição à nova matriz
                for (int k = 0; k < P.size(); k++) {
                    for (Estado estado : P.get(k)) {
                        
                        if (estado.getNome().equals(transicao)) {
                            novaMatriz[i][j] = Integer.toString(k);
                            break;
                        }
                    }
                }
            }
        }

        matriz = novaMatriz;
    }


    // Verifica se a partição está contida em particoes
    public Boolean possui (ArrayList<ArrayList<Estado>> particoes, ArrayList<Estado> particao) {
        int particaoIndex = getIndiceParticao(particoes, particao);
        int cont = 0;
        for (int i = 0; i < particoes.get(particaoIndex).size(); i++) {
            if (particoes.get(particaoIndex).get(i).getNome().equals(particao.get(0).getNome())) {
                cont++;
            }
        }
        if (particao.size() == particoes.get(particaoIndex).size() && cont == particao.size()) {
            return true;
        }
        return false;
    }

    // Clona uma partição
    public ArrayList<Estado> cloneEstados(ArrayList<Estado> estados) {
        ArrayList<Estado> resp = new ArrayList<Estado>();
        for (int i = 0; i < estados.size(); i++) {
            resp.add(new Estado(estados.get(i).getNome()));
        }
        return resp;
    }

    // Clona todas as partições
    public ArrayList<ArrayList<Estado>> cloneParticoes(ArrayList<ArrayList<Estado>> particoes) {
        ArrayList<ArrayList<Estado>> resp = new ArrayList<ArrayList<Estado>>();
        for (int i = 0; i < particoes.size(); i++) {
            ArrayList<Estado> particao = new ArrayList<Estado>();
            for (int j = 0; j < particoes.get(i).size(); j++) {
                particao.add(particoes.get(i).get(j));
            }
            resp.add(particao);
        }
        return resp;

    }

    // Retorna o índice da partição que contém o estado
    public int getIndiceParticao(ArrayList<ArrayList<Estado>> particoes, ArrayList<Estado> particao) {
        int resp = 0;
        for (int i = 0; i < particoes.size(); i++) {
            for (int j = 0; j < particoes.get(i).size(); j++) {
                if (particoes.get(i).get(j).getNome().equals(particao.get(0).getNome())) {
                    resp = i;
                    break;
                }
            }
        }
        return resp;
    }


    // Retorna a diferença entre duas partições
    public ArrayList<Estado> diferenca(ArrayList<Estado> A, ArrayList<Estado> B) {
        ArrayList<Estado> resp = new ArrayList<Estado>();
        for (Estado estadoA : A) {
            Boolean contem = false;
            for (Estado estadoB : B) {
                if (estadoA.getNome().equals(estadoB.getNome())) {
                    contem = true;
                }
            }
            if (!contem) resp.add(estadoA);
        }
        
        return resp;
    }

    // Retorna a interseção entre duas partições
    public ArrayList<Estado> intersecao(ArrayList<Estado> A, ArrayList<Estado> B) {
        ArrayList<Estado> resp = new ArrayList<Estado>();
        for (Estado estadoA : A) {
            for (Estado estadoB : B) {
                if (estadoA.getNome().equals(estadoB.getNome())) {
                    resp.add(estadoA);
                }
            }
        }
        return resp;
    }


    // Retorna o índice do estado na matriz
    public int getIndiceEstado(String estado) {
        int resp = 0;
        for (int i = 0; i < matriz.length; i++) {
            if (matriz[i][0].equals(estado)) {
                resp = i;
                break;
            }
        }
        return resp;
    }

    // Retorna o índice do estado na matriz
    public String testaTransicao(String estado, int simbolo) {
        for (int i = 0; i < matriz.length; i++) {
            if (matriz[i][0].equals(estado)) {
                for (int j = 1; j < matriz[i].length; j++) {
                    if (j == simbolo + 1) {
                        return matriz[i][j];
                    }
                }
            }
        }
        return "ERRO";
    }

    // Retorna o nome do estado a partir do índice
    public String getNomeEstado(int estado) {
        return matriz[estado][0];
    }

    // Retorna o índice da partição que contém o estado
    public int getIndiceEstado(ArrayList<ArrayList<Estado>> particoes, String estado) {
        int resp = 0;

        for (int i = 0; i < particoes.size(); i++) {
            for (int j = 0; j < particoes.get(i).size(); j++) {
                if (particoes.get(i).get(j).getNome().equals(estado)) {
                    resp = i;
                    break;
                }
            }
        }

        return resp;
    }

    // Clona a matriz
    public String[][] cloneMatriz(String[][] matriz) {
        String[][] resp = new String[matriz.length][matriz[0].length];

        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                resp[i][j] = matriz[i][j];
            }
        }

        return resp;
    }
}