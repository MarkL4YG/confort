//package de.mlessmann.confort.antlr
//Not packaged due to gradle/gradle#2565

lexer grammar DeltaDescriptorLexer;

TAG_META : '_' ;
TAG_DESCRIPTOR : '!' ;

fragment BREAK : [\n\r]+ ;

// HIDDEN Tokens
COMMENT: '//' ~[\r\n]* -> channel(HIDDEN);
BLANK_LINE : BREAK ->channel(HIDDEN);
WHITESPACE : [ \t\r\n\u000C]+ -> channel(HIDDEN);

IDENTIFIER : [a-zA-Z_]+ ;
QUOTED_STRING : '"' .+? '"' ;

META_RELATIVE : '~' ;
OP_EQ : '=' ;
OP_WEAK_EQ : '?=' ;
OP_NAVIGATE : '>' ;
OP_MOVE : '==>' ;
OP_MERGE_APPEND : '==+>' ;
OP_MERGE_PREPEND : '+==>' ;
OP_DROP : '=/=' ;
OP_GENERATE : '==$' ;
