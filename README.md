# Powered Signs

[Modrinth](https://modrinth.com/mod/powered-signs)

This is a simple mod that adds one thing; when signs are powered, they broadcast their contents to nearby players.\
When the block below a sign is powered, it will print its contents to any player's chat who is within 32 blocks.\
The sign is then unable to print for the next second, or 20 ticks. If the redstone power is constant, it will continually print to nearby players.\
The signs check for Weak and Strong redstone power, so be careful where you put them!

## Quick Tutorial:
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
- The file `./config/poweredsignsplayers.json` is how the mod saves what players have used `/togglesigns` so their settings remain after server restarts.

## Contributing
Feel free to fork and make PRs if you want a feature added, or make an issue if you want to submit a bug/fix/suggestion etc.
