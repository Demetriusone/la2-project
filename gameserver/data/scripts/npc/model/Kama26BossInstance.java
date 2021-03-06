package npc.model;

import java.util.concurrent.ScheduledFuture;

import l2ft.commons.threading.RunnableImpl;
import l2ft.gameserver.ThreadPoolManager;
import l2ft.gameserver.listener.reflection.OnReflectionCollapseListener;
import l2ft.gameserver.model.Creature;
import l2ft.gameserver.model.entity.Reflection;
import l2ft.gameserver.model.instances.MinionInstance;
import l2ft.gameserver.scripts.Functions;
import l2ft.gameserver.templates.npc.MinionData;
import l2ft.gameserver.templates.npc.NpcTemplate;

public class Kama26BossInstance extends KamalokaBossInstance
{
	private ScheduledFuture<?> _spawner;
	private ReflectionCollapseListener _refCollapseListener = new ReflectionCollapseListener();

	public Kama26BossInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
		getMinionList().addMinion(new MinionData(18556, 1));
	}

	@Override
	public void notifyMinionDied(MinionInstance minion)
	{
		_spawner = ThreadPoolManager.getInstance().scheduleAtFixedRate(new MinionSpawner(), 60000, 60000);
	}

	@Override
	protected void onSpawn()
	{
		super.onSpawn();
		
		getReflection().addListener(_refCollapseListener);
	}

	@Override
	protected void onDeath(Creature killer)
	{
		if(_spawner != null)
			_spawner.cancel(false);
		_spawner = null;
		super.onDeath(killer);
	}

	public class MinionSpawner extends RunnableImpl
	{
		@Override
		public void runImpl()
		{
			if(!isDead() && !getMinionList().hasAliveMinions())
			{
				getMinionList().spawnMinions();
				Functions.npcSayCustomMessage(Kama26BossInstance.this, "Kama26Boss.helpme");
			}
		}
	}

	public class ReflectionCollapseListener implements OnReflectionCollapseListener
	{
		@Override
		public void onReflectionCollapse(Reflection ref)
		{
			if(_spawner != null)
				_spawner.cancel(true);
		}
	}
}