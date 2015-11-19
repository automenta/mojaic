package ai.cogmission.mosaic;

import ai.cogmission.mosaic.PathIterator.HorizontalPath;
import ai.cogmission.mosaic.PathIterator.VerticalPath;

/**
 * Implemented by those classes interested in iterating over
 * {@link PathIterator.Path} objects to accomplish some work
 * over a given path.
 * 
 * @param <T>	the type specified by {@link MosaicEngineImpl}'s 
 * 				generics resolution.
 */
@SuppressWarnings("rawtypes")
interface PathVisitor <T> {
	void visit(HorizontalPath hp);
    void visit(VerticalPath vp);
}
