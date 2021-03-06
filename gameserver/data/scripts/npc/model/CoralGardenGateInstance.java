package npc.model;

import l2ft.gameserver.model.Player;
import l2ft.gameserver.model.entity.Reflection;
import l2ft.gameserver.model.instances.NpcInstance;
import l2ft.gameserver.templates.npc.NpcTemplate;
import l2ft.gameserver.utils.ReflectionUtils;
import instances.CrystalCaverns;

public class CoralGardenGateInstance extends NpcInstance
{
	private static final long serialVersionUID = -1L;
	public CoralGardenGateInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if(!canBypassCheck(player, this))
			return;

		if(command.equalsIgnoreCase("request_coralg"))
		{
			Reflection r = player.getActiveReflection();
			if(r != null)
			{
				if(player.canReenterInstance(10))
					player.teleToLocation(r.getTeleportLoc(), r);
			}
			else if(player.canEnterInstance(10))
			{
				ReflectionUtils.enterReflection(player, new CrystalCaverns(), 10);
			}
		}
		else
			super.onBypassFeedback(player, command);
	}
}
