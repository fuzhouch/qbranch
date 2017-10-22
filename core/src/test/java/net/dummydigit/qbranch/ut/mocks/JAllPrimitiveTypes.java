package net.dummydigit.qbranch.ut.mocks;

import net.dummydigit.qbranch.QBranchSerializable;
import net.dummydigit.qbranch.annotations.QBranchGeneratedCode;
import net.dummydigit.qbranch.generic.StructT;

@QBranchGeneratedCode(compilerName = "gbc", version = "version.mock")
public class JAllPrimitiveTypes implements QBranchSerializable {
    private int fieldInt = 0;

    public int getFieldInt() { return fieldInt; }
    public void setFieldInt(int newVal) { fieldInt = newVal; }

    public static class JAllPrimitiveTypesT extends StructT<JAllPrimitiveTypes> {
        public JAllPrimitiveTypesT() {}

        @Override
        public JAllPrimitiveTypes newInstance() {
            return new JAllPrimitiveTypes();
        }

        @Override
        public StructT<?> getBaseClassT() {
            return null;
        }

        @Override
        public Class<JAllPrimitiveTypes> getGenericType() {
            return JAllPrimitiveTypes.class;
        }
    }

    public static JAllPrimitiveTypesT asQTypeArg() {
        return new JAllPrimitiveTypesT();
    }
}
