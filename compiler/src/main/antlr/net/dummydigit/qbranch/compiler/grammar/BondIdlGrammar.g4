// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

grammar BondIdlGrammar;

bondIdl : namespaceDecl bondTypeDef
        | importDecl* namespaceDecl bondTypeDef
        ;

importDecl : IMPORT_KEYWORD QUOTED_STRING ;
namespaceDecl : NAMESPACE_KEYWORD namespaceName STMT_END?;
namespaceName : MULTI_SECTION_IDENTIFIER | IDENTIFIER ;

// Struct and enum definition
bondTypeDef : structDef bondTypeDef
            | enumDef bondTypeDef
            | typeAliasDef bondTypeDef
            | EOF
            ;

// ====================================================================
// type alias
// ====================================================================
typeAliasDef : TYPE_ALIAS_KEYWORD structNameDeclWithGeneric '=' typeNameWithGeneric STMT_END? ;

// ====================================================================
// Struct type
// ====================================================================
structDef : attributeDefList STRUCT_TYPE_KEYWORD structNameDeclWithGeneric structBaseClassDef? structBody STMT_END?
          | STRUCT_TYPE_KEYWORD structNameDeclWithGeneric structOrViewDef STMT_END?
          ;

structOrViewDef : structBaseClassDef? structBody?
                   | VIEW_OF_KEYWORD typeNameWithGeneric viewOfBody
                   ;

viewOfBody : BLOCK_BODY_BEGIN singleViewOfField STMT_END? BLOCK_BODY_END ;
singleViewOfField : IDENTIFIER ;

structBaseClassDef : FIELD_TYPENAME_DELIMITER typeNameWithGeneric ;

structBody : BLOCK_BODY_BEGIN structFieldDefList BLOCK_BODY_END ;
structFieldDefList : singleStructFieldDef* ;
singleStructFieldDef : attributeDefList fieldID fieldModifier? fieldType fieldName defaultValueSpec? STMT_END? ;

fieldID :  integerLiteral FIELD_TYPENAME_DELIMITER ;
fieldModifier : REQUIRED_KEYWORD
              | OPTIONAL_KEYWORD
              | REQUIRED_OPTIONAL_KEYWORD
              ;
fieldType : typeNameWithGeneric ;
fieldName : NOTHING_KEYWORD | IDENTIFIER ; // A special case in bond.bond.
defaultValueSpec : '=' defaultValues ;
defaultValues : HEX_NUMBER | DEC_NUMBER | FLOAT_NUMBER | NOTHING_KEYWORD | IDENTIFIER | QUOTED_STRING ;

// ====================================================================
// Two different generic type argument lists
// ====================================================================
typeNameWithGeneric : builtinPrimitiveType
                    | builtinContainerType genericTypeArgs
                    | customType genericTypeArgs?
                    ;

builtinPrimitiveType : SIGNED_INTEGER_TYPE_KEYWORD
                     | UNSIGNED_INTEGER_TYPE_KEYWORD
                     | FLOAT_POINT_TYPE_KEYWORD
                     | BYTESTRING_TYPE_KEYWORD
                     | WSTRING_TYPE_KEYWORD
                     ;
builtinContainerType : CONTAINER_TYPE_KEYWORD ;
customType : IDENTIFIER
           | MULTI_SECTION_IDENTIFIER
           ;

genericTypeArgs : GENERIC_TYPELIST_BEGIN typeArgsList GENERIC_TYPELIST_END ;
typeArgsList : typeArgName moreTypeArgsList* ;
moreTypeArgsList : DEFINITION_DELIMITER typeArgName;
typeArgName : typeNameWithGeneric ;

// StructFieldType arguments used when declaring a new struct.
// Unlike typeNameWithGeneric, it does not allow built-in type
// existing in type argument list, for example:
//
//     struct MyVector<int32> // invalid
//     struct MyVector<T> // valid
structNameDeclWithGeneric : typeDeclName genericTypeArgsDecl? ;
typeDeclName : IDENTIFIER ;
genericTypeArgsDecl : GENERIC_TYPELIST_BEGIN typeParamList GENERIC_TYPELIST_END ;
typeParamList : typeParamDef moreTypeParamList* ;
moreTypeParamList : DEFINITION_DELIMITER typeParamDef ;
typeParamDef : typeParamName typeParamValueConstraint? ;
typeParamName : IDENTIFIER ;
typeParamValueConstraint : VALUE_CONSTRAINT_STMT ;

// ====================================================================
// AttributeDef
// ====================================================================
attributeDefList : attributeDef* ;
attributeDef : ATTRIBUTE_BEGIN attributeBody ATTRIBUTE_END ;
attributeBody : IDENTIFIER ATTRIBUTE_PARAM_BEGIN QUOTED_STRING ATTRIBUTE_PARAM_END ;

// ====================================================================
// Enum
// ====================================================================
enumDef : ENUM_TYPE_KEYWORD enumName enumBody STMT_END?;
enumName : IDENTIFIER ;
enumBody : BLOCK_BODY_BEGIN enumSymbolDefList BLOCK_BODY_END ;
enumSymbolDefList : singleEnumSymbolDef moreEnumSymbolDef* ;
moreEnumSymbolDef : DEFINITION_DELIMITER enumSymbolDefList? ;
singleEnumSymbolDef : enumSymbol enumSymbolValueAssignment? ;
enumSymbol : IDENTIFIER ;
enumSymbolValueAssignment : '=' enumSymbolValue ;
enumSymbolValue : integerLiteral ;

integerLiteral : HEX_NUMBER | DEC_NUMBER ;

VALUE_CONSTRAINT_STMT : ':'WS+'value' ;
TYPE_ALIAS_KEYWORD : 'using'WS+ ;
VIEW_OF_KEYWORD : WS+'view_of'WS+ ;
NAMESPACE_KEYWORD : 'namespace'WS+ ;
STRUCT_TYPE_KEYWORD : 'struct'WS+ ;
ENUM_TYPE_KEYWORD : 'enum'WS+ ;
SIGNED_INTEGER_TYPE_KEYWORD : 'int8' | 'int16' | 'int32' | 'int64' ;
UNSIGNED_INTEGER_TYPE_KEYWORD : 'uint8' | 'uint16' | 'uint32' | 'uint64' ;
FLOAT_POINT_TYPE_KEYWORD : 'float' | 'double' ;
CONTAINER_TYPE_KEYWORD : 'vector' | 'list' | 'map' | 'set' | 'nullable' ;
BYTESTRING_TYPE_KEYWORD : 'string' ;
WSTRING_TYPE_KEYWORD : 'wstring' ;
NON_CONTAINER_TYPE_KEYWORD : SIGNED_INTEGER_TYPE_KEYWORD
                           | UNSIGNED_INTEGER_TYPE_KEYWORD
                           | FLOAT_POINT_TYPE_KEYWORD
                           | BYTESTRING_TYPE_KEYWORD
                           | WSTRING_TYPE_KEYWORD
                           ;
BUILTIN_TYPE_KEYWORD : NON_CONTAINER_TYPE_KEYWORD
                     | CONTAINER_TYPE_KEYWORD
                     ;
NOTHING_KEYWORD : 'nothing' ;
REQUIRED_KEYWORD : 'required' ;
OPTIONAL_KEYWORD : 'optional' ;
REQUIRED_OPTIONAL_KEYWORD : 'required_optional' ;
IMPORT_KEYWORD : 'import'WS+ ;
DEC_NUMBER : ('-'|'+')? DIGIT+ ;
FLOAT_NUMBER : ('-'|'+')? DIGIT+ FLOAT_HALF? 'f'? ;
FLOAT_HALF : '.'DIGIT+ ;
HEX_NUMBER : '0' ('x'|'X') (DIGIT|'A'..'F'|'a'..'f')+ ;
MULTI_SECTION_IDENTIFIER : IDENTIFIER('.'IDENTIFIER)+ ;
IDENTIFIER : NON_DIGIT ( NON_DIGIT | DIGIT )* ;
QUOTED_STRING : '"' (~["\r\n] | '\\''"')* '"' ;
NON_DIGIT : [A-Za-z_] ;
DIGIT : [0-9] ;
BLOCK_BODY_BEGIN : '{' ;
BLOCK_BODY_END : '}' ;
STMT_END : ';' ;
FIELD_TYPENAME_DELIMITER : ':' ;
DEFINITION_DELIMITER : ',' ;
GENERIC_TYPELIST_BEGIN : '<' ;
GENERIC_TYPELIST_END : '>' ;
NAMESPACE_DELIMITER : '.' ;
ATTRIBUTE_BEGIN : '[' ;
ATTRIBUTE_END : ']' ;
ATTRIBUTE_PARAM_BEGIN : '(' ;
ATTRIBUTE_PARAM_END : ')' ;
WS : [ \t]+ -> skip ;
NEWLINE : [\r\n]+ -> skip ;
MULTI_LINE_COMMENT : '/*' .*? '*/' -> skip ;
LINE_COMMENT : '//' ~ ('\n' | '\r') * '\r'? '\n' -> skip ;