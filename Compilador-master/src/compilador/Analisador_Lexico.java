package compilador;

import static compilador.Compilador.lista_atribuicoes;
import static compilador.Compilador.lista_adjetivo;
import static compilador.Compilador.lista_dispositivos;
import static compilador.Compilador.lista_fabricantes;
import static compilador.Compilador.lista_perguntas;
import static compilador.Compilador.stopWords;
import java.io.BufferedReader;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.portugueseStemmer;

public class Analisador_Lexico {

    public String geraLexema(String palavra) {
        String lexema;
        SnowballStemmer stemmer = new portugueseStemmer(); //http://www.java2s.com/Code/Jar/s/Downloadsnowball10jar.htm
        lexema = palavra;
        if (!verifica_palavra_chave(palavra)) { //Se não for palavra-chave 
            palavra = tiraAcento(palavra);
            lexema = palavra;
            stemmer.setCurrent(palavra);
            stemmer.stem();
            if (!verifica_palavra_chave(palavra)) {
                lexema = stemmer.getCurrent();
            }
            
        }
        return lexema;
    }

    public String trocaPorSinonimo(String lexema) {
        String sinonimo = lexema;
        for (Token fabricante : lista_fabricantes) {
            for (String nome : fabricante.sinonimos) {
                if (sinonimo.equals(nome)) {
                    sinonimo = fabricante.palavra_principal;
                }
            }
        }
        for (Token dispositivo : lista_dispositivos) {
            for (String nome : dispositivo.sinonimos) {
                if (sinonimo.equals(nome)) {
                    sinonimo = dispositivo.palavra_principal;
                }
            }
        }
        for (Token defeito : lista_adjetivo) {
            for (String nome : defeito.sinonimos) {
                if (sinonimo.equals(nome)) {
                    sinonimo = defeito.palavra_principal;
                }
            }
        }
        return sinonimo;
    }

    public String tiraAcento(String palavra) {
        String novaPalavra;
        if (!verifica_palavra_chave(palavra)) { //Se não for palavras de atribuições
            novaPalavra = Normalizer.normalize(palavra, Normalizer.Form.NFD).
                    replaceAll("[^\\p{ASCII}]", "");
        } else {
            novaPalavra = palavra;
        }
        return novaPalavra;
    }

    public void preencheStopWords(BufferedReader br) {
        try {
            String palavra;
            while (br.ready()) {
                palavra = br.readLine();
                if (!verifica_palavra_chave(palavra)) {
                    stopWords.add(geraLexema(palavra));
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void preenchePalavrasChaves(BufferedReader br) {
        try {
            while (br.ready()) {
                String vet[] = br.readLine().split(" ");
                if (vet[1].equals("atribuição")) {
                    lista_atribuicoes.add(vet[0].replaceAll("_", " "));
                } else if (vet[1].equals("pergunta")) {
                    lista_perguntas.add(vet[0].replaceAll("_", " "));
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void preencheLista(BufferedReader br, ArrayList<Token> lista, String tipo) {
        try {
            while (br.ready()) {
                String vetPalavras[] = br.readLine().split(" ");//lista de sinonimos
                String palavra = vetPalavras[0];//palavra principal
             
                palavra = geraLexema(palavra);
                Token token = new Token(palavra, tipo);
                token.add_sinonimos(vetPalavras);          
                lista.add(token);
                //}
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public boolean verifica_palavra_chave(String palavra) {
        return lista_atribuicoes.contains(palavra) || lista_perguntas.contains(palavra);
    }


}
