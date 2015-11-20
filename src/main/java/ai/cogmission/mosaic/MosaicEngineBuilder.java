package ai.cogmission.mosaic;


public class MosaicEngineBuilder<T> implements EngineBuilder<T> {
	/**
	 * Returns a new {@link MosaicEngine}
	 */
	@Override
	public MosaicEngine<T> build() {
		return new MosaicEngineImpl<>();
	}
}
