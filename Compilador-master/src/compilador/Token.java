package compilador;

import static compilador.Compilador.a_l;
import java.util.ArrayList;

public class Token implements Comparable {

    String palavra_principal;
    String tipo_token;
    ArrayList<String> sinonimos = new <String> ArrayList();

    public Token() {
    }

    public Token(String palavra_principal) {
        this.palavra_principal = palavra_principal;
    }
    
    public Token(String palavra_principal,String tipo_token) {
        this.palavra_principal = palavra_principal;
        this.tipo_token = tipo_token;
    }

    public String getPalavra_principal() {
        return palavra_principal;
    }

    public void setPalavra_principal(String palavra_principal) {
        this.palavra_principal = palavra_principal;
    }

    public ArrayList<String> getSinonimos() {
        return sinonimos;
    }

    public void setSinonimos(ArrayList<String> sinonimos) {
        this.sinonimos = sinonimos;
    }

    public String getTipo_token() {
        return tipo_token;
    }

    public void setTipo_token(String tipo_token) {
        this.tipo_token = tipo_token;
    }
    
    

    @Override
    public int compareTo(Object t) {
        Token outra_palavra = (Token) t;
        return this.palavra_principal.compareTo(outra_palavra.palavra_principal);
    }

    public void add_sinonimos(String vetPalavras[]) {
        String palavra_atual, lexema;
        if (vetPalavras.length != 0) {
            for (int i = 1; i < vetPalavras.length; i++) {
                palavra_atual = vetPalavras[i]; 
                lexema = a_l.geraLexema(palavra_atual);
                sinonimos.add(lexema);
            }
        }
    }
}
