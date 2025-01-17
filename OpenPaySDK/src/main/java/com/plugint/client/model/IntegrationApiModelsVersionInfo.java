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
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.IOException;
/**
 * IntegrationApiModelsVersionInfo
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2020-02-24T06:53:25.443Z[GMT]")
public class IntegrationApiModelsVersionInfo {
  @SerializedName("applicationName")
  private String applicationName = null;

  @SerializedName("applicationVersion")
  private String applicationVersion = null;

  @SerializedName("environmentName")
  private String environmentName = null;

  public IntegrationApiModelsVersionInfo applicationName(String applicationName) {
    this.applicationName = applicationName;
    return this;
  }

   /**
   * Get applicationName
   * @return applicationName
  **/
  @Schema(description = "")
  public String getApplicationName() {
    return applicationName;
  }

  public void setApplicationName(String applicationName) {
    this.applicationName = applicationName;
  }

  public IntegrationApiModelsVersionInfo applicationVersion(String applicationVersion) {
    this.applicationVersion = applicationVersion;
    return this;
  }

   /**
   * Get applicationVersion
   * @return applicationVersion
  **/
  @Schema(description = "")
  public String getApplicationVersion() {
    return applicationVersion;
  }

  public void setApplicationVersion(String applicationVersion) {
    this.applicationVersion = applicationVersion;
  }

  public IntegrationApiModelsVersionInfo environmentName(String environmentName) {
    this.environmentName = environmentName;
    return this;
  }

   /**
   * Get environmentName
   * @return environmentName
  **/
  @Schema(description = "")
  public String getEnvironmentName() {
    return environmentName;
  }

  public void setEnvironmentName(String environmentName) {
    this.environmentName = environmentName;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IntegrationApiModelsVersionInfo integrationApiModelsVersionInfo = (IntegrationApiModelsVersionInfo) o;
    return Objects.equals(this.applicationName, integrationApiModelsVersionInfo.applicationName) &&
        Objects.equals(this.applicationVersion, integrationApiModelsVersionInfo.applicationVersion) &&
        Objects.equals(this.environmentName, integrationApiModelsVersionInfo.environmentName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(applicationName, applicationVersion, environmentName);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IntegrationApiModelsVersionInfo {\n");
    
    sb.append("    applicationName: ").append(toIndentedString(applicationName)).append("\n");
    sb.append("    applicationVersion: ").append(toIndentedString(applicationVersion)).append("\n");
    sb.append("    environmentName: ").append(toIndentedString(environmentName)).append("\n");
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
