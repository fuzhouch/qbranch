// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch

import bond.BondDataType
import java.lang.reflect.Field
import java.nio.charset.Charset
import java.util.*
import net.dummydigit.qbranch.annotations.FieldId
import net.dummydigit.qbranch.exceptions.UnsupportedBondTypeException
import net.dummydigit.qbranch.protocols.TaggedProtocolReader
import net.dummydigit.qbranch.types.isQBranchGeneratedStruct

/**
 * Implementation that return deserialized struct as Object.
 */
internal class StructDeserializer(inputCls : Class<*>, stringCharset : Charset) {
    var baseDeserializer : StructDeserializer? = null
    val declaredFieldDeserializerMap = TreeMap<Int /* fieldId */, StructFieldSetter>()
    val cls = inputCls
    val charset = stringCharset

    init {
        val parent = inputCls.superclass
        if (parent != Any::class.java) {
            baseDeserializer = StructDeserializer(parent, charset)
        }
        buildDeclaredFieldDeserializer(inputCls)
    }

    /**
     * Deserialize byte buffer to object.
     * @param preCreatedObj Pre-created object that holds deserialized values.
     * @param reader A tagged reader.
     * @param isBase Check if this is a base class.
     */
    fun deserialize(preCreatedObj: Any, reader: TaggedProtocolReader, isBase: Boolean): Any {
        val obj = cls.cast(preCreatedObj)
        baseDeserializer?.deserialize(obj, reader, true)
        deserializeDeclaredFields(obj, reader, isBase)
        return obj
    }

    private fun deserializeDeclaredFields(obj : Any, reader: TaggedProtocolReader, isBase: Boolean) : Unit {
        val casted = cls.cast(obj)
        val stopSign = if (isBase) { BondDataType.BT_STOP_BASE } else { BondDataType.BT_STOP }

        var fieldInfo = reader.parseNextField()
        while (fieldInfo.typeId != stopSign) {
            val fieldSetter = declaredFieldDeserializerMap[fieldInfo.fieldId]
            if (fieldSetter != null) {
                fieldSetter.set(casted, reader)
            } else {
                // A field appears in encoded binary but unknown
                // to deserializer. There can be two cases:
                //
                // 1. The binary represents a struct with new version.
                // 2. This is an invalid buffer.
                //
                // We surely won't be able to handle #1
                reader.skipField(fieldInfo.typeId)
            }
            fieldInfo = reader.parseNextField()
        }
    }

    private fun buildDeclaredFieldDeserializer(inputCls : Class<*>) : Unit {
        if (!inputCls.isQBranchGeneratedStruct()) {
            throw UnsupportedBondTypeException(inputCls)
        }

        // For each field, create its deserializer
        inputCls.declaredFields.forEach {
            val field = it
            val fieldId = it.getDeclaredAnnotation(FieldId::class.java).id
            field.isAccessible = true
            val bondTag = BondJavaTypeMapping.builtInTypeToBondTag[field.genericType]
            if (bondTag != null) {
                declaredFieldDeserializerMap[fieldId] = createFieldSetterByBondTag(bondTag, field)
            } else {
                throw UnsupportedBondTypeException(field.type)
            }
        }
    }

    private fun createFieldSetterByBondTag(bondTag: BondDataType, field: Field) : StructFieldSetter {
        return when (bondTag) {
            BondDataType.BT_BOOL -> BondTypeFieldSetter.Bool(field)
            BondDataType.BT_UINT8 -> BondTypeFieldSetter.UInt8(field)
            BondDataType.BT_UINT16 -> BondTypeFieldSetter.UInt16(field)
            BondDataType.BT_UINT32 -> BondTypeFieldSetter.UInt32(field)
            BondDataType.BT_UINT64 -> BondTypeFieldSetter.UInt64(field)
            BondDataType.BT_FLOAT -> BondTypeFieldSetter.Float(field)
            BondDataType.BT_DOUBLE -> BondTypeFieldSetter.Double(field)
            BondDataType.BT_STRING -> BondTypeFieldSetter.ByteString(field)
            BondDataType.BT_INT8 -> BondTypeFieldSetter.Int8(field)
            BondDataType.BT_INT16 -> BondTypeFieldSetter.Int16(field)
            BondDataType.BT_INT32 -> BondTypeFieldSetter.Int32(field)
            BondDataType.BT_INT64 -> BondTypeFieldSetter.Int64(field)
            BondDataType.BT_WSTRING -> BondTypeFieldSetter.UTF16LEString(field)
            // TODO: Container is more complicated than primitive types.
            // BondDataType.BT_LIST
            // BondDataType.BT_SET,
            // BondDataType.BT_MAP,
            else -> throw UnsupportedBondTypeException(field.type)
        }
    }
}