package com.carrental.model;

public class InsuranceType {
    private int insuranceTypeId;
    private String typeName;
    private String coverage;

    public InsuranceType() {}
    public InsuranceType(int insuranceTypeId, String typeName, String coverage) {
        this.insuranceTypeId = insuranceTypeId;
        this.typeName = typeName;
        this.coverage = coverage;
    }

    public int getInsuranceTypeId()             { return insuranceTypeId; }
    public void setInsuranceTypeId(int v)       { this.insuranceTypeId = v; }
    public String getTypeName()                 { return typeName; }
    public void setTypeName(String v)           { this.typeName = v; }
    public String getCoverage()                 { return coverage; }
    public void setCoverage(String v)           { this.coverage = v; }

    @Override
    public String toString() {
        return String.format("InsuranceType[id=%d, name=%s, coverage=%s]", insuranceTypeId, typeName, coverage);
    }
}
