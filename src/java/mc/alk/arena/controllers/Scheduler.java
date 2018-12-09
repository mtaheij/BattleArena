package mc.alk.arena.controllers;

import mc.alk.arena.BattleArena;
import mc.alk.arena.Defaults;
import mc.alk.arena.util.Log;
import mc.alk.arena.util.compat.ISchedulerHelper;
import mc.euro.version.Version;
import mc.euro.version.VersionFactory;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author alkarin
 *
 */
public class Scheduler {

    static int count = 0; /// count of current async timers

    private static ISchedulerHelper handler;

    static {
        Class<?>[] args = {};
        try {
            Version server = VersionFactory.getServerVersion();
            final Class<?> clazz;
            if (server.isGreaterThanOrEqualTo("1.6")) {
                clazz = Class.forName("mc.alk.arena.util.compat.v1_6_R1.SchedulerHelper");
            } else {
                clazz = Class.forName("mc.alk.arena.util.compat.pre.SchedulerHelper");
            }
            handler = (ISchedulerHelper) clazz.getConstructor(args).newInstance((Object[]) args);
        } catch (Exception e) {
            try {
                final Class<?> clazz = Class.forName("mc.alk.arena.util.compat.pre.SchedulerHelper");
                handler = (ISchedulerHelper) clazz.getConstructor(args).newInstance((Object[]) args);
            } catch (Exception e2) {
                //noinspection PointlessBooleanExpression,ConstantConditions
                if (!Defaults.TESTSERVER && !Defaults.TESTSERVER_DEBUG) {
                    Log.printStackTrace(e2);
                }
            }
            //noinspection PointlessBooleanExpression,ConstantConditions
            if (!Defaults.TESTSERVER && !Defaults.TESTSERVER_DEBUG) {
                Log.printStackTrace(e);
            }
        }
    }

    public static int scheduleAsynchronousTask(Runnable task) {
        return handler.scheduleAsyncTask(BattleArena.getSelf(), task, 0);
    }

    public static int scheduleAsynchronousTask(Runnable task, long ticks) {
        return handler.scheduleAsyncTask(BattleArena.getSelf(), task, ticks);
    }

    public static int scheduleSynchronousTask(Runnable task) {
        return Bukkit.getScheduler().scheduleSyncDelayedTask(BattleArena.getSelf(), task, 0);
    }

    public static int scheduleSynchronousTask(Runnable task, long ticks) {
        return Bukkit.getScheduler().scheduleSyncDelayedTask(BattleArena.getSelf(), task, ticks);
    }

    public static int scheduleSynchronousTask(Plugin plugin, Runnable task) {
        return Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, task, 0);
    }

    public static int scheduleSynchronousTask(Plugin plugin, Runnable task, long ticks) {
        return Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, task, ticks);
    }

    public static void cancelTask(int taskId) {
        Bukkit.getScheduler().cancelTask(taskId);
    }

}
