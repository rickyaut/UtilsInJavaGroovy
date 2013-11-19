package com.rickyaut.tools.fatwire;

import java.util.List;


public class AssetType {
	String assetTypeId;
	String assetType;
	String description;
	AssetTypeEnum type;
	List<Association> associations;
	AssetType flexAttribute;
	AssetType parentDefinition;
	AssetType flexDefinition;
	AssetType flexParent;
	AssetType flexFilter;
	
	public AssetType() {
		super();
	}

	public AssetType(String assetTypeId, String assetType, String description, AssetTypeEnum type,
			List<Association> associations) {
		super();
		this.assetTypeId = assetTypeId;
		this.assetType = assetType;
		this.description = description;
		this.type = type;
		this.associations = associations;
	}

	public String getAssetTypeId() {
		return assetTypeId;
	}

	public void setAssetTypeId(String assetTypeId) {
		this.assetTypeId = assetTypeId;
	}

	public String getAssetType() {
		return assetType;
	}
	
	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public AssetTypeEnum getType() {
		return type;
	}
	
	public void setType(AssetTypeEnum type) {
		this.type = type;
	}
	
	public List<Association> getAssociations() {
		return associations;
	}

	public void setAssociations(List<Association> associations) {
		this.associations = associations;
	}

	
	public AssetType getFlexAttribute() {
		return flexAttribute;
	}

	
	public void setFlexAttribute(AssetType flexAttribute) {
		this.flexAttribute = flexAttribute;
	}

	
	public AssetType getParentDefinition() {
		return parentDefinition;
	}

	
	public void setParentDefinition(AssetType parentDefinition) {
		this.parentDefinition = parentDefinition;
	}

	
	public AssetType getFlexDefinition() {
		return flexDefinition;
	}

	
	public void setFlexDefinition(AssetType flexDefinition) {
		this.flexDefinition = flexDefinition;
	}

	
	public AssetType getFlexParent() {
		return flexParent;
	}

	
	public void setFlexParent(AssetType flexParent) {
		this.flexParent = flexParent;
	}

	
	public AssetType getFlexFilter() {
		return flexFilter;
	}

	
	public void setFlexFilter(AssetType flexFilter) {
		this.flexFilter = flexFilter;
	}
	
}
