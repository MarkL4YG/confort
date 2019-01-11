/** Taken from "The Definitive ANTLR 4 Reference" by Terence Parr */

// Derived from http://json.org
parser grammar JSONParser;

options {
    tokenVocab = JSONLexer;
}

json
   : value
   ;

obj
   : CURLY_OPEN pair (COMMA pair)* CURLY_CLOSE
   | CURLY_OPEN CURLY_CLOSE
   ;

pair
   : STRING COLON value
   ;

array
   : SQUARE_OPEN value (COMMA value)* SQUARE_CLOSE
   | SQUARE_OPEN SQUARE_CLOSE
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