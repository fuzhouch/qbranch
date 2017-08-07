grammar BondGrammar;

@header {
    package net.dummmydigit.qbranch.compiler.grammar;
}

structBody : '{' '}' ;
genericStructDef : '<' ID '>' structBody
                 | structBody ;
structDef : STRUCT_TYPE_KEYWORD ID genericStructDef ;


SIGNED_INTEGER_TYPE_KEYWORD : 'int8' | 'int16' | 'int32' | 'int64' ;
UNSIGNED_INTEGER_TYPE_KEYWORD : 'uint8' | 'uint16' | 'uint32' | 'uint64' ;
FLOAT_POINT_TYPE_KEYWORD : 'float' | 'double' ;
CONTAINER_TYPE_KEYWORD : 'vector' | 'list' | 'map' ;
BYTESTRING_TYPE_KEYWORD : 'string' ;
WSTRING_TYPE_KEYWORD : 'wstring' ;

BUILTIN_TYPE_KEYWORD : SIGNED_INTEGER_TYPE_KEYWORD | UNSIGNED_INTEGER_TYPE_KEYWORD
                     | BYTESTRING_TYPE_KEYWORD | WSTRING_TYPE_KEYWORD
                     | FLOAT_POINT_TYPE_KEYWORD |
                     | CONTAINER_TYPE_KEYWORD
                     ;

STRUCT_TYPE_KEYWORD : 'struct' ;
ENUM_TYPE_KEYWORD : 'enum' ;

ID : [A-Za-z_][A-Za-z0-9_]* ;
WS : [ \t\r\n]+ -> skip ;