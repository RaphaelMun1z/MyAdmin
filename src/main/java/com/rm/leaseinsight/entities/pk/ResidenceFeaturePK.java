package com.rm.leaseinsight.entities.pk;

import java.io.Serializable;
import java.util.Objects;

import com.rm.leaseinsight.entities.AdditionalFeature;
import com.rm.leaseinsight.entities.Residence;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Embeddable
public class ResidenceFeaturePK implements Serializable {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "property_id")
	private Residence property;

	@ManyToOne
	@JoinColumn(name = "feature_id")
	private AdditionalFeature additionalFeature;

	public Residence getProperty() {
		return property;
	}

	public void setProperty(Residence property) {
		this.property = property;
	}

	public AdditionalFeature getAdditionalFeature() {
		return additionalFeature;
	}

	public void setAdditionalFeature(AdditionalFeature additionalFeature) {
		this.additionalFeature = additionalFeature;
	}

	@Override
	public int hashCode() {
		return Objects.hash(additionalFeature, property);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResidenceFeaturePK other = (ResidenceFeaturePK) obj;
		return Objects.equals(additionalFeature, other.additionalFeature) && Objects.equals(property, other.property);
	}

}
