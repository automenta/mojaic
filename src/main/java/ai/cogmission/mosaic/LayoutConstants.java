package ai.cogmission.mosaic;

/**
 * Designates official indexes and keys for internal
 * storage and serialization.
 * 
 * @author David Ray
 */
public interface LayoutConstants {
	/** Parameter indexes */
	int ID = 0;
	int X = 1;
	int Y = 2;
	int W = 3;
	int H = 4;
	int PRV_N = 5;
	int MIN_W = 5;
	int NXT_N = 6;
	int MAX_W = 6;
	int LED_J = 7;
	int MIN_H = 7;
	int TRL_J = 8;
	int MAX_H = 8;
	int H_WT = 9;
	int V_WT = 10;
	
	String KEY_CORNER_CLICK_RADIUS = "cornerClickRadius";
	String KEY_SNAP_DISTANCE = "snapDistance";
	String KEY_SURFACE_OFFSET = "offset";
	String KEY_USE_INT_PRECISION = "useIntegerPrecision";
	String KEY_LAYOUT = "layout";
	String KEY_SURFACE_BOUNDS = "coords";
	String KEY_IGNORE_BOUNDS = "bounds";
	String KEY_DIVIDER_SIZE = "dividerSize";
	String KEY_USE_SURFACE_OFFSET = "useSurfaceOffset";
	String KEY_CELLS = "cells";
	String KEY_DIVIDER_BOUNDS = "divBounds";
	
	String CELL_PTRN = "[\\s,\\,]+";
	String SUB_PTRN = "[\\:]+";
}
