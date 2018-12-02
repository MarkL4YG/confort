//package de.mlessmann.confort.antlr
//Not packaged due to gradle/gradle#2565

parser grammar DeltaDescriptorParser;

options {
    tokenVocab = DeltaDescriptorLexer;
}

deltaDescriptor
  : commandBlock* EOF;

commandBlock
  : assignment
  | TAG_DESCRIPTOR command
  ;

command
  : metaCommand
  | descriptor
  ;

metaCommand
  : navigateTo
  ;

assignment
  : IDENTIFIER (OP_EQ|OP_WEAK_EQ) QUOTED_STRING ;

navigateTo
  : OP_NAVIGATE QUOTED_STRING? ;

descriptor
  : node_location reversableOP reversableArg?;

node_location
  : META_RELATIVE? QUOTED_STRING
  ;

reversableArg
  : generator
  | node_location
  ;

generator
  : IDENTIFIER
  ;

reversableOP
  : OP_MOVE
  | OP_MERGE_APPEND
  | OP_MERGE_PREPEND
  | OP_DROP
  | OP_GENERATE
  ;
