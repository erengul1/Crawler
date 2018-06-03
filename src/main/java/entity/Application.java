package entity;

public class Application {


    Interaction interactionObject;
    Language LanguageObject;
    private String hash;
    private String created_at;
    private String hashType;
    private String delivered_at;
    private float interactionId;
    private float subscriptionId;

    public Interaction getInteractionObject() {
        return interactionObject;
    }

    public void setInteractionObject(Interaction interactionObject) {
        this.interactionObject = interactionObject;
    }

    public Language getLanguageObject() {
        return LanguageObject;
    }

    public void setLanguageObject(Language languageObject) {
        LanguageObject = languageObject;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getHashType() {
        return hashType;
    }

    public void setHashType(String hashType) {
        this.hashType = hashType;
    }

    public String getDelivered_at() {
        return delivered_at;
    }

    public void setDelivered_at(String delivered_at) {
        this.delivered_at = delivered_at;
    }

    public float getInteractionId() {
        return interactionId;
    }

    public void setInteractionId(float interactionId) {
        this.interactionId = interactionId;
    }

    public float getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(float subscriptionId) {
        this.subscriptionId = subscriptionId;
    }
}
