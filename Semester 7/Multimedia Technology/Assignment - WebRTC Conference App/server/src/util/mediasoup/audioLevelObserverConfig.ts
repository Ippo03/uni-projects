export const audioLevelObserverConfig = {
    maxEntries: 10, // Maximum number of entries in the “volumes” event. We limit this to 10.
    threshold: -90, // Minimum average volume (in dBvo from -127 to 0) for entries in the “volumes” event.
    interval: 1000, // Interval in milliseconds for checking the audio level.
    muteThreshold: -80, // dBvo threshold for muting audio
}