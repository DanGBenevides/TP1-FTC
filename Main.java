import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception{
        int escolha = 0;
        Scanner scan = new Scanner(System.in);
        System.out.println("Digite o nome do arquivo sem a extensao: ");
        String path = scan.nextLine();

        System.out.println("Escolha qual algoritmo deseja utilizar para minimizar o automato: ");
        System.out.println("1 - Algoritmo de Minimizacao Comum");
        System.out.println("2 - Algoritmo de Minimizacao de Hopcroft");
        escolha = scan.nextInt();
        
        switch(escolha) {
            case 1:
                Automato automato = XmlParser.leAutomato("Automatos/" + path + ".jff");
                automato.minimizaAutomatoComum();    
                String automatoString = automato.automatoToString();
                AutomatoParser automatoParser = new AutomatoParser(automatoString);
                automatoParser.writeXmlFile("Resultados/" + path + "RES.jff");
                break;
            case 2:
                Automato automato2 = XmlParser.leAutomato("Automatos/" + path + ".jff");
                automato2.minimizaAutomatoHopcroft();
                String automatoString2 = automato2.automatoToString();
                AutomatoParser automatoParser2 = new AutomatoParser(automatoString2);
                automatoParser2.writeXmlFile("Resultados/" + path + "RES.jff");
                break;
            default:
                System.out.println("Opção inválida");        
            }
    

        scan.close();
    }
}
