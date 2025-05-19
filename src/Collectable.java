/**
 * Defines behavior for game items that can be picked up and equipped by the player.
 *
 * Equipable items can be collected once, after which they typically
 * modify the player's capabilities (e.g., hammer, blaster).
 */
public interface Equipable {
    /** Mark this item as equipped/collected. */
    void collect();

    /** @return true if this item has already been collected. */
    boolean isCollected();
}