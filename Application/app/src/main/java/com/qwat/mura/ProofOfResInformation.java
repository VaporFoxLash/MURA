package com.qwat.mura;

public class ProofOfResInformation {

    public String name;
    public String address;

    public String IDNumber;
    public String RequestReason;

    public ProofOfResInformation(){

    }

    public ProofOfResInformation(String name, String address, String RequestReason, String IDNumber) {
        this.name = name;
        this.address = address;
        this.IDNumber = IDNumber;
        RequestReason = RequestReason;
    }
}
