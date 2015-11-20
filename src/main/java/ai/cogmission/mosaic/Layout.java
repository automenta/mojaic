package ai.cogmission.mosaic;

/**
 * An aggregation of layout specifications used as a container to bulk add
 * specifications to a {@link Surface}
 * 
 * @author David Ray
 */
public interface Layout {
	
	/**
	 * Adds the specified {@code Layout}'s definitions to this one. Making this
	 * one a union of the two with no duplicates (according to id).
	 * 
	 * @param layout	the Layout to add to this one.
	 * @return	this {@code Layout}
	 */
	Layout add(Layout layout);
	
	/**
	 * Adds a cell specifying its most basic required attributes.
	 * 
	 * @param id
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return 	this {@code Layout}
	 */
	Layout addCell(String id, double x, double y, double width, double height);
	
	/**
	 * Adds a cell specifying the basic attributes and additional constraints.
	 * 
	 * @param id
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param minW
	 * @param maxW
	 * @param minH
	 * @param maxH
	 * @return 	this {@code Layout}
	 */
	Layout addCell(String id, double x, double y, double width, double height,
				   double minW, double maxW, double minH, double maxH);
	
	/**
	 * Returns a flag indicating whether this {@code Layout} object is
	 * relative or not.
	 * @return	true if relative, false if not.
	 */
	boolean isRelative();
	
	/**
	 * Returns the cell specification for the specified id if any 
	 * exists.
	 * 
	 * @param id	the id correlated to the returned specification string.
	 * @return		the comma-separated string representing the correlated layout.
	 */

	String getCell(String id);
	
}
