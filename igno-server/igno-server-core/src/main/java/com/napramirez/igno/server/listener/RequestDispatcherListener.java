package com.napramirez.igno.server.listener;

import org.jpos.core.Configurable;
import org.jpos.core.Configuration;
import org.jpos.core.ConfigurationException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISORequestListener;
import org.jpos.iso.ISOSource;
import org.jpos.space.Space;
import org.jpos.space.SpaceFactory;

import com.napramirez.igno.server.transaction.TransactionContext;
import com.napramirez.igno.server.transaction.TransactionContext.ContextKey;

/**
 * RequestDispatcherListener forwards the request to the switching TransactionManager.
 * 
 * @author <a href="mailto:napramirez@gmail.com">Nap Ramirez</a>
 */
public class RequestDispatcherListener
    implements ISORequestListener, Configurable
{
    private Configuration cfg;

    @SuppressWarnings( "rawtypes" )
    private Space sp;

    public void setConfiguration( Configuration cfg )
        throws ConfigurationException
    {
        this.cfg = cfg;
        sp = SpaceFactory.getSpace( cfg.get( "space" ) );
    }

    @SuppressWarnings( "unchecked" )
    public boolean process( ISOSource source, ISOMsg message )
    {
        TransactionContext ctx = new TransactionContext();
        ctx.put( ContextKey.REQUEST_MESSAGE, message );
        ctx.tput( ContextKey.ISO_MESSAGE_SOURCE, source );
        sp.out( cfg.get( "queue" ), ctx, cfg.getLong( "timeout" ) );

        return true;
    }
}
