package tech.deplant.java4ever.binding.generator.jtype;

import tech.deplant.java4ever.binding.generator.javapoet.TypeSpec;
import tech.deplant.java4ever.binding.generator.reference.ApiType;

public record JavaDummy(ApiType type) implements JavaType {
	@Override
	public TypeSpec.Builder poeticize() {
		return null;
	}

	@Override
	public String name() {
		return type().name();
	}

	@Override
	public boolean isSimpleWrapper() {
		return false;
	}

	@Override
	public boolean isFlatType() {
		return true;
	}

	@Override
	public boolean isStructure() {
		return false;
	}

	@Override
	public boolean isParams() {
		return false;
	}

	@Override
	public boolean isResult() {
		return false;
	}
}
