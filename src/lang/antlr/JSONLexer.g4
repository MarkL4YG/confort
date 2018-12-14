lexer grammar JSONLexer;


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
   : [ \t\n\r] + -> skip
   ;

STRING
   : '"' (ESC | SAFECODEPOINT)* '"'
   ;

CURLY_OPEN
   : '{'
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

COLON
   : ':'
   ;

LIT_TRUE
   : 'true'
   ;

LIT_FALSE
   : 'false'
   ;

LIT_NULL
   : 'null'
   ;