## Java VideoLAN RPC

### Table of contents:
* [Setup](#setup)
* [Showcase](#showcase)
* [Commands](#commands-and-preferences)
* [larp](#why-was-this-even-made)
* [Credits](#credits)

## Setup

Download the jar from the [Releases](https://github.com/Napoleon-x/VideoLAN-VLC-Discord-RPC-in-Java/releases/tag/1.6) and place in its own designated folder.
Java is required for the next step so make sure you have installed it if not [Click here!](https://www.azul.com/downloads/?package=jdk#zulu)

After making sure the necessary requirements are met, navigate to the folder with the jar file within it.

Open a terminal in the folder and execute this command `java -jar VLCDiscord-RPC-v1.6.jar` (adjust naming if needed)

First time runs will place an extension in `\AppData\Roaming\vlc` for which you will need to activate each time when running the program (VLC limitations)

If you get a np_metadata_full not found, that is completely normal it means its your first start and you have not ran the extension yet in VLC, just go under view and press the extension name to activate it and then play a song. You should now be able to start the program correctly.

## Commands and Preferences

Here is a list of available commands and preferences for customizing your Rich Presence:

- `--help` : Displays the list of available commands.
- `--keepTime <true/false>` : Keeps the elapsed time in the Rich Presence even when a different song is playing. The time won't be reset when a new song starts.
- `--customCPS <message here>` : Replaces the "Currently playing: (song_name)" from the Rich Presence. Available variables: (playing_status), (song_name), (artist), (metadata_name).
- `--details <message here>` : Adds an extra field in your Rich Presence with a custom message. You can use variables like: (playing_status), (song_name), (artist), (metadata_name).
- `--enableArtwork <true/false>` : Enables or disables the display of thumbnail artwork associated with your file.
- `--disableWarnings <true/false>` : Toggles whether to disable warnings from being printed.
- `--debug <true/false>` : Enables or disables printing of debug messages.
- `--showExtension <true/false>` : Shows or hides the file extension in the name of the media file on the Rich Presence.
- `--hideCPS <true/false>` : Removes or includes the "Currently playing: " prefix from the Rich Presence.
- `--npFullMetadataFile <file-path-to-np_metadata_full.txt>` : Use this if np_metadata_full.txt is not in the default directory on your operating system.
- `--skipPlugincheck <true/false>` : Skips the checking of whether the extension exists or not.
- `--checkActiveVLC <true/false>` : Shuts down the Rich Presence when VLC is deactivated.

Feel free to use these commands to customize your Rich Presence according to your preferences and needs.   

## Showcase

Arguments come after the jar name in the command `java -jar VLCDiscord-RPC-v1.6.jar`
1. `--hideCPS true  --customCPS (metadata_name)  --details (artist)`
2. `--hideCPS true`

| 1. --customCPS & --details | No arguments | 2. --hideCPS |
| --- | --- | --- |
| ![Image](https://github.com/Napoleon-x/VideoLAN-VLC-Discord-RPC-in-Java/assets/36709736/b8e92a6d-1c3d-4e08-bf2a-81778364df8f) | ![noAgrs](https://github.com/Napoleon-x/VideoLAN-VLC-Discord-RPC-in-Java/assets/36709736/4517914d-f2d6-4137-821d-43ff8a1bf731) | ![hideCPS](https://github.com/Napoleon-x/VideoLAN-VLC-Discord-RPC-in-Java/assets/36709736/91805c9e-3979-4d1d-be5a-a96347393c42) |

| Idle/Not playing |
| --- |
| ![idle](https://github.com/Napoleon-x/VideoLAN-VLC-Discord-RPC-in-Java/assets/36709736/b39b46a7-3608-425f-a839-ae4e223b9f8d) |

## Why was this even made?

This project was born out of sheer curiosity and boredom. After installing VLC, I realised I hadn't made a java project in a while so I sought out to create a Discord Rich Presence for it as a casual experiment. Despite the initial challenges, including a lack of comprehensive documentation, I inevitably wasted over three hours. Eventually, I finally made something that seemed at the very least good enough to release.

Is this solution flawless? Not by a long shot. It may even seem somewhat impractical. However, if you're seeking a simple and user-friendly option, this might suit your needs. Feel free to tinker and have fun playing your ~totally legit~ mp3 files or whatever – just remember, selling it is off the table. So, go ahead, and flex your mundane music tastes to your discord friends.

### Credits 

Huge thanks to the developers of these projects [Now Playing in texts by un-pogaz](https://github.com/un-pogaz/Now-Playing-in-texts) and [Discord IPC](https://github.com/jagrosh/DiscordIPC)
