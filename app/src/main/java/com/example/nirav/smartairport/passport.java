package com.example.nirav.smartairport;

/**
 * Created by Pratik on 09-10-2016.
 */
public class passport {

    private String F_name,L_name,M_name,Passport_no,Address,Mobile_no,Nationality;

    public passport()
    {}


    public passport(String f_name, String l_name, String m_name, String passport_no, String address, String mobile_no, String nationality) {
        this.F_name = f_name;
        this.L_name = l_name;
        this.M_name = m_name;
        this.Passport_no = passport_no;
        this.Address = address;
        this.Mobile_no = mobile_no;
        this.Nationality = nationality;
    }

    public String getF_name() {
        return F_name;
    }

    public String getL_name() {
        return L_name;
    }

    public String getM_name() {
        return M_name;
    }

    public String getAddress() {
        return Address;
    }

    public String getMobile_no() {
        return Mobile_no;
    }

    public String getNationality() {
        return Nationality;
    }

    public String getPassport_no() {
        return Passport_no;
    }

    public void setF_name(String f_name) {
        F_name = f_name;
    }

    public void setL_name(String l_name) {
        L_name = l_name;
    }

    public void setM_name(String m_name) {
        M_name = m_name;
    }

    public void setPassport_no(String passport_no) {
        Passport_no = passport_no;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setMobile_no(String mobile_no) {
        Mobile_no = mobile_no;
    }

    public void setNationality(String nationality) {
        Nationality = nationality;
    }
}
