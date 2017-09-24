// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch

import net.dummydigit.qbranch.annotations.FieldId
import net.dummydigit.qbranch.exceptions.UnsupportedBondTypeException
import net.dummydigit.qbranch.generic.*
import net.dummydigit.qbranch.protocols.TaggedProtocolReader
import net.dummydigit.qbranch.utils.GenericTypeToken
import java.lang.reflect.Field
import java.util.*

internal class StructDeserializer(private val typeArg : QTypeArg<*>,
                                  private val isBaseClass : Boolean) : DeserializerBase {
    private val cls = typeArg.getGenericType()
    private val fieldsByName = HashMap<String, Field>()
    private val declaredFieldDeserializerMap = buildDeclaredFieldDeserializer(cls)
    private val inheritanceChainDeserializer = buildInheritanceChainDeserializer()

    init {
        ensureClsIsQBranchGeneratedStruct(cls)
    }

    override fun deserialize(reader: TaggedProtocolReader) : Any {
        val obj = cls.newInstance()
        inheritanceChainDeserializer.forEach {
            it.deserializeDeclaredFields(it.cls.cast(obj), reader)
        }
        return obj
    }

    private fun buildInheritanceChainDeserializer() : List<StructDeserializer> {
        val deserializers = arrayListOf<StructDeserializer>()
        var baseClass = cls.superclass!!
        while (baseClass != Object::class.java) {
            ensureClsIsQBranchGeneratedStruct(baseClass)
            deserializers.add(StructDeserializer(baseClass, true))
            baseClass = cls.superclass
        }
        return deserializers.reversed() // Start from base class
    }

    private fun ensureClsIsQBranchGeneratedStruct(cls : Class<*>) {
        if (!cls.isQBranchGeneratedStruct()) {
            throw UnsupportedBondTypeException(cls)
        }
    }

    private fun deserializeDeclaredFields(preCreatedObj: Any, reader: TaggedProtocolReader) : Any {
        val stopSign = if (isBaseClass) {
            BondDataType.BT_STOP_BASE
        } else {
            BondDataType.BT_STOP
        }

        var fieldInfo = reader.parseNextField()
        while (fieldInfo.typeId != stopSign) {
            val valueSetter = declaredFieldDeserializerMap[fieldInfo.fieldId]
            if (valueSetter != null) {
                valueSetter.set(preCreatedObj, reader)
            } else {
                // A field appears in encoded binary but unknown
                // to deserializer. There can be two cases:
                //
                // 1. The binary represents a struct with new version.
                // 2. We are using an incorrect schema.
                //
                // We surely won't be able to handle #2,  but we
                // should be able to safely handle #1 by skipping it.
                // As for #2, it will crash somewhere later.
                reader.skipField(fieldInfo.typeId)
            }
            fieldInfo = reader.parseNextField()
        }

        return preCreatedObj
    }

    private fun getCreatorFieldName(valueFieldName : String) : String = "${valueFieldName}_QTypeArg"

    private fun ensureValidCreatorField(field: Field) : Field {
        val fieldIdAnnotation = field.getDeclaredAnnotation(FieldId::class.java)
        if (fieldIdAnnotation != null) {
            throw UnsupportedBondTypeException(cls)
        }

        field.getDeclaredAnnotation(Transient::class.java) ?: throw UnsupportedBondTypeException(cls)
        return field
    }

    private fun buildDeclaredFieldDeserializer(inputCls : Class<*>) : Map<Int, ValueSetter> {
        val fieldDeserializerMap = HashMap<Int, ValueSetter>()
        inputCls.declaredFields.forEach {
            fieldsByName[it.name] = it
            it.isAccessible = true
        }

        fieldsByName.forEach {
            val fieldIdAnnotation = it.value.getDeclaredAnnotation(FieldId::class.java)
            if (fieldIdAnnotation != null) {
                val creatorFieldName = getCreatorFieldName(it.key)
                val creatorField = fieldsByName[creatorFieldName]
                // Builtin primitive types have special handling, as unboxed
                // types (byte, int, etc... can't call Class.forName())
                val deserializer = if (creatorField != null) {
                    // All types that can be some kinds of generic goes here.
                    ensureValidCreatorField(creatorField)
                    buildDeserializerByName(it.value.name, creatorField.genericType.typeName)
                } else {
                    // Enum and primitive types goes here.
                    buildDeserializerForPrimitiveTypes(it.value)
                }
                val fieldSetter = ValueSetter(it.value, deserializer)
                fieldDeserializerMap[fieldIdAnnotation.id] = fieldSetter
            } // Fields without FieldId() tag are for internal. Don't deserialize.
        }

        return fieldDeserializerMap
    }

    private fun buildDeserializerForPrimitiveTypes(field : Field) : DeserializerBase {
        return when (field.genericType) {
            Boolean::class.java -> BuiltinTypeDeserializer.Bool
            Byte::class.java -> BuiltinTypeDeserializer.Int8
            Short::class.java -> BuiltinTypeDeserializer.Int16
            Int::class.java -> BuiltinTypeDeserializer.Int32
            Long::class.java -> BuiltinTypeDeserializer.Int64
            UnsignedByte::class.java -> BuiltinTypeDeserializer.UInt8
            UnsignedShort::class.java -> BuiltinTypeDeserializer.UInt16
            UnsignedInt::class.java -> BuiltinTypeDeserializer.UInt32
            UnsignedLong::class.java -> BuiltinTypeDeserializer.UInt64
            Float::class.java -> BuiltinTypeDeserializer.Float
            Double::class.java -> BuiltinTypeDeserializer.Double
            ByteString::class.java -> BuiltinTypeDeserializer.ByteString
            String::class.java -> BuiltinTypeDeserializer.WString
            Blob::class.java -> {
                throw NotImplementedError()
            }
            else -> {
                throw NotImplementedError()
            }
        }
    }

    private fun buildDeserializerByName(fieldName : String, typeName : String) : DeserializerBase {
        val tokens = GenericTypeToken.parseTypeName(typeName)
        // TODO What happen if we hit a 'T'?

        println("tokens.name: " + tokens.name)
        val typeClass = Class.forName(tokens.name)
        return when (typeClass) {
            VectorT::class.java -> {
                val elementTypeName = tokens.typeArguments[0].name
                val elementDeserializer = buildDeserializerByName(fieldName, elementTypeName)
                VectorDeserializer(fieldName, elementDeserializer)
            }

            SetT::class.java -> {
                val elementTypeName = tokens.typeArguments[0].name
                val elementDeserializer = buildDeserializerByName(fieldName, elementTypeName)
                SetDeserializer(elementDeserializer)
            }

            ListT::class.java -> {
                val elementTypeName = tokens.typeArguments[0].name
                val elementDeserializer = buildDeserializerByName(fieldName, elementTypeName)
                ListDeserializer(elementDeserializer)
            }

            MapT::class.java -> {
                val keyTypeName = tokens.typeArguments[0].name
                val valueTypeName = tokens.typeArguments[1].name
                val keyDeserializer = buildDeserializerByName(fieldName, keyTypeName)
                val valueDeserializer = buildDeserializerByName(fieldName, valueTypeName)
                MapDeserializer(keyDeserializer, valueDeserializer)
            }

            StructT::class.java -> {
                val structTypeName = tokens.typeArguments[0].name
                StructDeserializer(Class.forName(structTypeName), isBaseClass = false)
            }

            BuiltinQTypeArg.BoolT::class.java -> BuiltinTypeDeserializer.Bool
            BuiltinQTypeArg.Int8T::class.java -> BuiltinTypeDeserializer.Int8
            BuiltinQTypeArg.Int16T::class.java -> BuiltinTypeDeserializer.Int16
            BuiltinQTypeArg.Int32T::class.java -> BuiltinTypeDeserializer.Int32
            BuiltinQTypeArg.Int64T::class.java -> BuiltinTypeDeserializer.Int64
            BuiltinQTypeArg.UInt8T::class.java -> BuiltinTypeDeserializer.UInt8
            BuiltinQTypeArg.UInt16T::class.java -> BuiltinTypeDeserializer.UInt16
            BuiltinQTypeArg.UInt32T::class.java -> BuiltinTypeDeserializer.UInt32
            BuiltinQTypeArg.UInt64T::class.java -> BuiltinTypeDeserializer.UInt64
            BuiltinQTypeArg.FloatT::class.java -> BuiltinTypeDeserializer.Float
            BuiltinQTypeArg.DoubleT::class.java -> BuiltinTypeDeserializer.Double
            BuiltinQTypeArg.ByteStringT::class.java -> BuiltinTypeDeserializer.ByteString
            BuiltinQTypeArg.WStringT::class.java -> BuiltinTypeDeserializer.WString
            BuiltinQTypeArg.BlobT::class.java -> {
                throw NotImplementedError()
            }

            // TODO: Enum: how can I create enum deserializer.
            else -> {
                throw NotImplementedError() // Supposed it should never reach here.
            }
        }
    }
}