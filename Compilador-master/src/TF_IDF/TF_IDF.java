package TF_IDF;
import TF_IDF.Arquivo;

import compilador.Token;
import java.io.File;
import java.util.ArrayList;

public class TF_IDF {

    ArrayList<Arquivo> lista_arquivos = new <Arquivo>ArrayList();
      

    public boolean retornaTutorial(ArrayList<Token> tabela_simbolos) {
        Arquivo file_mais_relevante;
        leitura_arquivos();
        calcula_TF_IDF(tabela_simbolos);
        file_mais_relevante = encontra_arquivo_mais_relevante();
        if (file_mais_relevante.relevancia > 0) {
            file_mais_relevante.imprimeArquivo();
            return true;
        }
        return false;
    }
    
    public void leitura_arquivos() {
        try {
            int i = 1;
            while (true) { //Le todos os arquivos
                File arquivoPergunta = new File("entradas/tutoriais/pergunta/arquivo" + i +  "_p.txt");
                File arquivoResposta = new File("entradas/tutoriais/resposta/arquivo" + i + "_r.txt");
                Arquivo file = new Arquivo(arquivoPergunta, arquivoResposta);
                file.preenche_lista_tokens();
                lista_arquivos.add(file);
                i++;
            }
        } catch (Exception e) {
           // sem arquivos pra ler
        }
    }
    
 
    public void calcula_TF_IDF(ArrayList<Token> tabela_simbolos) {
        int nArquivos_possui_token_atual = 0;
        double pontuacaoTF;
        double pontuacaoIDF;

        for (Token token : tabela_simbolos) { //Calcula o TF/IDF para cada token
            for (Arquivo file : lista_arquivos) { //Calcula o TF do arquivo para esse token 
                pontuacaoTF = file.calcula_TF(token.getPalavra_principal());
                if (pontuacaoTF > 0) {
                    nArquivos_possui_token_atual++;
                }
                file.lista_pontuacaoTF.add(pontuacaoTF);
            }

            for (Arquivo file : lista_arquivos) { //Calcula o IDF do arquivo para esse token
                pontuacaoIDF = file.calcula_IDF(lista_arquivos.size(), nArquivos_possui_token_atual);
                file.lista_pontuacaoIDF.add(pontuacaoIDF);
            }
        }
    }

    public Arquivo encontra_arquivo_mais_relevante() {
        Arquivo arquivo_maior_relevancia = new Arquivo();
        for (Arquivo file : lista_arquivos) {
            file.calcula_relevancia();
            if (arquivo_maior_relevancia.relevancia < file.relevancia) {
                arquivo_maior_relevancia = file;
               // System.out.println("Relevancia" + file.relevancia);
            }
        }
        return arquivo_maior_relevancia;
    }
   

}
