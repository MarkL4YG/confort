/** Derived from the JSON grammar next to this one **/

lexer grammar HOCONLexer;


fragment ESC
   : '\\' (["\\/bfnrt] | UNICODE)
   ;


fragment UNICODE
   : 'u' HEX HEX HEX HEX
   ;


fragment HEX
   : [0-9a-fA-F]
   ;


fragment SAFECODEPOINT
   : ~ ["\\\u0000-\u001F]
   ;

fragment UNQUOT_STR
   : [a-zA-Z][a-zA-Z0-9_\-]+
   ;

NUMBER
   : '-'? INT ('.' [0-9] +)? EXP?
   ;


fragment INT
   : '0' | [1-9] [0-9]*
   ;

// no leading zeros

fragment EXP
   : [Ee] [+\-]? INT
   ;

// \- since - means "range" inside [...]

WS
   : [ \t] + -> skip
   ;

BREAK
   : '\n'+
   ;

EXTRA_NOT_A_NUMBER
  : '"__NaN' ('_d'|'_f') '"'
  | '__NaN' ('_d'|'_f')
  ;

EXTRA_POSITIVE_INFINITY
  : '"__Infinity' ('_d'|'_f') '"'
  | '__Infinity' ('_d'|'_f')
  ;

EXTRA_NEGATIVE_INFINITY
  : '"__-Infinity' ('_d'|'_f') '"'
  | '__-Infinity' ('_d'|'_f')
  ;

// Literals 'true', 'false', 'null' have to take precedence over being interpreted as unquoted strings.
LIT_TRUE
   : 'true'
   ;

LIT_FALSE
   : 'false'
   ;

LIT_NULL
   : 'null'
   ;

STRING
   : '"' (ESC | SAFECODEPOINT)* '"'
   | UNQUOT_STR
   ;

CURLY_OPEN
   : '{' BREAK*
   ;

CURLY_CLOSE
   : '}'
   ;

SQUARE_OPEN
   : '['
   ;

SQUARE_CLOSE
   : ']'
   ;

COMMA
   : ','
   ;

EQUALS
   : '='
   ;

COLON
   : ':'
   ;