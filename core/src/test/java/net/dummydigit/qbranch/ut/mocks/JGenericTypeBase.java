package net.dummydigit.qbranch.ut.mocks;

import net.dummydigit.qbranch.QBranchSerializable;
import net.dummydigit.qbranch.annotations.FieldId;
import net.dummydigit.qbranch.annotations.QBranchGeneratedCode;
import net.dummydigit.qbranch.generic.QTypeArg;
import net.dummydigit.qbranch.generic.StructT;

@QBranchGeneratedCode(compilerName = "mock", version = "version.mock")
public class JGenericTypeBase<T1, T2> implements QBranchSerializable {

    public JGenericTypeBase(JGenericTypeBaseT<T1, T2> qTypeArgs) {
        baseFieldT1 = qTypeArgs.getBaseFieldT1T().newInstance();
        baseFieldT2 = qTypeArgs.getBaseFieldT2T().newInstance();
    }

    private @FieldId(id = 0) T1 baseFieldT1;
    private T2 baseFieldT2;
    public T1 getBaseFieldT1() { return baseFieldT1; }
    public T2 getBaseFieldT2() { return baseFieldT2; }
    public void setBaseFieldT1(T1 newValue) { baseFieldT1 = newValue; }
    public void setBaseFieldT2(T2 newValue) { baseFieldT2 = newValue; }

    // =======================================================================
    // For codegen
    // =======================================================================

    private JGenericTypeBase() { }

    public static class JGenericTypeBaseT<T1, T2> extends StructT<JGenericTypeBase<T1, T2>> {

        private JGenericTypeBaseT(QTypeArg<T1> qTypeArgT1, QTypeArg<T2> qTypeArgT2,
                                  Class<JGenericTypeBase<T1, T2>> cls) {
            this.baseFieldT1T = qTypeArgT1;
            this.baseFieldT2T = qTypeArgT2;
            this.cls = cls;
        }

        @Override
        public StructT<?> getBaseClassT () {
            return null;
        }

        @Override
        public JGenericTypeBase<T1, T2> newInstance() {
            return new JGenericTypeBase<>(this);
        }

        @Override
        public Class<JGenericTypeBase<T1, T2>> getGenericType() {
            return cls;
        }

        private QTypeArg<T1> baseFieldT1T;
        private QTypeArg<T2> baseFieldT2T;
        private Class<JGenericTypeBase<T1, T2>> cls;

        public QTypeArg<T1> getBaseFieldT1T() {
            return baseFieldT1T;
        }

        public QTypeArg<T2> getBaseFieldT2T() {
            return baseFieldT2T;
        }
    }

    @SuppressWarnings("unchecked")
    public static<T1, T2> JGenericTypeBaseT asQTypeArg(QTypeArg<T1> qTypeArgT1, QTypeArg<T2> qTypeArgT2) {
        JGenericTypeBase<T1, T2> refObj = new JGenericTypeBase<>();
        return new JGenericTypeBaseT<>(qTypeArgT1, qTypeArgT2, (Class<JGenericTypeBase<T1, T2>>)refObj.getClass());
    }
}

