package utils;

import java.util.function.BooleanSupplier;
import org.osbot.rs07.utility.ConditionalSleep;

public final class Sleep extends ConditionalSleep {

	private final BooleanSupplier condition;

	public Sleep(final BooleanSupplier condition, final int timeout) {
		super(timeout);
		this.condition = condition;
	}

	@Override
	public final boolean condition() throws InterruptedException {
		return condition.getAsBoolean();
	}

	public static boolean sleepUntil(final BooleanSupplier condition, final int timeout) {
		return new Sleep(condition, timeout).sleep();

	}
}