package ai.cogmission.mosaic;


/**
 * Implemented by Nodes or Dividers accepting an {@link ElementVisitor}
 * to modify nodes.
 * 
 * @param <T>	the type specified by {@link MosaicEngineImpl}'s 
 * 				generics resolution.
 */
interface ElementVisitable <T> {
    void acceptHorizontal(ElementVisitor<T> ev);
    void acceptVertical(ElementVisitor<T> ev);
}
