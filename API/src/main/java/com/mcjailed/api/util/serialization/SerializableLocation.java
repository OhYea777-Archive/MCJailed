package com.mcjailed.api.util.serialization;

import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import javax.persistence.Id;

public class SerializableLocation {

    @Id
    private String id;

    @NotEmpty
    private String world;

    @NotNull
    private double x, y, z;

    @NotNull
    private float yaw, pitch;

    public SerializableLocation(Location location) {
        this.id = createId(location);
        this.world = location.getWorld().getName();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }

    /**
     * Returns the id for this SerializableLocation.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id for this SerializableLocation.
     *
     * @param id The id to be set.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the world for this SerializableLocation.
     */
    public String getWorld() {
        return world;
    }

    /**
     * Sets the world for this SerializableLocation.
     *
     * @param world The world to be set.
     */
    public void setWorld(String world) {
        this.world = world;
    }

    /**
     * Returns the x-coordinate for this SerializableLocation.
     */
    public double getX() {
        return x;
    }

    /**
     * Sets the x-coordinate for this SerializableLocation.
     *
     * @param x The x-coordinate to be set.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Returns the y-coordinate for this SerializableLocation.
     */
    public double getY() {
        return y;
    }

    /**
     * Sets the y-coordinate for this SerializableLocation.
     *
     * @param y The y-coordinate to be set.
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Returns the z-coordinate for this SerializableLocation.
     */
    public double getZ() {
        return z;
    }

    /**
     * Sets the z-coordinate for this SerializableLocation.
     *
     * @param z The z-coordinate to be set.
     */
    public void setZ(double z) {
        this.z = z;
    }

    /**
     * Returns the yaw for this SerializableLocation.
     */
    public float getYaw() {
        return yaw;
    }

    /**
     * Sets the yaw for this SerializableLocation.
     *
     * @param yaw The yaw to be set.
     */
    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    /**
     * Returns the pitch for this SerializableLocation.
     */
    public float getPitch() {
        return pitch;
    }

    /**
     * Sets the pitch for this SerializableLocation.
     *
     * @param pitch The yaw to be set.
     */
    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    /**
     * Returns a deSerialized SerializableLocation as its original {@link org.bukkit.Location}.
     */
    public Location getLocation() {
        World world = Bukkit.getWorld(this.world);

        return new Location(world, x, y, z, yaw, pitch);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof SerializableLocation) return ((SerializableLocation) object).getId().equals(id);
        else if (object instanceof Location) {
            Location location = (Location) object;

            return world.equals(location.getWorld().getName()) && (int) x == location.getBlockX() && (int) y == location.getBlockY() && (int) z == location.getBlockZ();
        }

        return super.equals(object);
    }

    /**
     * Returns a generated id for a given {@link org.bukkit.Location}.
     *
     * @param location The location to create the id from.
     */
    public static String createId(Location location) {
        return location.getWorld().getName() + ":" + location.getBlockX() + ":" + location.getBlockY() + ":" + location.getBlockZ();
    }

}
