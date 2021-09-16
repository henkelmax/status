# Status

A [Fabric](https://fabricmc.net/) mod that enables you to set a status.

This mod must be installed on the server. Vanilla clients are still able to join a server with this mod. If you want to
set a status, you need this mod installed on your client.

It requires [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api)

---

## Features

By pressing the `U` key, you will be able to set your status.

The status consists of multiple parts:

- The availability
    - If you don't want to get disturbed
    - If you are open for collaboration
- The base status
    - If you are recording
    - If you are streaming
- Additional stuff
    - If you don't want others to skip the night

![](https://i.imgur.com/Iqaz3jw.png)

These icons will show up next to the players head in the tab list.

![](https://i.imgur.com/SbtCkXD.png)

If no sleep is activated, players lying in bed will see a message that someone doesn't want the night to be skipped.
This message will also be shown to players that don't have the mod installed.

![](https://i.imgur.com/2NoywQA.png)

When joining a server the first time in your playing session, you will get a clickable message that will remind you to
set your status. The status is not persisted between playing sessions by default.

![](https://i.imgur.com/8dglntP.png)

## Configuration

**Client Configuration**
`.minecraft/config/status/status-client.properties`

Key | Default Value | Description
--- | --- | ---
`persist_state` | `false` | If the status should be persisted between playing sessions.
`show_join_message` | `true` | If the reminder when joining the server should be shown.

**Server Configuration**
`<your-server-directory>/config/status/status-server.properties`

Key | Default Value | Description
--- | --- | ---
`no_sleep_title`|`No Sleep` | The title of the no sleep popup
`no_sleep_player_subtitle` | `%s does not want you to sleep` | The subtitle of the no sleep popup if one player does not want to skip the night
`no_sleep_multiple_subtitle` | `Some players do not want you to sleep` | The subtitle of the no sleep popup if multiple players do not want to skip the night