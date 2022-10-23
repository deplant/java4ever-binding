package tech.deplant.java4ever.binding;

import java.util.concurrent.Future;

/**
 * Exception thrown when attempting to use EVER-SDK API methods
 * This exception has {@link #errorResponse()} field that holds error response from EVER-SDK.
 * All additional causes can be inspected using the {@link #getCause()} method.
 *
 * @author Doug Lea
 * @see Future
 * @since 1.5
 */
public class EverSdkException extends Exception {
	private static final long serialVersionUID = 7830266012832686185L;
	private final ErrorResult errorResponse;

	/**
	 * Constructs an {@code ExecutionException} with no detail message.
	 * The cause is not initialized, and may subsequently be
	 * initialized by a call to {@link #initCause(Throwable) initCause}.
	 */
	public EverSdkException(ErrorResult errorResponse) {
		super();
		this.errorResponse = errorResponse;
	}

	/**
	 * Constructs an {@code ExecutionException} with the specified detail
	 * message. The cause is not initialized, and may subsequently be
	 * initialized by a call to {@link #initCause(Throwable) initCause}.
	 *
	 * @param message the detail message
	 */
	public EverSdkException(ErrorResult errorResponse, String message) {
		super(message);
		this.errorResponse = errorResponse;
	}

	/**
	 * Constructs an {@code ExecutionException} with the specified detail
	 * message and cause.
	 *
	 * @param message the detail message
	 * @param cause   the cause (which is saved for later retrieval by the
	 *                {@link #getCause()} method)
	 */
	public EverSdkException(ErrorResult errorResponse, String message, Throwable cause) {
		super(message, cause);
		this.errorResponse = errorResponse;
	}

	/**
	 * Constructs an {@code ExecutionException} with the specified cause.
	 * The detail message is set to {@code (cause == null ? null :
	 * cause.toString())} (which typically contains the class and
	 * detail message of {@code cause}).
	 *
	 * @param cause the cause (which is saved for later retrieval by the
	 *              {@link #getCause()} method)
	 */
	public EverSdkException(ErrorResult errorResponse, Throwable cause) {
		super(cause);
		this.errorResponse = errorResponse;
	}

	public ErrorResult errorResponse() {
		return this.errorResponse;
	}

	public record ErrorResultData(String coreVersion,
	                              String phase,
	                              int exitCode,
	                              int exitArg,
	                              String accountAddress,
	                              int gasUsed,
	                              String description,
	                              String contractError,
	                              String[] configServers,
	                              String queryUrl,
	                              String queryIpAddress,
	                              String transactionId,
	                              String messageId,
	                              String shardBlockId,
	                              String waitingExpirationTime,
	                              String blockTime,
	                              ErrorResult localError) {
	}

	public record ErrorResult(int code, String message, ErrorResultData data) {
		public ErrorResult(int code, String message) {
			this(code, message, null);
		}
	}


}
