/*
 * Copyright 2008-2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

import java.util.concurrent.Executor;

/**
 * An expression used to set the final parameters on how the pv expression
 * should be written.
 * 
 * @param <T> the type of the expression
 * @author carcassi
 */
public class PVWriterConfiguration<T> extends CommonConfiguration {

    @Override
    public PVWriterConfiguration<T> from(DataSource dataSource) {
        super.from(dataSource);
        return this;
    }

    @Override
    public PVWriterConfiguration<T> notifyOn(Executor onThread) {
        super.notifyOn(onThread);
        return this;
    }
    private WriteExpression<T> writeExpression;
    private ExceptionHandler exceptionHandler;

    PVWriterConfiguration(WriteExpression<T> writeExpression) {
        this.writeExpression = writeExpression;
    }

    /**
     * Forwards exception to the given exception handler. No thread switch
     * is done, so the handler is notified on the thread where the exception
     * was thrown.
     * <p>
     * Giving a custom exception handler will disable the default handler,
     * so {@link PVWriter#lastWriteException() } is no longer set and no notification
     * is done.
     *
     * @param exceptionHandler an exception handler
     * @return this
     */
    public PVWriterConfiguration<T> routeExceptionsTo(ExceptionHandler exceptionHandler) {
        if (this.exceptionHandler != null) {
            throw new IllegalArgumentException("Exception handler already set");
        }
        this.exceptionHandler = exceptionHandler;
        return this;
    }
        
        private PVWriter<T> create(boolean syncWrite) {
            checkDataSourceAndThreadSwitch();

            // Create PVReader and connect
            PVWriterImpl<T> pvWriter = new PVWriterImpl<T>(syncWrite);
            WriteBuffer writeBuffer = WriteExpressionImpl.implOf(writeExpression).createWriteBuffer().build();
            if (exceptionHandler == null) {
                exceptionHandler = ExceptionHandler.createDefaultExceptionHandler(pvWriter, notificationExecutor);
            }
            WriteFunction<T> writeFunction = WriteExpressionImpl.implOf(writeExpression).getWriteFunction();
            
            pvWriter.setWriteDirector(new WriteDirector<T>(writeFunction, writeBuffer, source, PVManager.getAsyncWriteExecutor(), exceptionHandler));
            return pvWriter;
        }
        
        public PVWriter<T> sync() {
            return create(true);
        }
        
        public PVWriter<T> async() {
            return create(false);
        }
}
