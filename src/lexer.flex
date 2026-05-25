%%
%public
%class Lexer
%line
%column

digit = [0-9]
letter = [a-zA-Z]
whitespace = [ \t\r\n]
coments = "$"~"$"
saltolinea = "\\n"
tabulador = "\\t"
comillasimple = "\\\'"
comilla ="'"
True = true
False = false
create = "create"
coma = ","
punto = "."
dospuntos= ":" 
parabre = "("
parcierra = ")"
suma = "+"
resta = "-"
mult ="*"
div = "/"
mod ="%"
conjuncion ="/\\"
disyuncion ="\\/"
negacion ="~"
menor = "<"
menorigual = "<="
mayor = ">"
mayorigual = ">="
igual = "="
while = "while"
bool = "bool"
char = "char"
int = "int"
if = "if"
else = "else"
end = "end"
store = "store" 
receive = "receive"
bot = "bot"
activate = "activate"
execute = "execute"
on = "on"
send = "send"
advance = "advance"
deactivate = "deactivate"
drop = "drop"
collect = "collect"
left = "left"
right = "right"
up = "up"
down = "down"
read = "read"
as = "as"
default = "default"
me = "me"
underscore = "_"
desigual = "/="

%type Token

%eofval{
    return new Token(TokenConstants.TkFinal, yytext(),  yyline, yycolumn);
%eofval}
%%

{create} {return new Token(TokenConstants.TkCreate, yytext(), yyline + 1, yycolumn + 1);}
{desigual} {return new Token(TokenConstants.TkDesigual, yytext(), yyline + 1, yycolumn + 1);}
{default} {return new Token(TokenConstants.TkDefault, yytext(), yyline + 1, yycolumn + 1);}
{as} {return new Token(TokenConstants.TkAs, yytext(), yyline + 1, yycolumn + 1);}
{me} {return new Token(TokenConstants.TkMe, yytext(), yyline + 1, yycolumn + 1);}
{read} {return new Token(TokenConstants.TkRead, yytext(), yyline + 1, yycolumn + 1);}
{left} {return new Token(TokenConstants.TkLeft, yytext(), yyline + 1, yycolumn + 1);}
{right} {return new Token(TokenConstants.TkRight, yytext(), yyline + 1, yycolumn + 1);}
{up} {return new Token(TokenConstants.TkUp, yytext(), yyline + 1, yycolumn + 1);}
{down} {return new Token(TokenConstants.TkDown, yytext(), yyline + 1, yycolumn + 1);}
{on} {return new Token(TokenConstants.TkOn, yytext(), yyline + 1, yycolumn + 1);}
{char} {return new Token(TokenConstants.TkChar, yytext(), yyline + 1, yycolumn + 1);}
{int} {return new Token(TokenConstants.TkInt, yytext(), yyline + 1, yycolumn + 1);}
{receive} {return new Token(TokenConstants.TkReceive, yytext(), yyline + 1, yycolumn + 1);}
{collect} {return new Token(TokenConstants.TkCollect, yytext(), yyline + 1, yycolumn + 1);}
{drop} {return new Token(TokenConstants.TkDrop, yytext(), yyline + 1, yycolumn + 1);}
{send} {return new Token(TokenConstants.TkSend, yytext(), yyline + 1, yycolumn + 1);}
{advance} {return new Token(TokenConstants.TkAdvance, yytext(), yyline + 1, yycolumn + 1);}
{deactivate} {return new Token(TokenConstants.TkDeactivate, yytext(), yyline + 1, yycolumn + 1);}
{digit}+ {return new Token(TokenConstants.TkNum, yytext(), yyline + 1, yycolumn + 1);}
{True} {return new Token(TokenConstants.TkTrue, yytext(), yyline + 1, yycolumn + 1);}
{False} {return new Token(TokenConstants.TkFalse, yytext(), yyline + 1, yycolumn + 1);}
{comilla}{letter}{comilla} {return new Token(TokenConstants.TkCaracter, yytext(), yyline + 1, yycolumn + 1);}
{coma} {return new Token(TokenConstants.TkComa, yytext(), yyline + 1, yycolumn + 1);}
{punto} {return new Token(TokenConstants.TkPunto, yytext(),  yyline + 1, yycolumn + 1);}
{dospuntos} {return new Token(TokenConstants.TkDosPuntos, yytext(),  yyline + 1, yycolumn + 1);}
{parabre} {return new Token(TokenConstants.TkParAbre, yytext(),  yyline + 1, yycolumn + 1);}
{parcierra} {return new Token(TokenConstants.TkParCierra, yytext(),  yyline + 1, yycolumn + 1);}
{suma} {return new Token(TokenConstants.TkSuma, yytext(),  yyline + 1, yycolumn + 1);}
{resta} {return new Token(TokenConstants.TkResta, yytext(),  yyline + 1, yycolumn + 1);}
{mult} {return new Token(TokenConstants.TkMult, yytext(),  yyline + 1, yycolumn + 1);}
{div} {return new Token(TokenConstants.TkDiv, yytext(),  yyline + 1, yycolumn + 1);}
{mod} {return new Token(TokenConstants.TkMod, yytext(),  yyline + 1, yycolumn + 1);}
{disyuncion} {return new Token(TokenConstants.TkDisyuncion, yytext(),  yyline + 1, yycolumn + 1);}
{conjuncion} {return new Token(TokenConstants.TkConjuncion, yytext(),  yyline + 1, yycolumn + 1);}
{negacion} {return new Token(TokenConstants.TkNegacion, yytext(),  yyline + 1, yycolumn + 1);}
{menor} {return new Token(TokenConstants.TkMenor, yytext(),  yyline + 1, yycolumn + 1);}
{menorigual} {return new Token(TokenConstants.TkMenorIgual, yytext(),  yyline + 1, yycolumn + 1);}
{mayor} {return new Token(TokenConstants.TkMayor, yytext(),  yyline + 1, yycolumn + 1);}
{mayorigual} {return new Token(TokenConstants.TkMayorIgual, yytext(),  yyline + 1, yycolumn + 1);}
{igual} {return new Token(TokenConstants.TkIgual, yytext(),  yyline + 1, yycolumn + 1);}
{while} {return new Token(TokenConstants.TkWhile, yytext(),  yyline + 1, yycolumn + 1);}
{bool} {return new Token(TokenConstants.TkBool, yytext(),  yyline + 1, yycolumn + 1);}
{if} {return new Token(TokenConstants.TkIf, yytext(),  yyline + 1, yycolumn + 1);}
{else} {return new Token(TokenConstants.TkElse, yytext(),  yyline + 1, yycolumn + 1);}
{end} {return new Token(TokenConstants.TkEnd, yytext(),  yyline + 1, yycolumn + 1);}
{store} {return new Token(TokenConstants.TkStore, yytext(),  yyline + 1, yycolumn + 1);}
{bot} {return new Token(TokenConstants.TkBot, yytext(), yyline + 1, yycolumn + 1);}
{activate} {return new Token(TokenConstants.TkActivate, yytext(), yyline + 1, yycolumn + 1);}
{execute} {return new Token(TokenConstants.TkExecute, yytext(), yyline + 1, yycolumn + 1);}
{letter}({letter}|{digit}|{underscore})* { return new Token(TokenConstants.TkIdent, yytext(),  yyline + 1, yycolumn + 1);}

{whitespace} {/* ignorar */}
{coments} {/* ignorar */}
{comilla}{saltolinea}{comilla} {/* ignorar */}
{comilla}{tabulador}{comilla} {/* ignorar */}
{comilla}{comillasimple}{comilla} {/* ignorar */}
[^] { return new Token(TokenConstants.TkError, yytext(), yyline + 1, yycolumn + 1); }