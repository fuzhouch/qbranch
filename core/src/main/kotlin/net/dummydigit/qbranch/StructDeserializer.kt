// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch

import net.dummydigit.qbranch.annotations.FieldId
import net.dummydigit.qbranch.generic.*
import net.dummydigit.qbranch.protocols.TaggedProtocolReader
import net.dummydigit.qbranch.utils.GenericTypeToken
import java.lang.reflect.Field
import java.util.*

internal class StructDeserializer(val cls : Class<*>,
                                  private val isBaseClass : Boolean) : DeserializerBase {
    private val fieldsByName = HashMap<String, Field>()
    private val creatorFieldsByName = HashMap<String, Field>()
    private val declaredFieldDeserializerMap = buildDeclaredFieldDeserializer(cls)

    override fun deserialize(preCreatedObj: Any, reader: TaggedProtocolReader) {
        val stopSign = if (isBaseClass) {
            BondDataType.BT_STOP_BASE
        } else {
            BondDataType.BT_STOP
        }

        var fieldInfo = reader.parseNextField()
        while (fieldInfo.typeId != stopSign) {
            val fieldSetter = declaredFieldDeserializerMap[fieldInfo.fieldId]
            if (fieldSetter != null) {
                fieldSetter.set(preCreatedObj, reader)
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
    }

    private fun buildDeclaredFieldDeserializer(inputCls : Class<*>) : Map<Int, net.dummydigit.qbranch.StructFieldSetter> {
        val fieldDeserializerMap = HashMap<Int, net.dummydigit.qbranch.StructFieldSetter>()
        inputCls.declaredFields.forEach {
            fieldsByName[it.name] = it
            it.isAccessible = true
        }

        fieldsByName.forEach {
            val fieldIdAnnotation = it.value.getDeclaredAnnotation(FieldId::class.java)
            if (fieldIdAnnotation != null) {
                val creatorFieldName = "${it.key}_QTypeArg"
                val creatorField = fieldsByName[creatorFieldName]
                if (creatorField != null) {
                    creatorFieldsByName[it.key] = creatorField
                }
                val fieldId = fieldIdAnnotation.id
                val fieldSetter = createFieldSetter(it.value)
                fieldDeserializerMap[fieldId] = fieldSetter
            }
        }

        return fieldDeserializerMap
    }

    private fun createFieldSetter(field: Field) : net.dummydigit.qbranch.StructFieldSetter {
        val creator = creatorFieldsByName[field.name]
        if (creator != null) {
            // All containers and generic types go here.
            println("field: ${field.name} ${field.genericType.typeName}")
            buildDeserializer(creator.genericType.typeName)
            throw NotImplementedError()
        }

        return when (field.genericType) {
            Boolean::class.java -> net.dummydigit.qbranch.StructFieldSetter.Bool(field)
            Byte::class.java -> net.dummydigit.qbranch.StructFieldSetter.Int8(field)
            Short::class.java -> net.dummydigit.qbranch.StructFieldSetter.Int16(field)
            Int::class.java -> net.dummydigit.qbranch.StructFieldSetter.Int32(field)
            Long::class.java -> net.dummydigit.qbranch.StructFieldSetter.Int64(field)
            UnsignedByte::class.java -> net.dummydigit.qbranch.StructFieldSetter.UInt8(field)
            UnsignedShort::class.java -> net.dummydigit.qbranch.StructFieldSetter.UInt16(field)
            UnsignedInt::class.java -> net.dummydigit.qbranch.StructFieldSetter.UInt32(field)
            UnsignedLong::class.java -> net.dummydigit.qbranch.StructFieldSetter.UInt64(field)
            ByteString::class.java -> net.dummydigit.qbranch.StructFieldSetter.ByteString(field)
            String::class.java -> net.dummydigit.qbranch.StructFieldSetter.UTF16LEString(field)
            Float::class.java -> net.dummydigit.qbranch.StructFieldSetter.Float(field)
            Double::class.java -> net.dummydigit.qbranch.StructFieldSetter.Double(field)
            else -> {
                val fieldDeserializer = net.dummydigit.qbranch.StructDeserializer(field.type, false)
                net.dummydigit.qbranch.StructFieldSetter.Struct(field, fieldDeserializer)
            }
        }
    }

    private fun buildDeserializer(typeName : String) : DeserializerBase {
        val tokens = GenericTypeToken.parseTypeName(typeName)
        val typeArgDeserializer = buildTypeArgDeserializer(tokens.name)
        if (typeArgDeserializer != null) {
            return typeArgDeserializer
        }
        val typeClass = Class.forName(tokens.name)
        return when (typeClass) {
            VectorT::class.java -> {
                val elementTypeName = tokens.typeArguments[0].name
                val elementDeserializer = buildDeserializer(elementTypeName)
                VectorDeserializer(elementDeserializer)
            }
            SetT::class.java -> {
                val elementTypeName = tokens.typeArguments[0].name
                val elementDeserializer = buildDeserializer(elementTypeName)
                SetDeserializer(elementDeserializer)
            }
            ListT::class.java -> {
                val elementTypeName = tokens.typeArguments[0].name
                val elementDeserializer = buildDeserializer(elementTypeName)
                ListDeserializer(elementDeserializer)
            }
            MapT::class.java -> {
                val keyTypeName = tokens.typeArguments[0].name
                val valueTypeName = tokens.typeArguments[1].name
                val keyDeserializer = buildDeserializer(keyTypeName)
                val valueDeserializer = buildDeserializer(valueTypeName)
                MapDeserializer(keyDeserializer, valueDeserializer)
            }
            StructT::class.java -> {
                val structTypeName = tokens.typeArguments[0].name
                StructDeserializer(Class.forName(structTypeName), isBaseClass = false)
            }
            BuiltinQTypeArg.BoolT::class.java -> BuiltinTypeDeserializer.BoolDeserializer()
            BuiltinQTypeArg.Int8T::class.java -> BuiltinTypeDeserializer.Int8Deserializer()
            BuiltinQTypeArg.Int16T::class.java -> BuiltinTypeDeserializer.Int16Deserializer()
            BuiltinQTypeArg.Int32T::class.java -> BuiltinTypeDeserializer.Int32Deserializer()
            BuiltinQTypeArg.Int64T::class.java -> BuiltinTypeDeserializer.Int64Deserializer()
            BuiltinQTypeArg.UInt8T::class.java -> BuiltinTypeDeserializer.UInt8Deserializer()
            BuiltinQTypeArg.UInt16T::class.java -> BuiltinTypeDeserializer.UInt16Deserializer()
            BuiltinQTypeArg.UInt32T::class.java -> BuiltinTypeDeserializer.UInt32Deserializer()
            BuiltinQTypeArg.UInt64T::class.java -> BuiltinTypeDeserializer.UInt64Deserializer()
            BuiltinQTypeArg.FloatT::class.java -> BuiltinTypeDeserializer.FloatDeserializer()
            BuiltinQTypeArg.DoubleT::class.java -> BuiltinTypeDeserializer.DoubleDeserializer()
            BuiltinQTypeArg.ByteStringT::class.java -> BuiltinTypeDeserializer.ByteStringDeserializer()
            BuiltinQTypeArg.WStringT::class.java -> BuiltinTypeDeserializer.WStringDeserializer()
            BuiltinQTypeArg.BlobT::class.java -> {
                throw NotImplementedError()
            }
            else -> {
                throw NotImplementedError() // Supposed it should never reach here.
            }
        }
    }

    private fun buildTypeArgDeserializer(typeName : String) : DeserializerBase? {
        val typeArgName = "${typeName}_QTypeArg"
        if (fieldsByName.containsKey(typeArgName)) {

        } else {}
        return null
    }
}