package org.hyperion.rs2;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.net.Packet;
import org.hyperion.rs2.net.RS2CodecFactory;

/**
 * The <code>ConnectionHandler</code> processes incoming events from MINA,
 * submitting appropriate tasks to the <code>GameEngine</code>.
 * @author Graham Edgecombe
 *
 */
public class ConnectionHandler extends IoHandlerAdapter {
	
	/**
	 * The <code>GameEngine</code> instance.
	 */
	@SuppressWarnings("unused")
	private final GameEngine engine = World.getWorld().getEngine();

	@Override
	public void exceptionCaught(IoSession session, Throwable throwable) throws Exception {
		session.close(false);
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		if(session.containsAttribute("player")) {
			Player p = (Player) session.getAttribute("player");
			Packet packet = (Packet) message;
			if(p.getQueuedPackets().size() < 15)
				p.getQueuedPackets().add(packet);
		}
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		//engine.pushTask(new SessionClosedTask(session));
		if(session.containsAttribute("player")) {
			Player p = (Player) session.getAttribute("player");
			World.getWorld().unregister(p);
		}
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		//session.close(false);
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		session.setAttribute("remote", session.getRemoteAddress());
		session.getFilterChain().addFirst("protocol", new ProtocolCodecFilter(RS2CodecFactory.LOGIN));
		//engine.pushTask(new SessionOpenedTask(session));
	}

}
