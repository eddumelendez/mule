/*
 * (c) 2003-2014 MuleSoft, Inc. This software is protected under international copyright
 * law. All use of this software is subject to MuleSoft's Master Subscription Agreement
 * (or other master license agreement) separately entered into in writing between you and
 * MuleSoft. If such an agreement is not in place, you may not use the software.
 */
package org.mule.module.jca;

import org.mule.api.MuleException;
import org.mule.config.ImmutableThreadingProfile;

import java.util.concurrent.RejectedExecutionException;

import javax.resource.spi.work.ExecutionContext;
import javax.resource.spi.work.Work;
import javax.resource.spi.work.WorkException;
import javax.resource.spi.work.WorkListener;

/**
 * <code>DelegateWorkManager</code> is a wrapper around a WorkManager provided by a
 * JCA container.
 */
public class DelegateWorkManager implements org.mule.api.context.WorkManager
{
    private final javax.resource.spi.work.WorkManager workManager;

    public DelegateWorkManager(javax.resource.spi.work.WorkManager workManager2)
    {
        this.workManager = workManager2;
    }

    public void doWork(Work work) throws WorkException
    {
        workManager.doWork(work);
    }

    public void doWork(Work work, long l, ExecutionContext executionContext, WorkListener workListener)
        throws WorkException
    {
        workManager.doWork(work, l, executionContext, workListener);
    }

    public long startWork(Work work) throws WorkException
    {
        return workManager.startWork(work);
    }

    public long startWork(Work work, long l, ExecutionContext executionContext, WorkListener workListener)
        throws WorkException
    {
        return workManager.startWork(work, l, executionContext, workListener);
    }

    public void scheduleWork(Work work) throws WorkException
    {
        workManager.scheduleWork(work);
    }

    public void scheduleWork(Work work, long l, ExecutionContext executionContext, WorkListener workListener)
        throws WorkException
    {
        workManager.scheduleWork(work, l, executionContext, workListener);
    }

    public void execute(Runnable command)
    {
        try
        {
            this.scheduleWork(new RunnableWorkAdapter(command));
        }
        catch (WorkException wex)
        {
            // unfortunately RejectedExecutionException is the closest thing we have
            // as proper RuntimeException
            throw new RejectedExecutionException(wex);
        }
    }

    public void start() throws MuleException
    {
        // nothing to do
    }

    public boolean isStarted()
    {
        return true;
    }

    public void stop() throws MuleException
    {
        // nothing to do
    }

    public void dispose()
    {
        // nothing to do
    }

    protected static class RunnableWorkAdapter implements Work
    {
        private final Runnable command;

        public RunnableWorkAdapter(Runnable command)
        {
            super();
            this.command = command;
        }

        public void release()
        {
            // nothing to do
        }

        public void run()
        {
            command.run();
        }
    }

    public ImmutableThreadingProfile getThreadingProfile()
    {
        throw new UnsupportedOperationException("Container does not have a Mule ThreadingProfile");
    }

}