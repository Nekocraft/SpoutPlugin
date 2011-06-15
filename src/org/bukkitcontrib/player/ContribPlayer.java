package org.bukkitcontrib.player;

import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkitcontrib.event.input.RenderDistance;
import org.bukkitcontrib.keyboard.Keyboard;

public interface ContribPlayer extends org.bukkit.entity.Player{
    
    /**
     * Closes any dialog windows the client may have open at the time
     * @return true if a window was closed
     */
    public boolean closeActiveWindow();
    
    /**
     * Opens an inventory dialog to the player, with the given inventory displayed in the upper pane, and the player's inventory in the lower pane
     * @param inventory to use in the dialog GUI
     * @return true if an inventory window was opened
     */
    public boolean openInventoryWindow(Inventory inventory);
    
    /**
     * Opens an inventory dialog to the player, with the given inventory displayed in the upper pane, and the player's inventory in the lower pane.
     * The location is not used, but is passed to other plugins when notifying them of the open window
     * @param inventory to use in the dialog GUI
     * @param location that represents this inventory in the world (e.g Chest, Furnace). Use null if there is no physical location.
     * @return true if an inventory window was opened
     */
    public boolean openInventoryWindow(Inventory inventory, Location location);
    
    /**
     * Opens an inventory dialog to the player, with the given inventory displayed in the upper pane, and the player's inventory in the lower pane.
     * The location is not used, but is passed to other plugins when notifying them of the open window
     * @param inventory to use in the dialog GUI
     * @param location that represents this inventory in the world (e.g Chest, Furnace). Use null if there is no physical location.
     * @param ignoreDistance whether the distance from the inventory should be considered (opening an inventory will fail if it's too far away, without ignoring distance)
     * @return true if an inventory window was opened
     */
    public boolean openInventoryWindow(Inventory inventory, Location location, boolean ignoreDistance);
    
    /**
     * Opens an workbench dialog to the player, using the workbench at the given location
     * @param location of the workbench to use. Must be a valid workbench.
     * @return true if a workbench window was opened
     */
    public boolean openWorkbenchWindow(Location location);

    /**
     * Return's true if the player is using the bukkit contrib single player mod
     * @return bukkit contrib single player mod
     */
    public boolean isEnabledBukkitContribSinglePlayerMod();
    
    /**
     * Gets the version of the bukkitcontrib SP client mod in use, or -1 if none.
     * @return version
     */
    public int getVersion();
    
    /**
     * Return's the key bound to forward movement for this player, or unknown if not known.
     * @return forward key
     */
    public Keyboard getForwardKey();
    
    /**
     * Return's the key bound to backward movement for this player, or unknown if not known.
     * @return backward key
     */
    public Keyboard getBackwardKey();
    
    /**
     * Return's the key bound to left movement for this player, or unknown if not known.
     * @return left key
     */
    public Keyboard getLeftKey();
    
    /**
     * Return's the key bound to right movement for this player, or unknown if not known.
     * @return right key
     */
    public Keyboard getRightKey();
    
    /**
     * Return's the key bound to jumping for this player, or unknown if not known.
     * @return jump key
     */
    public Keyboard getJumpKey();
    
    /**
     * Return's the key bound to opening and closing inventories for this player, or unknown if not known.
     * @return inventory key
     */
    public Keyboard getInventoryKey();
    
    /**
     * Return's the key bound to forward movement for this player, or unknown if not known.
     * @return forward key
     */
    public Keyboard getDropItemKey();
    
    /**
     * Return's the key bound to opening the chat bar for this player, or unknown if not known.
     * @return chat key
     */
    public Keyboard getChatKey();
    
    /**
     * Return's the key bound to toggle view fog for this player, or unknown if not known.
     * @return toggle fog key
     */
    public Keyboard getToggleFogKey();
    
    /**
     * Return's the key bound to sneaking for this player, or unknown if not known.
     * @return sneak key
     */
    public Keyboard getSneakKey();
    
    /**
     * Gets the render distance that the player views, or null if unknown
     * @return render distance
     */
    public RenderDistance getRenderDistance();
    
    /**
     * Sets the render distance that the player views
     * @param distance to set
     */
    public void setRenderDistance(RenderDistance distance);
    
    /**
     * Gets the maximum render distance that the player can view, or null if unknown
     * @return maximum distance
     */
    public RenderDistance getMaximumRenderDistance();
    
    /**
     * Sets the maximum render distance that the player can view
     * @param maximum distance
     */
    public void setMaximumRenderDistance(RenderDistance maximum);
    
    /**
     * Gets the minimum render distance that the player can view, or null if unknown
     * @return minimum distance
     */
    public RenderDistance getMinimumRenderDistance();
    
    /**
     * Sets the minimum render distance that the player can view
     * @param minimum distance
     */
    public void setMinimumRenderDistance(RenderDistance minimum);

}
