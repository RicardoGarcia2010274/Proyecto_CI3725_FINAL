import java_cup.runtime.Symbol;

%%
%public
%class LexerCup
%cup
%line
%column

digit = [0-9]
letter = [a-zA-Z]
whitespace = [ \t\r\n]
coments = "$"~"$"
comentsline = "$$"~[\n]
saltolinea = "\\n"
tabulador = "\\t"
comillasimple = "\\\'"
comilla ="'"
underscore = "_"

%{
    private Symbol symbol(int type) {
        return new Symbol(type, yyline + 1, yycolumn + 1);
    }
    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline + 1, yycolumn + 1, value);
    }  
%}

%eofval{
    return new Symbol(ParserSym.EOF);
%eofval}

%%
"create" { return symbol(ParserSym.TkCreate); }
"default" { return symbol(ParserSym.TkDefault); }
"as" { return symbol(ParserSym.TkAs); }
"me" { return symbol(ParserSym.TkMe); }
"read" { return symbol(ParserSym.TkRead); }
"left" { return symbol(ParserSym.TkLeft); }
"right" { return symbol(ParserSym.TkRight); }
"up" { return symbol(ParserSym.TkUp); }
"down" { return symbol(ParserSym.TkDown); }
"on" { return symbol(ParserSym.TkOn); }
"char" { return symbol(ParserSym.TkChar); }
"int" { return symbol(ParserSym.TkInt); }
"collect" { return symbol(ParserSym.TkCollect); }
"drop" { return symbol(ParserSym.TkDrop); }
"send" { return symbol(ParserSym.TkSend); }
"advance" { return symbol(ParserSym.TkAdvance); }
"deactivate" { return symbol(ParserSym.TkDeactivate); }
"true" { return symbol(ParserSym.TkTrue); }
"false" { return symbol(ParserSym.TkFalse); }
"while" { return symbol(ParserSym.TkWhile); }
"bool" { return symbol(ParserSym.TkBool); }
"if" { return symbol(ParserSym.TkIf); }
"else" { return symbol(ParserSym.TkElse); }
"end" { return symbol(ParserSym.TkEnd); }
"store" { return symbol(ParserSym.TkStore); }
"bot" { return symbol(ParserSym.TkBot); }
"activate" { return symbol(ParserSym.TkActivate); }
"execute" { return symbol(ParserSym.TkExecute); }
"activation" { return symbol(ParserSym.TkActivation); }
"deactivation" { return symbol(ParserSym.TkDeactivation); }
"," { return symbol(ParserSym.TkComa); }
"." { return symbol(ParserSym.TkPunto); }
":" { return symbol(ParserSym.TkDosPuntos); }
"(" { return symbol(ParserSym.TkParAbre); }
")" { return symbol(ParserSym.TkParCierra); }
"+" { return symbol(ParserSym.TkSuma); }
"/=" { return symbol(ParserSym.TkDesigual); }
"=" { return symbol(ParserSym.TkIgual); }
">=" { return symbol(ParserSym.TkMayorIgual); }
">" { return symbol(ParserSym.TkMayor); }
"<=" { return symbol(ParserSym.TkMenorIgual); }
"<" { return symbol(ParserSym.TkMenor); }
"~" { return symbol(ParserSym.TkNegacion); }
"\\/" { return symbol(ParserSym.TkDisyuncion); }
"/\\" { return symbol(ParserSym.TkConjuncion); }
"-" { return symbol(ParserSym.TkResta); }
"%" { return symbol(ParserSym.TkMod); }
"/" { return symbol(ParserSym.TkDiv); }
"*" { return symbol(ParserSym.TkMult); }

{digit}+ { return symbol(ParserSym.TkNum, Integer.valueOf(yytext())); }
{letter}({letter}|{digit}|{underscore})* { return symbol(ParserSym.TkIdent, yytext()); }

{comilla}{letter}{comilla} { return symbol(ParserSym.TkCaracter, yytext()); }
{comilla}" "{comilla} { return symbol(ParserSym.TkCaracter, yytext()); }
{comilla}{saltolinea}{comilla} { return symbol(ParserSym.TkCaracter, yytext()); }
{comilla}{tabulador}{comilla} { return symbol(ParserSym.TkCaracter, yytext()); }
{comilla}{comillasimple}{comilla} { return symbol(ParserSym.TkCaracter, yytext()); }

{whitespace} {/* ignorar */}
{coments} {/* ignorar */}
{comentsline} {/* ignorar */}
[^] { return symbol(ParserSym.TkError, yytext()); }