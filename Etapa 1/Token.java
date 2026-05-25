// Clase que contiene todos los atributos y lógica de los tokens 

public class Token {

    /* Estos cuatro atributos representan: 
        tipo: el tipo del token, de los definidos en TokenConstants
        lexema: es el valor del token, esto es, la cadena leída por el Lexer
        fila: es la fila donde se encuentra el token
        columna: es la columna donde se encuentra el token 
    */
    private TokenConstants tipo;
    private String lexema;
    private Integer fila; 
    private Integer columna;
    public Token(TokenConstants tipo, String lexema, Integer fila, Integer columna){
        this.tipo = tipo;
        this.lexema = lexema;
        this.fila = fila;
        this.columna = columna;
    } 

    /* Métodos de la clase Token: 
        *El método toString devuelve un String con el Token correspondiente al elemento que 
        leyó el lexer según la especificación del proyecto.
        *Los métodos tipo, lexema y fila, retornan estos valores de la clase respectivamente. 
        En el Main.java se necesita esta informacion, pero como los atributos de la clase 
        son privados, no podemos acceder a ellos desde el Main. Entonces se crearon estos métodos. 
    */
    @Override
    public String toString(){
        String ubicacion = " "+fila+" "+ columna + "";
        // Esta serie de ifs controlan que tipo de token es recibido, definiendo su comportamiento
        if (this.tipo == TokenConstants.TkNum || this.tipo == TokenConstants.TkCaracter){
            return ""+tipo + "(" + lexema + ")" + ubicacion;
        } else if (this.tipo == TokenConstants.TkIdent){
            return ""+tipo + "(\"" + lexema + "\")" + ubicacion;
        } else if (
            this.tipo == TokenConstants.TkTrue || 
            this.tipo == TokenConstants.TkFalse ||
            this.tipo == TokenConstants.TkComa||
            this.tipo == TokenConstants.TkPunto||
            this.tipo == TokenConstants.TkDosPuntos||
            this.tipo == TokenConstants.TkParAbre||
            this.tipo == TokenConstants.TkParCierra||
            this.tipo == TokenConstants.TkSuma||
            this.tipo == TokenConstants.TkResta||
            this.tipo == TokenConstants.TkMult||
            this.tipo == TokenConstants.TkDiv||
            this.tipo == TokenConstants.TkMod||
            this.tipo == TokenConstants.TkConjuncion||
            this.tipo == TokenConstants.TkNegacion||
            this.tipo == TokenConstants.TkDisyuncion||
            this.tipo == TokenConstants.TkMayor||
            this.tipo == TokenConstants.TkMenor||
            this.tipo == TokenConstants.TkMayorIgual||
            this.tipo == TokenConstants.TkMenorIgual||
            this.tipo == TokenConstants.TkIgual ||
            this.tipo == TokenConstants.TkWhile ||
            this.tipo == TokenConstants.TkBool||
            this.tipo == TokenConstants.TkIf ||
            this.tipo == TokenConstants.TkElse||
            this.tipo == TokenConstants.TkCreate||
            this.tipo == TokenConstants.TkEnd||
            this.tipo == TokenConstants.TkStore||
            this.tipo == TokenConstants.TkExecute ||
            this.tipo == TokenConstants.TkError ||
            this.tipo == TokenConstants.TkBot ||
            this.tipo == TokenConstants.TkActivate||
            this.tipo == TokenConstants.TkExecute
            )  {
            return ""+tipo+ ubicacion;
        } 
        return ""+tipo;
    }


    public TokenConstants tipo(){
        return this.tipo;
    }

    public String lexema(){
        return this.lexema;
    }

    public Integer fila(){
        return this.fila;
    }

    public Integer columna(){
        return this.columna;
    }
}
