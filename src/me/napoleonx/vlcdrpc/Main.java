package me.napoleonx.vlcdrpc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.RichPresence;

  /**
   * A simple Java based Discord RPC made for the VideoLAN Media Player using the Discord IPC library by jagrosh<br>
   * @author Napoleon-x
   */
public class Main {
	public static OffsetDateTime time = OffsetDateTime.now();
	public static String programName = "VLCDiscordRPC", version = "v1.6";
	public static String song = "";
	public static String playingStateImage = "";
	public static String playingState = "";
	public static String songNameState = "";
	public static String npFullMetadataFile = "C:\\Users\\"+System.getProperty("user.name")+"\\AppData\\Roaming\\vlc\\np_metadata_full.txt";
	public static StringBuilder	detailText = null;
	public static StringBuilder	playingSongText = null;
	public static String songName;
	public static String artworkPath;
	public static String artist;
	public static String metadataName;
    public static String os = System.getProperty("os.name").toLowerCase();
	public static boolean pluginCheck = false;
	public static boolean ignoreActive = false;
	public static boolean hideCPS = false;
	public static boolean showExtension = false;
	public static boolean disableWarnings = false;
	public static boolean artwork = true;
	public static boolean availableMetdata;
	public static boolean debug = false;
	public static boolean keepTime = false; // Time will be set to program open time instead of time on current song

	public static void main(String[] args) {
		for(int i = 0; i < args.length; i++) {
			switch (args[i]) {
			case "--keepTime":
				if(args[i+1].contains("true")) {
					keepTime=true;
				} else if(args[i+1].contains("false")) {
					keepTime=false;
				}
				break;
				
			case "--debug":
				if(args[i+1].contains("true")) {
	                System.out.println("[VLC Discord RPC]: Debug has been enabled!");
					debug=true;
				}
				break;	
				
			case "--disableWarnings":
				if(args[i+1].contains("true")) {
					disableWarnings=true;
				}
				break;		
				
			case "--hideCPS":
				if(args[i+1].contains("true")) {
					hideCPS=true;
				} else if(args[i+1].contains("false")) {
					hideCPS=false;
				}
				break;
				
			case "--showExtension":
				if(args[i+1].contains("true")) {
					showExtension=true;
				} else if(args[i+1].contains("false")) {
					showExtension=false;
				}
				break;			
				
			case "--npFullMetadataFile":
				npFullMetadataFile = args[i+1];
				break;		
				
			case "--skipPlugincheck":
				if(args[i+1].contains("true")) {
					pluginCheck=true;
				} else if(args[i+1].contains("false")) {
					pluginCheck=false;
				}
				break;	
			
			case "--checkActiveVLC":
				if(args[i+1].contains("true")) {
					ignoreActive=false;
				} else if(args[i+1].contains("false")) {
					ignoreActive=true;
				}
				break;	
				
			case "--enableArtwork":
				if(args[i+1].contains("true")) {
					artwork=true;
				} else if(args[i+1].contains("false")) {
					artwork=false;
				}
				break;	
				
			case "--help":
			System.out.println("Commands: "
					+ "\n--help : Shows commands list\n--keepTime <true/false> : Time will be kept in elapsed section of your RPC even when on a different song and wont be reset when new song is playing.\n"
					+ "--customCPS <message here> : Replace the \"Currently playing: (song_name)\" from the RPC. Available variables (playing_status) (song_name) (artist) (metadata_name)\n"
					+ "--details <message here> : Puts an extra field in your rpc with a custom message. You can use variables like: (playing_status) (song_name) (artist) (metadata_name)\n"
					+ "--enableArtwork <true/false> : Flex the metadata thumbnail artwork associated to your file\n"
					+ "--disableWarnings <true/false> : Disables warnings from being printed\n"
					+ "--debug <true/false> : Enables debug messages being printed\n"
					+ "--showExtension <true/false> : Shows the file extension in its name on the RPC\n"
					+ "--hideCPS <true/false> : Removes the \"Currently playing: \" from the RPC\n"
					+ "--npFullMetadataFile <file-path-to-\np_metadata_full.txt> : If you are running an operating system other than Windows or another case scenario in which \\AppData\\Roaming\\vlc\\np_metadata_full.txt does not exist in usual directory when running extension on VLC\n"
					+ "--skipPlugincheck <true/false> : Skips the checking of whether the extension exists or not\n"
					+ "--checkActiveVLC <true/false> : Make the the RPC shut down when VLC is deactivated\n\n\n\n\n");
				System.exit(14);
			break;
			
			case "--details":
				detailText = new StringBuilder();
				for(int d = i+1; d < args.length; d++) { // int d = i+1; because you don't want the initial "--details" to be a part of the rpc text.
					if(args[d].contains("--")) break; // Stop it from including any commands 
					detailText.append(args[d]+" ");
				}
			break;
			
			case "--customCPS":
				playingSongText = new StringBuilder();
				for(int d = i+1; d < args.length; d++) { 
					if(args[d].contains("--")) break;
					playingSongText.append(args[d]+" ");
				}
			break;

			default:
				break;
			}
		}
		if(pluginCheck&&os.contains("win")) {
			try {
				// Step 1: Get the destination path
				String username = System.getProperty("user.name");
				String destinationPath = "C:\\Users\\"+username+"\\AppData\\Roaming\\vlc\\lua\\extensions\\Now.Playing.in.Texts.lua";
				if (!Paths.get(destinationPath).toFile().exists()) {
					System.out.println("[VLC Discord RPC]: VLC Plugin is being placed. Make sure to restart VLC to initiate properly. Remember to run the plugin from interface drop down and to make sure there is no song running or if there is just reload the input and if there is a crash just keep restarting vlc and trying to run the plugin until it works.");
					if(!disableWarnings) System.out.println("[VLC Discord RPC WARNING]: Using a VLC theme that is poorly designed or has certain references named something else may actually cause activating the extension to crash you, so base vlc is recommended. Continue with caution.");
					// Step 2: Open the InputStream to read the source file
					InputStream stream = Main.class.getClassLoader().getResourceAsStream("script/Now.Playing.in.Texts.lua");
					if (stream != null) {
						BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
					
						// Step 3: Read the lines and store them in a StringBuilder
						StringBuilder contentBuilder = new StringBuilder();
						String line;
						while ((line = reader.readLine()) != null) {
							contentBuilder.append(line);
							contentBuilder.append(System.lineSeparator()); // Add the line separator to preserve line breaks
						}
					
						// Step 4: Write the contents to the destination file
						try (BufferedWriter writer = new BufferedWriter(new FileWriter(destinationPath))) {
							writer.write(contentBuilder.toString());
						} catch (IOException e) {
							e.printStackTrace();
						} finally {
							reader.close();
						}
					} else {
						System.out.println("Failed to find the source file.");
					}
					System.out.println("[VLC Discord RPC]: VLC Plugin placed. Restart VLC and press view then click on Now.Playing.in.Texts extension name, play a song and execute this jar once more to complete.");
				} else {
					if(!disableWarnings) System.out.println("[VLC Discord RPC]: VLC Plugin already placed. Everything should work if not find the latest version of the helper vlc script https://addons.videolan.org/p/1172613 and make sure to press Interface and activate the plugin in VLC.");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(!os.contains("win")) {
			if(!disableWarnings) System.out.println("Your system is not Windows which means we could not autoplace the extension. Please open this jar up and copy the extension to your VLC extensions path manually and then rerun this program with --npFullMetadataFile <file path>");
		}
		// The actual Discord RPC stuff
		IPCClient client = new IPCClient(1133091172005454005L); // The application ID of the app with the vlc icons and stuff we are gonna use for the rich presence 
		RichPresence.Builder builder = new RichPresence.Builder();

        IPCListener listener = new IPCListener() {
            @Override
            public void onReady(IPCClient client) {
                System.out.println("[VLC Discord RPC]: Setting the RPC");
                if(!disableWarnings) System.out.println("[VLC Discord RPC WARNING]: Remember to press view and then plugin name to activate and then open a file.");
                builder.setStartTimestamp(time)
                .setSmallImage(playingStateImage, song)
                .setLargeImage("vlc_large", "Currently using VLC");
                client.sendRichPresence(builder.build());
                System.out.println("[VLC Discord RPC]: RPC has been set up.");
            }

            @Override
            public void onClose(IPCClient client, JSONObject json) {
                System.out.println("[VLC Discord RPC]: Connection closed");
            }
        };

        client.setListener(listener);

        try {
            client.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Schedule the task to update the presence every 300 milliseconds
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(() -> updatePresence(builder, client), 300, 300, TimeUnit.MILLISECONDS);
		// ScheduledExecutorService is actually pretty cool 
        
		Thread shutdownHook = new Thread(() -> {
			System.out.println("[VLC Discord RPC]: Shutting down...");
			client.close();
		});
		Runtime.getRuntime().addShutdownHook(shutdownHook);
	}
	
    private static void updatePresence(RichPresence.Builder builder, IPCClient client) {
    	if(!isVLCRunning()&&ignoreActive) {
    		System.out.println("[VLC Discord RPC ERROR]: VLC not detected as running within the tasks list. Please start VLC to continue running or run with --ignoreActiveVLC true");
    		System.exit(88);
    	}
        try {
        	if(!Paths.get(npFullMetadataFile).toFile().exists()) {
        		System.out.println("[VLC Discord RPC]: np_metadata_full.txt not found at \""+npFullMetadataFile+"\" Perhaps you have not ran the extension in VLC. Look under view and press the extension name and then rerun this program. If problems persist rerun using --npFullMetadataFile <path>");
        		System.exit(0);
        	}
        	InputStream stream = new FileInputStream(npFullMetadataFile);
        	if (stream != null) {
        		BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
        		
        		// Step 3: Read the lines and store them in a StringBuilder
        		StringBuilder contentBuilder = new StringBuilder();
        		String line;
        		while ((line = reader.readLine()) != null) {
        			contentBuilder.append(line);
        			contentBuilder.append(System.lineSeparator()); // Add the line separator to preserve line breaks
        		}
        		if(contentBuilder.toString().contains("NOT_PLAYING")) {
        			if(song=="Currently idle") {
                		reader.close();
        				return; // No need to update the RPC if we are already idle
        			}
        			if(!keepTime) time = OffsetDateTime.now();
        			song="Currently idle";
        			songNameState = song;
        		} else {
        			//String duration = extractValue(contentBuilder.toString(), "{duration}");
        			String playingStatus = extractValue(contentBuilder.toString(), "{playing_state}");
        		    String pausedTime = extractValue(contentBuilder.toString(), "{time_at_recent_pause}");
        			String artworkFilePath = extractValue(contentBuilder.toString(), "{artwork}");
        			String artistName = extractValue(contentBuilder.toString(), "{artist}");
        			String filenameTag = "{filename}";
        			String currentlyPlayingPrefix = "Currently playing: ";
        			if(hideCPS) currentlyPlayingPrefix="";
        			if(!showExtension) filenameTag = "{filename1}";
        			songName = extractValue(contentBuilder.toString(), filenameTag);
        			String newSong=currentlyPlayingPrefix+songName;
        			songNameState = newSong;
        			if(!(playingStatus==null)&&playingStatus.contains("paused")) {
            			songNameState = newSong +" - "+pausedTime;
        			} 
        			        			
        	        String cleanedPath = artworkFilePath.replaceAll("[^\\x20-\\x7E]", "");
        	        
        	        cleanedPath = cleanedPath.replaceAll("\\s+", " ");
        	        cleanedPath = cleanedPath.replaceAll(" +/", "/");
        	        cleanedPath = cleanedPath.replaceAll("/ +", "/");
        	        cleanedPath = FileSystems.getDefault().getPath(cleanedPath).normalize().toString();

        			artworkPath=cleanedPath;
        			artist=artistName.replace("\r\n\t", "");
        			metadataName=extractValue(contentBuilder.toString(), "{name}").replace("\r\n\t", "");
        			//System.out.println("Name: " + metadataName);
        			if(playingState.contains(playingStatus)&&song.contains(newSong)&&!(songName=="null")) {
        				//if(debug) System.out.println("no need to update anything");
        				reader.close();
        				return; // No reason to update
        			}
        			if(!song.contains(newSong)) {
    		        	if(!keepTime) {
    		        		time = OffsetDateTime.now(); // Reset with each new song except when keepTime is on.
    		        	}
        			}
        			if(debug) System.out.println("passed");
        			playingState = playingStatus;
        			song=newSong;
        		}
        		reader.close();
        	}
        } catch(Exception e) {	
        	e.printStackTrace();
        }
        if(playingState.contains("stopped")||playingState.contains("paused")) {
        	playingStateImage="vlc_pause"; // vlc_pause vlc_paused
        } else if(song.contains("Currently idle")) {
        	playingStateImage="vlc_idle_gray"; // vlc_idle_badge vlc_idle_gray
        } else {
        	playingStateImage="vlc_play"; // vlc_play vlc_playing 
        }
        if(!artwork||song.contains("Currently idle")||!Paths.get(artworkPath).toAbsolutePath().toString().contains(".") /* Checks for existing won't work as if its a whitespace it will think its the projects current directory and say true so this is the next best thing. */) {
        	artworkPath="vlc_large"; // Default VLC RPC logo if file does not have metadata for artwork or is just idle
        	availableMetdata=false;
        	
        } else {
        	artworkPath=getAlbumArtURL(artworkPath, song);
        	availableMetdata=true;
        }
        builder.setDetails(songNameState);
        if(!(playingSongText==null)&&availableMetdata)  builder.setDetails(playingSongText.toString().replace("(playing_status)", playingState).replace("(song_name)", songName).replace("(artist)", artist).replace("(metadata_name)", metadataName).replace("(song)", songNameState));
        if(!(detailText==null)&&availableMetdata) {
        	builder.setState(detailText.toString().replace("(playing_status)", playingState).replace("(song_name)", songName).replace("(artist)", artist).replace("(metadata_name)", metadataName).replace("(song)", songNameState)); /* Appears under the details in RPC. I have no idea on what to put in it, perhaps some program info? I will just let the user customise it ig */
        } else if(!(detailText==null)&&!availableMetdata) {
        	builder.setState(""); // Clean the details section so that if a song that has no metadata plays, it won't keep the artist info etc of the previous song.
        } 
        builder.setStartTimestamp(time)
        .setSmallImage(playingStateImage, song)
        .setLargeImage(artworkPath, "Currently using VLC"); // You can actually set URL links as the image key. Might integrate some sort of API for some shitty image upload site.
        client.sendRichPresence(builder.build());
    }
    
    public static String getAlbumArtURL(String imagePath, String songName) {
        Path savedImages = Paths.get("album.config");

        JSONObject existingData = new JSONObject();

        if (savedImages.toFile().exists()) {
            try (BufferedReader fileReader = new BufferedReader(new FileReader(savedImages.toFile()))) {
                StringBuilder fileContent = new StringBuilder();
                String line;
                while ((line = fileReader.readLine()) != null) {
                    fileContent.append(line);
                }
                existingData = new JSONObject(fileContent.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        // Remove the "Currently playing: " prefix from the song name
        String songKey = songName.replace("Currently playing: ", "").replace("\r\n\t", "");

        if (existingData.has("Album") && existingData.getJSONObject("Album").has(songKey)) {
            JSONObject albumInfo = existingData.getJSONObject("Album").getJSONObject(songKey);
            if(debug) System.out.println("Image URL for the artwork of \""+songKey+"\": "+albumInfo.getString("URL"));
            return albumInfo.getString("URL");
        } else {
            String apiKey = "e6d89588c1300895de959c6f0b085a19";

            try {
				Process processUp = Runtime.getRuntime().exec("curl --location --request POST \"https://api.imgbb.com/1/upload?expiration=600&key=" + apiKey + "\" --form \"image=@" + imagePath + "\"");
                processUp.waitFor();
                String lineUp;
                StringBuilder output = new StringBuilder();
                try (BufferedReader readerUp = new BufferedReader(new InputStreamReader(processUp.getInputStream()))) {
                    while ((lineUp = readerUp.readLine()) != null) {
                        output.append(lineUp);
                    }
                }
                if(debug) System.out.println(output.toString());
                JSONObject json = new JSONObject(output.toString());
                JSONObject data = json.getJSONObject("data");
                String imageUrl = data.getString("display_url");

                JSONObject albumInfo = new JSONObject();
                albumInfo.put("Path", imagePath);
                albumInfo.put("Artist", artist);
                albumInfo.put("URL", imageUrl);

                if (!existingData.has("Album")) {
                    existingData.put("Album", new JSONObject());
                }

                existingData.getJSONObject("Album").put(songKey, albumInfo);

                try (FileWriter fileWriter = new FileWriter(savedImages.toFile())) {
                    fileWriter.write(existingData.toString(4)); // Indentation of 4 spaces
                    System.out.println("Album data for \""+songKey+"\" saved to " + savedImages.toFile());
                    return imageUrl;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }
    }
    
    private static String extractValue(String input, String tag) {
        int tagIndex = input.indexOf(tag);
        
        if (tagIndex != -1) {
            int nextTagIndex = input.indexOf("{", tagIndex + tag.length());
            
            if (nextTagIndex == -1) {
            	String value = input.substring(tagIndex + tag.length()).trim();
                return value.substring(1); // Substring 1 because otherwise the " :" would also be included at the start, I don't want that.
            } else {
            	String value = input.substring(tagIndex + tag.length(), nextTagIndex).trim();
                return value.substring(1);
            }
        }
        return null; // Tag not found
    }
    
    public static boolean isVLCRunning() {
        Process process = null;
        ProcessBuilder processBuilder = null;
        try {
            if (os.contains("win")) {
                processBuilder = new ProcessBuilder(System.getenv("windir") + "\\system32\\tasklist.exe");
            } else if (os.contains("mac") || os.contains("nix") || os.contains("nux") || os.contains("aix")) {
                processBuilder = new ProcessBuilder("ps", "-e");
            } else {
                System.err.println("Unsupported operating system: " + os);
                return false;
            }
            process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("vlc")) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return false;
    }
}
