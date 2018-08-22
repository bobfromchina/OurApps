package com.lovely3x.common.hotfix;

import java.io.Serializable;

public class Instruction implements Serializable{

    private int opCode;

    private String patchUrl;

    private int aps;

    public int getAps() {
        return aps;
    }

    public void setAps(int aps) {
        this.aps = aps;
    }

    public Instruction() {
    }

    public Instruction(int opCode, String patchUrl) {
        this.opCode = opCode;
        this.patchUrl = patchUrl;
    }

    public int getOpCode() {
        return this.opCode;
    }

    public void setOpCode(int opCode) {
        this.opCode = opCode;
    }

    public String getPatchUrl() {
        return this.patchUrl;
    }

    public void setPatchUrl(String patchUrl) {
        this.patchUrl = patchUrl;
    }

    @Override
    public String toString() {
        return "Instruction{" +
                "opCode=" + opCode +
                ", patchUrl='" + patchUrl + '\'' +
                ", aps=" + aps +
                '}';
    }
}