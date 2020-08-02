package com.sih.hawkeye.ocr;

/**
 * Created by JithinJude on 02,August,2020
 */
class Vehicle {
    String vehicleReportId;
    String vehicleRegNo;
    String vehicleOffence;
    String vehicleImageUrl;
    String vehicleReportDate;

    public Vehicle(String vehicleReportId, String vehicleRegNo, String vehicleOffence, String vehicleImageUrl,
                 String vehicleReportDate){
        this.vehicleReportId = vehicleReportId;
        this.vehicleRegNo = vehicleRegNo;
        this.vehicleOffence = vehicleOffence;
        this.vehicleImageUrl = vehicleImageUrl;
        this.vehicleReportDate = vehicleReportDate;
    }

    public Vehicle(){}

    public String getVehicleId() {
        return vehicleReportId;
    }

    public void setVehicleId(String vehicleReportId) {
        this.vehicleReportId = vehicleReportId;
    }

    public String getVehicleOffence() {
        return vehicleOffence;
    }

    public void setVehicleOffence(String vehicleOffence) {
        this.vehicleOffence = vehicleOffence;
    }

    public String getVehicleRegNo() {
        return vehicleRegNo;
    }

    public void setVehicleRegNo(String vehicleRegNo) {
        this.vehicleRegNo = vehicleRegNo;
    }

    public String getVehicleImageUrl() {
        return vehicleImageUrl;
    }

    public void setVehicleImageUrl(String vehicleImageUrl) {
        this.vehicleImageUrl = vehicleImageUrl;
    }

    public String getVehicleReportDate() {
        return vehicleReportDate;
    }

    public void setVehicleReportDate(String vehicleReportDate) {
        this.vehicleReportDate = vehicleReportDate;
    }
}