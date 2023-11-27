import java.util.ArrayList;
import java.util.List;

public class ASLR implements Parser{
    private int i = 0;
    private boolean hayErrores = false;
    private Token preanalisis;
    private final List<Token> tokens;
    private final List<String> Frase = new ArrayList<>();
    public ASLR(List<Token> tokens){
        this.tokens = tokens;
        preanalisis = this.tokens.get(i);
    }

    @Override
    public boolean parse() {
        Q();
        System.out.println(Frase);
        if(preanalisis.tipo == TipoToken.EOF && !hayErrores){
            System.out.println("Cadena aceptada");
            return  true;
        }else {
            System.out.println("Se encontraron errores");
        }
        return false;
    }
    // Q -> select D from T
    private void Q(){
        match(TipoToken.SELECT);
        System.out.println(Frase);
        D();
        System.out.println(Frase);
        match(TipoToken.FROM);
        System.out.println(Frase);
        T();
        System.out.println(Frase);
        returner(4);
        Frase.add("Q");
    }

    // D -> distinct P | P
    private void D(){
        if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.DISTINCT){
            match(TipoToken.DISTINCT);
            System.out.println(Frase);
            P();
            System.out.println(Frase);
            returner(2);
            Frase.add("D");
        }
        else if (preanalisis.tipo == TipoToken.ASTERISCO
                || preanalisis.tipo == TipoToken.IDENTIFICADOR
                || preanalisis.tipo == TipoToken.ID) {
            P();
            System.out.println(Frase);
            returner(1);
            Frase.add("D");
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba 'distinct' or '*' or 'identificador'");
        }
    }
    // P -> * | A
    private void P(){
        if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.ASTERISCO){
            match(TipoToken.ASTERISCO);
            System.out.println(Frase);
            returner(1);
            Frase.add("P");
        }
        else if(preanalisis.tipo == TipoToken.IDENTIFICADOR
                || preanalisis.tipo == TipoToken.ID){
            A();
            System.out.println(Frase);
            returner(1);
            Frase.add("P");
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba '*' or 'identificador'");
        }
    }
    // A -> A,A1|A1
    private void A(){
        if(hayErrores)
            return;
        if(preanalisis.tipo == TipoToken.ID){
            A1();
            System.out.println(Frase);
            returner(1);
            Frase.add("A");
        }
        else if(preanalisis.tipo == TipoToken.IDENTIFICADOR){
            A1();
            System.out.println(Frase);
            returner(1);
            Frase.add("A");
        }
        else {
            hayErrores=true;
            System.out.println("Se esperaba un identificador,coma o id");
        }
    }
    // A2 -> .id | Ɛ
    private void A2(){
        if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.PUNTO) {
            match(TipoToken.PUNTO);
            match(TipoToken.ID);
            System.out.println(Frase);
            returner(2);
            Frase.add("A2");
        }
        else{
            Frase.add("Ɛ");
            System.out.println(Frase);
            returner(2);
            Frase.add("A2");
        }

    }
    // A1 -> idA2 | Ɛ
    private void A1(){
        if(hayErrores)
            return;
        if (preanalisis.tipo==TipoToken.COMA){
            match(TipoToken.COMA);
            System.out.println(Frase);
            A();
            returner(2);
            Frase.add("A");
        }
        if(preanalisis.tipo == TipoToken.ID){
            match(TipoToken.ID);
            A2();
            System.out.println(Frase);
            returner(1);
            Frase.add("A1");
        } else if (preanalisis.tipo == TipoToken.IDENTIFICADOR){
            match(TipoToken.IDENTIFICADOR);
            A2();
            System.out.println(Frase);
            returner(1);
            Frase.add("A1");
        }
        else {
            hayErrores=true;
            System.out.println("Se esperaba un id");
        }
    }
    //T->T,T1|T1
    private void T(){
        if(hayErrores)
            return;
        if(preanalisis.tipo == TipoToken.ID){
            T1();
            System.out.println(Frase);
            returner(1);
            Frase.add("T");
        }
        else if(preanalisis.tipo == TipoToken.IDENTIFICADOR){
            T1();
            System.out.println(Frase);
            returner(1);
            Frase.add("T");
        }
        else {
            hayErrores=true;
            System.out.println("Se esperaba un identificador,coma o id");
        }

    }
    //T1->id T2
    private void T1(){
        if(hayErrores)
            return;
        if(preanalisis.tipo == TipoToken.ID){
            match(TipoToken.ID);
            System.out.println(Frase);
            T2();
            System.out.println(Frase);
            returner(2);
            Frase.add("T1");
        } else if (preanalisis.tipo == TipoToken.IDENTIFICADOR) {
            match(TipoToken.IDENTIFICADOR);
            System.out.println(Frase);
            T2();
            System.out.println(Frase);
            returner(2);
            Frase.add("T1");
        } else if (preanalisis.tipo == TipoToken.COMA) {
            match(TipoToken.COMA);
            System.out.println(Frase);
            T();
            System.out.println(Frase);
            returner(2);
            Frase.add("T");
        } else {
            hayErrores=true;
            System.out.println("Se esperaba id.");
        }
    }
    //T2->id | Ɛ
    private void T2(){
        if (hayErrores)
            return;
        if (preanalisis.tipo == TipoToken.ID){
            match(TipoToken.ID);
            System.out.println(Frase);
            returner(1);
            Frase.add("T2");
        }
        else if (preanalisis.tipo == TipoToken.IDENTIFICADOR){
            match(TipoToken.IDENTIFICADOR);
            System.out.println(Frase);
            returner(1);
            Frase.add("T2");
        } else {
            Frase.add("Ɛ");
            System.out.println(Frase);
            returner(1);
            Frase.add("T2");
        }
    }
    private void match(TipoToken tt){
        if(preanalisis.tipo == tt){
            Frase.add(preanalisis.lexema);
            i++;
            preanalisis = tokens.get(i);
        }
        else{
            hayErrores = true;
            System.out.println(preanalisis.tipo + ": No coincide");
        }

    }
    private void returner(int c){
        int g;
        for(g=0;g<c;g++){
        Frase.remove(Frase.size()-1);
        }

    }
}
