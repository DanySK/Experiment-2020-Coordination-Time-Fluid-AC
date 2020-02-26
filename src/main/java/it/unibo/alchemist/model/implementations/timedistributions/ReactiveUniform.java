/*
 * Copyright (C) 2010-2019, Danilo Pianini and contributors listed in the main project's alchemist/build.gradle file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.model.implementations.timedistributions;

import it.unibo.alchemist.model.implementations.times.DoubleTime;
import it.unibo.alchemist.model.interfaces.Environment;
import it.unibo.alchemist.model.interfaces.Time;

/**
 * A DiracComb is a sequence of events that happen every fixed time interval.
 *
 * @param <T>
 */
public class ReactiveUniform<T> extends AbstractDistribution<T> {

    private static final long serialVersionUID = -5382454244629122722L;

    private final double timeInterval;
    private Time lastValidTime;

    /**
     * @param start
     *            initial time
     * @param rate
     *            how many events should happen per time unit
     */
    public ReactiveUniform(final Time start, final double rate) {
        super(start);
        timeInterval = 1 / rate;
        lastValidTime = start;
    }

    /**
     * @param rate
     *            how many events should happen per time unit
     */
    public ReactiveUniform(final double rate) {
        this(new DoubleTime(), rate);
    }

    @Override
    public final double getRate() {
        return 1 / timeInterval;
    }

    @Override
    protected final void updateStatus(
            final Time curTime,
            final boolean executed,
            final double param,
            final Environment<T, ?> env) {
        if (executed) {
            lastValidTime = curTime;
        }
        if (param == 0) {
            setTau(DoubleTime.INFINITE_TIME);
        } else {
            if (getNextOccurence().isInfinite()) {
                // I was previously suspended.
                var wantedNext = lastValidTime.toDouble() + timeInterval;
                if (curTime.toDouble() <= wantedNext) {
                    setTau(new DoubleTime(wantedNext));
                } else {
                    setTau(new DoubleTime(curTime.toDouble() + timeInterval));
                }
            }
        }
    }

    @Override
    public final ReactiveUniform<T> clone(final Time currentTime) {
        return new ReactiveUniform<>(currentTime, 1 / timeInterval);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + " every " + timeInterval;
    }

}
