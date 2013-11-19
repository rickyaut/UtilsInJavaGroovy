package com.rickyaut.tools.fatwire;


public enum AssetTypeEnum {
	Simple, Child, Parent, Attribute, Definition, Filter, ParentDefinition;

	public static AssetTypeEnum fromTypeName(String type) {
		if("Basic Asset".equals(type)){
			return Simple;
		}
		if("Flex Asset".equals(type)){
			return Child;
		}
		if("Complex Asset".equals(type)){
			return Simple;
		}
		if("Flex Attribute".equals(type)){
			return Attribute;
		}
		if("Flex Definition".equals(type)){
			return Definition;
		}
		if("Flex Parent".equals(type)){
			return Parent;
		}
		if("Flex Parent Definition".equals(type)){
			return ParentDefinition;
		}
		if("AssetMaker".equals(type)){
			return Simple;
		}
		return null;
	}
}
