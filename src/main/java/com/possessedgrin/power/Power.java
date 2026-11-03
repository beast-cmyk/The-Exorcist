package com.possessedgrin.power;

/** The key-activated powers. Default keys can be changed in Options > Controls > Possessed Powers. */
public enum Power {
    TELEKINESIS("telekinesis", 10, 82 /* R */, 0.4F, 10),
    POLTERGEIST("poltergeist", 60, 71 /* G */, 1.6F, 30),
    TELEPATHY("telepathy", 100, 86 /* V */, 0.3F, 12),
    HEAD_TURN("head_turn", 60, 72 /* H */, 0.9F, 25),
    BILE("bile", 80, 66 /* B */, 0.6F, 12);

    public final String id;
    /** Ticks before the power can be used again (20 ticks = 1 second). */
    public final int cooldown;
    /** GLFW key code (kept as a plain number so this class is safe to load on servers). */
    public final int defaultKey;
    /** How hard your screen shakes when you use it, and for how many ticks. */
    public final float shake;
    public final int shakeTicks;

    Power(String id, int cooldown, int defaultKey, float shake, int shakeTicks) {
        this.id = id;
        this.cooldown = cooldown;
        this.defaultKey = defaultKey;
        this.shake = shake;
        this.shakeTicks = shakeTicks;
    }
}
