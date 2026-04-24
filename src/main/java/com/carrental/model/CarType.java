package com.carrental.model;

public class CarType {
    private int typeId;
    private String typeName;
    private String description;

    public CarType() {}

    public CarType(int typeId, String typeName, String description) {
        this.typeId = typeId;
        this.typeName = typeName;
        this.description = description;
    }

    public int getTypeId()              { return typeId; }
    public void setTypeId(int v)        { this.typeId = v; }
    public String getTypeName()         { return typeName; }
    public void setTypeName(String v)   { this.typeName = v; }
    public String getDescription()      { return description; }
    public void setDescription(String v){ this.description = v; }

    @Override
    public String toString() {
        return String.format("CarType[id=%d, name=%s]", typeId, typeName);
    }
}
