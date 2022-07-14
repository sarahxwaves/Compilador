package compilador;

import TF_IDF.Arquivo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.portugueseStemmer;
import TF_IDF.TF_IDF;

public class Compilador {

    static ArrayList<String> stopWords = new ArrayList<String>();

    static ArrayList<Token> tabela_simbolos = new ArrayList<Token>();
    static ArrayList<Token> fraseSemSW;

    static ArrayList<String> lista_atribuicoes = new ArrayList<String>();
    static ArrayList<String> lista_perguntas = new ArrayList<String>();

    static ArrayList<String> afirmacao = new ArrayList<String>();
    static ArrayList<String> negacao = new ArrayList<String>();

    static ArrayList<Token> lista_fabricantes = new ArrayList<Token>();
    static ArrayList<Token> lista_dispositivos = new ArrayList<Token>();
        static ArrayList<Token> lista_operacao = new ArrayList<Token>();
    static ArrayList<Token> lista_adjetivo = new ArrayList<Token>();

    static Arquivo resposta_compilador = new Arquivo();
    static Analisador_Lexico a_l = new Analisador_Lexico();

    static Token token_dispositivo;
    static Token token_fabricante;
    static Token token_adjetivo;

    public static void main(String[] args) {
        try {
            File arquivo;
            FileReader fr;
            BufferedReader br;
            String linha;
            String vetPalavras[];
            int n_linha = 1;

            //Arquivo stop words https://github.com/stopwords-iso/stopwords-pt/blob/master/stopwords-pt.txt
            arquivo = new File("entradas/stopwords.txt");
            fr = new FileReader(arquivo);
            br = new BufferedReader(fr);
            a_l.preencheStopWords(br);

            //Arquivo palavras chaves
            arquivo = new File("entradas/palavrasChaves.txt");
            fr = new FileReader(arquivo);
            br = new BufferedReader(fr);
            a_l.preenchePalavrasChaves(br);

            //Arquivo dispositivos
            arquivo = new File("entradas/produto.txt");
            fr = new FileReader(arquivo);
            br = new BufferedReader(fr);
            a_l.preencheLista(br, lista_dispositivos, "dispositivo");
            
             arquivo = new File("entradas/operacao.txt");
            fr = new FileReader(arquivo);
            br = new BufferedReader(fr);
            a_l.preencheLista(br, lista_operacao, "operacao");

            //Arquivo fabricantes
            arquivo = new File("entradas/fabricante.txt");
            fr = new FileReader(arquivo);
            br = new BufferedReader(fr);
            a_l.preencheLista(br, lista_fabricantes, "fabricante");

            //Arquivo defeitos
            arquivo = new File("entradas/adjetivo.txt");
            fr = new FileReader(arquivo);
            br = new BufferedReader(fr);
            a_l.preencheLista(br, lista_adjetivo, "adjetivo");

           
            //Arquivo conversa
            arquivo = new File("entradas/tutoriais/pergunta/arquivo7_p.txt"); //Alterar numero arquivo para ver outras perguntas
            fr = new FileReader(arquivo);
            br = new BufferedReader(fr);
           

          
          resposta_compilador.boasVindas();
            while (br.ready()) { //lê arquivo
              
                fraseSemSW = new <Token>ArrayList();
                linha = br.readLine();
                vetPalavras = separaPalavras(linha); // vetor com todas as palavras da linha atual
                System.out.print("Usuário : ");
               
                adiciona_frase_Sem_SW(vetPalavras, fraseSemSW, true); //Encontra o lexema e exclui as stop words

                System.out.println("\n");
               
                adiciona_tabela_simbolos();
               
                n_linha++;
                procuraResposta();
             //  tf_idf.retornaTutorial(tabela_simbolos); 
               
                imprimeTudo();
                    break;    
            }
     
        } catch (IOException e) {
            System.out.println(e);
        }
        Collections.sort(tabela_simbolos); 
    }
    

    public static void procuraResposta() {
        TF_IDF tf_idf = new TF_IDF();
         boolean encontrouAlgo = false;
        encontrouAlgo = tf_idf.retornaTutorial(tabela_simbolos);
        if (!encontrouAlgo) {
            resposta_compilador.naoEncontrei();
            boolean ajudou = false;
        }
    }
    public static void adiciona_frase_Sem_SW(String vetPalavras[], ArrayList<Token> lista, boolean print) {
        String palavraAtual, lexema;
        
        for (int i = 0; i < vetPalavras.length; i++) { //Lê palavras de um linha

            palavraAtual = vetPalavras[i];
            lexema = a_l.geraLexema(palavraAtual);
            lexema = a_l.trocaPorSinonimo(lexema);

            if (!stopWords.contains(lexema)  && !palavraAtual.isEmpty()) { // se não é stop word 
                
                Token palavra_atual = new Token(lexema);
                lista.add(palavra_atual);
                 if (print) { //Imprime ou não a mensagem do usuário
                    System.out.print(lexema + " ");
                }
            }
        }
    }

   public static void adiciona_tabela_simbolos() {
        for (int i = 0; i < fraseSemSW.size(); i++) {
            Token token_atual = fraseSemSW.get(i);
            if (!a_l.verifica_palavra_chave(token_atual.palavra_principal)) {   //Se não for palavra chave
                    tabela_simbolos.add(token_atual);                      //adiciona na tabela de simbolos

                
            }
        }
    }


    public static String[] separaPalavras(String linha) {
        linha = linha.replaceAll("[\t!;+=)(}{\'\"!,.]", " ").replaceAll("[?]", " ? ");
        return linha.split(" ");
    }

    public static void imprimeLista(ArrayList<String> lista) {
        for (String simbolo : lista) {
            System.out.println(simbolo);
        }
        System.out.println("---------------------------------------");
    }

    public static void imprime_tabela_simbolos() {
        for (Token token : tabela_simbolos) {
            System.out.println(token.palavra_principal);

        }
        System.out.println("---------------------------------------");
    }
    
       public static void imprime_lista_dispositivos() {
        for (Token token : lista_dispositivos) {
            System.out.println(token.palavra_principal);

        }
        System.out.println("---------------------------------------");
    }

    public static void imprimeTudo() {
        System.out.println("---------------------------------------");
        System.out.println("\nTabela de símbolos:");
        imprime_tabela_simbolos();
        
       // System.out.println("Lista de atribuições:");
      //  imprimeLista(lista_atribuicoes);
      //  System.out.println("Lista de perguntas:");
      //  imprimeLista(lista_perguntas);
       // System.out.println("Lista de dispositivos:");
      //  imprime_lista_dispositivos();
      //  System.out.println("Lista de defeitos:");
       // imprimeLista(lista_defeitos);
      //  System.out.println("Lista de fabricantes:");
       // imprimeLista(lista_fabricantes);
         
    }

}
