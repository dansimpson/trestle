package io.trestle.assets;

public class MergedBundle implements Content {

	private final Content[] contents;
	private final String contentType;

	public MergedBundle(Content... contents) throws BundleException {
		assert (contents.length > 0);
		this.contentType = contents[0].getContentType();
		for (Content content : contents) {
			if (!contentType.equals(content.getContentType())) {
				throw new BundleException("MergedBundle expects bundles of the same content type");
			}
		}
		this.contents = contents;
	}

	@Override
	public String getContent() {
		StringBuilder builder = new StringBuilder();
		for (Content content : contents) {
			builder.append(content.getContent());
		}
		return builder.toString();
	}

	@Override
	public String getContentType() {
		return contentType;
	}

}
