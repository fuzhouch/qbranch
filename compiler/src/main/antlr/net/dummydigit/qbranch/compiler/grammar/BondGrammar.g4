grammar BondGrammar;

@header {
package net.dummydigit.qbranch.compiler.grammar;
}

bondDef : importDecl namespaceDecl bondTypeDef;
namespaceDecl : singleNamespaceDecl moreNamespaceDecl ;
singleNamespaceDecl : NAMESPACE_KEYWORD NAMESPACE_NAME ;
moreNamespaceDecl : NEWLINE namespaceDecl
                  | NEWLINE
                  ;
importDecl : IMPORT_KEYWORD '"' IMPORT_PATH '"' moreImportDecl;
moreImportDecl : NEWLINE importDecl
               | NEWLINE
               ;

bondTypeDef : structDef bondTypeDef
            | enumDef bondTypeDef
            ;

structDef : structNameDef structBodyDef ;
structNameDef : STRUCT_TYPE_KEYWORD ID ;
structBodyDef : structGenericTypeArgsDecl structBody
              | structBody
              ;
structGenericTypeArgsDecl : '<' typeArgsDeclList '>' ;
typeArgsDeclList : ID moreTypeArgsDeclList;
moreTypeArgsDeclList : ',' ID moreTypeArgsDeclList
                     |
                     ;

structBody : '{' '}' ; // TODO: Incomplete

enumDef : 'enum' ; // TODO: Incomplete


NAMESPACE_KEYWORD : 'namespace' ;
SIGNED_INTEGER_TYPE_KEYWORD : 'int8' | 'int16' | 'int32' | 'int64' ;
UNSIGNED_INTEGER_TYPE_KEYWORD : 'uint8' | 'uint16' | 'uint32' | 'uint64' ;
FLOAT_POINT_TYPE_KEYWORD : 'float' | 'double' ;
CONTAINER_TYPE_KEYWORD : 'vector' | 'list' | 'map' ;
BYTESTRING_TYPE_KEYWORD : 'string' ;
WSTRING_TYPE_KEYWORD : 'wstring' ;
IMPORT_KEYWORD : 'import' ;
IMPORT_PATH : [A-Za-z.\\/][A-Za-z.\\/]* ;
NAMESPACE_DELIMITER : '.' ;
STRUCT_TYPE_KEYWORD : 'struct' ;
ENUM_TYPE_KEYWORD : 'enum' ;

NAMESPACE_NAME : [A-Za-z_][A-Za-z0-9_]*(NAMESPACE_DELIMITER [A-Za-z_][A-Za-z0-9_]*)* ;
ID : [A-Za-z_][A-Za-z0-9_]* ;
WS : [ \t]+ -> skip ;
NEWLINE : [\r\n]+ ;