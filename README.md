# Powered Signs

<a href="https://modrinth.com/mod/powered-signs">![Modrinth Downloads](https://img.shields.io/modrinth/dt/powered-signs?logo=modrinth)</a>
*(todo: hangar and curseforge)*

A simple mod/plugin combo that adds one thing; when signs are powered, they broadcast their contents to the chat of nearby players.\

This applies to all sign types; standing signs are powered from the block its standing on,
wall signs are powered by the block they are attached to,
ceiling changing signs are powered by the block above you (or if on a chain, the top of that chain),
and wall-mounted hanging signs are powered by either block horizontally.

When the block below a sign is powered, it will print its contents to any player's chat who is within 48 blocks,
the same distance a note block can be heard. They act the same way as note blocks with powering as well,
as the signs change their block state in the same manner.

## Quick Tutorial:
TODO: very outdated

- Place a sign, and write on the front side, then back
- If you power the block below the sign, or the block the sign is attached to in the case of hanging and wall mounted signs, the contents of the sign will be printed
- The order of what is printed is front side top to bottom, back side top to bottom
- There are a few config options located at `./config/poweredsign.toml` as well;
  - `playerDistance`: Is how many blocks away, in a square shape, the sign can print to player's chat (Default: 32),
  - `coolDownTicks`: Is the amount of ticks that a sign as to wait before printing again (Default: 20, or 1 second),
  - `logSignPositions`: An option for server moderators and as a light debugging tool; prints the location of signs in the world when they are powered (Default: false),
  - `particles`: Redstone particles are spawned when the sign is powered (Default: true; Client Side Only),
  - `audio`: Powered signs make the lever clicking sound when powered (Default: true),
  - `strongPowerOnly`: The sign prints only if the block is strongly powered (Default: true),
  - `legacyPoweringSystem`: A toggle for the old powering system; the block under the sign is how you power it (Default: false)
- The file `./config/poweredsignsplayers.json` is how the mod saves what players have used `/togglesigns` so their settings remain after server restarts. Beware, this is Username based, not UUID based.

## Roadmap
Generally; implementation first, then QOL.

- Platforms/Versions
  -[ ] Paper
  -[ ] Fabric
  -[ ] Neoforge
  -[ ] 26.3
  -[ ] 27.1
  -[ ] 1.21.1 Backport
  -[ ] 1.20.1 Backport
  -[ ] Backport to a handful of other LTS-Modding versions
- QOL
  -[ ] Formatting is mirrored
  -[ ] General Configs
    -[ ] Print Distance
    -[ ] Waxed Signs configs:
      - Only ones printed (eventual default)
      - Only ones *not* printed
      - No Difference
    -[ ] Chat Prefix (GH-2)
  -[ ] Alternate sign powering
    -[ ] Chained Hanging Signs being powered from the top of the chain
    -[ ] Wall-mounted Hanging Signs in a row being powered together
    - No alternate powering methods for Standing or Wall Signs are currently planned. Open to suggestions

## Contributing and Licensing
While the original intent of the project was to be a simple patch for a minecraft server I ran years ago,
the current intent is to be a personal experiment and an example project for modern platform-agnostic mods/plugins to build off of.
This project will be, now and forever, dedicated to the public domain via the [CC0 License](https://creativecommons.org/publicdomain/zero/1.0/).\
See [DEVELOP.md](./DEVELOP.md) and the READMEs in each subproject's directory for some simple docs. Go wild :)\

As for the actual content of the mod, feel free to fork and contribute PRs if you want a feature added,
or make an issue if you want to submit a bug/fix/suggestion etc.\
AI usage in PRs will **not be tolerated**; with the single exception of translating if there is a language barrier.
