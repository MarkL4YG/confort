/** Derived from the JSON grammar next to this one **/

parser grammar HOCONParser;

options {
    tokenVocab = HOCONLexer;
}

hocon
   : value
   ;

obj
   : CURLY_OPEN pair ((BREAK) pair)* CURLY_CLOSE
   | CURLY_OPEN BREAK* CURLY_CLOSE
   ;

pair
    // normal syntax: '<key> = <value>'
   : STRING (EQUALS | COLON) value
    // object shorthand: '<key> { ...'
   | STRING obj
   ;

array
   : SQUARE_OPEN (value (COMMA | BREAK)?)* SQUARE_CLOSE
   ;

value
   : STRING
   | NUMBER
   | obj
   | array
   | LIT_TRUE
   | LIT_FALSE
   | LIT_NULL
   | EXTRA_NOT_A_NUMBER
   | EXTRA_POSITIVE_INFINITY
   | EXTRA_NEGATIVE_INFINITY
   ;