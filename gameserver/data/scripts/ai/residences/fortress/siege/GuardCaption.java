package ai.residences.fortress.siege;

import l2ft.gameserver.model.Creature;
import l2ft.gameserver.model.entity.events.impl.FortressSiegeEvent;
import l2ft.gameserver.model.entity.residence.Fortress;
import l2ft.gameserver.model.instances.NpcInstance;
import l2ft.gameserver.network.l2.components.NpcString;
import l2ft.gameserver.network.l2.components.SystemMsg;
import l2ft.gameserver.scripts.Functions;
import l2ft.gameserver.tables.SkillTable;
import ai.residences.SiegeGuardFighter;
import npc.model.residences.SiegeGuardInstance;

/**
 * @author VISTALL
 * @date 16:43/17.04.2011
 */
public class GuardCaption extends SiegeGuardFighter
{
	public GuardCaption(NpcInstance actor)
	{
		super(actor);

		actor.addListener(FortressSiegeEvent.RESTORE_BARRACKS_LISTENER);
	}

	@Override
	public void onEvtSpawn()
	{
		super.onEvtSpawn();
		SiegeGuardInstance actor = getActor();

		FortressSiegeEvent siegeEvent = actor.getEvent(FortressSiegeEvent.class);
		if(siegeEvent == null)
			return;

		if(siegeEvent.getResidence().getFacilityLevel(Fortress.GUARD_BUFF) > 0)
			actor.doCast(SkillTable.getInstance().getInfo(5432, siegeEvent.getResidence().getFacilityLevel(Fortress.GUARD_BUFF)), actor, false);

		siegeEvent.barrackAction(1, false);
	}

	@Override
	public void onEvtDead(Creature killer)
	{
		SiegeGuardInstance actor = getActor();
		FortressSiegeEvent siegeEvent = actor.getEvent(FortressSiegeEvent.class);
		if(siegeEvent == null)
			return;

		siegeEvent.barrackAction(1, true);

		siegeEvent.broadcastTo(SystemMsg.THE_BARRACKS_HAVE_BEEN_SEIZED, FortressSiegeEvent.ATTACKERS, FortressSiegeEvent.DEFENDERS);

		Functions.npcShout(actor, NpcString.AIIEEEE_COMMAND_CENTER_THIS_IS_GUARD_UNIT_WE_NEED_BACKUP_RIGHT_AWAY);

		super.onEvtDead(killer);

		siegeEvent.checkBarracks();
	}
}
