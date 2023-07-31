# TinyInv 

<p align="center"><img src="https://i.postimg.cc/yNRq8w6w/caelum.png"></p>

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![CurseForge](https://cf.way2muchnoise.eu/556708.svg)](https://www.curseforge.com/minecraft/mc-mods/caelum)
[![CurseForge](https://cf.way2muchnoise.eu/versions/For%20MC_556708_all.svg)](https://www.curseforge.com/minecraft/mc-mods/caelum)

[![Discord](https://img.shields.io/discord/765992108602687558.svg?style=for-the-badge)](https://discord.gg/sJQWn8p)


Caelum is a Minecraft mod, that aims to make the Minecraft sky somewhat more realistic. This is done by:
* replacing the Vanilla stars with real-life stars
* adding colors to the stars
* making the Moon move independently of the Sun, making it so lunar phases are actually possible
* making what stars are visible change over the course of the Minecraft year
* rotating the sky based on your latitude - the closer you are to one of the "poles", the lower the Sun is

The mod allows you to:
* choose whether to use the Vanilla or custom stars (or whether to render stars at all)
* add or even replace the custom stars using resource packs
* enable/disable star color (does not apply to Vanilla stars)
* change star size (does not apply to Vanilla stars)
* change the "length of the year" (different stars are visible over the course of the year). Alternatively, you can disable this feature
* change the length of the lunar month
* change whether the player's latitude (z coordinate) affects the entire sky, stars only, or nothing
* change what z coordinates are considered the south and north pole

Currently, the 1.20.1 version of the mod is intended to be used only on the client side. In the future, there might be added additional features, that will work only if the mod is on the server side too.
Because of this, even though in polar regions you can see the Sun at night, the game will still consider it to be dark (so mobs will spawn, daylight sensors will not detect the sun, etc.)

The movement of the celestial bodies is an approximation and does not fully reflect the way it works in real-life (both Moon and Earth have perfectly circular orbits, Earth has no axial tilt, etc.)

There are no guarantees made when it comes to compatibility with other mods (especially such that affect sky rendering in any way - such as Optifine).
