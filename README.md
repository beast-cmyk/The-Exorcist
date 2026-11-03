# Requiem: Possessed Grin

An addon for [Requiem](https://modrinth.com/mod/requiem) on **Minecraft 1.19.2 (Quilt)** that works alongside
**Requiem: Pandemonium**.

- **Possess the bosses** — the Warden, the Wither and the Ender Dragon (plus any modded boss in Requiem's boss list),
  and fight with their real attacks.
- **Every possessed mob looks possessed** — glowing white eyes and a malicious grin, on every mob.
- **Even passive mobs can attack** — villagers, cows, pigs, horses... anything you possess can punch.
- **Demonic powers** inspired by *The Exorcist* (film and novel): levitation, telekinesis, telepathy, the head
  turn, green bile, poltergeist fury, an icy presence, a demon's voice, unholy strength and the spider-walk.
- **The possessed look the part**: pale, veined, cracked skin; they float flat on their backs, scuttle face-up,
  and crawl walls with their heads bent back.
- **The haunting follows you**: knocking in the walls, lights flickering and dying, furniture rattling, your
  screen shaking.
- **Exorcism**: holy water burns the possessed, cleric villagers hunt you with it, and enough of it casts you out.
- **Ouija board**: hold a séance to start a haunting, possess any creature, and recharge powers twice as fast.

## Requirements
| | Version |
|---|---|
| Minecraft | 1.19.2 |
| Loader | Quilt Loader + Quilt Standard Libraries / Quilted Fabric API (what Requiem already needs) |
| Requiem | 2.0.0-beta.16 (the 1.19.2 release) |
| Pandemonium | optional, same Requiem release |

## Demonic powers
Every possessed mob has these. Keys can be changed in **Options → Controls → Possessed Powers**.

| Key | Power | What it does |
|---|---|---|
| **R** | Telekinesis | Lift whatever you look at (up to 24 blocks): a mob, a player, an item, or a block torn out of the ground. It floats in front of you. Press **R** again to hurl it; anything thrown into a wall gets slammed. |
| **G** | Poltergeist fury | Every door, trapdoor and fence gate within 8 blocks bangs open or slams shut, and a shockwave throws everything away from you. |
| **V** | Telepathy | Every creature within 64 blocks glows through walls for 10 seconds, and you read the mind of whoever you're looking at (or the nearest player): hearts, hunger, what they hold, where they stand, where they last died. Creatures give up their thoughts: who they're hunting, who they fear, who they're loyal to, what they're doing. A player you read hears a voice in their head that knows things it shouldn't. |
| **H** | Head turn | The head twists all the way around to face behind it with a sickening crack, stares, then twists back. Everything within 10 blocks is struck with terror: slowed and weakened for 5 s, mobs drop their fight and run, villagers panic. |
| **B** | Green bile | A gush of green muck where you look (8 blocks). Anything hit gets nausea, a little poison and some damage. |

Always on:
- **Levitation:** every possessed mob can fly. Double-tap jump, like creative mode.
- **Spider-walk:** walk into a wall to crawl up it.
- **Unholy strength:** melee hits do +50% damage and knock targets back much harder.
- **Demon's voice:** every growl and cry the mob makes comes out deep and wrong.
- **Cold presence:** frost drifts off you. Creatures nearby shiver with frost on them and slow down, players see
  their breath and frost on their screen (it never freezes anyone to death), and still water around you ices over
  like Frost Walker ice, melting back later.
- **Possessed skin:** the body turns pale and sickly, crossed with dark veins and cracks. It's generated from each
  mob's own texture, so it fits every mob, modded ones included.
- **Floating flat:** hover in place while flying and the body tips over to float on its back, bobbing gently.
  Start moving and it rights itself.
- **Spider-walk:** crouch on the ground (humanoids) and the body flips face-up, arms and legs bent under it, and
  scuttles head-first. On walls, the head bends all the way back to stare up.
- **Room reacts:** every power shakes your screen, wobbles armor stands and spins item frames nearby. Poltergeist
  fury can knock paintings off the walls.

Cooldowns: telekinesis ½ s, poltergeist 3 s, head turn 3 s, bile 4 s, telepathy 5 s.

Powers are inspired by the film and the novel. No dialogue is copied (every whisper is original), and the
disturbing scenes that aren't game material are left out. Lifting blocks follows the `mobGriefing` gamerule, and never takes unbreakable
blocks or chests and other containers.

## The haunting
While you possess something, it follows you everywhere:
- **Knocking** from inside the walls every so often: soft raps in threes, or hard bangs.
- **Dying lights:** torches and lanterns near you keep flickering (they briefly burn blue and dim), and now and
  then one goes out. Torches drop as items; candles and campfires can be relit.

## Exorcism
- **Holy water:** throw a splash bottle of plain water at a possessed creature and it burns (3 damage, hissing
  smoke). Six burns before the count fades and the demon is cast out, which forces you out of the body.
  The burn count shows on screen, and one burn fades every 10 seconds.
- **Exorcists:** cleric villagers know what you are. If one can see you within 14 blocks, it throws holy water at
  you every few seconds, even while the other villagers run.

## Ouija board
Craft it from 4 planks, 1 ink sac and 1 bone:

```
Plank   Ink sac  Plank
Plank   Bone     Plank
```

Right-click it to hold a one-minute séance:
- the haunting starts around you, whispers included, even if you aren't possessing anything;
- **any creature at all will accept you** while it lasts;
- your powers recharge **twice as fast**.

It can be used again when the séance ends.

## Possessing bosses
Bosses are possessed like any other mob: as a soul, walk up to one and use Requiem's possess action.
No soul vessel needed.

| Boss | Left click a mob | Left click air | Right click (mob or air) |
|---|---|---|---|
| **Warden** | Melee smash (its real 30 damage) | — | **Sonic boom**: charges ~1.7 s with the real animation, then fires where you look. 20 blocks, goes through walls, hits everything on the line. 4 s cooldown. |
| **Wither** | Black wither skull | Blue wither skull | — |
| **Ender Dragon** | Bite (10 damage) | — | **Dragon's breath** fireball where you look (lingering breath cloud, bottle it if you like). 2 s cooldown. |

- **Warden:** you're spared the Darkness it pulses out; everyone else around you still gets it.
- **Wither:** flies, and the skull attacks are Requiem's own, which this addon simply unlocks.
- **Ender Dragon:**
  - You fly it and it faces where you look, with its wings flapping.
  - Its head bites and its wings fling anything you fly into, but never you.
  - It smashes through blocks like the real dragon, if `mobGriefing` is on.
  - To possess it, get close to its **body** (not the tail or wing tips), ideally while it's perched on the exit portal.
  - It is enormous, so it needs open sky.

## The possessed face
- **Humanoids** have hand-made faces that sit exactly on their own eyes and mouths: all zombies and skeletons,
  villagers, wandering traders, witches, illagers, vexes, piglins, endermen, and Requiem's cured mobs.
- **Every other mob** gets a face found automatically on the front of its head. That includes cows, creepers, spiders,
  the Wither (all three heads), the Warden, the Ender Dragon, and modded mobs.
- The eyes glow in the dark.

## Passive mobs attacking
Any possessed mob without an attack of its own now punches for a tenth of its max health (min 1, max 10).
That's 2 for a villager, 1 for a chicken, and a bite of 10 for the Ender Dragon. Held weapons add on top, and
Requiem's attack cooldown still applies.

How it works: those mobs get an attack stat with a base of **0**, and the punch damage is added only for the moment
you swing. A base of 0 is exactly what Requiem's soul capture already assumes for them, so capturing their souls
is no harder than before, and their normal AI is unchanged.

## With Pandemonium
Pandemonium's config has a **"possess all mobs"** option. Turn it on and you can possess any mob without capturing
its soul first. This addon makes sure that includes the bosses, and gives all of them faces and attacks.
Nothing in the two mods overlaps.

## Building the jar

### Option A: no installs (GitHub)
1. Create a new GitHub repository and upload everything in this folder (keep the `.github` folder).
2. Open the **Actions** tab. The "Build mod jar" workflow runs on upload (or press **Run workflow**).
3. Download the **possessed-grin** artifact; inside is `possessed-grin-4.0.0.jar`.

### Option B: on your computer
Install a JDK 21 and [Gradle 8.14](https://gradle.org/install/), then run `gradle build` in this folder.
The jar is in `build/libs/`.

## Installing
Put the jar in your `mods` folder next to Requiem (and Pandemonium if you use it).
In singleplayer that's it. On a server, put it on the server **and** every player's game.

## Customizing (datapacks / resource packs, no code)
| What | Where |
|---|---|
| Which bosses are possessable | entity tag `possessedgrin:possessable_bosses` |
| Mobs that should *not* get the punch | entity tag `possessedgrin:never_punch` |
| A hand-made face for any mob | `assets/possessedgrin/textures/entity/possessed/<namespace>/<mob>_face.png` and `_eyes.png`, same layout as the mob's texture |
| The face designs themselves | edit the ASCII grids in `tools/gen_textures.py`, then `python3 tools/gen_textures.py src/main/resources/assets/possessedgrin` |

## How it works (for the curious)
- **Bosses:** Requiem blocks bosses with one possession rule called `requiem:blacklist`. Requiem stores its
  rules by name, so the addon swaps in a copy of that rule that lets the bosses through, then adds a rule allowing them.
- **Dragon targeting:** the dragon is made of separate hitbox parts. Aiming at a part redirects Requiem to the dragon itself.
- **Dragon flying:** the dragon flies on an autopilot ("phases") that Requiem doesn't switch off. While it's possessed,
  the autopilot is held in its hover phase with its steering cut, so you fly it.
- **Boss attacks:** Requiem abilities registered through the `requiem:plugin` entrypoint.
- **Demonic powers:** the keys send a tiny packet to the server, which checks you're possessing something and
  runs the power, so it all works in multiplayer. Levitation and the spider-walk hook Requiem's movement rules.
  Fabric API (keys, networking, ticking) is only compiled against; in game, Quilted Fabric API provides it.
- **Faces:**
  - An extra render layer on every mob renderer, plus a hook on the dragon renderer.
  - Hand-made faces re-draw the mob's model with an overlay texture.
  - Other mobs get a small decal on the front of their head part, which the addon finds by name in the model.
- **`src/main/java/ladysnake`:** compile-time copies of the few Requiem API types used. They are stripped from the jar, so
  Requiem's real classes are used in game, and building doesn't need Requiem's Maven repository.

## Status
Every Minecraft and Requiem name used here was checked against the real 1.19.2 mappings and Requiem beta.16
source, every sound and particle against the real 1.19.2 game data, and the code compiles cleanly against them. It has **not been play-tested yet**.

The Ender Dragon is the most experimental part. Its vanilla code was never meant to be player-driven, so expect
some jank. If something misbehaves, the game log (`logs/latest.log`) will say which part.
