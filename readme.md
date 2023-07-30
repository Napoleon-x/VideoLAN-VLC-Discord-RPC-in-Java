## Java VideoLAN RPC

### Table of contents:
* [Setup](#setup)
* [Commands](#commands-and-preferances)

## Setup

Download the jar from the [releases](https://github.com/Napoleon-x/VideoLAN-VLC-Discord-RPC-in-Java/releases/tag/1.0) and place in its own designated folder.
Java is required for the next step so make sure you have installed it if not [Click here!](https://www.azul.com/downloads/?package=jdk#zulu)

After making sure the necessary requirements are met, navigate to the folder with the jar file in it and open a terminal in the folder and execute this command `java -jar VLCDiscord-RPC-v1.jar --keepTime true` (adjust naming if needed)
First time runs will place an extension in `\AppData\Roaming\vlc` for which you will need to activate each time when running the program (VLC limitations)

## Commands and preferances

### `--help: Displays the list of available commands.`
### `--keepTime <true/false>: Enables or disables keeping the time in the elapsed section of your Rich Presence even when a different song is playing. The time won't be reset when a new song starts.`
### `--details <message here>: Adds an extra field in your Rich Presence with a custom message.`
### `--disableWarnings <true/false>: Toggles whether to disable warnings from being printed.`
### `--debug <true/false>: Enables or disables printing of debug messages.`
### `--showExtension <true/false>: Shows or hides the file extension in the name of the media file on the Rich Presence.`
### `--hideCPS <true/false>: Removes or includes the "Currently playing: " prefix from the Rich Presence.`
### `--npFullMetadataFile <file-path-to-\np_metadata_full.txt> : If you are running an operating system other than Windows or another case scenario in which \AppData\Roaming\vlc\np_metadata_full.txt does not exist in usual directory when running extension on VLC`
### `--skipPlugincheck <true/false> : Skips the checking of whether the extension exists or not`
### `--ignoreActiveVLC <true/false> : Ignores the check for whether VLC is running or not`

## Why was this even made?

I got bored and recently had installed VLC and wanted to see if I could make a Discord RPC for it just as a casual thing. After the first 3 hours of researching and the lack of proper documentation I still managed to continue and thought I should release this. Is it perfect? NO this seems even rather impracticle but if you want something simple and easy to use this is ok and you can do what you want with it (except sell it) and have fun playing your ~totally legit~ mp3 files or whatever.

### Credits 

Huge thanks to the developers of these projects [Now Playing in texts by un-pogaz](https://github.com/un-pogaz/Now-Playing-in-texts) and [Discord IPC](https://github.com/jagrosh/DiscordIPC)
