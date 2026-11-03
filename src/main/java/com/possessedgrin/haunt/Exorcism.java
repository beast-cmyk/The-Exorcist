package com.possessedgrin.haunt;

import com.possessedgrin.PossessedGrin;
import ladysnake.requiem.api.v1.possession.PossessionComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.VillagerProfession;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The exorcism.
 * <ul>
 *   <li><b>Holy water:</b> any splash bottle of water that bursts on a possessed creature burns it (3 damage,
 *       hissing smoke). Six burns in a row and the demon is cast out: you're forced out of the body. The count
 *       slowly fades if they stop.</li>
 *   <li><b>Exorcists:</b> cleric villagers recognise what you are. If one can see you within 14 blocks, it throws
 *       holy water at you every few seconds, even while the other villagers run.</li>
 * </ul>
 */
public final class Exorcism {
    private static final int BURNS_TO_CAST_OUT = 6;
    private static final int BURN_FADE_TICKS = 200;   // one burn forgotten every 10 seconds
    private static final float BURN_DAMAGE = 3.0F;
    private static final double EXORCIST_RANGE = 14.0;

    private static final Map<UUID, Integer> BURNS = new HashMap<>();
    private static final Map<UUID, Long> LAST_BURN = new HashMap<>();
    private static final Map<UUID, Long> EXORCIST_READY = new HashMap<>();

    private Exorcism() {
    }

    /** A splash bottle of water just burst. Called from the potion itself. */
    public static void holyWater(PotionEntity splash) {
        if (!(splash.world instanceof ServerWorld world)) {
            return;
        }
        for (LivingEntity touched : world.getEntitiesByClass(LivingEntity.class, splash.getBoundingBox().expand(4.0, 2.0, 4.0),
                PossessedGrin::isPossessed)) {
            if (splash.squaredDistanceTo(touched) < 16.0 && PossessedGrin.possessorOf(touched) instanceof ServerPlayerEntity rider) {
                burn(world, touched, rider);
            }
        }
    }

    private static void burn(ServerWorld world, LivingEntity host, ServerPlayerEntity rider) {
        host.damage(DamageSource.MAGIC, BURN_DAMAGE);
        world.playSound(null, host.getX(), host.getY(), host.getZ(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.HOSTILE, 1.2F, 0.6F);
        world.playSound(null, host.getX(), host.getY(), host.getZ(), SoundEvents.ENTITY_GHAST_HURT, SoundCategory.HOSTILE, 0.8F, 0.5F);
        world.spawnParticles(ParticleTypes.LARGE_SMOKE, host.getX(), host.getBodyY(0.6), host.getZ(), 15, host.getWidth() / 2.0, host.getHeight() / 3.0, host.getWidth() / 2.0, 0.02);

        int burns = BURNS.getOrDefault(rider.getUuid(), 0) + 1;
        if (burns >= BURNS_TO_CAST_OUT) {
            castOut(world, host, rider);
            return;
        }
        BURNS.put(rider.getUuid(), burns);
        LAST_BURN.put(rider.getUuid(), world.getTime());
        rider.sendMessage(Text.literal("The holy water burns! ").formatted(Formatting.RED)
                .append(Text.literal("■".repeat(burns)).formatted(Formatting.GOLD))
                .append(Text.literal("■".repeat(BURNS_TO_CAST_OUT - burns)).formatted(Formatting.DARK_GRAY)), true);
        Haunting.shake(rider, 0.9F, 15);
    }

    private static void castOut(ServerWorld world, LivingEntity host, ServerPlayerEntity rider) {
        BURNS.remove(rider.getUuid());
        LAST_BURN.remove(rider.getUuid());
        PossessionComponent.get(rider).stopPossessing();

        world.playSound(null, host.getX(), host.getY(), host.getZ(), SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.HOSTILE, 1.5F, 0.6F);
        world.playSound(null, host.getX(), host.getY(), host.getZ(), SoundEvents.ENTITY_WITHER_DEATH, SoundCategory.HOSTILE, 0.6F, 1.4F);
        world.spawnParticles(ParticleTypes.SOUL, host.getX(), host.getBodyY(0.5), host.getZ(), 60, 0.6, 1.0, 0.6, 0.08);
        world.spawnParticles(ParticleTypes.END_ROD, host.getX(), host.getBodyY(0.5), host.getZ(), 40, 0.5, 1.0, 0.5, 0.15);
        host.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 200, 1));
        rider.sendMessage(Text.literal("You have been cast out!").formatted(Formatting.DARK_RED, Formatting.BOLD), false);
        Haunting.shake(rider, 2.0F, 40);
    }

    public static void tick(ServerWorld world) {
        long now = world.getTime();
        if (now % 20 == 0) {
            LAST_BURN.entrySet().removeIf(entry -> {
                if (now - entry.getValue() < BURN_FADE_TICKS) {
                    return false;
                }
                int left = BURNS.getOrDefault(entry.getKey(), 0) - 1;
                if (left <= 0) {
                    BURNS.remove(entry.getKey());
                    return true;
                }
                BURNS.put(entry.getKey(), left);
                entry.setValue(now);
                return false;
            });
        }
        if (now % 10 != 0) {
            return;
        }
        for (ServerPlayerEntity rider : world.getPlayers()) {
            MobEntity host = PossessionComponent.getHost(rider);
            if (host == null) {
                continue;
            }
            for (VillagerEntity exorcist : world.getEntitiesByClass(VillagerEntity.class, host.getBoundingBox().expand(EXORCIST_RANGE),
                    v -> v != host && v.isAlive() && !v.isBaby() && !v.isSleeping() && !PossessedGrin.isPossessed(v)
                            && v.getVillagerData().getProfession() == VillagerProfession.CLERIC)) {
                if (now < EXORCIST_READY.getOrDefault(exorcist.getUuid(), 0L)) {
                    continue;
                }
                double distance = exorcist.distanceTo(host);
                if (distance < 2.5 || distance > EXORCIST_RANGE || !exorcist.canSee(host)) {
                    continue;
                }
                throwHolyWater(world, exorcist, host);
                EXORCIST_READY.put(exorcist.getUuid(), now + 50 + world.getRandom().nextInt(30));
            }
        }
        if (now % 1200 == 0) {
            EXORCIST_READY.values().removeIf(ready -> ready < now - 1200);
        }
    }

    /** Thrown the same way a witch throws her potions, aimed slightly ahead of a moving target. */
    private static void throwHolyWater(ServerWorld world, VillagerEntity exorcist, LivingEntity target) {
        exorcist.getLookControl().lookAt(target, 30.0F, 30.0F);
        Vec3d motion = target.getVelocity();
        double dx = target.getX() + motion.x - exorcist.getX();
        double dy = target.getEyeY() - 1.1 - exorcist.getY();
        double dz = target.getZ() + motion.z - exorcist.getZ();
        double flat = Math.sqrt(dx * dx + dz * dz);

        PotionEntity holyWater = new PotionEntity(world, exorcist);
        holyWater.setItem(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.WATER));
        holyWater.setPitch(holyWater.getPitch() + 20.0F);
        holyWater.setVelocity(dx, dy + flat * 0.2, dz, 0.75F, 8.0F);
        world.spawnEntity(holyWater);

        world.playSound(null, exorcist.getX(), exorcist.getY(), exorcist.getZ(), SoundEvents.ENTITY_WITCH_THROW, SoundCategory.NEUTRAL, 1.0F, 0.8F);
        world.playSound(null, exorcist.getX(), exorcist.getY(), exorcist.getZ(), SoundEvents.ENTITY_VILLAGER_WORK_CLERIC, SoundCategory.NEUTRAL, 1.0F, 0.7F);
        world.spawnParticles(ParticleTypes.END_ROD, exorcist.getX(), exorcist.getBodyY(0.8), exorcist.getZ(), 5, 0.3, 0.3, 0.3, 0.02);
    }

    /** Lets other code know if someone is close to being cast out (used for nothing else yet). */
    public static int burnsOn(PlayerEntity rider) {
        return BURNS.getOrDefault(rider.getUuid(), 0);
    }
}
