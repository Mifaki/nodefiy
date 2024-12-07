package ap.mobile.notedifywithfirebase.database;

public class Note {
    private String id; // Untuk menyimpan ID dari Firebase
    private String title;
    private String content;
    private long timestamp;
    private String category;
    private boolean isPlaceholder;

    public Note() {
        // Constructor kosong diperlukan oleh Firebase
    }

    public Note(String title, String content, String category, boolean isPlaceholder) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.timestamp = System.currentTimeMillis();
        this.isPlaceholder = isPlaceholder;
    }

    // Getters dan setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public boolean getIsPlaceholder() { return isPlaceholder; }
    public void setIsPlaceholder(boolean isPlaceholder) { this.isPlaceholder = isPlaceholder; }
}