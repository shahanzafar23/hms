package com.hms.domain;

import java.io.Serializable;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Wallet.
 */
@Document(collection = "wallet")
public class Wallet extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("street")
    private String street;

    @Field("city")
    private String city;

    @Field("state")
    private String state;

    @Field("country")
    private String country;

    @Field("post_code")
    private String postCode;

    @Field("age")
    private Double age;

    @Field("phone")
    private String phone;

    @Field("public_key")
    private String publicKey;

    @Field("private_key")
    private String privateKey;

    @Field("user_id")
    private String userId;

    @Field("hospital_name")
    private String hospitalName;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Wallet id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStreet() {
        return this.street;
    }

    public Wallet street(String street) {
        this.setStreet(street);
        return this;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return this.city;
    }

    public Wallet city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return this.state;
    }

    public Wallet state(String state) {
        this.setState(state);
        return this;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return this.country;
    }

    public Wallet country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostCode() {
        return this.postCode;
    }

    public Wallet postCode(String postCode) {
        this.setPostCode(postCode);
        return this;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public Double getAge() {
        return this.age;
    }

    public Wallet age(Double age) {
        this.setAge(age);
        return this;
    }

    public void setAge(Double age) {
        this.age = age;
    }

    public String getPhone() {
        return this.phone;
    }

    public Wallet phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPublicKey() {
        return this.publicKey;
    }

    public Wallet publicKey(String publicKey) {
        this.setPublicKey(publicKey);
        return this;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return this.privateKey;
    }

    public Wallet privateKey(String privateKey) {
        this.setPrivateKey(privateKey);
        return this;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public Wallet hospitalName(String hospitalName) {
        this.setHospitalName(hospitalName);
        return this;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getUserId() {
        return this.userId;
    }

    public Wallet userId(String userId) {
        this.setUserId(userId);
        return this;
    }

    public void generateKeys() {
        try {
            KeyPair keyPair;
            String algorithm = "RSA"; //DSA DH etc
            keyPair = KeyPairGenerator.getInstance(algorithm).
                generateKeyPair();
            privateKey = keyPair.getPrivate().getEncoded().toString();
            publicKey = keyPair.getPublic().getEncoded().toString();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Wallet)) {
            return false;
        }
        return id != null && id.equals(((Wallet) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Wallet{" +
            "id=" + getId() +
            ", street='" + getStreet() + "'" +
            ", city='" + getCity() + "'" +
            ", state='" + getState() + "'" +
            ", country='" + getCountry() + "'" +
            ", postCode='" + getPostCode() + "'" +
            ", age=" + getAge() +
            ", phone='" + getPhone() + "'" +
            ", publicKey='" + getPublicKey() + "'" +
            ", privateKey='" + getPrivateKey() + "'" +
            ", userId=" + getUserId() +
            "}";
    }
}
