package org.hyperion.rs2.tick;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class which schedules the execution of {@link Tick}s.
 * @author Graham
 */
public final class TickManager implements Runnable {

	/**
	 * A logger used to report error messages.
	 */
	private static final Logger logger = Logger.getLogger(TickManager.class.getName());

	/**
	 * The time period, in milliseconds, of a single cycle.
	 */
	private static final int TIME_PERIOD = 600;

	/**
	 * The {@link ScheduledExecutorService} which schedules calls to the
	 * {@link #run()} method.
	 */
	private final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

	/**
	 * A list of active Ticks.
	 */
	private final List<Tick> Ticks = new ArrayList<Tick>();

	/**
	 * A queue of Ticks that still need to be added.
	 */
	private final Queue<Tick> newTicks = new ArrayDeque<Tick>();

	/**
	 * Creates and starts the Tick scheduler.
	 */
	public TickManager() {
		service.scheduleAtFixedRate(this, 0, TIME_PERIOD, TimeUnit.MILLISECONDS);
	}

	/**
	 * Stops the Tick scheduler.
	 */
	public void terminate() {
		service.shutdown();
	}

	/**
	 * Schedules the specified Tick. If this scheduler has been stopped with
	 * the {@link #terminate()} method the Tick will not be executed or
	 * garbage-collected.
	 * @param Tick The Tick to schedule.
	 */
	public void submit(final Tick Tick) {
		if (Tick.isImmediate()) {
			service.execute(new Runnable() {
				@Override
				public void run() {
					Tick.execute();
				}
			});
		}

		synchronized (newTicks) {
			newTicks.add(Tick);
		}
	}

	/**
	 * This method is automatically called every cycle by the
	 * {@link ScheduledExecutorService} and executes, adds and removes
	 * {@link Tick}s. It should not be called directly as this will lead to
	 * concurrency issues and inaccurate time-keeping.
	 */
	@Override
	public void run() {
		synchronized (newTicks) {
			Tick Tick;
			while ((Tick = newTicks.poll()) != null)
				Ticks.add(Tick);
		}

		for (Iterator<Tick> it = Ticks.iterator(); it.hasNext(); ) {
			Tick Tick = it.next();
			try {
				if (!Tick.tick())
					it.remove();
			} catch (Throwable t) {
				logger.log(Level.SEVERE, "Exception during Tick execution.", t);
			}
		}
	}

}