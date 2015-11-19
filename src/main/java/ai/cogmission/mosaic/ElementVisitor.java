package ai.cogmission.mosaic;

/**
 * Implemented by Visitors wishing to modify {@link Nodes} within a 
 * given {@link PathIterator.Path}.
 *
 * @param <T>	the type specified by {@link MosaicEngineImpl}'s 
 * 				generics resolution.
 */
interface ElementVisitor <T> {
    void visitHorizontal(Node<T> n);
    void visitHorizontal(Divider<T> d);
    void visitVertical(Node<T> n);
    void visitVertical(Divider<T> d);
}
