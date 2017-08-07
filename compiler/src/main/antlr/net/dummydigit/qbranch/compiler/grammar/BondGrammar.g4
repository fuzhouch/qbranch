grammar BondGrammar;

@header {
package net.dummydigit.qbranch.compiler.grammar;
}

bondDefContent : namespaceDecl ;
// TODO: Should Bond body but put it later.
namespaceDecl : singleNamespaceDecl moreNamespaceDecl ;
singleNamespaceDecl : NAMESPACE_KEYWORD NAMESPACE_NAME ;
moreNamespaceDecl : NEWLINE namespaceDecl
                  | NEWLINE
                  ;

structBody : '{' '}' ;
genericStructDef : '<' ID '>' structBody
                 | structBody ;
structDef : STRUCT_TYPE_KEYWORD ID genericStructDef ;

NAMESPACE_KEYWORD : 'namespace' ;
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
NAMESPACE_NAME : ID '.' ID ;
WS : [ \t]+ -> skip ;
NEWLINE : [\r\n]+ ;