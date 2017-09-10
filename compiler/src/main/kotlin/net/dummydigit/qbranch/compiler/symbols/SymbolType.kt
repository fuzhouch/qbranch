// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.compiler.symbols

enum class SymbolType {
    Unknown,
    Any,
    Attribute,
    Import,
    Namespace,
    Enum,
    Struct,
    StructField,
    GenericTypeParam,
    BuiltinType,
    BuiltinContainer,
    BuiltinKvpContainer,
    EnumSymbol, // Internal only
    StructBase, // Internal only
    TypeRef, // Internal only
}