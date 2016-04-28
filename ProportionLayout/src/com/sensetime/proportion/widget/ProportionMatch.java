package com.sensetime.proportion.widget;

public enum ProportionMatch{
	MATHC_WIDTH(0),MATHC_HEIGHT(1),CENTER_INSIDE(2),FIT_CENTER(3),NONE(8);
	private int value;
	private ProportionMatch(int value){
		this.value = value;
	}
	public int getValue() {
		return value;
	}
	
	public static ProportionMatch valueToMatchType(int type) {
		switch (type) {
		case 0:
			return MATHC_WIDTH;
		case 1:
			return MATHC_HEIGHT;
		case 2:
			return CENTER_INSIDE;
		case 3:
			return FIT_CENTER;
		case 8:
			return NONE;
		default:
			break;
		}
		return MATHC_WIDTH;
	}
}
