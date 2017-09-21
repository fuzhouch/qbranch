// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.impl

import net.dummydigit.qbranch.*
import net.dummydigit.qbranch.annotations.FieldId
import net.dummydigit.qbranch.protocols.TaggedProtocolReader
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

    private fun buildDeclaredFieldDeserializer(inputCls : Class<*>) : Map<Int, StructFieldSetter> {
        val fieldDeserializerMap = HashMap<Int, StructFieldSetter>()
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
                val fieldSetter = createFieldSetter(it.value, fieldId)
                fieldDeserializerMap[fieldId] = fieldSetter
            }
        }

        return fieldDeserializerMap
    }

    private fun createFieldSetter(field: Field, id : Int) : StructFieldSetter {

        val creator = creatorFieldsByName[field.name]
        if (creator != null) {
            // All containers and generic types go here.
            println("${field.name}, ${field.genericType}, ${field.type}")
            throw NotImplementedError()
        }

        return when (field.genericType) {
            Boolean::class.java -> StructFieldSetter.Bool(field)
            Byte::class.java -> StructFieldSetter.Int8(field)
            Short::class.java -> StructFieldSetter.Int16(field)
            Int::class.java -> StructFieldSetter.Int32(field)
            Long::class.java -> StructFieldSetter.Int64(field)
            UnsignedByte::class.java -> StructFieldSetter.UInt8(field)
            UnsignedShort::class.java -> StructFieldSetter.UInt16(field)
            UnsignedInt::class.java -> StructFieldSetter.UInt32(field)
            UnsignedLong::class.java -> StructFieldSetter.UInt64(field)
            ByteString::class.java -> StructFieldSetter.ByteString(field)
            String::class.java -> StructFieldSetter.UTF16LEString(field)
            Float::class.java -> StructFieldSetter.Float(field)
            Double::class.java -> StructFieldSetter.Double(field)
            else -> {
                val fieldDeserializer = StructDeserializer(field.type, false)
                StructFieldSetter.Struct(field, fieldDeserializer)
            }
        }
    }
}