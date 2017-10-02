package de.wwu.wmss.core;

public class FilterCapability {

	boolean melody;
	boolean group;
	boolean personRole;
	boolean performanceMedium;
	boolean performanceMediumType;
	boolean solo;
	boolean tonalityTonic;
	boolean tonalityMode;
	boolean tempo;
	boolean creationDateFrom;
	boolean creationDateTo;
	boolean source;
	boolean identifier;
	boolean format;
	
	public FilterCapability() {
		
		super();

	}

	public boolean isMelodyEnabled() {
		return melody;
	}

	public void setMelody(boolean melody) {
		this.melody = melody;
	}

	public boolean isGroupEnabled() {
		return group;
	}

	public void setGroup(boolean group) {
		this.group = group;
	}

	public boolean isPersonRoleEnabled() {
		return personRole;
	}

	public void setPersonRole(boolean personRole) {
		this.personRole = personRole;
	}

	public boolean isPerformanceMediumEnabled() {
		return performanceMedium;
	}

	public void setPerformanceMedium(boolean performanceMedium) {
		this.performanceMedium = performanceMedium;
	}

	public boolean isPerformanceMediumTypeEnabled() {
		return performanceMediumType;
	}

	public void setPerformanceMediumType(boolean performanceMediumType) {
		this.performanceMediumType = performanceMediumType;
	}

	public boolean isSoloEnabled() {
		return solo;
	}

	public void setSolo(boolean solo) {
		this.solo = solo;
	}

	public boolean isTonalityTonicEnabled() {
		return tonalityTonic;
	}

	public void setTonalityTonic(boolean tonalityTonic) {
		this.tonalityTonic = tonalityTonic;
	}

	public boolean isTonalityModeEnabled() {
		return tonalityMode;
	}

	public void setTonalityMode(boolean tonalityMode) {
		this.tonalityMode = tonalityMode;
	}

	public boolean isTempoEnabled() {
		return tempo;
	}

	public void setTempo(boolean tempo) {
		this.tempo = tempo;
	}

	public boolean isCreationDateFromEnabled() {
		return creationDateFrom;
	}

	public void setCreationDateFrom(boolean creationDateFrom) {
		this.creationDateFrom = creationDateFrom;
	}

	public boolean isCreationDateToEnabled() {
		return creationDateTo;
	}

	public void setCreationDateTo(boolean creationDateTo) {
		this.creationDateTo = creationDateTo;
	}

	public boolean isSourceEnabled() {
		return source;
	}

	public void setSource(boolean source) {
		this.source = source;
	}

	public boolean isIdentifierEnabled() {
		return identifier;
	}

	public void setIdentifier(boolean identifier) {
		this.identifier = identifier;
	}

	public boolean isFormatEnabled() {
		return format;
	}

	public void setFormat(boolean format) {
		this.format = format;
	}

	
	
	
}
