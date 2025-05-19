/**
 * Anything the player can pick up and equip.
 */
public interface Equippable {
    /** Mark this item as equipped/collected. */
    void collect();

    /** @return true if this item has already been collected. */
    boolean isCollected();
}