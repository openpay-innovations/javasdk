/*
 * Integration API
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: v1
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package com.plugint.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.plugint.client.model.IntegrationApiModelsCommandsAddress;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.IOException;
/**
 * The customer&#x27;s&#x27; personal and contact details
 */
@Schema(description = "The customer's' personal and contact details")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2020-02-24T06:53:25.443Z[GMT]")
public class IntegrationApiModelsCommandsPersonalDetails {
  @SerializedName("firstName")
  private String firstName = null;

  @SerializedName("otherNames")
  private String otherNames = null;

  @SerializedName("familyName")
  private String familyName = null;

  @SerializedName("email")
  private String email = null;

  @SerializedName("dateOfBirth")
  private String dateOfBirth = null;

  @SerializedName("gender")
  private String gender = null;

  @SerializedName("phoneNumber")
  private String phoneNumber = null;

  @SerializedName("residentialAddress")
  private IntegrationApiModelsCommandsAddress residentialAddress = null;

  @SerializedName("deliveryAddress")
  private IntegrationApiModelsCommandsAddress deliveryAddress = null;

  public IntegrationApiModelsCommandsPersonalDetails firstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

   /**
   * The customer’s first name
   * @return firstName
  **/
  @Schema(required = true, description = "The customer’s first name")
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public IntegrationApiModelsCommandsPersonalDetails otherNames(String otherNames) {
    this.otherNames = otherNames;
    return this;
  }

   /**
   * The customer’s other names (middle names)
   * @return otherNames
  **/
  @Schema(description = "The customer’s other names (middle names)")
  public String getOtherNames() {
    return otherNames;
  }

  public void setOtherNames(String otherNames) {
    this.otherNames = otherNames;
  }

  public IntegrationApiModelsCommandsPersonalDetails familyName(String familyName) {
    this.familyName = familyName;
    return this;
  }

   /**
   * The customer’s family name
   * @return familyName
  **/
  @Schema(required = true, description = "The customer’s family name")
  public String getFamilyName() {
    return familyName;
  }

  public void setFamilyName(String familyName) {
    this.familyName = familyName;
  }

  public IntegrationApiModelsCommandsPersonalDetails email(String email) {
    this.email = email;
    return this;
  }

   /**
   * The email address in value@yy.yy format.  Specific email addreses can be used to achieve particular effects:  - success@xx.yy    Return to the caller’s website with the status SUCCESS or LODGED (depends on plan creation type)  - cancelled@xx.yy    Return to the caller’s website with the status CANCELLED  - failure@xx.yy    Return to the caller’s website with the status FAILURE  - declinecapture@xx.yy    Decline payment capture when plan created using this email address
   * @return email
  **/
  @Schema(required = true, description = "The email address in value@yy.yy format.  Specific email addreses can be used to achieve particular effects:  - success@xx.yy    Return to the caller’s website with the status SUCCESS or LODGED (depends on plan creation type)  - cancelled@xx.yy    Return to the caller’s website with the status CANCELLED  - failure@xx.yy    Return to the caller’s website with the status FAILURE  - declinecapture@xx.yy    Decline payment capture when plan created using this email address")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public IntegrationApiModelsCommandsPersonalDetails dateOfBirth(String dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
    return this;
  }

   /**
   * The customer&#x27;s date of birth
   * @return dateOfBirth
  **/
  @Schema(description = "The customer's date of birth")
  public String getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(String dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public IntegrationApiModelsCommandsPersonalDetails gender(String gender) {
    this.gender = gender;
    return this;
  }

   /**
   * The customer&#x27;s gender  U&#x3D;Unspecified, M&#x3D;Male, F&#x3D;Female, O&#x3D;Other
   * @return gender
  **/
  @Schema(description = "The customer's gender  U=Unspecified, M=Male, F=Female, O=Other")
  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public IntegrationApiModelsCommandsPersonalDetails phoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
    return this;
  }

   /**
   * A valid mobile phone number for the customer
   * @return phoneNumber
  **/
  @Schema(description = "A valid mobile phone number for the customer")
  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public IntegrationApiModelsCommandsPersonalDetails residentialAddress(IntegrationApiModelsCommandsAddress residentialAddress) {
    this.residentialAddress = residentialAddress;
    return this;
  }

   /**
   * Get residentialAddress
   * @return residentialAddress
  **/
  @Schema(description = "")
  public IntegrationApiModelsCommandsAddress getResidentialAddress() {
    return residentialAddress;
  }

  public void setResidentialAddress(IntegrationApiModelsCommandsAddress residentialAddress) {
    this.residentialAddress = residentialAddress;
  }

  public IntegrationApiModelsCommandsPersonalDetails deliveryAddress(IntegrationApiModelsCommandsAddress deliveryAddress) {
    this.deliveryAddress = deliveryAddress;
    return this;
  }

   /**
   * Get deliveryAddress
   * @return deliveryAddress
  **/
  @Schema(required = true, description = "")
  public IntegrationApiModelsCommandsAddress getDeliveryAddress() {
    return deliveryAddress;
  }

  public void setDeliveryAddress(IntegrationApiModelsCommandsAddress deliveryAddress) {
    this.deliveryAddress = deliveryAddress;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IntegrationApiModelsCommandsPersonalDetails integrationApiModelsCommandsPersonalDetails = (IntegrationApiModelsCommandsPersonalDetails) o;
    return Objects.equals(this.firstName, integrationApiModelsCommandsPersonalDetails.firstName) &&
        Objects.equals(this.otherNames, integrationApiModelsCommandsPersonalDetails.otherNames) &&
        Objects.equals(this.familyName, integrationApiModelsCommandsPersonalDetails.familyName) &&
        Objects.equals(this.email, integrationApiModelsCommandsPersonalDetails.email) &&
        Objects.equals(this.dateOfBirth, integrationApiModelsCommandsPersonalDetails.dateOfBirth) &&
        Objects.equals(this.gender, integrationApiModelsCommandsPersonalDetails.gender) &&
        Objects.equals(this.phoneNumber, integrationApiModelsCommandsPersonalDetails.phoneNumber) &&
        Objects.equals(this.residentialAddress, integrationApiModelsCommandsPersonalDetails.residentialAddress) &&
        Objects.equals(this.deliveryAddress, integrationApiModelsCommandsPersonalDetails.deliveryAddress);
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstName, otherNames, familyName, email, dateOfBirth, gender, phoneNumber, residentialAddress, deliveryAddress);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IntegrationApiModelsCommandsPersonalDetails {\n");
    
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    otherNames: ").append(toIndentedString(otherNames)).append("\n");
    sb.append("    familyName: ").append(toIndentedString(familyName)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    dateOfBirth: ").append(toIndentedString(dateOfBirth)).append("\n");
    sb.append("    gender: ").append(toIndentedString(gender)).append("\n");
    sb.append("    phoneNumber: ").append(toIndentedString(phoneNumber)).append("\n");
    sb.append("    residentialAddress: ").append(toIndentedString(residentialAddress)).append("\n");
    sb.append("    deliveryAddress: ").append(toIndentedString(deliveryAddress)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}
