package TF_IDF;

import compilador.Token;
import static compilador.Compilador.adiciona_frase_Sem_SW;
import static compilador.Compilador.separaPalavras;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Arquivo {
    
    boolean perguntado = false;
    boolean dispositivoInformado = false;
    boolean fabricanteInformado = false;
    boolean defeitoInformado = false;
    boolean respondido = false;
    boolean encontrouAlgo = false;
    boolean ajudou = false;
 

    ArrayList<Token> tokens = new <Token>ArrayList();
    File arquivoPergunta;
    File arquivoResposta;
    int numeroDePalavras;
    ArrayList<Double> lista_pontuacaoTF = new <Double> ArrayList();
    ArrayList<Double> lista_pontuacaoIDF = new <Double> ArrayList();
    double relevancia = 0;

    public Arquivo() {

    }

    public Arquivo(File arquivoPergunta, File arquivoResposta) {
        this.arquivoPergunta = arquivoPergunta;
        this.arquivoResposta = arquivoResposta;
    }

    public void preenche_lista_tokens() throws Exception {
        String vetPalavras[];
        String linha;
        FileReader fr = new FileReader(arquivoPergunta);
        BufferedReader br = new BufferedReader(fr);
        while (br.ready()) {
            linha = br.readLine();
            vetPalavras = separaPalavras(linha);
            adiciona_frase_Sem_SW(vetPalavras, tokens, false);
        }

    }
    
    public void calcula_relevancia() {
         for (int i = 0; i < lista_pontuacaoTF.size(); i++) { 
            relevancia += lista_pontuacaoTF.get(i) * lista_pontuacaoIDF.get(i);
           // System.out.println("Relevancia: " + relevancia);
        }
    }

    public double calcula_IDF(int numeroArquivos_totais, int nArquivos_palavra) {
        double idf;
        if (nArquivos_palavra != 0) {
            idf = Math.log1p(((double) numeroArquivos_totais) / ((double) nArquivos_palavra));
            //System.out.println("IDF" + idf);
        } else {
            idf = 0;
        }
        return idf;
    }

    public double calcula_TF(String palavra) {
        numeroDePalavras = tokens.size();
        int frequenciaPalavra = frequenciaPalavra(palavra);
        double tf = ((double) frequenciaPalavra) / numeroDePalavras;
      // System.out.println("TF" + tf);

        return tf;
    }

    public int frequenciaPalavra(String palavra) {
        int frequencia = 0;
        for (Token token_atual : tokens) {
          
            if (palavra.equals(token_atual.getPalavra_principal())) { //Cada vez que encontra a palavra na lista, aumenta a frequencia
                frequencia++;
            }
          //  System.out.println("Frequencia" + frequencia);
        }
        return frequencia;
    }
    
 

    public void imprimeArquivo() {
        String linha;
        try {
            FileReader fr = new FileReader(arquivoResposta);
            BufferedReader br = new BufferedReader(fr);
            System.out.print("Resposta ChatBot: ");
            while (br.ready()) {
                linha = br.readLine();
                System.out.println(linha);
            }
        } catch (Exception e) {
          System.out.println(e);
        }
    }
    
     public void pedirInformacao() { // Implementar
        if (!dispositivoInformado) {
            qualDispositivo();
        } else if (!fabricanteInformado) {
            qualFabricante();
        } else if (!defeitoInformado) {
            qualDefeito();
        }
    }
      public void naoEncontrei() {
        System.out.println("ChatBot: Não encontramos nenhuma informação sobre isso\n");
    }
      
    public void boasVindas() {
        System.out.println("ChatBot: Olá, em que posso ajudar?\n");
    }

    public void qualDispositivo() {
        System.out.println("ChatBot: Qual o seu dispositivo?\n");
    }

    public void qualFabricante() {
        System.out.println("ChatBot: Qual a marca do seu dispositivo?\n");
    }

    public void qualDefeito() {
        System.out.println("ChatBot: Qual é o problema?\n");
    }

    public void naoEntendiResposta() {
        System.out.println("ChatBot: Não entendi sua resposta. :/\n");
    }
    
    public void naoTemosSuporte() {
        System.out.println("ChatBot: Infelizmente não temos suporte para sua pergunta\n");
    }

}
